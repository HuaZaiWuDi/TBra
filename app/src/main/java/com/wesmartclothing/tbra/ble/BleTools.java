package com.wesmartclothing.tbra.ble;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;
import android.support.annotation.IntRange;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleIndicateCallback;
import com.clj.fastble.callback.BleMtuChangedCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleRssiCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.data.BleScanState;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.utils.HexUtil;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.aboutByte.ByteUtil;
import com.vondear.rxtools.utils.net.ExplainException;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.ble.listener.BleCallBack;
import com.wesmartclothing.tbra.ble.listener.BleOpenNotifyCallBack;
import com.wesmartclothing.tbra.constant.BLEKey;
import com.wesmartclothing.tbra.entity.DeviceBatteryInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.ConnectStateBus;
import com.wesmartclothing.tbra.tools.LogTools;

import java.util.List;


/**
 * 项目名称：BLEPlatformSDKDemo
 * 类描述：
 * 创建人：Jack
 * 创建时间：2018/1/24
 */
public class BleTools {
    private static BleTools bleTools;

    private BleDevice bleDevice;
    private Handler TimeOut;
    private static BleManager bleManager;
    private boolean isConnencted = false;
    private static final String TAG = "【BleTools】";

    private BleTools() {
        TimeOut = new Handler();
    }

    public static BleManager getBleManager() {
        return BleManager.getInstance();
    }

    public static synchronized BleTools getInstance() {
        if (bleTools == null) {
            bleTools = new BleTools();
        }
        return bleTools;
    }

    private BleCallBack mBleCallBack;
    private RxSubscriber subscriber;
    private byte[] bytes;
    private final int reWriteCount = 2;    //重连次数
    private int currentCount = 0;          //当前次数
    private final int timeOut = 3000;          //超时


    public static void initBLE(Application application) {
        bleManager = BleManager.getInstance();
        bleManager.init(application);

        if (!bleManager.isSupportBle()) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "设备不支持BLE");
            return;
        }
