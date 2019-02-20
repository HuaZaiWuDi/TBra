package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName AddTempDataBean
 * @Date 2019/1/22 14:00
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class AddTempDataBean {


    /**
     * yyyy-MM-dd hh:mm:ss
     * <p>
     * collectTime : 2019-01-22T05:57:46.309Z
     * dataList : [{"nodeName":"string","nodeTemp":0,"warningFlag":0}]
     * userId : string
     */

    private String collectTime;
    private List<JsonDataBean> dataList;
    private Integer index;
    private byte pickageSign;//包标识


    public byte getPickageSign() {
        return pickageSign;
    }

    public void setPickageSign(byte pickageSign) {
        this.pickageSign = pickageSign;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public List<JsonDataBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<JsonDataBean> dataList) {
        this.dataList = dataList;
    }


    @Override
    public String toString() {
        return "AddTempDataBean{" +
                "collectTime='" + collectTime + '\'' +
                ", dataList=" + dataList +
                ", index=" + index +
                ", pickageSign=" + pickageSign +
                '}';
    }
}
