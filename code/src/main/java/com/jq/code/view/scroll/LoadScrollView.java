package com.jq.code.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.jq.code.view.FooterLoadingView;

/**
 * Created by hfei on 2016/5/26.
 */
public class LoadScrollView extends ScrollView {

    private boolean isLoading = false;
    private OnLoadListener mOnLoadListener;
    private FooterLoadingView mListViewFooter;
    private LinearLayout parent;

    public LoadScrollView(Context context) {
        super(context);
    }

    public LoadScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);

    }

    private void init(Context context) {
        mListViewFooter = new FooterLoadingView(context);
        mListViewFooter.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (!(getChildAt(0) instanceof LinearLayout)) {
            throw new NullPointerException("ScrollView需添加一个LinearLayout");
        }
        parent = (LinearLayout) getChildAt(0);
        int d = parent.getBottom();
        d -= (getMeasuredHeight() + getScrollY());
        if (d == 0 && !isLoading) {
            isLoading = true;
            loadData();
        } else
            super.onScrollChanged(l, t, oldl, oldt);
    }

    /**
     * @param loadListener
     */
    public void setOnLoadListener(OnLoadListener loadListener) {
        mOnLoadListener = loadListener;
    }

    /**
     * 加载
     */
    private void loadData() {
        if (mOnLoadListener != null) {
            setLoading(isLoading);
            mOnLoadListener.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            parent.addView(mListViewFooter);
        } else {
            parent.removeView(mListViewFooter);
        }
    }

    public void LoadOver() {
        mListViewFooter.setText("全部加载完了");
        mListViewFooter.setProgressBarable(false);
    }

    /**
     * 加载更多的监听器
     *
     * @author mrsimple
     */
    public interface OnLoadListener {
        void onLoad();
    }
}