//        if (!bleManager.isBlueEnable())
//            bleManager.enableBluetooth();
//        bleManager.disableBluetooth();//关闭蓝牙
//        bleManager.enableLog(BuildConfig.DEBUG);//是否开启蓝牙日志
        bleManager.enableLog(true);//是否开启蓝牙日志
        bleManager.setMaxConnectCount(1);
        bleManager.setOperateTimeout(1000);//设置操作超时时间

        bleManager.setReConnectCount(3, 3000);
        bleManager.setConnectOverTime(15000);

    }

    public void setBleCallBack(BleCallBack bleCallBack) {
        mBleCallBack = bleCallBack;
    }

    public BleDevice getBleDevice() {
        List<BleDevice> bleDevices = getBleManager().getAllConnectedDevice();
        for (BleDevice bleDevice : bleDevices) {
            this.bleDevice = bleDevice;
        }
        return bleDevice;
    }

    public BleDevice getBleDevice(String mac) {
        List<BleDevice> bleDevices = getBleManager().getAllConnectedDevice();
        for (BleDevice bleDevice : bleDevices) {
            if (bleDevice.getMac().equalsIgnoreCase(mac)) {
                this.bleDevice = bleDevice;
            }
        }
        return bleDevice;
    }


    private Runnable reWrite = new Runnable() {
        @Override
        public void run() {
            Log.e(TAG, "重新写");
            write(bytes, subscriber);
        }
    };

    public <T> void write(final byte[] bytes, final RxSubscriber<T> subscriber) {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            if (subscriber != null)
                subscriber.onError(new Exception("蓝牙未连接"));
            RxBus.getInstance().post(new ConnectStateBus(false));
            return;
        }
        this.bytes = bytes;
        this.subscriber = subscriber;

        LogTools.saveDeviceLog("写数据:" + HexUtil.encodeHexStr(bytes));
        if (currentCount > reWriteCount) {
            Log.e(TAG, "写失败--次数：" + currentCount);
            if (subscriber != null) subscriber.onError(new ExplainException("写入失败", -2));
            currentCount = 0;
        } else {
            TimeOut.postDelayed(reWrite, timeOut);
            currentCount++;
            getBleManager().write(bleDevice, BLEKey.UUID_Servie, BLEKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {

                @Override
                public void onWriteSuccess(int current, int total, byte[] justWrite) {
//                    Log.d(TAG, "写成功" + "【current】" + current + "【total】" + total + "【justWrite】" + HexUtil.encodeHexStr(justWrite));
                }

                @Override
                public void onWriteFailure(BleException exception) {
                    Log.e(TAG, "写失败" + exception.toString());
                }
            });
        }
    }


    public void writeNo(byte[] bytes) {
        getBleManager().write(bleDevice, BLEKey.UUID_Servie, BLEKey.UUID_CHART_WRITE, bytes, new BleWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
//                Log.e(TAG, "无响应写成功:" + HexUtil.encodeHexStr(justWrite));
            }

            @Override
            public void onWriteFailure(BleException exception) {
                Log.e(TAG, "写失败");
            }
        });
    }

    public void read() {
        getBleManager().read(bleDevice, BLEKey.UUID_Servie, BLEKey.UUID_CHART_READ_NOTIFY, new BleReadCallback() {
            @Override
            public void onReadSuccess(byte[] data) {
                Log.e(TAG, "读失败:" + HexUtil.encodeHexStr(data));
            }

            @Override
            public void onReadFailure(BleException exception) {
                TimeOut.removeCallbacks(reWrite);
                Log.e(TAG, "读失败:" + exception);
                write(bytes, subscriber);
            }
        });
    }


    /**
     * 蓝牙数据通知通道
     *
     * @param notifySubscriber
     */
    public void openNotify(RxSubscriber<Boolean> notifySubscriber) {
        bleDevice = getBleDevice();
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            notifySubscriber.onError(new ExplainException("未连接", -2));
            return;
        }
        getBleManager().notify(bleDevice, BLEKey.UUID_Servie, BLEKey.UUID_CHART_READ_NOTIFY, new BleNotifyCallback() {
            @Override
            public void onNotifySuccess() {
                Log.d(TAG, "打开通知成功");
                if (notifySubscriber != null) notifySubscriber.onNext(true);
            }

            @Override
            public void onNotifyFailure(BleException exception) {
                Log.e(TAG, "打开通知失败:" + exception.toString());
                notifySubscriber.onError(new ExplainException("打开通知失败", -2));
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                Log.d(TAG, "蓝牙数据更新:" + HexUtil.encodeHexStr(data));

//                LogTools.saveDeviceLog("蓝牙数据更新:" + HexUtil.encodeHexStr(data));


                //命令数据
                if (subscriber != null && data[3] == bytes[3]) {
                    subscriber.onNext(data);
                    currentCount = 0;
                    TimeOut.removeCallbacks(reWrite);
                }

                //蓝牙历史数据同步回调
                if (mBleCallBack != null && data[3] == 0x05) {
                    mBleCallBack.onNotify(data);
                }

                //电池信息反馈
                if (data[3] == 0x04) {
                    currentCount = 0;
                    TimeOut.removeCallbacks(reWrite);

                    DeviceBatteryInfoBean batteryInfoBean = new DeviceBatteryInfoBean();
                    batteryInfoBean.setBatteryState(data[4]);
                    batteryInfoBean.setBatteryValue(data[5]);
                    batteryInfoBean.setBatteryVoltage(ByteUtil.bytesToIntD2(new byte[]{data[6], data[7]}));
                    batteryInfoBean.setBatteryTemperature(ByteUtil.bytesToIntD2(new byte[]{data[8], data[9]}));
                    RxLogUtils.d("电池温度信息：" + batteryInfoBean.toString());

                    RxBus.getInstance().post(batteryInfoBean);
                }

            }
        });
    }

    public void openIndicate(final BleOpenNotifyCallBack mBleOpenNotifyCallBack) {
        bleDevice = getBleDevice();
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        getBleManager().indicate(bleDevice, BLEKey.UUID_Servie, BLEKey.UUID_CHART_READ_NOTIFY, new BleIndicateCallback() {
            @Override
            public void onIndicateSuccess() {
                Log.e(TAG, "打开indicate成功");
                if (mBleOpenNotifyCallBack != null) mBleOpenNotifyCallBack.success();
            }

            @Override
            public void onIndicateFailure(BleException exception) {
                Log.e(TAG, "打开indicate失败:" + exception.toString());
            }

            @Override
            public void onCharacteristicChanged(byte[] data) {
                Log.d(TAG, "蓝牙数据更新:" + HexUtil.encodeHexStr(data));
                if (mBleCallBack != null)
                    mBleCallBack.onNotify(data);

                TimeOut.removeCallbacks(reWrite);
                currentCount = 0;
                Log.d(TAG, "数据正确");
                if (subscriber != null)
                    subscriber.onNext(data);
            }
        });
    }


    public void readRssi() {
        if (bleDevice == null || !bleManager.isConnected(bleDevice)) {
            Log.e(TAG, "未连接");
            return;
        }
        getBleManager().readRssi(bleDevice, new BleRssiCallback() {
            @Override
            public void onRssiFailure(BleException exception) {
                Log.e(TAG, "读取蓝牙信号失败:" + exception.toString());
            }

            @Override
            public void onRssiSuccess(int rssi) {
                Log.e(TAG, "读取蓝牙信号成功: 【" + rssi + "】");
            }
        });
    }


    public BleDevice mac2Device(String MAC) {
        BleDevice bleDevice = null;
        if (!BluetoothAdapter.checkBluetoothAddress(MAC)) {
            return bleDevice;
        }
        BluetoothAdapter bluetoothAdapter = BleManager.getInstance().getBluetoothAdapter();
        if (bluetoothAdapter != null) {
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(MAC);
            if (device != null) {
                bleDevice = BleManager.getInstance().convertBleDevice(device);
            }
        }
        return bleDevice;
    }


    public void stopScan() {
        if (getBleManager().getScanSate() == BleScanState.STATE_SCANNING) {
            Log.d("bleManager", "结束扫描");
            getBleManager().cancelScan();
        }
    }


    /**
     * 是否连接设备
     *
     * @return
     */
    public boolean isConnected() {
        if (bleDevice != null) {
            return getBleManager().isConnected(bleDevice);
        }
        return false;
    }

    public boolean connectedOrConnecting() {
        if (bleDevice != null) {
            RxLogUtils.d("连接状态：" + getBleManager().getConnectState(bleDevice));
            return getBleManager().getConnectState(bleDevice) == BluetoothProfile.STATE_CONNECTED
                    || getBleManager().getConnectState(bleDevice) == BluetoothProfile.STATE_CONNECTING;
        }
        return false;
    }


    public static boolean checkMac(String Mac) {
        return BluetoothAdapter.checkBluetoothAddress(Mac);
    }

    public void disConnect() {
        if (getBleManager() != null && bleDevice != null)
            getBleManager().disconnect(bleDevice);
    }

    public void diaConnectLastDevice(BleDevice newBleDevice) {
        getBleManager().disconnect(getLastDevice(newBleDevice));
    }

    public BleDevice getLastDevice(BleDevice newBleDevice) {
        List<BleDevice> deviceLists = getBleManager().getAllConnectedDevice();
        for (BleDevice bleDevice : deviceLists) {
            if (!bleDevice.getMac().equals(newBleDevice.getMac())) {
                return bleDevice;
            }
        }
        return null;
    }


    public boolean isBind(String Mac) {
        return BluetoothAdapter.checkBluetoothAddress(Mac);
    }


    public void setMTU(@IntRange(from = 23, to = 512) int mtu, BleMtuChangedCallback callback) {
        if (bleDevice != null) {
            getBleManager().setMtu(bleDevice, mtu, callback);
        }
    }

    public boolean isConnencted() {
        return isConnencted;
    }

    public void setConnencted(boolean connencted) {
        isConnencted = connencted;
    }
}
