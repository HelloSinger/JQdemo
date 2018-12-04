
package com.jq.code.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.btlib.util.LogUtil;
import com.jq.code.R;
import com.jq.code.model.Constant;
import com.jq.code.model.trend.BPressTrend;
import com.jq.code.view.wheel.BloodTrendBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class BPressWeekTrendView extends BloodTrendBaseView {
    private static final String TAG =BPressWeekTrendView.class.getSimpleName() ;
    private List<BPressTrend> bPressTrends;
    /**
     * textY轴坐标
     */
    protected float textYAxis[]  ;
    protected List<Integer> textYValue ;
    private Paint tagPaint ;
    private Bitmap sysBitmap ,diaBitmap ;
    public void setbPressTrends(List<BPressTrend> bPressTrends) {
        this.bPressTrends = bPressTrends;
        this.textYValue = computeYValue(bPressTrends) ;
        if(textYValue.size() > 0){
            maxYValue = textYValue.get(textYValue.size() -1) ;
            minYValue = textYValue.get(0) ;
        }
        this.isReset = true ;
    }


    public BPressWeekTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private List computeYValue(List<BPressTrend> bPressEntities) {
        List tempYValue = new ArrayList() ;
        int tempMaxValue = 0;
        int tempMinValue = 0;
        if(bPressEntities.size()> 0){
            int maxsys1 = (int) bPressEntities.get(0).getMaxSys();
            int minsys1 = (int) bPressEntities.get(0).getMinSys();
            int maxdia1 = (int) bPressEntities.get(0).getMaxDia();
            int mindia1 = (int) bPressEntities.get(0).getMinDia();
            tempMaxValue =  (maxsys1 > maxdia1 ? maxsys1 : maxdia1);
            tempMinValue =   (minsys1 < mindia1 ? minsys1 : mindia1);
            for (BPressTrend entity: bPressEntities) {
                int tempmaxsys = (int) entity.getMaxSys();
                int tempminsys = (int) entity.getMinSys();
                int tempmaxdia = (int) entity.getMaxDia();
                int tempmindia = (int) entity.getMinDia();
                tempMaxValue = tempMaxValue > tempmaxsys ? tempMaxValue : tempmaxsys ;
                tempMaxValue = tempMaxValue > tempmaxdia ? tempMaxValue : tempmaxdia ;
                tempMinValue = tempMinValue < tempminsys ? tempMinValue : tempminsys ;
                tempMinValue = tempMinValue < tempmindia ? tempMinValue : tempmindia ;
            }
            LogUtil.e(TAG ,"tempMaxValue = " +  tempMaxValue);
            LogUtil.e(TAG ,"tempMinValue = " +  tempMinValue);
        }
        if(tempMaxValue != 0 && tempMinValue != 0){
            tempMaxValue  += 10 ;
            tempMinValue  -= 10 ;
            tempMaxValue  = tempMaxValue > 300 ? 300 : tempMaxValue ;
            tempMinValue = tempMinValue < 30 ? 30 : tempMinValue ;
            int yifen = (tempMaxValue - tempMinValue) /5;
            tempYValue.add(tempMinValue) ;
            for (int i = 1;i < 5 ;i ++){
                tempYValue.add(tempMinValue + i * yifen );
            }
            tempYValue.add(tempMaxValue);
        }
        return tempYValue ;
    }
    private void init(){
        sysBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bpress_trend_sys);
        diaBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bpress_trend_dia);
        tagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tagPaint.setTextSize(YTextSize * 2/3);
        tagPaint.setTextAlign(Paint.Align.CENTER);
        tagPaint.setColor(Color.parseColor("#ffffff"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(bPressTrends == null)
            return;
        super.onDraw(canvas);
    }

    @Override
    protected void drawYin() {

    }

    protected void drawLineAndPoint() {
        for (int i = 0; i < bPressTrends.size() ; i++) {
            BPressTrend chartEntity = bPressTrends.get(i) ;
            pointPaint.setPathEffect(null);
            mCanvas.drawLine(textXAxis[chartEntity.getxPosition()], chartEntity.getMinDiaAxis(), textXAxis[chartEntity.getxPosition()], chartEntity.getMaxDiaAxis(), linePaint); //划线
            mCanvas.drawLine(textXAxis[chartEntity.getxPosition()], chartEntity.getMinSysAxis(), textXAxis[chartEntity.getxPosition()], chartEntity.getMaxSysAxis(), linePaint); //划线
            mCanvas.drawCircle(textXAxis[chartEntity.getxPosition()], chartEntity.getMaxDiaAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[chartEntity.getxPosition()], chartEntity.getMaxSysAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[chartEntity.getxPosition()], chartEntity.getMinSysAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[chartEntity.getxPosition()], chartEntity.getMinDiaAxis(), radius, pointPaint);
            mCanvas.drawBitmap(sysBitmap,textXAxis[chartEntity.getxPosition()]- sysBitmap.getWidth()/2, chartEntity.getMinSysAxis() - sysBitmap.getHeight()/2  , tagPaint);
            mCanvas.drawBitmap(sysBitmap,textXAxis[chartEntity.getxPosition()]- sysBitmap.getWidth()/2, chartEntity.getMaxSysAxis() - sysBitmap.getHeight()/2, tagPaint);
            mCanvas.drawBitmap(diaBitmap,textXAxis[chartEntity.getxPosition()]- diaBitmap.getWidth()/2, chartEntity.getMinDiaAxis() - diaBitmap.getHeight()/2  , tagPaint);
            mCanvas.drawBitmap(diaBitmap,textXAxis[chartEntity.getxPosition()]- diaBitmap.getWidth()/2, chartEntity.getMaxDiaAxis() - diaBitmap.getHeight()/2, tagPaint);
        }
    }

    protected void computeYAxis() {
        textYAxis = new float[textYValue.size()] ;
        float yifenY = (mHeight - XTextSize - 3 * mSpace) /(maxYValue - minYValue) ;
        for (int i = 0; i < textYValue.size(); i++) {
            textYAxis[i] = mHeight - yifenY * (textYValue.get(i) - minYValue)  - XTextSize -  2 * mSpace;
        }
        for (int i = 0; i < bPressTrends.size(); i++) {
            BPressTrend chartEntity = bPressTrends.get(i);
            chartEntity.setMaxSysAxis((float) (mHeight - yifenY * (chartEntity.getMaxSys() - minYValue) - XTextSize - 2 * mSpace));
            chartEntity.setMaxDiaAxis((float) (mHeight - yifenY * (chartEntity.getMaxDia() - minYValue) - XTextSize - 2 * mSpace));
            chartEntity.setMinSysAxis((float) (mHeight - yifenY * (chartEntity.getMinSys() - minYValue) - XTextSize - 2 * mSpace));
            chartEntity.setMinDiaAxis((float) (mHeight - yifenY * (chartEntity.getMinDia() - minYValue) - XTextSize - 2 * mSpace));
        }
    }

    protected void drawText() {
        super.drawText();
        for (int i = 0; i < textYValue.size(); i++) {//画y轴text
            mCanvas.drawText(textYValue.get(i) + "" , mSpace + YTextSize/2, textYAxis[i] + YTextSize/2 , YTextPaint);
        }
    }
    public interface OnBPressWeekPointClickListener {
        void onBPressWeekPoint(BPressTrend pressTrend);
    }
    public void setThisOnTouch(OnBPressWeekPointClickListener listener){
        this.setOnTouchListener(new OnBPressWeekTouchListenerImp(listener));
    }
    public  class OnBPressWeekTouchListenerImp implements View.OnTouchListener{
        OnBPressWeekPointClickListener listener;
        public OnBPressWeekTouchListenerImp(OnBPressWeekPointClickListener listener){
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
                        if (bPressTrends != null && bPressTrends.size() > 0) {
                            for (int i = 0; i < bPressTrends.size(); i++) {
                                BPressTrend temmEntity = bPressTrends.get(i) ;
                                if (downX > (textXAxis[temmEntity.getxPosition()] - 3*mSpace)
                                        && downX < (textXAxis[temmEntity.getxPosition()] + 3*mSpace)) {
                                    if (listener != null) {
                                        listener.onBPressWeekPoint(temmEntity);
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
