package com.wesmartclothing.tbra.net;

import com.vondear.rxtools.utils.net.HttpResult;
import com.wesmartclothing.tbra.entity.AddTempDataBean;
import com.wesmartclothing.tbra.entity.BindDeviceBean;
import com.wesmartclothing.tbra.entity.CarouselPictureBean;
import com.wesmartclothing.tbra.entity.ErrorRecordPointDetailBean;
import com.wesmartclothing.tbra.entity.FeedBackBean;
import com.wesmartclothing.tbra.entity.GidBean;
import com.wesmartclothing.tbra.entity.IllnessBean;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.entity.MonthOrWeekPointsBean;
import com.wesmartclothing.tbra.entity.OtherLoginBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.entity.RecordBean;
import com.wesmartclothing.tbra.entity.RelateAccountListBean;
import com.wesmartclothing.tbra.entity.ReportDataBean;
import com.wesmartclothing.tbra.entity.ReportDetailBean;
import com.wesmartclothing.tbra.entity.SingleDataDetailBean;
import com.wesmartclothing.tbra.entity.SingleHistoryPointBean;
import com.wesmartclothing.tbra.entity.UserCenterBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.entity.WarningRecordBean;
import com.wesmartclothing.tbra.entity.WarningRuleBean;

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
    Observable<HttpResult<Integer>> addSingleData(@Body List<AddTempDataBean> bean);


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
    Observable<HttpResult<String>> saveUserInfo(@Body UserInfoBean Bean);


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


    /**
     * 查询用户告警信息列表
     */
    @FormUrlEncoded
    @POST(" user/warningInfo")
    Observable<HttpResult<WarningRecordBean>> warningInfo(
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );


    /**
     * 告警信息已读
     */
    @POST("user/warningInfoReaded")
    Observable<HttpResult<PointDataBean>> warningInfoReaded(@Body GidBean bean);


    /**
     * 告警信息全部已读
     */
    @POST("user/warningInfoReadedAll")
    Observable<HttpResult<Integer>> warningInfoReadedAll();


    /**
     * 用户反馈文字信息接口
     */
    @POST("user/feedback")
    Observable<HttpResult<String>> feedback(@Body FeedBackBean backBean);

    /**
     * 用户反馈图片信息接口
     */
    @Multipart
    @POST("user/feedbackImg")
    Observable<HttpResult<String>> feedbackImg(@Part() List<MultipartBody.Part> parts);


    /**
     * 查询绑定第三方信息接口
     */
    @POST("user/fetchBindingOuterInfo")
    Observable<HttpResult<List<OtherLoginBean>>> fetchBindingOuterInfo();


    /**
     * 绑定第三方信息接口
     */
    @POST("user/bindingOuterInfo")
    Observable<HttpResult<String>> bindingOuterInfo(@Body OtherLoginBean bean);

    /**
     * 解除绑定第三方信息接口
     */
    @FormUrlEncoded
    @POST("user/unbindOuterInfo")
    Observable<HttpResult<String>> unbindOuterInfo(
            @Field("userType") String userType
    );


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


    // 获取广告轮播图列表
    @POST("user/fetchCarouselPictureList")
    Observable<HttpResult<List<CarouselPictureBean>>> fetchCarouselPictureList();


    ///////////////////////////////////////////////////////////////////////////
    //设备信息接口
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 绑定设备信息
     */
    @POST("device/bindDevice")
    Observable<HttpResult<String>> bindDevice(@Body BindDeviceBean Bean);


    /**
     * 获取设备信息
     */
    @POST("device/deviceInfoList")
    Observable<HttpResult<String>> deviceInfoList(@Body GidBean Bean);


    ///////////////////////////////////////////////////////////////////////////
    // 通知账号信息
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加通知账号信息
     */
    @POST("relateAccount/addRelateAccount")
    Observable<HttpResult<String>> addRelateAccount(@Body RelateAccountListBean.ListBean Bean);


    /**
     * 获取通知账号信息
     */
    @POST("relateAccount/relateAccountList")
    Observable<HttpResult<RelateAccountListBean>> relateAccountList();


    /**
     * 开启或关闭通知账号信息
     */
    @POST("relateAccount/openOrClose")
    Observable<HttpResult<String>> openOrClose(@Body RelateAccountListBean.ListBean Bean);


    /**
     * 移除通知账号信息
     */
    @POST("relateAccount/removeRelateAccount")
    Observable<HttpResult<String>> removeRelateAccount(@Body RelateAccountListBean.ListBean Bean);


    /**
     * 修改通知账号信息
     */
    @POST("relateAccount/editRelateAccount")
    Observable<HttpResult<String>> editRelateAccount(@Body RelateAccountListBean.ListBean Bean);

    ///////////////////////////////////////////////////////////////////////////
    // 用户告警规则信息
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 添加用户告警规则信息
     */
    @POST("warningRule/addUserRule")
    Observable<HttpResult<String>> addUserRule(@Body WarningRuleBean Bean);

    /**
     * 用户告警规则信息详情
     */
    @POST("warningRule/userRuleDetail")
    Observable<HttpResult<WarningRuleBean>> userRuleDetail();


    ///////////////////////////////////////////////////////////////////////////
    // 用户数据模块
    ///////////////////////////////////////////////////////////////////////////


    /**
     * 月报信息列表数据
     */
    @FormUrlEncoded
    @POST("dataRecord/monthDataList")
    Observable<HttpResult<ReportDataBean>> monthDataList(
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );

    /**
     * 周报信息列表数据
     */
    @FormUrlEncoded
    @POST("dataRecord/weekDataList")
    Observable<HttpResult<ReportDataBean>> weekDataList(
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );


    /**
     * 获取用户最近一段时间的单次数据
     */
    @POST("dataRecord/latestSingleData")
    Observable<HttpResult<List<SingleDataDetailBean>>> latestSingleData(@Body RecordBean bean);


    /**
     * 获取用户最近一段时间的异常点位数据
     */
    @FormUrlEncoded
    @POST("dataRecord/latestUnusualPointData")
    Observable<HttpResult<SingleHistoryPointBean>> latestUnusualPointData(
            @Field("latestType") String latestType,
            @Field("point") String point,
            @Field("side") String side,
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );


    /**
     * 每月的月报数据
     */
    @POST("dataRecord/monthInfo")
    Observable<HttpResult<ReportDetailBean>> monthInfo(@Body GidBean bean);


    /**
     * 每周的周报数据
     */
    @POST("dataRecord/weekInfo")
    Observable<HttpResult<ReportDetailBean>> weekInfo(@Body GidBean bean);


    /**
     * 获取用户最近一段时间的异常点位数据
     */
    @FormUrlEncoded
    @POST(" dataRecord/unusualPointData")
    Observable<HttpResult<ErrorRecordPointDetailBean>> unusualPointData(
            @Field("side") String side,
            @Field("point") String point,
            @Field("startTime") long startTime,
            @Field("endTime") long endTime,
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );


    /**
     * 获取用户最近一段时间的异常数据
     */
    @POST("dataRecord/unusualData")
    Observable<HttpResult<PointDataBean>> unusualData(@Body RecordBean bean);


    /**
     * 获取用户每天的数据信息列表（操作->使用记录）
     */
    @FormUrlEncoded
    @POST("dataRecord/symmetryPointInfo")
    Observable<HttpResult<SingleHistoryPointBean>> symmetryPointInfo(
            @Field("latestType") String latestType,
            @Field("point") String point,
            @Field("pageNum") int pageNum,
            @Field("pageSize") int pageSize
    );


    /**
     * 每月或每周点位数据信息
     */
    @POST("dataRecord/monthOrWeekPointsInfo")
    Observable<HttpResult<List<ReportDetailBean.PointsListBean>>> monthOrWeekPointsInfo(@Body MonthOrWeekPointsBean bean);
}
