package com.jq.btc;

import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.code.MyApplication;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2017/3/6.
 */

public class CSApplication extends MyApplication {

    /**
     * Application单例
     */
    private static CSApplication sInstance;
    /**
     * ble控制类
     */
    BleHelper mBle;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        //初始化ble的控制类
        mBle = new BleHelper();
        mBle.init(getApplicationContext());
        initUser();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                         //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);
    }

    /**
     * ctx可以传递ApplicatonContext，
     * debug模式在没有用户中心apk的时候传递true，
     * 如果用户中心apk未安装，
     * 所有的用户信息都返回字符串空(NOT null)，
     * loginToDo()方法的Runnable都会执行。
     *
     * @return
     */

    private void initUser() {
        UserUtils.get().setup(getInstance(), false);
    }

    /**
     * @return Application实例
     */
    public static CSApplication getInstance() {
        return sInstance;
    }

    public BleHelper getmBle() {
        return mBle;
    }

    public void setmBle(BleHelper mBle) {
        this.mBle = mBle;
    }
}
