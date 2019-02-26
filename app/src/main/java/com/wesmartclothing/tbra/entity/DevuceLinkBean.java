package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName DevuceLinkBean
 * @Date 2019/2/26 13:58
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class DevuceLinkBean {


    /**
     * city : string
     * country : string
     * deviceName : string
     * deviceNo : string
     * firmwareVersion : string
     * lastOnline : 2019-02-26T05:56:50.522Z
     * linkStatus : 1
     * macAddr : string
     * mcuVersion : string
     * onlineDuration : 1
     * productId : string
     * productName : string
     * province : string
     * wakeTime : 2019-02-26T05:56:50.522Z
     */

    private String city;
    private String country;
    private String deviceName;
    private String deviceNo;
    private String firmwareVersion;
    private long lastOnline;
    private int linkStatus;
    private String macAddr;
    private String mcuVersion;
    private long onlineDuration;
    private String productId;
    private String productName;
    private String province;
    private long wakeTime;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public int getLinkStatus() {
        return linkStatus;
    }

    public void setLinkStatus(int linkStatus) {
        this.linkStatus = linkStatus;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getMcuVersion() {
        return mcuVersion;
    }

    public void setMcuVersion(String mcuVersion) {
        this.mcuVersion = mcuVersion;
    }

    public long getOnlineDuration() {
        return onlineDuration;
    }

    public void setOnlineDuration(long onlineDuration) {
        this.onlineDuration = onlineDuration;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public long getWakeTime() {
        return wakeTime;
    }

    public void setWakeTime(long wakeTime) {
        this.wakeTime = wakeTime;
    }
}
