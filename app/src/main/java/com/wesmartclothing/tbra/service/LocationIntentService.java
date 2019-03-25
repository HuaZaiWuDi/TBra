package com.wesmartclothing.tbra.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.kongzue.dialog.v2.SelectDialog;
import com.vondear.rxtools.activity.RxActivityUtils;
import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;
import com.wesmartclothing.tbra.entity.rxbus.LocationBus;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationIntentService extends Service {

    final String TAG = "【LocationIntentService】";

    private boolean isSuccess;
    private String lastLatitude = "loading...";
    private String lastLongitude = "loading...";
    private String latitude = "loading...";
    private String longitude = "loading...";
    private String country = "loading...";
    private String locality = "loading...";
    private String street = "loading...";
    public static Address mAddress;
    private RxLocationUtils.OnLocationChangeListener mOnLocationChangeListener = new RxLocationUtils.OnLocationChangeListener() {
        @Override
        public void getLastKnownLocation(Location location) {
            if (location == null) return;
            lastLatitude = String.valueOf(location.getLatitude());
            lastLongitude = String.valueOf(location.getLongitude());

            mAddress = RxLocationUtils.getAddress(getApplicationContext(), Double.parseDouble(lastLatitude), Double.parseDouble(lastLongitude));
            if (mAddress != null) {
                RxBus.getInstance().post(new LocationBus(mAddress));
                RxLogUtils.d(TAG, "last地址位置：" + mAddress.toString());
                stopSelf();
            }
        }

        @Override
        public void onLocationChanged(final Location location) {
            if (location == null) return;
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            country = RxLocationUtils.getCountryName(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            locality = RxLocationUtils.getLocality(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            street = RxLocationUtils.getStreet(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));

            mAddress = RxLocationUtils.getAddress(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            if (mAddress != null) {
                RxLogUtils.d(TAG, "地址位置：" + mAddress.toString());
                RxBus.getInstance().post(new LocationBus(mAddress));
                stopSelf();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onLocationFail(boolean b, boolean b1) {

        }
    };


    @Override
    public void onCreate() {
        RxLogUtils.d(TAG, "onCreate");
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") != 0
                && ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            RxBus.getInstance().post(new LocationBus(mAddress));
            return;
        }
        if (!RxLocationUtils.isLocationEnabled(getApplicationContext())) {
            SelectDialog.show(RxActivityUtils.currentActivity(), "提示",
                    "您需要在系统设置中打开GPS\n部分手机可能搜索不到蓝牙设备", "设置",
                    (dialogInterface, i) -> {
                        RxLocationUtils.openGpsSettings(getApplicationContext());
                    });

            RxBus.getInstance().post(new LocationBus(mAddress));
            return;
        }

        isSuccess = RxLocationUtils.register(getApplicationContext(), 1000, 0, mOnLocationChangeListener);
        if (isSuccess) {
            RxLogUtils.d("init success");
        } else {
            RxBus.getInstance().post(new LocationBus(mAddress));
        }
    }


    @Override
    public void onDestroy() {
        RxLogUtils.d(TAG, "onDestroy");
        RxLocationUtils.unregister();
        mOnLocationChangeListener = null;
        // 一定要制空，否则内存泄漏
        super.onDestroy();
    }

    @Nullable
    @android.support.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
