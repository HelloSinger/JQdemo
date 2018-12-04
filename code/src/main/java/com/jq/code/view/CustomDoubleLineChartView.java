/*
 * Copyright (c) 2016 Kaku咖枯 <kaku201313@163.com | 3772304@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.jq.code.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;
import com.jq.code.model.PointHelpEntity;

import java.util.List;

public class CustomDoubleLineChartView extends View {
    private int xSesion ;
    private List<PointHelpEntity> topPoint,bottomPoint;
    /**
     * textX轴坐标
     */
    private float textXAxis[];
    /**
     * 控件高
     */
    private int mHeight;
    /**
     * 控件高
     */
    private int mWidth;
    /**
     * 圓半径
     */
    private float mRadius;
    /**
     * 圓内半径
     */
    private float mInteriorRadius;
    /**
     * 屏幕密度
     */
    private float mDensity;

    /**
     * 控件边的空白空间
     */
    private float mSpace;

    /**
     * 线画笔
     */
    private Paint mLinePaint;
    /**
     * 线画笔
     */
    private Paint mGaryLinePaint;

    /**
     * 线画笔
     */
    private Paint yinPaint;
    /**
     * 点画笔
     */
    private Paint mPointPaint;
    private Paint mInterior ;

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    private float scale = 0.4f ;   //显示在视图中间占比

    public CustomDoubleLineChartView(Context context) {
        super(context);
    }
    public CustomDoubleLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomDoubleLineChartView);
        xSesion = a.getInteger(R.styleable.CustomDoubleLineChartView_xSesionValueDoubleSingle,7) ;
        int mlineColor = a.getColor(R.styleable.CustomDoubleLineChartView_doubleLineColor,
                getResources().getColor(R.color.exercise_sport_color));
        int mYinColor = a.getColor(R.styleable.CustomDoubleLineChartView_doubleYinColor,  //阴影颜色
                getResources().getColor(R.color.text_gray));
        mSpace = a.getFloat(R.styleable.CustomDoubleLineChartView_doubleSpaceValue, 10.0f);
        a.recycle();

        mDensity = getResources().getDisplayMetrics().density;
        mRadius = 5 * mDensity;
        mInteriorRadius = 3 * mDensity ;
        mSpace = mSpace * mDensity;

        float stokeWidth = 2 * mDensity;
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mlineColor);
        mLinePaint.setStrokeWidth(stokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mGaryLinePaint = new Paint();
        mGaryLinePaint.setAntiAlias(true);
        mGaryLinePaint.setStrokeWidth(mDensity);
        mGaryLinePaint.setStyle(Paint.Style.STROKE);
        mGaryLinePaint.setColor(Color.parseColor("#cccccc"));

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(mlineColor);

        mInterior = new Paint();
        mInterior.setAntiAlias(true);
        mInterior.setColor(Color.parseColor("#ffffff"));

        yinPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setColor(mYinColor);
    }


    private Bitmap baseBitmap;
    public boolean isReset = true ;
    private Canvas mCanvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mHeight = getHeight() ;
        mWidth = getWidth() ;
        if(isReset){
            isReset = false ;
            resetCavas();
        }
        if(baseBitmap != null){
            // 将前面已经画过得显示出来
            canvas.drawBitmap(baseBitmap, 0, 0, null);
        }
        invalidate();
    }
    public void resetCavas(){
        computeXAxis()  ;
        computePointYAxis() ;
        baseBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(baseBitmap);
        mCanvas.drawColor(Color.WHITE);
        // 计算y轴集合数值
        drawYin() ;
        // 画白天折线图
        drawGrayLine() ;
        drawTopChart();
        drawBottomChart() ;
    }
    private void computeXAxis() {
        textXAxis = new float[xSesion] ;
        float yifenX = (mWidth - 2 * mSpace) / (xSesion-1) ;
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] =  mSpace + i * yifenX ;
        }
    }
    private void computePointYAxis() {
        if(topPoint.size() > 0 && bottomPoint.size() > 0){
            float maxYValue = topPoint.get(0).getyValue() ;
            float minYValue = bottomPoint.get(0).getyValue()  ;
            for (int i = 0; i <topPoint.size() ; i++) {
                if(topPoint.get(i).getyValue() > maxYValue){
                    maxYValue = topPoint.get(i).getyValue()  ;
                }
            }
            for (int i = 0; i <bottomPoint.size() ; i++) {
                if(bottomPoint.get(i).getyValue() < minYValue){
                    minYValue = bottomPoint.get(i).getyValue()  ;
                }
            }
            float trueHight = mHeight  -  mSpace ;
            if(maxYValue == minYValue){
                for (int i = 0; i < topPoint.size(); i++) {
                    PointHelpEntity pointHelpEntity = topPoint.get(i) ;
                    pointHelpEntity.setyYAxis(0.5f * trueHight);
                }
                for (int i = 0; i < bottomPoint.size(); i++) {
                    PointHelpEntity pointHelpEntity = bottomPoint.get(i) ;
                    pointHelpEntity.setyYAxis(0.5f * trueHight);
                }
            }else{
                float minH = trueHight* ((1-scale)/2);
                float scale = (trueHight * this.scale)/(maxYValue-minYValue);
                for (int i = 0; i < topPoint.size(); i++) {
                    PointHelpEntity pointHelpEntity = topPoint.get(i) ;
                    pointHelpEntity.setyYAxis(minH+(maxYValue-pointHelpEntity.getyValue())*scale);
                }
                for (int i = 0; i < bottomPoint.size(); i++) {
                    PointHelpEntity pointHelpEntity = bottomPoint.get(i) ;
                    pointHelpEntity.setyYAxis(minH+(maxYValue-pointHelpEntity.getyValue())*scale);
                }
            }
        }
    }
    private void drawGrayLine() {
        mGaryLinePaint.setPathEffect(new DashPathEffect(new float[]{3 * mDensity, 2 * mDensity}, 0));
        Path path = new Path();
        // 路径起点
        path.moveTo(0 , mHeight - mSpace/2);
        // 路径连接到
        path.lineTo(mWidth , mHeight  - mSpace/2);
        mCanvas.drawPath(path, mGaryLinePaint); //底部线

        Path path2 = new Path();
        // 路径起点
        path2.moveTo(0 , (mHeight - mSpace/2)/2);
        // 路径连接到
        path2.lineTo(mWidth , (mHeight - mSpace/2)/2);
        mCanvas.drawPath(path2, mGaryLinePaint);

    }

    private void drawYin() {
        int lenth = topPoint.size() ;
        if(lenth > 0){
            float[]  pointXAxis= new float[lenth] ;
            float[]  topYAxis= new float[lenth] ;
            float[]  bottomYAxis= new float[lenth] ;
            for (int i = 0; i < topPoint.size(); i++) {
                pointXAxis[i] = textXAxis[topPoint.get(i).getxPosition()] ;
                topYAxis[i] = topPoint.get(i).getyYAxis() ;
            }
            for (int i = 0; i < bottomPoint.size() ; i++) {
                bottomYAxis[i] = bottomPoint.get(i).getyYAxis() ;
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


    /**
     * 画折线图
     *
     */
    private void drawTopChart(){
        int alpha2 = 255;
        for (int i = 0; i < topPoint.size(); i++) {
            // 画线
            PointHelpEntity tempEntity    = topPoint.get(i) ;
            if (i < topPoint.size() - 1) {
                PointHelpEntity tempEntity2 = topPoint.get(i + 1) ;
                mLinePaint.setAlpha(alpha2);
                mLinePaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity.getxPosition()], tempEntity.getyYAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getyYAxis(), mLinePaint);
            }
            // 画点
            mPointPaint.setAlpha(alpha2);
            mCanvas.drawCircle(textXAxis[tempEntity.getxPosition()], tempEntity.getyYAxis(), mRadius, mPointPaint);
            mCanvas.drawCircle(textXAxis[tempEntity.getxPosition()],tempEntity.getyYAxis(), mInteriorRadius, mInterior);
        }
    }
    /**
     * 画折线图
     *
     */
    private void drawBottomChart() {
        int alpha2 = 255;
        for (int i = 0; i < bottomPoint.size(); i++) {
            // 画线
            PointHelpEntity tempEntity    = bottomPoint.get(i) ;
            if (i < bottomPoint.size() - 1) {
                PointHelpEntity tempEntity2 = bottomPoint.get(i + 1) ;
                mLinePaint.setAlpha(alpha2);
                mLinePaint.setPathEffect(null);
                mCanvas.drawLine(textXAxis[tempEntity.getxPosition()], tempEntity.getyYAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getyYAxis(), mLinePaint);
            }
            // 画点
            mPointPaint.setAlpha(alpha2);
            mCanvas.drawCircle(textXAxis[tempEntity.getxPosition()], tempEntity.getyYAxis(), mRadius, mPointPaint);
            mCanvas.drawCircle(textXAxis[tempEntity.getxPosition()],tempEntity.getyYAxis(), mInteriorRadius, mInterior);
        }
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

    public void setTopPoint(List<PointHelpEntity> topPoint) {
        this.topPoint = topPoint;
        this.isReset = true ;
    }

    public void setBottomPoint(List<PointHelpEntity> bottomPoint) {
        this.bottomPoint = bottomPoint;
    }


}
