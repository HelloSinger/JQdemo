package com.jq.code.view.trend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;


/**
 * Created by Administrator on 2016/10/9.
 */

public class SportBarView extends View {
    private static final int DEFAULT_PART = 60 ;
    private static final int BAR_HEIGHT = 10 ;
    private float barWidth;
    private float barHeight ;
    protected float mDensity;
    protected Paint footPaint , sportPaint, defaultPaint, daiPaint;
    protected int mHeight;
    protected int mWidth;
    private int footValue, sportValue , daiValue ;
    protected float textXAxis[];
    public void setValue(int footValue,int sportValue,int daiValue){
        this.footValue = DEFAULT_PART * footValue / 100 ;
        this.sportValue = DEFAULT_PART * sportValue / 100;
        this.daiValue = DEFAULT_PART * daiValue / 100;
    }
    public SportBarView(Context context) {
        super(context);
    }



    public SportBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        footPaint = new Paint();
        footPaint.setAntiAlias(true);
        footPaint.setStyle(Paint.Style.FILL);
        footPaint.setColor(getResources().getColor(R.color.sport_foot_pop_color));

        sportPaint = new Paint();
        sportPaint.setAntiAlias(true);
        sportPaint.setStyle(Paint.Style.FILL);
        sportPaint.setColor(getResources().getColor(R.color.sport_sport_pop_color));

        defaultPaint = new Paint();
        defaultPaint.setAntiAlias(true);
        defaultPaint.setStyle(Paint.Style.FILL);
        defaultPaint.setColor(getResources().getColor(R.color.sport_trend_value3_color));

        daiPaint = new Paint();
        daiPaint.setAntiAlias(true);
        daiPaint.setStyle(Paint.Style.FILL);
        daiPaint.setColor(getResources().getColor(R.color.sport_zi_color));

        mDensity = getResources().getDisplayMetrics().density;
        barHeight = BAR_HEIGHT * mDensity ;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getHeight() ;
        mWidth = getWidth() ;
        computeValue() ;
        drawRecy(canvas) ;
    }

    private void drawRecy(Canvas canvas) {
        for (int i = 0; i < DEFAULT_PART; i++) {
            canvas.drawRect(textXAxis[i],0,textXAxis[i] + barWidth, barHeight ,defaultPaint);
        }
        for (int i = 0; i < DEFAULT_PART; i++) {
            canvas.drawRect(textXAxis[i],mHeight - barHeight,textXAxis[i] + barWidth , mHeight ,defaultPaint);
        }
        for (int i = 0; i < footValue; i++) {
            canvas.drawRect(textXAxis[i],0,textXAxis[i] +  barWidth, barHeight ,footPaint);
        }
        for (int i = 0; i < sportValue; i++) {
            canvas.drawRect(textXAxis[i],mHeight - barHeight,textXAxis[i] + barWidth , mHeight ,sportPaint);
        }
        for (int i = 0; i < daiValue; i++) {
            canvas.drawRect(textXAxis[i],mHeight - barHeight,textXAxis[i] + barWidth , mHeight ,daiPaint);
        }
    }

    private void computeValue() {
        textXAxis = new float[DEFAULT_PART] ;
        barWidth = mWidth / (2 * DEFAULT_PART) ;
        for (int i = 0; i < DEFAULT_PART; i++) {
            textXAxis[i] = 2 * i * barWidth ;
        }
    }
}
