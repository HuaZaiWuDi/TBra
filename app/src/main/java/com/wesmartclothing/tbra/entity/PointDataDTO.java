package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName PointDataDTO
 * @Date 2019/2/11 16:18
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class PointDataDTO {


    /**
     * endTime : 2019-02-11T08:17:18.480Z
     * point : string
     * side : string
     * startTime : 2019-02-11T08:17:18.480Z
     * userId : string
     * warningFlag : 1
     */

    private Long endTime;
    private String point;
    private String side;
    private Long startTime;
    private String userId;
    private int warningFlag;


    public PointDataDTO(Long endTime, String point, String side, Long startTime) {
        this.endTime = endTime;
        this.point = point;
        this.side = side;
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(int warningFlag) {
        this.warningFlag = warningFlag;
    }
}
