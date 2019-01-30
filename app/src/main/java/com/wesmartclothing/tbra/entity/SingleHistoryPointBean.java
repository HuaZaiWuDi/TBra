package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName SingleHistoryPointBean
 * @Date 2019/1/15 18:56
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class SingleHistoryPointBean {


    /**
     * endRow : 0
     * firstPage : 0
     * hasNextPage : true
     * hasPreviousPage : true
     * isFirstPage : true
     * isLastPage : true
     * lastPage : 0
     * list : [{"avgTemp":0,"collectDate":"2019-01-29T10:20:51.420Z","leftTemp":0,"leftWarning":0,"rightTemp":0,"rightWarning":0}]
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
         * avgTemp : 0
         * collectDate : 2019-01-29T10:20:51.420Z
         * leftTemp : 0
         * leftWarning : 0
         * rightTemp : 0
         * rightWarning : 0
         */

        private Double avgTemp;
        private Long collectDate;
        private Double leftTemp;
        private Integer leftWarning;
        private Double rightTemp;
        private Integer rightWarning;
        private String point;


        public double getAvgTemp() {
            return avgTemp;
        }

        public void setAvgTemp(Double avgTemp) {
            this.avgTemp = avgTemp;
        }

        public Long getCollectDate() {
            return collectDate;
        }

        public void setCollectDate(Long collectDate) {
            this.collectDate = collectDate;
        }

        public double getLeftTemp() {
            return leftTemp;
        }

        public void setLeftTemp(Double leftTemp) {
            this.leftTemp = leftTemp;
        }

        public Integer getLeftWarning() {
            return leftWarning;
        }

        public void setLeftWarning(Integer leftWarning) {
            this.leftWarning = leftWarning;
        }

        public double getRightTemp() {
            return rightTemp;
        }

        public void setRightTemp(Double rightTemp) {
            this.rightTemp = rightTemp;
        }

        public Integer getRightWarning() {
            return rightWarning;
        }

        public void setRightWarning(Integer rightWarning) {
            this.rightWarning = rightWarning;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        @Override
        public String toString() {
            return "ListBean{" +
                    "avgTemp=" + avgTemp +
                    ", collectDate=" + collectDate +
                    ", leftTemp=" + leftTemp +
                    ", leftWarning=" + leftWarning +
                    ", rightTemp=" + rightTemp +
                    ", rightWarning=" + rightWarning +
                    ", point='" + point + '\'' +
                    '}';
        }
    }
}
