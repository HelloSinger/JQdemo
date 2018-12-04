package com.jq.code.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.LinearLayout;

import com.jq.btlib.util.LogUtil;

/**
 * 左右滚动视图
 *
 * @author xlj
 * @version 1.0 2016/7/05
 */
public class LeftRightScrollLayout extends LinearLayout {
    public static final String TAG = "LeftRightScrollLayout" ;
    /**
     * 滚动显示和隐藏menu时，手指滑动需要达到的速度。
     */
    public static final int SNAP_VELOCITY = 200;
    public static  int SCROLL_LEAST_DISTANCE = 50;

    public LeftRightScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        SCROLL_LEAST_DISTANCE = (int) (getResources().getDisplayMetrics().density*SCROLL_LEAST_DISTANCE);
    }



    public LeftRightScrollLayout(Context context) {
        super(context);
    }

    private float xDown;  
  
    private float xMove;

    private float xUp;
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
    private long timelong ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	// TODO Auto-generated method stub
        createVelocityTracker(event);
         switch (event.getAction()) {
         case MotionEvent.ACTION_DOWN:
             LogUtil.e(TAG,"onTouchEvent ACTION_DOWN");
             timelong = System.currentTimeMillis() ;
             // 手指按下时，记录按下时的横坐标  
             xDown = event.getRawX();
             break;
         case MotionEvent.ACTION_MOVE:
             LogUtil.e(TAG,"onTouchEvent ACTION_MOVE");
             // 手指移动时，对比按下时的横坐标，计算出移动的距离，来调整menu的leftMargin值，从而显示和隐藏menu  
             break;
         case MotionEvent.ACTION_UP:
             // 手指抬起时，进行判断当前手势的意图，从而决定是滚动到menu界面，还是滚动到content界面
             LogUtil.e(TAG,"onTouchEvent ACTION_UP");
             long tempLongTime = System.currentTimeMillis() ;
             if(tempLongTime - timelong < 80){
                 performClick() ;
                 return true ;
             }
             xUp = event.getRawX() ;
             int distanceX = (int) (xUp  - xDown);
             if(distanceX < 0 && isLeft){
                 if(Math.abs(distanceX)> SCROLL_LEAST_DISTANCE || getScrollVelocity() > SNAP_VELOCITY){
                     if(leftRightListener != null){
                         leftRightListener.onRightScroll();
                         isLeft = !isLeft ;
                     }
                 }
             }
             if(distanceX >0 && !isLeft){
                 if(Math.abs(distanceX)> SCROLL_LEAST_DISTANCE || getScrollVelocity() > SNAP_VELOCITY){
                     if(leftRightListener != null){
                         leftRightListener.onLeftScroll();
                         isLeft = !isLeft ;
                     }
                 }
             }
             recycleVelocityTracker();
             break;  
         }  
         return true;  
    }


    private boolean isLeft = true ;

    public void setIsLeft(boolean left) {
        isLeft = left;
    }

    private OnLeftRightListener leftRightListener ;

    public void setLeftRightListener(OnLeftRightListener leftRightListener) {
        this.leftRightListener = leftRightListener;
    }

    public interface OnLeftRightListener{
        void onLeftScroll() ;
        void onRightScroll() ;
    }
}
