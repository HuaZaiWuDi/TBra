package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName SingleDataDetailBean
 * @Date 2019/1/3 16:09
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class SingleDataDetailBean {


    /**
     * avgTemp : 1
     * collectTime : 2019-01-03T08:07:17.672Z
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * jsonData : string
     * leftUnusual : 1
     * leftUsual : 1
     * maxTemp : 1
     * minTemp : 1
     * rightUnusual : 1
     * rightUsual : 1
     * status : 101
     * totalPoints : 1
     * unusualNum : 1
     * unusualPoints : string
     * updateTime : 1511248354000
     * updateUser : 1
     * userId : string
     * usualNum : 1
     * warnMax : 1
     * warnMin : 1
     * warningFlag : 1
     */

    private Double avgTemp;
    private String collectTime;
    private Long createTime;
    private String gid;
    private String jsonData;
    private Integer leftUnusual;
    private Integer leftUsual;
    private double maxTemp;
    private double minTemp;
    private Integer rightUnusual;
    private Integer rightUsual;
    private Integer status;
    private Integer totalPoints;
    private Integer unusualNum;
    private String unusualPoints;
    private Long updateTime;
    private Integer usualNum;
    private double warnMax;
    private double warnMin;
    private Integer warningFlag;

    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public int getLeftUnusual() {
        return leftUnusual;
    }

    public void setLeftUnusual(int leftUnusual) {
        this.leftUnusual = leftUnusual;
    }

    public int getLeftUsual() {
        return leftUsual;
    }

    public void setLeftUsual(int leftUsual) {
        this.leftUsual = leftUsual;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public int getRightUnusual() {
        return rightUnusual;
    }

    public void setRightUnusual(int rightUnusual) {
        this.rightUnusual = rightUnusual;
    }

    public int getRightUsual() {
        return rightUsual;
    }

    public void setRightUsual(int rightUsual) {
        this.rightUsual = rightUsual;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getUnusualNum() {
        return unusualNum;
    }

    public void setUnusualNum(int unusualNum) {
        this.unusualNum = unusualNum;
    }

    public String getUnusualPoints() {
        return unusualPoints;
    }

    public void setUnusualPoints(String unusualPoints) {
        this.unusualPoints = unusualPoints;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getUsualNum() {
        return usualNum;
    }

    public void setUsualNum(int usualNum) {
        this.usualNum = usualNum;
    }

    public double getWarnMax() {
        return warnMax;
    }

    public void setWarnMax(double warnMax) {
        this.warnMax = warnMax;
    }

    public double getWarnMin() {
        return warnMin;
    }

    public void setWarnMin(double warnMin) {
        this.warnMin = warnMin;
    }

    public int getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(int warningFlag) {
        this.warningFlag = warningFlag;
    }
}
