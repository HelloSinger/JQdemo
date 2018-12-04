package com.jq.btc.mine.setting;

import android.os.Bundle;
import android.view.View;

import com.jq.btc.app.R;
import com.jq.btc.helper.CsBtEngine;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.text.CustomTextView;


public class UnitSetActivity extends CommonActivity implements
        View.OnClickListener {

    private ViewHolder mViewHolder;
    private Account mAccount;
    private CsBtEngine mCsBtEngine;

    private class ViewHolder {
        CustomTextView lengthMButton;
        CustomTextView lengthCButton;
        CustomTextView weightJButton;
        CustomTextView weightKGButton;
        CustomTextView weightBButton;
        CustomTextView weightSTButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentSubView(R.layout.activity_unit_set, R.string.settingUnit);
        initView();
    }

    private void initView() {
        // 绑定控件
        mViewHolder = new ViewHolder();
        mViewHolder.lengthMButton = (CustomTextView) findViewById(R.id.button_gmi);
        mViewHolder.lengthCButton = (CustomTextView) findViewById(R.id.button_ychi);
        mViewHolder.weightJButton = (CustomTextView) findViewById(R.id.button_sjin);
        mViewHolder.weightKGButton = (CustomTextView) findViewById(R.id.button_gjin);
        mViewHolder.weightBButton = (CustomTextView) findViewById(R.id.button_ybang);
        mViewHolder.weightSTButton = (CustomTextView) findViewById(R.id.button_st);

        mViewHolder.lengthMButton.setOnClickListener(this);
        mViewHolder.lengthCButton.setOnClickListener(this);
        mViewHolder.weightJButton.setOnClickListener(this);
        mViewHolder.weightKGButton.setOnClickListener(this);
        mViewHolder.weightBButton.setOnClickListener(this);
        mViewHolder.weightSTButton.setOnClickListener(this);

        mAccount = Account.getInstance(this);
        mCsBtEngine = CsBtEngine.getInstance(this);
        int lengthUnit = Config.getInstance(this).getIntLengthUnit();
        if (lengthUnit == Config.INCH) {
            lengthInch();
        } else {
            lenthMetric();
        }

        int weightUnit = Config.getInstance(this).getIntWeightUnit();
        if (weightUnit == Config.INCH) {
            weightInch();
        } else if (weightUnit == Config.JIN) {
            weightJin();
        } else if (weightUnit == Config.ST) {
            weightST();
        } else {
            weightMetric();
        }
    }

    @Override
    protected void onOtherClick(View v) {
        int lengthUnit = Config.getInstance(this).getIntLengthUnit();
        int weightUnit = Config.getInstance(this).getIntWeightUnit();
        if (v == mViewHolder.lengthMButton) {
            lenthMetric();
            lengthUnit = Config.METRIC;
            Config.getInstance(this).setIntLengthUnit(lengthUnit);
        } else if (v == mViewHolder.lengthCButton) {
            lengthInch();
            lengthUnit = Config.INCH;
            Config.getInstance(this).setIntLengthUnit(lengthUnit);
        } else if (v == mViewHolder.weightJButton) {
            weightJin();
            weightUnit = Config.JIN;
            Config.getInstance(this).setIntWeightUnit(weightUnit);
            mCsBtEngine.writeWeightUnit();
        } else if (v == mViewHolder.weightKGButton) {
            weightMetric();
            weightUnit = Config.METRIC;
            Config.getInstance(this).setIntWeightUnit(weightUnit);
            mCsBtEngine.writeWeightUnit();
        } else if (v == mViewHolder.weightBButton) {
            weightInch();
            weightUnit = Config.INCH;
            Config.getInstance(this).setIntWeightUnit(weightUnit);
        } else if (v == mViewHolder.weightSTButton) {
            weightST();
            weightUnit = Config.ST;
            Config.getInstance(this).setIntWeightUnit(weightUnit);
        }

    }

    private void lenthMetric() {
        mViewHolder.lengthMButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
        mViewHolder.lengthCButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void lengthInch() {
        mViewHolder.lengthMButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.lengthCButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
    }

    private void weightJin() {
        mViewHolder.weightJButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
        mViewHolder.weightKGButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightBButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightSTButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void weightMetric() {
        mViewHolder.weightJButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightKGButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
        mViewHolder.weightBButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightSTButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void weightInch() {
        mViewHolder.weightJButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightKGButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightBButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
        mViewHolder.weightSTButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
    }

    private void weightST() {
        mViewHolder.weightJButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightKGButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightBButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mViewHolder.weightSTButton.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.weight_stat_check,0,0,0);
    }
}
