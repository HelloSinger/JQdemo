package com.jq.btc;

import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.code.MyApplication;

/**
 * Created by Administrator on 2017/3/6.
 */

public class CSApplication extends MyApplication{

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
