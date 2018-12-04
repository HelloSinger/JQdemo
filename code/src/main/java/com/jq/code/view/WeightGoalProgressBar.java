package com.jq.code.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;

/**
 * 描述：环形的ProgressBar,用于体重达标进度
 */
public class WeightGoalProgressBar extends View {
    private static final int DEFAULT_MAX_VALUE = 100;
    private static final int START_ANGLE = -75;
    private static final int SWEEP_ANGLE = 330;

    /**
     * 当前进度
     */
    private int mProgress = 0;

    /**
     * The max.
     */
    private int max = DEFAULT_MAX_VALUE;

    //绘制轨迹
    /**
     * The path paint.
     */
    private Paint pathPaint = null;

    //绘制填充
    /**
     * The fill arc paint.
     */
    private Paint fillArcPaint = null;


    /**
     * The oval.
     */
    private RectF oval;

    //环的路径宽度
    /**
     * The path width.
     */
    private int pathWidth;

    //默认圆的半径
    /**
     * The radius.
     */
    private int radius;

    //监听器
    /**
     * The m ab on mProgress listener.
     */
    private AbOnProgressListener mAbOnProgressListener = null;

    //view重绘的标记
    /**
     * The reset.
     */
    private boolean reset = false;


    /**
     * Instantiates a new ab circle mProgress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public WeightGoalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WeightGoalProgressBar);
        pathWidth = a.getInteger(R.styleable.WeightGoalProgressBar_line_path_width, 10);
        a.recycle();
        pathWidth = (int) (Resources.getSystem().getDisplayMetrics().density * pathWidth + 0.5);
        pathPaint = new Paint();
        // 设置是否抗锯齿
        pathPaint.setAntiAlias(true);
        // 帮助消除锯齿
        pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setDither(true);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setColor(Color.parseColor("#20ffffff"));
        // 设置画笔宽度
        pathPaint.setStrokeWidth(pathWidth);
        fillArcPaint = new Paint();
        // 设置是否抗锯齿
        fillArcPaint.setAntiAlias(true);
        // 帮助消除锯齿
        fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        // 设置中空的样式
        fillArcPaint.setStyle(Paint.Style.STROKE);
        fillArcPaint.setDither(true);
        fillArcPaint.setStrokeWidth(pathWidth);
        fillArcPaint.setStrokeJoin(Paint.Join.ROUND);
        //设置线的类型,边是圆的
        fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
        fillArcPaint.setColor(0xFFFFFFFF);

        oval = new RectF();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (reset) {
            canvas.drawColor(Color.TRANSPARENT);
            reset = false;
        }
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        this.radius = getMeasuredWidth() / 2 - pathWidth;

        oval.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);
        canvas.drawArc(oval, START_ANGLE, SWEEP_ANGLE, false, pathPaint);
        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
        // startAngle指的是绘制的起始角度，钟表的3点位置对应着0度，如果传入的startAngle小于0或者大于等于360，那么用startAngle对360进行取模后作为起始绘制角度。
        // sweepAngle指的是从startAngle开始沿着钟表的顺时针方向旋转扫过的角度。
        // 如果sweepAngle大于等于360，那么会绘制完整的椭圆弧。如果sweepAngle小于0，那么会用sweepAngle对360进行取模后作为扫过的角度。
        if(mProgress > 0) {
            canvas.drawArc(oval, START_ANGLE, ((float) mProgress / max) * SWEEP_ANGLE, false, fillArcPaint);
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return mProgress;
    }

    public void setProgress(int progress) {
        mProgress = progress > max ? max : progress;
        invalidate();
        if (mAbOnProgressListener != null) {
            if (mProgress >= max) {
                mAbOnProgressListener.onComplete();
            } else {
                mAbOnProgressListener.onProgress(progress);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * Gets the ab on mProgress listener.
     *
     * @return the ab on mProgress listener
     */
    public AbOnProgressListener getAbOnProgressListener() {
        return mAbOnProgressListener;
    }

    /**
     * Sets the ab on mProgress listener.
     *
     * @param mAbOnProgressListener the new ab on mProgress listener
     */
    public void setAbOnProgressListener(AbOnProgressListener mAbOnProgressListener) {
        this.mAbOnProgressListener = mAbOnProgressListener;
    }


    /**
     * 描述：重置进度.
     */
    public void reset() {
        reset = true;
        mProgress = 0;
        invalidate();
    }

    /**
     * 进度监听器.
     *
     * @see
     */
    public interface AbOnProgressListener {

        /**
         * 描述：进度.
         *
         * @param progress the mProgress
         */
        void onProgress(int progress);

        /**
         * 完成.
         */
        void onComplete();

    }
}
