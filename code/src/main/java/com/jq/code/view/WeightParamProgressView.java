package com.jq.code.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jq.code.R;
import com.jq.code.view.text.CustomTextView;

public class WeightParamProgressView extends LinearLayout {
    protected Context mContext;
    private LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private int totalWidth;

    public WeightParamProgressView(Context context) {
        super(context);
        init(context);
    }

    public WeightParamProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WeightParamProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);

    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.item_weight_param, this, false);
        this.setOrientation(LinearLayout.VERTICAL);
        mViewHolder = new ViewHolder();
        mViewHolder.fatherLL = (FrameLayout) findViewById(R.id.progress_father_ll);
        mViewHolder.progressBg = (ImageView) view.findViewById(R.id.progress_standard);
        mViewHolder.percent1 = (CustomTextView) view.findViewById(R.id.progress_percent1);
        mViewHolder.percent2 = (CustomTextView) view.findViewById(R.id.progress_percent2);
        mViewHolder.percent3 = (CustomTextView) view.findViewById(R.id.progress_percent3);
        mViewHolder.percent4 = (CustomTextView) view.findViewById(R.id.progress_percent4);
        mViewHolder.name1 = (CustomTextView) view.findViewById(R.id.progress_standard_name1);
        mViewHolder.name2 = (CustomTextView) view.findViewById(R.id.progress_standard_name2);
        mViewHolder.name3 = (CustomTextView) view.findViewById(R.id.progress_standard_name3);
        mViewHolder.name4 = (CustomTextView) view.findViewById(R.id.progress_standard_name4);
        mViewHolder.name5 = (CustomTextView) view.findViewById(R.id.progress_standard_name5);
        mViewHolder.progressPoint = (CustomTextView) view.findViewById(R.id.progress_point);
        mViewHolder.progressFL = (LinearLayout) view.findViewById(R.id.progress_fl);
        this.addView(view);
        ViewTreeObserver vto = mViewHolder.progressFL.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                int margin = mViewHolder.progressPoint.getMeasuredWidth() / 2;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mViewHolder.progressFL.getLayoutParams();
                layoutParams.rightMargin = layoutParams.leftMargin = margin;
                totalWidth = mViewHolder.progressFL.getMeasuredWidth() - margin * 2;
                mViewHolder.progressFL.invalidate();
                getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public void setPosinVisibility(int visibility) {
        if (mViewHolder != null)
            mViewHolder.progressPoint.setVisibility(visibility);
    }

    /**
     * 设置百分段
     *
     * @param backgoundId
     */
    public void setProgressBackgound(int backgoundId) {
        mViewHolder.progressBg.setBackgroundResource(backgoundId);
    }

    /**
     * 设置标准名称
     *
     * @param standards
     */
    public void setStandardName(int[] standards) {
        if (standards.length == 0) {
            mViewHolder.name1.setVisibility(View.GONE);
        } else {
            mViewHolder.name1.setText(standards[0]);
        }
        if (standards.length <= 1) {
            mViewHolder.name2.setVisibility(View.GONE);
        } else {
            mViewHolder.name2.setText(standards[1]);
        }
        if (standards.length <= 2) {
            mViewHolder.name3.setVisibility(View.GONE);
        } else {
            mViewHolder.name3.setText(standards[2]);
        }
        if (standards.length <= 3) {
            mViewHolder.name4.setVisibility(View.GONE);
        } else {
            mViewHolder.name4.setText(standards[3]);
        }
        if (standards.length <= 4) {
            mViewHolder.name5.setVisibility(View.GONE);
        } else {
            mViewHolder.name5.setText(standards[4]);
        }
    }

    /**
     * 设置百分段
     *
     * @param percent
     */
    public void setPercent(String... percent) {
        if (percent.length == 0) {
            mViewHolder.percent1.setVisibility(View.GONE);
        } else {
            mViewHolder.percent1.setText(percent[0]);
        }
        if (percent.length <= 1) {
            mViewHolder.percent2.setVisibility(View.GONE);
        } else {
            mViewHolder.percent2.setText(percent[1]);
        }
        if (percent.length <= 2) {
            mViewHolder.percent3.setVisibility(View.GONE);
        } else {
            mViewHolder.percent3.setText(percent[2]);
        }
        if(percent.length <= 3){
            mViewHolder.percent4.setVisibility(View.GONE);
        }else {
            mViewHolder.percent4.setText(percent[3]);
        }
    }

    /**
     * num为百分比 num/100 满值为100
     *
     * @param num
     */
    public void setProgress(float num) {
        if (num > totalWidth) {
            num = totalWidth;
        }
        if (num < 0) {
            num = 0;
        }
        setAnim(num);
    }
    public void setAnim(float num){
        if (mViewHolder.progressPoint != null){
            ValueAnimator anim = ValueAnimator.ofFloat(0f, num);
            anim.setDuration(200);
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float currentValue = (float) animation.getAnimatedValue();
                    {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mViewHolder.progressPoint.getLayoutParams();
                        lp.leftMargin = (int) currentValue;
                        mViewHolder.progressPoint.setLayoutParams(lp);
                    }
                }
            });
            anim.start();
        }
    }
    /**
     * 设置游标值
     *
     * @param text
     */
    public void setValueText(String text) {
        mViewHolder.progressPoint.setText(text);
    }


    /**
     * 设置进度下标范围的范围
     *
     * @param value
     */
    public void setProgress(final float value, final float[] points) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                float sum = calculatePogress(value, points);
                setProgress(sum);
            }
        }, 200);
    }


    /**
     * @param curValue 当前值
     * @param points
     * @return
     */
    private float calculatePogress(float curValue, float[] points) {
        float totalValue = totalWidth / (points.length - 1);
        if (curValue <= points[0]) {
            return 0;
        } else if (curValue > points[points.length - 1]) {
            return totalValue * points.length - 1;
        } else {
            float value = 0;
            for (int i = 1; i < points.length; i++) {
                if (curValue <= points[i]) {
                    value = (curValue - points[i - 1]) * totalValue / (points[i] - points[i - 1]) + totalValue * (i - 1);
                    break;
                }
            }
            return value;
        }
    }

    public class ViewHolder {
        public CustomTextView progressPoint;
        public LinearLayout progressFL;
        public FrameLayout fatherLL;
        CustomTextView percent1;
        CustomTextView percent2;
        CustomTextView percent3;
        CustomTextView percent4;
        CustomTextView name1;
        CustomTextView name2;
        CustomTextView name3;
        CustomTextView name4;
        CustomTextView name5;
        ImageView progressBg;
    }
}
