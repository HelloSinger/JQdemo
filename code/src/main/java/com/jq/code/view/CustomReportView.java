package com.jq.code.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：环形的ProgressBar
 */
public class CustomReportView extends View {
    private static final int LINE_WIDTH = 6 ;
    private static final int WHITE_LINE_LENGTH = 2 ;
    private static final int FACE_WIDTH = 18 ;
    private static final int TEXT_SPACE = 15 ;
    private int mWidth;
    private int mHeight;
    private int density ;
    private int lineWidth ;
    private int radius ;
    private int faceWidth ;
    private int textSpace ;
    private int textHight;
    float realBetween ;
    private Paint paint;
    private Paint whitePaint ;
    private Paint textPaint ;
    private int whiteLineLength ;
    private List<Integer> colors ;
    private List<Float> xAxis ;
    private String[] topStr ;
    private int[] bottomStr;
    private float[] standardRange ;
    private float value ;
    private int face ;

    public List<Integer> getColors() {
        return colors;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }
    public void setContent(String[] topStr,int[] bottomStr,float[]standardRange,float value,int face){
        this.topStr = topStr ;
        this.bottomStr = bottomStr ;
        this.standardRange = standardRange ;
        this.value = value ;
        this.face = face ;
        invalidate();
    }
    public CustomReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = (int) getResources().getDisplayMetrics().density;
        lineWidth = density * LINE_WIDTH;
        whiteLineLength = density * WHITE_LINE_LENGTH ;
        faceWidth = density * FACE_WIDTH ;
        textSpace = density * TEXT_SPACE ;
        radius = lineWidth / 2 ;
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
        textPaint.setTextSize(13 * density);
        textPaint.setColor(Color.GRAY);
        String testStr = "偏高";
        Rect rect = new Rect();
        textPaint.getTextBounds(testStr,0,testStr.length(),rect);
        textHight = rect.height() ;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calXAxis();
        drawColorLine(canvas);
        drawText(canvas) ;
        drawFace(canvas) ;
    }

    private void drawFace(Canvas canvas) {
        if(face < 0) {
            // 没有图片，暂无数据的情况
            return;
        }
        InputStream is = getResources().openRawResource(face);
        Bitmap mBitmap = BitmapFactory.decodeStream(is);
        //canvas.drawBitmap(mBitmap,calFacePogress() - mBitmap.getWidth()/2,mHeight/2 - mBitmap.getHeight()/2,whitePaint);
        float f = calFacePogress();
        RectF rectF = new RectF(f - faceWidth / 2f, mHeight/2 - faceWidth / 2f, f + faceWidth / 2f, mHeight/2 + faceWidth / 2f);
        canvas.drawBitmap(mBitmap, null, rectF, null);
    }
    private float calFacePogress() {
//        if (value <= standardRange[0]) {
//            return xAxis.get(0);
//        } else if (value > standardRange[standardRange.length - 1]) {
//            return xAxis.get(xAxis.size() -1) + realBetween;
//        } else {
//            float pogress = 0;
//            for (int i = 1; i < standardRange.length; i++) {
//                if (value <= standardRange[i]) {
//                    pogress = (value - standardRange[i - 1]) * realBetween / (standardRange[i] - standardRange[i - 1]) + realBetween * (i - 1) + faceWidth/2 + radius;
//                    break;
//                }
//            }
//            return pogress;
//        }

        // 计算肥胖度级别
        int corpulentLevel = 0;
        for(int i = 0; i < standardRange.length; i++) {
            if(value < standardRange[i]) {
                break;
            } else {
                corpulentLevel++;
            }
        }
        Log.e("IndexFragmentCorpulent", "corpulentLevel=" + corpulentLevel);
        if(corpulentLevel == 0) {
            return xAxis.get(0) + realBetween / 2;
        } else if(corpulentLevel == standardRange.length) {
            return xAxis.get(standardRange.length) + realBetween / 2;
        } else {
            return xAxis.get(corpulentLevel) + realBetween * (value - standardRange[corpulentLevel - 1])/(standardRange[corpulentLevel] - standardRange[corpulentLevel - 1]);
        }
    }
    private void drawText(Canvas canvas) {
        for (int i = 0; i < topStr.length ; i++) {
            String tempText = topStr[i];
            Rect rect = new Rect();
            textPaint.getTextBounds(tempText,0,tempText.length(),rect);
            int textWidth = rect.width() ;
            canvas.drawText(tempText,xAxis.get(i + 1)-textWidth/2,mHeight / 2 - lineWidth /2 - textSpace ,textPaint);
        }
        for (int i = 0; i < bottomStr.length; i++) {
            String tempText = getResources().getString(bottomStr[i]);
            Rect rect = new Rect();
            textPaint.getTextBounds(tempText,0,tempText.length(),rect);
            int textWidth = rect.width() ;
            canvas.drawText(tempText,xAxis.get(i) + realBetween / 2 - textWidth /2,mHeight / 2 + lineWidth /2 + textSpace + textHight,textPaint);
        }
    }

    public void drawColorLine(Canvas canvas){
        if(colors == null || colors.size() == 0)return;
        for (int i = 0; i <colors.size() ; i++) {
            int color = colors.get(i) ;
            paint.setColor(getResources().getColor(color));
            canvas.drawLine(xAxis.get(i),mHeight / 2,xAxis.get(i) + realBetween ,mHeight /2,paint);
        }
        for (int i = 0; i <colors.size() -1 ; i++) {
            canvas.drawLine(xAxis.get(i + 1) - whiteLineLength / 2,mHeight / 2,xAxis.get(i + 1) + whiteLineLength / 2 ,mHeight /2,whitePaint);
        }
        //画两个圆
        paint.setColor(getResources().getColor(colors.get(0)));
        canvas.drawCircle(xAxis.get(0),mHeight /2 ,radius,paint);
        paint.setColor(getResources().getColor(colors.get(colors.size()-1)));
        canvas.drawCircle(xAxis.get(xAxis.size() -1) + realBetween,mHeight /2 ,radius,paint);
    }
    public void calXAxis(){
        if(colors == null || colors.size() == 0)return;
        xAxis = new ArrayList<Float>() ;
        int calWidth = mWidth - 2 * radius /*- faceWidth */;
        realBetween = calWidth / colors.size() ;
        for (int i = 0; i <colors.size() ; i++) {
            xAxis.add(/*faceWidth /2 + */radius + i * realBetween) ;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, height);
        mHeight = getMeasuredHeight() ;
        mWidth = getMeasuredWidth() ;

    }

}
