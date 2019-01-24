package com.wesmartclothing.tbra.entity;

import com.clj.fastble.data.BleDevice;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName DeviceConnectBean
 * @Date 2019/1/23 10:34
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class DeviceConnectBean {

    private BleDevice mBleDevice;
    private Boolean connect;


    public DeviceConnectBean(BleDevice bleDevice) {
        mBleDevice = bleDevice;
        connect = false;
    }

    public BleDevice getBleDevice() {
        return mBleDevice;
    }

    public void setBleDevice(BleDevice bleDevice) {
        mBleDevice = bleDevice;
    }

    public boolean isConnect() {
        return connect;
    }

    public void setConnect(boolean connect) {
        this.connect = connect;
    }
}
