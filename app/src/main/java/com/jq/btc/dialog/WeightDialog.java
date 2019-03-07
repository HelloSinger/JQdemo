package com.jq.btc.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jq.btc.adapter.DialogRecyAdapter;
import com.jq.btc.app.R;
import com.jq.btc.model.MatchModel;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.SpUtils;

/**
 * Create by AYD on 2018/12/7
 * 计算, 匹配, 详细数据弹窗
 */
public class WeightDialog extends Dialog implements View.OnClickListener, DialogRecyAdapter.RecyItemOnClickListener {
    //----以下显示计算中的布局
    private TextView tv_count_time;
    private RelativeLayout rl_progress;
    private RelativeLayout rl_progress_1;
    private ImageView iv_close_dialog;
    private TextView tv_dialog_weight_cal;
    private TextView tv_title_dialog;//title
    //-----以下是显示各项数据布局
    private LinearLayout ll_result;
    private TextView tv_dialog_name;
    private TextView tv_dialog_weight;
    private TextView tv_dialog_bmi;
    private TextView tv_dialog_style;
    private ImageView iv_dialog_head;
    private TextView tv_body_fat;
    private TextView tv_bone_weight;
    private TextView tv_muscle_rate;
    //------以下是匹配家庭成员数据
    private LinearLayout ll_match;
    private TextView tv_dialog_weight_match;
    private TextView tv_add_family;//添加成员
    private RecyclerView rv_family;
    private DialogRecyAdapter dialogRecyAdapter;

    private UserData userData;

    private static CountDownTimers countDownTimersMatch;
    private static CountDownTimers countDownTimersData;
    private DiaLogRecyItemOnClick diaLogRecyItemOnClick;
    private DiaLogAddUserOnClick diaLogAddUserOnClick;
    private LinearLayout ll_dialog_loading;

    private WeightDialog weightDialog;

    public WeightDialog(@NonNull Context context) {
        super(context);
    }

