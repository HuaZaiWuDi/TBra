package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName PointDataBean
 * @Date 2019/1/15 11:46
 * @Author JACK
 * @Describe TODO异常点位信息界面接口
 * @Project tbra
 */
public class PointDataBean {


    /**
     * avgTemp : 1
     * collectCount : 1
     * collectDate : 2019-02-11T06:14:49.347Z
     * createTime : 1511248354000
     * createUser : 1
     * cycleNumber : 0
     * endTime : 2019-02-11T06:14:49.347Z
     * gid : 1
     * leftUnusual : 1
     * leftUsual : 1
     * maxTemp : 1
     * minTemp : 1
     * pointsList : [{"avgTemp":1,"collectCount":1,"collectDate":"2019-02-11T06:14:49.347Z","createTime":1511248354000,"createUser":1,"endTime":"2019-02-11T06:14:49.347Z","gid":1,"jsonData":"string","maxTemp":1,"minTemp":1,"point":"string","side":"string","startTime":"2019-02-11T06:14:49.347Z","status":101,"tempDTOList":[{"temp":0,"time":"2019-02-11T06:14:49.347Z","warning":1}],"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warningFlag":1}]
     * rightUnusual : 1
     * rightUsual : 1
     * startTime : 2019-02-11T06:14:49.347Z
     * status : 101
     * unusualCount : 1
     * updateTime : 1511248354000
     * updateUser : 1
     * userId : string
     * usualCount : 1
     * warnMax : 1
     * warnMin : 1
     * warningFlag : 1
     */

    private double avgTemp;
    private int collectCount;
    private long collectDate;
    private int cycleNumber;
    private long endTime;
    private String gid;
    private int leftUnusual;
    private int leftUsual;
    private double maxTemp;
    private double minTemp;
    private int rightUnusual;
    private int rightUsual;
    private long startTime;
    private int status;
    private int unusualCount;
    private int usualCount;
    private double warnMax;
    private double warnMin;
    private int warningFlag;
    private String jsonData;
    private List<PointsListBean> pointsList;


    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public long getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(long collectDate) {
        this.collectDate = collectDate;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
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

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUnusualCount() {
        return unusualCount;
    }

    public void setUnusualCount(int unusualCount) {
        this.unusualCount = unusualCount;
    }

    public int getUsualCount() {
        return usualCount;
    }

    public void setUsualCount(int usualCount) {
        this.usualCount = usualCount;
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

    public List<PointsListBean> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<PointsListBean> pointsList) {
        this.pointsList = pointsList;
    }

    public static class PointsListBean {
        /**
         * avgTemp : 1
         * collectCount : 1
         * collectDate : 2019-02-11T06:14:49.347Z
         * createTime : 1511248354000
         * createUser : 1
         * endTime : 2019-02-11T06:14:49.347Z
         * gid : 1
         * jsonData : string
         * maxTemp : 1
         * minTemp : 1
         * point : string
         * side : string
         * startTime : 2019-02-11T06:14:49.347Z
         * status : 101
         * tempDTOList : [{"temp":0,"time":"2019-02-11T06:14:49.347Z","warning":1}]
         * unusualCount : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * usualCount : 1
         * warningFlag : 1
         */

        private double avgTemp;
        private int collectCount;
        private long collectDate;
        private long endTime;
        private String gid;
        private String jsonData;
        private double maxTemp;
        private double minTemp;
        private String point;
        private String side;
        private long startTime;
        private int status;
        private int unusualCount;
        private int usualCount;
        private int warningFlag;
        private List<TempDTOListBean> tempDTOList;

        public double getAvgTemp() {
            return avgTemp;
        }

        public void setAvgTemp(double avgTemp) {
            this.avgTemp = avgTemp;
        }

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public long getCollectDate() {
            return collectDate;
        }

        public void setCollectDate(long collectDate) {
            this.collectDate = collectDate;
        }


        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
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

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getUnusualCount() {
            return unusualCount;
        }

        public void setUnusualCount(int unusualCount) {
            this.unusualCount = unusualCount;
        }

        public int getUsualCount() {
            return usualCount;
        }

        public void setUsualCount(int usualCount) {
            this.usualCount = usualCount;
        }

        public int getWarningFlag() {
            return warningFlag;
        }

        public void setWarningFlag(int warningFlag) {
            this.warningFlag = warningFlag;
        }

        public List<TempDTOListBean> getTempDTOList() {
            return tempDTOList;
        }

        public void setTempDTOList(List<TempDTOListBean> tempDTOList) {
            this.tempDTOList = tempDTOList;
        }

        public static class TempDTOListBean {
            /**
             * temp : 0
             * time : 2019-02-11T06:14:49.347Z
             * warning : 1
             */

            private double temp;
            private long time;
            private int warning;

            public double getTemp() {
                return temp;
            }

            public void setTemp(double temp) {
                this.temp = temp;
            }

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public int getWarning() {
                return warning;
            }

            public void setWarning(int warning) {
                this.warning = warning;
            }
        }

        @Override
        public String toString() {
            return "PointsListBean{" +
                    "avgTemp=" + avgTemp +
                    ", collectCount=" + collectCount +
                    ", collectDate=" + collectDate +
                    ", endTime=" + endTime +
                    ", gid='" + gid + '\'' +
                    ", jsonData='" + jsonData + '\'' +
                    ", maxTemp=" + maxTemp +
                    ", minTemp=" + minTemp +
                    ", point='" + point + '\'' +
                    ", side='" + side + '\'' +
                    ", startTime=" + startTime +
                    ", status=" + status +
                    ", unusualCount=" + unusualCount +
                    ", usualCount=" + usualCount +
                    ", warningFlag=" + warningFlag +
                    ", tempDTOList=" + tempDTOList +
                    '}';
        }
    }
}
