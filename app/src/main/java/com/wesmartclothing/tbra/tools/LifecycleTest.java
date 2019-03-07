package com.wesmartclothing.tbra.tools;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.vondear.rxtools.utils.RxLogUtils;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName LifecycleTest
 * @Date 2019/2/28 15:45
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class LifecycleTest implements LifecycleObserver {
    public static final String TAG = "【LifecycleTest】";


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        RxLogUtils.d(TAG, "onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        RxLogUtils.d(TAG, "onDestroy");
    }
}
