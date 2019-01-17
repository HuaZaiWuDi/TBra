package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName ReportDataBean
 * @Date 2019/1/15 11:51
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class ReportDataBean {


    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{"avgTemp":1,"collectCount":1,"collectDate":"2019-01-15T03:50:48.313Z","createTime":1511248354000,"createUser":1,"endTime":"2019-01-15T03:50:48.313Z","gid":1,"leftUnusual":1,"leftUsual":1,"maxTemp":1,"minTemp":1,"months":0,"rightUnusual":1,"rightUsual":1,"startTime":"2019-01-15T03:50:48.313Z","status":101,"unusualCount":1,"updateTime":1511248354000,"updateUser":1,"userId":"string","usualCount":1,"warnMax":1,"warnMin":1,"warningFlag":1}]
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
         * collectDate : 2019-01-15T03:50:48.313Z
         * createTime : 1511248354000
         * createUser : 1
         * endTime : 2019-01-15T03:50:48.313Z
         * gid : 1
         * leftUnusual : 1
         * leftUsual : 1
         * maxTemp : 1
         * minTemp : 1
         * months : 0
         * rightUnusual : 1
         * rightUsual : 1
         * startTime : 2019-01-15T03:50:48.313Z
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
        private long endTime;
        private String gid;
        private int leftUnusual;
        private int leftUsual;
        private double maxTemp;
        private double minTemp;
        private int months;
        private int rightUnusual;
        private int rightUsual;
        private long startTime;
        private int status;
        private int unusualCount;
        private long updateTime;
        private int usualCount;
        private double warnMax;
        private double warnMin;
        private int warningFlag;
        private int weeks;

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

        public int getMonths() {
            return months;
        }

        public void setMonths(int months) {
            this.months = months;
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

        public int getWeeks() {
            return weeks;
        }

        public void setWeeks(int weeks) {
            this.weeks = weeks;
        }
    }
}
