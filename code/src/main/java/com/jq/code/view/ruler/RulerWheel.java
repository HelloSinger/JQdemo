package com.jq.code.view.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.jq.code.R;


/**
 * Created by zz date 2015/5/15 Description: &#x53c2;&#x8003;
 */
public class RulerWheel extends View {
    // 默认刻度模式
    public static final int MOD_TYPE_SCALE = 5;
    // 1/2模式
    public static final int MOD_TYPE_HALF = 2;
    // 字体大小
    private int mTextSize = 36;
    // 字体颜色
    private int mTextColor = Color.BLACK;
    // 分隔线(大号)
    private int mLineHeighMax;
    private int mLineColorMax;
    // 分隔线(中号)
    private int mLineHeighMid;
    private int mLineColorMid;
    // 分隔线(小号)
    private int mLineHeighMin;
    private int mLineColorMin;
    // 当前值
    private int mCurrValue;
    // 显示最大值
    private int mMaxValue;
    // 分隔模式
    private int mModType = MOD_TYPE_SCALE;
    // 分隔线之间间隔
    private int mLineDivder;
    // 滚动器
    private WheelHorizontalScroller scroller;
    // 是否执行滚动
    private boolean isScrollingPerformed;
    // 滚动偏移量
    private int scrollingOffset;
    // 中间标线
//    private Bitmap midBitmap;
    // 显示刻度值
    private boolean isScaleValue;
    private int shadowsColor;

    private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint markPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private float mTpDesiredWidth;

    /**
     * The left shadow.
     */
    private GradientDrawable mLeftShadow = null;

    /**
     * The right shadow.
     */
    private GradientDrawable mRightShadow = null;

    private Context context;

    /**
     * Shadow colors
     */
    private final int[] SHADOWS_COLORS = {0xFFFFFFFF, 0x70FFFFFF, 0x30FFFFFF,
            0x20FFFFFF, 0x10FFFFFF, 0x00FFFFFF};

    public RulerWheel(Context context) {
        this(context, null);
        this.context = context;
        init();
    }

    public RulerWheel(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
        init();
    }

    public RulerWheel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        scroller = new WheelHorizontalScroller(context, scrollingListener);
        this.context = context;
        // 获取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RulerWheel);

        // mask资源
//        int maskResId = mTypedArray.getResourceId(
//                R.styleable.RulerWheel_maskBg, R.mipmap.ruler_line);
//        midBitmap = BitmapFactory.decodeResource(getResources(), maskResId);

        // 刻度宽度
        int scaleWidth = mTypedArray.getDimensionPixelSize(
                R.styleable.RulerWheel_scaleWidth, 4);
        linePaint.setStrokeWidth(scaleWidth);

        // 刻度颜色
        mLineColorMax = mTypedArray.getColor(
                R.styleable.RulerWheel_lineColorMax, Color.BLACK);
        mLineColorMid = mTypedArray.getColor(
                R.styleable.RulerWheel_lineColorMid, Color.BLACK);
        mLineColorMin = mTypedArray.getColor(
                R.styleable.RulerWheel_lineColorMin, Color.BLACK);
        mTextColor = mTypedArray.getColor(
                R.styleable.RulerWheel_rulerTextColor, Color.BLACK);
        mTextSize = mTypedArray.getInteger(
                R.styleable.RulerWheel_rulerTextSize, 30);
        mCurrValue = mTypedArray.getInteger(R.styleable.RulerWheel_defValue, 0);
        mMaxValue = mTypedArray
                .getInteger(R.styleable.RulerWheel_maxValue, 100);
        shadowsColor = mTypedArray.getColor(R.styleable.RulerWheel_shadowsColor, Color.WHITE);
        hasDivideLine = mTypedArray.getBoolean(R.styleable.RulerWheel_hasDivideLine, false);
        // 刻度模式
        mModType = obtainMode(mTypedArray.getInteger(
                R.styleable.RulerWheel_mode, 0));
        // 线条间距
        mLineDivder = obtainLineDivder(mTypedArray.getDimensionPixelSize(
                R.styleable.RulerWheel_lineDivider, 0));
        // 显示刻度值
        isScaleValue = mTypedArray.getBoolean(
                R.styleable.RulerWheel_showScaleValue, false);
        textPaint.setTextSize(dip2px(mTextSize));
        textPaint.setColor(mTextColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(getLtExTypeface(context));
        mTpDesiredWidth = Layout.getDesiredWidth("0", textPaint);
        mTypedArray.recycle();
        init();
    }


