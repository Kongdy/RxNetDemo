package com.kongdy.rxnetdemo.model;

import android.databinding.BaseObservable;

/**
 * @author kongdy
 * @date 2017-05-26 14:36
 * @TIME 14:36
 **/

public class Recommend<T> extends BaseObservable {

    private String recommend;

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}
