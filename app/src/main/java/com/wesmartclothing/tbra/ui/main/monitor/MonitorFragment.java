package com.wesmartclothing.tbra.ui.main.monitor;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.CustomDialog;
import com.tmall.ultraviewpager.UltraViewPager;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.timer.MyTimer;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.cardview.CardView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.adapter.UltraPagerAdapter;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.entity.CarouselPictureBean;
import com.wesmartclothing.tbra.entity.DeviceBatteryInfoBean;
import com.wesmartclothing.tbra.entity.JsonDataBean;
import com.wesmartclothing.tbra.entity.WarningRuleBean;
import com.wesmartclothing.tbra.entity.rxbus.ConnectStateBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.CheckTempErrorUtil;
import com.wesmartclothing.tbra.ui.main.UsedTipDialog;
import com.wesmartclothing.tbra.ui.main.mine.ScanDeviceActivity;
import com.wesmartclothing.tbra.view.BatteryView;
import com.wesmartclothing.tbra.view.HistoryTempView;
import com.wesmartclothing.tbra.view.TimingMonitorView;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Package com.wesmartclothing.tbra.ui.main.testing
 * @FileName MonitorFragment
 * @Date 2019/1/8 11:02
 * @Author JACK
 * @Describe TODO监测界面
 * @Project tbra
 */
public class MonitorFragment extends BaseAcFragment {

    @BindView(R.id.ultraViewPager)
    UltraViewPager ultraViewPager;
    @BindView(R.id.tv_deviceName)
    TextView mTvDeviceName;
    @BindView(R.id.powerIcon)
    BatteryView mPowerIcon;
    @BindView(R.id.tv_switchDevice)
    TextView mTvSwitchDevice;
    @BindView(R.id.tv_bindDevice)
    TextView mTvBindDevice;
    @BindView(R.id.layout_device_empty)
    RelativeLayout mLayoutDeviceEmpty;
    @BindView(R.id.layout_device)
    CardView mLayoutDevice;
    @BindView(R.id.timingMonitorView)
    TimingMonitorView mTimingMonitorView;
    @BindView(R.id.layout_monitor_empty)
    RelativeLayout mLayoutMonitorEmpty;
    @BindView(R.id.historyTempView)
    HistoryTempView mHistoryTempView;
    @BindView(R.id.img_informationEmpty)
    ImageView mImgInformationEmpty;


    private WarningRuleBean mWarningRuleBean;
    private UltraPagerAdapter adapter;
    private boolean isConnected = false;


    public static MonitorFragment getInstance() {
        return new MonitorFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_monitor;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        setTGA("【MonitorFragment】");
        bleConnectState(BleTools.getInstance().isConnected());
    }

    private void bleConnectState(boolean isConnected) {
        this.isConnected = isConnected;
        if (!isConnected) {//未连接
            mPowerIcon.setVisibility(View.GONE);
            mTvSwitchDevice.setText("连接设备\t\t>>");
            mTvDeviceName.setText("");
            mTvBindDevice.setText("去连接");
            mLayoutDeviceEmpty.setVisibility(View.VISIBLE);
            mLayoutMonitorEmpty.setVisibility(View.VISIBLE);
        } else {//已连接
            mLayoutDeviceEmpty.setVisibility(View.GONE);
            mTvSwitchDevice.setText("切换设备\t\t>>");
            mPowerIcon.setVisibility(View.VISIBLE);
            mTvDeviceName.setText(BleTools.getInstance().getBleDevice().getMac());
            RxLogUtils.d("是否显示：" + isVisibled());
        }
        isStartTimer();
    }


    @Override
    public void initNetData() {
        getRuleDetail();
        getInformationPhoto();
    }

