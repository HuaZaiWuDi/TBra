package com.wesmartclothing.tbra.ui.guide;

import android.content.Intent;
import android.os.Bundle;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.service.BleService;
import com.wesmartclothing.tbra.ui.login.InputInfoActivity;
import com.wesmartclothing.tbra.ui.main.MainActivity;
import com.zchu.rxcache.stategy.CacheStrategy;

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
        startService(new Intent(mContext, BleService.class));
    }

    @Override
    public void initNetData() {
        RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity.class);


        if (!SPUtils.getBoolean(SPKey.SP_GUIDE)) {
            SPUtils.put(SPKey.SP_GUIDE, true);
//            RxActivityUtils.skipActivityAndFinish(mActivity, GuideActivity.class);
            return;
        }
//        initUserInfo();
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
//                .timeout(3, TimeUnit.SECONDS)
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfo) {

//                        if (RxDataUtils.isNullString(userInfo.getInvitationCode())) {
//                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InvitationCodeActivity.class);
//                        } else
                        if (userInfo.getAge() == 0) {
                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), InputInfoActivity.class);
                        } else {
                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), MainActivity.class);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);

                    }
                });
    }


    @Override
    public void initRxBus2() {

    }
}
