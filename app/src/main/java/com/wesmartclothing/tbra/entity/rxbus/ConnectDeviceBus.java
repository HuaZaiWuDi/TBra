package com.wesmartclothing.tbra.entity.rxbus;

import com.clj.fastble.data.BleDevice;

/**
 * @Package com.wesmartclothing.tbra.entity.rxbus
 * @FileName ConnectDeviceBus
 * @Date 2019/1/14 17:22
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class ConnectDeviceBus {

    public BleDevice mBleDevice;

    public ConnectDeviceBus(BleDevice mBleDevice) {
        this.mBleDevice = mBleDevice;
    }
}
