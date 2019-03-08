package com.wesmartclothing.tbra.ui.guide;

import android.content.Intent;
import android.os.Bundle;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.service.BleService;
import com.wesmartclothing.tbra.tools.AddTempData;
import com.wesmartclothing.tbra.ui.login.InputInfoActivity;
import com.wesmartclothing.tbra.ui.login.InvitationCodeActivity;
import com.wesmartclothing.tbra.ui.login.LoginActivity;
import com.wesmartclothing.tbra.ui.main.MainActivity;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author Jack
 * @date on 2018/12/27
 * @describe TODO启动页
 * @org 智裳科技
 */
public class SplashActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
    }

    @Override
    public void initNetData() {

//        RxActivityUtils.skipActivityAndFinish(mContext, ScanDeviceActivity.class);

        if (!SPUtils.getBoolean(SPKey.SP_GUIDE)) {
            SPUtils.put(SPKey.SP_GUIDE, true);
            RxActivityUtils.skipActivityAndFinish(mActivity, GuideActivity.class);
            return;
        }
        if (RxDataUtils.isNullString(SPUtils.getString(SPKey.SP_UserId, ""))) {
            RxActivityUtils.skipActivityAndFinish(mActivity, LoginActivity.class);
            return;
        }
        initUserInfo();
        otherSetting();

        startService(new Intent(mContext, BleService.class));
    }

    private void otherSetting() {
        new AddTempData().uploadCacheOrBleData();
    }

    //获取用户信息
    public void initUserInfo() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userInfo(),
                lifecycleSubject,
                SPKey.SP_UserInfo,
                UserInfoBean.class,
                CacheStrategy.firstRemote()
        )
                .timeout(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfo) {
                        if (!RxDataUtils.isEmpty(userInfo.getMacAddrList())) {
                            SPUtils.put(SPKey.SP_BIND_DEVICE, userInfo.getMacAddrList().get(0));
                        }
                        if (RxDataUtils.isNullString(userInfo.getInvitationCode())) {
                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InvitationCodeActivity.class);
                        } else if (userInfo.getAge() == 0) {
                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InputInfoActivity.class);
                        } else {
                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), MainActivity.class);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        if (RxCache.getDefault().containsKey(SPKey.SP_UserInfo)) {
                            RxCache.getDefault().<UserInfoBean>load(SPKey.SP_UserInfo, UserInfoBean.class)
                                    .compose(RxComposeUtils.bindLife(lifecycleSubject))
                                    .map(new CacheResult.MapFunc<>())
                                    .subscribe(new RxNetSubscriber<UserInfoBean>() {
                                        @Override
                                        protected void _onNext(UserInfoBean userInfo) {
                                            if (!RxDataUtils.isEmpty(userInfo.getMacAddrList())) {
                                                SPUtils.put(SPKey.SP_BIND_DEVICE, userInfo.getMacAddrList().get(0));
                                            }
                                            if (RxDataUtils.isNullString(userInfo.getInvitationCode())) {
                                                RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InvitationCodeActivity.class);
                                            } else if (userInfo.getAge() == 0) {
                                                RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InputInfoActivity.class);
                                            } else {
                                                RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), MainActivity.class);
                                            }
                                        }
                                    });
                        } else
                            RxActivityUtils.skipActivityAndFinish(mContext, LoginActivity.class);
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initRxBus2() {

    }
}
