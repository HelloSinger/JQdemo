package com.jq.code.view.button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jq.code.R;


public class SwitchButtonPregnant extends View implements OnTouchListener {

    private Bitmap bg_on, bg_off, slipper_btn;
    /**
     * 按下时的x和当前的x
     */
    private float downX, nowX;


    /**
     * 当前的状态
     */
    private boolean nowStatus = false;

    /**
     * 监听接口
     */
    private OnChangedListener listener;

    public SwitchButtonPregnant(Context context) {
        super(context);
        init();
    }

    public SwitchButtonPregnant(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwitchButtonPregnant(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        // 载入图片资源
        bg_on = BitmapFactory.decodeResource(getResources(), R.mipmap.remind_switch_on_pregnant);
        bg_off = BitmapFactory.decodeResource(getResources(),R.mipmap.remind_switch_off);
        slipper_btn = BitmapFactory.decodeResource(getResources(), R.mipmap.remind_switch_thumb);
        setOnTouchListener(this);
    }

    public void setOnBgRes(int resId) {
        bg_on = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setOfBgRes(int resId) {
        bg_off = BitmapFactory.decodeResource(getResources(), resId);
    }

    public void setBtBgRes(int resId) {
        slipper_btn = BitmapFactory.decodeResource(getResources(), resId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = bg_on.getWidth();
        int heightSize = bg_on.getHeight();
        int width = slipper_btn.getWidth();
        int height = slipper_btn.getHeight();
        setMeasuredDimension(Math.max(widthSize, width), Math.max(heightSize, height));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        float x = 0;
        float top = bg_on.getHeight() >= slipper_btn.getHeight() ? 0 : (slipper_btn.getHeight() / 2f - bg_on.getHeight() / 2f);
        canvas.drawBitmap(bg_off, 0, top, paint);// 画出关闭时的背景
        if (nowStatus) {// 根据当前的状态设置滑块的x值
            x = bg_on.getWidth() - slipper_btn.getWidth();
            canvas.drawBitmap(bg_on, 0, top, paint);// 画出打开时的背景
        } else {
            x = 0;
            canvas.drawBitmap(bg_off, 0, top, paint);// 画出关闭时的背景
        }

        // 对滑块滑动进行异常处理，不能让滑块出界
        if (x < 0) {
            x = 0;
        } else if (x > bg_on.getWidth() - slipper_btn.getWidth()) {
            x = bg_on.getWidth() - slipper_btn.getWidth();
        }

        // 画出滑块
        canvas.drawBitmap(slipper_btn, x, 0, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                nowX = downX;
                break;
            }
//			case MotionEvent.ACTION_MOVE: {
//				nowX = event.getX();
//				break;
//			}
            case MotionEvent.ACTION_UP: {
                if (event.getX() >= (bg_on.getWidth() / 2)) {
                    nowStatus = true;
                    nowX = bg_on.getWidth() - slipper_btn.getWidth();
                } else {
                    nowStatus = false;
                    nowX = 0;
                }
                if (listener != null) {
                    listener.OnChanged(SwitchButtonPregnant.this, nowStatus);
                }
                // 刷新界面
                invalidate();
                break;
            }
        }
        return true;
    }

    /**
     * 为WiperSwitch设置一个监听，供外部调用的方法
     *
     * @param listener
     */
    public void setOnChangedListener(OnChangedListener listener) {
        this.listener = listener;
    }

    /**
     * 设置滑动开关的初始状态，供外部调用
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checked) {
            nowX = bg_off.getWidth();
        } else {
            nowX = 0;
        }
        nowStatus = checked;
//        if (listener != null) {
//            listener.OnChanged(SwitchButton.this, nowStatus);
//        }
        invalidate();
    }

    /**
     * 回调接口
     *
     * @author len
     */
    public interface OnChangedListener {
        void OnChanged(SwitchButtonPregnant switchButton, boolean checkState);
    }
}