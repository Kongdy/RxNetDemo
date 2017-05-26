package com.kongdy.rxnetdemo.net;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @author kongdy
 * @date 2017-04-24 17:51
 * @TIME 17:51
 * retrofit创建类
 **/
public class RetrofitHelper {

    private static final long timeOutTime = 10;// 10秒的单次超时时间

    private static final String BASE_URL = "http://m.jd.com/index/";

    public static ApiService getRetrofit() {
        return new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GlobalExceptionCatch.create(buildGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(genericClient())
                .build()
                .create(ApiService.class);
    }

    /**
     * 定义请求参数
     */
    private static OkHttpClient genericClient() {
        return new OkHttpClient.Builder()
                .addInterceptor((chain->{
//                    Request oldRequest = chain.request();
//                    HttpUrl.Builder httpUrl = oldRequest.url().newBuilder();
//                    if (AppBasic.getToken() != null) {
//                        httpUrl = oldRequest.url()
//                                .newBuilder()
//                                .scheme(oldRequest.url().scheme())
//                                .host(oldRequest.url().host())
//                                .addQueryParameter("u_token", AppBasic.getToken());
//                    }
//                    // head
//                    Request request = oldRequest
//                            .newBuilder()
//                            .method(oldRequest.method(), oldRequest.body())
//                            .url(httpUrl.build())
//                            .header("deviceID",AppBasic.getDeviceId())
//                            .build();
//                    return chain.proceed(request);
                    return chain.proceed(chain.request());
                }))
                .retryOnConnectionFailure(true)
                .connectTimeout(timeOutTime, TimeUnit.SECONDS)
                .readTimeout(timeOutTime, TimeUnit.SECONDS)
                .connectTimeout(timeOutTime,TimeUnit.SECONDS)
                .build();
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Integer.class, new IntegerJsonSerializer())
                .registerTypeAdapter(int.class, new IntegerJsonSerializer())
                .create();
    }

}
