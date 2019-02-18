package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.base.BaseTitleWebActivity;
import com.wesmartclothing.tbra.ble.BleAPI;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.entity.BleDeviceInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.layoutTitle)
    CardView mLayoutTitle;
    @BindView(R.id.tv_clothingVersion)
    TextView mTvClothingVersion;
    @BindView(R.id.btn_update)
    RxTextView mBtnUpdate;
    @BindView(R.id.tv_appVersion)
    TextView mTvAppVersion;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.btn_reUpdate)
    RxTextView mBtnReUpdate;
    @BindView(R.id.layout_updateFail)
    LinearLayout mLayoutUpdateFail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        RxTextUtils.getBuilder("智裳科技 ")
                .append("服务条款和隐私条款")
                .setForegroundColor(getResources().getColor(R.color.red))
                .setUnderline()
                .into(mTvTip);
        mTvAppVersion.setText("软件版本号 v" + RxDeviceUtils.getAppVersionName());
        mTvClothingVersion.setText("固件版本号 v-\t-");
    }

    @Override
    public void initNetData() {
        initReadVersion();
    }

    private void initReadVersion() {
        BleAPI.getSettingInfo(new RxSubscriber<BleDeviceInfoBean>() {
            @Override
            protected void _onNext(BleDeviceInfoBean bleDeviceInfoBean) {

            }
        });
    }

    @Override
    public void initRxBus2() {

    }

    @OnClick({R.id.btn_update, R.id.tv_tip, R.id.btn_reUpdate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                break;
            case R.id.tv_tip:
                BaseTitleWebActivity.startBaseWebAc(mContext,
                        "服务条款和隐私条款", Key.WEB_URL_Implicit_Clause);
                break;
            case R.id.btn_reUpdate:
                mLayoutUpdateFail.setVisibility(View.GONE);
                break;
        }
    }
}
