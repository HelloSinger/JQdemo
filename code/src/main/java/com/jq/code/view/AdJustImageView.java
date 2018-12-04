package com.jq.code.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/11/9.
 */

public class AdJustImageView extends ImageView {

    private float hRatio = 247f / 480f;

    public float gethRatio() {
        return hRatio;
    }

    public void sethRatio(float hRatio) {
        this.hRatio = hRatio;
    }

    public AdJustImageView(Context context) {
        super(context);
    }

    public AdJustImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public AdJustImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        setMeasuredDimension(w, (int) (hRatio * w));
    }
}
