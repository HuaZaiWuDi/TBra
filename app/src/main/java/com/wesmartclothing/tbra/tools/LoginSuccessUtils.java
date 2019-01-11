package com.wesmartclothing.tbra.tools;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.lifecycyle.LifeCycleEvent;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.ui.main.MainActivity;

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
    private void initUserInfo(BehaviorSubject<LifeCycleEvent> lifecycleSubject) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().userInfo(), lifecycleSubject)
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean s) {
//                        SPUtils.put(SPKey.SP_UserInfo, s);


//                        SPUtils.put(SPKey.SP_scaleMAC, userInfo.getScalesMacAddr());
//                        SPUtils.put(SPKey.SP_clothingMAC, userInfo.getClothesMacAddr());


//                        if (!userInfo.isHasInviteCode()) {
//                            RxActivityUtils.skipActivityAndFinish(mContext, InvitationCodeActivity.class);
//                        } else
                        if (s.getAge() == 0) {
//                            RxActivityUtils.skipActivityAndFinish(RxActivityUtils.currentActivity(), UserInfoActivity.class);
                        } else {
                            RxActivityUtils.skipActivity(RxActivityUtils.currentActivity(), MainActivity.class);
                        }

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error);
                    }
                });
    }


}
