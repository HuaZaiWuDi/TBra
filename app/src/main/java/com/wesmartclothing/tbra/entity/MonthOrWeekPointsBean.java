package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName MonthOrWeekPointsBean
 * @Date 2019/1/26 17:26
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class MonthOrWeekPointsBean {


    /**
     * cycleFlag : string  "week","month"
     * gid : string
     * pointName : string
     */

    private String cycleFlag;
    private String gid;
    private String pointName;

    public String getCycleFlag() {
        return cycleFlag;
    }

    public void setCycleFlag(String cycleFlag) {
        this.cycleFlag = cycleFlag;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
}
