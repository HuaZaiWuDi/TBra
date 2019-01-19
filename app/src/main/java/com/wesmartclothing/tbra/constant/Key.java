package com.wesmartclothing.tbra.constant;

/**
 * @Package com.wesmartclothing.tbra.constant
 * @FileName Key
 * @Date 2019/1/4 17:46
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public interface Key {


    long CACHE_TIME_OUT = 7 * 24 * 60 * 60 * 1000;//一周

    String BUNDLE_OTHER_LOGIN_INFO = "BUNDLE_OTHER_LOGIN_INFO";//第三方登录用户信息

    String LoginType_WEXIN = "WeChat";
    String LoginType_QQ = "ic_qq";
    String LoginType_WEIBO = "MicroBlog";


    //异常点的日期
    String BUNDLE_LATEST_TYPE = "BUNDLE_LATEST_TYPE";

    //异常点的名字
    String BUNDLE_POINT_NAME = "BUNDLE_POINT_NAME";
}
