package com.kongdy.rxnetdemo.net;


import com.kongdy.rxnetdemo.model.Root;

/**
 * @author kongdy
 *         on 2016/8/3
 *         请求错误管理
 */
public class HttpExceptionManager {

    /**
     * 捕获错误
     */
     static void catchException(Root netErrorCode) {

        throw new MyServerException(netErrorCode.getCode()+"", netErrorCode.getExpid());
    }
}
