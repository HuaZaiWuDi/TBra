package com.wesmartclothing.tbra.entity.rxbus;

import com.wesmartclothing.tbra.entity.UserInfoBean;

/**
 * @Package com.wesmartclothing.tbra.entity.rxbus
 * @FileName RefreshUserInfoBus
 * @Date 2019/2/13 16:53
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class RefreshUserInfoBus {

    private UserInfoBean mUserInfoBean;

    public RefreshUserInfoBus(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }

    public UserInfoBean getUserInfoBean() {
        return mUserInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        mUserInfoBean = userInfoBean;
    }
}
