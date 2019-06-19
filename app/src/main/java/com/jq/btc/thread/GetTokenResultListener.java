package com.jq.btc.thread;


import com.jq.btc.model.TokenEntity;

/**
 * Created by zhaowenlong on 2018/2/27.
 */

public interface GetTokenResultListener {
    void onSuccess(TokenEntity entity);

    void onFaild(String error);
}
