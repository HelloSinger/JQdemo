package com.jq.code;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.jq.btlib.util.ThreadUtil;
import com.jq.code.code.business.Config;
import com.jq.code.code.db.FoodDB;
import com.jq.code.code.db.LocalDB;
import com.jq.code.code.db.WeightDataTipDB;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class MyApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        initJpush();
        mobClick();
        localDB();
        // 记录打开APP时间
        appConfig();
    }
    /**
     * 基础配置信息
     */
    private void appConfig() {
        Config.getInstance(this).initAppOpenTs();
    }

    /**
     * 初始化本地库
     */
    private void localDB() {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                // 复制预先准备的“食物和运动”数据库文件
                LocalDB.copyDB(MyApplication.this);
                // 把string文件的提示字段拷贝到数据库，为了后面的对应、匹配、查询、展示
                WeightDataTipDB.getInstance(MyApplication.this);
                FoodDB.getInstance(MyApplication.this);
            }
        });
    }

    /**
     * 友盟统计
     */
    private void mobClick() {
        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 推送
     */
    private void initJpush() {
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
//        if (!Account.getInstance(this).isAccountLogined()) {
//            JpushHelper.getInstance(this).setTag();
//            JpushHelper.getInstance(this).setAlias();
//        }
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
