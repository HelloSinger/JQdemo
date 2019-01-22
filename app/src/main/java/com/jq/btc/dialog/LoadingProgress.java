package com.jq.btc.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * Create by AYD on 2019/1/22
 */
public class LoadingProgress extends View {
    private Paint mPaint1;
    private Paint mPaint2;
    private int mRadius;
    private RectF oval;
    private float extendDegrees;
    private float startDegrees1;
    private float startDegrees2;
    private boolean extend;
    private boolean shorten;
    private float degrees;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler;

    public LoadingProgress(Context context) {
        this(context, (AttributeSet)null);
    }

    public LoadingProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.startDegrees1 = -90.0F;
        this.startDegrees2 = 90.0F;
        this.extend = true;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what) {
                    case 0:
                       LoadingProgress.this.setRotate();
                        break;
                    case 1:
                        LoadingProgress.this.setExtendDegrees();
                        break;
                    case 2:
                       LoadingProgress.this.setShortenDegrees();
                        break;
                    case 3:
                        LoadingProgress.this.extendDegrees = 0.0F;
                        LoadingProgress.this.extend = false;
                        LoadingProgress.this.shorten = true;
                        LoadingProgress.this.setShortenDegrees();
                        break;
                    case 4:
                        LoadingProgress.this.extendDegrees = 0.0F;
                        LoadingProgress.this.shorten = false;
                        LoadingProgress.this.extend = true;
                        LoadingProgress.this.startDegrees1 = LoadingProgress.this.startDegrees1 + 135.0F;
                        LoadingProgress.this.startDegrees2 = LoadingProgress.this.startDegrees2 + 135.0F;
                        LoadingProgress.this.setExtendDegrees();
                }

            }
        };
        this.mPaint1 = new Paint(1);
        this.mPaint1.setAntiAlias(true);
        this.mPaint1.setStyle(Paint.Style.FILL);
        this.mPaint1.setStrokeWidth(7.0F);
        this.mPaint1.setStyle(Paint.Style.STROKE);
        this.mPaint1.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint1.setDither(true);
        this.mPaint2 = new Paint(1);
        this.mPaint2.setAntiAlias(true);
        this.mPaint2.setStyle(Paint.Style.FILL);
        this.mPaint2.setStrokeWidth(7.0F);
        this.mPaint2.setStyle(Paint.Style.STROKE);
        this.mPaint2.setStrokeCap(Paint.Cap.ROUND);
        this.mPaint2.setDither(true);
        this.setExtendDegrees();
        this.setRotate();
    }

    @SuppressLint({"DrawAllocation"})
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width;
        if (wideMode == 1073741824) {
            width = wideSize;
        } else {
            width = this.mRadius * 2 + this.getPaddingLeft() + this.getPaddingRight();
            if (wideMode == -2147483648) {
                width = Math.min(width, wideSize);
            }
        }

        int height;
        if (heightMode == 1073741824) {
            height = heightSize;
        } else {
            height = this.mRadius * 2 + this.getPaddingTop() + this.getPaddingBottom();
            if (heightMode == -2147483648) {
                height = Math.min(height, heightSize);
            }
        }

        this.setMeasuredDimension(width, height);
        this.mRadius = (int)((float)Math.min(width - this.getPaddingLeft() - this.getPaddingRight(), height - this.getPaddingTop() - this.getPaddingBottom()) * 1.0F / 2.0F);
        this.oval = new RectF((float)(-this.mRadius + 8), (float)(-this.mRadius + 8), (float)(this.mRadius - 8), (float)(this.mRadius - 8));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate((float)((this.getWidth() + this.getPaddingLeft() - this.getPaddingRight()) / 2), (float)((this.getHeight() + this.getPaddingTop() - this.getPaddingBottom()) / 2));
        this.paintPie(canvas);
    }

    private void paintPie(Canvas mCanvas) {
        mCanvas.rotate(this.degrees);
        this.mPaint1.setColor(Color.parseColor("#24b4fd"));
        this.mPaint2.setColor(Color.parseColor("#00ffffff"));
        if (this.extend) {
            mCanvas.drawArc(this.oval, this.startDegrees1, this.extendDegrees, false, this.mPaint1);
            mCanvas.drawArc(this.oval, this.startDegrees2, this.extendDegrees, false, this.mPaint1);
        } else if (this.shorten) {
            mCanvas.drawArc(this.oval, this.startDegrees1, this.extendDegrees, false, this.mPaint2);
            mCanvas.drawArc(this.oval, this.startDegrees1 + this.extendDegrees, 136.0F - this.extendDegrees, false, this.mPaint1);
            mCanvas.drawArc(this.oval, this.startDegrees2, this.extendDegrees, false, this.mPaint2);
            mCanvas.drawArc(this.oval, this.startDegrees2 + this.extendDegrees, 136.0F - this.extendDegrees, false, this.mPaint1);
        }

    }

    private void setRotate() {
        this.degrees += 5.0F;
        this.mHandler.sendEmptyMessageDelayed(0, 20L);
        this.invalidate();
    }

    private void setExtendDegrees() {
        if (this.extendDegrees == 135.0F) {
            this.mHandler.sendEmptyMessageDelayed(3, 1000L);
        } else {
            this.extendDegrees += 5.0F;
            this.mHandler.sendEmptyMessageDelayed(1, 10L);
        }

        this.invalidate();
    }

    private void setShortenDegrees() {
        if (this.extendDegrees == 135.0F) {
            this.mHandler.sendEmptyMessageDelayed(4, 1000L);
        } else {
            this.extendDegrees += 5.0F;
            this.mHandler.sendEmptyMessageDelayed(2, 10L);
        }

        this.invalidate();
    }
}
