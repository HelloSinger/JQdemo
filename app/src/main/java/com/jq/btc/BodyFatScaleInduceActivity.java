package com.jq.btc;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.utils.SpUtils;

/**
 * Create by AYD on 2018/12/5
 */
public class BodyFatScaleInduceActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView tv_open;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_fat_scale_induce_layout);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        tv_open = findViewById(R.id.tv_open);
        iv_back.setOnClickListener(this);
        tv_open.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_open:
                SpUtils.getInstance(BodyFatScaleInduceActivity.this).setIsFirst(true);
                startActivity(new Intent(this, BoundDeviceActivity.class));
                break;
        }
    }
}
