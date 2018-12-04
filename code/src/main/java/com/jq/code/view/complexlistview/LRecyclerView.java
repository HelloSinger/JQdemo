package com.jq.code.view.complexlistview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;

import com.jq.code.code.business.DelayTimer;

/**
 * Created by Administrator on 2016/10/11.
 */

public class LRecyclerView extends EmptyRecyclerView {
    public static final int LOAD_ACTION_ING = 1001;
    public static final int LOAD_ACTION_OVER = 1002;
    public static final int LOAD_OVER = 1003;
    public static final int LOAD_ERR = 1004;
    private int state = LOAD_ACTION_OVER;

    public enum LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    private LAYOUT_MANAGER_TYPE layoutManagerType;

    /**
     * 最后一个的位置
     */
    private int[] lastPositions;

    /**
     * 最后一个可见的item的位置
     */
    private int lastVisibleItemPosition;

    private OnLReclerLoad onLoadMore;


    public void addOnLReclerLoad(OnLReclerLoad onLoadMore) {
        this.onLoadMore = onLoadMore;
    }


    public LRecyclerView(Context context) {
        super(context);
    }

    public LRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public LRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private LRecyclerViewAdapter lRecyclerViewAdapter;

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof LRecyclerViewAdapter) {
            lRecyclerViewAdapter = (LRecyclerViewAdapter) adapter;
        }
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        Log.i("onScrolled", dx + "   " + dy);

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManagerType == null) {
            if (layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR;
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID;
            } else {
                throw new RuntimeException(
                        "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastPositions == null) {
                    lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }
                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                lastVisibleItemPosition = findMax(lastPositions);
                break;
        }
    }

    /**
     * 设置加载状态
     *
     * @param state
     */
    public void setLoadState(int state) {
        this.state = state;
        if(lRecyclerViewAdapter == null) return;
        lRecyclerViewAdapter.setLoadState(state);
    }


    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        if (visibleItemCount > 0 && state == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition == totalItemCount - 1) {
            loadMore();
        }
    }
    @Override
    public boolean canScrollVertically(int direction) {
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < 0 || original;
        }
        return super.canScrollVertically(direction);

    }

    DelayTimer fastChecker = new DelayTimer(new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == DelayTimer.EVENT_CLICK_CALID) {
                if (state == LOAD_ACTION_ING) {
                    setLoadState(LOAD_ERR);
                }
            }
            return true;
        }
    }));

    public void loadMore() {
        if (onLoadMore != null && state != LOAD_ACTION_ING) {
            setLoadState(LOAD_ACTION_ING);
            fastChecker.check(10 * 1000);
            onLoadMore.onLoadMore();
        }
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public interface OnLReclerLoad {
        void onLoadMore();
    }
}