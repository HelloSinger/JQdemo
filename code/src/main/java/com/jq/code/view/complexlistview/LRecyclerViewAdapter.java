package com.jq.code.view.complexlistview;

/**
 * Created by Administrator on 2016/10/11.
 */

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jq.code.R;
import com.jq.code.code.business.DelayTimer;


/**
 * 外层RecyclerView的Adapter
 */
public abstract class LRecyclerViewAdapter extends RecyclerView.Adapter<BaseHolder> {

    public static final int LOAD_MORE = "more".hashCode();
    private int loadOverText;
    private LRecyclerView recyclerView;
    private LoadViewHolder mLoadViewHolder;

    public void setLoadOverText(int loadOverText) {
        this.loadOverText = loadOverText;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (recyclerView == null && (parent instanceof LRecyclerView)) {
            recyclerView = (LRecyclerView) parent;
        }
        if (viewType == LOAD_MORE) {
            mLoadViewHolder = new LoadViewHolder(R.layout.listview_footer, parent, viewType);
            return mLoadViewHolder;
        } else {
            return onCreateLViewHolder(parent, viewType);
        }
    }

    public abstract BaseHolder onCreateLViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if (holder instanceof LoadViewHolder) {
            holder.refreshData(LRecyclerView.LOAD_ACTION_OVER, getItemCount() - 1);
        } else {
            onBindLViewHolder(holder, position);
        }
    }


    public void setLoadState(int state) {
        if (mLoadViewHolder != null) {
            mLoadViewHolder.refreshData(state, getItemCount() - 1);
        }
    }

    protected abstract void onBindLViewHolder(BaseHolder holder, int position);

    @Override
    /**
     * 当Item出现时调用此方法
     */
    public void onViewAttachedToWindow(BaseHolder holder) {
        Log.i("mengyuan", "onViewAttachedToWindow:" + holder.getClass().toString());
    }

    @Override
    /**
     * 当Item被回收时调用此方法
     */
    public void onViewDetachedFromWindow(BaseHolder holder) {
        Log.i("mengyuan", "onViewDetachedFromWindow:" + holder.getClass().toString());
    }


    @Override
    public int getItemCount() {
        return 1 + getLItemCount();
    }

    protected abstract int getLItemCount();

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return LOAD_MORE;
        } else {
            return getLItemViewType(position);
        }
    }

    protected abstract int getLItemViewType(int position);


    /**
     * Load hodler
     */
    private class LoadViewHolder extends BaseHolder<Integer> {
        TextView text;
        ProgressBar loadView;
        DelayTimer delayTimer = new DelayTimer(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (text != null) text.setVisibility(View.INVISIBLE);
                return true;
            }
        }));

        public LoadViewHolder(int viewId, ViewGroup parent, int viewType) {
            super(viewId, parent, viewType);
            text = (TextView) itemView.findViewById(R.id.pull_to_refresh_loadmore_text);
            loadView = (ProgressBar) itemView.findViewById(R.id.pull_to_refresh_load_progress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerView != null) {
                        recyclerView.loadMore();
                    }
                }
            });
        }

        @Override
        public void refreshData(Integer state, int position) {
            super.refreshData(state, position);
            itemView.setClickable(false);
            switch (state) {
                case LRecyclerView.LOAD_OVER:
                    loadView.setVisibility(View.GONE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(loadOverText == 0 ? R.string.lastDataTip : loadOverText);
                    delayTimer.check(1000);
                    break;
                case LRecyclerView.LOAD_ACTION_ING:
                    loadView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.VISIBLE);
                    text.setText(R.string.loadMore);
                    break;
                case LRecyclerView.LOAD_ERR:
                case LRecyclerView.LOAD_ACTION_OVER:
                    loadView.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.INVISIBLE);
                    break;
//                case LOAD_ERR:
//                    loadView.setVisibility(View.INVISIBLE);
//                    text.setText(R.string.reLoadMore);
//                    itemView.setClickable(true);
//                    if (recyclerView != null) {
//                        recyclerView.setLoading(false);
//                    }
//                    break;
            }
        }
    }
}
