package com.wesmartclothing.tbra.app;

import android.app.Application;
import android.content.Context;

import com.kongzue.dialog.v2.DialogSettings;
import com.tencent.bugly.Bugly;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.vondear.rxtools.activity.ActivityLifecycleImpl;
import com.vondear.rxtools.utils.RxDataUtils;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxFileUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.RxThreadPoolUtils;
import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.R;
import com.wesmartclothing.tbra.ble.BleTools;
import com.wesmartclothing.tbra.constant.SPKey;
import com.wesmartclothing.tbra.net.ServiceAPI;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.diskconverter.GsonDiskConverter;

import java.util.Arrays;

/**
 * @Package com.wesmartclothing.tbra.app
 * @FileName APP
 * @Date 2018/12/28 10:45
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class APP extends Application {

    public static Application myApp;

    @Override
    public void onCreate() {
        super.onCreate();

        myApp = this;

        syncInit();
        asynInit();
    }

    /**
     * 同步初始化
     */
    private void syncInit() {

    }

    /**
     * 异步初始化
     */
    private void asynInit() {
        new RxThreadPoolUtils(RxThreadPoolUtils.Type.SingleThread, 1)
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        RxUtils.init(myApp);
                        RxLogUtils.setLogSwitch(true);
                        initUM();
                        switchURL();
                        initRxCache();
                        registerActivityLifecycleCallbacks(new ActivityLifecycleImpl());
                        BleTools.initBLE(myApp);
                        BugLyInit();
                        initDialog();
                    }


                });
    }

    private void initDialog() {
        DialogSettings.use_blur = false;
        DialogSettings.dialog_cancelable_default = true;
        DialogSettings.style = DialogSettings.STYLE_IOS;
    }

    private void BugLyInit() {
        /**
         * 过滤开发设备
         *AppID：838d015ddb
         * AppKey：c2a63b7c-6ed7-4da5-b778-1acdbcf30bbd
         * */
        String[] androidIds = {"171e7dfb5b3005f2", "54409e1a3d1be330"};
        boolean isDevelopmentDevice = BuildConfig.DEBUG && Arrays.asList(androidIds).contains(RxDeviceUtils.getAndroidId());
        RxLogUtils.d("是否是开发设备：" + isDevelopmentDevice);

        Bugly.init(getApplicationContext(), "838d015ddb", BuildConfig.DEBUG);
        Bugly.setIsDevelopmentDevice(myApp, isDevelopmentDevice);
    }

    private void initRxCache() {
        try {
            RxCache.initializeDefault(new RxCache.Builder()
                    .appVersion(2)
                    .diskDir(RxFileUtils.getCecheFolder(myApp, getString(R.string.appName) + "-cache"))
                    .diskConverter(new GsonDiskConverter())
                    .diskMax((100 * 1024 * 1024))
                    .memoryMax(0)
//                    .memoryMax((20 * 1024 * 1024))
                    .setDebug(false)
                    .build());
        } catch (Exception e) {
            RxLogUtils.e(e);
        }

    }

    private void switchURL() {
        //开发版直接使用发布版API
        if (!BuildConfig.DEBUG) {
            ServiceAPI.switchURL(ServiceAPI.BASE_RELEASE);
        } else {
            String baseUrl = SPUtils.getString(SPKey.SP_BASE_URL, ServiceAPI.BASE_URL);
            if (!RxDataUtils.isNullString(baseUrl)) {
                ServiceAPI.switchURL(baseUrl);
            }
        }
    }

    private void initUM() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(myApp, "5a38b2e8b27b0a02a7000026", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin("wxaaeb0352e04684de", "0d23407fe42a2665dabe3ea2a958daf9");
        PlatformConfig.setQQZone("1106924585", "RGcOhc7q8qZMrhxz");
        PlatformConfig.setSinaWeibo("3322261844", "60eabde1de49af086f53aa5fb230f7ed", "https://sns.whalecloud.com/sina2/callback");

    }

    /**
     * oppo (Android4.4.4 , api19) 手机上运行项目,一直闪退 ,
     * 可能是添加MultiDex分包，但未初始化的原因，在Application中重写attachBaseContext函数，对MultiDex初始化即可。
     */
    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);

        RxLogUtils.i("启动时长：开始启动");
//        MultiDex.install(base);

    }

}
