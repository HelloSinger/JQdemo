package com.jq.btc.bluettooth.report.haier;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.report.haier.item.BuildItemsUtil;
import com.jq.btc.bluettooth.report.haier.item.IndexDataItem;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.home.utils.ScrollJudge;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CircleImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 只有体重和BMI
 * Created by lijh on 2017/7/31.
 */

public class OnlyWeightBmiFragment extends Fragment {

    @BindView(R2.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R2.id.mBgLayout)
    LinearLayout mBgLayout;
    @BindView(R2.id.mRoleImage)
    CircleImageView mRoleImage;
    @BindView(R2.id.mRoleName)
    TextView mRoleName;
    @BindView(R2.id.mWeightTime)
    TextView mWeightTime;
    @BindView(R2.id.mIndexLayout)
    LinearLayout mIndexLayout;
    @BindView(R2.id.xiaoqingHint)
    TextView xiaoqingHint;
    Unbinder unbinder;

    private WeightEntity mWeightEntity;

    public void setWeightEntity(WeightEntity weightEntity) {
        mWeightEntity = weightEntity;
    }

    private CsAlgoBuilder csAlgoBuilder;
    private String mCurrentWeightUnit;
    private List<IndexDataItem> mIndexList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_index_only_weight, container, false);
        unbinder = ButterKnife.bind(this, view);

        mCurrentWeightUnit = StandardUtil.getWeightExchangeUnit(getContext());

        mBgLayout.setPadding(0, ScreenUtils.getStatusBarHeight(getActivity()), 0, 0);
        initView();
        initValue();

        return view;
    }

    private void initView() {
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            private ScrollJudge scrollJudge = new ScrollJudge();
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(mScrollView.getScrollY() > 10) {
                    return false;
                }

                // 手势向下 down
                if (scrollJudge.shouldBottom(e1,e2, velocityX, velocityY)) {
                    ((HaierReportActivity)getActivity()).onFinish();
                    return true;
                }
                // 手势向上 up
//                if ((e1.getRawY() - e2.getRawY()) > 200) {
//                    return true;
//                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            private int lastY = 0;
            private int touchEventId = -9983761;
            private int height;
            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    View scroller = (View) msg.obj;
                    if(height == 0) {
                        height = (int)getResources().getDisplayMetrics().density * 80;
                    }
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            if(lastY > height) {
                                ((HaierReportActivity)getActivity()).setScrollUpState(true, OnlyWeightBmiFragment.this);
                            } else {
                                ((HaierReportActivity)getActivity()).setScrollUpState(false, OnlyWeightBmiFragment.this);
                            }
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                            lastY = scroller.getScrollY();
                        }
                    }
                }
            };
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_UP:
                        //handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    default:
                        break;
                }
                return mGestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });
    }

    private void initValue() {
        RoleInfo roleInfo = Account.getInstance(getActivity()).getRoleInfo();
        WeightEntity weightEntity = mWeightEntity;

        mRoleName.setText(roleInfo.getNickname());
        try {
            //weight_time=2017-06-01 16:16:15
            String weightTime = weightEntity.getWeight_time();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date weightDate = sdf.parse(weightTime);
            // "04月25日 13:25"
            SimpleDateFormat textFormat = new SimpleDateFormat("MM月dd日 HH:mm");
            String text = textFormat.format(weightDate);
            mWeightTime.setText(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int age = WeighDataParser.getCalAge(roleInfo,weightEntity) ;
        int height = WeighDataParser.getCalHeight(roleInfo, weightEntity);
        String sexStr = WeighDataParser.getCalSex(roleInfo, weightEntity);
        Log.v("===sess",""+sexStr);
        byte sex = (byte) (sexStr.equals(getString(R.string.women)) ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, weightEntity.getWeight(), sex, age, weightEntity.getR1());

        if(weightEntity.getSex() == 2) {
            xiaoqingHint.setText(R.string.HaierReport_index_hint1);
        } else if(age < 18) {
            xiaoqingHint.setText(R.string.age_tip);
        } else {
            // 显示肥胖度的提示
            float corpulent;
            if (weightEntity.getR1() > 0) {
                corpulent = csAlgoBuilder.getOD();
            } else {
                corpulent = CsAlgoBuilder.calOD(csAlgoBuilder.getH(), weightEntity.getWeight(), csAlgoBuilder.getSex(),
                        csAlgoBuilder.getAge());
            }
            float[] levelNums = WeighDataParser.StandardSet.CORPULENT.getLevelNums();
            int level = 0;
            for (int i = 0; i < levelNums.length; i++) {
                if (corpulent < levelNums[i]) {
                    break;
                } else {
                    level++;
                }
            }
            xiaoqingHint.setText(WeighDataParser.StandardSet.CORPULENT.getTips()[level]);
            xiaoqingHint.setText(getString(WeighDataParser.StandardSet.CORPULENT.getTips()[level],
                    StandardUtil.getWeightExchangeValue(getContext(), csAlgoBuilder.getBW(), "", (byte) 5) + mCurrentWeightUnit));
        }

        BuildItemsUtil util = new BuildItemsUtil(getContext(), mWeightEntity, roleInfo, null);
        IndexDataItem weightItem = util.buildWeightItem();
        IndexDataItem bmiItem = util.buildBMIItem();
        mIndexList.add(weightItem);
        mIndexList.add(bmiItem);

        inflateViews(mIndexList, mIndexLayout);
    }

    private void inflateViews(List<IndexDataItem> items, LinearLayout parent) {
        float density = getResources().getDisplayMetrics().density;
        View line = new View(getContext());
        line.setBackgroundColor(0xfff0f0f5);
        parent.addView(line, LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        for (IndexDataItem item : items) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_haier_index_item, parent, false);
            HaierIndexFragment.ViewHolder holder = new HaierIndexFragment.ViewHolder(view);

            holder.dataItem = item;
            holder.iconView.setImageResource(item.mIconRes);
            holder.nameView.setText(item.nameRes);
            holder.mValueText.setText(item.valueText);
            if (-1 != item.mLevelTextRes) {
                holder.mLevelText.setVisibility(View.VISIBLE);

                holder.mLevelText.setText(item.mLevelTextRes);
                GradientDrawable gd;
                gd = (GradientDrawable) holder.mLevelText.getBackground();
                gd.setColor(getResources().getColor(item.mLevelColorRes));
                holder.mLevelText.setBackground(gd);
            } else {
                holder.mLevelText.setVisibility(View.INVISIBLE);
            }

            if (null != item.levelNums) {
                mDetailViews.add(holder.mOpenDetailView);
                holder.mLayout.setTag(holder);
                holder.mLayout.setOnClickListener(mOnLayoutClick);
            }

            parent.addView(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            line = new View(getContext());
            line.setBackgroundColor(0xfff0f0f5);
            parent.addView(line, LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        }
    }

    private List<View> mDetailViews = new ArrayList<>();
    View.OnClickListener mOnLayoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HaierIndexFragment.ViewHolder holder = (HaierIndexFragment.ViewHolder) v.getTag();
            if (holder.mOpenDetailView.getVisibility() == View.GONE) {
                holder.mOpenDetailView.setVisibility(View.VISIBLE);

                if(holder.reportView.getColors() == null) {
                    // 还没有设置过数据
                    IndexDataItem item = holder.dataItem;
                    if (item.mStandard == WeighDataParser.StandardSet.CORPULENT) {
                        holder.mLevelTip.setText(getString(item.mLevelTipRes,
                                StandardUtil.getWeightExchangeValue(getContext(), csAlgoBuilder.getBW(), "", (byte) 5) + mCurrentWeightUnit));
                    } else {
                        holder.mLevelTip.setText(item.mLevelTipRes);
                    }

                    holder.reportView.setColors(item.colors);
                    holder.reportView.setContent(item.topStr, item.bottomStr, item.levelNums, item.value, item.mBiaoqingIcon);
                }
            } else {
                holder.mOpenDetailView.setVisibility(View.GONE);
            }
            for (View view : mDetailViews) {
                if (view != holder.mOpenDetailView) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
