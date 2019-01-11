package com.wesmartclothing.tbra.net;

import com.vondear.rxtools.utils.net.HttpResult;
import com.wesmartclothing.tbra.entity.AddSingleDataBean;
import com.wesmartclothing.tbra.entity.BindDeviceBean;
import com.wesmartclothing.tbra.entity.EmptyBean;
import com.wesmartclothing.tbra.entity.GidBean;
import com.wesmartclothing.tbra.entity.IllnessBean;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.entity.SingleDataDetailBean;
import com.wesmartclothing.tbra.entity.UserCenterBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @Package com.wesmartclothing.tbra.net
 * @FileName ApiService
 * @Date 2018/12/28 16:01
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public interface ApiService {
    String BASE_URL = ServiceAPI.BASE_URL;


    ///////////////////////////////////////////////////////////////////////////
    // 单次检测数据信息
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 添加单次或多次监测数据信息
     */
    @POST("singleData/addSingleData")
    Observable<HttpResult<EmptyBean>> addSingleData(@Body AddSingleDataBean bean);


    /**
     * 单次监测数据信息详情
     */
    @POST("singleData/singleDataDetail")
    Observable<HttpResult<SingleDataDetailBean>> singleDataDetail(@Body GidBean gidBean);


    /**
     * 获取单次监测数据信息
     */
    @FormUrlEncoded
    @POST("singleData/singleDataList")
    Observable<HttpResult<SingleDataDetailBean>> singleDataList(
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize,
            @Field("collectTime") long collectTime
    );

///////////////////////////////////////////////////////////////////////////
// 我的模块
///////////////////////////////////////////////////////////////////////////


    /**
     * 保存用户信息，无需上送imgUrl参数
     */
    @POST("user/saveUserInfo")
    Observable<HttpResult<String>> saveUserInfo(@Body UserInfoBean gidBean);


    /**
     * 上传用户头像文件
     */
    @Multipart
    @POST("user/uploadUserImg")
    Observable<HttpResult<String>> uploadUserImg(@Part MultipartBody.Part file);


    /**
     * 获取用户信息
     */
    @POST("user/userCenter")
    Observable<HttpResult<UserCenterBean>> userCenter();


    /**
     * 获取用户个人资料
     */
    @POST("user/userInfo")
    Observable<HttpResult<UserInfoBean>> userInfo();


    ///////////////////////////////////////////////////////////////////////////
    // 登录
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 登录验证
     */
    @FormUrlEncoded
    @POST("login")
    Observable<HttpResult<LoginInfoBean>> login(
            @Field("phone") String phone,
            @Field("code") String code
    );


    /**
     * 退出登录
     */
    @POST("logout")
    Observable<HttpResult<String>> logout();

    /**
     * 第三方登录接口
     */
    @FormUrlEncoded
    @POST("outerLogin")
    Observable<HttpResult<LoginInfoBean>> outerLogin(
            @Field("outerId") String outerId,
            @Field("userName") String userName,
            @Field("avatar") String avatar,
            @Field("userType") String userType);

    /**
     * 绑定手机号
     */
    @FormUrlEncoded
    @POST("phoneBind")
    Observable<HttpResult<LoginInfoBean>> phoneBind(
            @Field("phone") String phone,
            @Field("code") String password,
            @Field("outerId") String outerId,
            @Field("userName") String userName,
            @Field("imgUrl") String imgUrl,
            @Field("userType") String userType);

    /**
     * 密码登录
     */
    @FormUrlEncoded
    @POST("pwdLogin")
    Observable<HttpResult<LoginInfoBean>> pwdLogin(
            @Field("phone") String phone,
            @Field("password") String password);


    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("register")
    Observable<HttpResult<LoginInfoBean>> register(
            @Field("phone") String phone,
            @Field("code") String code,
            @Field("password") String password);

    /**
     * 重置密码
     */
    @FormUrlEncoded
    @POST("resetPassword")
    Observable<HttpResult<LoginInfoBean>> resetPassword(
            @Field("phone") String phone,
            @Field("password") String password,
            @Field("code") String code);


    /**
     * 获取验证码
     */
    @FormUrlEncoded
    @POST("sendCode")
    Observable<HttpResult<String>> sendCode(
            @Field("phone") String phone,
            @Field("verify") String verify);

    /**
     * 去重置密码
     */
    @FormUrlEncoded
    @POST("toResetPassword")
    Observable<HttpResult<Boolean>> toResetPassword(
            @Field("phone") String phone,
            @Field("code") String code);


    //验证邀请码
    @FormUrlEncoded
    @POST("user/verifyInvitationCode")
    Observable<HttpResult<Boolean>> verifyInvitationCode(@Field("invitationCode") String invitationCode);


    // 疾病列表下拉框
    @POST("user/systemIllnessList")
    Observable<HttpResult<List<IllnessBean>>> systemIllnessList();

    ///////////////////////////////////////////////////////////////////////////
    //设备信息接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 绑定设备信息
     */
    @POST("device/bindDevice")
    Observable<HttpResult<EmptyBean>> saveUserInfo(@Body BindDeviceBean Bean);

    /**
     * 移除绑定设备
     */
    @POST("device/removeDeviceBind")
    Observable<HttpResult<EmptyBean>> saveUserInfo(@Body GidBean Bean);


}
