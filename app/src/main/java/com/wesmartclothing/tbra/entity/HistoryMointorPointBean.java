package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName HistoryMointorPointBean
 * @Date 2019/1/29 17:05
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class HistoryMointorPointBean {


    /**
     * latestType : string
     * point : string
     * side : string
     */

    private String latestType;
    private String point;
    private String side;


    public HistoryMointorPointBean(String latestType, String point) {
        this.latestType = latestType;
        this.point = point;
    }

    public String getLatestType() {
        return latestType;
    }

    public void setLatestType(String latestType) {
        this.latestType = latestType;
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
}
