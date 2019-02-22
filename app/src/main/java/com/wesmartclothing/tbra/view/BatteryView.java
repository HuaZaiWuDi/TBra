package com.wesmartclothing.tbra.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.vondear.rxtools.utils.RxUtils;
import com.vondear.rxtools.view.layout.RxTextView;
import com.wesmartclothing.tbra.R;

/**
 * @Package com.wesmartclothing.tbra.view
 * @FileName BatteryView
 * @Date 2019/1/18 16:58
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class BatteryView extends RelativeLayout {
    RxTextView mTvBattery;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.layout_battery, this);
        mTvBattery = view.findViewById(R.id.tv_battery);
    }

    public void setBatteryValue(@IntRange(from = 0, to = 100) int batteryValue) {

        ViewGroup.LayoutParams params = mTvBattery.getLayoutParams();
        params.width = (int) (RxUtils.dp2px(18.5f) * batteryValue / 100f);
        mTvBattery.setLayoutParams(params);
        //低电量
        if (batteryValue <= 15) {
            mTvBattery.getHelper().setBackgroundColorNormal(Color.RED);
        } else {
            mTvBattery.getHelper().setBackgroundColorNormal(Color.parseColor("#7ED321"));
        }

    }
}
