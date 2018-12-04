package com.jq.btc.homePage.home.utils;

import android.content.res.Resources;
import android.view.MotionEvent;

/**
 * Created by lijh on 2017/8/14.
 */

public class ScrollJudge {
    private float verticalSpace;
    public ScrollJudge() {
        verticalSpace = Resources.getSystem().getDisplayMetrics().density * 65;
    }

    public boolean shouldUp(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) < 50) {
            // System.out.println("手指移动的太慢了");
            return false;
        }
        return velocityY < -300 || e1 != null && e2 != null && (e2.getRawY() - e1.getRawY()) < -verticalSpace;
    }

    public boolean shouldBottom(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (Math.abs(velocityY) < 50) {
            // System.out.println("手指移动的太慢了");
            return false;
        }
        return velocityY > 400 || e1 != null && e2 != null && (e2.getRawY() - e1.getRawY()) > verticalSpace * 2;
    }
}
