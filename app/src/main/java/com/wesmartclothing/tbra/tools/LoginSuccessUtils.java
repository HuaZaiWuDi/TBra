package com.wesmartclothing.tbra.tools;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.lifecycyle.LifeCycleEvent;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.ui.login.InputInfoActivity;
import com.wesmartclothing.tbra.ui.login.InvitationCodeActivity;
import com.wesmartclothing.tbra.ui.main.MainActivity;
import com.zchu.rxcache.stategy.CacheStrategy;

import io.reactivex.subjects.BehaviorSubject;


/**
 * Created by jk on 2018/7/5.
 */

public class LoginSuccessUtils {


    public LoginSuccessUtils(LoginInfoBean bean, BehaviorSubject<LifeCycleEvent> lifecycleSubject) {

        SPUtils.put(SPKey.SP_UserId, bean.getUserId());
        SPUtils.put(SPKey.SP_token, bean.getToken());


        initUserInfo(lifecycleSubject);
    }


    //获取用户信息
    public void initUserInfo(BehaviorSubject<LifeCycleEvent> lifecycleSubject) {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userInfo(),
                lifecycleSubject,
                SPKey.SP_UserInfo,
                UserInfoBean.class,
                CacheStrategy.firstRemote()
        )
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfo) {

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
                        RxToast.error(error);
                    }
                });
    }


}
