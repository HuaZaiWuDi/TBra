package com.wesmartclothing.tbra.ui.guide;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kongzue.dialog.v2.CustomDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.vondear.rxtools.utils.net.RxSubscriber;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.base.BaseActivity;
import com.wesmartclothing.tbra.base.BaseTitleWebActivity;
import com.wesmartclothing.tbra.constant.Key;
import com.wesmartclothing.tbra.tools.GlideImageLoader;
import com.wesmartclothing.tbra.ui.login.LoginActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.img_right)
    ImageView mImgRight;
    @BindView(R.id.tv_go)
    TextView mTvGo;
    @BindView(R.id.layout_logo)
    FrameLayout mLayoutLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initStatusBar() {
        StatusBarUtils.from(this).setHindStatusBar(true).process();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initBundle(Bundle bundle) {

    }

    @Override
    public void initViews() {
        initViewPager();
    }

    private void initViewPager() {
        ArrayList<Object> mImageItems = new ArrayList<>();
        mImageItems.add(R.drawable.bg_guide);
        mImageItems.add(R.drawable.bg_guide);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mImgRight.setVisibility(i == mImageItems.size() - 1 ? View.GONE : View.VISIBLE);
                mLayoutLogo.setVisibility(i == mImageItems.size() - 1 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageItems.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView photoView = new ImageView(mActivity);
                RelativeLayout.MarginLayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.MarginLayoutParams.MATCH_PARENT,
                        RelativeLayout.MarginLayoutParams.MATCH_PARENT
                );
                photoView.setScaleType(ImageView.ScaleType.FIT_XY);
                photoView.setLayoutParams(params);
                GlideImageLoader.getInstance().displayImage(mActivity, mImageItems.get(position), photoView);
                container.addView(photoView);
                return photoView;
            }
        });
    }

    private void initPermissions() {
        new RxPermissions(mActivity)
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .compose(RxComposeUtils.bindLife(lifecycleSubject))
                .subscribe(new RxSubscriber<Boolean>() {
                    @Override
                    protected void _onNext(Boolean aBoolean) {
                        RxActivityUtils.skipActivityAndFinish(mContext, LoginActivity.class);
                    }
                });
    }

    @Override
    public void initNetData() {

    }

    @Override
    public void initRxBus2() {

    }

    @OnClick(R.id.tv_go)
    public void onViewClicked() {
        CustomDialog.show(mContext, R.layout.dialog_agreement, rootView -> {
            rootView.findViewById(R.id.tv_agreement)
                    .setOnClickListener(view -> {
                        //协议1
                        BaseTitleWebActivity.startBaseWebAc(mContext,
                                "注册协议", Key.WEB_URL_Registration_Agreement);
                    });
            rootView.findViewById(R.id.tv_agreement2)
                    .setOnClickListener(view -> {
                        //协议2
                        BaseTitleWebActivity.startBaseWebAc(mContext,
                                "服务条款和隐私条款", Key.WEB_URL_Implicit_Clause);
                    });
            rootView.findViewById(R.id.img_agree)
                    .setOnClickListener(view -> {
                        CustomDialog.unloadAllDialog();
                        initPermissions();
                    });
            rootView.findViewById(R.id.img_unAgree)
                    .setOnClickListener(view -> {
                        CustomDialog.unloadAllDialog();
                    });
        });
    }
}
