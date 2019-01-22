package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;

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
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @OnClick({R.id.layout_account, R.id.layout_resetPwd, R.id.layout_clearCache, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_account:
                break;
            case R.id.layout_resetPwd:
                RxActivityUtils.skipActivity(mContext, ResetPwdActivity.class);
                break;
            case R.id.layout_clearCache:
                break;
            case R.id.tv_logout:
                break;
        }
    }
}
