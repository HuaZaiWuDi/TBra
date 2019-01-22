package com.wesmartclothing.tbra.tools;

import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import java.util.ArrayList;
import java.util.List;

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
    private int maxCount = 0, endIndex = 0;
    private List<AddTempDataBean> tempDataBeans = new ArrayList<>();

    public AddTempData() {
    }


    /**
     * 412a0005 0000 0000 0000000000
     * * 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a 796a
     */
    public void addAllTempData() {
        BleAPI.getTempCount(new RxSubscriber<Integer>() {
            @Override
            protected void _onNext(Integer integer) {
                maxCount = integer;

                getTempData();

            }
        });
    }

    private void getTempData() {
        BleAPI.getTempData(0, maxCount, new RxSubscriber<AddTempDataBean>() {
            @Override
            protected void _onNext(AddTempDataBean addTempDataBean) {
                tempDataBeans.add(addTempDataBean);
                if (addTempDataBean.getIndex() == maxCount - 1) {
                    RxLogUtils.d("获取结束");
                    uploadTempData();
                    saveCache(tempDataBeans);
                }
            }
        });
    }

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


    public void uploadCacheData() {
        RxCache.getDefault().<List<AddTempDataBean>>load(SPKey.SP_SAVE_TEMP_DATA, new TypeToken<List<AddTempDataBean>>() {
        }.getType())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new CacheResult.MapFunc<>())
                .subscribe(new RxNetSubscriber<List<AddTempDataBean>>() {
                    @Override
                    protected void _onNext(List<AddTempDataBean> addTempDataBeans) {
                        tempDataBeans = addTempDataBeans;
                        uploadTempData();
                    }
                });
    }


    private void uploadTempData() {
        List<AddTempDataBean> temp = null;
        if (endIndex >= tempDataBeans.size()) {
            RxLogUtils.d("上传成功");
            saveCache(null);
            return;
        }
        endIndex += 200;
        if (tempDataBeans.size() - endIndex >= 0) {//总数量大于200
            temp = tempDataBeans.subList(endIndex - 200, endIndex);
        } else if (tempDataBeans.size() - endIndex < 0) {//总数量少于200
            temp = tempDataBeans.subList(endIndex - 200, tempDataBeans.size());
        }

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().addSingleData(temp)
        )
                .subscribe(new RxNetSubscriber<Integer>() {
                    @Override
                    protected void _onNext(Integer integer) {
                        uploadTempData();
                    }
                });
    }

}
