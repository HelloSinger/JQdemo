package com.jq.btc.myview;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * describe：
 *
 *
 */
public class UIUtils {

    /**
     * 获取屏内容
     */
    public static DisplayMetrics getScreen(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics;
    }

    /**
     * dip转换px
     */
    public static int dp2px(Context context, double dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    /**
     * pxz转换dip
     */
    public static int px2dp(Context context, int px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

}
