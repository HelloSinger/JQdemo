package com.jq.btc.account.role;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btlib.util.WeightUnitUtil;
import com.jq.code.code.business.Config;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.view.CustomReportViewForGoalWegiht;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.ruler.RulerWheel;
import com.jq.code.view.text.CustomTextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

/**
 * Created by hfei on 2015/10/20.
 */
public class WeightGoalActivity extends CommonActivity
        implements RulerWheel.OnWheelScrollListener {

    public static final String KEY_FROM_ADD_ROLE = "KEY_FROM_ADD_ROLE";
    public static final String WEIGHT_GOAL = "WEIGHT_GOAL";
    private ViewHolder mViewHolder;
    private RoleInfo mRoleInfo;
    /** 是否添加角色时顺便设置目标体重 */
    private boolean mFromAddRole;

    private class ViewHolder {
        TextView standarWeighttValue;
        CustomReportViewForGoalWegiht reportView;
        TextView goalUnit;
        CustomTextView goalValue;
        RulerWheel rulerWheel;
        View sure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSubView(R.layout.activity_goal, R.string.changeWeightGoal);
        initView();
        initValue();
    }

    private void initValue() {
        mFromAddRole = getIntent().getBooleanExtra(KEY_FROM_ADD_ROLE, false);
        // 控件初始化
        mRoleInfo = getIntent().getParcelableExtra(
                RoleInfo.ROLE_KEY);
        float[] fs = WeighDataParser.getWeightStandard(mRoleInfo.getHeight());

        String weightExchangeUnit = StandardUtil.getWeightExchangeUnit(this);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        DecimalFormat format = new DecimalFormat("##0.0", symbols);
        // 单位换算
        String s1 = format.format(fs[0]) + weightExchangeUnit;
        String s2 = format.format(fs[1]) + weightExchangeUnit;
        String standerValue = s1 + " - " + s2;
        int unit = Config.getInstance(this).getIntWeightUnit();
        int rulerMax = 200;
        float ruleValue = mRoleInfo.getWeight_goal();
        if(ruleValue == 0) {
            String sex = mRoleInfo.getSex();
            if (sex.equals("男")) {
                ruleValue = 60;
            } else {
                ruleValue = 50;
            }
        }
        if (unit == Config.JIN) {
            rulerMax = Math.round(WeightUnitUtil.KG2JIN(rulerMax));
            ruleValue = ruleValue * 2;
            s1 = format.format(fs[0] * 2) + weightExchangeUnit;
            s2 = format.format(fs[1] * 2) + weightExchangeUnit;
            standerValue = s1 + " - "
                    + s2;
        }
        // 1、标准单位
        mViewHolder.standarWeighttValue.setText(standerValue);

        float[] bmiLevel = WeighDataParser.StandardSet.BMI.getLevelNums();
        float[] levelNums = new float[bmiLevel.length];
        // 单位为cm
        int height = mRoleInfo.getHeight();
        for (int i = 0; i < levelNums.length; i++) {
            levelNums[i] = bmiLevel[i] * height * height / 10000f;
        }
//        String[] topStr = new String[levelNums.length];
//        for (int i = 0; i < levelNums.length; i++) {
//            topStr[i] = StandardUtil.getWeightExchangeValue(this, levelNums[i], "", (byte) 5);
//        }
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(WeighDataParser.StandardSet.WEIGHT.getColor()[0]);
        colors.add(WeighDataParser.StandardSet.WEIGHT.getColor()[1]);
        colors.add(WeighDataParser.StandardSet.WEIGHT.getColor()[3]);

        String[] bStrs = new String[colors.size()];
        bStrs[0] = "";
        bStrs[1] = getString(R.string.Haier_standard);
        bStrs[2] = "";

        String[] middleStr = new String[2];
        middleStr[0] = s1;
        middleStr[1] = s2;

        mViewHolder.reportView.setColors(colors);
        mViewHolder.reportView.setBottomMiddleStr(middleStr);
        mViewHolder.reportView.setContent(null, bStrs, null, 0, -1);

        // 2、卡尺
        mViewHolder.goalUnit.setText(StandardUtil.getWeightExchangeUnit(this));
        mViewHolder.rulerWheel.setValue(ruleValue, rulerMax);
        mViewHolder.goalValue.setText(format.format(ruleValue) + "");
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.standarWeighttValue = (TextView) findViewById(R.id.goal_stander_weight_value);
        mViewHolder.reportView = (CustomReportViewForGoalWegiht)findViewById(R.id.reportView);
        mViewHolder.goalValue = (CustomTextView) findViewById(R.id.goal_weight_ruler_value);
        mViewHolder.goalUnit = (TextView) findViewById(R.id.goal_weight_ruler_unit);
        mViewHolder.rulerWheel = (RulerWheel) findViewById(R.id.goal_weight_ruler);
        mViewHolder.sure = findViewById(R.id.goal_sure);
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
                showToast(R.string.mygoalweight_goalerr);
                return;
            }
            if (uint == Config.JIN) {
                weight = weight / 2;
            }

            if(mFromAddRole) {
                Intent data = new Intent();
                data.putExtra(WEIGHT_GOAL, weight);
                setResult(RESULT_OK, data);
                mRoleInfo.setWeight_goal(weight);
                finish();
            } else {
                upLoadRole("weight_goal", weight + "");
            }
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
