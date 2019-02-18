package com.wesmartclothing.tbra.entity;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.wesmartclothing.tbra.constant.Key;

import java.io.Serializable;
import java.util.Map;


/**
 * @Package lab.wesmartclothing.wefit.flyso.entity
 * @FileName LoginResult
 * @Date 2018/12/27 15:49
 * @Author JACK
 * @Describe TODO
 * @Project Android_WeFit_2.0
 */
public class LoginResult implements Serializable {


    public String openId = "";
    public String nickname = "";
    public String imageUrl = "";
    public String userType = "";


    public LoginResult(Map<String, String> map, SHARE_MEDIA share_media) {
        openId = map.get("uid");
        nickname = map.get("name");
        imageUrl = map.get("iconurl");
        userType = share_media == SHARE_MEDIA.QQ ? Key.LoginType_QQ :
                share_media == SHARE_MEDIA.SINA ? Key.LoginType_WEIBO : Key.LoginType_WEXIN;

    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "LoginResult{" +
                "openId='" + openId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
