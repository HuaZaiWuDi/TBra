package com.wesmartclothing.tbra.entity.rxbus;

/**
 * @Package com.wesmartclothing.tbra.entity.rxbus
 * @FileName LocationBeanBus
 * @Date 2019/1/11 14:14
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class LocationBeanBus {

    public String lastLatitude;
    public String lastLongitude;
    public String latitude;//维度
    public String longitude;//经度
    public String country;//国建
    public String locality;//位置
    public String street;//街道


    public LocationBeanBus(String lastLatitude, String lastLongitude, String latitude, String longitude, String country, String locality, String street) {
        this.lastLatitude = lastLatitude;
        this.lastLongitude = lastLongitude;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.locality = locality;
        this.street = street;
    }
}
