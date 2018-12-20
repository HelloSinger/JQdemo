package com.jq.btc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.jq.btc.app.R;

/**
 * Create by AYD on 2018/12/5
 */
public class BodyFatScaleInduceActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_fat_scale_induce_layout);
        initView();
    }

    private void initView() {
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
