package com.jq.code.code.business;

/**
 * Created by Administrator on 2017/3/8.
 */

public interface ImpCallbak<T> {
    void onData(T t);

    void onEmpty();

    void onError(String msg, int code);
}
