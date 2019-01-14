package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName WarningRuleBean
 * @Date 2019/1/14 14:28
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class WarningRuleBean {


    /**
     * baseNum : 1
     * pointType : string :1,单点；2,多点
     * tempNum : 1
     */

    private int baseNum;
    private String pointType = "1";
    private double tempNum;

    public int getBaseNum() {
        return baseNum;
    }

    public void setBaseNum(int baseNum) {
        this.baseNum = baseNum;
    }

    public String getPointType() {
        return pointType;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }

    public double getTempNum() {
        return tempNum;
    }

    public void setTempNum(double tempNum) {
        this.tempNum = tempNum;
    }
}
