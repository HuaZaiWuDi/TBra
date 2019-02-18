package com.wesmartclothing.tbra.tools;

import android.text.style.ClickableSpan;
import android.view.View;

public class CustomClickUrlSpan extends ClickableSpan {
    private OnLinkClickListener mListener;

    public CustomClickUrlSpan(OnLinkClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null) {
            mListener.onLinkClick(widget);
        }
    }

    /**
     * 跳转链接接口
     */
    public interface OnLinkClickListener {
        void onLinkClick(View view);
    }
}