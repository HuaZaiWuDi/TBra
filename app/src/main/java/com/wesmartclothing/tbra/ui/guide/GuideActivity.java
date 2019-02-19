package com.wesmartclothing.tbra.ui.guide;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
        mImageItems.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=87077840f66f6c7607074ad9d7681e80&imgtype=0&src=http%3A%2F%2Fpic.kekenet.com%2F2017%2F0323%2F24621490236152.png");
        mImageItems.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=d7b02b4c46569e0197f5949164433c52&imgtype=0&src=http%3A%2F%2Fww2.sinaimg.cn%2Flarge%2F574ddb5egw1eqosahw1m6j20pa0g00w3.jpg");
        mImageItems.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546937098325&di=53404ec533a940868fef1c0793cc8a5d&imgtype=0&src=http%3A%2F%2Fwerkstette.dk%2Fwp-content%2Fuploads%2F2015%2F09%2FEntertainment_Weekly_Photographer_Marc_Hom_British_Actor_Charlie_Hunnam_as_King_Arthur_Retouch_Werkstette10-770x841.jpg");

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
