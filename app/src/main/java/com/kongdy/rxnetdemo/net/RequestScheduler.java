package com.kongdy.rxnetdemo.net;


import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author kongdy
 * @date 2017-04-25 16:12
 * @TIME 16:12
 * 线程调度
 **/
public class RequestScheduler {
    /**
     * 默认线程调度，网络请求单独线程，返回数据处理的时候主线程
     */
    public static <T> ObservableTransformer<T, T> defaultSchedulers() {
        return (upstream -> upstream.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.single()).
                retryWhen(new RequestRetryException()));
    }
}
