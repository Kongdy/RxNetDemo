package com.kongdy.rxnetdemo.net;


import com.google.gson.Gson;
import com.kongdy.rxnetdemo.model.Recommend;
import com.kongdy.rxnetdemo.model.Root;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * @author kongdy
 *         on 2016/8/3
 * 全局错误捕获
 */
public class ApiResponseBodyConverter <T> implements Converter<ResponseBody,T> {

    private final Gson gson;
    private final Type type;

    public ApiResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        // TODO: 2017/5/26
        /**
         *  京东返回的json数据强行加了 \ ，这里去过滤，可以根据自己需要选择是否添加
         *  */
    //    response = response.replace("\\","");
        Recommend rooDto = gson.fromJson(response,Recommend.class);

        Root root = gson.fromJson(rooDto.getRecommend(),Root.class);

        if(root.getCode() != 0)
            HttpExceptionManager.catchException(root);
        return gson.fromJson(rooDto.getRecommend(),type);
    }
}
