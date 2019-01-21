package com.jq.btc.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.jq.btc.app.R;


/**
 * describe：圆形指示器
 *
 */
public class CircleIndicatorView extends View {

    /**
     * 画笔 线
     */
    private Paint mLinePaint;

    /**
     * 线 起始颜色
     */
    private int mLineStartColor;

    /**
     * 线 结束颜色
     */
    private int mLineEndColor;

    /**
     * 线 宽度
     */
    private int mLineWidth;

    /**
     * 画笔 圆形指示器
     */
    private Paint mCirclePaint;

    /**
     * 圆 半径
     */
    private int mCircleRadius;

    /**
     * 圆 颜色
     */
    private int mCircleColor;

    /**
     * 圆 背景颜色
     */
    private int mCircleBackColor;

    /**
     * 圆 当前坐标
     */
    private float mCircleY;

    public CircleIndicatorView(Context context) {
        this(context, null);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化一些参数
//        mLineStartColor = ContextCompat.getColor(context, R.color.colorIndicatorStart);
//        mLineEndColor = ContextCompat.getColor(context, R.color.colorIndicatorEnd);

        mLineStartColor = ContextCompat.getColor(context, R.color.colorSelected);
        mLineEndColor = ContextCompat.getColor(context, R.color.colorSelected);
        mLineWidth = UIUtils.dp2px(context, 0.5);

        mCircleColor = ContextCompat.getColor(context, R.color.colorSelected1);
        mCircleBackColor = ContextCompat.getColor(context, R.color.colorBaseBlack);
        mCircleRadius = UIUtils.dp2px(context, 5);

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mLineWidth);
        PathEffect effects = new DashPathEffect(new float[]{4,4,4,4},1);
        mLinePaint.setPathEffect(effects);
        Shader shader = new LinearGradient(0, getHeight(), getWidth(), 0, mLineEndColor, mLineStartColor, Shader.TileMode.CLAMP);
        mLinePaint.setShader(shader);

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mCircleY == 0) {
            return;
        }
        //画线
        drawLine(canvas);
        //画圆形指示器
        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        mCirclePaint.setColor(mCircleColor);
        canvas.drawCircle(getWidth() / 2, mCircleY, 10, mCirclePaint);
        mCirclePaint.setColor(mCircleBackColor);
//        canvas.drawCircle(getWidth() / 2, mCircleY, mCircleRadius / 2, mCirclePaint);
    }

    private void drawLine(Canvas canvas) {
        int left = getWidth() / 2;
        int bottom = getHeight()-12;
        int top = 0;
        Path path = new Path();
        path.moveTo(left, top);
        path.lineTo(left, bottom);
        canvas.drawPath(path, mLinePaint);
    }

    public void setCircleY(float circleY) {
        mCircleY = circleY;
        invalidate();
    }


    public int getmLineStartColor() {
        return mLineStartColor;
    }

    public void setmLineStartColor(int mLineStartColor) {
        this.mLineStartColor = mLineStartColor;
    }

    public int getmLineEndColor() {
        return mLineEndColor;
    }

    public void setmLineEndColor(int mLineEndColor) {
        this.mLineEndColor = mLineEndColor;
    }

    public int getmLineWidth() {
        return mLineWidth;
    }

    public void setmLineWidth(int mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public int getmCircleRadius() {
        return mCircleRadius;
    }

    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    public int getmCircleColor() {
        return mCircleColor;
    }

    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    public int getmCircleBackColor() {
        return mCircleBackColor;
    }

    public void setmCircleBackColor(int mCircleBackColor) {
        this.mCircleBackColor = mCircleBackColor;
    }
}
