package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName CarouselPictureBean
 * @Date 2019/2/14 17:46
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class CarouselPictureBean {


    /**
     * appType : string
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * href : string
     * imgName : string
     * imgUrl : string
     * newsId : string
     * sort : 0
     * status : 101
     * summary : string
     * title : string
     * updateTime : 1511248354000
     * updateUser : 1
     */

    private String appType;
    private long createTime;
    private String gid;
    private String href;
    private String imgName;
    private String imgUrl;
    private String newsId;
    private int sort;
    private int status;
    private String summary;
    private String title;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
