package com.wesmartclothing.tbra.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.IBinder;

import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.kongzue.dialog.v2.TipDialog;
import com.vondear.rxtools.activity.ActivityLifecycleImpl;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxNetUtils;
import com.vondear.rxtools.utils.RxSystemBroadcastUtil;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.BleDeviceInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.ConnectStateBus;
import com.wesmartclothing.tbra.entity.rxbus.NetWorkTypeBus;
import com.wesmartclothing.tbra.entity.rxbus.SystemBleOpenBus;
import com.wesmartclothing.tbra.ui.main.mine.ScanDeviceActivity;

import java.util.List;

public class BleService extends Service {

    private int currentConnectState = BluetoothProfile.STATE_DISCONNECTED;
    private static boolean isFirstJoin = true;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                    if (state == BluetoothAdapter.STATE_OFF) {
                        BleTools.getInstance().stopScan();

                    } else if (state == BluetoothAdapter.STATE_ON) {
                        RxLogUtils.d("蓝牙开启");
                        scanConnectDevice();
                    } else if (state == BluetoothAdapter.STATE_TURNING_ON) {//正在开启蓝牙
                    }
                    RxBus.getInstance().post(new SystemBleOpenBus(state == BluetoothAdapter.STATE_ON));
                    break;
                case RxSystemBroadcastUtil.SCREEN_ON:
                    RxLogUtils.d("亮屏");
                    scanConnectDevice();
                    break;
                case RxSystemBroadcastUtil.SCREEN_OFF:
                    RxLogUtils.d("息屏");
                    BleTools.getInstance().stopScan();
                    break;
                case Intent.ACTION_DATE_CHANGED://日期的变化
                    RxLogUtils.d("日期变化");
//                    RxBus.getInstance().post(new RefreshSlimming());
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    int workType = RxNetUtils.getNetWorkType(context);
                    if (isFirstJoin) {
                        isFirstJoin = false;
                        if (workType != -1 && workType != 5) {

                        } else {
                            if (ActivityLifecycleImpl.APP_IS_FOREGROUND)
                                RxToast.normal(RxNetUtils.getNetType(workType));
                        }
                    } else {
                        if (ActivityLifecycleImpl.APP_IS_FOREGROUND)
                            RxToast.normal(RxNetUtils.getNetType(workType));
                        RxBus.getInstance().post(new NetWorkTypeBus(workType, workType != -1 && workType != 5));
                    }
                    RxLogUtils.d("网络状态：" + workType);
                    break;
            }
        }
    };

    public BleService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initBroadcast();
    }

    private void initBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_ON);
        filter.addAction(RxSystemBroadcastUtil.SCREEN_OFF);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadcastReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RxLogUtils.d("启动Service");
        scanConnectDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    private synchronized void scanConnectDevice() {

        RxLogUtils.d("开始连接");

        if (ScanDeviceActivity.SCAN_BLE_DEVICE) {
            RxLogUtils.e("正在扫描界面");
            return;
        }
//
//        if (BleTools.getBleManager().getScanSate() == BleScanState.STATE_SCANNING) {
//            RxLogUtils.e("正在扫描");
//            return;
//        }

        if (currentConnectState == BluetoothProfile.STATE_CONNECTED ||
                currentConnectState == BluetoothProfile.STATE_CONNECTING) {
            RxLogUtils.e("设备已连接或正在连接:");
            return;
        }

        try {
            final BleScanRuleConfig bleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BLEKey.UUID_Servie)})
//                .setDeviceName(true, BLEKey.DEVICE_NAME)
                    .setDeviceMac(SPUtils.getString(SPKey.SP_BIND_DEVICE, ""))
                    .setScanTimeOut(-1)
                    .build();
            BleTools.getBleManager().initScanRule(bleConfig);

            RxLogUtils.d("开始连接设备:" + SPUtils.getString(SPKey.SP_BIND_DEVICE, ""));
        } catch (Exception e) {

        }

        BleTools.getBleManager().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                RxLogUtils.d("扫描结束：");


            }

            @Override
            public void onScanStarted(boolean success) {
                RxLogUtils.d("开启扫描：" + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                RxLogUtils.d("正在扫描：" + bleDevice.getMac());

                BleTools.getInstance().stopScan();
                startConnect(bleDevice);

            }
        });

    }

    private void startConnect(BleDevice bleDevice) {
        BleTools.getBleManager().connect(bleDevice, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                RxLogUtils.d("开始连接：" + Thread.currentThread().getName());
                currentConnectState = BluetoothProfile.STATE_CONNECTING;
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                currentConnectState = BluetoothProfile.STATE_DISCONNECTED;
                RxLogUtils.d("连接失败：");
                RxBus.getInstance().post(new ConnectStateBus(false));
                //连接失败或断开连接给3000的延迟
                new Handler().postDelayed(() -> scanConnectDevice(), 3000);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                currentConnectState = BluetoothProfile.STATE_CONNECTED;
                RxLogUtils.d("连接成功：" + Thread.currentThread().getName());

                new Handler().postDelayed(() -> connectSuccess(), 300);
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                currentConnectState = BluetoothProfile.STATE_DISCONNECTED;
                RxLogUtils.d("断开连接：");
                RxBus.getInstance().post(new ConnectStateBus(false));

                TipDialog tipDialog = TipDialog.build(RxActivityUtils.currentActivity(), "断开连接", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR);
                tipDialog.setCanCancel(true);
                tipDialog.showDialog();

                new Handler().postDelayed(() -> scanConnectDevice(), 3000);
            }
        });
    }


    private synchronized void connectSuccess() {
        BleTools.getInstance().openNotify(new RxSubscriber<Boolean>() {
            @Override
            protected void _onNext(Boolean aBoolean) {
                BleTools.getInstance().setMTU(200, new BleMtuChangedCallback() {
                    @Override
                    public void onSetMTUFailure(BleException exception) {
                        errorReConnect();
                    }

                    @Override
                    public void onMtuChanged(int mtu) {
                        BleAPI.syncTime(new RxSubscriber<byte[]>() {
                            @Override
                            protected void _onNext(byte[] bytes) {
                                BleAPI.getSettingInfo(new RxSubscriber<BleDeviceInfoBean>() {
                                    @Override
                                    protected void _onNext(BleDeviceInfoBean bleDeviceInfoBean) {
                                        TipDialog tipDialog = TipDialog.build(RxActivityUtils.currentActivity(), "连接成功", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_FINISH);
                                        tipDialog.setCanCancel(true);
                                        tipDialog.showDialog();
                                        RxBus.getInstance().post(new ConnectStateBus(true));
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                        errorReConnect();
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                errorReConnect();
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                RxLogUtils.e(e.toString());
                errorReConnect();
            }
        });
    }


    private void errorReConnect() {
        BleTools.getBleManager().disconnectAllDevice();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        BleTools.getBleManager().destroy();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
