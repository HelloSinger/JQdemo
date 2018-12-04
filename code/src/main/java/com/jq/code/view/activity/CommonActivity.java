package com.jq.code.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jq.code.R;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.view.LevelLoadingRenderer;
import com.jq.code.view.LoadingDrawable;
import com.jq.code.view.LoadingRenderer;


public class CommonActivity extends DragActivity implements View.OnClickListener{

    private ViewHolder mViewHolder;
    private LayoutInflater mInflater;
    private LoadingDrawable loadingDrawable;

    private class ViewHolder {
        LinearLayout contentLinearLayout;
        RelativeLayout titleLayout;
        ImageView back;
        ImageView rightIcon;
        TextView title;
        TextView rightText;
        ImageView loadImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        initView();
        ScreenUtils.setScreenFullStyle(this, Color.WHITE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.back = (ImageView) findViewById(R.id.common_back);
        mViewHolder.title = (TextView) findViewById(R.id.common_middle_text);
        mViewHolder.loadImage = (ImageView) findViewById(R.id.loadImage);
        LoadingRenderer renderer = new LevelLoadingRenderer(this, mViewHolder.loadImage);
        loadingDrawable = new LoadingDrawable(renderer);
        mViewHolder.loadImage.setImageDrawable(loadingDrawable);
        mViewHolder.rightText = (TextView) findViewById(R.id.common_right_text);
        mViewHolder.rightIcon = (ImageView) findViewById(R.id.rightIcon);
        mViewHolder.contentLinearLayout = (LinearLayout) findViewById(R.id.contentLinearLayout);
        mViewHolder.titleLayout = (RelativeLayout) findViewById(R.id.title_layout);

        mViewHolder.back.setOnClickListener(this);
        mViewHolder.rightText.setOnClickListener(this);
        mViewHolder.rightIcon.setOnClickListener(this);

        mViewHolder.titleLayout.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 6);
        mInflater = LayoutInflater.from(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    public void onBack() {
        finishSelf();
    }

    protected void finishSelf() {
        finish();
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }


    protected void setContentSubView(int layoutId, int title) {
        setContentSubView(layoutId, 0, title, 0);
    }

    protected void setRightImageRes(int res) {
        if (mViewHolder != null) {
            mViewHolder.rightIcon.setVisibility(View.VISIBLE);
            mViewHolder.rightIcon.setImageResource(res);
        }
    }

    protected void setRightText(int res) {
        if (mViewHolder != null) {
            mViewHolder.rightText.setVisibility(View.VISIBLE);
            mViewHolder.rightText.setText(res);
        }
    }

    protected void setTitle(String title) {
        mViewHolder.title.setText(title);
    }

    protected void setContentSubView(int layoutId, String title) {
        setContentSubView(layoutId, 0, title, false);
    }

    protected void setContentSubView(int layoutId, int layoutColor, String title) {
        setContentSubView(layoutId, layoutColor, title, false);
    }

    protected void setContentSubView(int layoutId, int layoutColor, String title, boolean showRightIcon) {
        if (mInflater != null) {
            View view = mInflater.inflate(layoutId,
                    mViewHolder.contentLinearLayout, false);
            mViewHolder.contentLinearLayout.addView(view);
        }
        if (mViewHolder != null) {
            if (!TextUtils.isEmpty(title))
                mViewHolder.title.setText(title);
//            if (layoutColor != 0)
//                mViewHolder.titleLayout.setBackgroundColor(layoutColor);
            if (showRightIcon) {
                mViewHolder.rightIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化view
     *
     * @param layoutId
     * @param layoutColor
     * @param title
     * @param rightid
     */
    protected void setContentSubView(int layoutId, int layoutColor, int title, int rightid) {
        setContentSubView(layoutId, layoutColor, title, rightid, false);
    }

    protected void setContentSubView(int layoutId, int layoutColor, int title, boolean showRightShare) {
        setContentSubView(layoutId, layoutColor, title, 0, showRightShare);
    }

    /**
     * 初始化view
     *
     * @param layoutId
     * @param layoutColor
     * @param title
     * @param rightid
     */
    protected void setContentSubView(int layoutId, int layoutColor, int title, int rightid,
                                     boolean showRightShare) {
        if (mInflater != null) {
            View view = mInflater.inflate(layoutId,
                    mViewHolder.contentLinearLayout, false);
            mViewHolder.contentLinearLayout.addView(view);
        }
        if (mViewHolder != null) {
            if (title != 0)
                mViewHolder.title.setText(title);
            if (rightid != 0) {
                mViewHolder.rightText.setVisibility(View.VISIBLE);
                mViewHolder.rightText.setText(rightid);
            }
//            if (layoutColor != 0)
//                mViewHolder.titleLayout.setBackgroundColor(layoutColor);
            if (showRightShare) {
                mViewHolder.rightIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setRightOnClickListener(View.OnClickListener l) {
        if (mViewHolder.rightText.getVisibility() == View.VISIBLE) {
            mViewHolder.rightText.setOnClickListener(l);
        } else if (mViewHolder.rightIcon.getVisibility() == View.VISIBLE) {
            mViewHolder.rightIcon.setOnClickListener(l);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mViewHolder.back) {
            onBack();
        } else if (v == mViewHolder.rightText || v == mViewHolder.rightIcon) {
            onRightClick();
        } else {
            onOtherClick(v);
        }
    }

    protected void onOtherClick(View v) {

    }

    protected void onRightClick() {
    }

}
