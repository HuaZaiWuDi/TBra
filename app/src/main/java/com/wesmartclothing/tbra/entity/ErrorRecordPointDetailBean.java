package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName ErrorRecordPointDetailBean
 * @Date 2019/2/11 14:54
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class ErrorRecordPointDetailBean {


    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{"avgTemp":1,"collectCount":1,"collectDate":"2019-02-11T06:58:05.543Z","createTime":1511248354000,"createUser":1,"endTime":"2019-02-11T06:58:05.543Z","gid":1,"jsonData":"string","maxTemp":1,"minTemp":1,"point":"string","side":"string","startTime":"2019-02-11T06:58:05.543Z","status":101,"tempDTOList":[{"temp":0,"time":"2019-02-11T06:58:05.543Z","warning":1}],"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warningFlag":1}]
     * navigateFirstPage : 0
     * navigateLastPage : 0
     * navigatePages : 0
     * navigatepageNums : [0]
     * nextPage : 0
     * pageNum : 0
     * pageSize : 0
     * pages : 0
     * prePage : 0
     * size : 0
     * startRow : 0
     * total : 0
     */

    private int endRow;
    private int firstPage;
    private boolean hasNextPage;
    private boolean hasPreviousPage;
    private boolean isFirstPage;
    private boolean isLastPage;
    private int lastPage;
    private int navigateFirstPage;
    private int navigateLastPage;
    private int navigatePages;
    private int nextPage;
    private int pageNum;
    private int pageSize;
    private int pages;
    private int prePage;
    private int size;
    private int startRow;
    private int total;
    private List<ListBean> list;
    private List<Integer> navigatepageNums;

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPreviousPage() {
        return hasPreviousPage;
    }

    public void setHasPreviousPage(boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }

    public boolean isIsFirstPage() {
        return isFirstPage;
    }

    public void setIsFirstPage(boolean isFirstPage) {
        this.isFirstPage = isFirstPage;
    }

    public boolean isIsLastPage() {
        return isLastPage;
    }

    public void setIsLastPage(boolean isLastPage) {
        this.isLastPage = isLastPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getNavigateFirstPage() {
        return navigateFirstPage;
    }

    public void setNavigateFirstPage(int navigateFirstPage) {
        this.navigateFirstPage = navigateFirstPage;
    }

    public int getNavigateLastPage() {
        return navigateLastPage;
    }

    public void setNavigateLastPage(int navigateLastPage) {
        this.navigateLastPage = navigateLastPage;
    }

    public int getNavigatePages() {
        return navigatePages;
    }

    public void setNavigatePages(int navigatePages) {
        this.navigatePages = navigatePages;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<Integer> getNavigatepageNums() {
        return navigatepageNums;
    }

    public void setNavigatepageNums(List<Integer> navigatepageNums) {
        this.navigatepageNums = navigatepageNums;
    }

    public static class ListBean {
        /**
         * avgTemp : 1
         * collectCount : 1
         * collectDate : 2019-02-11T06:58:05.543Z
         * createTime : 1511248354000
         * createUser : 1
         * endTime : 2019-02-11T06:58:05.543Z
         * gid : 1
         * jsonData : string
         * maxTemp : 1
         * minTemp : 1
         * point : string
         * side : string
         * startTime : 2019-02-11T06:58:05.543Z
         * status : 101
         * tempDTOList : [{"temp":0,"time":"2019-02-11T06:58:05.543Z","warning":1}]
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
        private long updateTime;
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

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
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
             * time : 2019-02-11T06:58:05.543Z
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
    }
}
