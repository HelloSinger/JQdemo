package com.jq.code.view.trend;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;


/**
 * Created by Administrator on 2016/10/9.
 */

public class BaseCustomView extends View {
    protected float mDensity;
    protected Paint textPaint ;
    protected int mHeight;
    protected int mWidth;
    public BaseCustomView(Context context) {
        super(context);
    }

    public BaseCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDensity = getResources().getDisplayMetrics().density;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(10 * mDensity);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.trend_view_text_clor));
    }
}
