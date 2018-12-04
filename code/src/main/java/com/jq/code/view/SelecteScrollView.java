package com.jq.code.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;


import com.jq.code.R;
import com.jq.code.view.ruler.WheelHorizontalScroller;
import com.jq.code.view.text.CustomTextView;

import java.util.ArrayList;

/**
 * Created by zz date 2015/5/15
 */
public class SelecteScrollView extends View {
    private String TAG = "SelecteScrollView" ;
    // 默认刻度模式
    // 字体大小
    private int mTextSize = 20;
    // 字体颜色
    private int mTextColor = Color.BLACK;
    // 当前值
    private int mCurrIndex = 0;
    // 显示最大个数
    private int mMaxCount;
    // text宽度
    private int mTextWidth;

    private int ratio = 1;
    // 滚动器
    private WheelHorizontalScroller horizontalScroller;
    // 是否执行滚动
    private boolean isScrollingPerformed;
    // 滚动偏移量
    private int scrollingOffset;
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    // 显示text
    private ArrayList<String> textList;
    // shadow高度
    private int mShadowHeight;
    // 字体位置
    private int mGravity;
    private static final int CENTER = 0;
    private static final int TOP = 1;
    private static final int BUTTOM = 2;

    /**
     * The left shadow.
     */
    private GradientDrawable mLeftShadow = null;

    /**
     * The right shadow.
     */
    private GradientDrawable mRightShadow = null;

    private int shadowsColor;
    /**
     * Shadow colors
     */
    private final int[] SHADOWS_COLORS = { 0xFFFFFFFF, 0xe0FFFFFF, 0xa0FFFFFF,
            0x80FFFFFF, 0x50FFFFFF, 0x10FFFFFF, 0x00FFFFFF };

    public SelecteScrollView(Context context) {
        this(context, null);
        init();
    }

