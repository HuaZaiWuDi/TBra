package com.wesmartclothing.tbra.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.vondear.rxtools.utils.RxBus;
import com.vondear.rxtools.utils.RxLocationUtils;
import com.vondear.rxtools.utils.RxLogUtils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationIntentService extends IntentService {

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
            lastLatitude = String.valueOf(location.getLatitude());
            lastLongitude = String.valueOf(location.getLongitude());

            mAddress = RxLocationUtils.getAddress(getApplicationContext(), Double.parseDouble(lastLatitude), Double.parseDouble(lastLongitude));
            RxBus.getInstance().post(mAddress);
            RxLogUtils.d(TAG, "last地址位置：" + mAddress.toString());
        }

        @Override
        public void onLocationChanged(final Location location) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());

            country = RxLocationUtils.getCountryName(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            locality = RxLocationUtils.getLocality(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            street = RxLocationUtils.getStreet(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));

            mAddress = RxLocationUtils.getAddress(getApplicationContext(), Double.parseDouble(latitude), Double.parseDouble(longitude));
            RxLogUtils.d(TAG, "地址位置：" + mAddress.toString());
            RxBus.getInstance().post(mAddress);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };


    @Override
    public void onCreate() {
        RxLogUtils.d(TAG, "onCreate");
        super.onCreate();
        initLocation();
    }

    private void initLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION") != 0
                && ActivityCompat.checkSelfPermission(getApplicationContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
            return;
        }
        isSuccess = RxLocationUtils.register(getApplicationContext(), 0, 0, mOnLocationChangeListener);
        if (isSuccess) {
            RxLogUtils.d("init success");
        } else {
            initLocation();
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


    public LocationIntentService() {
        super("LocationIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        RxLogUtils.d(TAG, "onHandleIntent");
        if (intent != null) {
            final String action = intent.getAction();

        }
    }

}
