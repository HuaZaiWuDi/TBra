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
    private String pointName;

    public RecordBean(@PointDate String latestType) {
        this.latestType = latestType;
    }

    public RecordBean(@PointDate String latestType, String pointName) {
        this.latestType = latestType;
        this.pointName = pointName;
    }

    public String getLatestType() {
        return latestType;
    }

    public void setLatestType(@PointDate String latestType) {
        this.latestType = latestType;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
}
