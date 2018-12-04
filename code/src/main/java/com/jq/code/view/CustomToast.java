package com.jq.code.view;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by hfei on 2016/4/21.
 */
public class CustomToast {
    private static Toast mToast;
    //会导致http://192.168.0.90/redmine/issues/3194如下错误
    //private static Handler mhandler = new Handler();
    private static Handler mhandler;
    private static Context mContext;
    private static String mShowText;
    private static int mDuration;


    private static Runnable r = new Runnable(){
        public void run() {
            if(mToast!=null){
                mToast.cancel();
            }
        }
    };

    private static Runnable p=new Runnable() {
        @Override
        public void run() {
            if (null != mToast) {
                mToast.setDuration(mDuration);
                mToast.setText(mShowText);
            } else {
                mToast = Toast.makeText(mContext, mShowText, mDuration);
            }

            mToast.show();
        }
    };

    public static void showToast (Context context, String text, int duration) {
        if(context == null) return;
        mContext=context;
        mShowText=text;
        mDuration=duration;
        mhandler=new Handler(context.getMainLooper());
        mhandler.removeCallbacks(r);
        mhandler.post(p);
        mhandler.postDelayed(r, 5000);

    }

    public static void showToast (Context context, int strId, int duration) {
        showToast (context, context.getString(strId), duration);
    }

    public static void cancel(){
        if(mToast != null){
            mToast.cancel();
        }
    }
}
