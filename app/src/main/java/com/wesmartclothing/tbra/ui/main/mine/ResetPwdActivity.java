package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxEncryptUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.LoginInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.tools.LoginSuccessUtils;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;
    @BindView(R.id.edit_code)
    RxEditText mEditCode;
    @BindView(R.id.tv_code)
    RxTextView mTvCode;
    @BindView(R.id.edit_pwd)
    RxEditText mEditPwd;
    @BindView(R.id.tv_pwd)
    RxTextView mTvPwd;
    @BindView(R.id.edit_pwd_again)
    RxEditText mEditPwdAgain;
    @BindView(R.id.tv_pwd_again)
    RxTextView mTvPwdAgain;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;


    private String phone, code, password, againPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initEdit();
    }

    private void initEdit() {
        mEditPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                String phone = mEditPhone.getText().toString();
                if (!b && !RxRegUtils.isMobileExact(phone)) {
                    RxToast.warning(getString(R.string.phoneError));
                }
            }
        });
        mEditPwdAgain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !RxDataUtils.isNullString(password)
                        && !password.equals(againPassword)) {
                    RxToast.normal("两次密码输入不一致");
                }
            }
        });

        mEditPhone.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone = charSequence.toString();
                checkBtn();
            }
        });
        mEditCode.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                code = charSequence.toString();
                checkBtn();
            }
        });
        mEditPwd.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
                checkBtn();
            }
        });
        mEditPwdAgain.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                againPassword = charSequence.toString();
                checkBtn();
            }
        });
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    private void checkBtn() {
        boolean complete = !RxDataUtils.isNullString(phone)
                && !RxDataUtils.isNullString(code)
                && !RxDataUtils.isNullString(password)
                && !RxDataUtils.isNullString(againPassword);

        if (complete == mTvComplete.isEnabled()) {
            return;
        }
        mTvComplete.setEnabled(complete);
        mTvComplete.setAlpha(complete ? 1f : 0.6f);
    }


    private void restPassWord() {
        if (!RxRegUtils.isPassword(password) || !RxRegUtils.isPassword(againPassword)) {
            RxToast.warning(getString(R.string.passwordError));
            return;
        }
        if (!password.equals(againPassword)) {
            RxToast.warning(getString(R.string.password_not_same));
            return;
        }
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(code)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService().resetPassword(phone,
                RxEncryptUtils.encryptMD5ToString(password), code),
                lifecycleSubject)
                .compose(RxComposeTools.<LoginInfoBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<LoginInfoBean>() {
                    @Override
                    protected void _onNext(LoginInfoBean s) {
                        RxToast.success(getString(R.string.settingPasswordSuccess));
                        new LoginSuccessUtils(s, lifecycleSubject);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }

                });
    }

    private void getVCode() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().sendCode(phone, null),
                lifecycleSubject)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        RxUtils.countDown(mTvCode, 60, 1, getString(R.string.getVCode));
                    }
                })
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

    private void againSwitchPwdState() {
        RxLogUtils.d("输入模式：" + mEditPwd.getInputType());
        if (mEditPwdAgain.getInputType() == 129) {
            mTvPwdAgain.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye));
            mEditPwdAgain.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEditPwdAgain.setInputType(129);
            mTvPwdAgain.getHelper().setIconNormal(ContextCompat.getDrawable(mContext, R.mipmap.ic_eye_close));
        }
    }

    @OnClick({R.id.tv_code, R.id.tv_pwd, R.id.tv_pwd_again, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                getVCode();
                break;
            case R.id.tv_pwd:
                switchPwdState();
                break;
            case R.id.tv_pwd_again:
                againSwitchPwdState();
                break;
            case R.id.tv_complete:
                restPassWord();
                break;
        }
    }
}
