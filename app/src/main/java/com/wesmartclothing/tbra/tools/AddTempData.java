package com.wesmartclothing.tbra.tools;

import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxFormatValue;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.ExplainException;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName AddTempData
 * @Date 2019/1/22 14:03
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class AddTempData {

    //15天数据最多4200条
    private int maxCount = 0, endIndex = 0, lastIndex = 0;
    private List<AddTempDataBean> tempDataBeans = new ArrayList<>();
    private RxSubscriber<Integer> mRxSubscriber;

    public AddTempData() {
    }


    public void setRxSubscriber(RxSubscriber<Integer> rxSubscriber) {
        mRxSubscriber = rxSubscriber;
    }

    /**
     * 412a0005 0000 0000 0000000000
     * * 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a
     */
    private void getDateByBle() {
        BleAPI.getTempCount(new RxSubscriber<Integer>() {
            @Override
            protected void _onNext(Integer integer) {
                RxLogUtils.d("硬件包数：" + integer);
                if (integer > 0) {
                    maxCount = integer;
                    if (mRxSubscriber != null) {
                        mRxSubscriber.onSubscribe(null);
                    }
                    getTempData();
                }
            }
        });
    }


    /**
     * 2.15 修改逻辑，不在按照序号判断是否结束，判断命令行开头字符标识
     * 2.17 所以每包温度数据都无效，则这条数据无效，不用上传
     */
    public void getTempData() {
        BleAPI.getTempData(SPUtils.getInt(SPKey.SP_LAST_INDEX, 0), maxCount, new RxSubscriber<AddTempDataBean>() {
            @Override
            protected void _onNext(AddTempDataBean addTempDataBean) {
                if (mRxSubscriber != null) {
                    int progress = (int) (addTempDataBean.getIndex() * 100f / (maxCount - 1));
                    mRxSubscriber.onNext(progress);
                }

//                if (tempDataBeans.size() < maxCount)
//                    tempDataBeans.add(addTempDataBean);
//
//                if (tempDataBeans.size() == maxCount) {
//                    RxLogUtils.d("获取结束");
//                    uploadTempData();
//                    saveCache(tempDataBeans);
//                    SPUtils.put(SPKey.SP_LAST_INDEX, 0);
//                    BleAPI.clearTempData();
//                }
                if (checkTempValid(addTempDataBean)) {
                    tempDataBeans.add(addTempDataBean);
                }

                if (addTempDataBean.getPickageSign() == (byte) 0xc1) {
                    RxLogUtils.d("获取结束");
                    uploadTempData();
                    saveCache(tempDataBeans);
                    SPUtils.put(SPKey.SP_LAST_INDEX, 0);
                    BleAPI.clearTempData();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

                if (!RxDataUtils.isEmpty(tempDataBeans)) {
                    RxLogUtils.d("出现异常，上传已经获取的数据");
                    uploadTempData();
                    saveCache(tempDataBeans);
                    SPUtils.put(SPKey.SP_LAST_INDEX, tempDataBeans.get(tempDataBeans.size() - 1).getIndex());
                }

                if (mRxSubscriber != null)
                    mRxSubscriber.onError(new ExplainException("获取失败", -3));
            }
        });
    }

    /**
     * 验证温度是否有效，所有点位均无效，则这条表示无效，不用上传
     *
     * @return 是否有效true有效
     */
    private boolean checkTempValid(AddTempDataBean addTempDataBean) {
        int unValidCount = 0;
        List<JsonDataBean> dataList = addTempDataBean.getDataList();
        for (JsonDataBean bean : dataList) {
            if (!CheckTempErrorUtil.isValidTemperature(bean.getNodeTemp())) {
                unValidCount++;
            }
        }
        return unValidCount != 16;
    }


    /**
     * 保存数据到本地
     *
     * @param tempDataBeans
     */
    private void saveCache(List<AddTempDataBean> tempDataBeans) {
        RxCache.getDefault().save(SPKey.SP_SAVE_TEMP_DATA, tempDataBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxLogUtils.d("保存温度数据是否成功：" + aBoolean);
                    }
                });
    }


    /**
     * 上传温度数据，有缓存的时候，先上传缓存数据，下次再请求网络数据
     */
    public void uploadCacheOrBleData() {
        RxCache.getDefault().<List<AddTempDataBean>>load(SPKey.SP_SAVE_TEMP_DATA, new TypeToken<List<AddTempDataBean>>() {
        }.getType())
                .map(new CacheResult.MapFunc<>())
                .compose(RxComposeUtils.rxThreadHelper())
                .subscribe(new RxNetSubscriber<List<AddTempDataBean>>() {
                    @Override
                    protected void _onNext(List<AddTempDataBean> addTempDataBeans) {
                        tempDataBeans = addTempDataBeans;
                        if (tempDataBeans != null && !tempDataBeans.isEmpty()) {
                            RxLogUtils.d("上传缓存数据");
                            uploadTempData();
                        } else {
                            RxLogUtils.d("蓝牙获取数据并上传");
                            getDateByBle();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        RxLogUtils.d("没有本地数据");

                        getDateByBle();
                    }
                });
    }


    private void uploadTempData() {
        List<AddTempDataBean> temp = null;
        if (endIndex >= tempDataBeans.size()) {
            RxLogUtils.d("上传成功");

            if (mRxSubscriber != null)
                mRxSubscriber.onComplete();
            tempDataBeans.clear();
            saveCache(tempDataBeans);
            tempDataBeans = null;
            return;
        }
        endIndex += 200;
        if (tempDataBeans.size() - endIndex >= 0) {//总数量大于200
            temp = tempDataBeans.subList(endIndex - 200, endIndex);
        } else if (tempDataBeans.size() - endIndex < 0) {//总数量少于200
            temp = tempDataBeans.subList(endIndex - 200, tempDataBeans.size());
            endIndex = tempDataBeans.size();
        }

        RxManager.getInstance().doNetSubscribe(
                NetManager.getBigFileService().addSingleData(temp)
        )
                .subscribe(new RxNetSubscriber<Integer>() {
                    @Override
                    protected void _onNext(Integer integer) {
                        RxLogUtils.d("结束下标：" + endIndex);
                        uploadTempData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mRxSubscriber != null)
                            mRxSubscriber.onError(new ExplainException("上传失败", -4));
                        //上传出现异常，刷新本地缓存，清空已经上传的数据源
                        saveCache(tempDataBeans.subList(Math.max(endIndex - 200, 0), tempDataBeans.size()));

                    }
                });
    }


    /**
     * 测试数据，模拟从蓝牙获取数据并上传
     */
    public void testData() {
        maxCount = 4200;

        Calendar instance = Calendar.getInstance();

        Observable.create((ObservableOnSubscribe<List<AddTempDataBean>>) emitter -> {
            for (int i = 0; i < maxCount; i++) {
                AddTempDataBean dataBean = new AddTempDataBean();
                if (i % 100 == 0) {
                    instance.set(Calendar.DATE, +(i / 100 + 1));
                }

                dataBean.setCollectTime(RxFormat.setFormatDate(instance.getTime(), RxFormat.Date_Date));
                List<JsonDataBean> dataList = new ArrayList<>();
                for (int j = 0; j < 16; j++) {
                    JsonDataBean bean = new JsonDataBean();
                    if (j < 8) {
                        bean.setNodeName("L0" + (j + 1));
                    } else {
                        bean.setNodeName("R0" + ((j - 8) + 1));
                    }
                    bean.setNodeTemp(RxFormatValue.format4S5R(Math.random() * 10 + 35, 1));
                    dataList.add(bean);
                }
                dataBean.setIndex(i);
                dataBean.setDataList(dataList);
                Thread.sleep(20);
                tempDataBeans.add(dataBean);
                if (tempDataBeans.size() == 200 || i == maxCount - 1) {
                    lastIndex = i;
                    SPUtils.put(SPKey.SP_LAST_INDEX, lastIndex);
                    List<AddTempDataBean> dataBeans = new ArrayList<>();
                    dataBeans.addAll(tempDataBeans);
                    tempDataBeans.clear();
                    emitter.onNext(dataBeans);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new RxSubscriber<List<AddTempDataBean>>() {
                    @Override
                    protected void _onNext(List<AddTempDataBean> lists) {
                        uploadOnceData(lists);
                    }
                });
    }

    private void uploadOnceData(List<AddTempDataBean> dataBeans) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getBigFileService().addSingleData(dataBeans)
        )
                .subscribe(new RxNetSubscriber<Integer>() {
                    @Override
                    protected void _onNext(Integer integer) {
                        RxLogUtils.d("结束下标：" + lastIndex);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mRxSubscriber != null)
                            mRxSubscriber.onError(new ExplainException("上传失败", -4));
                    }
                });
    }

}
