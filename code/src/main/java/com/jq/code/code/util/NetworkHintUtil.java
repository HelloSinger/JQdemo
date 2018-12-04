package com.jq.code.code.util;

import android.content.Context;
import android.widget.Toast;

import com.jq.code.R;
import com.jq.code.view.CustomToast;

/**
 * Created by Administrator on 2017/8/16.
 */

public class NetworkHintUtil {
    public static boolean judgeNetWork(Context context){
        if (!NetWorkUtil.isNetworkConnected(context)) {
            CustomToast.showToast(context, R.string.netword_error_tip, Toast.LENGTH_SHORT);
            return false;
        }else {
            return true ;
        }
    }
}
