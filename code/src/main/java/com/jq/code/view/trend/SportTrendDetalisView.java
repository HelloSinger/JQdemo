package com.jq.code.view.trend;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jq.code.R;
import com.jq.code.model.trend.SportDetalisViewEntity;

import java.util.List;


/**
 * Created by Administrator on 2016/10/9.
 */

public class SportTrendDetalisView extends View {
    private static final int SINTAKE_WIDTH = 14 ;
    private static final int SPACE_WIDTH = 2 ;
    private static final int INTAKE_WIDTH = 25 ;
    private float sIntakeWidth;
    private float spaceWidth ;
    private float intakeWidth ;
    private float unitWidth ;
    private int textHeight ;
    protected float mDensity;
    protected Paint textPaint , value1Paint, value2Paint,value3Paint;
    private boolean showSIntake ;
    protected int mHeight;
    protected int mWidth;
    protected int maxValue = 0 ,minValue = 0 ;

    private List<SportDetalisViewEntity> entities ;
    public SportTrendDetalisView(Context context) {
        super(context);
    }

    public void setEntities(int maxValue, int minValue,  List<SportDetalisViewEntity> entities) {
        this.maxValue = maxValue ;
        this.minValue = minValue ;
        this.entities = entities;
    }

    public SportTrendDetalisView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SportTrendDetalisView);
        int intackColor = a.getColor(R.styleable.SportTrendDetalisView_IntakeColor,  getResources().getColor(R.color.sport_trend_value2_color));
        showSIntake = a.getBoolean(R.styleable.SportTrendDetalisView_showSuggestIntake,false) ;  //
        a.recycle();

        value1Paint = new Paint();
        value1Paint.setAntiAlias(true);
        value1Paint.setStyle(Paint.Style.FILL);
        value1Paint.setColor(intackColor);

        value2Paint = new Paint();
        value2Paint.setAntiAlias(true);
        value2Paint.setStyle(Paint.Style.FILL);
        value2Paint.setColor(getResources().getColor(R.color.sport_trend_value3_color));

        value3Paint = new Paint();
        value3Paint.setAntiAlias(true);
        value3Paint.setStyle(Paint.Style.FILL);
        value3Paint.setColor(getResources().getColor(R.color.white));

        mDensity = getResources().getDisplayMetrics().density;
        sIntakeWidth = SINTAKE_WIDTH * mDensity ;
        spaceWidth = SPACE_WIDTH * mDensity ;
        intakeWidth = INTAKE_WIDTH * mDensity ;

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(12 * mDensity);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color.trend_view_text_clor));
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(entities != null && entities.size() > 0){
            unitWidth = sIntakeWidth + intakeWidth  + spaceWidth ;
            setMeasuredDimension((int) ((entities.size() * 2  - 1 )* unitWidth + 4 * mDensity), getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getHeight() ;
        mWidth = getWidth() ;
        Log.e("TrendLeftView","mHeight =================== " +  mHeight) ;
        Log.e("TrendLeftView","mWidth =================== " +  mWidth) ;
        computeAxis() ;
        drawTrendRecy(canvas);
    }
    protected void computeAxis() {
        if(entities == null || entities.isEmpty())return;
        for (int i = 0; i < entities.size(); i++) {
            SportDetalisViewEntity tempXText = entities.get(i) ;
            tempXText.setLeftAxis(2 * i * unitWidth);
        }
        Rect rect = new Rect();
        textPaint.getTextBounds(entities.get(0).getTextStr() , 0, 1, rect);
        textHeight= rect.height();
        int trueHight = mHeight - 3 * textHeight;
        float scale = (trueHight) / (float)(maxValue - minValue);
        float computHight = mHeight - textHeight * 3  / 2.0f ;
        for (int i = 0; i < entities.size(); i++) {
            SportDetalisViewEntity rightXText = entities.get(i);
            rightXText.setValue1Axis(computHight - (rightXText.getValue1() - minValue) * scale);
            if(showSIntake){
                rightXText.setValue2Axis(computHight - (rightXText.getValue2() - minValue) * scale);
                rightXText.setValue3Axis(computHight - (rightXText.getValue3() - minValue) * scale);
            }
        }
    }
    private void drawTrendRecy(Canvas canvas) {
        if(entities == null || entities.isEmpty())return;
        for (int i = 0; i < entities.size(); i++) {
            SportDetalisViewEntity tempXText = entities.get(i) ;
            if(showSIntake){
                canvas.drawRect(tempXText.getLeftAxis(),tempXText.getValue2Axis(),tempXText.getLeftAxis() + sIntakeWidth, mHeight - 3 * textHeight / 2  , value2Paint);
                canvas.drawRect(tempXText.getLeftAxis() + 2,tempXText.getValue3Axis(),tempXText.getLeftAxis() + sIntakeWidth - 2, mHeight - 3 * textHeight / 2  , value3Paint);
            }
            canvas.drawRect(tempXText.getLeftAxis() + sIntakeWidth + spaceWidth,tempXText.getValue1Axis(),tempXText.getLeftAxis() +  unitWidth ,mHeight - 3 * textHeight / 2 , value1Paint);
            String textStr = tempXText.getTextStr().length() > 2 ? tempXText.getTextStr().substring(0 , 2) + "...": tempXText.getTextStr() ;
            canvas.drawText(textStr,tempXText.getLeftAxis() +  unitWidth - intakeWidth / 2,mHeight - textHeight/3,textPaint);
            canvas.drawText(tempXText.getValue1() + "",tempXText.getLeftAxis() +  unitWidth - intakeWidth / 2,tempXText.getValue1Axis() - textHeight / 2,textPaint);
        }
    }
}
