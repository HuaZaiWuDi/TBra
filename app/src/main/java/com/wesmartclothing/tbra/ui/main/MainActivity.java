package com.wesmartclothing.tbra.ui.main;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.entity.BottomTabItem;
import com.wesmartclothing.tbra.net.ServiceAPI;
import com.wesmartclothing.tbra.ui.guide.SplashActivity;
import com.wesmartclothing.tbra.ui.main.home.HomeFragment;
import com.wesmartclothing.tbra.ui.main.mine.MineFragment;
import com.wesmartclothing.tbra.ui.main.monitor.MonitorFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {


    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
    @BindView(R.id.commonTabLayout)
    CommonTabLayout mCommonTabLayout;
    @BindView(R.id.layoutTitle)
    RelativeLayout mLayoutTitle;


    private ArrayList<CustomTabEntity> mBottomTabItems = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();
    private int position = 0;

    @Override
    public void initStatusBar() {
//        super.initStatusBar();
        StatusBarUtils.from(mActivity)
                .setTransparentStatusbar(true)
                .setLightStatusBar(true)
                .process();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        RxLogUtils.e("加载：MainActivity：" + savedInstanceState);
        //防止应用处于后台，被杀死，再次唤醒时，重走启动流程
        if (savedInstanceState != null && mActivity != null) {
            RxActivityUtils.skipActivityAndFinish(mActivity, SplashActivity.class);
            return;
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initViews() {
        initFragment();
        initBottomBar();
        setDefaultFragment();
        initSystemConfig();
    }

    private void initFragment() {
        mFragments.clear();
        mFragments.add(MonitorFragment.getInstance());
        mFragments.add(HomeFragment.getInstance());
        mFragments.add(MineFragment.getInstance());
    }


    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }


    private void initBottomBar() {
        String[] tab_text = {getString(R.string.navMonitor), getString(R.string.navMain), getString(R.string.navMine)};
//        int[] imgs_unselect = {R.mipmap.icon_slimming_unselect, R.mipmap.icon_record,
//                R.mipmap.icon_find_unselect, R.mipmap.icon_mine_unselect};
//        int[] imgs_select = {R.mipmap.icon_slimming_select, R.mipmap.icon_record_unselect,
//                R.mipmap.icon_find_select, R.mipmap.icon_mine_select};
        mBottomTabItems.clear();
        for (int i = 0; i < tab_text.length; i++) {
            mBottomTabItems.add(new BottomTabItem(tab_text[i]));
        }

        mCommonTabLayout.setTabData(mBottomTabItems);

        mCommonTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                switchFragment(position);
                switchPage(position);
            }

            @Override
            public void onTabReselect(int position) {
                //双击我的按钮，出现切换网络界面，同时需要退出重新登录
                if (position == mFragments.size() - 1 && RxUtils.isFastClick(1000) && BuildConfig.DEBUG) {
                    new QMUIBottomSheet.BottomListSheetBuilder(mContext)
                            .addItem(ServiceAPI.BASE_URL_192)
                            .addItem(ServiceAPI.BASE_URL_208)
                            .addItem(ServiceAPI.BASE_URL_125)
                            .addItem(ServiceAPI.BASE_URL_mix)
                            .addItem(ServiceAPI.BASE_RELEASE)
                            .addItem(ServiceAPI.BASE_DEBUG)
                            .setTitle("修改网络需要重启应用，提示网络错误，需要重新登录")
                            .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                                @Override
                                public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                                    dialog.dismiss();
//                                    SPUtils.put(SPKey.SP_BSER_URL, tag);
//                                    ServiceAPI.switchURL(tag);
                                }
                            })
                            .build()
                            .show();
                }
            }
        });
    }

    private void switchPage(int position) {
        mLayoutTitle.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
        switch (position) {
            case 0:

                break;
            case 1:
                break;
            default:
                break;
        }

    }


    private void setDefaultFragment() {
        Fragment defaultFragment = mFragments.get(position);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(R.id.frameLayout, defaultFragment);
        transaction.commit();
    }

    private void switchFragment(int index) {
        if (index != position) {
            Fragment fromFragment = mFragments.get(position);
            Fragment toFragment = mFragments.get(index);
            position = index;
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);

            if (!toFragment.isAdded()) {    // 先判断是否被add过
                transaction.hide(fromFragment).add(R.id.frameLayout, toFragment)
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
                        .commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(fromFragment).show(toFragment)
                        .setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out)
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

}
