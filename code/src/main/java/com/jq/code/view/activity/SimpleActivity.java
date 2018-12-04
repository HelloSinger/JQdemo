package com.jq.code.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.jq.code.R;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.view.CustomToast;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by xulj on 2016/7/19.
 */
public class SimpleActivity extends FragmentActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setScreenFullStyle(this, Color.WHITE);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    @Override
    public void startActivityForResult(Intent intent,int requestCode) {
        super.startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
    public void onFinish(View view) {
        finish();
    }
    public void showToast(String text) {
        CustomToast.showToast(this, text, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        CustomToast.showToast(this, resId, Toast.LENGTH_SHORT);
    }
}
