package com.wesmartclothing.tbra.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kongzue.dialog.v2.WaitDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.dialog.RxDialogSureCancel;
import com.vondear.rxtools.view.layout.RxEditText;
import com.vondear.rxtools.view.layout.RxImageView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.BottomTabItem;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.entity.LoginResult;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.LoginSuccessUtils;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.ui.main.mine.ResetPwdActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.layout_register)
    CardView mLayoutRegister;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;
    @BindView(R.id.edit_pwd_code)
    RxEditText mEditPwdCode;
    @BindView(R.id.tv_pwd)
    RxTextView mTvPwd;
    @BindView(R.id.tv_code)
    RxTextView mTvCode;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_forgetPwd)
    TextView mTvForgetPwd;
    @BindView(R.id.img_QQ)
    RxImageView mImgQQ;
    @BindView(R.id.img_WB)
    RxImageView mImgWB;
    @BindView(R.id.img_WX)
    RxImageView mImgWX;


    private String phone, code, password;
    private int lastInoutType = 129;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initStatusBar() {
        StatusBarUtils.from(mActivity)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitleTab();
        initEditText();
        RxTextUtils.getBuilder("没有账号？")
                .append("立即注册")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.font_475669))
                .into(mTvRegister);

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

    private void initEditText() {
        mEditPhone.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone = charSequence.toString();
                vCodeState();
                loginBtnState();
            }
        });
        mEditPwdCode.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mCommonTabLayout.getCurrentTab() == 0) {
                    password = charSequence.toString();
                } else {
                    code = charSequence.toString();
                }
                loginBtnState();
            }
        });

        mEditPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !RxRegUtils.isMobileExact(phone)) {
                    RxToast.warning(getString(R.string.phoneError));
                }
            }
        });
    }

    private void initTitleTab() {
        ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
        mBottomTabItems.add(new BottomTabItem("密码登录"));
        mBottomTabItems.add(new BottomTabItem("验证码登录"));

        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchState(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

    }

    private void switchState(int position) {
        if (position == 0) {//密码登录
            mEditPwdCode.setText(password);
            mEditPwdCode.setHint("请输入密码");
            if (lastInoutType == 144) {
                mEditPwdCode.setInputType(144);
            } else {
                mEditPwdCode.setInputType(129);
            }
            mTvPwd.setVisibility(View.VISIBLE);
            mTvCode.setVisibility(View.GONE);
        } else {//验证码登录
            mEditPwdCode.setText(code);
            mEditPwdCode.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mEditPwdCode.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)}); //最大输入长度
            mEditPwdCode.setHint("请输入验证码");
            mTvPwd.setVisibility(View.GONE);
            mTvCode.setVisibility(View.VISIBLE);
        }
    }


    private void loginBtnState() {
        boolean complete = false;
        if (mCommonTabLayout.getCurrentTab() == 0 &&
                !RxDataUtils.isNullString(phone) && !RxDataUtils.isNullString(password)) {
            complete = true;
        } else if (!RxDataUtils.isNullString(phone) && !RxDataUtils.isNullString(code)) {
            complete = true;
        } else {
            complete = false;
        }
        if (complete == mTvLogin.isEnabled()) {
            return;
        }
        mTvLogin.setEnabled(complete);
        mTvLogin.setAlpha(complete ? 1f : 0.6f);
    }

    private void vCodeState() {
//        if (mCommonTabLayout.getCurrentTab() == 1 && RxRegUtils.isMobileExact(phone)) {
//            boolean complete = RxRegUtils.isMobileExact(phone);
//            mTvCode.setEnabled(complete);
//            mTvCode.setAlpha(0.6f);
//        }
    }

    private void switchPwdState() {
        RxLogUtils.d("输入模式：" + mEditPwdCode.getInputType());
        if (mEditPwdCode.getInputType() == 129) {
            mTvPwd.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye));
            mEditPwdCode.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEditPwdCode.setInputType(129);
            mTvPwd.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye_close));
        }
        lastInoutType = mEditPwdCode.getInputType();
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    private void getVCode() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().sendCode(phone, null),
                lifecycleSubject)
                .doOnSubscribe(disposable -> RxUtils.countDown(mTvCode, 60, 1, getString(R.string.getVCode)))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxLogUtils.d("验证码：" + s);
                        if (BuildConfig.DEBUG)
                            mEditPwdCode.setText(s);
                        RxToast.success(getString(R.string.SMSSended));
                        mEditPwdCode.setFocusable(true);
                        mEditPwdCode.setFocusableInTouchMode(true);
                        mEditPwdCode.requestFocus();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }


    private void loginOther(final LoginResult result) {
        if (result == null) return;
        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                        .outerLogin(result.openId, result.nickname, result.imageUrl, result.userType)
                , lifecycleSubject)
                .compose(RxComposeTools.<LoginInfoBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<LoginInfoBean>() {
                    @Override
                    protected void _onNext(LoginInfoBean bean) {
                        if (!bean.isSuccess()) {
                            RxToast.normal("登录成功，未绑定手机号");
                            //!success意味着没有绑定手机号码需要跳转到绑定手机号界面
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(Key.BUNDLE_OTHER_LOGIN_INFO, result);
                            RxActivityUtils.skipActivity(mContext, VerifyPhoneActivity.class, bundle);
                        } else {
                            RxToast.normal("登录成功");
                            new LoginSuccessUtils(bean, lifecycleSubject);
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    private void login() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (mCommonTabLayout.getCurrentTab() == 0 && !RxRegUtils.isPassword(password)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }
        if (mCommonTabLayout.getCurrentTab() == 1 && !RxRegUtils.isVCode(code)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        RxManager.getInstance().doNetSubscribe(
                mCommonTabLayout.getCurrentTab() == 0 ?
                        NetManager.getApiService().pwdLogin(phone, RxEncryptUtils.encryptMD5ToString(password)) :
                        NetManager.getApiService().login(phone, code), lifecycleSubject)
                .compose(RxComposeTools.<LoginInfoBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<LoginInfoBean>() {
                    @Override
                    protected void _onNext(LoginInfoBean s) {
                        RxToast.normal("登录成功");
                        new LoginSuccessUtils(s, lifecycleSubject);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        if (code == 10021) {//未设置登录密码
                            showDialog2settingPassword();
                        } else {
                            RxToast.error(error);
                        }
                    }
                });
    }

    private void showDialog2settingPassword() {
        RxDialogSureCancel rxDialog = new RxDialogSureCancel(mContext)
                .setContent("该手机号还未设置密码")
                .setSure("设置密码")
                .setSureListener(v -> {
                    //进入设置密码流程，跳转验证手机号
//                        RxActivityUtils.skipActivity(mActivity, VerificationPhoneActivity.class);
                })
                .setCancel("验证码登录")
                .setCancelListener(v -> mCommonTabLayout.setCurrentTab(1));
        rxDialog.show();
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
            Set<String> strings = map.keySet();
            for (String s : strings) {
                RxLogUtils.d("s: " + s + "--value" + map.get(s));
            }

            loginOther(new LoginResult(map, share_media));
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


    @OnClick({R.id.tv_register, R.id.tv_code, R.id.tv_pwd, R.id.tv_login, R.id.tv_forgetPwd, R.id.img_QQ, R.id.img_WB, R.id.img_WX})
    public void onViewClicked(View view) {
        SHARE_MEDIA media = SHARE_MEDIA.QQ;
        UMShareAPI umShareAPI = UMShareAPI.get(mContext);
        switch (view.getId()) {
            case R.id.tv_register:
                RxActivityUtils.skipActivity(mContext, RegisterActivity.class);
                break;
            case R.id.tv_code:
                getVCode();
                break;
            case R.id.tv_pwd:
                switchPwdState();
                break;
            case R.id.tv_login:
                login();
                break;
            case R.id.tv_forgetPwd:
                RxActivityUtils.skipActivity(mContext, ResetPwdActivity.class);
                break;
            case R.id.img_QQ:
                media = SHARE_MEDIA.QQ;
                umShareAPI.getPlatformInfo(mActivity, media, mUMAuthListener);
                break;
            case R.id.img_WB:
                media = SHARE_MEDIA.SINA;
                umShareAPI.getPlatformInfo(mActivity, media, mUMAuthListener);
                break;
            case R.id.img_WX:
                media = SHARE_MEDIA.WEIXIN;
                umShareAPI.getPlatformInfo(mActivity, media, mUMAuthListener);
                break;
        }
    }
}
