package com.jq.code.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：环形的ProgressBar
 */
public class CustomReportViewForGoalWegiht extends View {
    private static final int LINE_WIDTH = 6;
    private static final int WHITE_LINE_LENGTH = 2;
    private static final int FACE_WIDTH = 18;
    private static final int TEXT_SPACE = 15;
    private int mWidth;
    private int mHeight;
    private int density;
    private int lineWidth;
    private int radius;
    private int faceWidth;
    private int textSpace;
    private int textHight;
    float realBetween;
    private Paint paint;
    private Paint whitePaint;
    private Paint textPaint;
    private int whiteLineLength;
    private List<Integer> colors;
    private List<Float> xAxis;
    private String[] topStr;
    private int[] bottomStrRes;
    private String[] bottomStrings;
    private float[] standardRange;
    private float value;
    private int face;
    private String[] bottomMiddleStr;

    public List<Integer> getColors() {
        return colors;
    }

    public void setBottomMiddleStr(String[] bottomMiddleStr) {
        this.bottomMiddleStr = bottomMiddleStr;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

    public void setContent(String[] topStr, int[] bottomStr, float[] standardRange, float value, int face) {
        this.bottomStrRes = bottomStr;

        String[] bStrs = null;
        if (null != bottomStrRes && bottomStrRes.length > 0) {
            bStrs = new String[bottomStrRes.length];
            for (int i = 0; i < bottomStrRes.length; i++) {
                String tempText = getResources().getString(bottomStrRes[i]);
                bStrs[i] = tempText;
            }
        }
        setContent(topStr, bStrs, standardRange, value, face);
    }

    public void setContent(String[] topStr, String[] bottomStr, float[] standardRange, float value, int face) {
        this.topStr = topStr;
        this.bottomStrings = bottomStr;
        this.standardRange = standardRange;
        this.value = value;
        this.face = face;
        invalidate();
    }

    public CustomReportViewForGoalWegiht(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = (int) getResources().getDisplayMetrics().density;
        lineWidth = density * LINE_WIDTH;
        whiteLineLength = density * WHITE_LINE_LENGTH;
        faceWidth = density * FACE_WIDTH;
        textSpace = density * TEXT_SPACE;
        radius = lineWidth / 2;
        paint = new Paint();
        // 设置是否抗锯齿
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        // 设置中空的样式
        paint.setStyle(Paint.Style.FILL);

        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setStrokeWidth(lineWidth);
        whitePaint.setStyle(Paint.Style.FILL);
        whitePaint.setColor(Color.WHITE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(10 * density);
        textPaint.setColor(Color.GRAY);
        String testStr = "偏高";
        Rect rect = new Rect();
        textPaint.getTextBounds(testStr, 0, testStr.length(), rect);
        textHight = rect.height();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calXAxis();
        drawColorLine(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {

        String text1 = bottomStrings[1];
        Rect r = new Rect();
        textPaint.getTextBounds(text1, 0, text1.length(), r);
        int tw = r.width();
        canvas.drawText(text1, xAxis.get(1) + (xAxis.get(2) - xAxis.get(1)) / 2 - tw / 2, mHeight / 2 + lineWidth / 2 + textSpace + textHight, textPaint);

        if (null != bottomMiddleStr && bottomMiddleStr.length > 0) {
            for (int i = 0; i < bottomMiddleStr.length; i++) {
                String tempText = bottomMiddleStr[i];
                Rect rect = new Rect();
                textPaint.getTextBounds(tempText, 0, tempText.length(), rect);
                int textWidth = rect.width();
                canvas.drawText(tempText, xAxis.get(i + 1) - textWidth / 2, mHeight / 2 + lineWidth / 2 + textSpace + textHight, textPaint);
            }
        }
    }

    public void drawColorLine(Canvas canvas) {
        if (colors == null || colors.size() == 0) return;
        for (int i = 0; i < colors.size(); i++) {
            int color = colors.get(i);
            paint.setColor(getResources().getColor(color));
            if (i == 1) {
                canvas.drawLine(xAxis.get(i), mHeight / 2, xAxis.get(i + 1), mHeight / 2, paint);
            } else {
                canvas.drawLine(xAxis.get(i), mHeight / 2, xAxis.get(i) + realBetween, mHeight / 2, paint);
            }
        }
        for (int i = 0; i < colors.size() - 1; i++) {
            canvas.drawLine(xAxis.get(i + 1) - whiteLineLength / 2, mHeight / 2, xAxis.get(i + 1) + whiteLineLength / 2, mHeight / 2, whitePaint);
        }
        //画两个圆
        paint.setColor(getResources().getColor(colors.get(0)));
        canvas.drawCircle(xAxis.get(0), mHeight / 2, radius, paint);
        paint.setColor(getResources().getColor(colors.get(colors.size() - 1)));
        canvas.drawCircle(xAxis.get(xAxis.size() - 1) + realBetween, mHeight / 2, radius, paint);
    }

    public void calXAxis() {
        if (colors == null || colors.size() == 0) return;
        xAxis = new ArrayList<Float>();
        int calWidth = mWidth - 2 * radius /*- faceWidth */;
        realBetween = calWidth / (colors.size() + 1);
//        for (int i = 0; i <colors.size() ; i++) {
//            xAxis.add(/*faceWidth /2 + */radius + i * realBetween) ;
//
//        }
        xAxis.add((float) radius);
        xAxis.add((float) radius + realBetween);
        xAxis.add((float) radius + realBetween * 3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, height);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();

    }

}