    public SelecteScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public SelecteScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        horizontalScroller = new WheelHorizontalScroller(context, scrollingListener);
        init();
        // 获取自定义属性和默认值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MidScrollView);
        mGravity = mTypedArray.getInt(R.styleable.MidScrollView_mGravity, 0);
        mShadowHeight = mTypedArray.getInteger(
                R.styleable.MidScrollView_mShadowsHeight,
                LayoutParams.MATCH_PARENT);
        mTextColor = mTypedArray.getColor(R.styleable.MidScrollView_mtextcolor,
                Color.BLACK);
        mTextSize = mTypedArray.getInteger(R.styleable.MidScrollView_mtextSize,
                20);
        textPaint.setTextSize(dip2px(context, mTextSize));
        textPaint.setColor(mTextColor);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(CustomTextView.getExTypeface(context));
        shadowsColor = mTypedArray.getInt(
                R.styleable.MidScrollView_mShadowsColor, Color.WHITE);
        for (int i = 0; i < SHADOWS_COLORS.length; i++) {
            SHADOWS_COLORS[i] = SHADOWS_COLORS[i] & shadowsColor;
        }
        ratio = mTypedArray.getInteger(R.styleable.MidScrollView_mtextRatio, 4);
        mTypedArray.recycle();
    }

    public void setShadowsColor(int shadowsColor) {
        this.shadowsColor = shadowsColor;
        for (int i = 0; i < SHADOWS_COLORS.length; i++) {
            SHADOWS_COLORS[i] = SHADOWS_COLORS[i] & this.shadowsColor;
        }
//        init();
        invalidate();
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void init() {
        this.mLeftShadow = new GradientDrawable(Orientation.LEFT_RIGHT,SHADOWS_COLORS);
        this.mRightShadow = new GradientDrawable(Orientation.RIGHT_LEFT,SHADOWS_COLORS);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = getDefaultSize(getSuggestedMinimumWidth(),
                widthMeasureSpec);
        int heightSize = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        mTextWidth = widthSize / ratio;
        setMeasuredDimension(widthSize, heightSize);
    }

    // Scrolling listener
    WheelHorizontalScroller.ScrollingListener scrollingListener = new WheelHorizontalScroller.ScrollingListener() {
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
//            if () {
//                notifyScrollingListenersAboutEnd(mCurrIndex);
////                return;
//            }
            thatExceed() ;
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd(mCurrIndex);
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
                if (scrollingOffset < -mTextWidth / 2) {
                    horizontalScroller.scroll(mTextWidth + scrollingOffset, 0);
                } else if (scrollingOffset > mTextWidth / 2) {
                    horizontalScroller.scroll(scrollingOffset - mTextWidth, 0);
                } else {
                    horizontalScroller.scroll(scrollingOffset, 0);
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
        Log.e(TAG,"thatExceed  + 越界回滚方法") ;
        int outRange = 0;
        if (mCurrIndex < 0) {
            mCurrIndex = 0 ;
            outRange = mCurrIndex * mTextWidth;
        } else if (mCurrIndex > mMaxCount) {
            mCurrIndex = mMaxCount ;
            outRange = (mCurrIndex - mMaxCount) * mTextWidth;
        }
        if (0 != outRange) {
            scrollingOffset = 0;
            horizontalScroller.scroll(-outRange, 100);
            return true;
        }
        return false;
    }

    public void scrollTo(int index) {
        mCurrIndex = index;
        if (index < 0) {
            mCurrIndex = 0;
        } else if (index > mMaxCount) {
            mCurrIndex = mMaxCount;
        }
        invalidate();
    }

    /**
     * 获取当前xiaob
     *
     * @return
     */
    public int getCurrentIndex() {
        return Math.min(Math.max(0, mCurrIndex), mMaxCount);
    }

    private void doScroll(int delta) {
        scrollingOffset += delta;
        int offsetCount = scrollingOffset / mTextWidth;
        if (0 != offsetCount) {
            // 显示在范围内
            mCurrIndex -= offsetCount;
            scrollingOffset -= offsetCount * mTextWidth;
            if (null != onWheelListener) {
                onWheelListener.onChanged(this,
                        Math.min(Math.max(0, mCurrIndex), mMaxCount));
            }
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("SelecteScrollView","onDraw") ;
        int rWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        // int rHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        int rHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        drawText(canvas, rWidth, rHeight);
        if (mShadowHeight == LayoutParams.MATCH_PARENT) {
            mShadowHeight = getHeight();
        }
        Log.e("SelecteScrollView","mShadowHeight = " + mShadowHeight) ;
        mLeftShadow.setBounds(0, 0, getWidth() / 2, mShadowHeight);
        mLeftShadow.draw(canvas);
        mRightShadow.setBounds(getWidth() - getWidth() / 2, 0, getWidth(), mShadowHeight);
        mRightShadow.draw(canvas);
    }

    /**
     * 设置要显示的文字
     *
     * @param textList
     */
    public void setTexts(ArrayList<String> textList) {
        this.textList = textList;
        if (textList == null) {
            mMaxCount = 0;
        } else {
            mMaxCount = textList.size() - 1;
        }
    }

    /**
     * @param canvas
     * @param rWidth
     *            显示宽度
     * @param rHeight
     *            显示高度
     */
    private void drawText(Canvas canvas, int rWidth, int rHeight) {
        final int distanceX = scrollingOffset;
        final int currValue = mCurrIndex;
        int index;
        float xPosition;
        float yPosition;
        FontMetrics fm = textPaint.getFontMetrics();
        if (mGravity == TOP) {
            yPosition = Math.abs(fm.ascent);
        } else if (mGravity == BUTTOM) {
            yPosition = rHeight;
        } else if (mGravity == CENTER) {
            yPosition = rHeight / 2 + Math.abs(fm.ascent / 2) - 2;
        } else {
            yPosition = rHeight / 2 + Math.abs(fm.ascent / 2) - 2;
        }
        for (int i = 0; i <= mMaxCount; i++) {
            // right
            xPosition = rWidth / 2f + i * mTextWidth + distanceX;
            index = currValue + i;
            if (xPosition <= rWidth && index >= 0 && index <= mMaxCount) {
                canvas.drawText(textList == null ? "" : textList.get(index),
                        xPosition, yPosition, textPaint);
            }
            // left
            xPosition = rWidth / 2f - i * mTextWidth + distanceX;
            index = currValue - i;
            if (xPosition > getPaddingLeft() && index >= 0
                    && index <= mMaxCount) {
                canvas.drawText(textList == null ? "" : textList.get(index),
                        xPosition, yPosition, textPaint);
            }
        }
    }

    private float mDownFocusX;
    private float mDownFocusY;
    private boolean isDisallowIntercept;

    private long timelong ;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                timelong = System.currentTimeMillis() ;
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
                long tempLongTime = System.currentTimeMillis() ;
                if(tempLongTime - timelong < 80){
                    performClick() ;
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                isDisallowIntercept = false;
                break;
        }
        return horizontalScroller.onTouchEvent(event);
    }

    private OnScrollingListener onWheelListener;

    /**
     * Adds wheel changing listener
     *
     * @param listener
     *            the listener
     */
    public void setOnScrollingListener(OnScrollingListener listener) {
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
     * @param index
     *            the new wheel value
     */
    protected void notifyScrollingListeners(int index) {
        onWheelListener.onChanged(this, index);
    }

    private void notifyScrollingListenersAboutStart() {
        if (null != onWheelListener) {
            onWheelListener.onScrollingStarted(this);
        }
    }

    private void notifyScrollingListenersAboutEnd(int index) {
        if (null != onWheelListener) {
            onWheelListener.onScrollingFinished(this, index);
        }
    }

    public interface OnScrollingListener {
        /**
         * Callback method to be invoked when current item changed
         *
         * @param wheel
         *            the wheel view whose state has changed
         * @param index
         *            the old value of current item
         */
        void onChanged(SelecteScrollView wheel, int index);

        /**
         * Callback method to be invoked when scrolling started.
         *
         * @param wheel
         *            the wheel view whose state has changed.
         */
        void onScrollingStarted(SelecteScrollView wheel);

        /**
         * Callback method to be invoked when scrolling ended.
         *
         * @param wheel
         *            the wheel view whose state has changed.
         */
        void onScrollingFinished(SelecteScrollView wheel, int index);
    }

}
