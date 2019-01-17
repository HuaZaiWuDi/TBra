package com.wesmartclothing.tbra.ui.login;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxManager;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.RxToast;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.service.LocationIntentService;
import com.wesmartclothing.tbra.tools.RxComposeTools;
import com.wesmartclothing.tbra.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class LocationActivity extends BaseActivity {

    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.img_location)
    ImageView mImgLocation;
    @BindView(R.id.tv_location)
    TextView mTvLocation;
    @BindView(R.id.tv_nextStep)
    TextView mTvNextStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public int layoutId() {
        return R.layout.activity_location;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initTitle(mRxTitle);
        //跳过
        mRxTitle.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInfo(true);
            }
        });
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(Address.class)
                .compose(RxComposeUtils.<Address>bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<Address>() {
                    @Override
                    protected void _onNext(Address address) {
                        if (address != null) {
                            mTvLocation.setVisibility(View.VISIBLE);
                            mTvLocation.setText(address.getAdminArea() + address.getLocality());
                            WaitDialog.dismiss();
                            mTvNextStep.setEnabled(true);
                            mTvNextStep.setAlpha(1f);
                            if (InputInfoActivity.sInfoBean != null) {
                                InputInfoActivity.sInfoBean.setCountry("中国");
                                InputInfoActivity.sInfoBean.setProvince(address.getAdminArea());
                                InputInfoActivity.sInfoBean.setCity(address.getLocality());
                            }
                        }
                    }
                });
    }

    private void saveUserInfo(boolean isSkip) {
        if (isSkip) {
            InputInfoActivity.sInfoBean.setCountry("");
            InputInfoActivity.sInfoBean.setProvince("");
            InputInfoActivity.sInfoBean.setCity("");
        }
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().saveUserInfo(InputInfoActivity.sInfoBean),
                lifecycleSubject)
                .compose(RxComposeTools.<String>showDialog(mContext))
                .subscribe(new RxNetSubscriber<String>() {
                    @Override
                    protected void _onNext(String s) {
                        InputInfoActivity.sInfoBean = null;
                        //跳转扫描界面
//                        Bundle bundle = new Bundle();
//                        bundle.putBoolean(Key.BUNDLE_FORCE_BIND, false);
//                        RxActivityUtils.skipActivity(mContext, AddDeviceActivity.class, bundle);
                        RxActivityUtils.skipActivity(mContext, MainActivity.class);
                    }

                    @Override
                    protected void _onError(String error, int code) {
                        RxToast.error(error, code);
                    }
                });
    }


    @OnClick({R.id.tv_nextStep, R.id.img_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_nextStep:
                saveUserInfo(false);
                break;
            case R.id.img_location:
                WaitDialog.show(mContext, "正在定位");
                startService(new Intent(this, LocationIntentService.class));
                break;
        }
    }
}