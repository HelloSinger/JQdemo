package com.jq.code.code.business;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTimer {

    public final static int EVENT_CLICK_CALID = 0x100;
    private Timer mTimer;
    private CheckTimerTask mTimerTask;
    private Handler handler;

    public DelayTimer(Handler handler) {
        this.handler = handler;
        mTimer = new Timer(true);
    }

    public void check(long limit) {
        if (mTimer != null) {
            cancel();
            mTimerTask = new CheckTimerTask();  // 新建一个任务
            mTimer.schedule(mTimerTask, limit);
        }
    }

    public void cancel() {
        if (mTimerTask != null) {
            mTimerTask.cancel();  //将原任务从队列中移除
        }
    }

    class CheckTimerTask extends TimerTask {
        @Override
        public void run() {
            Message msg = new Message();
            msg.what = EVENT_CLICK_CALID;
            handler.sendMessage(msg);
        }
    }
}
