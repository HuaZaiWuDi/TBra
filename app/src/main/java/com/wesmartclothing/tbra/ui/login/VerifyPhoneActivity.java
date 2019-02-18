package com.wesmartclothing.tbra.ui.login;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxDataUtils;
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
import com.wesmartclothing.tbra.entity.LoginResult;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.CustomClickUrlSpan;
import com.wesmartclothing.tbra.tools.LoginSuccessUtils;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VerifyPhoneActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;
    @BindView(R.id.edit_code)
    RxEditText mEditCode;
    @BindView(R.id.tv_code)
    RxTextView mTvCode;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
    @BindView(R.id.tv_agreement)
    TextView mTvAgreement;

    private String phone, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_verify_phone;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initEdit();

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
    }

    private void checkBtn() {
        boolean complete = !RxDataUtils.isNullString(phone)
                && !RxDataUtils.isNullString(code);

        if (complete == mTvComplete.isEnabled()) {
            return;
        }
        mTvComplete.setEnabled(complete);
        mTvComplete.setAlpha(complete ? 1f : 0.6f);
    }


    private void bindPhone() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(code)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        LoginResult result = (LoginResult) getIntent().getSerializableExtra(Key.BUNDLE_OTHER_LOGIN_INFO);
        if (result == null) return;
        RxLogUtils.e("登录信息：" + result.toString());
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().phoneBind(phone, code, result.openId, result.nickname, result.imageUrl, result.userType),
                lifecycleSubject)
                .compose(RxComposeTools.<LoginInfoBean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<LoginInfoBean>() {
                    @Override
                    protected void _onNext(LoginInfoBean s) {
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


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @OnClick({R.id.tv_code, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                getVCode();
                break;
            case R.id.tv_complete:
                bindPhone();
                break;
        }
    }
}