    public WeightDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WeightDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
        Log.e("ADY", "setUserDataList: " + userData.getData().getMemberList().size());

    }

    public void setDiaLogAddUserOnClick(DiaLogAddUserOnClick diaLogAddUserOnClick) {
        this.diaLogAddUserOnClick = diaLogAddUserOnClick;
    }

    public void setDiaLogRecyItemOnClick(DiaLogRecyItemOnClick diaLogRecyItemOnClick) {
        this.diaLogRecyItemOnClick = diaLogRecyItemOnClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_dialog_layout);
        initView();
        setAnimator();
    }

    private void initView() {
        tv_count_time = findViewById(R.id.tv_count_time);
        ll_result = findViewById(R.id.ll_result);
        rl_progress = findViewById(R.id.rl_progress);
        rl_progress_1 = findViewById(R.id.rl_progress_1);
        ll_match = findViewById(R.id.ll_match);
        iv_close_dialog = findViewById(R.id.iv_close_dialog);
        tv_dialog_weight_cal = findViewById(R.id.tv_dialog_weight_cal);
        rv_family = findViewById(R.id.rv_family);
        tv_title_dialog = findViewById(R.id.tv_title_dialog);
        tv_dialog_name = findViewById(R.id.tv_dialog_name);
        tv_dialog_weight = findViewById(R.id.tv_dialog_weight);
        tv_dialog_bmi = findViewById(R.id.tv_dialog_bmi);
        tv_dialog_style = findViewById(R.id.tv_dialog_style);
        iv_dialog_head = findViewById(R.id.iv_dialog_head);
        tv_body_fat = findViewById(R.id.tv_body_fat);
        tv_bone_weight = findViewById(R.id.tv_bone_weight);
        tv_muscle_rate = findViewById(R.id.tv_muscle_rate);
        tv_dialog_weight_match = findViewById(R.id.tv_dialog_weight_match);
        tv_add_family = findViewById(R.id.tv_add_family);
        ll_dialog_loading = findViewById(R.id.ll_dialog_loading);
        tv_add_family.setOnClickListener(this);
        rv_family = findViewById(R.id.rv_family);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_family.setLayoutManager(linearLayoutManager);
        iv_close_dialog.setOnClickListener(this);
    }

    /**
     * 设置各项数据 布局是否可见
     *
     * @param b
     */
    public void setResultVisibility(boolean b) {
        if (b) {
            ll_result.setVisibility(View.VISIBLE);
            tv_count_time.setVisibility(View.VISIBLE);
            countDownTimersMatch.cancel();
            iv_close_dialog.setVisibility(View.VISIBLE);
            startTimesData();
        } else {
            ll_result.setVisibility(View.GONE);
            if (countDownTimersData != null) {
                countDownTimersData.cancel();
            }
        }
    }

    /**
     * 设置计算数据布局是否可见
     *
     * @param b
     */
    public void setProgressVisibility(boolean b) {
        if (b) {
            rl_progress.setVisibility(View.VISIBLE);
            tv_count_time.setVisibility(View.GONE);
            iv_close_dialog.setVisibility(View.GONE);

        } else {
            rl_progress.setVisibility(View.GONE);
        }
    }

    /**
     * 设置匹配布局是否可见
     *
     * @param b
     */
    public void setMatchVisibility(boolean b) {
        if (b) {
            ll_match.setVisibility(View.VISIBLE);
            dialogRecyAdapter = new DialogRecyAdapter(getContext(), userData);
            dialogRecyAdapter.setRecyItemOnClickListener(this);
            rv_family.setAdapter(dialogRecyAdapter);
            tv_count_time.setVisibility(View.VISIBLE);
            iv_close_dialog.setVisibility(View.VISIBLE);
            startTimesMatch();
        } else {
            ll_match.setVisibility(View.GONE);
            if (countDownTimersMatch != null) {
                countDownTimersMatch.cancel();
            }
        }
    }

    public void setLoading(boolean b) {
        if (b) {
            ll_dialog_loading.setVisibility(View.VISIBLE);
        } else {
            ll_dialog_loading.setVisibility(View.GONE);
        }
    }

    /**
     * 设置匹配倒计时时间
     *
     * @param
     */
    private void startTimesMatch() {
        countDownTimersMatch = new CountDownTimers(30000, 1000);
        countDownTimersMatch.start();
    }

    private void startTimesData() {
        countDownTimersData = new CountDownTimers(30000, 1000);
        countDownTimersData.start();
    }

    /**
     * 设置体重
     *
     * @param t
     */
    public void setWeigth(String t) {
        tv_dialog_weight_cal.setText(t);
    }

    /**
     * 设置匹配体重
     *
     * @param weight
     */
    public void setMatchWeight(String weight) {
        tv_dialog_weight_match.setText(weight);
    }

    /**
     * 设置title
     *
     * @param t
     */
    public void setTitle(String t) {
        tv_title_dialog.setText(t);
    }

    private void setAnimator() {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(rl_progress_1, "rotation", 0f, 360f);
        LinearInterpolator lin = new LinearInterpolator();
        animator.setInterpolator(lin);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(5000);
        animator.start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close_dialog:
                dismiss();
                weightDialog = null;
                break;
            case R.id.tv_add_family:
                Intent intent = new Intent();
                intent.setAction("com.unilife.fridge.app.family.add");
                getContext().startActivity(intent);
                SpUtils.getInstance(getContext()).isDiaLogAddUser(true);
                dismiss();
//                diaLogAddUserOnClick.onDialogAddUsetListener();
                break;
        }
    }


    public void setStyle(String style) {
        tv_dialog_style.setText(style);
    }

    /**
     * 设置个人数据
     *
     * @param bmi
     * @param name
     * @param weight
     */
    public void setPersonlData(String bmi, String name, String weight, String bodyFat, String boneWeight, String muscleRate) {
        tv_dialog_bmi.setText(bmi);
        tv_dialog_name.setText(name);
        tv_dialog_weight.setText(weight);
        if (bodyFat.equals("-1%") || bodyFat.equals("0") || bodyFat.equals("0%")) {
            tv_body_fat.setText("一 一");
        } else {
            tv_body_fat.setText(bodyFat);
        }
        if (boneWeight.equals("0") || boneWeight.equals("0.0")) {
            tv_bone_weight.setText("一 一");
        } else {
            tv_bone_weight.setText(boneWeight + "KG");
        }
        if (muscleRate.equals("0%") || muscleRate.equals("0.0") || muscleRate.equals("0")) {
            tv_muscle_rate.setText("一 一");
        } else {
            tv_muscle_rate.setText(muscleRate + "%");

        }
    }

    /**
     * 匹配弹窗item点击事件
     *
     * @param pos
     */
    @Override
    public void itemOnClickListener(int pos) {
        diaLogRecyItemOnClick.itemOnClickListener(pos);
    }

    public interface DiaLogRecyItemOnClick {
        void itemOnClickListener(int pos);
    }


    public interface DiaLogAddUserOnClick {
        void onDialogAddUsetListener();
    }


    /**
     * 倒计时
     */
    class CountDownTimers extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public CountDownTimers(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tv_count_time.setText("" + millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            dismiss();
        }
    }

}
