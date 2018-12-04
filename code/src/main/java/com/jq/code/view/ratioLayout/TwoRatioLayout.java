package com.jq.code.view.ratioLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jq.code.code.util.ScreenUtils;

/**
 * Created by Administrator on 2017/2/16.
 */

public class TwoRatioLayout extends LinearLayout {
    public TwoRatioLayout(Context context) {
        super(context);
    }

    public TwoRatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoRatioLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (getChildCount() != 2) {
            throw new TwoRatioException("该父控件只能且需要包含2个子控件");
        }
        setOrientation(VERTICAL);
        View child1 = getChildAt(0);
        View child2 = getChildAt(1);
        child1.setPadding(0, ScreenUtils.getStatusBarHeight(getContext()),0,0);
        child1.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.1f));
        child2.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 10.1f));
    }
}
