package com.jq.code.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/11/3.
 */

public class CircleView extends View {

    private int h, w;
    private int radio;
    private Paint paint;
    private int color = Color.RED;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = h;
        this.w = w;
        radio = Math.min(h, w) / 2;
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        canvas.drawCircle(w / 2, h / 2, radio, paint);
    }

    public void setColor(int color){
        this.color = color;
        invalidate();
    }
}
