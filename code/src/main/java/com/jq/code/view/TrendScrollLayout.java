package com.jq.code.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.jq.btlib.util.LogUtil;
import com.jq.code.model.Constant;

/**
 * 趋势修改图
 *
 * @author xlj
 * @version 1.0 2016/7/05
 */
public class TrendScrollLayout extends FrameLayout {
    public static final String TAG = "TrendScrollLayout" ;
    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */

    public TrendScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mScroller = new Scroller(context);
    }

    public TrendScrollLayout(Context context) {
        super(context);
    }

    private float xDown;  
  
    private float xMove;  
  
    private Scroller mScroller;

    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 回收VelocityTracker对象。
     */
    private void recycleVelocityTracker() {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
    /**
     * 创建VelocityTracker对象，并将触摸content界面的滑动事件加入到VelocityTracker当中。
     *
     * @param event
     *            content界面的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在content界面滑动的速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        createVelocityTracker(event);
        int rightBorder =  getWidth() ;
        int leftBorder  = - getWidth() ;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG,"onInterceptTouchEvent ACTION_DOWN");
                timelong = System.currentTimeMillis() ;
                xDown = event.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG,"onInterceptTouchEvent ACTION_MOVE");
                xMove = event.getRawX();
                int distanceX = (int) (xDown  - xMove);
                if(getScrollX() > rightBorder){
                    scrollTo(rightBorder, 0);
                    return true;
                }
                if(getScrollX() < leftBorder){
                    scrollTo(leftBorder, 0);
                    return true;
                }
                if(Math.abs(distanceX) > Constant.SCROLL_RANGE){
                    scrollBy(distanceX, 0);
                }
                xDown = xMove ;
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG,"onInterceptTouchEvent ACTION_UP");
                // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
                if(shouldLeftScroll() && !invalidTag){
                    nextPageScroll();
                }else if(shouldRightScroll()){
                    upPageScroll();
                }else {
                    if(invalidTag && pageListener != null && shouldLeftScroll()){
                        pageListener.invalidScrolly();
                    }
                    mScroller.startScroll(getScrollX(), 0,-getScrollX() , 0);
                }
                invalidate();
                recycleVelocityTracker();
                long tempLongTime = System.currentTimeMillis() ;
                if(tempLongTime - timelong < Constant.SCROLL_TOUCH_MIN_TIME){
                    performClick() ;
                }
                break;
        }
        return false;
    }

    private long timelong ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                LogUtil.e(TAG,"onTouchEvent ACTION_DOWN");

                break;
            case MotionEvent.ACTION_MOVE:
                LogUtil.e(TAG,"onTouchEvent ACTION_MOVE");

                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e(TAG,"onTouchEvent ACTION_UP");
                break;
        }
        return false ;
    }
    public boolean shouldLeftScroll(){
        boolean result = false ;
        if((getScrollX() > 0 && getScrollVelocity() > Constant.SNAP_VELOCITY ) ||(getScrollX() > 0 && getScrollX() > getWidth()/2) ){
            result = true ;
        }
        return result ;
    }
    public boolean shouldRightScroll(){
        boolean result = false ;
        if((getScrollX() < 0 && getScrollVelocity() > Constant.SNAP_VELOCITY ) ||(getScrollX() < 0 && getScrollX() < -getWidth()/2) ){
            result = true ;
        }
        return result ;
    }
    public void nextPageScroll(){
        mScroller.startScroll(getScrollX(), 0,getWidth() , 0);
        if(pageListener != null){
            pageListener.nextPage();
        }
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void run() {
                setScrollX(-getWidth());
                mScroller.startScroll(getScrollX(), 0, getWidth() , 0);
            }
        },400) ;
    }
    public void upPageScroll(){
        mScroller.startScroll(getScrollX(), 0, -getWidth() ,0);
        if(pageListener != null){
            pageListener.upPage();
        }
        new Handler().postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void run() {
                setScrollX(getWidth());
                mScroller.startScroll(getScrollX(), 0, -getWidth() , 0);
            }
        },400) ;
    }
    @Override
    public void computeScroll() {
        // 第三步，重写computeScroll()方法，并在其内部完成平滑滚动的逻辑
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
    private OnPageListener pageListener ;

    public void setOnPageListener(OnPageListener pageListener) {
        this.pageListener = pageListener;
    }
    public boolean invalidTag  ;

    public void setInvalidTag(boolean invalidTag) {
        this.invalidTag = invalidTag;
    }

    public interface OnPageListener{
        void upPage() ;
        void nextPage() ;
        void invalidScrolly() ;
    }
}
