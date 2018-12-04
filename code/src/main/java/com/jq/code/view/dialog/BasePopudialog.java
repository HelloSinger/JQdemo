package com.jq.code.view.dialog;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.PopupWindow;

public class BasePopudialog extends PopupWindow {

    protected View.OnClickListener l;
    protected AdapterView.OnItemClickListener onChangedListener;
    protected Context mContext;


    public BasePopudialog(Context context) {
        mContext = context;
        setBackgroundDrawable(new ColorDrawable(0x7e000000));
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOutsideTouchable(true);
        setFocusable(true);
    }

    public void setOnClickListener(
            View.OnClickListener l) {
        this.l = l;
    }

    public void seOnChangedListener(AdapterView.OnItemClickListener onChangedListener) {
        this.onChangedListener = onChangedListener;
    }
}
