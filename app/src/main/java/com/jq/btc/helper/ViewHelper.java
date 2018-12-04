package com.jq.btc.helper;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.jq.btc.app.R;
import com.jq.code.code.util.ViewUtil;
import com.jq.code.view.text.CustomTextView;

/**
 * Created by Administrator on 2016/9/10.
 */
public class ViewHelper {
    /**
     * 数据为空时显示视图
     *
     * @return
     */
    public static View getEmptyView(Context context, String tip) {
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.setPadding(0, ViewUtil.dip2px(context, 60), 0, ViewUtil.dip2px(context, 60));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        ImageView view = new ImageView(context);
        view.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.setImageResource(R.mipmap.data_empty);
        CustomTextView textView = new CustomTextView(context);
        textView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setPadding(0, ViewUtil.dip2px(context, 6), 0, 0);
        textView.setText(tip);
        textView.setTextColor(context.getResources().getColor(R.color.text_gray));
        textView.setTextSize(34);
        textView.setTypeface(CustomTextView.LTEX);
        layout.addView(view);
        layout.addView(textView);
        return layout;
    }
}
