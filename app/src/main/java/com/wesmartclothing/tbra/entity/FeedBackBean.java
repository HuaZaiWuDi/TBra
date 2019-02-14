package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName FeedBackBean
 * @Date 2019/2/14 14:34
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class FeedBackBean {


    /**
     * appType : string
     * ariseFreq : 0
     * contactInfo : string
     * dealStatus : 0
     * feedbackDesc : string
     * feedbackImg : string
     * feedbackType : 0
     * userId : string
     */

    private String appType;
    private int ariseFreq;
    private String contactInfo;
    private int dealStatus;
    private String feedbackDesc;
    private String feedbackImg;
    private int feedbackType;

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public int getAriseFreq() {
        return ariseFreq;
    }

    public void setAriseFreq(int ariseFreq) {
        this.ariseFreq = ariseFreq;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(int dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getFeedbackDesc() {
        return feedbackDesc;
    }

    public void setFeedbackDesc(String feedbackDesc) {
        this.feedbackDesc = feedbackDesc;
    }

    public String getFeedbackImg() {
        return feedbackImg;
    }

    public void setFeedbackImg(String feedbackImg) {
        this.feedbackImg = feedbackImg;
    }

    public int getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(int feedbackType) {
        this.feedbackType = feedbackType;
    }
}
