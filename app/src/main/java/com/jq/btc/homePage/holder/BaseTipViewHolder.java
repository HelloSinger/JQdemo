package com.jq.btc.homePage.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2016/10/23.
 */

public class BaseTipViewHolder extends RecyclerView.ViewHolder {
    private static BaseTipViewHolder defualtHolder;

    public BaseTipViewHolder(View itemView) {
        super(itemView);
    }

    public static BaseTipViewHolder getDefault(Context context) {
        if (defualtHolder == null) {
            View view = new View(context);
            view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            defualtHolder = new BaseTipViewHolder(view);
        }
        return defualtHolder;
    }

    public View getView() {
        return itemView;
    }

    private boolean isShow;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public void update(Object obj) {
    }
}
