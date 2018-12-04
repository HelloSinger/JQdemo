package com.jq.code.code.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hfei on 2015/11/5.
 */
public class ActivityUtil {
    // Activity列表
    private final List<WeakReference<Activity>> activityList;
    // 单例模式
    private static ActivityUtil instance;

    private ActivityUtil() {
        activityList = new ArrayList<>();
    }

    /**
     * 单一实例
     */
    public static ActivityUtil getInstance() {
        if (instance == null) {
            synchronized (ActivityUtil.class) {
                if(instance == null) {
                    instance = new ActivityUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityList.add(new WeakReference<>(activity));
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        List<WeakReference<Activity>> found = new ArrayList<>();
        for(WeakReference<Activity> activityWeakReference:activityList) {
            Activity activity = activityWeakReference.get();
            if(activity != null && activity.getClass().equals(cls) && !activity.isDestroyed()) {
                activity.finish();
                found.add(activityWeakReference);
            }
        }
        for(WeakReference<Activity> weakReference:found) {
            activityList.remove(weakReference);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for(WeakReference<Activity> activityWeakReference:activityList) {
            Activity activity = activityWeakReference.get();
            if(activity != null && !activity.isDestroyed()) {
                activity.finish();
            }
        }
        activityList.clear();
    }

    /**
     * 清除activity栈
     */
    public void clear() {
        activityList.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public static final int APP_RUNNING_BACKGROUND = 1001;
    public static final int APP_RUNNING_FOREGROUND = 1002;
    public static final int APP_RUNNING_NO = 1003;

    public static int getAppSate(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                LogUtil.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    LogUtil.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return APP_RUNNING_BACKGROUND;
                } else {
                    LogUtil.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return APP_RUNNING_FOREGROUND;
                }
            }
        }
        return APP_RUNNING_NO;
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前应用程序处于前台还是后台
     *
     * @param context
     * @return
     */

    public static boolean isAppRunning(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
