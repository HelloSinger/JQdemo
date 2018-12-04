package com.jq.code.view.trend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.jq.code.model.trend.RecentWeghtEntity;
import com.jq.code.view.wheel.BloodTrendBaseView;

import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public class SynthTrendWeightView extends BloodTrendBaseView {
    private List<RecentWeghtEntity> recentEntitys;
    private Paint mGaryLinePaint;
    private float scale = 0.2f;
    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    private String showType;

    public void setRecentEntitys(List<RecentWeghtEntity> recentEntitys) {
        this.recentEntitys = recentEntitys;
        isReset = true;
    }

    public SynthTrendWeightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mGaryLinePaint = new Paint();
        mGaryLinePaint.setAntiAlias(true);
        mGaryLinePaint.setStrokeWidth(1);
        mGaryLinePaint.setStyle(Paint.Style.STROKE);
        mGaryLinePaint.setColor(Color.parseColor("#cccccc"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (recentEntitys == null)
            return;
        super.onDraw(canvas);
    }

    protected void drawYin() {
        if (isShowYin) {
            int lenth = recentEntitys.size();
            if (lenth > 0) {
                float[] pointXAxis = new float[lenth];
                float[] topYAxis = new float[lenth];
                for (int i = 0; i < recentEntitys.size(); i++) {
                    pointXAxis[i] = textXAxis[recentEntitys.get(i).getxPosition()];
                    topYAxis[i] = recentEntitys.get(i).getyAxis();
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
        for (int i = 0; i < recentEntitys.size(); i++) {
            RecentWeghtEntity tempEntity1 = recentEntitys.get(i);
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), radius + 3, interiorPaint);
            if (i < recentEntitys.size() - 1) {
                RecentWeghtEntity tempEntity2 = recentEntitys.get(i + 1);
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
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), radius, pointPaint);
            mCanvas.drawCircle(textXAxis[tempEntity1.getxPosition()], tempEntity1.getyAxis(), interiorRadius, interiorPaint);
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
            if (recentEntitys.size() > 0) {
                float tempValue = RecentWeghtEntity.getTypeValue(showType, recentEntitys.get(0));
                maxYValue = tempValue;
                minYValue = tempValue;
                for (int i = 0; i < recentEntitys.size(); i++) {
                    RecentWeghtEntity tempTrend = recentEntitys.get(i);
                    float tempValue1 = RecentWeghtEntity.getTypeValue(showType, tempTrend);
                    if (tempValue1 > maxYValue) {
                        maxYValue = tempValue1;
                    }
                    if (tempValue1 < minYValue) {
                        minYValue = tempValue1;
                    }
                }
                float trueHight = mHeight - XTextSize - mSpace;
                if (maxYValue == minYValue) {
                    for (int i = 0; i < recentEntitys.size(); i++) {
                        RecentWeghtEntity weightTrend = recentEntitys.get(i);
                        weightTrend.setyAxis(0.5f * trueHight);
                    }
                } else {
                    float minH = trueHight * ((1 - scale) / 2);
                    float scale = (trueHight * this.scale) / (maxYValue - minYValue);
                    for (int i = 0; i < recentEntitys.size(); i++) {
                        RecentWeghtEntity weightTrend = recentEntitys.get(i);
                        float tempValue1 = RecentWeghtEntity.getTypeValue(showType, weightTrend);
                        weightTrend.setyAxis(minH + (maxYValue - tempValue1) * scale);
             }
          }
       }
    }

}
