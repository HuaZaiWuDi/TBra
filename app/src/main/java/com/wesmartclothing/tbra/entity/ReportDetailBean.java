package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName ReportDetailBean
 * @Date 2019/1/26 11:12
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class ReportDetailBean {


    /**
     * pointsList : [{"avgTemp":1,"collectCount":1,"collectDate":"2019-01-26T03:11:28.971Z","createTime":1511248354000,"createUser":1,"endTime":"2019-01-26T03:11:28.971Z","gid":1,"jsonData":"string","maxTemp":1,"minTemp":1,"point":"string","side":"string","startTime":"2019-01-26T03:11:28.971Z","status":101,"tempDTOList":[{"temp":0,"time":"2019-01-26T03:11:28.971Z","warning":1}],"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warningFlag":1}]
     * weekData : {"avgTemp":1,"collectCount":1,"collectDate":"2019-01-26T03:11:28.971Z","createTime":1511248354000,"createUser":1,"endTime":"2019-01-26T03:11:28.971Z","gid":1,"leftUnusual":1,"leftUsual":1,"maxTemp":1,"minTemp":1,"rightUnusual":1,"rightUsual":1,"startTime":"2019-01-26T03:11:28.971Z","status":101,"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warnMax":1,"warnMin":1,"warningFlag":1,"weeks":1}
     * monthData : {"avgTemp":1,"collectCount":1,"collectDate":"2019-01-26T03:11:28.971Z","createTime":1511248354000,"createUser":1,"endTime":"2019-01-26T03:11:28.971Z","gid":1,"leftUnusual":1,"leftUsual":1,"maxTemp":1,"minTemp":1,"rightUnusual":1,"rightUsual":1,"startTime":"2019-01-26T03:11:28.971Z","status":101,"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warnMax":1,"warnMin":1,"warningFlag":1,"weeks":1}
     */

    private WeekDataBean weekData;
    private WeekDataBean monthData;
    private List<PointsListBean> pointsList;

    public WeekDataBean getWeekData() {
        return weekData;
    }

    public void setWeekData(WeekDataBean weekData) {
        this.weekData = weekData;
    }

    public List<PointsListBean> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<PointsListBean> pointsList) {
        this.pointsList = pointsList;
    }


    public WeekDataBean getMonthData() {
        return monthData;
    }

    public void setMonthData(WeekDataBean monthData) {
        this.monthData = monthData;
    }

    public static class WeekDataBean {
        /**
         * avgTemp : 1
         * collectCount : 1
         * collectDate : 2019-01-26T03:11:28.971Z
         * createTime : 1511248354000
         * createUser : 1
         * endTime : 2019-01-26T03:11:28.971Z
         * gid : 1
         * leftUnusual : 1
         * leftUsual : 1
         * maxTemp : 1
         * minTemp : 1
         * rightUnusual : 1
         * rightUsual : 1
         * startTime : 2019-01-26T03:11:28.971Z
         * status : 101
         * unusualCount : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * usualCount : 1
         * warnMax : 1
         * warnMin : 1
         * warningFlag : 1
         * weeks : 1
         */

        private Double avgTemp;
        private int collectCount;
        private long collectDate;
        private long endTime;
        private String gid;
        private int leftUnusual;
        private int leftUsual;
        private Double maxTemp;
        private Double minTemp;
        private int rightUnusual;
        private int rightUsual;
        private long startTime;
        private int status;
        private int unusualCount;
        private int usualCount;
        private Double warnMax;
        private Double warnMin;
        private int warningFlag;
        private int weeks;

        public Double getAvgTemp() {
            return avgTemp;
        }

        public void setAvgTemp(Double avgTemp) {
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

        public Double getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(Double maxTemp) {
            this.maxTemp = maxTemp;
        }

        public Double getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(Double minTemp) {
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

        public Double getWarnMax() {
            return warnMax;
        }

        public void setWarnMax(Double warnMax) {
            this.warnMax = warnMax;
        }

        public Double getWarnMin() {
            return warnMin;
        }

        public void setWarnMin(Double warnMin) {
            this.warnMin = warnMin;
        }

        public int getWarningFlag() {
            return warningFlag;
        }

        public void setWarningFlag(int warningFlag) {
            this.warningFlag = warningFlag;
        }

        public int getWeeks() {
            return weeks;
        }

        public void setWeeks(int weeks) {
            this.weeks = weeks;
        }
    }

    public static class PointsListBean {
        /**
         * avgTemp : 1
         * collectCount : 1
         * collectDate : 2019-01-26T03:11:28.971Z
         * createTime : 1511248354000
         * createUser : 1
         * endTime : 2019-01-26T03:11:28.971Z
         * gid : 1
         * jsonData : string
         * maxTemp : 1
         * minTemp : 1
         * point : string
         * side : string
         * startTime : 2019-01-26T03:11:28.971Z
         * status : 101
         * tempDTOList : [{"temp":0,"time":"2019-01-26T03:11:28.971Z","warning":1}]
         * unusualCount : 1
         * updateTime : 1511248354000
         * updateUser : 1
         * userId : string
         * usualCount : 1
         * warningFlag : 1
         */

        private Double avgTemp;
        private int collectCount;
        private Long collectDate;
        private String endTime;
        private String gid;
        private String jsonData;
        private Double maxTemp;
        private Double minTemp;
        private String point;
        private String side;
        private String startTime;
        private int unusualCount;
        private String userId;
        private int usualCount;
        private int warningFlag;
        private List<TempDTOListBean> tempDTOList;

        public Double getAvgTemp() {
            return avgTemp;
        }

        public void setAvgTemp(Double avgTemp) {
            this.avgTemp = avgTemp;
        }

        public int getCollectCount() {
            return collectCount;
        }

        public void setCollectCount(int collectCount) {
            this.collectCount = collectCount;
        }

        public Long getCollectDate() {
            return collectDate;
        }

        public void setCollectDate(Long collectDate) {
            this.collectDate = collectDate;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
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

        public Double getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(Double maxTemp) {
            this.maxTemp = maxTemp;
        }

        public Double getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(Double minTemp) {
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getUnusualCount() {
            return unusualCount;
        }

        public void setUnusualCount(int unusualCount) {
            this.unusualCount = unusualCount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
             * time : 2019-01-26T03:11:28.971Z
             * warning : 1
             */

            private Double temp;
            private long time;
            private int warning;

            public Double getTemp() {
                return temp;
            }

            public void setTemp(Double temp) {
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
    }
}
