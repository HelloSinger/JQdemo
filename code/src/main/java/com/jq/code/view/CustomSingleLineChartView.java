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

/**
 * 自定义单线图
 */
public class CustomSingleLineChartView extends View {
    private int xSesion ;
    private List<PointHelpEntity> pointEntitys;
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
     * 白天折线颜色
     */
    private int mColorLine;

    /**
     * 白天折线颜色
     */
    private int mYinColor;
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
    private float scale = 0.2f ;   //显示在视图中间占比

    public void setScale(float scale) {
        this.scale = scale;
    }

    public CustomSingleLineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomSingleLineChartView);
        xSesion = a.getInteger(R.styleable.CustomSingleLineChartView_xSesionValue,7) ;
        mColorLine = a.getColor(R.styleable.CustomSingleLineChartView_lineColor,
                getResources().getColor(R.color.bloodglucose_gray));
        mYinColor = a.getColor(R.styleable.CustomSingleLineChartView_yinColor,  //阴影颜色
                getResources().getColor(R.color.text_black));
        mSpace = a.getFloat(R.styleable.CustomSingleLineChartView_spaceValue, 10.0f);
        a.recycle();

        mDensity = getResources().getDisplayMetrics().density;
        mRadius = 5 * mDensity;
        mInteriorRadius = 3 * mDensity ;
        mSpace = mSpace * mDensity;

        float stokeWidth = 2 * mDensity;
        mLinePaint = new Paint();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mColorLine);
        mLinePaint.setStrokeWidth(stokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mGaryLinePaint = new Paint();
        mGaryLinePaint.setAntiAlias(true);
        mGaryLinePaint.setStrokeWidth(mDensity);
        mGaryLinePaint.setStyle(Paint.Style.STROKE);
        mGaryLinePaint.setColor(getResources().getColor(R.color.text_gray));

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(mColorLine);


        mInterior = new Paint();
        mInterior.setAntiAlias(true);
        mInterior.setColor(getResources().getColor(R.color.white));

        yinPaint =new Paint(Paint.ANTI_ALIAS_FLAG);
        yinPaint.setColor(mYinColor);
     }

    public CustomSingleLineChartView(Context context) {
        super(context);
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
            if(pointEntitys == null){
                return;
            }
            resetCavas();
        }
        // 将前面已经画过得显示出来
        if(baseBitmap != null){
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
        drawChart();
    }
    private void computeXAxis() {
        textXAxis = new float[xSesion] ;
        float yifenX = (mWidth - 2 * mSpace) / (xSesion-1) ;
        for (int i = 0; i < xSesion; i++) {
            textXAxis[i] =  mSpace + i * yifenX ;
        }
    }
    private void computePointYAxis() {
        if(pointEntitys.size() > 0){
           float maxYValue = pointEntitys.get(0).getyValue() ;
           float minYValue = pointEntitys.get(0).getyValue()  ;
            for (int i = 0; i <pointEntitys.size() ; i++) {
                if(pointEntitys.get(i).getyValue() > maxYValue){
                    maxYValue = pointEntitys.get(i).getyValue()  ;
                }
                if(pointEntitys.get(i).getyValue()  < minYValue){
                    minYValue = pointEntitys.get(i).getyValue()  ;
                }
            }
            float trueHight = mHeight  -  mSpace ;
            if(maxYValue == minYValue){
                for (int i = 0; i < pointEntitys.size(); i++) {
                    PointHelpEntity pointHelpEntity = pointEntitys.get(i) ;
                    pointHelpEntity.setyYAxis(0.5f * trueHight);
                }
            }else{
                float minH = trueHight* ((1-scale)/2);
                float scale = (trueHight * this.scale)/(maxYValue-minYValue);
                for (int i = 0; i < pointEntitys.size(); i++) {
                    PointHelpEntity pointHelpEntity = pointEntitys.get(i) ;
                    pointHelpEntity.setyYAxis(minH+(maxYValue-pointHelpEntity.getyValue())*scale);
                }
            }
        }
    }
    private void drawGrayLine() {
        mGaryLinePaint.setPathEffect(new DashPathEffect(new float[]{3 * mDensity, 2 * mDensity}, 0));
        Path path = new Path();
        // 路径起点
        path.moveTo(0, mHeight - mSpace/2);
        // 路径连接到
        path.lineTo(mWidth , mHeight - mSpace/2);
        mCanvas.drawPath(path, mGaryLinePaint); //底部线

        Path path2 = new Path();
        // 路径起点
        path2.moveTo(0 , (mHeight - mSpace/2)/2);
        // 路径连接到
        path2.lineTo(mWidth , (mHeight - mSpace/2)/2);
        mCanvas.drawPath(path2, mGaryLinePaint);
    }

    private void drawYin() {
        int lenth = pointEntitys.size() ;
        if(lenth > 0){
            float[]  pointXAxis= new float[lenth] ;
            float[]  topYAxis= new float[lenth] ;
            for (int i = 0; i < pointEntitys.size(); i++) {
                pointXAxis[i] = textXAxis[pointEntitys.get(i).getxPosition()] ;
                topYAxis[i] = pointEntitys.get(i).getyYAxis() ;
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

    /**
     * 画折线图
     *
     */
    private void drawChart() {
        int alpha2 = 255;
        for (int i = 0; i < pointEntitys.size(); i++) {
            // 画线
            PointHelpEntity tempEntity    = pointEntitys.get(i) ;
            if (i < pointEntitys.size() - 1) {
                PointHelpEntity tempEntity2 = pointEntitys.get(i + 1) ;
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

    public List<PointHelpEntity> getPointEntitys() {
        return pointEntitys;
    }

    public void setPointEntitys(List<PointHelpEntity> pointEntitys) {
        this.pointEntitys = pointEntitys;
        this.isReset = true ;
    }

    public void setxSesion(int xSesion) {
        this.xSesion = xSesion;
    }

}
