package com.wesmartclothing.tbra.ui.main.mine;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxTextUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.vondear.rxtools.utils.dateUtils.RxFormat;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.layout.RxImageView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseAcFragment;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.UserCenterBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.RefreshUserInfoBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.tools.GlideImageLoader;
import com.wesmartclothing.tbra.ui.main.mine.relationphone.RelationPhoneActivity;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

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
        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().userCenter(),
                lifecycleSubject,
                "userCenter",
                UserCenterBean.class,
                CacheStrategy.firstRemote()
        ).subscribe(new RxNetSubscriber<UserCenterBean>() {
            @Override
            protected void _onNext(UserCenterBean bean) {
                setTextView(bean);
            }

            @Override
            protected void _onError(String error, int code) {
                super._onError(error, code);
                RxToast.normal(error);
            }
        });
        syncUserInfo();
    }

    private void syncUserInfo() {
        RxCache.getDefault().<UserInfoBean>load(SPKey.SP_UserInfo, UserInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfoBean) {
                        mTvUserName.setText(userInfoBean.getUserName());
                        mTvSign.setText(userInfoBean.getSignature());
                        if (!RxDataUtils.isEmpty(userInfoBean.getMacAddrList()))
                            mTvDeviceCount.setText(userInfoBean.getMacAddrList().get(0));
                        else {
                            mTvDeviceCount.setText(SPUtils.getString(SPKey.SP_BIND_DEVICE, ""));
                        }
                        GlideImageLoader.getInstance().displayImage(mContext, userInfoBean.getAvatar(), mImgUserImg);
                    }
                });
    }


    private void setTextView(UserCenterBean bean) {
        RxTextUtils.getBuilder("使用天数\n")
                .append(bean.getTotalDays() + "")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.font_475669))
                .setProportion(1.6f)
                .into(mTvUseCount);

        RxTextUtils.getBuilder("告警天数\n")
                .append(bean.getWarningDays() + "")
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.font_475669))
                .setProportion(1.6f)
                .into(mTvWarningCount);

        RxTextUtils.getBuilder("累计监测时长\n")
                .append(RxFormat.setSec2HM(bean.getTotalCount() * 5 * 60))
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.font_475669))
                .setProportion(1.6f)
                .into(mTvTimes);
    }


    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(RefreshUserInfoBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<RefreshUserInfoBus>() {
                    @Override
                    protected void _onNext(RefreshUserInfoBus refreshUserInfoBus) {
                        syncUserInfo();
                    }
                });
    }


    @OnClick({R.id.img_userImg, R.id.img_message, R.id.img_setting, R.id.layout_device, R.id.layout_warningSetting, R.id.layout_warningRecord, R.id.layout_relationPhone, R.id.layout_feedback, R.id.layout_shareApp, R.id.layout_aboutUs})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_userImg:
                RxActivityUtils.skipActivity(mContext, UserInfoActivity.class);
                break;
            case R.id.img_message:
                RxActivityUtils.skipActivity(mContext, MessageActivity.class);
                break;
            case R.id.img_setting:
                RxActivityUtils.skipActivity(mContext, SettingActivity.class);
                break;
            case R.id.layout_device:
                break;
            case R.id.layout_warningSetting:
                RxActivityUtils.skipActivity(mContext, WarningSettingActivity.class);
                break;
            case R.id.layout_warningRecord:
                RxActivityUtils.skipActivity(mContext, WarningRecordActivity.class);
                break;
            case R.id.layout_relationPhone:
                RxActivityUtils.skipActivity(mContext, RelationPhoneActivity.class);
                break;
            case R.id.layout_feedback:
                RxActivityUtils.skipActivity(mContext, FeedbackActivity.class);
                break;
            case R.id.layout_shareApp:
                break;
            case R.id.layout_aboutUs:
                RxActivityUtils.skipActivity(mContext, AboutUsActivity.class);
                break;
        }
    }
}
