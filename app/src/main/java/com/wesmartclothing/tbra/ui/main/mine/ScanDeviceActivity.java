package com.wesmartclothing.tbra.ui.main.mine;

import android.Manifest;
import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.kongzue.dialog.v2.CustomDialog;
import com.kongzue.dialog.v2.MessageDialog;
import com.kongzue.dialog.v2.TipDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.constant.BLEKey;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.BindDeviceBean;
import com.wesmartclothing.tbra.entity.DeviceConnectBean;
import com.wesmartclothing.tbra.entity.rxbus.ConnectStateBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.service.LocationIntentService;
import com.wesmartclothing.tbra.tools.BLEUtil;
import com.wesmartclothing.tbra.ui.main.MainActivity;

import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanDeviceActivity extends BaseActivity {

    private static final String TAG = "【ScanDeviceActivity】";
    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.deviceRecyclerView)
    RecyclerView mDeviceRecyclerView;
    @BindView(R.id.tv_btn)
    TextView mTvBtn;
    @BindView(R.id.img_ble_scan)
    ImageView mImgBleScan;
    @BindView(R.id.layout_ble_scan)
    FrameLayout mLayoutBleScan;

    private BaseQuickAdapter adapter;
    private BindDeviceBean deviceBean = new BindDeviceBean();
    private int position = 0;
    private WaitDialog waitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_scan_device;
    }

    @Override
    public void initBundle(Bundle bundle) {
        if (bundle.getBoolean(Key.BUNDLE_CAN_SKIP)) {
            mRxTitle.setRightTextVisibility(true);
            mRxTitle.setLeftIconVisibility(false);
            mRxTitle.setRightTextOnClickListener(view -> {
                RxActivityUtils.skipActivity(mContext, MainActivity.class);
            });
        }
    }

    @Override
    public void initViews() {
        mRxTitle.setRightTextVisibility(false);
        startService(new Intent(this, LocationIntentService.class));
        initTitle(mRxTitle);
        initRecyclerView();
        initPermissions();

    }

    private void initPermissions() {
        //判断是否有权限
        new RxPermissions(mActivity)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .compose(RxComposeUtils.<Permission>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Permission>() {
                    @Override
                    protected void _onNext(Permission aBoolean) {
                        RxLogUtils.e("是否开启了权限：" + aBoolean);
                        if (!aBoolean.granted) {
                            MessageDialog.show(mContext, "提示", "未开启定位权限无法搜索到蓝牙设备");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        mDeviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<DeviceConnectBean, BaseViewHolder>(R.layout.item_scan_deivce) {
            @Override
            protected void convert(BaseViewHolder helper, DeviceConnectBean item) {
                BleDevice bleDevice = item.getBleDevice();
                String deviceDetail = bleDevice.getName() + "\t" +
                        bleDevice.getMac().substring(12, bleDevice.getMac().length()) + "\n" +
                        "距离：" + BLEUtil.rssi2Distance(bleDevice.getRssi(), 2) + "米";

                helper.setText(R.id.tv_deviceDetails, deviceDetail)
                        .setText(R.id.tv_deviceState, item.isConnect() ? "已连接" : "连接");
            }
        };
        adapter.bindToRecyclerView(mDeviceRecyclerView);
        adapter.setEmptyView(R.layout.layout_empty_device);
        mDeviceRecyclerView.setTag(-1);
        adapter.setOnItemClickListener((adapter, view, position) -> {
                    this.position = position;
                    BleTools.getInstance().disConnect();
                    connectDevice(((DeviceConnectBean) adapter.getItem(position)).getBleDevice().getMac());
                }
        );
    }

    private void connectDevice() {
        int lastPosition = (int) mDeviceRecyclerView.getTag();
        if (lastPosition != position) {
            //之前的item
            if (lastPosition >= 0) {
                DeviceConnectBean lastItem = (DeviceConnectBean) adapter.getItem(lastPosition);
                lastItem.setConnect(!lastItem.isConnect());
                adapter.setData(lastPosition, lastItem);
            }

            //当前的item
            DeviceConnectBean currentItem = (DeviceConnectBean) adapter.getItem(position);
            currentItem.setConnect(!currentItem.isConnect());
            adapter.setData(position, currentItem);

            mDeviceRecyclerView.setTag(position);
        }
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(Address.class)
                .compose(RxComposeUtils.<Address>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<Address>() {
                    @Override
                    protected void _onNext(Address address) {
                        if (address != null) {
                            deviceBean.setProvince(address.getAdminArea());
                            deviceBean.setCity(address.getLocality());
                            deviceBean.setCountry("中国");
                        }
                    }
                });
    }

    @OnClick(R.id.tv_btn)
    public void onViewClicked() {
        startScan();
    }

    private void startScan() {
        if (!BleTools.getBleManager().isBlueEnable()) {
            CustomDialog.build(mContext, LayoutInflater.from(mContext).inflate(R.layout.dialog_default, null), rootView ->
                    rootView.findViewById(R.id.tv_complete)
                            .setOnClickListener(view1 -> {
                                CustomDialog.unloadAllDialog();
                                BleTools.getBleManager().enableBluetooth();
                            })).setCanCancel(true).showDialog();
            return;
        }


        final BleScanRuleConfig bleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(new UUID[]{UUID.fromString(BLEKey.UUID_Servie)})
//                .setDeviceName(true, BLEKey.DEVICE_NAME)
                .setScanTimeOut(15000)
                .build();
        BleTools.getBleManager().initScanRule(bleConfig);

        BleTools.getBleManager().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                RxLogUtils.d("扫描结果:" + scanResultList.size());
                if (scanResultList.isEmpty()) {
                    TipDialog.show(mContext, "未发现设备", TipDialog.TYPE_ERROR);
                }
            }

            @Override
            public void onScanStarted(boolean success) {
                RxLogUtils.d("扫描开始：" + success);
                //清空列表
                if (success) {
                    mLayoutBleScan.setVisibility(View.VISIBLE);
                    mImgBleScan.startAnimation(RxAnimationUtils.RotateAnim(15));
                    adapter.setNewData(null);
                } else {
//                    TipDialog.show(mContext, "扫描失败", TipDialog.TYPE_ERROR);
                }
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                mLayoutBleScan.setVisibility(View.GONE);
                mImgBleScan.clearAnimation();
                RxLogUtils.d("正在扫描：" + bleDevice.getMac());
                DeviceConnectBean firstItem = (DeviceConnectBean) adapter.getItem(0);
                if (firstItem != null && firstItem.getBleDevice().getRssi() < bleDevice.getRssi()) {
                    adapter.addData(0, new DeviceConnectBean(bleDevice));
                } else {
                    adapter.addData(new DeviceConnectBean(bleDevice));
                }
            }
        });
    }


    private void connectDevice(String mac) {
        BleTools.getBleManager().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                waitDialog = WaitDialog.show(RxActivityUtils.currentActivity(), "正在连接");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                WaitDialog.dismiss();
                if (mContext != null) {
                    TipDialog tipDialog = TipDialog.build(mContext, "连接失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                    tipDialog.setCanCancel(true);
                    tipDialog.showDialog();
                }
                startScan();
                RxBus.getInstance().post(new ConnectStateBus(false));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                BleTools.getInstance().stopScan();

//                RxLogUtils.d(TAG, "连接成功");
//                bindDevice(bleDevice.getMac());

                BleTools.getInstance().openNotify(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        BleTools.getInstance().setMTU(200, new BleMtuChangedCallback() {
                            @Override
                            public void onSetMTUFailure(BleException exception) {
                                waitDialog.setText("连接异常，正在重连");
                                BleTools.getInstance().disConnect();
                                connectDevice(mac);
                            }

                            @Override
                            public void onMtuChanged(int mtu) {
                                RxLogUtils.d("连接成功");
                                BleAPI.syncTime(new RxSubscriber<byte[]>() {
                                    @Override
                                    protected void _onNext(byte[] bytes) {
                                        RxLogUtils.d("同步时间成功");
                                        WaitDialog.dismiss();
                                        if (mContext != null) {
                                            TipDialog tipDialog = TipDialog.build(mContext, "连接成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                            tipDialog.setCanCancel(true);
                                            tipDialog.showDialog();
                                        }
                                        connectDevice();
                                        bindDevice(bleDevice.getMac());
                                        RxBus.getInstance().post(new ConnectStateBus(true));
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        waitDialog.setText("连接异常，正在重连");
                        BleTools.getInstance().disConnect();
                        connectDevice(mac);
                    }
                });
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                WaitDialog.dismiss();
                if (mContext != null) {
                    TipDialog tipDialog = TipDialog.build(mContext, "断开连接", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                    tipDialog.setCanCancel(true);
                    tipDialog.showDialog();
                }

                RxBus.getInstance().post(new ConnectStateBus(false));
            }
        });
    }

    private void bindDevice(String macAddress) {
        deviceBean.setMacAddr(macAddress);

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().bindDevice(deviceBean),
                lifecycleSubject
        ).subscribe(new RxNetSubscriber<String>() {
            @Override
            protected void _onNext(String s) {
                SPUtils.put(SPKey.SP_BIND_DEVICE, macAddress);
            }
        });
    }


    @Override
    protected void onDestroy() {
        BleTools.getInstance().stopScan();
//        BleTools.getBleManager().removeConnectGattCallback(BleTools.getInstance().getBleDevice());
        super.onDestroy();
    }
}
