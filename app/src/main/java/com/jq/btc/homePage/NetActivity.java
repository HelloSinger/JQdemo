package com.jq.btc.homePage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.utils.NetSpeed;
import com.jq.btc.utils.NetSpeedTimer;

/**
 * Create by AYD on 2019/3/2
 */
public class NetActivity extends AppCompatActivity implements Handler.Callback {

    private TextView tv_net_show;
    private Button button;
    private NetSpeedTimer mNetSpeedTimer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_activity_layout);
        tv_net_show = findViewById(R.id.tv_net_show);
        button = findViewById(R.id.btn_black);

        Handler handler = new Handler(this);
        //创建NetSpeedTimer实例
        mNetSpeedTimer = new NetSpeedTimer(this, new NetSpeed(), handler).setDelayTime(1000).setPeriodTime(2000);
        //在想要开始执行的地方调用该段代码
        mNetSpeedTimer.startSpeedTimer();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean handleMessage(Message msg) {
        // TODO Auto-generated method stub
        switch (msg.what) {
            case NetSpeedTimer.NET_SPEED_TIMER_DEFAULT:
                String speed = (String) msg.obj;
                //打印你所需要的网速值，单位默认为kb/s
                tv_net_show.setText("当前网速为:" + speed );
                break;

            default:
                break;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mNetSpeedTimer) {
            mNetSpeedTimer.stopSpeedTimer();
        }
    }
}
