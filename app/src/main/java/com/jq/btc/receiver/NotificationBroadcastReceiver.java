package com.jq.btc.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.jq.btc.InitActivity;
import com.jq.code.code.service.MusicService;

/**
 * Created by Administrator on 2016/12/7.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public static final String TYPE = "type"; //这个type是为了Notification更新信息的，这个不明白的朋友可以去搜搜，很多

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int type = intent.getIntExtra(TYPE, -1);

        if (type != -1) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(type);
        }

        if (action.equals("notification_clicked")) {
            //处理点击事件
            stopMuisc(context);
            Intent intentClick = new Intent(context, InitActivity.class);
            intentClick.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式
            context.startActivity(intentClick);
        }

        if (action.equals("notification_cancelled")) {
            //处理滑动清除和点击删除事件
            stopMuisc(context);
        }
    }


    private void stopMuisc(Context context) {
        Intent in = new Intent();
        in.setClassName(context.getPackageName(), MusicService.class.getName());
        context.stopService(in);
    }
}