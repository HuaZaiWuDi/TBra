package com.wesmartclothing.tbra.entity;

import java.util.List;

/**
 * @Package com.wesmartclothing.tbra.entity
 * @FileName UserInfoBean
 * @Date 2019/1/3 16:33
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class UserInfoBean {


    /**
     * age : 1
     * avatar : string
     * birthday : 2019-01-11T02:23:04.433Z
     * city : string
     * country : string
     * createTime : 1511248354000
     * createUser : 1
     * gid : 1
     * height : 0
     * illnessList : [{"createTime":1511248354000,"createUser":1,"gid":1,"illnessName":"string","illnessValue":"string","sort":1,"status":101,"updateTime":1511248354000,"updateUser":1,"userId":"string"}]
     * invitationCode : string
     * macAddrList : ["string"]
     * phone : string
     * province : string
     * signature : string
     * status : 101
     * updateTime : 1511248354000
     * updateUser : 1
     * userName : string
     */

    private int age;
    private String avatar;
    private long birthday = Long.parseLong("631123200000");
    private String city;
    private String country;
    private long createTime;
    private int height;
    private String invitationCode;
    private String phone;
    private String province;
    private String signature;
    private int status;
    private String userName;
    private List<IllnessBean> illnessList;
    private List<String> macAddrList;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<IllnessBean> getIllnessList() {
        return illnessList;
    }

    public void setIllnessList(List<IllnessBean> illnessList) {
        this.illnessList = illnessList;
    }

    public List<String> getMacAddrList() {
        return macAddrList;
    }

    public void setMacAddrList(List<String> macAddrList) {
        this.macAddrList = macAddrList;
    }


}
