package com.wesmartclothing.tbra.ui.main;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxAnimationUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxNetSubscriber;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.vondear.rxtools.view.cardview.CardView;
import com.vondear.rxtools.view.layout.RxImageView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.entity.BottomTabItem;
import com.wesmartclothing.tbra.entity.GidBean;
import com.wesmartclothing.tbra.entity.NotifyDataBean;
import com.wesmartclothing.tbra.entity.PointDataBean;
import com.wesmartclothing.tbra.entity.UserInfoBean;
import com.wesmartclothing.tbra.entity.rxbus.RefreshUserInfoBus;
import com.wesmartclothing.tbra.net.NetManager;
import com.wesmartclothing.tbra.net.RxManager;
import com.wesmartclothing.tbra.service.BleService;
import com.wesmartclothing.tbra.tools.GlideImageLoader;
import com.wesmartclothing.tbra.tools.jpush.JPushUtils;
import com.wesmartclothing.tbra.ui.guide.SplashActivity;
import com.wesmartclothing.tbra.ui.main.home.ErrorPointActivity;
import com.wesmartclothing.tbra.ui.main.home.HomeFragment;
import com.wesmartclothing.tbra.ui.main.mine.MessageActivity;
import com.wesmartclothing.tbra.ui.main.mine.MineFragment;
import com.wesmartclothing.tbra.ui.main.monitor.MonitorFragment;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.CacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {


    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.layoutTitle)
    CardView mLayoutTitle;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.img_message)
    ImageView mImgMessage;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.img_userImg)
    RxImageView mImgUserImg;


    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private int position = 1;
    private Intent serviceIntent;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initBundle(Bundle bundle) {
        initPush(bundle);
    }

    /**
     * 点击推送跳转界面
     *
     * @param bundle
     */
    private void initPush(Bundle bundle) {
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        RxLogUtils.d("点击通知：" + extra);
        NotifyDataBean notifyDataBean = JSON.parseObject(extra, NotifyDataBean.class);
        if (notifyDataBean == null) return;
        String openTarget = notifyDataBean.getOpenTarget();
        int type = notifyDataBean.getOperation();
        String gid = notifyDataBean.getGId();

        RxManager.getInstance().doNetSubscribe(
                NetManager.getApiService().warningInfoReaded(new GidBean(gid)),
                lifecycleSubject,
                "warningInfoReaded" + gid,
                PointDataBean.class,
                CacheStrategy.firstCache()
        ).subscribe(new RxNetSubscriber<PointDataBean>() {
            @Override
            protected void _onNext(PointDataBean pointDataBean) {
                Bundle bundle = new Bundle();
                bundle.putString(Key.BUNDLE_LATEST_TYPE, "warningInfoReaded" + gid);
                RxActivityUtils.skipActivity(mContext, ErrorPointActivity.class, bundle);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        RxLogUtils.e("加载：MainActivity：" + savedInstanceState);
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            RxActivityUtils.skipActivityAndFinish(mContext, SplashActivity.class);
        }
    }

    private void initFragment() {
        mFragments.clear();
        mFragments.add(MonitorFragment.getInstance());
        mFragments.add(HomeFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
    }

    @Override
    public void initViews() {
        initFragment();
        initBottomBar();
        setDefaultFragment();
        initSystemConfig();
        serviceIntent = new Intent(mContext, BleService.class);
        startService(serviceIntent);
    }


    @Override
    public void initNetData() {
        RxCache.getDefault().<UserInfoBean>load(SPKey.SP_UserInfo, UserInfoBean.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .map(new CacheResult.MapFunc<>())
                .subscribe(new RxNetSubscriber<UserInfoBean>() {
                    @Override
                    protected void _onNext(UserInfoBean userInfoBean) {
                        GlideImageLoader.getInstance().displayImage(mContext, userInfoBean.getAvatar(), R.mipmap.ic_default_avater, mImgUserImg);
                    }
                });
    }

    @Override
    public void initRxBus2() {
        RxBus.getInstance().register2(RefreshUserInfoBus.class)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxNetSubscriber<RefreshUserInfoBus>() {
                    @Override
                    protected void _onNext(RefreshUserInfoBus refreshUserInfoBus) {
                        initNetData();
                    }
                });
    }


    private void initBottomBar() {
        int[] imgs_unselect = {R.mipmap.nav_monitor, R.mipmap.nav_home,
                R.mipmap.nav_mine};
        int[] imgs_select = {R.mipmap.nav_monitor_select, R.mipmap.nav_home_select,
                R.mipmap.nav_mine_select};
        mBottomTabItems.clear();
        for (int i = 0; i < imgs_unselect.length; i++) {
            mBottomTabItems.add(new BottomTabItem(imgs_select[i], imgs_unselect[i], ""));
        }

        mCommonTabLayout.setTabData(mBottomTabItems);
        mCommonTabLayout.setCurrentTab(position);
        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(position);
                switchPage(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 1) {
                    JPushUtils.setStyleBasic(mActivity);
                }
            }
        });
    }

    private void switchPage(int position) {
//        mLayoutTitle.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
        if (position == 2) {
            RxAnimationUtils.animateHeight(RxUtils.dp2px(105), 0, mLayoutTitle);
        } else if (mLayoutTitle.getHeight() < RxUtils.dp2px(105)) {
            RxAnimationUtils.animateHeight(RxUtils.dp2px(95), RxUtils.dp2px(105), mLayoutTitle);
        }

        switch (position) {
            case 0:
                mTvTitle.setText("监测");
                break;
            case 1:
                mTvTitle.setText("首页");
                break;
            default:
                break;
        }
    }


    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.frameLayout, mFragments.get(0));
        transaction.add(R.id.frameLayout, mFragments.get(1));
        transaction.add(R.id.frameLayout, mFragments.get(2));
        transaction.hide(mFragments.get(0));
        transaction.hide(mFragments.get(1));
        transaction.hide(mFragments.get(2));
        transaction.show(mFragments.get(position));
        transaction.commit();
    }

    private void switchFragment(int index) {
        if (index != position) {
            Fragment fromFragment = mFragments.get(position);
            Fragment toFragment = mFragments.get(index);
            position = index;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            if (!toFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(fromFragment).add(R.id.frameLayout, toFragment)
                        .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(fromFragment).show(toFragment)
                        .commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
        }
    }

    private void initSystemConfig() {
        //判断是否有权限
        new RxPermissions(mActivity)
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxComposeUtils.<Permission>bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Permission>() {
                    @Override
                    protected void _onNext(Permission aBoolean) {
                        RxLogUtils.e("是否开启了权限：" + aBoolean);
                    }
                });

//        //判断是否关闭了通知栏权限
//        RxLogUtils.e("通知栏权限：" + NotificationManagerCompat.from(mContext).areNotificationsEnabled());
//        if (!NotificationManagerCompat.from(mContext).areNotificationsEnabled()) {
//            new RxDialogSureCancel(mContext)
//                    .setTitle("提示")
//                    .setContent("您的通知权限未开启，可能影响APP的正常使用")
//                    .setSure("现在去开启")
//                    .setSureListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            /**
//                             * 跳到通知栏设置界面
//                             * @param context
//                             */
//                            Intent localIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            localIntent.addCategory(Intent.CATEGORY_DEFAULT);
//                            localIntent.setData(Uri.parse("package:" + mContext.getPackageName()));
//                            mContext.startActivity(localIntent);
//                        }
//                    }).show();
//        }

    }

    @Override
    protected void onDestroy() {
        if (serviceIntent != null)
            stopService(serviceIntent);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @OnClick(R.id.img_message)
    public void onViewClicked() {
        RxActivityUtils.skipActivity(mContext, MessageActivity.class);
    }
}
