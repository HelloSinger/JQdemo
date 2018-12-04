package com.jq.btc.account.role;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.ruler.RulerWheel;
import com.jq.code.view.text.CustomTextView;

import java.math.BigDecimal;

/**
 * Created by hfei on 2015/10/20.
 */
public class WeightInitActivity extends CommonActivity
        implements RulerWheel.OnWheelScrollListener {

    private ViewHolder mViewHolder;
    private RoleInfo mRoleInfo;
    private boolean hasNext;

    private class ViewHolder {
        TextView goalUnit;
        CustomTextView goalValue;
        RulerWheel rulerWheel;
        TextView sure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSubView(R.layout.activity_weight_init, R.string.changeWeightInit);
        initView();
        initValue();
    }

    private void initValue() {

        // 控件初始化
        mRoleInfo = getIntent().getParcelableExtra(RoleInfo.ROLE_KEY);
        hasNext = Account.getInstance(this).isMainRole(mRoleInfo) || getIntent().getStringExtra("type") != null;

        // 单位换算
        int unit = Config.getInstance(this).getIntWeightUnit();
        int rulerMax = 200;
        float ruleValue = mRoleInfo.getWeight_init();
        if (ruleValue == 0) {
            WeightEntity weightEntity = WeightDataDB.getInstance(this).findLastRoleDataByRoleId(mRoleInfo);
            if (weightEntity != null) {
                ruleValue = weightEntity.getWeight();
            }
        }
        if (unit == Config.JIN) {
            rulerMax = Math.round(WeightUnitUtil.KG2JIN(rulerMax));
            ruleValue = ruleValue * 2;
        }

        BigDecimal b = new BigDecimal(ruleValue);
        ruleValue = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        // 2、卡尺
        mViewHolder.goalUnit.setText(StandardUtil.getWeightExchangeUnit(this));
        mViewHolder.rulerWheel.setValue(ruleValue, rulerMax);
        mViewHolder.goalValue.setText(ruleValue + "");
//        if (hasNext) {
//            mViewHolder.sure.setText(R.string.down);
//        } else {
//            mViewHolder.sure.setText(R.string.sure);
//        }
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.goalValue = (CustomTextView) findViewById(R.id.goal_weight_ruler_value);
        mViewHolder.goalUnit = (TextView) findViewById(R.id.goal_weight_ruler_unit);
        mViewHolder.rulerWheel = (RulerWheel) findViewById(R.id.goal_weight_ruler);
        mViewHolder.sure = (TextView) findViewById(R.id.goal_sure);

        mViewHolder.sure.setOnClickListener(this);
        mViewHolder.rulerWheel.setScrollingListener(this);

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.mainColor));
        float roundRadius = getResources().getDisplayMetrics().density * 6;
        gd.setCornerRadius(roundRadius);
        mViewHolder.sure.setBackground(gd);

    }

    @Override
    protected void onOtherClick(View view) {
        if (view == mViewHolder.sure) {
            int uint = Config.getInstance(this).getIntWeightUnit();
            float weight = Float
                    .valueOf(mViewHolder.goalValue.getText() == null ? "0" :
                            mViewHolder.goalValue.getText().toString());
            if (weight == 0) {
                showToast(R.string.mygoalweight_initerr);
                return;
            }
            if (uint == Config.JIN) {
                weight = weight / 2;
            }
            upLoadRole("weight_init", weight + "");
        }
    }

    /**
     * 上传更新角色
     */
    private void upLoadRole(String key, String value) {
    }

    @Override
    public void onChanged(RulerWheel wheel, int oldValue, int newValue) {
        float value = (((float) newValue) / 10);
        mViewHolder.goalValue.setText(String.valueOf(value));
    }

    @Override
    public void onScrollingStarted(RulerWheel wheel) {

    }

    @Override
    public void onScrollingFinished(RulerWheel wheel) {

    }

}
