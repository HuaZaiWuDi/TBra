package com.wesmartclothing.tbra.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.model.lifecycyle.LifeCycleEvent;
import com.vondear.rxtools.utils.RxScreenAdapter;
import com.vondear.rxtools.utils.StatusBarUtils;
import com.vondear.rxtools.view.RxTitle;
import com.wesmartclothing.tbra.app.APP;
import com.wesmartclothing.tbra.ui.guide.SplashActivity;

import butterknife.ButterKnife;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author Jack
 * @date on 2018/12/28
 * @describe TODO基类
 * @org 智裳科技
 */
public abstract class BaseActivity extends AppCompatActivity implements IBase {

    public Context mContext;
    public Activity mActivity;
    protected final BehaviorSubject<LifeCycleEvent> lifecycleSubject = BehaviorSubject.create();
    public boolean isVisibility = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(LifeCycleEvent.CREATE);

        //设置为不响应横竖屏切换
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //输入框被遮挡问题
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        super.onCreate(savedInstanceState);

        mContext = this;
        mActivity = this;

        //防止应用处于后台，被杀死，再次唤醒时，重走启动流程
        if (savedInstanceState != null) {
            Intent intent = new Intent(APP.myApp, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        initStatusBar();

        RxActivityUtils.addActivity(mActivity);

        RxScreenAdapter.setCustomDensity(mActivity.getApplication(), mActivity);

        if (layoutId() != 0) {
            setContentView(layoutId());
            ButterKnife.bind(mActivity);
            initViews();
            if (getIntent().getExtras() != null) {
                initBundle(getIntent().getExtras());
            }
            initRxBus2();
            initNetData();
        }
    }

    public void initTitle(RxTitle mRxTitle) {
        mRxTitle.setLeftOnClickListener(view -> onBackPressed());
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            initBundle(intent.getExtras());
        }
    }


    @Override
    public void initStatusBar() {
//        //屏幕沉浸
        StatusBarUtils.from(mActivity)
                .setTransparentStatusbar(true)
                .setLightStatusBar(true)
                .process();
    }


    @Override
    protected void onStart() {
        lifecycleSubject.onNext(LifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onStop() {
        isVisibility = false;
        lifecycleSubject.onNext(LifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(LifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        isVisibility = true;
        lifecycleSubject.onNext(LifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onRestart() {
        lifecycleSubject.onNext(LifeCycleEvent.RESTART);
        super.onRestart();
    }


    @Override
    protected void onDestroy() {
        lifecycleSubject.onNext(LifeCycleEvent.DESTROY);
        RxActivityUtils.removeActivity(this);
        super.onDestroy();
        mContext = null;
        mActivity = null;
    }


    @Override
    public void onBackPressed() {
        RxActivityUtils.finishActivity();
    }


    public boolean isVisibility() {
        return isVisibility;
    }


}
