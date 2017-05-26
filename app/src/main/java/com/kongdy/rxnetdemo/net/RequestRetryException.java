package com.kongdy.rxnetdemo.net;


import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author kongdy
 * @date 2017-04-25 16:28
 * @TIME 16:28
 **/

public class RequestRetryException implements Function<Observable<? extends Throwable>,Observable<?>> {

    private int count = 2;
    private int delay = 3000;

    public RequestRetryException() {
    }

    public RequestRetryException(int count) {
        this.count = count;
    }

    public RequestRetryException(int count, int delay) {
        this.count = count;
        this.delay = delay;
    }

    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable
                .zipWith(Observable.range(1, count + 1),(throwable,integer) -> new Wrapper(integer,throwable))
                .flatMap((wrapper -> {
                    if((wrapper.throwable instanceof ConnectException
                             || wrapper.throwable instanceof SocketTimeoutException
                            || wrapper.throwable instanceof TimeoutException) && wrapper.index < count+1)
                        return Observable.timer(delay+(wrapper.index-1)*delay, TimeUnit.MILLISECONDS);
                    return Observable.error(wrapper.throwable);
                }));
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(int index, Throwable throwable) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}
