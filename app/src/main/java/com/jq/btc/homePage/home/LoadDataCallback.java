package com.jq.btc.homePage.home;

/**
 * Created by lijh on 2017/5/31.
 */

public interface LoadDataCallback {
    void onSuccess(Object data);
    void onFailure(String msg, int code);
}
