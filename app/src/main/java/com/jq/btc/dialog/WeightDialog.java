package com.jq.btc.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
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

/**
 * Create by AYD on 2018/12/7
 */
public class WeightDialog extends Dialog implements View.OnClickListener {
    private static TextView tv_count_time;
    private static ImageView iv_progress;
    private static RelativeLayout rl_progress;
    private static ImageView iv_close_dialog;
    private static TextView tv_dialog_weight_cal;
    private static TextView tv_title_dialog;//title
    //-----以下是显示各项数据布局
    private static LinearLayout ll_result;
    private static TextView tv_dialog_name;
    private static TextView tv_dialog_weight;
    private static TextView tv_dialog_bmi;
    private static TextView tv_dialog_style;
    private static ImageView iv_dialog_head;
    private static TextView tv_body_fat;
    private static TextView tv_bone_weight;
    private static TextView tv_muscle_rate;

    //------以下是匹配家庭成员数据
    private static LinearLayout ll_match;
    private static TextView tv_dialog_weight_match;
    private static TextView tv_add_family;//添加成员
    private static RecyclerView rv_family;
    private DialogRecyAdapter dialogRecyAdapter;

    private MatchModel matchModel;

    private static CountDownTimers countDownTimers;

    public WeightDialog(@NonNull Context context) {
        super(context);
    }

    public WeightDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WeightDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setMatchModel(MatchModel matchModel) {
        this.matchModel = matchModel;
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
        iv_progress = findViewById(R.id.iv_progress);
        ll_match = findViewById(R.id.ll_match);
        iv_close_dialog = findViewById(R.id.iv_close_dialog);
        tv_dialog_weight_cal = findViewById(R.id.tv_dialog_weight_cal);
        tv_add_family = findViewById(R.id.tv_add_family);
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
        rv_family = findViewById(R.id.rv_family);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_family.setLayoutManager(linearLayoutManager);
        dialogRecyAdapter = new DialogRecyAdapter(getContext());
//        dialogRecyAdapter.setUserDataList();
        iv_close_dialog.setOnClickListener(this);
    }

    /**
     * 设置各项数据 布局是否可见
     *
     * @param b
     */
    public  void setResultVisibility(boolean b) {
        if (b) {
            ll_result.setVisibility(View.VISIBLE);
            tv_count_time.setVisibility(View.VISIBLE);
            startTimes();
        } else {
            ll_result.setVisibility(View.GONE);
        }
    }

    /**
     * 设置计算数据布局是否可见
     *
     * @param b
     */
    public static void setProgressVisibility(boolean b) {
        if (b) {
            rl_progress.setVisibility(View.VISIBLE);
            tv_count_time.setVisibility(View.GONE);
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
            tv_count_time.setVisibility(View.VISIBLE);
            startTimes();
        } else {
            ll_match.setVisibility(View.GONE);
        }
    }


    /**
     * 设置倒计时时间
     *
     * @param
     */
    private void startTimes() {
        countDownTimers = new CountDownTimers(30000, 1000);
        countDownTimers.start();
    }

    /**
     * 设置体重
     *
     * @param t
     */
    public static void setWeigth(String t) {
        tv_dialog_weight_cal.setText(t);
    }

    /**
     * 设置匹配体重
     *
     * @param weight
     */
    public static void setMatchWeight(String weight) {
        tv_dialog_weight_match.setText(weight);
    }

    /**
     * 设置title
     *
     * @param t
     */
    public static void setTitle(String t) {
        tv_title_dialog.setText(t);
    }

    private void setAnimator() {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(iv_progress, "rotation", 0f, 360f);
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
                break;
        }
    }


    public static void setStyle(String style) {
        tv_dialog_style.setText(style);
    }

    /**
     * 设置个人数据
     *
     * @param bmi
     * @param name
     * @param weight
     */
    public static void setPersonlData(String bmi, String name, String weight, String bodyFat, String boneWeight, String muscleRate) {
        tv_dialog_bmi.setText(bmi);
        tv_dialog_name.setText(name);
        tv_dialog_weight.setText(weight);
        tv_body_fat.setText(bodyFat);
        tv_bone_weight.setText(boneWeight);
        tv_muscle_rate.setText(muscleRate);
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
