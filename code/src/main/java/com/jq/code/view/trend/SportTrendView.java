package com.jq.code.view.trend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.code.R;
import com.jq.code.model.Constant;
import com.jq.code.model.XHelpEntity;
import com.jq.code.model.trend.SportEntityLeftYText;
import com.jq.code.model.trend.SportEntityRightXText;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/10/9.
 */

public class SportTrendView extends BaseCustomView {
    protected float textXAxis[];
    private List<XHelpEntity> xText ;

    public void setxText(List<XHelpEntity> xText) {
        this.xText = xText;
    }

    private List<SportEntityLeftYText> yTexts ;
    protected int maxValue = 0 ,minValue = 0 ;
    private int textHeight ,textWidth ;

    private Paint value1Paint, value2Paint, value3Paint;
    private float unitWidth  ;
    private List<SportEntityRightXText> rightXTexts;
    private int section ;
    private boolean showText ;

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void setRightXTexts(List<SportEntityRightXText> rightXTexts) {
        this.rightXTexts = rightXTexts;
    }

    public void setTexts(int maxValue, int minValue){
        this.maxValue = maxValue ;
        this.minValue = minValue ;
        this.yTexts = initYText() ;
    }

    private List<SportEntityLeftYText> initYText() {
        List<SportEntityLeftYText> yText = new ArrayList<SportEntityLeftYText>() ;
        float fyifen = (maxValue - minValue ) /  5.0f ;   //浮点型
        int yifen = Math.round(fyifen) ;
        yText.add(new SportEntityLeftYText(minValue)) ;
        for (int i = 1; i < 5; i++) {
            yText.add(new SportEntityLeftYText(minValue + i * yifen)) ;
        }
        yText.add(new SportEntityLeftYText(maxValue)) ;
        return yText ;
    }
    public SportTrendView(Context context) {
        super(context);
    }

    public SportTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        value1Paint = new Paint();
        value1Paint.setAntiAlias(true);
        value1Paint.setStyle(Paint.Style.FILL);
        value1Paint.setColor(getResources().getColor(R.color.sport_trend_value1_color));

        value2Paint = new Paint();
        value2Paint.setAntiAlias(true);
        value2Paint.setStyle(Paint.Style.FILL);
        value2Paint.setColor(getResources().getColor(R.color.sport_trend_value2_color));

