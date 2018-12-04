package com.jq.code.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public abstract class LoadingRenderer {
  private static final long ANIMATION_DURATION = 1333;

  private static final float DEFAULT_SIZE = 56.0f;
  private static final float DEFAULT_CENTER_RADIUS = 12.5f;
  private static final float DEFAULT_STROKE_WIDTH = 2.5f;

  protected float mWidth;
  protected float mHeight;
  protected float mStrokeWidth;
  protected float mCenterRadius;

  private long mDuration;
  private Drawable.Callback mCallback;
  private ValueAnimator mRenderAnimator;
  private  ImageView imageView ;
  public LoadingRenderer(Context context, ImageView imageView) {
    this.imageView = imageView ;
    setupDefaultParams(context);
    setupAnimators();
  }

  public abstract void draw(Canvas canvas, Rect bounds);
  public abstract void computeRender(float renderProgress);
  public abstract void setAlpha(int alpha);
  public abstract void setColorFilter(ColorFilter cf);
  public abstract void reset();

  public void start() {
    if(imageView != null){
      imageView.setVisibility(View.VISIBLE);
    }
    reset();
    setDuration(mDuration);
    mRenderAnimator.start();
  }

  public void stop() {
    if(imageView != null){
      imageView.setVisibility(View.GONE);
    }
    mRenderAnimator.cancel();
  }

  public boolean isRunning() {
    return mRenderAnimator.isRunning();
  }

  public void setCallback(Drawable.Callback callback) {
    this.mCallback = callback;
  }

  protected void invalidateSelf() {
    mCallback.invalidateDrawable(null);
  }

  private void setupDefaultParams(Context context) {
    final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    final float screenDensity = metrics.density;

    mWidth = DEFAULT_SIZE * screenDensity;
    mHeight = DEFAULT_SIZE * screenDensity;
    mStrokeWidth = DEFAULT_STROKE_WIDTH * screenDensity;
    mCenterRadius = DEFAULT_CENTER_RADIUS * screenDensity;

    mDuration = ANIMATION_DURATION;
  }

  private void setupAnimators() {
    mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    mRenderAnimator.setRepeatCount(Animation.INFINITE);
    mRenderAnimator.setRepeatMode(Animation.RESTART);
    //fuck you! the default interpolator is AccelerateDecelerateInterpolator
    mRenderAnimator.setInterpolator(new LinearInterpolator());
    mRenderAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        computeRender((Float) animation.getAnimatedValue());
        invalidateSelf();
      }
    });
  }

  protected void addRenderListener(Animator.AnimatorListener animatorListener) {
    mRenderAnimator.addListener(animatorListener);
  }

  public void setCenterRadius(float centerRadius) {
    mCenterRadius = centerRadius;
  }

  public float getCenterRadius() {
    return mCenterRadius;
  }

  public void setStrokeWidth(float strokeWidth) {
    mStrokeWidth = strokeWidth;
  }

  public float getStrokeWidth() {
    return mStrokeWidth;
  }

  public float getWidth() {
    return mWidth;
  }

  public void setWidth(float width) {
    this.mWidth = width;
  }

  public float getHeight() {
    return mHeight;
  }

  public void setHeight(float height) {
    this.mHeight = height;
  }

  public long getDuration() {
    return mDuration;
  }

  public void setDuration(long duration) {
    this.mDuration = duration;
    mRenderAnimator.setDuration(mDuration);
  }
}
