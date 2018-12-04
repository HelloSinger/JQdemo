package com.jq.code.view.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.jq.code.R;


/**
 * 性别选择开关
 */
public class SwitchTextView extends ImageView implements OnTouchListener {

    /**
     * 监听接口
     */
    private AdapterView.OnItemClickListener listener;
    private Context context;

    public SwitchTextView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SwitchTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public SwitchTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Bitmap bg_m = BitmapFactory.decodeResource(getResources(),
                R.mipmap.sex_frame_m);
        int widthSize = bg_m.getWidth();
        int heightSize = bg_m.getHeight();
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (event.getX() > getWidth()
                        || event.getY() > getHeight()) {
                    return false;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (event.getX() > getWidth() / 2) {
                    setImageResource(R.mipmap.sex_frame_m);
                    if (listener != null) {
                        listener.onItemClick(null,SwitchTextView.this, 1,1);
                    }
                } else{
                    setImageResource(R.mipmap.sex_frame_f);
                    if (listener != null) {
                        listener.onItemClick(null,SwitchTextView.this, 0, 0);
                    }
                }
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
    public void setOnChangedListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 设置滑动开关的初始状态，供外部调用
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checked) {
            setImageResource(R.mipmap.sex_frame_m);
        } else {
            setImageResource(R.mipmap.sex_frame_f);
        }
    }
}