        value3Paint = new Paint();
        value3Paint.setAntiAlias(true);
        value3Paint.setStyle(Paint.Style.FILL);
        value3Paint.setColor(getResources().getColor(R.color.sport_trend_value3_color));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getHeight() ;
        mWidth = getWidth() ;
        computeYAxis() ;
        computeXAxis();
        drawText(canvas);
        drawTrendRecy(canvas);
    }

    protected void computeXAxis() {
        textXAxis = new float[section] ;
        if(rightXTexts == null )return;
        unitWidth = ((mWidth -  textWidth - 5 * mDensity) / (section * 3 -1));  //左边ytext给了 二分之一 mSpace
        for (int i = 0; i < section; i++) {
            textXAxis[i] = textWidth + 5 * mDensity + 3  * unitWidth * i ;
        }
        if(rightXTexts.isEmpty()) return;
        for (int i = 0; i < rightXTexts.size() ; i++) {
            SportEntityRightXText tempXText = rightXTexts.get(i) ;
            tempXText.setLeftAxis(textXAxis[tempXText.getPosition()]);
        }
    }
    private void computeYAxis() {
        if(yTexts != null && !yTexts.isEmpty()){
            String textY = yTexts.get(yTexts.size() -1).getValue() + "" ;
            Rect rect = new Rect();
            textPaint.getTextBounds(textY, 0, textY.length(), rect);
            textHeight= rect.height();
            textWidth = rect.width() ;
            int trueHight = mHeight - 3 * textHeight;
            float scale = (trueHight) / (float)(maxValue - minValue);
            float computHight = mHeight - 2 * textHeight  ;
            for (int i = 0; i < yTexts.size(); i++) {
                SportEntityLeftYText yTrendText = yTexts.get(i);
                yTrendText.setyAxis(computHight - (yTrendText.getValue() - minValue) * scale);
            }
            for (int i = 0; i < rightXTexts.size(); i++) {
                SportEntityRightXText sportEntityRightXText = rightXTexts.get(i);
                sportEntityRightXText.setValueAxis1(computHight - (sportEntityRightXText.getValue1() - minValue) * scale);
                sportEntityRightXText.setValueAxis2(computHight - (sportEntityRightXText.getValue2() - minValue) * scale);
                sportEntityRightXText.setValueAxis3(computHight - (sportEntityRightXText.getValue3() - minValue) * scale);
            }
        }
    }
    protected  void drawText(Canvas mCanvas){
        if(!showText) return;
        if(yTexts == null || rightXTexts == null) return;

        if(!yTexts.isEmpty()){
            for (int i = 0; i < yTexts.size(); i++) {
             mCanvas.drawText(yTexts.get(i).getValue() + "" ,textWidth / 2  , yTexts.get(i).getyAxis() + textHeight /2 , textPaint);
            }
        }
        if(!xText.isEmpty()){
            for (int i = 0; i < xText.size(); i++) {
                mCanvas.drawText(xText.get(i).getTextStr(),textXAxis[xText.get(i).getPosition()] + unitWidth,mHeight,textPaint);
            }
        }
    }
    private void drawTrendRecy(Canvas canvas) {
        if(rightXTexts == null || rightXTexts.isEmpty())return;
        for (int i = 0; i < rightXTexts.size(); i++) {
            SportEntityRightXText tempXText = rightXTexts.get(i) ;
            canvas.drawRect(tempXText.getLeftAxis(),tempXText.getValueAxis1(),tempXText.getLeftAxis() + unitWidth,mHeight - 2 * textHeight ,value1Paint);
            canvas.drawRect(tempXText.getLeftAxis() + unitWidth,tempXText.getValueAxis2(),tempXText.getLeftAxis() + 2 * unitWidth ,mHeight - 2 * textHeight,value2Paint);
            canvas.drawRect(tempXText.getLeftAxis() + unitWidth,tempXText.getValueAxis3(),tempXText.getLeftAxis() + 2 * unitWidth ,mHeight - 2 * textHeight,value3Paint);
//            if(!TextUtils.isEmpty(tempXText.getText()) && showText){
//                canvas.drawText(tempXText.getText(),tempXText.getLeftAxis() + unitWidth,mHeight,textPaint);
//            }
        }
    }
    public void setOnTouchListerner(){
        this.setOnTouchListener(new SportOnTouch());
    }
    public class SportOnTouch implements View.OnTouchListener{
        private long timelong ;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float downX = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timelong = System.currentTimeMillis() ;
                    break;
                case MotionEvent.ACTION_UP:
                    if(rightXTexts == null)return true ;
                    long tempLongTime = System.currentTimeMillis() ;
                    if(tempLongTime - timelong < Constant.SCROLL_TOUCH_MIN_TIME){
                        for (int i = 0; i < rightXTexts.size(); i++) {
                            SportEntityRightXText tempText1 = rightXTexts.get(i) ;
                            if(i < rightXTexts.size() - 1){
                                SportEntityRightXText tempText2 = rightXTexts.get(i + 1) ;
                                if( Math.abs(downX - (tempText1.getLeftAxis() + unitWidth)) < Math.abs(downX - (tempText2.getLeftAxis() + unitWidth))){
                                    if(pointClickListener != null){
                                        pointClickListener.checkPoint(i);
                                    }
                                    break;
                                }
                            }else {
                                if(pointClickListener != null){
                                    pointClickListener.checkPoint(i);
                                }
                            }
                        }
                    }
                    break;
            }
            return true;
        }
    }
    public SportPointClickListener pointClickListener ;

    public void setPointClickListener(SportPointClickListener pointClickListener) {
        this.pointClickListener = pointClickListener;
    }

    public interface SportPointClickListener{
        void checkPoint(int position) ;
    }
}
