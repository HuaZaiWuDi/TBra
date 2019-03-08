package com.wesmartclothing.tbra.entity.rxbus;

import android.location.Address;

/**
 * @Package com.wesmartclothing.tbra.entity.rxbus
 * @FileName LocationBus
 * @Date 2019/3/8 10:37
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class LocationBus {


    private Address mAddress;

    public LocationBus(Address address) {
        mAddress = address;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }
}
