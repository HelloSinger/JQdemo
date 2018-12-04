package com.jq.btc.account.role;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jq.btc.app.R;
import com.jq.btc.bluettooth.WeightParser;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.ruler.RulerWheel;
import com.jq.code.view.text.CustomTextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by hfei on 2015/10/20.
 */
public class STWeightInitActivity extends CommonActivity
        implements RulerWheel.OnWheelScrollListener {

    private ViewHolder mViewHolder;
    private RoleInfo mRoleInfo;
    //显示值
    private String scaleValue;
    //重量属性值
    private byte scaleProperty;
    private int stValue;
    private float lbValue;

    private int newSTValue;
    private float newLbValue;
    private boolean hasNext;
    private class ViewHolder {
        CustomTextView goalUnit, addUnit1, value1;
        CustomTextView goalValue;
        RulerWheel rulerWheel, rulerWheel1;
        CustomTextView sure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSubView(R.layout.activity_init_st, R.string.changeWeightInit);
        initView();
        initValue();
    }

    private void initValue() {
        // 控件初始化
        mRoleInfo = getIntent().getParcelableExtra(RoleInfo.ROLE_KEY);
        hasNext = Account.getInstance(this).isMainRole(mRoleInfo) || getIntent().getStringExtra("type") != null;
        float[] fs = WeighDataParser.getWeightStandard(mRoleInfo.getHeight());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("##0.0", symbols);
        float ruleValue = mRoleInfo.getWeight_init();
        WeightEntity weightEntity = WeightDataDB.getInstance(this).findLastRoleDataByRoleId(mRoleInfo);
        if (weightEntity != null) {
            ruleValue = ruleValue == 0 ? weightEntity.getWeight() : ruleValue;
        }
        String st = WeightParser.onWeight(this, ruleValue, "", (byte) 1);
        String[] s1 = st.split(":");
        if (s1.length == 2) {
            stValue = Integer.parseInt(s1[0]);
            lbValue = Float.parseFloat(s1[1]);

            newSTValue = stValue;
            newLbValue = lbValue;
        }
        mViewHolder.goalUnit.setText(R.string.stalias);
        mViewHolder.rulerWheel.seRatio(1);
        mViewHolder.rulerWheel.setValue(stValue, 31);
        mViewHolder.goalValue.setText(stValue + "");

        mViewHolder.addUnit1.setText(R.string.pounds);
        setLbWheelValue(stValue, lbValue);
        if(hasNext){
            mViewHolder.sure.setText(R.string.down);
        }else{
            mViewHolder.sure.setText(R.string.sure);
        }
    }

    private void setLbWheelValue(int stValue, float lbValue) {
        if (stValue >= 31) {
            if (lbValue > 7) {
                lbValue = 7;
                newLbValue = 7;
            }
            mViewHolder.rulerWheel1.setValue(lbValue, 7.0f);
        } else {
            mViewHolder.rulerWheel1.setValue(lbValue, 13.9f);
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("##0.0", symbols);
        mViewHolder.value1.setText(format.format(lbValue) + "");
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.goalValue = (CustomTextView) findViewById(R.id.goal_weight_ruler_value);
        mViewHolder.goalUnit = (CustomTextView) findViewById(R.id.goal_weight_ruler_unit);
        mViewHolder.rulerWheel = (RulerWheel) findViewById(R.id.goal_weight_ruler);
        mViewHolder.sure = (CustomTextView) findViewById(R.id.goal_sure);
        mViewHolder.rulerWheel1 = (RulerWheel) findViewById(R.id.goal_weight_ruler1);
        mViewHolder.addUnit1 = (CustomTextView) findViewById(R.id.goal_weight_ruler_unit1);
        mViewHolder.value1 = (CustomTextView) findViewById(R.id.goal_weight_ruler_value1);

        mViewHolder.sure.setOnClickListener(this);
        mViewHolder.rulerWheel.setScrollingListener(this);
        mViewHolder.rulerWheel1.setScrollingListener(this);

    }

    @Override
    protected void onOtherClick(View view) {
        if (view == mViewHolder.sure) {
            if (newSTValue == 0 && newLbValue == 0) {
                showToast(R.string.mygoalweight_initerr);
                return;
            }

            if (newSTValue == stValue && newLbValue == lbValue && Float.compare(mRoleInfo.getWeight_init(), 0.0f) > 0) {
                onBack(mRoleInfo);
                return;
            }

            scaleProperty = (byte) 25;
            scaleValue = newSTValue + ":" + newLbValue;
            float value = WeightUnitUtil.ST2KG(scaleValue);
            upLoadRole("weight_init", value + "");
        }
    }

    private void onBack(RoleInfo roleInfo) {
        if (hasNext) {
            Intent intent = new Intent(this, STWeightGoalActivity.class);
            intent.putExtra(RoleInfo.ROLE_KEY, mRoleInfo);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        } else {
            setResult(RESULT_OK,
                    new Intent().putExtra(RoleInfo.ROLE_KEY, roleInfo));
            finish();
        }
    }

    /**
     * 上传更新角色
     */
    private void upLoadRole(String key, String value) {
    }

    @Override
    public void onChanged(RulerWheel wheel, int oldValue, int newValue) {
        if (mViewHolder.rulerWheel1 == wheel) {
            newLbValue = newValue / (mViewHolder.rulerWheel1.getRatio() * 1.0f);
            mViewHolder.value1.setText(String.valueOf(newLbValue));
        } else if (mViewHolder.rulerWheel == wheel) {
            newSTValue = newValue;
            mViewHolder.goalValue.setText(String.valueOf(newValue));
            setLbWheelValue(newSTValue, newLbValue);
        }
    }

    @Override
    public void onScrollingStarted(RulerWheel wheel) {

    }

    @Override
    public void onScrollingFinished(RulerWheel wheel) {

    }
}
