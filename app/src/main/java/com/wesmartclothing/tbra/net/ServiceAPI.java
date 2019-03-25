package com.wesmartclothing.tbra.net;

import com.vondear.rxtools.utils.RxLogUtils;

/**
 * @author Jack
 * @date on 2018/11/29
 * @describe TODO
 * @org 智裳科技
 */
public class ServiceAPI {


    //一期
//    public static final String BASE_URL = "https://dev.wesmartclothing.com/mix/";//外网
    public static final String BASE_URL_192 = "http://10.10.11.233:16320";//德人服务器

    //二期
    public static final String BASE_URL_208 = "http://10.10.11.208:15390";//牛耕测试环境
    public static final String BASE_URL_125 = "http://119.23.225.125:15390";
    public static final String BASE_URL_mix = "http://10.10.11.208:15390/mix/";//网关

    public static final String BASE_URL_TEST = "http://10.10.11.208:15112";//网关


    //上线
    public static final String BASE_RELEASE = "http://tbra.wesmartclothing.com/";//上线版本


    public static final String BASE_DEBUG = "http://tbra.wesmartclothing.com/";//测试版本


    public static String BASE_URL = BASE_URL_192;

    public static String BASE_SERVICE = BASE_URL.replace("mix", "system");


    public static void switchURL(String baseUrl) {
        BASE_URL = baseUrl;
        BASE_SERVICE = baseUrl.replace("mix", "system");
        RxLogUtils.e("系统配置接口:" + BASE_SERVICE);
    }


}
