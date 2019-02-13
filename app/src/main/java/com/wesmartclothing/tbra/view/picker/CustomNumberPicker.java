package com.wesmartclothing.tbra.view.picker;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wesmartclothing.tbra.R;

import cn.qqtheme.framework.picker.NumberPicker;

/**
 * Created by jk on 2018/8/13.
 */
public class CustomNumberPicker extends NumberPicker {


    public CustomNumberPicker(Activity activity) {
        super(activity);
        setTopLineVisible(false);
        setGravity(Gravity.BOTTOM);
        setHeight((int) (getScreenHeightPixels() * 0.4));
        setTopLineVisible(false);
        setCycleDisable(true);
        setDividerConfig(null);
        setOffset(2);//偏移量
        setTextSize(24);
        setUseWeight(false);
        setLabel("cm");
        setTextColor(activity.getResources().getColor(R.color.Gray));
    }

    @Nullable
    @Override
    protected View makeHeaderView() {
        return null;
    }


    @Nullable
    @Override
    protected View makeFooterView() {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_picker_footer, null);
        TextView picker_cancel = view.findViewById(R.id.tv_cancel);
        picker_cancel.setOnClickListener(v -> {
            dismiss();
            onCancel();
        });
        TextView picker_ok = view.findViewById(R.id.tv_ok);
        picker_ok.setOnClickListener(v -> {
            dismiss();
            onSubmit();
        });
        return view;
    }

}
