package com.wesmartclothing.tbra.entity;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName IllnessBean
 * @Date 2019/1/11 11:02
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class IllnessBean {


    /**
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * illnessName : string
     * illnessValue : string
     * sort : 1
     * status : 101
     * updateTime : 1511248354000
     * updateUser : 1
     * userId : string
     */

    private String illnessName;
    private String illnessValue;
    private Integer sort;
    private Integer status;
    private Boolean click = false;


    public boolean isClick() {
        return click;
    }

    public void setClick(boolean click) {
        this.click = click;
    }

    public String getIllnessName() {
        return illnessName;
    }

    public void setIllnessName(String illnessName) {
        this.illnessName = illnessName;
    }

    public String getIllnessValue() {
        return illnessValue;
    }

    public void setIllnessValue(String illnessValue) {
        this.illnessValue = illnessValue;
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
}
