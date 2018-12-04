package com.jq.code.view.wheel.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jq.code.code.util.ViewUtil;
import com.jq.code.view.text.CustomTextView;
import com.jq.code.view.wheel.TosGallery;

import java.util.ArrayList;
import java.util.List;

public class WheelViewAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> mHeightSelected;
    private int mCenterIndex = -1;
    private int mCenterColor;
    private int mOthersColor;
    private int gravity = Gravity.CENTER;
    private int mHeight = 60;
    /** 以sp为单位 */
    private int mTextSize = 21;

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public WheelViewAdapter(Context context, int start, int end) {
        this.context = context;
        mHeight = ViewUtil.dip2px(context, 50);
        mHeightSelected = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            mHeightSelected.add(i);
        }
    }

    @Override
    public int getCount() {
        return mHeightSelected.size();
    }

    @Override
    public Integer getItem(int arg0) {
        return mHeightSelected.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void setTextColor(int vulue, int centerColor, int othersColor) {
        mCenterIndex = vulue;
        mCenterColor = centerColor;
        mOthersColor = othersColor;
        notifyDataSetChanged();
    }

    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    public void setDateRanger(int min, int max) {
        mHeightSelected = new ArrayList<Integer>();
        for (int i = min; i <= max; i++) {
            mHeightSelected.add(i);
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomTextView txtView = null;

        if (null == convertView) {
            convertView = new CustomTextView(context);
            convertView
                    .setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
            txtView = (CustomTextView) convertView;
        }

        String text = mHeightSelected.get(position) < 10 ? ("0" + mHeightSelected
                .get(position)) : (mHeightSelected.get(position) + "");
        if (null == txtView) {
            txtView = (CustomTextView) convertView;
        }
        txtView.setGravity(gravity);
        if (mCenterIndex == position) {
            txtView.setTextColor(mCenterColor);
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize);
        } else {
            txtView.setTextColor(mOthersColor);
            txtView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (int)(mTextSize * 0.9));
        }
        txtView.setText(text);
        return convertView;
    }
}
