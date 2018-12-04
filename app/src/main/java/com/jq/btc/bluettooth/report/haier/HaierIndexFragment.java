package com.jq.btc.bluettooth.report.haier;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jq.code.view.CustomReportView;
import com.jq.code.view.text.CustomTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lijh on 2017/7/26.
 * 数据指标
 */

public class HaierIndexFragment extends Fragment {
    @BindView(R2.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R2.id.mBgLayout)
    View mBgLayout;
    @BindView(R2.id.t1)
    TextView t1;
    @BindView(R2.id.mScore)
    CustomTextView mScore;
    @BindView(R2.id.t2)
    TextView t2;
    @BindView(R2.id.mBodily)
    CustomTextView mBodily;
    @BindView(R2.id.mRoleImage)
    CircleImageView mRoleImage;
    @BindView(R2.id.mRoleName)
    TextView mRoleName;
    @BindView(R2.id.mWeightTime)
    TextView mWeightTime;
    @BindView(R2.id.mAbNormalHint)
    TextView mAbNormalHint;
    @BindView(R2.id.mAbNormalIndexLayout)
    LinearLayout mAbNormalIndexLayout;
    @BindView(R2.id.mNormalHint)
    TextView mNormalHint;
    @BindView(R2.id.mNormalIndexLayout)
    LinearLayout mNormalIndexLayout;
    @BindView(R2.id.mOtherHint)
    TextView mOtherHint;
    @BindView(R2.id.mOtherIndexLayout)
    LinearLayout mOtherIndexLayout;
    Unbinder unbinder;

    private WeightEntity mWeightEntity;

    public void setWeightEntity(WeightEntity weightEntity) {
        mWeightEntity = weightEntity;
    }

    private String mCurrentWeightUnit;
    private CsAlgoBuilder csAlgoBuilder;
    private ArrayList<Integer> mSortedNames = new ArrayList<>();

    {
        // 按照这个顺序排序显示
        mSortedNames.add(WeighDataParser.StandardSet.WEIGHT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.AXUNGE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.AXUNGEWEIGHT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLEWEIGHT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.VISCERA.getName());
        mSortedNames.add(WeighDataParser.StandardSet.WATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.CONTAINWATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.METABOLISM.getName());
        mSortedNames.add(WeighDataParser.StandardSet.PROTEIN.getName());
        mSortedNames.add(WeighDataParser.StandardSet.BONE.getName());
    }

