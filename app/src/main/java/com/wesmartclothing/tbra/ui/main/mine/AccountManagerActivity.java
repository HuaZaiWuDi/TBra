package com.wesmartclothing.tbra.ui.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.kongzue.dialog.v2.SelectDialog;
import com.kongzue.dialog.v2.WaitDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.LoginResult;
import com.wesmartclothing.tbra.entity.OtherLoginBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountManagerActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.layout_phone)
    LinearLayout mLayoutPhone;
    @BindView(R.id.tv_wechatName)
    TextView mTvWechatName;
    @BindView(R.id.iv_wechat_state)
    ImageView mIvWechatState;
    @BindView(R.id.layout_wechat)
    RelativeLayout mLayoutWechat;
    @BindView(R.id.tv_QQName)
    TextView mTvQQName;
    @BindView(R.id.iv_QQ_state)
    ImageView mIvQQState;
    @BindView(R.id.layout_QQ)
    RelativeLayout mLayoutQQ;
    @BindView(R.id.tv_weiboName)
    TextView mTvWeiboName;
    @BindView(R.id.iv_weibo_state)
    ImageView mIvWeiboState;
    @BindView(R.id.layout_weibo)
    RelativeLayout mLayoutWeibo;


    public interface SwitchBindListener {
        void complete(String result);
    }

    private SwitchBindListener mSwitchBindListener;
    private boolean wechatIsBind, QQIsBind, weiboIsBind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_account_manager;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
    }

    @Override
    public void initNetData() {
        RxCache.getDefault().<UserInfoBean>load(SPKey.SP_UserInfo, UserInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfoBean) {
                        mTvPhone.setText(userInfoBean.getPhone());
                    }
                });
        otherData();
    }


    private UMAuthListener mUMAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            RxLogUtils.d("开始登录");
            WaitDialog.show(mContext, "正在登陆");
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            RxLogUtils.d("login:onComplete: ");
            WaitDialog.dismiss();
            if (!RxDataUtils.isEmpty(map)) {
                Set<String> strings = map.keySet();
                for (String s : strings) {
                    RxLogUtils.d("s: " + s + "--value" + map.get(s));
                }

                bindOther(new LoginResult(map, share_media));
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            RxLogUtils.e("登录失败", throwable);
            RxToast.error("登录失败");
            WaitDialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            RxLogUtils.e("登录取消");
            RxToast.error("登录取消");
            WaitDialog.dismiss();
        }
    };

    @Override
    public void initRxBus2() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }


    private void bindOther(final LoginResult result) {
        OtherLoginBean bean = new OtherLoginBean();
        bean.setAvatar(result.imageUrl);
        bean.setUserType(result.userType);
        bean.setUserName(result.nickname);
        bean.setOuterId(result.openId);

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().bindingOuterInfo(bean),
                lifecycleSubject
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.normal("绑定成功", 3000);
                        if (mSwitchBindListener != null) {
                            mSwitchBindListener.complete(result.getNickname());
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
                    }
                });
    }

    private void unbindOther(final SHARE_MEDIA otherType) {
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().unbindOuterInfo(typeTransformation(otherType)),
                lifecycleSubject)
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxToast.normal("解绑成功", 3000);
                        if (mSwitchBindListener != null) {
                            mSwitchBindListener.complete("");
                        }
                        UMShareAPI.get(mContext).deleteOauth(mActivity, otherType, mUMAuthListener);

                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }

    //获取第三方登录信息
    private void otherData() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().fetchBindingOuterInfo(),
                lifecycleSubject,
                "fetchBindingOuterInfo",
                new TypeToken<List<OtherLoginBean>>() {
                }.getType(),
                CacheStrategy.firstRemote()
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<List<OtherLoginBean>>() {
                    @Override
                    protected void _onNext(List<OtherLoginBean> beans) {

                        for (int i = 0; i < beans.size(); i++) {
                            OtherLoginBean loginBean = beans.get(i);
                            switch (loginBean.getUserType()) {
                                case Key.LoginType_WEXIN:
                                    wechatIsBind = true;
                                    mTvWechatName.setText(loginBean.getUserName());
                                    mIvWechatState.setImageResource(R.mipmap.ic_account_bind);
                                    break;
                                case Key.LoginType_QQ:
                                    QQIsBind = true;
                                    mTvQQName.setText(loginBean.getUserName());
                                    mIvQQState.setImageResource(R.mipmap.ic_account_bind);
                                    break;
                                case Key.LoginType_WEIBO:
                                    weiboIsBind = true;
                                    mTvWeiboName.setText(loginBean.getUserName());
                                    mIvWeiboState.setImageResource(R.mipmap.ic_account_bind);
                                    break;
                            }
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.normal(error, code);
                    }
                });
    }

    private void switchBind(boolean isBind, SHARE_MEDIA type, SwitchBindListener switchBindListener) {
        mSwitchBindListener = switchBindListener;
        if (isBind) {
            SelectDialog.show(mContext, "提示", "解绑后将不能作为登录方式，确定解除微信账号绑定么？",
                    "解除绑定", (dialogInterface, i) -> {
                        unbindOther(type);
                    });
        } else {
            UMShareAPI umShareAPI = UMShareAPI.get(mContext);
            umShareAPI.getPlatformInfo(mActivity, type, mUMAuthListener);
        }
    }

    private String typeTransformation(SHARE_MEDIA type) {
        String otherType = "";
        if (type == SHARE_MEDIA.WEIXIN) {
            otherType = Key.LoginType_WEXIN;
        } else if (type == SHARE_MEDIA.QQ) {
            otherType = Key.LoginType_QQ;
        } else if (type == SHARE_MEDIA.SINA) {
            otherType = Key.LoginType_WEIBO;
        }
        return otherType;
    }


    @OnClick({R.id.layout_wechat, R.id.layout_QQ, R.id.layout_weibo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_wechat:
                switchBind(wechatIsBind, SHARE_MEDIA.WEIXIN, result -> {
                    wechatIsBind = !wechatIsBind;
                    mIvWechatState.setImageResource(wechatIsBind ? R.mipmap.ic_account_bind : R.mipmap.ic_account_unbind);
                    mTvWechatName.setText(result);
                });
                break;
            case R.id.layout_QQ:
                switchBind(QQIsBind, SHARE_MEDIA.QQ, result -> {
                    QQIsBind = !QQIsBind;
                    mIvQQState.setImageResource(QQIsBind ? R.mipmap.ic_account_bind : R.mipmap.ic_account_unbind);
                    mTvQQName.setText(result);
                });
                break;
            case R.id.layout_weibo:
                switchBind(weiboIsBind, SHARE_MEDIA.SINA, result -> {
                    weiboIsBind = !weiboIsBind;
                    mIvWeiboState.setImageResource(weiboIsBind ? R.mipmap.ic_account_bind : R.mipmap.ic_account_unbind);
                    mTvWeiboName.setText(result);
                });
                break;
        }
    }
}
