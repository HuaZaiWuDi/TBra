package com.wesmartclothing.tbra.net;


import com.google.gson.reflect.TypeToken;
import com.vondear.rxtools.model.lifecycyle.LifeCycleEvent;
import com.vondear.rxtools.utils.net.HttpResult;
import com.vondear.rxtools.utils.net.RxComposeUtils;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.stategy.IObservableStrategy;

import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 项目名称：BleCar
 * 类描述：
 * 创建人：oden
 * 创建时间：2016/8/30 11:06
 */
public class RxManager {
    private static RxManager rxManager = null;
    String TAG = "[RxManager]";


    private RxManager() {

    }

    public synchronized static RxManager getInstance() {
        if (rxManager == null) {
            rxManager = new RxManager();
        }
        return rxManager;
    }


//    public <T> Observable<T> dofunSubscribe(Observable<T> observable) {
//        return observable
//                .throttleFirst(1, TimeUnit.SECONDS)  //两秒只发生第一时间
//                .timeout(1, TimeUnit.SECONDS)   //两秒超时重新发送
//                .retry(2)  //重试两次
//                .compose(RxComposeUtils.<T>rxThreadHelper());
//    }


    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                ;
    }

    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable,
                                            BehaviorSubject<LifeCycleEvent> lifecycleSubject,
                                            String cacheKey,
                                            Type type,
                                            IObservableStrategy strategy
    ) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                .compose(RxComposeUtils.<T>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<T>transformObservable(cacheKey, type, strategy))
                .map(new CacheResult.MapFunc<T>())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable,
                                            BehaviorSubject<LifeCycleEvent> lifecycleSubject,
                                            String cacheKey,
                                            IObservableStrategy strategy
    ) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                .compose(RxComposeUtils.<T>bindLife(lifecycleSubject))
                .compose(RxCache.getDefault().<T>transformObservable(cacheKey, new TypeToken<T>() {
                }.getType(), strategy))
                .map(new CacheResult.MapFunc<T>())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }


    public <T> Observable<T> doNetSubscribe(Observable<HttpResult<T>> observable,
                                            BehaviorSubject<LifeCycleEvent> lifecycleSubject
    ) {
        return observable
                .compose(RxComposeUtils.<T>handleResult2())
                .compose(RxComposeUtils.<T>rxThreadHelper())
                .compose(RxComposeUtils.<T>bindLife(lifecycleSubject));
    }


    public <T> Observable<T> doLoadDownSubscribe(Observable<T> observable) {
        return observable
                .compose(RxComposeUtils.<T>rxThreadHelper());
    }

}
