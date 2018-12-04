
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
import com.jq.code.model.BGlucoseEntity;
import com.jq.code.model.Constant;
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
public class BGlucoseTrendDayView extends BloodTrendBaseView {

    private List<BGlucoseEntity> bGlucoseEntities;
    protected List<Float> textYValue ;
    private List<Float> standardLineValue;
    private Paint standardLinePaint;
    private Paint standardTextPaint ;
    /**
     * textY轴坐标
     */
    protected float textYAxis[]  ;
    protected float standarLineAxis[] ;

    public void setbGlucoseEntities(List<BGlucoseEntity> bGlucoseEntities) {
        this.bGlucoseEntities = bGlucoseEntities;
        this.textYValue = computeYValue(bGlucoseEntities) ;
        if(textYValue.size() > 0){
            maxYValue = textYValue.get(textYValue.size() -1) ;
            minYValue = textYValue.get(0) ;
            computLineValue() ;
        }
        isReset = true ;
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

    public BGlucoseTrendDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init() ;
    }
    protected void computeXAxis() {
        textXAxis = new float[xSesion] ;
        float yifenX = (mWidth - YTextWidth -  4 * mSpace - YTextSize ) / (xSesion-1) ;
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] = YTextWidth +  2 * mSpace + i * yifenX ;
        }
    }
    public List computeYValue(List<BGlucoseEntity> bGlucoseEntities){
        standardLineValue = new ArrayList<Float>() ;
        List<Float> result = new ArrayList<Float>() ;
        float tempMaxValue = 0f;
        float tempMinValue = 0f;
        if(bGlucoseEntities.size() > 0){
            tempMaxValue = bGlucoseEntities.get(0).getBsl() ;
            tempMinValue = bGlucoseEntities.get(0).getBsl() ;
            for (BGlucoseEntity entity:bGlucoseEntities) {
                if(tempMaxValue < entity.getBsl()){
                    tempMaxValue = entity.getBsl() ;
                    continue;
                }
                if(tempMinValue > entity.getBsl()){
                    tempMinValue = entity.getBsl() ;
                }
            }
        }
        if(tempMaxValue != 0f && tempMinValue != 0f){
            if(tempMaxValue == tempMinValue){
                tempMaxValue += 2f ;
                tempMinValue -= 2f ;
            }
            float yifen = (tempMaxValue - tempMinValue)/5;
            tempMaxValue +=  yifen/2 ;
            tempMinValue -=  yifen/2 ;
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
    @Override
    protected void onDraw(Canvas canvas) {
        if(bGlucoseEntities == null)
            return;
        super.onDraw(canvas);
    }
    protected void drawYin() {
       int lenth = bGlucoseEntities.size() ;
            if(lenth > 0){
                float[]  pointXAxis= new float[lenth] ;
                float[]  topYAxis= new float[lenth] ;
                for (int i = 0; i < bGlucoseEntities.size(); i++) {
                    pointXAxis[i] = textXAxis[bGlucoseEntities.get(i).getxPosition()] ;
                    topYAxis[i] = bGlucoseEntities.get(i).getAxis() ;
                }
                int i = 0 ;
                Path mPath=new Path();
                mPath.moveTo(pointXAxis[0], mHeight - mSpace/2);
                while (true){
                    if(i >  pointXAxis.length-1){
                        break;
                    }
                    mPath.lineTo(pointXAxis[i], topYAxis[i]);
                    ++ i ;
                }
                -- i ;
                mPath.lineTo(pointXAxis[i], mHeight - mSpace/2);
                mPath.close();
                mCanvas.drawPath(mPath, yinPaint) ;
        }
    }
    @Override
    protected void drawLineAndPoint() {
        for (int i = 0; i < bGlucoseEntities.size() ; i++) {
            BGlucoseEntity tempEntity1 = bGlucoseEntities.get(i) ;
            if(i < bGlucoseEntities.size() - 1){
                BGlucoseEntity tempEntity2 = bGlucoseEntities.get(i + 1) ;
                // 画线
                pointPaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity1.getxPosition()], tempEntity1.getAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getAxis(), linePaint);
            }
            // 画点
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()],tempEntity1.getAxis(), interiorRadius, interiorPaint);
        }
        //画标准线
        for (int i = 0; i < standardLineValue.size(); i++) {
            Path path = new Path();
            path.moveTo(mSpace + YTextSize * 2, standarLineAxis[i]+ YTextSize/4);
            path.lineTo(mWidth - mSpace - YTextSize * 2, standarLineAxis[i]+ YTextSize/4);
            mCanvas.drawPath(path, standardLinePaint);
        }
    }
    @Override
    protected void computeYAxis() {
        textYAxis = new float[textYValue.size()] ;
        float yifenY = (mHeight - XTextSize - 4 * mSpace) /(maxYValue - minYValue) ;
        for (int i = 0; i < textYValue.size(); i++) {
            float value = yifenY * (textYValue.get(i) - minYValue) ;
            textYAxis[i] = mHeight - value  - XTextSize -  2 * mSpace;
        }
        for (int i = 0; i < bGlucoseEntities.size(); i++) {
            BGlucoseEntity entity = bGlucoseEntities.get(i) ;
            entity.setAxis(mHeight - yifenY * (entity.getBsl() - minYValue)  - XTextSize -  2 * mSpace);
        }
        if(standardLineValue != null){
            standarLineAxis = new float[standardLineValue.size()] ;
            for (int i = 0; i < standardLineValue.size() ; i++) {
                float value = yifenY * (standardLineValue.get(i) - minYValue) ;
                standarLineAxis[i] = mHeight - value  - XTextSize -  2 * mSpace;
            }
        }
    }

    @Override
    protected void drawText() {
            super.drawText();
            for (int i = 0; i < textYValue.size(); i++) {//画y轴text
                mCanvas.drawText(textYValue.get(i) + "" , mSpace + YTextSize/2, textYAxis[i] + YTextSize/2 , YTextPaint);
        }
        for (int i = 0; i < standardLineValue.size(); i++) {
            mCanvas.drawText(standardLineValue.get(i) + "" , mWidth - YTextSize, standarLineAxis[i] + YTextSize/2 , standardTextPaint);
        }
    }

    public interface OnBGlucoseDayPointClickListener {
        void onBGlucoseDayPoint(BGlucoseEntity entity);
    }
    public void setThisOnTouch(OnBGlucoseDayPointClickListener listener){
        this.setOnTouchListener(new OnBGlucoseDayTouchListenerImp(listener));
    }
    public  class OnBGlucoseDayTouchListenerImp implements View.OnTouchListener{
        OnBGlucoseDayPointClickListener listener;
        public OnBGlucoseDayTouchListenerImp(OnBGlucoseDayPointClickListener listener){
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
                        if (bGlucoseEntities != null && bGlucoseEntities.size() > 0) {
                            for (int i = 0; i < bGlucoseEntities.size(); i++) {
                                BGlucoseEntity tempEntity = bGlucoseEntities.get(i) ;
                                if (downX > (textXAxis[tempEntity.getxPosition()] - 3 * mSpace)
                                        && downX < (textXAxis[tempEntity.getxPosition()] + 3 *  mSpace)) {
                                    if (listener != null) {
                                        listener.onBGlucoseDayPoint(tempEntity);
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
