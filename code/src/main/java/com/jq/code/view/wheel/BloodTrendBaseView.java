
package com.jq.code.view.wheel;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;
import com.jq.code.model.XHelpEntity;

import java.util.List;

/**
 * 折线温度双曲线
 *
 * @author 咖枯
 * @version 1.0 2015/11/06
 */
public abstract class BloodTrendBaseView extends View {
    protected int xSesion ;
    /**
     * textX轴坐标
     */
    protected float textXAxis[];

    /**
     * 控件高
     */
    protected int mHeight;
    /**
     * 控件高
     */
    protected int mWidth;
    /**
     * 密度
     */
    protected float mDensity;

    /**
     * Text与图形之间的空隙
     */
    protected float mSpace;
    /**
    /**
     * Ytext宽度
     */
    protected float YTextWidth ;
    /**
     * X字体大小
     */
    protected float XTextSize;
    /**
     * Y字体大小
     */
    protected float YTextSize;
    /**
     * Text Y画笔
     */
    protected Paint YTextPaint;
    /**
     * Text X画笔
     */
    protected Paint XTextPaint;
    /**
     * x轴内容
     */
    private List<XHelpEntity> xHelpEntities ;

    protected float maxYValue ,minYValue  ;
    /**
     * 点画笔
     */
    protected Paint pointPaint;
    protected Paint interiorPaint ;

    public int getxSesion() {
        return xSesion;
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

    /**
     * 线画笔
     */
    protected Paint linePaint;
    protected float radius;
    protected float interiorRadius;
    /**
     * 线画笔
     */
    protected Paint yinPaint;
    protected boolean isShowYin = false ;
    protected boolean isShowText = true ;

    public boolean isShowText() {
        return isShowText;
    }

    public void setShowText(boolean showText) {
        isShowText = showText;
    }

    public boolean isShowYin() {
        return isShowYin;
    }

    public void setShowYin(boolean showYin) {
        isShowYin = showYin;
    }




    public void setxHelpEntities(List<XHelpEntity> xHelpEntities) {
        this.xHelpEntities = xHelpEntities;
    }


    public BloodTrendBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BloodTrendBaseView);
        xSesion = a.getInteger(R.styleable.BloodTrendBaseView_xSesion,7) ;
        int pointColor = a.getColor(R.styleable.BloodTrendBaseView_pointPaintColor,  //阴影颜色
                getResources().getColor(R.color.red_line));
        int connetColor = a.getColor(R.styleable.BloodTrendBaseView_connLineColor,  //阴影颜色
                getResources().getColor(R.color.bloodglucose_level1));
        int mYinColor = a.getColor(R.styleable.BloodTrendBaseView_connYinColor,  //阴影颜色
                getResources().getColor(R.color.text_black));
        a.recycle();

        mDensity = getResources().getDisplayMetrics().density;
        mSpace = 6 * mDensity;
        XTextSize = 10 * mDensity ;
        YTextSize = 10 * mDensity ;
        YTextWidth = 2 * YTextSize ;
        radius = 5 * mDensity;
        interiorRadius = 3 * mDensity ;

        XTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        XTextPaint.setTextSize(XTextSize);
        XTextPaint.setTextAlign(Paint.Align.CENTER);
        XTextPaint.setColor(Color.parseColor("#cccccc"));
        YTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        YTextPaint.setTextSize(YTextSize);
        YTextPaint.setTextAlign(Paint.Align.CENTER);
        YTextPaint.setColor(Color.parseColor("#cccccc"));

        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setColor(pointColor);

        interiorPaint = new Paint();
        interiorPaint.setAntiAlias(true);
        interiorPaint.setColor(Color.parseColor("#ffffff"));

        float stokeWidth = 2 * mDensity;
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(stokeWidth);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(connetColor);
        yinPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setColor(mYinColor);
    }



    public BloodTrendBaseView(Context context) {
        super(context);
    }
    private Bitmap baseBitmap;
    public boolean isReset = true ;
    protected Canvas mCanvas;
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
            canvas.drawBitmap(baseBitmap, 0, 0, null);
        }
        invalidate();
    }
    public void resetCavas(){
        computeXAxis()  ;
        computeYAxis() ;
        baseBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(baseBitmap);
        mCanvas.drawColor(Color.WHITE);
        // 计算y轴集合数值
        if(isShowYin){
            drawYin() ;
        }
        if(isShowText){
            drawText() ;
        }
        drawLineAndPoint() ;
    }
    protected abstract void drawYin() ;


    protected abstract void drawLineAndPoint() ;


    protected void computeXAxis() {
        textXAxis = new float[xSesion] ;
        float yifenX = (mWidth - YTextWidth -  4 * mSpace) / (xSesion-1) ;
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] = YTextWidth +  2 * mSpace + i * yifenX ;
        }
    }
    protected abstract void computeYAxis();

    protected  void drawText(){
        if(xHelpEntities != null){
            for (int i = 0; i < xHelpEntities.size(); i++) {    //x轴text
                mCanvas.drawText(xHelpEntities.get(i).getTextStr() , textXAxis[xHelpEntities.get(i).getPosition()]  , mHeight - mSpace , XTextPaint);   //画x轴
            }
        }
    }
}
