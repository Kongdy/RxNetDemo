package com.kongdy.rxnetdemo.model;

import android.databinding.BaseObservable;

import java.util.List;

/**
 * @author kongdy
 * @date 2017-05-26 14:38
 * @TIME 14:38
 **/

public class Root<T> extends BaseObservable {
    private Integer code;
    private String expid;
    private List<WareInfo> wareInfoList;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getExpid() {
        return expid;
    }

    public void setExpid(String expid) {
        this.expid = expid;
    }

    public List<WareInfo> getWareInfoList() {
        return wareInfoList;
    }

    public void setWareInfoList(List<WareInfo> wareInfoList) {
        this.wareInfoList = wareInfoList;
    }
}
