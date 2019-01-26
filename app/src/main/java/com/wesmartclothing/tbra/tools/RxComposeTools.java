package com.wesmartclothing.tbra.tools;

import android.content.Context;

import com.kongzue.dialog.v2.WaitDialog;
import com.zchu.rxcache.RxCache;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;

/**
 * @Package com.wesmartclothing.tbra.tools
 * @FileName RxComposeTools
 * @Date 2019/1/10 10:40
 * @Author JACK
 * @Describe TODO
 * @Project tbra
 */
public class RxComposeTools {

    public static <T> ObservableTransformer<T, T> showDialog(final Context mContext) {
        return observable -> observable.doOnSubscribe(disposable ->
                WaitDialog.show(mContext, "正在加载"))
                .doFinally((Action) () -> {
                    WaitDialog.dismiss();
                });
    }


    public static <T> ObservableTransformer<T, T> showDialog(final Context mContext, String cacheKey) {
        return observable -> observable.doOnSubscribe(disposable -> {
            if (!RxCache.getDefault().containsKey(cacheKey)) {
                WaitDialog.show(mContext, "正在加载");
            }
        })
                .doFinally((Action) () -> {
                    WaitDialog.dismiss();
                });
    }


}
