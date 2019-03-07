package com.wesmartclothing.tbra.tools;

import android.content.Context;

import com.kongzue.dialog.v2.WaitDialog;
import com.vondear.rxtools.utils.RxLogUtils;
import com.zchu.rxcache.RxCache;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

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
                WaitDialog.show(mContext, "数据加载中"))
                .doFinally((Action) () -> {
                    WaitDialog.dismiss();
                });
    }


    public static <T> ObservableTransformer<T, T> showDialog(final Context mContext, String cacheKey) {
        return observable -> observable.doOnSubscribe(disposable -> {
            RxLogUtils.d("showDialog当前线程：" + Thread.currentThread().getName());
            if (!RxCache.getDefault().containsKey(cacheKey)) {
                WaitDialog.show(mContext, "数据加载中");
            }
        })
                .doFinally((Action) () -> {
                    RxLogUtils.d("showDialog2当前线程：" + Thread.currentThread().getName());
                    WaitDialog.dismiss();
                });
    }


    public static <T> ObservableTransformer<T, T> rxThreadHelper() {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
    }

}
