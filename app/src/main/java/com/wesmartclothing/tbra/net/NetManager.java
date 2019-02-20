package com.wesmartclothing.tbra.net;


import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.vondear.rxtools.utils.RxDeviceUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.vondear.rxtools.utils.SPUtils;
import com.wesmartclothing.tbra.BuildConfig;
import com.wesmartclothing.tbra.constant.SPKey;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 项目名称：BleCar
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/30 11:57
 */
public class NetManager {
    private static NetManager netManager = null;
    private static ApiService mApiService, mBigFileService;
    private static SystemService mSystemService;

    public synchronized static NetManager getInstance() {
        if (netManager == null) {
            netManager = new NetManager();
        }
        return netManager;
    }


    public NetManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.connectTimeout(30, TimeUnit.SECONDS)
//                .readTimeout(30, TimeUnit.SECONDS)
//                .writeTimeout(30, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true);

        OkHttpClient.Builder bigBuilder = new OkHttpClient.Builder();
        bigBuilder.connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        if (BuildConfig.DEBUG) {
            //日志显示级别
            HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
            //新建log拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.w("【NetManager】", message);
                }
            });
            loggingInterceptor.setLevel(level);
            //OkHttp进行添加拦截器loggingInterceptor
            builder.addInterceptor(loggingInterceptor);
            bigBuilder.addInterceptor(loggingInterceptor);
        }

        RxLogUtils.d("刷新");

        builder.addInterceptor(NetInterceptor);
        bigBuilder.addInterceptor(NetInterceptor);

        Retrofit apiRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .baseUrl(ApiService.BASE_URL)
                .build();

        mApiService = apiRetrofit.create(ApiService.class);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .baseUrl(SystemService.BASE_URL)
                .build();
        mSystemService = retrofit.create(SystemService.class);


        Retrofit bigApiRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(bigBuilder.build())
                .baseUrl(ApiService.BASE_URL)
                .build();

        mBigFileService = bigApiRetrofit.create(ApiService.class);

    }

    public static void flushInstance() {
        netManager = null;
        getInstance();
    }

    public static SystemService getSystemService() {
        if (mSystemService == null) {
            getInstance();
        }
        return mSystemService;
    }

    public static ApiService getBigFileService() {
        if (mBigFileService == null) {
            getInstance();
        }
        return mBigFileService;
    }

    public static ApiService getApiService() {
        if (mApiService == null) {
            getInstance();
        }
        return mApiService;
    }


    //在请求头添加参数
    private static Interceptor NetInterceptor = chain -> {
        Request request = chain.request().newBuilder()
//                .header("userId", SPUtils.getString(SPKey.SP_UserId))
//                    .header("userId", "255bd665a36e461294aa9f66959fbbbb")
                .header("userId", "4d974e25cebe4535bde4e23302ba0dd2")
                .header("version", RxDeviceUtils.getAppVersionName())
                .header("phoneType", RxDeviceUtils.getBuildMANUFACTURER())
                .header("system", "Android")
                .header("macAddr", RxDeviceUtils.getAndroidId())
                .header("token", SPUtils.getString(SPKey.SP_token)).build();
//                .header("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwYjFiZTMyZDkzNjY0MGQxODI1ZTUzMTk4YjE3MmFiOCIsImlhdCI6MTU0NzE3NzUxMiwiZXhwIjoxNTYyODE1OTEyLCJzdWIiOiJ1c2VyIn0.mve64-k8dKsj79ndsGsUuUCpEMeLO07lPS6O3_nSO7U").build();
        return chain.proceed(request);
    };


    //添加全局请求体参数
    Interceptor publicParamInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request oldRequest = chain.request();

            // 添加新的参数
            HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                    .newBuilder()
                    .scheme(oldRequest.url().scheme())
                    .host(oldRequest.url().host())
                    .setQueryParameter("userId", "testuser")
                    .setQueryParameter("token", "testuser");

            // 新的请求
            Request newRequest = oldRequest.newBuilder()
                    .method(oldRequest.method(), oldRequest.body())
                    .url(authorizedUrlBuilder.build())
                    .build();

            return chain.proceed(newRequest);
        }
    };


    public static RequestBody fetchRequest(String body) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
    }

}
