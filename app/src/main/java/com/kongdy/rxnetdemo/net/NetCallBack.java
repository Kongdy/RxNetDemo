package com.kongdy.rxnetdemo.net;


import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


import com.kongdy.rxnetdemo.R;
import com.kongdy.rxnetdemo.widgets.DialogHelper;
import com.kongdy.rxnetdemo.widgets.MyToast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * @author kongdy
 * @date 2017-04-24 18:39
 * @TIME 18:39
 * 网络捕获
 **/
public abstract class NetCallBack<T> implements Observer<T> {

    private final Context context;
    private final boolean showLoading;

    public NetCallBack(Context context,boolean showLoading) {
        this.context = context;
        this.showLoading = showLoading;
    }

    public NetCallBack(Context context) {
        this.context = context;
        this.showLoading = true;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if(showLoading)
            DialogHelper.showDialogForRequestLoading(context,context.getString(R.string.short_loading)
            ,false);
    }

    /**
     * 请求到数据
     */
    @Override
    public void onNext(T t) {
        onResponse(t);
    }
    /**
     * 发生错误
     */
    @Override
    public void onError(Throwable t) {
        DialogHelper.stopRequestShowLoading();
        String errorMsg;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null || connectivityManager.getActiveNetworkInfo() == null ||
                !connectivityManager.getActiveNetworkInfo().isAvailable() || t instanceof ConnectException) {
            errorMsg = context.getString(R.string.unable_net_work);
            MyToast.show(context, errorMsg, false,MyToast.alwaysShow);
            return;
        }
        /*
           超时
         */
        if (t instanceof SocketTimeoutException) {
            MyToast.show(context, "请求超时", false,MyToast.alwaysShow);
            return;
        }
        if(t instanceof MyServerException) {
            MyServerException s = (MyServerException) t;
            errorMsg = s.getDisplayMessage();
            if(s.getState().equalsIgnoreCase(MyServerException.DO_NOTHING)) {
                Log.d("NetCallBack", s.getDisplayMessage());
                return;
            }
        } else
            errorMsg = "提示"+t.toString();
        MyToast.show(context, errorMsg, false, MyToast.alwaysShow);
    }
    /**
     * 请求完成
     */
    @Override
    public void onComplete() {
        if(showLoading)
            DialogHelper.stopRequestShowLoading();
    }


    public abstract void onResponse(T t);
}
