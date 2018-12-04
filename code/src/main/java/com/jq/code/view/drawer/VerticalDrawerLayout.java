package com.jq.code.view.drawer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.ViewUtil;

/**
 * Created by Administrator on 2016/10/18.
 */

public class VerticalDrawerLayout extends RelativeLayout {

    private static final String TAG = VerticalDrawerLayout.class.getSimpleName();
    private View firstView;
    private View secondView;
    private int maxDistance;
    private int minDistance = 0;
    private float dy, my;
    private boolean mIsBeingDragged;
    private float y;
    private float firstViewScale = 0.25f;
    private float curScale = 1;
    public static final int ANIMA_DURATION = 300;
    public static final int overDp = 18;
    private int minSliderDistance = 40;
    private boolean mIsDragRange;
    private float screenHeight;

    public VerticalDrawerLayout(Context context) {
        super(context);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenHeight = ScreenUtils.getScreenHeight(context);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (getChildCount() != 2) {
            try {
                throw new NotTwoChildException("父控件只能添加两个子控件");
            } catch (NotTwoChildException e) {
                e.printStackTrace();
            }
        }
        if (firstView == null)
            firstView = getChildAt(0);
        if (secondView == null) {
            secondView = getChildAt(getChildCount() - 1);
        }
        minSliderDistance = ViewUtil.dip2px(getContext(), minSliderDistance);
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (firstView.getMeasuredHeight() != maxDistance && y != minDistance) {
            y = maxDistance = firstView.getMeasuredHeight();
            canTranslation = true;
        }else{
            maxDistance = firstView.getMeasuredHeight();
        }
        if(canTranslation){
            secondView.setTranslationY(y);
        }
        adjustLayout();
    }

    public void reLayout() {
        if (y == minDistance) {
            if(secondView instanceof RecyclerView){
                RecyclerView secondView = (RecyclerView) this.secondView;
                if(secondView.getChildCount() > 0){
                    secondView.scrollToPosition(0);
                }
            }
            y = maxDistance;
            smoothTo(secondView, y, ANIMA_DURATION);
            scrollUp(-1);

        }
    }

    class NotTwoChildException extends RuntimeException {
        public NotTwoChildException(String meg) {
            super(meg);
        }
    }

