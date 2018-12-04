package com.jq.code.view.scroll;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.jq.code.code.business.DelayTimer;
import com.jq.code.view.FooterLoadingView;

public class RefreshListView extends ListView {

    boolean isLoading = false;
    private FooterLoadingView mListViewFooter;
    private DelayTimer fastChecker;
    private static final long TIME_OUT = 10 * 1000;

    public RefreshListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOnScrollListener(new AutoLoadListener());
        mListViewFooter = new FooterLoadingView(context);
        mListViewFooter.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        addFooterView(mListViewFooter);
        fastChecker = new DelayTimer(new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == DelayTimer.EVENT_CLICK_CALID) {
                    if (isLoading && RefreshListView.this.isActivated()) {
                        setLoading(false);
                    }
                }
                return true;
            }
        }));
    }

    public View getLoadFootView() {
        return mListViewFooter;
    }

    /**
     * 加载
     */
    private void loadData() {
        if (l != null) {
            setLoading(isLoading);
            l.onLoad();
        }
    }

    /**
     * @param loading
     */
    public void setLoading(boolean loading) {
        isLoading = loading;
        if (isLoading) {
            if (getFooterViewsCount() > 0) {
                removeFooterView(mListViewFooter);
            }
            mListViewFooter.reLoad();
            addFooterView(mListViewFooter);
            fastChecker.check(TIME_OUT);
        } else {
            removeFooterView(mListViewFooter);
        }
    }

    public void setLoadOver(String tip) {
        isLoading = false;
        mListViewFooter.reLoadOver(tip);
        if(getFooterViewsCount() == 0){
            addFooterView(mListViewFooter);
        }
    }

    private AutoLoadCallBack l;

    public void addAutoLoadCallBack(AutoLoadCallBack l) {
        this.l = l;
    }

    public interface AutoLoadCallBack {
        void onLoad();
    }

    private boolean isUpScroll = false;
    private float downY = 0;
    private float upY = 0;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                upY = ev.getY();
                isUpScroll = Float.compare(upY, downY) < 0;
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

    class AutoLoadListener implements OnScrollListener {

        private int getLastVisiblePosition = 0;

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
                //滚动到底部
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    View v = view.getChildAt(view.getChildCount() - 1);
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                    int y = location[1];
                    if (view.getLastVisiblePosition() != getLastVisiblePosition) {
                        getLastVisiblePosition = view.getLastVisiblePosition();
                        if (!isLoading && isUpScroll) {
                            isLoading = true;
                            loadData();
                        }
                        return;
                    }
                }
                //未滚动到底部，第二次拖至底部都初始化
                getLastVisiblePosition = 0;
            }
        }

        @Override

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }
}