package com.kongdy.rxnetdemo.net;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author kongdy
 *         on 2016/8/4
 *         convert工厂
 */
public class GlobalExceptionCatch extends Converter.Factory {

        public static GlobalExceptionCatch create() {
            return create(new Gson());
        }


        public static GlobalExceptionCatch create(Gson gson) {
            return new GlobalExceptionCatch(gson);
        }

        private final Gson gson;

        private GlobalExceptionCatch(Gson gson) {
            if (gson == null) throw new NullPointerException("gson == null");
            this.gson = gson;
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            return new ApiResponseBodyConverter<>(gson, type);
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new MyGsonRequestBodyConverter<>(gson, adapter);
        }
}