    private void getInformationPhoto() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().fetchCarouselPictureList(),
                lifecycleSubject,
                "fetchCarouselPictureList",
                new TypeToken<List<CarouselPictureBean>>() {
                }.getType(),
                CacheStrategy.firstCacheTimeout(Key.CACHE_TIME_OUT_DAY)
        )
                .subscribe(new RxNetSubscriber<List<CarouselPictureBean>>() {
                    @Override
                    protected void _onNext(List<CarouselPictureBean> carouselPictureBeans) {
                        if (!RxDataUtils.isEmpty(mImgInformationEmpty)) {
                            mImgInformationEmpty.setVisibility(View.GONE);
                        }
                        //UltraPagerAdapter 绑定子view到UltraViewPager
                        adapter = new UltraPagerAdapter(carouselPictureBeans, ultraViewPager);
                        adapter.setSelectImgListener(bean -> {

                        });
                        ultraViewPager.setAdapter(adapter);
                    }
                });
    }

    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(ConnectStateBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<ConnectStateBus>() {
                    @Override
                    protected void _onNext(ConnectStateBus connectStateBus) {
                        bleConnectState(connectStateBus.isConnect());
                    }
                });

        RxBus.getInstance().register2(DeviceBatteryInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<DeviceBatteryInfoBean>() {
                    @Override
                    protected void _onNext(DeviceBatteryInfoBean deviceBatteryInfoBean) {
                        mPowerIcon.setBatteryValue(deviceBatteryInfoBean.getBatteryValue());
                    }
                });
    }


    @Override
    protected void onVisible() {
        super.onVisible();
        isStartTimer();
    }


    @Override
    protected void onInvisible() {
        super.onInvisible();
        isStartTimer();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myTimer.stopTimer();
    }

    private void isStartTimer() {
        if (isConnected && isVisibled()) {
            myTimer.startTimer();
        } else {
            myTimer.stopTimer();
        }
        RxLogUtils.d("【MonitorFragment】开始：" + isVisibled());
    }


    private void getRuleDetail() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userRuleDetail(),
                lifecycleSubject,
                "userRuleDetail",
                WarningRuleBean.class,
                CacheStrategy.firstRemote()
        ).subscribe(new RxSubscriber<WarningRuleBean>() {
            @Override
            protected void _onNext(WarningRuleBean warningRuleBean) {
                if (warningRuleBean == null || RxDataUtils.isNullString(warningRuleBean.getPointType())) {
                    RxLogUtils.d("未设置告警规则");
                    UsedTipDialog usedTipDialog = new UsedTipDialog(mContext, lifecycleSubject);
                    usedTipDialog.setRxNetSubscriber(new RxNetSubscriber<WarningRuleBean>() {
                        @Override
                        protected void _onNext(WarningRuleBean warningRuleBean) {
                            mWarningRuleBean = warningRuleBean;
                            isStartTimer();
                        }
                    });
                    return;
                }
                mWarningRuleBean = warningRuleBean;
                isStartTimer();
            }
        });
    }

    private MyTimer myTimer = new MyTimer(0, 5000, () -> {
        BleAPI.getTimingBraInfo(new RxSubscriber<AddTempDataBean>() {
            @Override
            protected void _onNext(AddTempDataBean addTempDataBean) {
                checkTemp(addTempDataBean);
            }
        });
    });


    private void checkTemp(AddTempDataBean data) {
        if (data == null) return;
        if (mWarningRuleBean == null) {
            return;
        }
        List<Double> tempLists = new ArrayList<>();
        for (JsonDataBean bean : data.getDataList()) {
            if (CheckTempErrorUtil.isValidTemperature(bean.getNodeTemp())) {
                tempLists.add(bean.getNodeTemp());
            }
        }
        //标准温度
        double normTemp = CheckTempErrorUtil.calculationNormTemp(tempLists);
        RxLogUtils.d("标准温度：" + normTemp);
        //标准温度的区间
        double[] normTemps = {normTemp - mWarningRuleBean.getTempNum(), normTemp + mWarningRuleBean.getTempNum()};
        RxLogUtils.d("标准温度区间：" + Arrays.toString(normTemps));

        for (JsonDataBean bean : data.getDataList()) {
            double nodeTemp = bean.getNodeTemp();
            //设定异常温度数值，在0-50之间
            bean.setNodeTemp(Math.max(0, Math.min(nodeTemp, 50)));

            int flag = -1;
            if (CheckTempErrorUtil.isValidTemperature(nodeTemp)) {
                if (nodeTemp <= normTemps[1] && nodeTemp >= normTemps[0]) {
                    flag = 0;
                } else {
                    flag = 1;
                }
            }
            bean.setWarningFlag(flag);
        }

        mHistoryTempView.setTimingData(data.getDataList());
        mTimingMonitorView.updateUI(data.getDataList());
        mLayoutDeviceEmpty.setVisibility(View.GONE);
        mLayoutMonitorEmpty.setVisibility(View.GONE);
    }


    @OnClick({R.id.tv_switchDevice, R.id.tv_bindDevice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_switchDevice:
            case R.id.tv_bindDevice:
                if (!BleTools.getBleManager().isBlueEnable()) {
                    CustomDialog.build(mContext, R.layout.dialog_default, (dialog, rootView) -> {
                        rootView.findViewById(R.id.tv_complete)
                                .setOnClickListener(view1 -> {
                                    CustomDialog.unloadAllDialog();
                                    BleTools.getBleManager().enableBluetooth();
                                });
                    }).setCanCancel(true).showDialog();
                } else {
                    //去绑定或者连接
                    RxActivityUtils.skipActivitySingleTop(mContext, ScanDeviceActivity.class);
                }
                break;
        }
    }

}
