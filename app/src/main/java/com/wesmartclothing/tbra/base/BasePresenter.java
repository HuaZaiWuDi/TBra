package com.wesmartclothing.tbra.base;

import android.view.View;

public abstract class BasePresenter {
    private View view;

    /**
     * 绑定View
     */
    public void attachView(View view) {
        this.view = view;
    }

    /**
     * 解绑View
     */
    public void detachView() {
        this.view = null;
    }
}