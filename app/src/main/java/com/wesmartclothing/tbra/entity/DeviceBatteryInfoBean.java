package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName DeviceBatteryInfoBean
 * @Date 2019/1/22 10:46
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class DeviceBatteryInfoBean {

    /**
     * 电池状态信息。
     * 00：unknown(未知)
     * 01：discharging(非充电状态)
     * 02：charging(充电状态)
     * 03：full charged(充满状态)
     */
    Integer batteryState;

    //电池电量百分比0~100(uint：%)
    Integer batteryValue;

    //电池电压0~0xFFFF(uint：mV)
    Integer batteryVoltage;

    //电池温度0~0xFFFF(uint：0.1K)
    Integer batteryTemperature;


    public int getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(int batteryState) {
        this.batteryState = batteryState;
    }

    public int getBatteryValue() {
        return batteryValue;
    }

    public void setBatteryValue(int batteryValue) {
        this.batteryValue = batteryValue;
    }

    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatteryTemperature() {
        return batteryTemperature;
    }

    public void setBatteryTemperature(int batteryTemperature) {
        this.batteryTemperature = batteryTemperature;
    }

    @Override
    public String toString() {
        return "DeviceBatteryInfoBean{" +
                "batteryState=" + batteryState +
                ", batteryValue=" + batteryValue +
                ", batteryVoltage=" + batteryVoltage +
                ", batteryTemperature=" + batteryTemperature +
                '}';
    }
}
