package com.wesmartclothing.tbra.ui.login;

import android.os.Bundle;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.interfaces.onEditTextChangeListener;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxRegUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.layout.RxEditText;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.ui.main.MainActivity;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;

import butterknife.BindView;
import butterknife.OnClick;

public class InvitationCodeActivity extends BaseActivity {

    public static final String REGEX_CODE = "^[A-Za-z0-9]{5}$";

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.edit_invitationCode)
    RxEditText mEditInvitationCode;
    @BindView(R.id.tv_verify)
    TextView mTvVerify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_invitation_code;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        initEditText();
    }

    private void initEditText() {
        mEditInvitationCode.addTextChangedListener(new onEditTextChangeListener() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                RxLogUtils.e("邀请码：" + charSequence);
                if (RxRegUtils.isMatch(REGEX_CODE, charSequence.toString())) {
                    mTvVerify.setAlpha(1f);
                    mTvVerify.setEnabled(true);
                } else {
                    mTvVerify.setAlpha(0.6f);
                    mTvVerify.setEnabled(false);
                }
            }
        });

    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    private void checkInvitation() {
        String invitation = mEditInvitationCode.getText().toString();

        RxManager.getInstance().doNetSubscribe(NetManager.getApiService()
                .verifyInvitationCode(invitation), lifecycleSubject)
                .compose(RxComposeTools.<Boolean>showDialog(mContext))
                .subscribe(new RxNetSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean b) {
                        if (b) {
                            RxCache.getDefault().load(SPKey.SP_UserInfo, UserInfoBean.class)
                                    .map(new CacheResult.MapFunc())
                                    .subscribe(new RxNetSubscriber<UserInfoBean>() {
                                        @Override
                                        protected void _onNext(UserInfoBean mUserInfo) {
                                            if (mUserInfo != null && mUserInfo.getAge() == 0) {
                                                RxActivityUtils.skipActivityAndFinish(mActivity, InputInfoActivity.class);
                                            } else {
                                                RxActivityUtils.skipActivityAndFinish(mActivity, MainActivity.class);
                                            }
                                        }
                                    });
                        } else {
                            RxToast.normal("邀请码无效，请正确输入有效邀请码");
                        }
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        super._onError(error, code);
                        RxToast.normal(error);
                    }
                });
    }


    @OnClick(R.id.tv_verify)
    public void onViewClicked() {
        checkInvitation();
    }
}
