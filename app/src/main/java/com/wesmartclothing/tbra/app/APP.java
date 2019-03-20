package com.wesmartclothing.tbra.app;

import android.app.Application;
import android.content.Context;

import com.kongzue.dialog.v2.DialogSettings;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
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
import com.wesmartclothing.tbra.tools.jpush.JPushUtils;
import com.wesmartclothing.tbra.tools.soinc.SonicRuntimeImpl;
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
        RxThreadPoolUtils threadPoolUtils = new RxThreadPoolUtils(RxThreadPoolUtils.Type.SingleThread, 1);
        threadPoolUtils.execute(() -> {
            RxUtils.init(myApp);
            RxLogUtils.setLogSwitch(BuildConfig.DEBUG);
            switchURL();
            initRxCache();
            initUM();
            registerActivityLifecycleCallbacks(new ActivityLifecycleImpl());
            BleTools.initBLE(myApp);
            BugLyInit();
            initDialog();
            initSonicWeb();
            JPushUtils.init(myApp);
            threadPoolUtils.shutDownNow();
        });
    }

    private void initSonicWeb() {
        // step 1: 必要时初始化sonic引擎，或者在创建应用程序时进行初始化
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplicationContext()), new SonicConfig.Builder().build());
        }
//        //如果是脱机pkg模式，我们需要拦截会话连接
//        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
//        sessionConfigBuilder.setSupportLocalServer(true);
//        sessionConfigBuilder.setCacheInterceptor(new SonicCacheInterceptor(null) {
//            @Override
//            public String getCacheData(SonicSession session) {
//                return null; // offline pkg does not need cache
//            }
//        });
//
//        sessionConfigBuilder.setConnectionInterceptor(new SonicSessionConnectionInterceptor() {
//            @Override
//            public SonicSessionConnection getConnection(SonicSession session, Intent intent) {
//                return new OfflinePkgSessionConnection(mContext, session, intent);
//            }
//        });

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
        boolean isDevelopmentDevice = Arrays.asList(androidIds).contains(RxDeviceUtils.getAndroidId());
        RxLogUtils.d("是否是开发设备：" + isDevelopmentDevice);

        if (!isDevelopmentDevice) {
            Bugly.init(getApplicationContext(), "838d015ddb", BuildConfig.DEBUG);
        }
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
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        UMConfigure.init(myApp, "5c662a98b465f543d1001245", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin("wx940e7690d27085d8", "3156b46cbc94d76f1d73266db8683a4b");
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

        // 安装tinker
        Beta.installTinker();
    }

}
