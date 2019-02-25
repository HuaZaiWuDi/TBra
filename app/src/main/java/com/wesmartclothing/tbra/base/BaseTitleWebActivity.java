package com.wesmartclothing.tbra.base;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.fragment.FragmentUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.view.RxTitle;
import com.vondear.rxtools.view.cardview.CardView;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.constant.Key;

import butterknife.BindView;

public class BaseTitleWebActivity extends BaseActivity {

    @BindView(R.id.parent)
    LinearLayout mParent;
    @BindView(R.id.rxTitle)
    RxTitle mRxTitle;
    @BindView(R.id.layoutTitle)
    CardView mLayoutTitle;
    @BindView(R.id.layout_frameLayout)
    FrameLayout mLayoutFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int layoutId() {
        return R.layout.activity_base_title_web;
    }


    public static void startBaseWebAc(Context mContext, String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Key.BUNDLE_TITLE, title);
        bundle.putString(Key.BUNDLE_WEB_URL, url);
        RxActivityUtils.skipActivity(mContext, BaseTitleWebActivity.class, bundle);
    }

    @Override
    public void initBundle(Bundle bundle) {
        String url = bundle.getString(Key.BUNDLE_WEB_URL);
        String title = bundle.getString(Key.BUNDLE_TITLE);
        RxLogUtils.d("title:" + title);
        RxLogUtils.d("url:" + url);
//        url = "http://mc.vip.qq.com/demo/indexv3";
        mRxTitle.setTitle(title);
        FragmentUtils.replace(getSupportFragmentManager(), BaseWebFragment.getInstance(url), R.id.layout_frameLayout);
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
}
