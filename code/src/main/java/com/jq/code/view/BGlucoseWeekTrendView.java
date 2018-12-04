
package com.jq.code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.btlib.util.LogUtil;
import com.jq.code.R;
import com.jq.code.model.Constant;
import com.jq.code.model.trend.BGlucoseTrend;
import com.jq.code.view.wheel.BloodTrendBaseView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class BGlucoseWeekTrendView extends BloodTrendBaseView {

    private List<BGlucoseTrend> glucoseTrends;
    protected List<Float> textYValue ;
    private List<Float> standardLineValue;
    private boolean showStandarLine = true ;
    private Paint standardLinePaint;
    private Paint standardTextPaint ;
    public List<BGlucoseTrend> getGlucoseTrends() {
        return glucoseTrends;

    }

    public void setShowStandarLine(boolean showStandarLine) {
        this.showStandarLine = showStandarLine;
    }

    /**
     * textY轴坐标
     */
    protected float textYAxis[]  ;
    protected float standarLineAxis[] ;

    public void setGlucoseTrends(List<BGlucoseTrend> glucoseTrends) {
        this.glucoseTrends = glucoseTrends;
        this.textYValue = computeYValue(glucoseTrends) ;
        if(textYValue.size() > 0){
            maxYValue = textYValue.get(textYValue.size() -1) ;
            minYValue = textYValue.get(0) ;
            computLineValue() ;
        }
        isReset = true ;
    }

    public BGlucoseWeekTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init() ;
    }
    private void computLineValue() {
        standardLineValue = new ArrayList<Float>() ;
        for (int i = 0; i <Constant.defalutBGlucose.length ; i++) {
            float tempValue = Constant.defalutBGlucose[i] ;
            if(tempValue >= minYValue && tempValue <= maxYValue){
                standardLineValue.add(tempValue) ;
            }
        }
    }
    private void init(){
        standardLinePaint = new Paint();
        standardLinePaint.setAntiAlias(true);
        standardLinePaint.setStrokeWidth(1);
        standardLinePaint.setStyle(Paint.Style.STROKE);
        standardLinePaint.setColor(getResources().getColor(R.color.standard_line));
        standardLinePaint.setPathEffect(new DashPathEffect(new float[]{3 * mDensity, 2 * mDensity}, 0));

        standardTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        standardTextPaint.setTextSize(YTextSize);
        standardTextPaint.setTextAlign(Paint.Align.CENTER);
        standardTextPaint.setColor(getResources().getColor(R.color.standard_text));
    }
    public List computeYValue(List<BGlucoseTrend> bGlucoseEntities){
        standardLineValue = new ArrayList<Float>() ;
        List<Float> result = new ArrayList<Float>() ;
        float tempMaxValue = 0f;
        float tempMinValue = 0f;
        if(bGlucoseEntities.size() > 0){
            tempMaxValue = (float) bGlucoseEntities.get(0).getMaxBsl();
            tempMinValue = (float) bGlucoseEntities.get(0).getMinBsl();
            for (BGlucoseTrend entity:bGlucoseEntities) {
                if(tempMaxValue < entity.getMaxBsl()){
                    tempMaxValue = (float) entity.getMaxBsl();
                }
                if(tempMinValue > entity.getMinBsl()){
                    tempMinValue = (float) entity.getMinBsl();
                }
            }
        }
        if(tempMaxValue != 0f && tempMinValue != 0f){
            if(tempMaxValue == tempMinValue){  //当天只有一个数据的时候
                tempMaxValue += 2f ;
                tempMinValue -= 2f ;
            }
            float yifen = (tempMaxValue - tempMinValue)/5;
            tempMaxValue += yifen/2 ;
            tempMinValue -= yifen/2 ;
            tempMaxValue = getOneXiaoShu(tempMaxValue) ;
            tempMinValue = getOneXiaoShu(tempMinValue) ;
            tempMaxValue  = tempMaxValue > Constant.MAX_BLUCOSE_VALUE ?  Constant.MAX_BLUCOSE_VALUE : tempMaxValue ;
            tempMinValue = tempMinValue < Constant.MIN_BLUCOSE_VALUE ? Constant.MIN_BLUCOSE_VALUE : tempMinValue ;
            LogUtil.e("XLJ: " , "-------------------------yifen = " + yifen);
            result.add(tempMinValue) ;
            for (int i = 1;i < 6 ;i ++){
                result.add(getOneXiaoShu(tempMinValue + i * yifen));
            }
            result.add(tempMaxValue);
        }
        return result ;
    }
    public float getOneXiaoShu(float value){
        BigDecimal bigDecimal=  new BigDecimal(value);
        float nuber = bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
        return nuber ;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(glucoseTrends == null)
            return;
        super.onDraw(canvas);
    }

    @Override
    protected void drawYin() {

    }


    protected void drawLineAndPoint() {
        for (int i = 0; i < glucoseTrends.size() ; i++) {
            BGlucoseTrend helpEntity = glucoseTrends.get(i) ;
            pointPaint.setPathEffect(null);
            mCanvas.drawLine(textXAxis[helpEntity.getxPosition()], helpEntity.getMinAxis(), textXAxis[helpEntity.getxPosition()], helpEntity.getMaxAxis(), linePaint); //划线

            mCanvas.drawCircle(textXAxis[helpEntity.getxPosition()], helpEntity.getMinAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[helpEntity.getxPosition()],helpEntity.getMinAxis(), interiorRadius, interiorPaint);

            mCanvas.drawCircle(textXAxis[helpEntity.getxPosition()], helpEntity.getMaxAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[helpEntity.getxPosition()],helpEntity.getMaxAxis(), interiorRadius, interiorPaint);
        }
        //画标准线
        if(showStandarLine){
            for (int i = 0; i < standardLineValue.size(); i++) {
                Path path = new Path();
                path.moveTo(mSpace + YTextSize * 2, standarLineAxis[i]+ YTextSize/4);
                path.lineTo(mWidth - mSpace - YTextSize * 2, standarLineAxis[i]+ YTextSize/4);
                mCanvas.drawPath(path, standardLinePaint);
            }
        }
    }


    protected void computeYAxis() {
        textYAxis = new float[textYValue.size()] ;
        float yifenY = (mHeight - XTextSize - 3 * mSpace) /(maxYValue - minYValue) ;
        for (int i = 0; i < textYValue.size(); i++) {
            float value = yifenY * (textYValue.get(i) - minYValue) ;
            textYAxis[i] = mHeight - value  - XTextSize -  2 * mSpace;
        }
        for (int i = 0; i < glucoseTrends.size(); i++) {
            BGlucoseTrend helpEntity = glucoseTrends.get(i) ;
            helpEntity.setMaxAxis((float) (mHeight - yifenY * (helpEntity.getMaxBsl() - minYValue)  - XTextSize -  2 * mSpace));
            helpEntity.setMinAxis((float) (mHeight - yifenY * (helpEntity.getMinBsl() - minYValue)  - XTextSize -  2 * mSpace));
        }
        if(standardLineValue != null){
            standarLineAxis = new float[standardLineValue.size()] ;
            for (int i = 0; i < standardLineValue.size() ; i++) {
                float value = yifenY * (standardLineValue.get(i) - minYValue) ;
                standarLineAxis[i] = mHeight - value  - XTextSize -  2 * mSpace;
            }
        }
    }

    protected void computeXAxis() {
        textXAxis = new float[xSesion] ;
        float yifenX = (mWidth - YTextWidth -  4 * mSpace - YTextSize ) / (xSesion-1) ;
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] = YTextWidth +  2 * mSpace + i * yifenX ;
        }
    }
    protected void drawText() {
        super.drawText();
        for (int i = 0; i < textYValue.size(); i++) {//画y轴text
            mCanvas.drawText(textYValue.get(i) + "" , mSpace + YTextSize/2, textYAxis[i] + YTextSize/2 , YTextPaint);
        }
        for (int i = 0; i < standardLineValue.size(); i++) {
            mCanvas.drawText(standardLineValue.get(i) + "" , mWidth - YTextSize, standarLineAxis[i] + YTextSize/2 , standardTextPaint);
        }
    }
    public interface OnBGlucoseWeekPointClickListener {
        void onBGlucoseWeekPoint(BGlucoseTrend pressTrend);
    }
    public void setThisOnTouch(OnBGlucoseWeekPointClickListener listener){
        this.setOnTouchListener(new OnBGlucoseWeekTouchListenerImp(listener));
    }
    public  class OnBGlucoseWeekTouchListenerImp implements View.OnTouchListener{
        OnBGlucoseWeekPointClickListener listener;
        public OnBGlucoseWeekTouchListenerImp(OnBGlucoseWeekPointClickListener listener){
            this.listener = listener;
        }
        private long timelong ;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float downX = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    timelong = System.currentTimeMillis() ;

                    break;
                case MotionEvent.ACTION_UP:
                    long tempLongTime = System.currentTimeMillis() ;
                    if(tempLongTime - timelong < Constant.SCROLL_TOUCH_MIN_TIME){
                        if (glucoseTrends != null && glucoseTrends.size() > 0) {
                            for (int i = 0; i < glucoseTrends.size(); i++) {
                                BGlucoseTrend temmEntity = glucoseTrends.get(i) ;
                                if (downX > (textXAxis[temmEntity.getxPosition()] - 3 * mSpace)
                                        && downX < (textXAxis[temmEntity.getxPosition()] + 3 * mSpace)) {
                                    if (listener != null) {
                                        listener.onBGlucoseWeekPoint(temmEntity);
                                    }
                                    break;
                                }

                            }
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
