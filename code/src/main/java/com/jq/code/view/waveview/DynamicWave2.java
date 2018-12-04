package com.jq.code.view.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Administrator on 2017/7/25.
 */

public class DynamicWave2 extends SurfaceView {

    // 波纹颜色
    //private static final int WAVE_PAINT_COLOR = 0x880000aa;
    private static final int WAVE_PAINT_COLOR = Color.argb(200, 239, 188, 193);
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 30;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset = 200;
    private int mXTwoOffset;

    private Paint mWavePaint, mTwoWavePaint;
    private DrawFilter mDrawFilter;

    private SurfaceHolder mHolder;
    private boolean mIsRunning;
    private DrawThread mDrawThread;
    private int mWavePaintColor = 0xFFFFFFFF;

    public DynamicWave2(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = UiUtils.dipToPx(context, TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        //mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(mWavePaintColor);

        mTwoWavePaint = new Paint();
        // 去除画笔锯齿
        // mTwoWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mTwoWavePaint.setStyle(Paint.Style.FILL);
        // 设置画笔颜色
        //Color.argb(127, 255, 255, 255)
        mTwoWavePaint.setColor(0x88FFFFFF);

        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

        mHolder = getHolder();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mIsRunning = true;
                if (null != mDrawThread) {
                    if (mDrawThread.isAlive()) {
                        mDrawThread.interrupt();
                    }
                    mDrawThread = null;
                }
                mDrawThread = new DrawThread();
                mDrawThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mIsRunning = false;
                if (null != mDrawThread) {
                    if (mDrawThread.isAlive()) {
                        mDrawThread.interrupt();
                    }
                    mDrawThread = null;
                }
            }
        });
    }

//    public void setWavePaintColor(int wavePaintColor) {
//        if(true) {
//            return;
//        }
//        mWavePaintColor = wavePaintColor;
//
//        // 初始绘制波纹的画笔
//        mWavePaint = new Paint();
//        // 去除画笔锯齿
//        mWavePaint.setAntiAlias(true);
//        // 设置风格为实线
//        mWavePaint.setStyle(Paint.Style.FILL);
//        // 设置画笔颜色
//        mWavePaint.setColor(mWavePaintColor);
//    }

    private class DrawThread extends Thread {

        @Override
        public void run() {
            while (mIsRunning) {
                Canvas canvas = mHolder.lockCanvas();
                try {
                    if (null != canvas) {

//                        canvas.drawColor(0xFF33BEFF);
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        // 从canvas层面去除绘制时锯齿
                        //canvas.setDrawFilter(mDrawFilter);
                        resetPositonY();

                        for (int i = 0; i < mTotalWidth; i++) {

                            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
                            // 绘制第一条水波纹
                            canvas.drawLine(i, mTotalHeight, i,
                                    mTotalHeight / 2 - mResetOneYPositions[i],
                                    mWavePaint);



                            // 绘制第二条水波纹
                            canvas.drawLine(i, mTotalHeight, i,
                                    mTotalHeight / 2 - mResetTwoYPositions[i],
                                    mTwoWavePaint);

                        }

                        // 改变两条波纹的移动点
                        mXOneOffset += mXOffsetSpeedOne;
                        mXTwoOffset += mXOffsetSpeedTwo;


                        // 如果已经移动到结尾处，则重头记录
                        if (mXOneOffset >= mTotalWidth) {
                            mXOneOffset = 0;
                        }
                        if (mXTwoOffset > mTotalWidth) {
                            mXTwoOffset = 0;
                        }
                        mHolder.unlockCanvasAndPost(canvas);
                        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
                        Thread.sleep(30);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            //mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
            mYPositions[i] = (float) (((h - 4) / 2) * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

}
