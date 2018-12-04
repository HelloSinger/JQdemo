package com.jq.btc.homePage;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by lijh on 2017/6/17.
 */

public class LocalBroadcastUtil {
    /** 表明 当前账号的全量数据加载完了，通知界面可以加载数据了 */
    public static final String ACTION_LOAD_ACCOUNT_DATA_FINISH = "ACTION_LOAD_ACCOUNT_DATA_FINISH";

    /** 表明 当前账号的所有角色信息同步完成，通知界面刷新用户的昵称、头像等信息 */
    public static final String ACTION_ROLES_SYNC = "ACTION_ROLES_SYNC";

    /** 表明 当前账号的所有角色的体重数据同步完成 */
    public static final String ACTION_WEIGHTS_SYNC = "ACTION_WEIGHTS_SYNC";

    /** 表明 当前账号的所有角色的饮食数据同步完成 */
    public static final String ACTION_FOODS_SYNC = "ACTION_FOODS_SYNC";

    /** 表明 当前账号的所有角色的运动数据同步完成 */
    public static final String ACTION_EXERCISES_SYNC = "ACTION_EXERCISES_SYNC";

    /** 手动添加体重完成 */
    public static final String ACTION_HAND_ADD_WEIGHT = "ACTION_HAND_ADD_WEIGHT";
    /** 手动添加体重完成 */
    public static final String ACTION_BLUETOOTH_ADD_WEIGHT = "ACTION_BLUETOOTH_ADD_WEIGHT";

    /** 有以前秤到的体重数据，没有（认领）匹配到哪个角色，现在匹配角色了 */
    public static final String ACTION_TEMP_WEIGHT_DATA_MATCH_ROLE = "ACTION_TEMP_WEIGHT_DATA_MATCH_ROLE";

    /** 删除体重 */
    public static final String ACTION_DELETE_WEIGHT = "ACTION_DELETE_WEIGHT";

    /**  */
    public static final String ACTION_ROLE_CHANGED = "ACTION_ROLE_CHANGED";

    /** 角色修改孕妇模式 dataKey的值为角色ID*/
    public static final String ACTION_ROLE_PREGNANT_MODE_CHANGE = "ACTION_ROLE_PREGNANT_MODE_CHANGE";

    public static class DataKey {

    }

    /**
     * 通知当前账号全量数据加载完成
     * @param context 上下文
     */
    public static void notifyAccountDataLoadFinished(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_LOAD_ACCOUNT_DATA_FINISH));
    }

    /**
     * 通知当前账号所有角色信息同步完成
     * @param context 上下文
     */
    public static void notifyRolesSync(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_ROLES_SYNC));
    }

    /**
     * 通知当前账号所有角色的体重数据同步完成
     * @param context 上下文
     */
    public static void notifyWeightsSync(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_WEIGHTS_SYNC));
    }

    /**
     * 通知当前账号所有角色的饮食数据同步完成
     * @param context 上下文
     */
    public static void notifyFoodsSync(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_FOODS_SYNC));
    }

    /**
     * 通知当前账号所有角色的运动数据同步完成
     * @param context 上下文
     */
    public static void notifyExercisesSync(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_EXERCISES_SYNC));
    }

    /**
     * 通知手动添加体重完成
     * @param context 上下文
     */
    public static void notifyHandAddWeight(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_HAND_ADD_WEIGHT));
    }
    /**
     * 通知手动添加体重完成
     * @param context 上下文
     */
    public static void notifyBlueToothAddWeight(Context context) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_BLUETOOTH_ADD_WEIGHT));
    }
    public static void notifyAction(Context context, String action) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }

    public static void notify(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }
}
