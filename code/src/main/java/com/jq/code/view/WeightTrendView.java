package com.jq.code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.code.model.WeightEntity;
import com.jq.code.view.wheel.BloodTrendBaseView;

import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class WeightTrendView extends BloodTrendBaseView implements View.OnTouchListener {

    private List<WeightEntity> weightEntities;
    private float xAxis[];
    private float yAxis[];
    /**
     * textY轴坐标
     */
    protected float textYAxis[];
    private float startValue, targetValue;
    private String showStartValue, showTargetValue;
    private Paint mGaryLinePaint;
    private float scale = 0.2f;

    public String getShowTargetValue() {
        return showTargetValue;
    }

    public void setShowTargetValue(String showTargetValue) {
        this.showTargetValue = showTargetValue;
    }

    public String getShowStartValue() {
        return showStartValue;
    }

    public void setShowStartValue(String showStartValue) {
        this.showStartValue = showStartValue;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    private String showType;

    public float getStartValue() {
        return startValue;
    }

    public void setStartValue(float startValue) {
        this.startValue = startValue;
    }

    public float getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(float targetValue) {
        this.targetValue = targetValue;
    }

    public void setWeightEntities(List<WeightEntity> weightEntities,int isSlect) {
        this.weightEntities = weightEntities;
        selectedIndex = isSlect;
        isReset = true;
    }

    public WeightTrendView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mGaryLinePaint = new Paint();
        mGaryLinePaint.setAntiAlias(true);
        mGaryLinePaint.setStrokeWidth(1);
        mGaryLinePaint.setStyle(Paint.Style.STROKE);
        mGaryLinePaint.setColor(Color.parseColor("#cccccc"));
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (weightEntities == null)
            return;
        super.onDraw(canvas);
        drawGrayLine();
    }

    protected void drawYin() {
        if (isShowYin) {
            int lenth = weightEntities.size();
            if (lenth > 0) {
                float[] pointXAxis = new float[lenth];
                float[] topYAxis = new float[lenth];
                for (int i = 0; i < weightEntities.size(); i++) {
                    pointXAxis[i] = textXAxis[weightEntities.get(i).getxPosition()];
                    topYAxis[i] = weightEntities.get(i).getyAxis();
                }

                int i = 0;
                Path mPath = new Path();
                mPath.moveTo(pointXAxis[0], mHeight - mSpace / 2);
                while (true) {
                    if (i > pointXAxis.length - 1) {
                        break;
                    }
                    mPath.lineTo(pointXAxis[i], topYAxis[i]);
                    ++i;
                }
                --i;
                mPath.lineTo(pointXAxis[i], mHeight - mSpace / 2);
                mPath.close();
                mCanvas.drawPath(mPath, yinPaint);
            }
        }
    }

    protected void drawLineAndPoint() {
        xAxis = new float[weightEntities.size()];
        yAxis = new float[weightEntities.size()];
        for (int i = 0; i < weightEntities.size(); i++) {
            WeightEntity tempEntity1 = weightEntities.get(i);
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), radius + 3, interiorPaint);
            if (i < weightEntities.size() - 1) {
                WeightEntity tempEntity2 = weightEntities.get(i + 1);
                // 画线
                pointPaint.setPathEffect(null);
                if (tempEntity1.getxPosition() > textXAxis.length) {
                    continue;
                }
                mCanvas.drawLine(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), textXAxis[tempEntity2.getxPosition()], tempEntity2.getyAxis(), linePaint);
            }
            //画竖线
            mGaryLinePaint.setPathEffect(new DashPathEffect(new float[]{3 * mDensity, 2 * mDensity}, 0));
            Path path = new Path();
            path.moveTo(textXAxis[tempEntity1.getxPosition()], mHeight - mSpace - XTextSize);
            path.lineTo(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis());
            mCanvas.drawPath(path, mGaryLinePaint); //底部线
            // 画点
            xAxis[i] = textXAxis[tempEntity1.getxPosition()];
            yAxis[i] = tempEntity1.getyAxis();
            if (selectedIndex == i) {
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), radius + 3, pointPaint);
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), interiorRadius - 0.2f, interiorPaint);
            } else {
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), radius, pointPaint);
                mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), interiorRadius, interiorPaint);
            }
        }
    }

    protected void drawText() {
        super.drawText();
        if (startValue != 0) {
            Rect rect = new Rect();
            YTextPaint.getTextBounds(showStartValue, 0, 1, rect);
            int height = rect.height();
            int width = rect.width();
            mCanvas.drawText("初始值", YTextWidth * 2 / 3 + 2, textYAxis[0] - 4, YTextPaint);
            mCanvas.drawText(showStartValue, YTextWidth * 2 / 3 + 2, textYAxis[0] + height + 4, YTextPaint);
            mCanvas.drawText("目标值", mWidth - YTextWidth * 2 / 3 - 2, textYAxis[1] - 4, YTextPaint);
            mCanvas.drawText(showTargetValue, mWidth - YTextWidth * 2 / 3 - 2, textYAxis[1] + height + 4, YTextPaint);
        }
        if(getxSesion() == 12){
            mCanvas.drawText("周/年", YTextWidth * 2 / 3 + 3 , mHeight - mSpace, YTextPaint);
        }
    }

    private void drawGrayLine() {
        if (startValue != 0) {
            mGaryLinePaint.setPathEffect(new DashPathEffect(new float[]{3 * mDensity, 2 * mDensity}, 0));
            Path path = new Path();
            // 路径起点
            path.moveTo(0, textYAxis[0]);
            // 路径连接到
            path.lineTo(mWidth, textYAxis[0]);
            mCanvas.drawPath(path, mGaryLinePaint); //底部线

            Path path2 = new Path();
            // 路径起点
            path2.moveTo(0, textYAxis[1]);
            // 路径连接到
            path2.lineTo(mWidth, textYAxis[1]);
            mCanvas.drawPath(path2, mGaryLinePaint);
        }
    }

    protected void computeXAxis() {
        textXAxis = new float[xSesion];
        float yifenX = (mWidth - 2 * YTextWidth - 5 * radius) / (xSesion - 1);
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] = YTextWidth + radius * 3 + i * yifenX;
        }
    }

    protected void computeYAxis() {
        textYAxis = new float[2];
        if (startValue != 0) {
            maxYValue = startValue > targetValue ? startValue : targetValue;
            minYValue = startValue < targetValue ? startValue : targetValue;
            for (int i = 0; i < weightEntities.size(); i++) {
                float tempValue = WeightEntity.getTypeValue(showType, weightEntities.get(i));
                if (tempValue > maxYValue) {
                    maxYValue = tempValue;
                }
                if (tempValue < minYValue) {
                    minYValue = tempValue;
                }
            }
            float trueHight = mHeight - XTextSize - mSpace;
            float minH = trueHight * ((1 - scale) / 2);
            float scale = (trueHight * this.scale) / (maxYValue - minYValue);
            textYAxis[0] = minH + (maxYValue - startValue) * scale;
            textYAxis[1] = minH + (maxYValue - targetValue) * scale;
            for (int i = 0; i < weightEntities.size(); i++) {
                WeightEntity tempTrend = weightEntities.get(i);
                float tempValue = WeightEntity.getTypeValue(showType, tempTrend);
                tempTrend.setyAxis(minH + (maxYValue - tempValue) * scale);
            }
        } else {
            if (weightEntities.size() > 0) {
                float tempValue = WeightEntity.getTypeValue(showType, weightEntities.get(0));
                maxYValue = tempValue;
                minYValue = tempValue;
                for (int i = 0; i < weightEntities.size(); i++) {
                    WeightEntity tempTrend = weightEntities.get(i);
                    float tempValue1 = WeightEntity.getTypeValue(showType, tempTrend);
                    if (tempValue1 > maxYValue) {
                        maxYValue = tempValue1;
                    }
                    if (tempValue1 < minYValue) {
                        minYValue = tempValue1;
                    }
                }
                float trueHight = mHeight - XTextSize - mSpace;
                if (maxYValue == minYValue) {
                    for (int i = 0; i < weightEntities.size(); i++) {
                        WeightEntity weightTrend = weightEntities.get(i);
                        weightTrend.setyAxis(0.5f * trueHight);
                    }
                } else {
                    float minH = trueHight * ((1 - scale) / 2);
                    float scale = (trueHight * this.scale) / (maxYValue - minYValue);
                    for (int i = 0; i < weightEntities.size(); i++) {
                        WeightEntity weightTrend = weightEntities.get(i);
                        float tempValue1 = WeightEntity.getTypeValue(showType, weightTrend);
                        weightTrend.setyAxis(minH + (maxYValue - tempValue1) * scale);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float downX = event.getX();
        float downY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (xAxis != null && xAxis.length > 0) {
                    for (int i = 0; i < xAxis.length; i++) {
                        if (downX > (xAxis[i] - mSpace)
                                && downX < (xAxis[i] + mSpace)) {
                            setPointSelected(i);
                            if (lIstener != null) {
                                lIstener.onPoint(v, weightEntities.get(i), i);
                            }
                            break;
                        }

                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private int selectedIndex;

    public void setPointSelected(int selectedIndex) {
        this.selectedIndex = selectedIndex;
        drawLineAndPoint();
    }

    OnPointClickListener lIstener;

    public void setOnPointClickListener(OnPointClickListener lIstener) {
        this.lIstener = lIstener;
    }

    public interface OnPointClickListener {
        void onPoint(View v, WeightEntity entity, int position);
    }
}
