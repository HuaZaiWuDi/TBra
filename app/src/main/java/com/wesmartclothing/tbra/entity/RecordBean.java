package com.wesmartclothing.tbra.entity;

import com.wesmartclothing.tbra.constant.PointDate;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName RecordBean
 * @Date 2019/1/15 10:31
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class RecordBean {


    /**
     * （注：1_week-近一周，1_month-近一月，3_month-近三个月，6_month-近半年，1_year-近一年,all-全部）
     * <p>
     * pointName	string
     * 采集点位名称。
     * <p>
     * }
     * latestType : string
     * pointName : string
     */
    @PointDate
    private String latestType;
    private String point;
    private String side;

    public RecordBean(@PointDate String latestType) {
        this.latestType = latestType;
    }

    public RecordBean(String latestType, String point, String side) {
        this.latestType = latestType;
        this.point = point;
        this.side = side;
    }


    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getLatestType() {
        return latestType;
    }

    public void setLatestType(@PointDate String latestType) {
        this.latestType = latestType;
    }

}
