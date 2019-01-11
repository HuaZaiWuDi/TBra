package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.view.layout.RxImageView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseAcFragment;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Package com.wesmartclothing.tbra.ui.main.mine
 * @FileName MineFragment
 * @Date 2019/1/8 11:02
 * @Author JACK
 * @Describe TODO我的界面
 * @Project tbra
 */
public class MineFragment extends BaseAcFragment {

    @BindView(R.id.tv_userName)
    TextView mTvUserName;
    @BindView(R.id.tv_sign)
    TextView mTvSign;
    @BindView(R.id.tv_times)
    TextView mTvTimes;
    @BindView(R.id.tv_useCount)
    TextView mTvUseCount;
    @BindView(R.id.tv_warningCount)
    TextView mTvWarningCount;
    @BindView(R.id.layout_userInfo)
    CardView mLayoutUserInfo;
    @BindView(R.id.img_userImg)
    RxImageView mImgUserImg;
    @BindView(R.id.img_message)
    ImageView mImgMessage;
    @BindView(R.id.img_setting)
    ImageView mImgSetting;
    @BindView(R.id.tv_deviceCount)
    TextView mTvDeviceCount;
    @BindView(R.id.layout_device)
    CardView mLayoutDevice;
    @BindView(R.id.layout_warningSetting)
    RelativeLayout mLayoutWarningSetting;
    @BindView(R.id.layout_warningRecord)
    RelativeLayout mLayoutWarningRecord;
    @BindView(R.id.layout_relationPhone)
    RelativeLayout mLayoutRelationPhone;
    @BindView(R.id.layout_feedback)
    RelativeLayout mLayoutFeedback;
    @BindView(R.id.layout_shareApp)
    RelativeLayout mLayoutShareApp;
    @BindView(R.id.layout_aboutUs)
    RelativeLayout mLayoutAboutUs;
    Unbinder unbinder;

    public static MineFragment getInstance() {
        return new MineFragment();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    @OnClick({R.id.img_userImg, R.id.img_message, R.id.img_setting, R.id.layout_device, R.id.layout_warningSetting, R.id.layout_warningRecord, R.id.layout_relationPhone, R.id.layout_feedback, R.id.layout_shareApp, R.id.layout_aboutUs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_userImg:
                break;
            case R.id.img_message:
                break;
            case R.id.img_setting:
                break;
            case R.id.layout_device:
                break;
            case R.id.layout_warningSetting:
                break;
            case R.id.layout_warningRecord:
                break;
            case R.id.layout_relationPhone:
                break;
            case R.id.layout_feedback:
                break;
            case R.id.layout_shareApp:
                break;
            case R.id.layout_aboutUs:
                break;
        }
    }
}