    /**
     * 不正常指标
     */
    private List<IndexDataItem> mAbnormalIndexList = new ArrayList<>();
    /**
     * 正常指标
     */
    private List<IndexDataItem> normalIndexList = new ArrayList<>();
    /**
     * 其他指标
     */
    private List<IndexDataItem> mOtherIndexList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_index, container, false);
        unbinder = ButterKnife.bind(this, view);

        mCurrentWeightUnit = StandardUtil.getWeightExchangeUnit(getContext());
        buildSuanfa();

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
                        height = (int)getResources().getDisplayMetrics().density * 50;
                    }
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            if (lastY > height) {
                                ((HaierReportActivity) getActivity()).setScrollUpState(true, HaierIndexFragment.this);
                            } else {
                                ((HaierReportActivity) getActivity()).setScrollUpState(false, HaierIndexFragment.this);
                            }
                        } else {
                            handler.sendMessageDelayed(handler.obtainMessage(touchEventId, scroller), 1);
                            lastY = scroller.getScrollY();

                            // 不停止也要做
                            if (lastY > height) {
                                ((HaierReportActivity) getActivity()).setScrollUpState(true, HaierIndexFragment.this);
                            } else {
                                ((HaierReportActivity) getActivity()).setScrollUpState(false, HaierIndexFragment.this);
                            }
                        }
                    }
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_UP:
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    default:
                        break;
                }
                return mGestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });
    }

    private void initValue() {
       RoleInfo roleInfo = Account.getInstance(getContext()).getRoleInfo();
        WeighDataParser weighDataParser = WeighDataParser.create(getContext());
        WeightEntity weightEntity = mWeightEntity;

        // 身体得分
        int score = 0;
        int age = WeighDataParser.getCalAge(roleInfo, weightEntity);
        if (weightEntity.getR1() > 0) {
            if (age > 5) {
                score = (int) csAlgoBuilder.getScore();
            }
        } else if (weightEntity.getAxunge() > 0) {
            score = weighDataParser.calculateCode(weightEntity, roleInfo);
        }
        if (score > 0) {
            mScore.setText(score + "");
        } else {
            mScore.setText("- -");
        }

        // 体型
        int bodilyLevel = -1;
        if (mWeightEntity.getR1() > 0) {
            bodilyLevel = (int) (csAlgoBuilder.getShape()) + 1;
        } else {
            bodilyLevel = (int) (CsAlgoBuilder.calShape(csAlgoBuilder.getH(), mWeightEntity.getWeight(), csAlgoBuilder.getSex(), csAlgoBuilder.getAge(), mWeightEntity.getAxunge())) + 1;
        }
        mBodily.setText(WeighDataParser.StandardSet.BODILY.getStandards()[bodilyLevel]);

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

        BuildItemsUtil util = new BuildItemsUtil(getContext(), mWeightEntity, roleInfo, csAlgoBuilder);
        IndexDataItem weightItem = util.buildWeightItem();
        IndexDataItem axungeItem = util.buildAxungeItem();
        IndexDataItem fatWeightItem = util.buildFatWeightItem();
        IndexDataItem boneItem = util.buildBoneItem();
        IndexDataItem muscleItem = util.buildMuscleItem();
        IndexDataItem muscleWeightItem = util.buildMuscleWeightItem();
        IndexDataItem metabolismItem = util.buildMetabolismItem();
        IndexDataItem corpulentItem = util.buildCorpulentItem();
        IndexDataItem bmiItem = util.buildBMIItem();
        IndexDataItem thinItem = util.buildThinItem();

        judgeItemAndAdd(weightItem, WeighDataParser.StandardSet.WEIGHT);
        judgeItemAndAdd(axungeItem, WeighDataParser.StandardSet.AXUNGE);
        judgeItemAndAdd(fatWeightItem, WeighDataParser.StandardSet.AXUNGEWEIGHT);
        judgeItemAndAdd(boneItem, WeighDataParser.StandardSet.BONE);
        judgeItemAndAdd(muscleItem, WeighDataParser.StandardSet.MUSCLE);
        judgeItemAndAdd(muscleWeightItem, WeighDataParser.StandardSet.MUSCLEWEIGHT);
        judgeItemAndAdd(metabolismItem, WeighDataParser.StandardSet.METABOLISM);


        mOtherIndexList.add(corpulentItem);
        mOtherIndexList.add(bmiItem);
        mOtherIndexList.add(thinItem);

        if (weightEntity.getR1() > 0 && age < 18) {
            // 交流测脂称，未成年人，水份、水含量、蛋白质、内脏脂肪、身体年龄，隐藏不显示
        } else {
            IndexDataItem waterItem = util.buildWaterItem();
            IndexDataItem waterWeightItem = util.buildWaterWeightItem();
            IndexDataItem proteinItem = util.buildProteinItem();
            IndexDataItem visceraItem = util.buildVisceraItem();
            IndexDataItem bodyAgeItem = util.buildBodyAgeItem();

            judgeItemAndAdd(visceraItem, WeighDataParser.StandardSet.VISCERA);
            judgeItemAndAdd(waterItem, WeighDataParser.StandardSet.WATER);
            judgeItemAndAdd(waterWeightItem, WeighDataParser.StandardSet.CONTAINWATER);
            judgeItemAndAdd(proteinItem, WeighDataParser.StandardSet.PROTEIN);
            mOtherIndexList.add(bodyAgeItem);
        }

        createViews();
    }

    /**
     * 判断某个指标是否合格并分类添加
     *
     * @param item     数据对象
     * @param standard 标准对象
     */
    private void judgeItemAndAdd(IndexDataItem item, WeighDataParser.StandardSet standard) {
        // 判断是否合格
        boolean normal = false;
        switch (standard) {
            case VISCERA:
                normal = item.level == 0;
                break;
            case WATER:
                normal = item.level >= 1;
                break;
            case CONTAINWATER:
                normal = item.level >= 1;
                break;
            case WEIGHT:
                normal = item.level == 1;
                break;
            case AXUNGE:
                normal = item.level == 1;
                break;
            case AXUNGEWEIGHT:
                normal = item.level == 1;
                break;
            case PROTEIN:
                normal = item.level == 1;
                break;
            case BONE:
                normal = item.level >= 1;
                break;
            case MUSCLE:
                normal = item.level >= 1;
                break;
            case MUSCLEWEIGHT:
                normal = item.level >= 1;
                break;
            case METABOLISM:
                normal = item.level == 1;
                break;
        }
        if (normal) {
            normalIndexList.add(item);
        } else {
            mAbnormalIndexList.add(item);
        }
    }

    private void sort(List<IndexDataItem> items) {
        // 排序
        Collections.sort(items, new Comparator<IndexDataItem>() {
            @Override
            public int compare(IndexDataItem lhs, IndexDataItem rhs) {
                int leftIndex = mSortedNames.indexOf(lhs.nameRes);
                int rightIndex = mSortedNames.indexOf(rhs.nameRes);
                if (leftIndex == -1) {
                    leftIndex = Integer.MAX_VALUE;
                }
                if (rightIndex == -1) {
                    rightIndex = Integer.MAX_VALUE;
                }
                return leftIndex - rightIndex;
            }
        });
    }

    private void createViews() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (mAbnormalIndexList.size() > 0) {
                    mAbNormalHint.setVisibility(View.VISIBLE);
                    mAbNormalIndexLayout.setVisibility(View.VISIBLE);

                    sort(mAbnormalIndexList);
                    inflateViews(mAbnormalIndexList, mAbNormalIndexLayout);
                } else {
                    mAbNormalHint.setVisibility(View.GONE);
                    mAbNormalIndexLayout.setVisibility(View.GONE);
                }
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (normalIndexList.size() > 0) {
                    mNormalHint.setVisibility(View.VISIBLE);
                    mNormalIndexLayout.setVisibility(View.VISIBLE);

                    sort(normalIndexList);
                    inflateViews(normalIndexList, mNormalIndexLayout);
                } else {
                    mNormalHint.setVisibility(View.GONE);
                    mNormalIndexLayout.setVisibility(View.GONE);
                }
            }
        }, 100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mOtherIndexList.size() > 0) {
                    mOtherHint.setVisibility(View.VISIBLE);
                    mOtherIndexLayout.setVisibility(View.VISIBLE);

                    inflateViews(mOtherIndexList, mOtherIndexLayout);
                } else {
                    mOtherHint.setVisibility(View.GONE);
                    mOtherIndexLayout.setVisibility(View.GONE);
                }
            }
        }, 400);

    }

    private void inflateViews(List<IndexDataItem> items, LinearLayout parent) {
        float density = getResources().getDisplayMetrics().density;
        View line = new View(getContext());
        line.setBackgroundColor(0xfff0f0f5);
        parent.addView(line, LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        for (IndexDataItem item : items) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_haier_index_item, parent, false);
            ViewHolder holder = new ViewHolder(view);

            holder.dataItem = item;
            holder.iconView.setImageResource(item.mIconRes);
            holder.nameView.setText(item.nameRes);
            holder.mValueText.setText(item.valueText);
            holder.mUnitText.setText(item.mUnitText);
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
            ViewHolder holder = (ViewHolder) v.getTag();
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

    /**
     * 创建算法对象
     */
    private void buildSuanfa() {
        RoleInfo curRoleInfo = Account.getInstance(getActivity()).getRoleInfo();
        WeightEntity weightEntity = mWeightEntity;
        int age = WeighDataParser.getCalAge(curRoleInfo, weightEntity);
        int height = WeighDataParser.getCalHeight(curRoleInfo, weightEntity);
        String sexStr = WeighDataParser.getCalSex(curRoleInfo, weightEntity);
        byte sex = (byte) (sexStr.equals("女") ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, weightEntity.getWeight(), sex, age, weightEntity.getR1());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    static class ViewHolder {
        @BindView(R2.id.iconView)
        ImageView iconView;
        @BindView(R2.id.nameView)
        TextView nameView;
        @BindView(R2.id.mLevelText)
        TextView mLevelText;
        @BindView(R2.id.mValueText)
        CustomTextView mValueText;
        @BindView(R2.id.mUnitText)
        TextView mUnitText;
        @BindView(R2.id.mLayout)
        RelativeLayout mLayout;
        @BindView(R2.id.dotted_line)
        View dottedLine;
        @BindView(R2.id.reportView)
        CustomReportView reportView;
        @BindView(R2.id.mLevelTip)
        TextView mLevelTip;
        @BindView(R2.id.mOpenDetailView)
        LinearLayout mOpenDetailView;

        IndexDataItem dataItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
