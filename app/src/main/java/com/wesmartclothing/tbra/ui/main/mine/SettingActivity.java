package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.cardview.CardView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.ui.login.LoginActivity;
import com.wesmartclothing.tbra.ui.login.ResetPwdActivity;
import com.zchu.rxcache.RxCache;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.layout_account)
    CardView mLayoutAccount;
    @BindView(R.id.layout_resetPwd)
    CardView mLayoutResetPwd;
    @BindView(R.id.tv_cacheSize)
    TextView mTvCacheSize;
    @BindView(R.id.layout_clearCache)
    CardView mLayoutClearCache;
    @BindView(R.id.tv_logout)
    TextView mTvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        mTvCacheSize.setText(RxFileUtils.getTotalCacheSize(mContext.getApplicationContext()));
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    private void logout() {
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().logout(),
                lifecycleSubject
        )
                .compose(RxComposeTools.showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        try {
                            RxCache.getDefault().clear2();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        SPUtils.put(SPKey.SP_UserId, "");
                        SPUtils.put(SPKey.SP_BIND_DEVICE, "");

                        RxActivityUtils.skipActivityAndFinishAll(mActivity, LoginActivity.class);
                    }
                });
    }


    @OnClick({R.id.layout_account, R.id.layout_resetPwd, R.id.layout_clearCache, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_account:
                RxActivityUtils.skipActivity(mContext, AccountManagerActivity.class);
                break;
            case R.id.layout_resetPwd:
                RxActivityUtils.skipActivity(mContext, ResetPwdActivity.class);
                break;
            case R.id.layout_clearCache:
                SelectDialog.show(mContext, "提示", "是否清除全部缓存", "清除", (dialog, i) -> {
                    RxFileUtils.clearAllCache(mContext.getApplicationContext());
                    mTvCacheSize.setText("0MB");
                });
                break;
            case R.id.tv_logout:
                SelectDialog.show(mContext, "提示", "是否退出登录", "退出", (dialog, i) -> {
                    logout();
                });
                break;
        }
    }
}
