package com.wesmartclothing.tbra.entity.rxbus;

/**
 * @Package com.wesmartclothing.tbra.entity.rxbus
 * @FileName BatteryChangedBus
 * @Date 2019/2/12 18:27
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BatteryChangedBus {


    private int value;

    public BatteryChangedBus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
