package com.kongdy.rxnetdemo.net;

import com.kongdy.rxnetdemo.model.Recommend;
import com.kongdy.rxnetdemo.model.Root;
import com.kongdy.rxnetdemo.model.WareInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author kongdy
 * @date 2017-04-24 17:52
 * @TIME 17:52
 * 接口集合
 **/
@SuppressWarnings("WeakerAccess")
public interface ApiService {

    /**
     * 获取京东wap版本首页推荐
     * @param page
     * @return
     */
    @GET("recommend.action?_format_=json")
    Observable<Root<WareInfo>> getJDShopRecommend(@Query("page")Integer page);
}
