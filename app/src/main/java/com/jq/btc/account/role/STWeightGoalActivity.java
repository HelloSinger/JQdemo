package com.jq.btc.account.role;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jq.btc.app.R;
import com.jq.btc.bluettooth.WeightParser;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.LogUtil;
import com.jq.code.code.util.StandardUtil;
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
public class STWeightGoalActivity extends CommonActivity
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

    private class ViewHolder {
        CustomTextView standarWeighttValue;
        CustomTextView standarUnit;
        CustomTextView goalUnit, addUnit1, value1;
        CustomTextView goalValue;
        RulerWheel rulerWheel, rulerWheel1;
        CustomTextView sure;
        CustomTextView loss, keep, gain;
        View lossIndex, keepIndex, gainIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSubView(R.layout.activity_goal_st, R.string.changeWeightGoal);
        initView();
        initValue();
    }

    private void initValue() {
        // 控件初始化
        mRoleInfo = getIntent().getParcelableExtra(
                RoleInfo.ROLE_KEY);
        float[] fs = WeighDataParser.getWeightStandard(mRoleInfo.getHeight());

        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("##0.0", symbols);
        float ruleValue = (mRoleInfo.getWeight_goal());
        WeightEntity weightEntity = WeightDataDB.getInstance(this).findLastRoleDataByRoleId(mRoleInfo);
        if (weightEntity != null) {
            ruleValue = (ruleValue == 0) ? (weightEntity.getWeight()) : ruleValue;
        }
        float tmpstGoal = ruleValue;
        String standerValue =
                StandardUtil.getWeightExchangeValue(this, fs[0], "", (byte) 1) + "-" +
                        StandardUtil.getWeightExchangeValue(this, fs[1], "", (byte) 1);
        // 1、标准单位
        mViewHolder.standarWeighttValue.setText(standerValue);
        mViewHolder.standarUnit.setText(StandardUtil.getWeightExchangeUnit(this));
        String st = WeightParser.onWeight(this, ruleValue, "", (byte) 1);
        String[] s1 = st.split(":");
        if (s1.length == 2) {
            stValue = Integer.parseInt(s1[0]);
            lbValue = Float.parseFloat(s1[1]);

            newSTValue=stValue;
            newLbValue=lbValue;
        }
        mViewHolder.goalUnit.setText(R.string.stalias);
        mViewHolder.rulerWheel.seRatio(1);
        mViewHolder.rulerWheel.setValue(stValue, 31);
        mViewHolder.goalValue.setText(stValue + "");

        mViewHolder.addUnit1.setText(R.string.pounds);
        setLbWheelValue(stValue,lbValue);

        setSmaller();
        if (mRoleInfo.getWeight_init() != 0) {
            int compare = Float.compare(mRoleInfo.getWeight_init(), tmpstGoal);
            if (compare > 0) {
                mViewHolder.loss.performClick();
            } else if (compare < 0) {
                mViewHolder.gain.performClick();
            } else if (compare == 0) {
                mViewHolder.keep.performClick();
            }
        }
    }

    private void setLbWheelValue(int stValue,float lbValue){
        if(stValue>=31){
            if(lbValue>7){
                lbValue=7;
                newLbValue=7;
            }
            mViewHolder.rulerWheel1.setValue(lbValue, 7.0f);
        }else{
            mViewHolder.rulerWheel1.setValue(lbValue, 13.9f);
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("##0.0", symbols);
        mViewHolder.value1.setText(format.format(lbValue) + "");
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.standarWeighttValue = (CustomTextView) findViewById(R.id.goal_stander_weight_value);
        mViewHolder.standarUnit = (CustomTextView) findViewById(R.id.goal_stander_weight_unit);
        mViewHolder.goalValue = (CustomTextView) findViewById(R.id.goal_weight_ruler_value);
        mViewHolder.goalUnit = (CustomTextView) findViewById(R.id.goal_weight_ruler_unit);
        mViewHolder.rulerWheel = (RulerWheel) findViewById(R.id.goal_weight_ruler);
        mViewHolder.sure = (CustomTextView) findViewById(R.id.goal_sure);
        mViewHolder.loss = (CustomTextView) findViewById(R.id.role_aims_loss);
        mViewHolder.keep = (CustomTextView) findViewById(R.id.role_aims_keep);
        mViewHolder.gain = (CustomTextView) findViewById(R.id.role_aims_gain);
        mViewHolder.lossIndex = findViewById(R.id.role_aims_loss_index);
        mViewHolder.keepIndex = findViewById(R.id.role_aims_keep_index);
        mViewHolder.gainIndex = findViewById(R.id.role_aims_gain_index);
        mViewHolder.rulerWheel1 = (RulerWheel) findViewById(R.id.goal_weight_ruler1);
        mViewHolder.addUnit1 = (CustomTextView) findViewById(R.id.goal_weight_ruler_unit1);
        mViewHolder.value1 = (CustomTextView) findViewById(R.id.goal_weight_ruler_value1);

        mViewHolder.loss.setOnClickListener(this);
        mViewHolder.keep.setOnClickListener(this);
        mViewHolder.gain.setOnClickListener(this);
        mViewHolder.sure.setOnClickListener(this);
        mViewHolder.rulerWheel.setScrollingListener(this);
        mViewHolder.rulerWheel1.setScrollingListener(this);

    }

    @Override
    protected void onOtherClick(View view) {
        if (view == mViewHolder.sure) {
            if(newSTValue == 0 && newLbValue == 0){
                showToast(R.string.mygoalweight_goalerr);
                return;
            }

            if (newSTValue == stValue && newLbValue == lbValue && Float.compare(mRoleInfo.getWeight_goal(), 0.0f) > 0) {
                onBack(mRoleInfo);
                return;
            }

            scaleProperty = (byte) 25;
            scaleValue = newSTValue + ":" + newLbValue;
            float value = WeightUnitUtil.ST2KG(scaleValue);
            LogUtil.e("-----------------","value = " + value);
            mRoleInfo.setWeight_goal(value);
            LogUtil.e("-----------------","WeightUnitUtil.KG2ST(value) = " + WeightUnitUtil.KG2ST(value));
            upLoadRole("weight_goal", value + "");
        } else if (view == mViewHolder.loss) {
            mViewHolder.keep.scaleSmaller();
            mViewHolder.gain.scaleSmaller();
            mViewHolder.loss.scaleLarger();
            mViewHolder.gainIndex.setVisibility(View.INVISIBLE);
            mViewHolder.keepIndex.setVisibility(View.INVISIBLE);
            mViewHolder.lossIndex.setVisibility(View.VISIBLE);
        } else if (view == mViewHolder.keep) {
            mViewHolder.keep.scaleLarger();
            mViewHolder.gain.scaleSmaller();
            mViewHolder.loss.scaleSmaller();
            mViewHolder.gainIndex.setVisibility(View.INVISIBLE);
            mViewHolder.keepIndex.setVisibility(View.VISIBLE);
            mViewHolder.lossIndex.setVisibility(View.INVISIBLE);
        } else if (view == mViewHolder.gain) {
            mViewHolder.keep.scaleSmaller();
            mViewHolder.gain.scaleLarger();
            mViewHolder.loss.scaleSmaller();
            mViewHolder.gainIndex.setVisibility(View.VISIBLE);
            mViewHolder.keepIndex.setVisibility(View.INVISIBLE);
            mViewHolder.lossIndex.setVisibility(View.INVISIBLE);
        }
    }

    private void setSmaller() {
        mViewHolder.keep.scaleSmaller();
        mViewHolder.gain.scaleSmaller();
        mViewHolder.loss.scaleSmaller();
        mViewHolder.gainIndex.setVisibility(View.INVISIBLE);
        mViewHolder.keepIndex.setVisibility(View.INVISIBLE);
        mViewHolder.lossIndex.setVisibility(View.INVISIBLE);
    }

    /**
     * 上传更新角色
     */
    private void upLoadRole(String key, String value) {

    }

    private void onBack(RoleInfo roleInfo) {
        setResult(RESULT_OK,
                new Intent().putExtra(RoleInfo.ROLE_KEY, roleInfo));
        finish();
    }

    @Override
    public void onChanged(RulerWheel wheel, int oldValue, int newValue) {
        if (mViewHolder.rulerWheel1 == wheel) {
            newLbValue = newValue / (mViewHolder.rulerWheel1.getRatio() * 1.0f);
            mViewHolder.value1.setText(String.valueOf(newLbValue));
        } else if (mViewHolder.rulerWheel == wheel) {
            newSTValue = newValue;
            mViewHolder.goalValue.setText(String.valueOf(newSTValue));
            setLbWheelValue(newSTValue,newLbValue);
        }

        if(mRoleInfo.getWeight_init()>0){
            String strTmp = newSTValue + ":" + newLbValue;
            float goalWeight = WeightUnitUtil.ST2KG(strTmp);
            if(goalWeight<mRoleInfo.getWeight_init()){
                onOtherClick(mViewHolder.loss);
            }else if(goalWeight==mRoleInfo.getWeight_init()){
                onOtherClick(mViewHolder.keep);
            }else{
                onOtherClick(mViewHolder.gain);
            }
        }


    }

    @Override
    public void onScrollingStarted(RulerWheel wheel) {

    }

    @Override
    public void onScrollingFinished(RulerWheel wheel) {

    }

}
