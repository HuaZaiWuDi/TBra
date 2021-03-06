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


    long CACHE_TIME_OUT_HOUR = 60 * 60 * 1000;//一周
    long CACHE_TIME_OUT_DAY = 24 * 60 * 60 * 1000;//一天

    String BUNDLE_OTHER_LOGIN_INFO = "BUNDLE_OTHER_LOGIN_INFO";//第三方登录用户信息

    String LoginType_WEXIN = "WeChat";
    String LoginType_QQ = "ic_qq";
    String LoginType_WEIBO = "MicroBlog";

    String WEB_URL_Registration_Agreement = "file:///android_asset/html/registration_agreement.html";
//    String WEB_URL_Registration_Agreement = "http://doc.wesmartclothing.com/terms/wemeet.html";

    String WEB_URL_Implicit_Clause = "file:///android_asset/html/implicit_clause.html";
//    String WEB_URL_Implicit_Clause = "http://doc.wesmartclothing.com/terms/wemeet-policy.html";


    String DEVICE_NO = "ZS-TBRA-0001";

    //异常点的日期
    String BUNDLE_LATEST_TYPE = "BUNDLE_LATEST_TYPE";

    //异常点的名字
    String BUNDLE_POINT_NAME = "BUNDLE_POINT_NAME";

    String BUNDLE_WEB_URL = "BUNDLE_WEB_URL";

    String BUNDLE_CAN_SKIP = "BUNDLE_CAN_SKIP";

    String BUNDLE_REPORT_TYPE = "BUNDLE_REPORT_TYPE";

    String BUNDLE_GID_DATA = "BUNDLE_GID_DATA";

    //标题
    String BUNDLE_TITLE = "BUNDLE_TITLE";

    String BUNDLE_DATA = "BUNDLE_DATA";
}
