package com.jq.code.code.business;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface HotCallback<T> {
    void onData(int type,T t);

    void onEmpty(int type);

    void onError(String msg, int code);
}
