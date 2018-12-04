package com.jq.code.view.button;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.jq.code.R;

public class ThreeSwitchButton extends View implements View.OnTouchListener {
    public static final int STATE_LEFT = 0;
    public static final int STATE_MIDDLE = 1;
    public static final int STATE_RIGHT = 2;
    private Bitmap bg_m;
    private Context context;
    private float downX;
    private AdapterView.OnItemClickListener listener;
    private int nowStatus = STATE_LEFT;
    private float nowX;
    private boolean onSlip = false;
    private Bitmap slipper_btn;

    public ThreeSwitchButton(Context paramContext) {
        super(paramContext);
        this.context = paramContext;
        init();
    }

    public ThreeSwitchButton(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.context = paramContext;
        init();
    }

    public ThreeSwitchButton(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.context = paramContext;
        init();
    }

    /**
     * 获取字体大小
     *
     * @param context
     * @param textSize 被计算字体大小值
     * @return 计算后的字体大小值
     */
    private int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;
        // screenWidth = screenWidth > screenHeight ?screenWidth :screenHeight;
        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }

    public void init() {
        this.bg_m = BitmapFactory.decodeResource(getResources(), R.mipmap.three_switch_bar);
        this.slipper_btn = BitmapFactory.decodeResource(getResources(), R.mipmap.three_switch_button);
        setOnTouchListener(this);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        float x = 0;
        canvas.drawBitmap(this.bg_m, 0.0F, this.slipper_btn.getHeight() / 2 + 10, paint);
        if (onSlip) {
            if (nowX > bg_m.getWidth())// 是否划出指定范围,不能让滑块跑到外头,必须做这个判断
                x = bg_m.getWidth() - slipper_btn.getWidth();// 减去滑块1/2的长度
            else
                x = nowX - slipper_btn.getWidth() / 2;
        } else {
            if (nowStatus == STATE_RIGHT) {
                x = this.bg_m.getWidth() - this.slipper_btn.getWidth();
                if (listener != null) {
                    listener.onItemClick(null, this, STATE_RIGHT, STATE_RIGHT);
                }
            } else if (nowStatus == STATE_LEFT) {
                x = 0;
                if (listener != null) {
                    listener.onItemClick(null, this, STATE_LEFT, STATE_LEFT);
                }
            } else if (nowStatus == STATE_MIDDLE) {
                x = this.bg_m.getWidth() / 2 - this.slipper_btn.getWidth() / 2;
                if (listener != null) {
                    listener.onItemClick(null, this, STATE_MIDDLE, STATE_MIDDLE);
                }
            }
        }
        // 对滑块滑动进行异常处理，不能让滑块出界
        if (x < 0) {
            x = 0;
        } else if (x > bg_m.getWidth() - slipper_btn.getWidth()) {
            x = bg_m.getWidth() - slipper_btn.getWidth();
        }

        // 画出滑块
        canvas.drawBitmap(this.slipper_btn, x, 11, paint);
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        int width = this.bg_m.getWidth();
        int height = this.slipper_btn.getHeight();
        if (width < this.slipper_btn.getWidth() * 3) {
            width = this.slipper_btn.getWidth() * 3;
        }
        setMeasuredDimension(width, height + 20);
    }

    public boolean onTouch(View paramView, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (event.getX() > bg_m.getWidth()
                        || event.getY() > getMeasuredHeight()) {
                    return false;
                } else {
                    onSlip = true;
                    downX = event.getX();
                    nowX = downX;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                nowX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP: {
                onSlip = false;
                if (event.getX() >= bg_m.getWidth() * 2 / 3) {
                    nowStatus = STATE_RIGHT;
                    nowX = bg_m.getWidth() - slipper_btn.getWidth();
                } else if (event.getX() <= bg_m.getWidth() / 3) {
                    nowStatus = STATE_LEFT;
                    nowX = 0;
                } else {
                    this.nowStatus = STATE_MIDDLE;
                    nowX = bg_m.getWidth() / 2 - slipper_btn.getWidth() / 2;
                }
                break;
            }
        }
        // 刷新界面
        invalidate();
        return true;
    }

    public void setChecked(int index) {
        if (index == 2) {
            nowX = (this.bg_m.getWidth() - this.slipper_btn.getWidth());
        } else if (index == 0) {
            nowX = 0;
        } else if (index == 1) {
            nowX = (this.bg_m.getWidth() / 2);
        }
        nowStatus = index;
        invalidate();
    }

    public void setOnChangedListener(AdapterView.OnItemClickListener paramOnChangedListener) {
        this.listener = paramOnChangedListener;
    }
}