    private boolean canTranslation = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (!checkslideable()) return super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                my = dy = event.getY();
                canTranslation = true;
                mIsBeingDragged = false;
                mIsDragRange = !(y >= 0 && y <= maxDistance && dy < y);
                break;
            case MotionEvent.ACTION_MOVE:
                float distance = event.getY() - my;
                my = event.getY();
                float translationY = secondView.getTranslationY();
                if (distance < 0) {
                    mIsBeingDragged = translationY <= maxDistance && translationY > minDistance;
                }
                if (distance > 0) {
                    if (translationY < maxDistance && translationY >= minDistance) {
                        mIsBeingDragged = !canSecendViewScrollDown();
                    } else {
                        mIsBeingDragged = false;
                    }
                }
                if (Math.abs(event.getY() - dy) > 0 && y == maxDistance) {
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged && mIsDragRange) {
                    if ((y == minDistance || y == maxDistance) && Math.abs(event.getY() - dy) <= minSliderDistance) {
                        if (event.getY() - dy < 0) {
                            return false;
                        } else {
                            return super.dispatchTouchEvent(event);
                        }
                    } else {
                        y = translationY + distance;
                        if (y < minDistance) {
                            y = minDistance;
                            mIsBeingDragged = false;
                        }
                        if (y > maxDistance) {
                            y = maxDistance;
                            mIsBeingDragged = false;
                        }
                        curScale = (maxDistance - y) * firstViewScale / maxDistance + 1;
                        if (curScale < 1) {
                            curScale = 1;
                        }
                        if (curScale > (1 + firstViewScale)) {
                            curScale = (1 + firstViewScale);
                        }
//                        firstView.setScaleY(curScale);
//                        firstView.setScaleX(curScale);
                        secondView.setTranslationY(y);
                        if (scrollListener != null) {
                            float ratio = (secondView.getTranslationY() - minDistance) / (maxDistance - minDistance);
                            scrollListener.onScrolling(this, 1 - (ratio < 0 ? 0 : ratio));
                        }
                    }
                    return mIsBeingDragged;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                canTranslation = false;
                selfAdaption(event);
                if (mIsBeingDragged && mIsDragRange) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean checkslideable() {
        return (firstView.getMeasuredHeight() + secondView.getMeasuredHeight()) > screenHeight;
    }

    private void selfAdaption(MotionEvent event) {
        if (y == minDistance || y == maxDistance) return;
        float upy = event.getY() - dy;
        if (upy < 0 && y > 0) {
            if (Math.abs(upy) > maxDistance / 3) {
                y = minDistance;
//                scale(firstView, curScale, 1 + firstViewScale, ANIMA_DURATION);
                smoothTo(secondView, y, ANIMA_DURATION);
                scrollUp(1);
            } else {
                y = maxDistance;
//                scale(firstView, curScale, 1, ANIMA_DURATION);
                smoothTo(secondView, y, ANIMA_DURATION);
                scrollUp(-1);
            }
        }
        if (upy > 0 && y < maxDistance && !canSecendViewScrollDown()) {
            if (Math.abs(upy) > maxDistance / 3) {
                y = maxDistance;
//                scale(firstView, curScale, 1, ANIMA_DURATION);
                smoothTo(secondView, y, ANIMA_DURATION);
                scrollUp(-1);
            } else {
                y = minDistance;
//                scale(firstView, curScale, 1 + firstViewScale, ANIMA_DURATION);
                smoothTo(secondView, y, ANIMA_DURATION);
                scrollUp(1);
            }
        }
    }

    private void scrollUp(int dd) {
        if (scrollListener != null) {
            float ratio = (secondView.getTranslationY() - minDistance) / (maxDistance - minDistance);
            scrollListener.onScrollUp(this, (1 - ratio) * dd);
        }
    }

    private void adjustLayout() {
        if (secondView != null) {
            int bottom = secondView.getBottom();
            if ((bottom + maxDistance - overDp) <= screenHeight && y < maxDistance) {
                y = maxDistance;
//                scale(firstView, curScale, 1, ANIMA_DURATION);
                smoothTo(secondView, y, ANIMA_DURATION);
                scrollUp(-1);
            }
        }
    }

    /**
     * 判断View是否可以下拉
     *
     * @return canChildScrollDown
     */
    public boolean canSecendViewScrollUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (secondView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) secondView;
                return absListView.getChildCount() > 0 && absListView.getAdapter() != null
                        && (absListView.getLastVisiblePosition() < absListView.getAdapter().getCount() - 1 || absListView.getChildAt(absListView.getChildCount() - 1)
                        .getBottom() < absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(secondView, 1) || secondView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(secondView, 1);
        }
    }

    /**
     * 判断View是否可以上拉
     *
     * @return canChildScrollUp
     */
    public boolean canSecendViewScrollDown() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (secondView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) secondView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(secondView, -1) || secondView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(secondView, -1);
        }
    }


    public void scale(View view, float from, float to, long duration) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        ObjectAnimator xScaleAnim = ObjectAnimator.ofFloat(view, "scaleX", from, to);
        ObjectAnimator yScaleAnim = ObjectAnimator.ofFloat(view, "scaleY", from, to);
        AnimatorSet animSet = new AnimatorSet();
        animSet.play(xScaleAnim).with(yScaleAnim);
        animSet.setDuration(duration);
        animSet.start();
    }

    public void smoothTo(View view, float y, long duration) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
        android.animation.ObjectAnimator.ofFloat(view, "translationY", y).setDuration(duration).start();
    }

    private OnScrollListener scrollListener;

    public void setOnScrollListener(OnScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    public interface OnScrollListener {
        void onScrolling(View view, float offsetRatio);

        void onScrollUp(View view, float offsetRatio);
    }
}
