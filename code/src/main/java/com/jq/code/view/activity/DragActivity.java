package com.jq.code.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;

import com.jq.code.R;
import com.jq.code.code.business.DispatchEventController;
import com.jq.code.code.util.ActivityUtil;

/**
 * Created by Administrator on 2017/2/16.
 */

public class DragActivity extends SimpleActivity implements DispatchEventController.EventCallback {
    private DispatchEventController dispatchEventController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this) ;
        dispatchEventController = DispatchEventController.newInstance();
        dispatchEventController.addEventCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dispatchEventController = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        dispatchEventController.init(event);
        return super.dispatchTouchEvent(event);
    }

    public void setFinishDistance(float finishDistance){
        dispatchEventController.setFinishDistance(finishDistance);
    }


    @Override
    public void onfinish() {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    @Override
    public void onLeft() {

    }

    @Override
    public void onRight() {

    }
}
