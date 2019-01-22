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
    private List<DataListBean> dataList;
    private int index;

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

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * nodeName : string
         * nodeTemp : 0
         * warningFlag : 0
         */

        private String nodeName;
        private double nodeTemp;
        private int warningFlag;

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
            return "DataListBean{" +
                    "nodeName='" + nodeName + '\'' +
                    ", nodeTemp=" + nodeTemp +
                    ", warningFlag=" + warningFlag +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AddTempDataBean{" +
                "collectTime='" + collectTime + '\'' +
                ", dataList=" + dataList +
                ", index=" + index +
                '}';
    }
}
