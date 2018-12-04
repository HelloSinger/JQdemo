package com.jq.code.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.btlib.util.LogUtil;
import com.jq.code.R;
import com.jq.code.model.BPressEntity;
import com.jq.code.model.Constant;
import com.jq.code.view.wheel.BloodTrendBaseView;

import java.util.ArrayList;
import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class BPressTrendDayView extends BloodTrendBaseView {
    private static final String TAG =BPressTrendDayView.class.getSimpleName() ;
    private List<BPressEntity> bPressEntities;
    /**
     * textY轴坐标
     */
    protected float textYAxis[]  ;
    protected List<Integer> textYValue ;
    private Paint tagPaint ;
    private Bitmap sysBitmap ,diaBitmap ;
    public List<BPressEntity> getbPressEntities() {
        return bPressEntities;
    }
    public void setbPressEntities(List<BPressEntity> bPressEntities) {
        this.bPressEntities = bPressEntities;
        this.textYValue = computeYValue(bPressEntities) ;
        if(textYValue.size() > 0){
            maxYValue = textYValue.get(textYValue.size() -1) ;
            minYValue = textYValue.get(0) ;
        }
        this.isReset = true ;
    }

    private List computeYValue(List<BPressEntity> bPressEntities) {
        List tempYValue = new ArrayList() ;
        int tempMaxValue = 0;
        int tempMinValue = 0;
        if(bPressEntities.size()> 0){
            float sys1 = bPressEntities.get(0).getSys() ;
            float dia1 = bPressEntities.get(0).getDia() ;
            tempMaxValue = (int) (sys1 > dia1 ? sys1 : dia1);
            tempMinValue = (int) (sys1 < dia1 ? sys1 : dia1);
            for (BPressEntity entity: bPressEntities) {
                int tempsys = entity.getSys() ;
                int tempdia = entity.getDia() ;
                tempMaxValue = tempMaxValue > tempsys ? tempMaxValue : tempsys ;
                tempMaxValue = tempMaxValue > tempdia ? tempMaxValue : tempdia ;
                tempMinValue = tempMinValue < tempsys ? tempMinValue : tempsys ;
                tempMinValue = tempMinValue < tempdia ? tempMinValue : tempdia ;
            }
            LogUtil.e(TAG ,"tempMaxValue = " +  tempMaxValue);
            LogUtil.e(TAG ,"tempMinValue = " +  tempMinValue);
        }
        if(tempMaxValue != 0 && tempMinValue != 0){
            tempMaxValue  += 10 ;
            tempMinValue  -= 10 ;
            tempMaxValue  = tempMaxValue > Constant.MAX_BPRESS_DIA ?  Constant.MAX_BPRESS_DIA : tempMaxValue ;
            tempMinValue = tempMinValue < Constant.MIN_BPRESS_SYS ? Constant.MIN_BPRESS_SYS : tempMinValue ;
            int yifen = (tempMaxValue - tempMinValue) /5;
            tempYValue.add(tempMinValue) ;
            for (int i = 1;i < 5 ;i ++){
                tempYValue.add(tempMinValue + i * yifen );
            }
            tempYValue.add(tempMaxValue);
        }
        return tempYValue ;
    }

    public BPressTrendDayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init() ;
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
        if(bPressEntities == null)
            return;
        super.onDraw(canvas);
    }
  

    protected void drawLineAndPoint() {
        for (int i = 0; i < bPressEntities.size() ; i++) {
            BPressEntity tempEntity1 = bPressEntities.get(i) ;
            if(i < bPressEntities.size() - 1){
                BPressEntity tempEntity2 = bPressEntities.get(i + 1) ;
                // 画线
                pointPaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity1.getxPosition()], tempEntity1.getSysAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getSysAxis(), linePaint);
            }
            // 画点
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getSysAxis(), radius, pointPaint);
            mCanvas.drawBitmap(sysBitmap,textXAxis[tempEntity1.getxPosition()]- sysBitmap.getWidth()/2, tempEntity1.getSysAxis() - sysBitmap.getHeight()/2  , tagPaint);
        }
        for (int i = 0; i < bPressEntities.size() ; i++) {
            BPressEntity tempEntity1 = bPressEntities.get(i) ;
            if(i < bPressEntities.size() - 1){
                BPressEntity tempEntity2 = bPressEntities.get(i + 1) ;
                // 画线
                pointPaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity1.getxPosition()], tempEntity1.getDiaAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getDiaAxis(), linePaint);
            }
            // 画点
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getDiaAxis(), radius, pointPaint);
            mCanvas.drawBitmap(diaBitmap,textXAxis[tempEntity1.getxPosition()]- diaBitmap.getWidth()/2, tempEntity1.getDiaAxis() - diaBitmap.getHeight()/2, tagPaint);
        }
    }

    protected void computeYAxis() {
        textYAxis = new float[textYValue.size()] ;
        float yifenY = (mHeight - XTextSize - 3 * mSpace) /(maxYValue - minYValue) ;
        for (int i = 0; i < textYValue.size(); i++) {
            textYAxis[i] = mHeight - yifenY * (textYValue.get(i) - minYValue)  - XTextSize -  2 * mSpace;
        }
        for (int i = 0; i < bPressEntities.size(); i++) {
            BPressEntity pointHelpEntity = bPressEntities.get(i) ;
            pointHelpEntity.setSysAxis(mHeight - yifenY * (pointHelpEntity.getSys() - minYValue)  - XTextSize -  2 * mSpace);
        }
        for (int i = 0; i < bPressEntities.size(); i++) {
            BPressEntity pointHelpEntity = bPressEntities.get(i) ;
            pointHelpEntity.setDiaAxis(mHeight - yifenY * (pointHelpEntity.getDia() - minYValue)  - XTextSize -  2 * mSpace);
        }
    }
    protected void drawYin() {
        int lenth = bPressEntities.size() ;
        if(lenth > 0){
            float[]  pointXAxis= new float[lenth] ;
            float[]  topYAxis= new float[lenth] ;
            float[]  bottomYAxis= new float[lenth] ;
            for (int i = 0; i < bPressEntities.size(); i++) {
                pointXAxis[i] = textXAxis[bPressEntities.get(i).getxPosition()] ;
                topYAxis[i] = bPressEntities.get(i).getSysAxis() ;
            }
            for (int i = 0; i < bPressEntities.size() ; i++) {
                bottomYAxis[i] = bPressEntities.get(i).getDiaAxis() ;
            }
            int i = 0 ;
            Path mPath=new Path();
            mPath.moveTo(pointXAxis[0], bottomYAxis[0]);
            while (true){
                if(i >  pointXAxis.length-1){
                    break;
                }
                mPath.lineTo(pointXAxis[i], topYAxis[i]);
                ++ i ;
            }
            while (true){
                -- i ;
                if(i <  0){
                    break;
                }
                mPath.lineTo(pointXAxis[i], bottomYAxis[i]);
            }
            mPath.close();
            mCanvas.drawPath(mPath, yinPaint) ;
        }
    }
    protected void drawText() {
        super.drawText();
        for (int i = 0; i < textYValue.size(); i++) {//画y轴text
            mCanvas.drawText(textYValue.get(i) + "" , mSpace + YTextSize/2, textYAxis[i] + YTextSize/2 , YTextPaint);
        }
    }
    public void setThisOnTouch(OnBPreeDayPointClickListener lIstener){
        this.setOnTouchListener(new OnBPressDayTouchListenerImp(lIstener));
    }
    public  class OnBPressDayTouchListenerImp implements View.OnTouchListener{
        OnBPreeDayPointClickListener listener;
        public OnBPressDayTouchListenerImp(OnBPreeDayPointClickListener listener){
            this.listener = listener;
        }
        private long timelong ;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            float downX = event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LogUtil.e(TAG,"onTouchEvent ACTION_DOWN");
                    timelong = System.currentTimeMillis() ;

                    break;
                case MotionEvent.ACTION_UP:
                    LogUtil.e(TAG,"onTouchEvent ACTION_UP");
                    long tempLongTime = System.currentTimeMillis() ;
                    if(tempLongTime - timelong < Constant.SCROLL_TOUCH_MIN_TIME){
                        if (bPressEntities != null && bPressEntities.size() > 0) {
                            for (int i = 0; i < bPressEntities.size(); i++) {
                                BPressEntity temmEntity = bPressEntities.get(i) ;
                                if (downX > (textXAxis[temmEntity.getxPosition()] - 3 * mSpace)
                                        && downX < (textXAxis[temmEntity.getxPosition()] + 3 * mSpace)) {
                                    if (listener != null) {
                                        listener.onBPreeDayPoint(temmEntity);
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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//    }



    public interface OnBPreeDayPointClickListener {
        void onBPreeDayPoint(BPressEntity entity);
    }
}
