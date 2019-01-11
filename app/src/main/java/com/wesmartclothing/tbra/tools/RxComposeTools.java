package com.wesmartclothing.tbra.tools;

import android.content.Context;

import com.kongzue.dialog.v2.WaitDialog;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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
        return new ObservableTransformer<T, T>() {
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.doOnSubscribe(new Consumer<Disposable>() {
                    public void accept(Disposable disposable) throws Exception {
                        WaitDialog.show(mContext, "正在加载");

                    }
                }).doFinally(new Action() {
                    public void run() throws Exception {
                        WaitDialog.dismiss();
                    }
                });
            }
        };
    }

    public static <T> ObservableTransformer<T, T> showDialog(final Context mContext, final String tip) {
        return new ObservableTransformer<T, T>() {
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.doOnSubscribe(new Consumer<Disposable>() {
                    public void accept(Disposable disposable) throws Exception {
                        WaitDialog.show(mContext, tip);

                    }
                }).doFinally(new Action() {
                    public void run() throws Exception {
                        WaitDialog.dismiss();
                    }
                });
            }
        };
    }

}
