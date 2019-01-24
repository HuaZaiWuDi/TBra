package com.wesmartclothing.tbra.entity;

public class JsonDataBean {
    /**
     * nodeName : R01
     * nodeTemp : 36.7
     * warningFlag : 1 //warningFlag:0正常,1警告
     */

    private String nodeName;
    private Double nodeTemp;
    private Integer warningFlag;

    public JsonDataBean() {
    }

    public JsonDataBean(String nodeName, double nodeTemp, int warningFlag) {
        this.nodeName = nodeName;
        this.nodeTemp = nodeTemp;
        this.warningFlag = warningFlag;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public double getNodeTemp() {
        return nodeTemp;
    }

    public void setNodeTemp(double nodeTemp) {
        this.nodeTemp = nodeTemp;
    }

    public int getWarningFlag() {
        return warningFlag;
    }

    public void setWarningFlag(int warningFlag) {
        this.warningFlag = warningFlag;
    }

    @Override
    public String toString() {
        return "JsonDataBean{" +
                "nodeName='" + nodeName + '\'' +
                ", nodeTemp=" + nodeTemp +
                ", warningFlag=" + warningFlag +
                '}';
    }
}