package com.jq.btc.bluettooth.report.haier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.jq.btc.NoScrollViewPager;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HaierReportActivity extends FragmentActivity {

    public static final String INTENT_KEY_WEIGHT = "weight";
    public static final String INTENT_KEY_LAST_WEIGHT = "lastWeight";
    public static final String INTENT_KEY_FROM_HOME = "fromHome";

    @BindView(R2.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R2.id.mGoBackView)
    ImageView mGoBackView;
    @BindView(R2.id.indexRb)
    RadioButton indexRb;
    @BindView(R2.id.indexImg)
    View indexImg;
    @BindView(R2.id.indexLayout)
    LinearLayout indexLayout;
    @BindView(R2.id.reportRb)
    RadioButton reportRb;
    @BindView(R2.id.reportImg)
    View reportImg;
    @BindView(R2.id.reportLayout)
    LinearLayout reportLayout;
    @BindView(R2.id.mTopLayout)
    LinearLayout mTopLayout;

    private WeightEntity mCurWeightEntity;
    private WeightEntity mLastWeightEntity;
    private RoleInfo mCurRoleInfo;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    /**
     * 是否从动态页来
     */
    private boolean mFromHome;

    private String useId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haier_report);
        ButterKnife.bind(this);

        initValue();
        initView();
    }

    public void onFinish() {
        if (mFromHome) {
            finish();
            overridePendingTransition(0, R.anim.slide_up_out);
        }
    }

    private void initValue() {
        useId = getIntent().getStringExtra("useId");
        mCurWeightEntity = getIntent().getParcelableExtra(INTENT_KEY_WEIGHT);
        Log.v("===sess3", "" + mCurWeightEntity.toString());
        mLastWeightEntity = getIntent().getParcelableExtra(INTENT_KEY_LAST_WEIGHT);
        mFromHome = getIntent().getBooleanExtra(INTENT_KEY_FROM_HOME, false);
        mCurRoleInfo = Account.getInstance(this).getRoleInfo();
    }

    private void initView() {
        mTopLayout.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);
        if (mFromHome) {
            mGoBackView.setImageResource(R.mipmap.back_bottom);
            bottomAnim();
        } else {
            mGoBackView.setImageResource(R.mipmap.head_back);
        }

        fragments = new ArrayList<>();

        if ((mCurWeightEntity.getR1() == 0 && mCurWeightEntity.getAxunge() <= 0) || (mCurWeightEntity.getSex() == 2)) {
            // 只有体重和BMI
            OnlyWeightBmiFragment onlyWeightBmiFragment = new OnlyWeightBmiFragment();
            Log.v("===sess1", "" + onlyWeightBmiFragment.toString());
            onlyWeightBmiFragment.setWeightEntity(mCurWeightEntity);
            fragments.add(onlyWeightBmiFragment);

            // 名字叫做“测量报告”
            indexRb.setText(R.string.measure_report);
            indexRb.setChecked(true);
            indexImg.setVisibility(View.GONE);
            reportLayout.setVisibility(View.GONE);
        } else {
            HaierIndexFragment indexFragment = new HaierIndexFragment();
            indexFragment.setWeightEntity(mCurWeightEntity);
            BodyConstitutionFragment bodyConstitutionFragment = new BodyConstitutionFragment();
            bodyConstitutionFragment.setCurrWeightEntity(mCurWeightEntity);
            bodyConstitutionFragment.setLastWeightEntity(mLastWeightEntity);
            Log.v("===sess2", "" + mCurWeightEntity.toString());
            Bundle bundle = new Bundle();
            bundle.putString("useId", useId);
            indexFragment.setArguments(bundle);
            fragments.add(indexFragment);
            fragments.add(bodyConstitutionFragment);

            indexRb.setChecked(true);
            indexImg.setVisibility(View.VISIBLE);
            reportRb.setChecked(false);
            reportImg.setVisibility(View.INVISIBLE);
        }

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @OnClick({R2.id.mGoBackView, R2.id.indexLayout, R2.id.reportLayout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R2.id.mGoBackView:
                finish();
                if (mFromHome) {
                    overridePendingTransition(0, R.anim.slide_up_out);
                }
                break;
            case R2.id.indexLayout:
                if (fragments.size() == 1) {
                    return;
                }
                viewPager.setCurrentItem(0, false);
                indexRb.setChecked(true);
                indexImg.setVisibility(View.VISIBLE);
                reportRb.setChecked(false);
                reportImg.setVisibility(View.INVISIBLE);
                setLayoutTextColor(fragments.get(0));
                break;
            case R2.id.reportLayout:
                viewPager.setCurrentItem(1, false);
                indexRb.setChecked(false);
                indexImg.setVisibility(View.INVISIBLE);
                reportRb.setChecked(true);
                reportImg.setVisibility(View.VISIBLE);
                setLayoutTextColor(fragments.get(1));
                break;
        }
    }

    private void bottomAnim() {
        boolean b = false;
        if (b) {
            return;
        }
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mGoBackView, "scaleX", 1, 0.8f);
        animatorScaleX.setRepeatCount(-1);
        animatorScaleX.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mGoBackView, "scaleY", 1, 0.8f);
        animatorScaleY.setRepeatCount(-1);
        animatorScaleY.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator animatorTranslationY = ObjectAnimator.ofFloat(mGoBackView, "translationY", 0, 12f);
        animatorTranslationY.setRepeatCount(-1);
        animatorTranslationY.setRepeatMode(ValueAnimator.RESTART);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorScaleX, animatorScaleY, animatorTranslationY);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }

    private boolean mIndexUp;
    private boolean mReportUp;

    public void setScrollUpState(boolean scrollUp, Fragment fragment) {
        boolean different = false;
        if (fragment instanceof HaierIndexFragment || fragment instanceof OnlyWeightBmiFragment) {
            if (scrollUp != mIndexUp) {
                different = true;
                mIndexUp = scrollUp;
            }
        } else {
            if (scrollUp != mReportUp) {
                different = true;
                mReportUp = scrollUp;
            }
        }
        if (different) {
            setLayoutTextColor(fragment);
        }
    }

    public boolean getScrollUpState(Fragment fragment) {
        if (fragment instanceof HaierIndexFragment || fragment instanceof OnlyWeightBmiFragment) {
            return mIndexUp;
        } else {
            return mReportUp;
        }
    }

    private void setLayoutTextColor(Fragment fragment) {
        //0xFF505050
        if (fragment instanceof HaierIndexFragment) {
            if (mIndexUp) {
                mTopLayout.setBackgroundColor(0xFFFFFFFF);
                ScreenUtils.setScreenFullStyle(this, Color.WHITE);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom_blue);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                indexImg.setBackgroundColor(0xFF505050);
                reportImg.setBackgroundColor(0xFF505050);
            } else {
                mTopLayout.setBackgroundColor(0);
                ScreenUtils.setScreenFullStyle(this, Color.TRANSPARENT);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                indexImg.setBackgroundColor(0xFFFFFFFF);
                reportImg.setBackgroundColor(0xFFFFFFFF);
            }
        } else if (fragment instanceof BodyConstitutionFragment) {
            if (mReportUp) {
                mTopLayout.setBackgroundColor(0xFFFFFFFF);
                ScreenUtils.setScreenFullStyle(this, Color.WHITE);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom_blue);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                indexImg.setBackgroundColor(0xFF505050);
                reportImg.setBackgroundColor(0xFF505050);
            } else {
                mTopLayout.setBackgroundColor(0);
                ScreenUtils.setScreenFullStyle(this, Color.TRANSPARENT);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                indexImg.setBackgroundColor(0xFFFFFFFF);
                reportImg.setBackgroundColor(0xFFFFFFFF);
            }
        } else if (fragment instanceof OnlyWeightBmiFragment) {
            if (mIndexUp) {
                mTopLayout.setBackgroundColor(0xFFFFFFFF);
                ScreenUtils.setScreenFullStyle(this, Color.WHITE);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom_blue);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.report_up_text_style));
                indexImg.setBackgroundColor(0xFF505050);
                reportImg.setBackgroundColor(0xFF505050);
            } else {
                mTopLayout.setBackgroundColor(0);
                ScreenUtils.setScreenFullStyle(this, Color.TRANSPARENT);
                if (mFromHome) {
                    mGoBackView.setImageResource(R.mipmap.back_bottom);
                }

                indexRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                reportRb.setTextColor(getResources().getColorStateList(R.drawable.trend_tag_text_style));
                indexImg.setBackgroundColor(0xFFFFFFFF);
                reportImg.setBackgroundColor(0xFFFFFFFF);
            }
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

    }
}
