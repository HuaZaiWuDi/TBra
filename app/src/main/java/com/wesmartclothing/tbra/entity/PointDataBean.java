package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName PointDataBean
 * @Date 2019/1/15 11:46
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class PointDataBean {


    /**
     * avgTemp : 35.77
     * collectTime : 1543890167000
     * createTime : 1546684135000
     * createUser : 4d974e25cebe4535bde4e23302ba0dd2
     * gid : ee4dfbf6f6f24726b4278a8c051ca7ca
     * //warningFlag:0正常,1警告
     * jsonData : [{"nodeName":"R01","nodeTemp":36.7,"warningFlag":1},{"nodeName":"R02","nodeTemp":34.7,"warningFlag":1},{"nodeName":"R03","nodeTemp":35.7,"warningFlag":0},{"nodeName":"R04","nodeTemp":36.2,"warningFlag":0},{"nodeName":"R05","nodeTemp":36,"warningFlag":0},{"nodeName":"R06","nodeTemp":35.7,"warningFlag":0},{"nodeName":"R07","nodeTemp":35.3,"warningFlag":0},{"nodeName":"R08","nodeTemp":35.7,"warningFlag":0},{"nodeName":"L01","nodeTemp":36.7,"warningFlag":1},{"nodeName":"L02","nodeTemp":34.7,"warningFlag":1},{"nodeName":"L03","nodeTemp":35.7,"warningFlag":0},{"nodeName":"L04","nodeTemp":36.2,"warningFlag":0},{"nodeName":"L05","nodeTemp":36,"warningFlag":0},{"nodeName":"L06","nodeTemp":35.7,"warningFlag":0},{"nodeName":"L07","nodeTemp":35.3,"warningFlag":0},{"nodeName":"L08","nodeTemp":35.7,"warningFlag":0}]
     * leftUnusual : 2
     * leftUsual : 6
     * maxTemp : 36.7
     * minTemp : 34.7
     * rightUnusual : 2
     * rightUsual : 6
     * status : 101
     * totalPoints : 16
     * unusualNum : 4
     * unusualPoints : ["R01 ","R02 ","L01 ","L02 "]
     * updateTime : 1546684135000
     * updateUser : 4d974e25cebe4535bde4e23302ba0dd2
     * userId : 4d974e25cebe4535bde4e23302ba0dd2
     * usualNum : 12
     * warnMax : 36.27
     * warnMin : 35.27
     * warningFlag : 0
     */

    private double avgTemp;
    private long collectTime;
    private long createTime;
    private String createUser;
    private String gid;
    private int leftUnusual;
    private int leftUsual;
    private double maxTemp;
    private double minTemp;
    private int rightUnusual;
    private int rightUsual;
    private int status;
    private int totalPoints;
    private int unusualNum;
    private long updateTime;
    private String updateUser;
    private String userId;
    private int usualNum;
    private double warnMax;
    private double warnMin;
    private int warningFlag;
    private String jsonData;
    private String unusualPoints;

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
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

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getUnusualPoints() {
        return unusualPoints;
    }

    public void setUnusualPoints(String unusualPoints) {
        this.unusualPoints = unusualPoints;
    }


}
