package com.jq.code.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jq.code.R;

/**
 * 描述：环形的ProgressBar
 */
public class ExerciseFoodProgressBar extends View {
    public static final int DEFAULT_MAXT_VALUE = 8000 ;
    public static final int MODE_NORMAL = 1;
    public static final int MODE_REVERSAL= -1;
    private int mode = MODE_NORMAL;

    /**
     * The progress.
     */
    private int progress;

    /**
     * The max.
     */
    private int max = DEFAULT_MAXT_VALUE;

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

    //灰色轨迹

    //环的路径宽度
    /**
     * The path width.
     */
    private int pathWidth ;

    /**
     * The width.
     */
    private int width;

    /**
     * The height.
     */
    private int height;

    //默认圆的半径
    /**
     * The radius.
     */
    private int radius = 120;

    // 指定了光源的方向和环境光强度来添加浮雕效果
    /**
     * The emboss.
     */
    private EmbossMaskFilter emboss = null;
    // 设置光源的方向
    /**
     * The direction.
     */

    float[] direction = new float[]{1, 1, 1};
    //设置环境光亮度
    /**
     * The light.
     */
    float light = 0.4f;
    // 选择要应用的反射等级
    /**
     * The specular.
     */
    float specular = 6;
    // 向 mask应用一定级别的模糊
    /**
     * The blur.
     */
    float blur = 3.5f;

    //监听器
    /**
     * The m ab on progress listener.
     */
    private AbOnProgressListener mAbOnProgressListener = null;

    //view重绘的标记
    /**
     * The reset.
     */
    private boolean reset = false;


    /**
     * Instantiates a new ab circle progress bar.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public ExerciseFoodProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExerciseFoodProgressBar);
        pathWidth = a.getInteger(R.styleable.ExerciseFoodProgressBar_pro_line_path_width,10) ;
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
        pathPaint.setColor(Color.parseColor("#10ffffff"));
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

        oval = new RectF();
        emboss = new EmbossMaskFilter(direction, light, specular, blur);
    }

    /* (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (reset) {
            canvas.drawColor(Color.TRANSPARENT);
            reset = false;
        }
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
        this.radius = getMeasuredWidth() / 2 - pathWidth;

        // 设置画笔颜色 #DCDCDC  pathColor
        oval.set(this.width / 2 - radius, this.height / 2 - radius, this.width / 2 + radius, this.height / 2 + radius);
        canvas.drawArc(oval, -225, 270, false, pathPaint);
        // 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
        canvas.drawArc(oval, -90, mode *((float) progress / max) * 135, false, fillArcPaint);
    }

    /**
     * 描述：获取圆的半径.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }


    /**
     * 描述：设置圆的半径.
     *
     * @param radius the new radius
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Gets the max.
     *
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * Sets the max.
     *
     * @param max the new max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Gets the progress.
     *
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
        if (this.mAbOnProgressListener != null) {
            if (this.max <= this.progress) {
                this.mAbOnProgressListener.onComplete();
            } else {
                this.mAbOnProgressListener.onProgress(progress);
            }
        }
    }

    /**
     * Sets the progress.
     *
     * @param progress the new progress
     */
    public void setProgress(int progress,int mode) {
        this.mode = mode;
        progress = progress > max ? max : progress ;
        if(mode == MODE_NORMAL){
            fillArcPaint.setColor(getResources().getColor(R.color.sport_sport_pop_color)) ;
        }else {
            fillArcPaint.setColor(Color.parseColor("#FFF65D")) ;
        }
        setProgress(progress);
    }

    /* (non-Javadoc)
     * @see android.view.View#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width, height);
    }

    /**
     * Gets the ab on progress listener.
     *
     * @return the ab on progress listener
     */
    public AbOnProgressListener getAbOnProgressListener() {
        return mAbOnProgressListener;
    }

    /**
     * Sets the ab on progress listener.
     *
     * @param mAbOnProgressListener the new ab on progress listener
     */
    public void setAbOnProgressListener(AbOnProgressListener mAbOnProgressListener) {
        this.mAbOnProgressListener = mAbOnProgressListener;
    }


    /**
     * 描述：重置进度.
     */
    public void reset() {
        reset = true;
        this.progress = 0;
        this.invalidate();
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
         * @param progress the progress
         */
        void onProgress(int progress);

        /**
         * 完成.
         */
        void onComplete();

    }
}
