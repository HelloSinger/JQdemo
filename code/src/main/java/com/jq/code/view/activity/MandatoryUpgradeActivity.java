package com.jq.code.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.jq.code.R;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.model.json.JsonAppUpdateInfo;
import com.jq.code.view.text.CustomTextView;

/**
 * Created by hfei on 2015/11/27.
 */
public class MandatoryUpgradeActivity extends Activity implements View.OnClickListener{

    private ViewHolder mViewHolder;
    private JsonAppUpdateInfo appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandatory_upgrade);
        appInfo = (JsonAppUpdateInfo) getIntent().getSerializableExtra(JsonAppUpdateInfo.JSON_APP_UPDATE_FLAG);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        appInfo = (JsonAppUpdateInfo) intent.getSerializableExtra(JsonAppUpdateInfo.JSON_APP_UPDATE_FLAG);
        initValue();
    }

    private void init() {
        mViewHolder = new ViewHolder();
        mViewHolder.tip = (CustomTextView) findViewById(R.id.upgrade_tip);
        mViewHolder.cancel = (CustomTextView) findViewById(R.id.upgrade_cancle);
        mViewHolder.sure = (CustomTextView) findViewById(R.id.upgrade_sure);
        mViewHolder.mandatury = (CustomTextView) findViewById(R.id.upgrade_mandatory);
        mViewHolder.usually = (LinearLayout) findViewById(R.id.upgrade_usually);

        mViewHolder.cancel.setOnClickListener(this);
        mViewHolder.sure.setOnClickListener(this);
        mViewHolder.mandatury.setOnClickListener(this);
        initValue();

    }

    private void initValue() {
        if(appInfo != null){
            if(isMandaturyUpgrade()){
                mViewHolder.mandatury.setVisibility(View.VISIBLE);
                mViewHolder.usually.setVisibility(View.GONE);
            }else{
                mViewHolder.mandatury.setVisibility(View.GONE);
                mViewHolder.usually.setVisibility(View.VISIBLE);
            }
            mViewHolder.tip.setText(getString(R.string.settingAppUpdateTestHas) + appInfo.getVersion() + "\n" + getString(R.string.settingAppUpdateTime) + appInfo.getUpgrade_time());
        }
    }

    private boolean isMandaturyUpgrade(){
        return appInfo != null && appInfo.getRequired().equals("y");
    }

    @Override
    public void onClick(View v) {
        if(v == mViewHolder.sure){
            upgrade();
            finish();
        }else if(v == mViewHolder.cancel){
            finish();
        }else if(v == mViewHolder.mandatury){
            upgrade();
            mViewHolder.mandatury.setText(R.string.handlerUpgrading);
            mViewHolder.mandatury.setClickable(false);
        }
    }

    private void upgrade() {
//        Intent updateIntent = new Intent(this, UpdataServer.class);
//        updateIntent.putExtra("titleId", R.string.appName);
//        updateIntent.putExtra(JsonAppUpdateInfo.JSON_APP_UPDATE_FLAG, appInfo);
//        startService(updateIntent);
    }

    private class ViewHolder{
        CustomTextView tip,cancel,sure,mandatury;
        LinearLayout usually;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.upgrade_alpha_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(isMandaturyUpgrade()){
                ActivityUtil.getInstance().AppExit(this);
            }else{
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
