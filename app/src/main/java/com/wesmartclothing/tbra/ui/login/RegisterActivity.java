package com.wesmartclothing.tbra.ui.login;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.base.BaseTitleWebActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.CustomClickUrlSpan;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.edit_code)
    RxEditText mEditCode;
    @BindView(R.id.tv_code)
    RxTextView mTvCode;
    @BindView(R.id.edit_pwd)
    RxEditText mEditPwd;
    @BindView(R.id.tv_pwd)
    RxTextView mTvPwd;
    @BindView(R.id.tv_register)
    TextView mTvRegister;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;
    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int layoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initEdit();
        initTitle(mRxTitle);
        initTextUrl();
    }

    private void initTextUrl() {
        mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());

        RxTextUtils.getBuilder("《威觅注册协议》")
                .setUrl(Key.WEB_URL_Registration_Agreement)
                .setClickSpan(new CustomClickUrlSpan(view -> {
                    BaseTitleWebActivity.startBaseWebAc(mContext, "注册协议", Key.WEB_URL_Registration_Agreement);
                }))
                .append("和")
                .append("《服务条款和隐私条款》")
                .setUrl(Key.WEB_URL_Implicit_Clause)
                .setClickSpan(new CustomClickUrlSpan(view -> {
                    BaseTitleWebActivity.startBaseWebAc(mContext,
                            "服务条款和隐私条款", Key.WEB_URL_Implicit_Clause);
                }))
                .into(mTvAgreement);
    }

    private void initEdit() {
        mEditPhone.setOnFocusChangeListener((view, b) -> {
            String phone = mEditPhone.getText().toString();
            if (!b && !RxRegUtils.isMobileExact(phone)) {
                RxToast.warning(getString(R.string.phoneError));
            }
        });
        mEditPhone.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginBtnState();
            }
        });
        mEditPwd.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginBtnState();
            }
        });
        mEditCode.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginBtnState();
            }
        });

    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    private void getVCode() {
        String phone = mEditPhone.getText().toString();
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
                            mEditCode.setText(s);
                        RxToast.success(getString(R.string.SMSSended));
                        mEditCode.setFocusable(true);
                        mEditCode.setFocusableInTouchMode(true);
                        mEditCode.requestFocus();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }

    private void switchPwdState() {
        RxLogUtils.d("输入模式：" + mEditPwd.getInputType());
        if (mEditPwd.getInputType() == 129) {
            mTvPwd.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye));
            mEditPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEditPwd.setInputType(129);
            mTvPwd.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye_close));
        }
    }

    private void loginBtnState() {
        boolean complete = !RxDataUtils.isNullString(mEditPhone.getText().toString())
                && !RxDataUtils.isNullString(mEditPwd.getText().toString())
                && !RxDataUtils.isNullString(mEditCode.getText().toString());
        if (complete == mTvRegister.isEnabled()) {
            return;
        }
        mTvRegister.setEnabled(complete);
        mTvRegister.setAlpha(complete ? 1f : 0.6f);
    }


    private void register() {
        String phone = mEditPhone.getText().toString();
        String vCode = mEditCode.getText().toString();
        String password = mEditPwd.getText().toString();
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(vCode)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        if (!RxRegUtils.isPassword(password)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().register(phone, vCode, RxEncryptUtils.encryptMD5ToString(password)),
                lifecycleSubject)
                .compose(RxComposeTools.<LoginInfoBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<LoginInfoBean>() {
                    @Override
                    protected void _onNext(LoginInfoBean s) {
                        RxLogUtils.d("验证码：" + s);
                        RxToast.success("注册成功");
                        onBackPressed();
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    @OnClick({R.id.tv_code, R.id.tv_pwd, R.id.tv_register, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                getVCode();
                break;
            case R.id.tv_pwd:
                switchPwdState();
                break;
            case R.id.tv_register:
                register();
                break;
            case R.id.tv_agreement:
                break;
        }
    }
}
