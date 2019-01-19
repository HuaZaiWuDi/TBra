package com.wesmartclothing.tbra.ui.main.mine.relationphone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
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
import com.wesmartclothing.tbra.entity.RelateAccountListBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_niceName)
    RxEditText mEditNiceName;
    @BindView(R.id.edit_phone)
    RxEditText mEditPhone;
    @BindView(R.id.edit_code)
    RxEditText mEditCode;
    @BindView(R.id.tv_code)
    RxTextView mTvCode;
    @BindView(R.id.tv_addPhone)
    TextView mTvAddPhone;


    private String nickName, phone, code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_edit_phone;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initEditView();
    }

    private void initEditView() {
        mEditPhone.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                phone = charSequence.toString();
                checkBtn();
            }
        });
        mEditNiceName.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nickName = charSequence.toString();
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


        mEditPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && !RxRegUtils.isMobileExact(phone)) {
                    RxToast.warning(getString(R.string.phoneError));
                }
            }
        });
    }

    private void checkBtn() {
        boolean complete = !RxDataUtils.isNullString(phone)
                && !RxDataUtils.isNullString(code)
                && !RxDataUtils.isNullString(nickName);

        if (complete == mTvAddPhone.isEnabled()) {
            return;
        }
        mTvAddPhone.setEnabled(complete);
        mTvAddPhone.setAlpha(complete ? 1f : 0.6f);
    }

    private void getVCode() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().sendCode(phone, null),
                lifecycleSubject)
                .doOnSubscribe(disposable ->
                        RxUtils.countDown(mTvCode, 60, 1, getString(R.string.getVCode)))
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

    private void addRelationPhone() {
        if (!RxRegUtils.isMobileExact(phone)) {
            RxToast.warning(getString(R.string.phoneError));
            return;
        }
        if (!RxRegUtils.isVCode(code)) {
            RxToast.warning(getString(R.string.VCodeError));
            return;
        }
        RelateAccountListBean.ListBean mListBean = new RelateAccountListBean.ListBean();
        mListBean.setContactPhone(phone);
        mListBean.setUsingFlag(1);
        mListBean.setVerifyCode(code);
        mListBean.setContactName(nickName);
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().addRelateAccount(mListBean),
                lifecycleSubject
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        RxBus.getInstance().post(mListBean);
                        onBackPressed();
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

    @OnClick({R.id.tv_code, R.id.tv_addPhone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_code:
                getVCode();
                break;
            case R.id.tv_addPhone:
                addRelationPhone();
                break;
        }
    }
}
