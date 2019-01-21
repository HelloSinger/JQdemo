package com.jq.btc.utils;

/**
 * Created by YJ on 2017/1/9.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.btc.app.R;


/**
 * Toast管理
 */
public class BMToastUtil {

    public static Toast toast;

    public static void showToastShort(Context context, Integer resId) {
        if (context == null){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        View layout = View.inflate(context, R.layout.bm_dialog,null);
        // 设置toast文本，把设置好的布局传进来
        toast.setView(layout);
        TextView tv_content = layout.findViewById(R.id.tv_content);
        tv_content.setText(context.getResources().getString(resId));
        // 设置土司显示在屏幕的位置
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,400);
        // 显示土司
        toast.show();
    }

    public static void showToastShort(Context context, String content) {
        if (context == null){
            return;
        }
        if (toast == null) {
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }
        View layout = View.inflate(context, R.layout.bm_dialog,null);
        // 设置toast文本，把设置好的布局传进来
        toast.setView(layout);
        TextView tv_content = layout.findViewById(R.id.tv_content);
        tv_content.setText(content);
        // 设置土司显示在屏幕的位置
        toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP,0,400);
        // 显示土司
        toast.show();
    }


}