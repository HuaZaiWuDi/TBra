package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName UserCenterBean
 * @Date 2019/1/3 16:37
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class UserCenterBean {


    /**
     * totalDays : 0
     * warningDays : 0
     * totalCount：0
     */

    private int totalDays;
    private int warningDays;
    private int totalCount;


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }

    public int getWarningDays() {
        return warningDays;
    }

    public void setWarningDays(int warningDays) {
        this.warningDays = warningDays;
    }
}
