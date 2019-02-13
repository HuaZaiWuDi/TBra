package com.wesmartclothing.tbra.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleScanAndConnectCallback;
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

public class BleService extends Service {

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

        RxBus.getInstance().register2(ConnectStateBus.class)
                .subscribe(new RxSubscriber<ConnectStateBus>() {
                    @Override
                    protected void _onNext(ConnectStateBus connectStateBus) {
                        //断开连接重连
                        if (!connectStateBus.isConnect()) {
                            scanConnectDevice();
                        }
                    }
                });
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
        scanConnectDevice();
        return super.onStartCommand(intent, flags, startId);
    }

    private void scanConnectDevice() {

        if (BleTools.getInstance().isConnected()) {
            RxLogUtils.e("设备已连接");
            return;
        }

        final BleScanRuleConfig bleConfig = new BleScanRuleConfig.Builder()
//                .setServiceUuids(new UUID[]{UUID.fromString(BLEKey.UUID_Servie)})
//                .setDeviceName(true, BLEKey.DEVICE_NAME)
                .setDeviceMac(SPUtils.getString(SPKey.SP_BIND_DEVICE, ""))
                .setScanTimeOut(-1)
                .build();
        BleTools.getBleManager().initScanRule(bleConfig);


        BleTools.getBleManager().scanAndConnect(new BleScanAndConnectCallback() {
            @Override
            public void onScanFinished(BleDevice scanResult) {
                RxLogUtils.d("扫描结束：");
            }

            @Override
            public void onStartConnect() {
                RxLogUtils.d("开始连接：");
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                RxLogUtils.d("连接失败：");
                RxBus.getInstance().post(new ConnectStateBus(false));
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                RxLogUtils.d("连接成功：");
                BleTools.getInstance().stopScan();
                connectSuccess();
            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int status) {
                RxLogUtils.d("断开连接：");
                RxBus.getInstance().post(new ConnectStateBus(false));
            }

            @Override
            public void onScanStarted(boolean success) {
                RxLogUtils.d("开启扫描：" + success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                RxLogUtils.d("正在扫描：" + bleDevice.getMac());
            }
        });
    }

    private void connectSuccess() {
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
                                });
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
        BleTools.getInstance().disConnect();
        scanConnectDevice();
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
