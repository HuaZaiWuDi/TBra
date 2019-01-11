package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName AddSingleDataBean
 * @Date 2019/1/3 16:04
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class AddSingleDataBean {


    /**
     * collectTime : 2019-01-03T08:01:30.099Z
     * dataList : [{"nodeName":"string","nodeTemp":0,"warningFlag":0}]
     */

    private String collectTime;
    private List<DataListBean> dataList;

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
        private int nodeTemp;
        private int warningFlag;

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        public int getNodeTemp() {
            return nodeTemp;
        }

        public void setNodeTemp(int nodeTemp) {
            this.nodeTemp = nodeTemp;
        }

        public int getWarningFlag() {
            return warningFlag;
        }

        public void setWarningFlag(int warningFlag) {
            this.warningFlag = warningFlag;
        }
    }
}
