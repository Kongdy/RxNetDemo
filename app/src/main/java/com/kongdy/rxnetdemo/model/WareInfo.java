package com.kongdy.rxnetdemo.model;

import android.databinding.BaseObservable;

/**
 * @author kongdy
 * @date 2017-05-26 14:39
 * @TIME 14:39
 **/

public class WareInfo extends BaseObservable {

    private String wname;
    private String imageurl;
    private String clickUrl;

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }
}
