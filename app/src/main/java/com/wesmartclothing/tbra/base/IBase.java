package com.wesmartclothing.tbra.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**
 * @Package com.wesmartclothing.tbra.base
 * @FileName IBase
 * @Date 2019/1/8 10:16
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public interface IBase {

    /**
     * 初始化状态栏
     */
    void initStatusBar();

    /**
     * 初始化布局Id
     */
    @LayoutRes
    int layoutId();

    /**
     * 初始化Bundle数据
     */
    void initBundle(Bundle bundle);

    /**
     * 初始化布局逻辑
     */
    void initViews();

    /**
     * 初始化网络数据
     */
    void initNetData();

    /**
     * 初始化事件总成
     */
    void initRxBus2();
}