    /**
     * LTEX字体风格
     *
     * @return
     */
    public static Typeface getLtExTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(),
                "fonts/ltex.otf");
    }

    public int dip2px(float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void init() {
        for (int i = 0; i < SHADOWS_COLORS.length; i++) {
            SHADOWS_COLORS[i] = SHADOWS_COLORS[i] & shadowsColor;
        }
        this.mLeftShadow = new GradientDrawable(Orientation.LEFT_RIGHT,
                SHADOWS_COLORS);
        this.mRightShadow = new GradientDrawable(Orientation.RIGHT_LEFT,
                SHADOWS_COLORS);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mLeftShadow.setBounds(0, 0, getWidth() / 3, getHeight());
        mLeftShadow.draw(canvas);

        mRightShadow.setBounds(getWidth() - getWidth() / 3, 0, getWidth(),
                getHeight());
        mRightShadow.draw(canvas);
    }

    /**
     * 刻度模式
     *
     * @param mode
     * @return
     */
    private int obtainMode(int mode) {
        if (mode == 1) {
            return MOD_TYPE_HALF;
        }
        return MOD_TYPE_SCALE;
    }

    /**
     * 获得线间距
     *
     * @param lineDivder
     * @return
     */
    private int obtainLineDivder(int lineDivder) {
        if (0 == lineDivder) {
            if (mModType == MOD_TYPE_HALF) {
                mLineDivder = 80;
            } else {
                mLineDivder = 20;
            }
            return mLineDivder;
        }

        return lineDivder;
    }

    // Scrolling listener
    WheelHorizontalScroller.ScrollingListener scrollingListener =
            new WheelHorizontalScroller.ScrollingListener() {
                @Override
                public void onStarted() {
                    isScrollingPerformed = true;
                    notifyScrollingListenersAboutStart();
                }

                @Override
                public void onScroll(int distance) {
                    doScroll(distance);
                }

                @Override
                public void onFinished() {
                    if (thatExceed()) {
                        return;
                    }
                    if (isScrollingPerformed) {
                        notifyScrollingListenersAboutEnd();
                        isScrollingPerformed = false;
                    }
                    scrollingOffset = 0;
                    invalidate();
                }

                @Override
                public void onJustify() {
                    if (thatExceed()) {
                        return;
                    }
                    if (Math.abs(scrollingOffset) > WheelHorizontalScroller.MIN_DELTA_FOR_SCROLLING) {
                        if (scrollingOffset < -mLineDivder / 2) {
                            scroller.scroll(mLineDivder + scrollingOffset, 0);
                        } else if (scrollingOffset > mLineDivder / 2) {
                            scroller.scroll(scrollingOffset - mLineDivder, 0);
                        } else {
                            scroller.scroll(scrollingOffset, 0);
                        }
                    }
                }
            };

    /**
     * 越界回滚
     *
     * @return
     */
    private boolean thatExceed() {
        int outRange = 0;
        if (mCurrValue < 0) {
            outRange = mCurrValue * mLineDivder;
        } else if (mCurrValue > mMaxValue) {
            outRange = (mCurrValue - mMaxValue) * mLineDivder;
        }
        if (0 != outRange) {
            scrollingOffset = 0;
            scroller.scroll(-outRange, 100);
            return true;
        }
        return false;
    }

    private int ratio = 10;

    public void seRatio(int ratio) {
        this.ratio = ratio;
    }

    public int getRatio() {
        return ratio;
    }

    public void setValue(float current, float maxValue) {
        if (current < 0) {
            current = 0;
        }
        if (maxValue < 0) {
            maxValue = 100;
        }
        this.mCurrValue = (int) (current * ratio);
        this.mMaxValue = (int) (maxValue * ratio);
        invalidate();
    }

    /**
     * 获取当前值
     *
     * @return
     */
    public int getValue() {
        return Math.min(Math.max(0, mCurrValue), mMaxValue);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getDefaultSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
//        int heightSize = midBitmap.getHeight() + getPaddingTop()
//                + getPaddingBottom() + dip2px(20);
        int heightSize = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w == 0 || h == 0)
            return;
        int rHeight = h - getPaddingTop() - getPaddingBottom();
        mLineHeighMax = (int)(rHeight / 3);
        mLineHeighMid = (int)(rHeight / 3);
        mLineHeighMin = (int)(rHeight / 4.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mLineHeighMin == 0) {
            return;
        }
        int rWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        // int rHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int rHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int ry = getPaddingTop() + (rHeight - mLineHeighMax) / 2;
        drawDivideline(canvas, rWidth, rHeight, ry);
        drawScaleLine(canvas, rWidth, rHeight, ry);
        drawMiddleLine(canvas, rWidth, rHeight, ry);
    }

    private void doScroll(int delta) {
        scrollingOffset += delta;
        int offsetCount = scrollingOffset / mLineDivder;
        if (0 != offsetCount) {
            // 显示在范围内
            int oldValue = Math.min(Math.max(0, mCurrValue), mMaxValue);
            mCurrValue -= offsetCount;
            scrollingOffset -= offsetCount * mLineDivder;
            if (null != onWheelListener) {
                onWheelListener.onChanged(this, oldValue,
                        Math.min(Math.max(0, mCurrValue), mMaxValue));
            }
        }
        invalidate();
    }

    /**
     * @param canvas
     * @param rWidth  显示宽度
     * @param rHeight 显示高度
     * @param ry      线y坐标
     */
    private void drawScaleLine(Canvas canvas, int rWidth, int rHeight, int ry) {
        // 根据间隔计算当前一半宽度的个数+偏移2个
        final int halfCount = (int) Math.ceil(rWidth / 2f / mLineDivder) + 2;
        final int distanceX = scrollingOffset;
        final int currValue = mCurrValue;
        int value;
        float xPosition;
        for (int i = 0; i < halfCount; i++) {
            // right
            xPosition = rWidth / 2f + i * mLineDivder + distanceX;
            value = currValue + i;
            if (xPosition <= rWidth && value >= 0 && value <= mMaxValue) {
                if (value % mModType == 0) {
                    // 这里是一半模式下面
                    if (mModType == MOD_TYPE_HALF) {
                        linePaint.setColor(mLineColorMax);
                        canvas.drawLine(xPosition, ry, xPosition, ry
                                + mLineHeighMax, linePaint);
                        if (isScaleValue) {
                            canvas.drawText(String.valueOf(value / 2),
                                    xPosition, rHeight - mTpDesiredWidth,
                                    textPaint);
                            canvas.drawText(String.valueOf(value / 2),
                                    xPosition, rHeight, textPaint);
                        }
                        // 这里是全模式下面
                    } else if (mModType == MOD_TYPE_SCALE) {
                        if (value % (MOD_TYPE_SCALE) == 0) {
                            linePaint.setColor(mLineColorMax);
                            canvas.drawLine(xPosition, ry, xPosition, ry
                                    + mLineHeighMax, linePaint);
                            if (isScaleValue && value % ratio == 0) {
                                canvas.drawText(String.valueOf(value / ratio),
                                        xPosition, rHeight - mTpDesiredWidth + dip2px(6),
                                        textPaint);
//								canvas.drawText(
//										String.valueOf((((float) value) / 10)),
//										xPosition, rHeight - mTpDesiredWidth
//												- ry - mLineHeighMax, textPaint);
                            }
                        } else {
                            linePaint.setColor(mLineColorMid);
                            canvas.drawLine(xPosition, ry, xPosition, ry
                                    + mLineHeighMid, linePaint);
                        }
                    }
                } else {
                    linePaint.setColor(mLineColorMin);
                    canvas.drawLine(xPosition, ry, xPosition, ry + mLineHeighMin, linePaint);
                    // 最小的线居中显示
//                    canvas.drawLine(xPosition, ry
//                                    + ((mLineHeighMax - mLineHeighMin) / 2), xPosition,
//                            ry + ((mLineHeighMax - mLineHeighMin) / 2)
//                                    + mLineHeighMin, linePaint);
                }
            }

            // left
            xPosition = rWidth / 2f - i * mLineDivder + distanceX;
            value = currValue - i;
            if (xPosition > getPaddingLeft() && value >= 0
                    && value <= mMaxValue) {
                if (value % mModType == 0) {
                    if (mModType == MOD_TYPE_HALF) {
                        linePaint.setColor(mLineColorMax);
                        canvas.drawLine(xPosition, ry, xPosition, ry
                                + mLineHeighMax, linePaint);
                        if (isScaleValue) {
                            canvas.drawText(
                                    String.valueOf((float) (value / 2)),
                                    xPosition, rHeight - mTpDesiredWidth,
                                    textPaint);

                        }
                    } else if (mModType == MOD_TYPE_SCALE) {
                        if (value % (MOD_TYPE_SCALE) == 0) {
                            linePaint.setColor(mLineColorMax);
                            canvas.drawLine(xPosition, ry, xPosition, ry
                                    + mLineHeighMax, linePaint);
                            if (isScaleValue && value % ratio == 0) {
                                canvas.drawText(String.valueOf(value / ratio),
                                        xPosition, rHeight - mTpDesiredWidth + dip2px(6),
                                        textPaint);
//								canvas.drawText(
//										String.valueOf((((float) value) / 10)),
//										xPosition, rHeight - mTpDesiredWidth
//												- ry - mLineHeighMax, textPaint);
                            }
                        } else {
                            linePaint.setColor(mLineColorMid);
                            canvas.drawLine(xPosition, ry, xPosition, ry
                                    + mLineHeighMid, linePaint);
                        }
                    }
                } else {
                    linePaint.setColor(mLineColorMin);
                    canvas.drawLine(xPosition, ry, xPosition, ry + mLineHeighMin, linePaint);
                    // 最小的线居中显示
//                    canvas.drawLine(xPosition, ry
//                                    + ((mLineHeighMax - mLineHeighMin) / 2), xPosition,
//                            ry + ((mLineHeighMax - mLineHeighMin) / 2)
//                                    + mLineHeighMin, linePaint);
                }
            }
        }
    }

    /**
     * 绘制中间线
     *
     * @param canvas
     * @param rHeight
     * @param rWidth
     * @param ry
     */
    @SuppressWarnings("unused")
    private void drawMiddleLine(Canvas canvas, int rWidth, int rHeight, int ry) {
        markPaint.setColor(mLineColorMid);
        markPaint.setStrokeWidth(4);
        canvas.drawLine(rWidth / 2, ry, rWidth / 2, ry + mLineHeighMax, markPaint);
    }

    private boolean hasDivideLine = false;

    private void drawDivideline(Canvas canvas, int rWidth, int rHeight, int ry) {
        if (hasDivideLine) {
            linePaint.setColor(mLineColorMax);
            canvas.drawLine(0, ry / 2, rWidth, ry / 2, linePaint);
        }
    }

    private float mDownFocusX;
    private float mDownFocusY;
    private boolean isDisallowIntercept;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownFocusX = event.getX();
                mDownFocusY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isDisallowIntercept
                        && Math.abs(event.getY() - mDownFocusY) < Math.abs(event
                        .getX() - mDownFocusX)) {
                    isDisallowIntercept = true;
                    if (getParent() != null) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                isDisallowIntercept = false;
                break;
        }
        return scroller.onTouchEvent(event);
    }

    private OnWheelScrollListener onWheelListener;

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    public void setScrollingListener(OnWheelScrollListener listener) {
        onWheelListener = listener;
    }

    /**
     * Removes wheel changing listener
     */
    public void removeScrollingListener() {
        onWheelListener = null;
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected void notifyScrollingListeners(int oldValue, int newValue) {
        onWheelListener.onChanged(this, oldValue, newValue);
    }

    private void notifyScrollingListenersAboutStart() {
        if (null != onWheelListener) {
            onWheelListener.onScrollingStarted(this);
        }
    }

    private void notifyScrollingListenersAboutEnd() {
        if (null != onWheelListener) {
            onWheelListener.onScrollingFinished(this);
        }
    }

    public interface OnWheelScrollListener {
        /**
         * Callback method to be invoked when current item changed
         *
         * @param wheel    the wheel view whose state has changed
         * @param oldValue the old value of current item
         * @param newValue the new value of current item
         */
        void onChanged(RulerWheel wheel, int oldValue, int newValue);

        /**
         * Callback method to be invoked when scrolling started.
         *
         * @param wheel the wheel view whose state has changed.
         */
        void onScrollingStarted(RulerWheel wheel);

        /**
         * Callback method to be invoked when scrolling ended.
         *
         * @param wheel the wheel view whose state has changed.
         */
        void onScrollingFinished(RulerWheel wheel);
    }

}