package com.jq.code.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.jq.code.R;
import com.jq.code.view.text.CustomTextView;

/**
 * Created by hfei on 2016/5/27.
 */
public class FooterLoadingView extends FrameLayout {

    private ViewHolder mHolder;

    class ViewHolder {
        CustomTextView text;
        ProgressBar loadView;
    }

    public FooterLoadingView(Context context) {
        super(context);
        init(context);
    }

    public FooterLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FooterLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.listview_footer, null, false);
        mHolder = new ViewHolder();
        mHolder.text = (CustomTextView) view.findViewById(R.id.pull_to_refresh_loadmore_text);
        mHolder.loadView = (ProgressBar) view.findViewById(R.id.pull_to_refresh_load_progress);
        addView(view);
    }

    public void reLoad() {
        setProgressBarable(true);
        setText("加载更多");
    }

    public void reLoadOver(String tip) {
        setProgressBarable(false);
        setText(tip);
    }

    public void setText(String text) {
        mHolder.text.setText(text);
    }

    public void setText(int text) {
        mHolder.text.setText(text);
    }

    public void setProgressBarable(boolean isVisibility) {
        if (isVisibility) {
            mHolder.loadView.setVisibility(View.VISIBLE);
        } else {
            mHolder.loadView.setVisibility(View.GONE);
        }
    }

}
