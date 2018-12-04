package com.jq.code.view.wheel;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jq.code.view.text.CustomTextView;
import com.jq.code.view.wheel.adapter.WheelViewAdapter;

/**
 * Created by hfei on 2016/5/13.
 */
public class NumberPicker extends LinearLayout implements TosAdapterView.OnItemSelectedListener {
    private Context context;
    private WheelView wheelView;
    private CustomTextView textView;
    private WheelViewAdapter viewAdapter;
    private int number;
    private int max = 200;
    private int min = 0;
    private int centerColer = 0xff3c3c3c;
    private int othersColor = 0x1A000000;
    private int textViewColor = centerColer;
    /** 以sp为单位 */
    private int mTextSize;

    public NumberPicker(Context context, int max, int min, int number) {
        super(context);
        this.number = number;
        this.min = min;
        this.max = max;
        initialize(context);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params =
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0, dp(1), 0, 0);
        wheelView = new WheelView(context);
        wheelView.setLayoutParams(params);
        wheelView.setPadding(wheelView.getPaddingLeft(), wheelView.getPaddingTop(),
                wheelView.getPaddingRight() + dp(2),
                wheelView.getPaddingBottom());

        textView = new CustomTextView(context);
        textView.setLayoutParams(
                new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(),
                textView.getPaddingRight() + dp(2),
                textView.getPaddingBottom());
        textView.setTextSize(30);
        textView.setTextColor(textViewColor);
        textView.setTypeface(CustomTextView.EX);
        textView.setGravity(Gravity.CENTER);

        viewAdapter = new WheelViewAdapter(context, min, max);
        wheelView.setBackgroundColor(Color.TRANSPARENT);
        wheelView.setAdapter(viewAdapter);
        wheelView.setSelection(number - min);
        viewAdapter.setTextColor(number - min, centerColer, othersColor);
        wheelView.setOnItemSelectedListener(this);

        addView(wheelView);
        addView(textView);
    }

    public Object getText() {
        return textView.getText();
    }

    public void setText(String text) {
        textView.setText(text);
    }

    /**
     * 设置字体大小
     * @param mTextSize 字体大小，以sp为单位
     */
    public void setTextSize(int mTextSize) {
        int ts = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getResources().getDisplayMetrics());
        this.mTextSize = mTextSize;
        textView.setTextSize(ts);
        viewAdapter.setTextSize(mTextSize);
    }

    public void setText(int textId) {
        textView.setText(textId);
    }

    public void setCenter(int number){
        this.number = number;
        wheelView.setSelection(number - min);
        viewAdapter.setTextColor(number - min, centerColer, othersColor);
    }

    public void setRange(int min,int max){
        viewAdapter.setDateRanger(min,max);
    }

    @Override
    public void onItemSelected(TosAdapterView<?> parent, View view,
                               int position, long id) {
        viewAdapter.setTextColor(position, centerColer, othersColor);
        number = viewAdapter.getItem(position);
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onSelected(this,number);
        }
    }

    @Override
    public void onNothingSelected(TosAdapterView<?> parent) {
        if (onItemSelectedListener != null) {
            onItemSelectedListener.onNothing(this);
        }
    }

    private OnNumberItemSelectedListener onItemSelectedListener;

    public void setOnNumberItemSelectedListener(OnNumberItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnNumberItemSelectedListener {
        void onSelected(ViewGroup parent, int number);

        void onNothing(ViewGroup parent);
    }

    public int dp(int value){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return (int) (dm.density * value);
    }
}
