package com.wesmartclothing.tbra.ui.login;

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
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.LoginSuccessUtils;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
    RxTextView mTvRegister;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;
    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;


    private String phone, code, password;

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
                        new LoginSuccessUtils(s, lifecycleSubject);
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
