package com.jq.btc.bluettooth.report.haier;

import android.annotation.SuppressLint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.ConstantUrl;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.report.haier.item.BuildItemsUtil;
import com.jq.btc.bluettooth.report.haier.item.IndexDataItem;
import com.jq.btc.bluettooth.report.haier.item.NewBuidItemUtil;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.home.utils.ScrollJudge;
import com.jq.btc.model.MoreDataModel;
import com.jq.btc.myview.CircleIndicatorView;
import com.jq.btc.myview.ScrollChartView;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CircleImageView;
import com.jq.code.view.CustomReportView;
import com.jq.code.view.text.CustomTextView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    @BindView(R2.id.mScore1)
    TextView mScore;
    @BindView(R2.id.t2)
    TextView t2;
    @BindView(R2.id.mBodily)
    CustomTextView mBodily;
    @BindView(R2.id.mRoleImage1)
    CircleImageView mRoleImage;
    @BindView(R2.id.mRoleName1)
    TextView mRoleName;
    @BindView(R2.id.mWeightTime1)
    TextView mWeightTime;
    @BindView(R2.id.mAbNormalHint)
    TextView mAbNormalHint;
    @BindView(R2.id.mAbNormalIndexLayout1)
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
    @BindView(R2.id.tv_weight)
    TextView tv_weight;
    @BindView(R2.id.iv_back)
    ImageView iv_back;
    private WeightEntity mWeightEntity;

    @BindView(R2.id.scroll_chart_main)
    ScrollChartView scroll_chart_main;
    @BindView(R2.id.civ_main)
    CircleIndicatorView civ_main;
    @BindView(R2.id.lv_body_fat_data)
    ListView lv_body_fat_data;

    private String useId;

    private NewBuidItemUtil newBuidItemUtil;
    private List<MoreDataModel.DataBean> data;

    public void setWeightEntity(WeightEntity weightEntity) {
        mWeightEntity = weightEntity;
    }

    private String mCurrentWeightUnit;
    private CsAlgoBuilder csAlgoBuilder;
    private ArrayList<Integer> mSortedNames = new ArrayList<>();

    {
        // 按照这个顺序排序显示
        mSortedNames.add(WeighDataParser.StandardSet.METABOLISM.getName());
        mSortedNames.add(WeighDataParser.StandardSet.BONE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLEWEIGHT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.VISCERA.getName());
        mSortedNames.add(WeighDataParser.StandardSet.WATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.CONTAINWATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.CORPULENT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.BMI.getName());

    }

    /**
     * 不正常指标
     */
    private List<IndexDataItem> mAbnormalIndexList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_index, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        useId = bundle.getString("useId");
        mCurrentWeightUnit = StandardUtil.getWeightExchangeUnit(getContext());

        buildSuanfa();

        mBgLayout.setPadding(0, ScreenUtils.getStatusBarHeight(getActivity()), 0, 0);
        initView();
        initValue();
        handler.sendEmptyMessageDelayed(0, 1000);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final List<String> timeList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                timeList.add(i + "");
            }

            final List<Double> dataList = new ArrayList<>();
            for (int i = 0; i < 50; i++) {
                dataList.add((double) new Random().nextInt(200));
            }
            scroll_chart_main.setData(timeList, dataList);
            scroll_chart_main.setOnScaleListener(new ScrollChartView.OnScaleListener() {
                @Override
                public void onScaleChanged(int position) {
                    mScore.setText(timeList.get(position));
                    Log.e("AYD", "" + timeList.get(position));
                    tv_weight.setText(dataList.get(position) + "");
                    ScrollChartView.Point point = scroll_chart_main.getList().get(position);
                    civ_main.setCircleY(point.y);

                }
            });

            //滚动到目标position
            scroll_chart_main.smoothScrollTo(dataList.size() - 1);
        }
    };

    private void initView() {
        scroll_chart_main.setLineType(ScrollChartView.LineType.LINE);
        scroll_chart_main.invalidateView();
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void initValue() {
        getMoreData(UserUtils.get().userId(), useId);
        RoleInfo roleInfo = Account.getInstance(getContext()).getRoleInfo();
        WeighDataParser weighDataParser = WeighDataParser.create(getContext());
        WeightEntity weightEntity = mWeightEntity;
        tv_weight.setText(weightEntity.getWeight() + "KG");
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date weightDate = sdf.parse(weightTime);
            // "04月25日 13:25"
            SimpleDateFormat textFormat = new SimpleDateFormat("yyyy年MM月dd日");
            String text = textFormat.format(weightDate);
            mWeightTime.setText(text);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        newBuidItemUtil = new NewBuidItemUtil(getContext(), roleInfo, csAlgoBuilder);
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

        judgeItemAndAdd(boneItem, WeighDataParser.StandardSet.BONE);
        judgeItemAndAdd(muscleItem, WeighDataParser.StandardSet.MUSCLE);
        judgeItemAndAdd(muscleWeightItem, WeighDataParser.StandardSet.MUSCLEWEIGHT);
        judgeItemAndAdd(metabolismItem, WeighDataParser.StandardSet.METABOLISM);

        mAbnormalIndexList.add(corpulentItem);
        mAbnormalIndexList.add(bmiItem);

        IndexDataItem waterItem = util.buildWaterItem();
        IndexDataItem waterWeightItem = util.buildWaterWeightItem();
        IndexDataItem proteinItem = util.buildProteinItem();
        IndexDataItem visceraItem = util.buildVisceraItem();
        IndexDataItem bodyAgeItem = util.buildBodyAgeItem();

        judgeItemAndAdd(visceraItem, WeighDataParser.StandardSet.VISCERA);
        judgeItemAndAdd(waterItem, WeighDataParser.StandardSet.WATER);
        judgeItemAndAdd(waterWeightItem, WeighDataParser.StandardSet.CONTAINWATER);

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
            case VISCERA://内脏脂肪
                normal = item.level == 0;
                break;
            case WATER://水份
                normal = item.level >= 1;
                break;
            case CONTAINWATER://水含量
                normal = item.level >= 1;
                break;
            case BONE://骨量
                normal = item.level >= 1;
                break;
            case MUSCLE://肌肉率
                normal = item.level >= 1;
                break;
            case MUSCLEWEIGHT://肌肉重量
                normal = item.level >= 1;
                break;
            case METABOLISM://基础代谢
//                normal = item.level == 1;
                break;
        }

        mAbnormalIndexList.add(item);
//        }
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

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inflateViews(List<IndexDataItem> items, LinearLayout parent) {
        float density = getResources().getDisplayMetrics().density;
        View line = new View(getContext());
        line.setBackgroundColor(0xfff0f0f5);
        parent.addView(line, LinearLayout.LayoutParams.MATCH_PARENT, (int) (0.5 * density));
        for (IndexDataItem item : items) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_haier_index_item, parent, false);
            ViewHolder holder = new ViewHolder(view);

            holder.dataItem = item;
//            holder.iconView.setImageResource(item.mIconRes);
            holder.nameView.setText(item.nameRes);
            holder.mValueText.setText(item.valueText);
            holder.mUnitText.setText(item.mUnitText);
            holder.progressBar.setMax((int) item.mLevelMaxtRes);
            float j = 544 * (item.value / item.criticalValue);
            Log.e("AYD", "value--->0" + item.nameRes + "--->" + item.valueText + "--->" + item.value + "--->" +
                    item.criticalValue + "--->" + item.mLevelMaxtRes + "--->" + j);

            holder.progressBar.setProgress((int) item.value);
            GradientDrawable gd;
            gd = (GradientDrawable) holder.mLevelText.getBackground();
            gd.setColor(getResources().getColor(item.mLevelColorRes));


            LayerDrawable drawable = (LayerDrawable) holder.progressBar.getProgressDrawable();
            GradientDrawable gradientDrawable = new GradientDrawable();
            //这里设置后台传过来的颜色
            gradientDrawable.setColor(getResources().getColor(item.mLevelColorRes));
            ClipDrawable clipDrawable = new ClipDrawable(gd, Gravity.START, ClipDrawable.HORIZONTAL);
            drawable.setDrawableByLayerId(R2.id.progressBar, clipDrawable);
//            holder.progressBar.setProgressDrawableTiled(gd);
            if (-1 != item.mLevelTextRes) {
                holder.mLevelText.setVisibility(View.VISIBLE);
                holder.mLevelText.setText(item.mLevelTextRes);
                Log.e("AYD", "item--->" + item.mLevelTextRes);
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

                if (holder.reportView.getColors() == null) {
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
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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
        TextView mValueText;
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
        @BindView(R2.id.progressBar)
        ProgressBar progressBar;
        IndexDataItem dataItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 请求个人每一天的数据
     *
     * @param famaliyId
     * @param userId
     */
    private void getMoreData(String famaliyId, String userId) {
        final IndexDataItem indexDataItem = new IndexDataItem();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("famaliyId", famaliyId);
        jsonObject.put("userId", userId);
        OkGo.<String>post(ConstantUrl.GET_CHARTLINE_DATA_URL)
                .upJson(String.valueOf(jsonObject))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        MoreDataModel moreDataModel = new Gson().fromJson(response.body(), MoreDataModel.class);
                        data = moreDataModel.getData();
                        Log.e("AYD", "--->" + response.body());
                        Log.e("AYD", "" + newBuidItemUtil.buildVisceraItem(Float.parseFloat(data.get(0).getVisceralFat())));

                        Log.e("ADY", "an" + indexDataItem.mLevelTextRes);
                    }
                });

    }
}
