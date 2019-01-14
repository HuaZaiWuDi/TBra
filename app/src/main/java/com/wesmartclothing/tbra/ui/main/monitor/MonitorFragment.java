package com.wesmartclothing.tbra.ui.main.monitor;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.dialog.v2.CustomDialog;
import com.tmall.ultraviewpager.UltraViewPager;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.adapter.UltraPagerAdapter;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.entity.rxbus.SystemBleOpenBus;
import com.wesmartclothing.tbra.ui.main.mine.ScanDeviceActivity;
import com.wesmartclothing.tbra.view.PowerIconView;
import com.wesmartclothing.tbra.view.TimingMonitorView;

import java.util.ArrayList;
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
    PowerIconView mPowerIcon;
    @BindView(R.id.tv_switchDevice)
    TextView mTvSwitchDevice;
    @BindView(R.id.tv_bindDevice)
    TextView mTvBindDevice;
    @BindView(R.id.layout_device_empty)
    LinearLayout mLayoutDeviceEmpty;
    @BindView(R.id.layout_device)
    CardView mLayoutDevice;
    @BindView(R.id.timingMonitorView)
    TimingMonitorView mTimingMonitorView;
    @BindView(R.id.layout_monitor_empty)
    LinearLayout mLayoutMonitorEmpty;


    private List<String> imgs = new ArrayList<>();


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
        initViewPage();
        bleConnectState(BleTools.getInstance().isConnected());
    }

    private void bleConnectState(boolean isConnected) {
        mLayoutDeviceEmpty.setVisibility(View.VISIBLE);
        mTvBindDevice.setText("去连接");
        if (!isConnected) {//未连接
            mPowerIcon.setVisibility(View.GONE);
            mTvSwitchDevice.setText("连接设备\t\t>>");
            mTvDeviceName.setText("设备名字：-\t-");
        } else {//已连接
            mTvSwitchDevice.setText("切换设备\t\t>>");
            mPowerIcon.setVisibility(View.VISIBLE);
            mTvDeviceName.setText("设备名字：" + BleTools.getInstance().getBleDevice().getMac());
        }
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {
        //系统蓝牙监听
        RxBus.getInstance().register2(SystemBleOpenBus.class)
                .compose(RxComposeUtils.<SystemBleOpenBus>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<SystemBleOpenBus>() {
                    @Override
                    protected void _onNext(SystemBleOpenBus systemBleOpenBus) {
//                        if (systemBleOpenBus.isOpen) {
//                            CustomDialog.unloadAllDialog();
//                            RxActivityUtils.skipActivity(mContext, ScanDeviceActivity.class);
//                        }
                    }
                });
    }


    private void initViewPage() {
        imgs.clear();
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=87077840f66f6c7607074ad9d7681e80&imgtype=0&src=http%3A%2F%2Fpic.kekenet.com%2F2017%2F0323%2F24621490236152.png");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=d7b02b4c46569e0197f5949164433c52&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F574ddb5egw1eqosahw1m6j20pa0g00w3.jpg");
        imgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=53404ec533a940868fef1c0793cc8a5d&imgtype=0&src=http%3A%2F%2Fwerkstette.dk%2Fwp-content%2Fuploads%2F2015%2F09%2FEntertainment_Weekly_Photographer_Marc_Hom_British_Actor_Charlie_Hunnam_as_King_Arthur_Retouch_Werkstette10-770x841.jpg");

        //UltraPagerAdapter 绑定子view到UltraViewPager
        UltraPagerAdapter adapter = new UltraPagerAdapter(imgs, ultraViewPager);
        adapter.setSelectImgListener(new UltraPagerAdapter.SelectImgListener() {
            @Override
            public void selectItem(String URL) {

            }
        });
        ultraViewPager.setAdapter(adapter);

    }


    @OnClick({R.id.tv_switchDevice, R.id.tv_bindDevice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_switchDevice:
                break;
            case R.id.tv_bindDevice:
                if (!BleTools.getBleManager().isBlueEnable()) {
                    CustomDialog.show(mContext, R.layout.dialog_default, new CustomDialog.BindView() {
                        @Override
                        public void onBind(View rootView) {
                            rootView.findViewById(R.id.tv_complete)
                                    .setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            CustomDialog.unloadAllDialog();
                                            BleTools.getBleManager().enableBluetooth();
                                        }
                                    });
                        }
                    }).setCanCancel(true);
                } else {
                    //去绑定或者连接
                    RxActivityUtils.skipActivity(mContext, ScanDeviceActivity.class);
                }
                break;
        }
    }
}
