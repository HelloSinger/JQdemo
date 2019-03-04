package com.jq.btc.homePage.home.haier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.CSApplication;
import com.jq.btc.ConstantUrl;
import com.jq.btc.adapter.CookBookNoDataAdapter;
import com.jq.btc.adapter.CookBookOneAdapter;
import com.jq.btc.adapter.CookBookThreeAdapter;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.report.haier.BodyFatMoreDataActivity;
import com.jq.btc.bluettooth.report.haier.item.BuildItemsUtil;
import com.jq.btc.bluettooth.report.haier.item.IndexDataItem;
import com.jq.btc.dialog.WeightDialog;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.NetActivity;
import com.jq.btc.listener.OnDoubleClickListener;
import com.jq.btc.model.LastWeightModel;
import com.jq.btc.model.MatchModel;
import com.jq.btc.model.MenuModel;
import com.jq.btc.model.NoDataModel;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.BMToastUtil;
import com.jq.btc.utils.RecyclerViewSpacesItemDecoration;
import com.jq.btc.utils.SpUtils;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CircleImageView;
import com.jq.code.view.WeightGoalProgressBar;
import com.jq.code.view.text.CustomTextView;
import com.jq.code.view.waveview.DynamicWave2;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.growingio.android.sdk.utils.ThreadUtils.runOnUiThread;

/**
 * 正常模式的动态页
 * Created by lijh on 2017/7/19.
 */

@SuppressLint("ValidFragment")
public class NormalFragment extends BaseFragment implements View.OnClickListener, WeightDialog.DiaLogRecyItemOnClick
        , CookBookOneAdapter.OnItemClickListener, CookBookNoDataAdapter.OnItemClickListener1,
        CookBookThreeAdapter.OnItemClickListener2 {
    private static final int REQUEST_GOAL_WEIGHT_SETTINGS = 89;
    ImageView click_kitchen_scale;
    CircleImageView mRoleImage;
    TextView mTimeText;
    ImageView mShareView;
    ImageView mHistoryView;
    ImageView mBluetoothIcon;
    TextView mBluetoothStateText;
    View mIconGo;
    LinearLayout mBluetoothStateLayout;
    TextView mSetWeightGoalView;
    CustomTextView mWeightGoalText;
    CustomTextView mWeightText;
    CustomTextView mBmiText;
    TextView mBmiLevelText;
    TextView mCompareLastText;
    CustomTextView mCompareLastWeight;
    TextView mCompareLastWeightUnit;
    View mCompareGoalLayout;
    TextView mCompareGoalText;
    CustomTextView mCompareGoalWeight;
    TextView mCompareGoalWeightUnit;
    WeightGoalProgressBar mWeightGoalProgressBar;
    LinearLayout mBlueLayout;
    View mWeightUnit1;
    View mFatUnit1;
    View mBoneUnit1;
    View mMuscleUnit1;
    ImageView mToReportPage;
    View mWholeLayout;
    ImageView mAddWeight;
    View mBmiLayout;
    View mCompareLayout;
    View mHasDataBottom;
    View mNoDataIcon;
    View mNoDataBottom;
    DynamicWave2 mDynamicView;
    TextView mBmiText_one;
    TextView mBmiLevelText_one;
    TextView tv_score;
    TextView mCompareLastText_new;
    TextView mCompareLastWeight_new;
    TextView tv_tips;
    TextView tv_user_name;
    TextView tv_more_data;
    WeightDialog weightDialog;
    ImageView mTrends;
    TextView tv_add_member;
    private CsAlgoBuilder csAlgoBuilder;
    private List<IndexDataItem> mAbnormalIndexList = new ArrayList<>();
    RoleInfo roleInfo;
    private int mak;
    float weight;
    // 脂肪率
    float axunge;
    // 骨量
    float bone;
    // 肌肉
    float muscle;
    float bmi;
    NumberFormat df;
    int bmiLevel;
    private float lastWeight;
    float curWeight;
    private int position;
    private UserData userData;
    WeightEntity entity;
    WeighDataParser weighDataParser;
    BuildItemsUtil util;
    NumberFormat formatter;
    String userName;
    NoDataModel noDataModel;
    LastWeightModel lastWeightModel;
    private TextView tv_recommend, tv_recommend_two;
    private TextView tv_weight, tv_body_fat, tv_bone_weight, tv_muscle_rate;
    private TextView tv_weight_unit, tv_body_fat_unit, tv_bone_weight_unit, tv_muscle_rate_unit;

    private TextView tv_net;
    private ImageView iv_head;
    private View myView;
    //每一项指标
    private String metabolismLevel, boneLevel, muscleLevel, muscleWeightLevel, visceraLevel, waterLevel,
            waterContainLevel, ciroulentLevel, bmiLevels, axungeLevel;

    //每一项最大值
    private float metabolismMax, boneMax, muscleMax, muscleWeightMax, visceraMax, waterMax,
            waterContainMax, ciroulenMax, bmiMax, axungeMax;

    //每一项实际值
    private float metabolismValue, boneValue, muscleValue, muscleWeightValue, visceraValue, waterValue,
            waterContainValue, ciroulenValue, bmiValue, axungeValue;

    private String lastHeavy;
    String tips;
    private ViewPagerCurrentListener viewPagerCurrentListener;
    private LinearLayout ll_fragment_loading;
    private RecyclerView rcy_menu;
    private CookBookOneAdapter cookBookAdapter;
    private CookBookNoDataAdapter cookBookNoDataAdapter;
    private CookBookThreeAdapter cookBookThreeAdapter;
    RecyclerViewSpacesItemDecoration itemDecoration;
    byte sex;
    int age;
    int height;
    int score;
    int _pos;

    public interface ViewPagerCurrentListener {
        void setViewPagerCurrent(int pos);
    }

    private List<MenuModel.DataBeanX.DataBean.RecipesBean> recipes = new ArrayList<>();
    // 按照这个顺序排序显示 并上传服务器
    private ArrayList<Integer> mSortedNames = new ArrayList<>();

    {
        mSortedNames.add(WeighDataParser.StandardSet.METABOLISM.getName());
        mSortedNames.add(WeighDataParser.StandardSet.BONE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLE.getName());
        mSortedNames.add(WeighDataParser.StandardSet.MUSCLEWEIGHT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.VISCERA.getName());
        mSortedNames.add(WeighDataParser.StandardSet.WATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.CONTAINWATER.getName());
        mSortedNames.add(WeighDataParser.StandardSet.CORPULENT.getName());
        mSortedNames.add(WeighDataParser.StandardSet.BMI.getName());
        mSortedNames.add(WeighDataParser.StandardSet.AXUNGE.getName());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ViewPagerCurrentListener) {
            viewPagerCurrentListener = (ViewPagerCurrentListener) context;
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }

    /**
     * mNoDataBottom
     * 当前角色的目标体重
     */
    private float mCurRoleWeightGoal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_normal, null, false);
        mak = getActivity().getIntent().getIntExtra("mak", 0);
        mCurrentWeightUnit = StandardUtil.getWeightExchangeUnit(getContext());
        weighDataParser = WeighDataParser.create(getContext());
        Log.e("AYD", "----fragment----onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;
        initView(myView);
        Log.e("AYD", "----fragment----onViewCreated" + position);
        Log.e("AYD", "----fragment--走没走-----");
//        initValue();
        if (userData.getData().getMemberList().size() == 0) return;
        getUserLastWeight(UserUtils.get().userId(), userData.getData().getMemberList().get(0).getFamilyMemeberId());
//        WeightEntity bean = SpUtils.getInstance(getActivity()).getBean();
//        Log.e("AYD", "bean---" + bean);
    }


    private void initView(View view) {
        df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(1);
        formatter = new DecimalFormat("0.##");
        weightDialog = new WeightDialog(getActivity(), R.style.WeightDialog);
        weightDialog.setCanceledOnTouchOutside(false);
        weightDialog.setDiaLogRecyItemOnClick(this);
        tv_weight = view.findViewById(R2.id.tv_weight);
        tv_net = view.findViewById(R2.id.tv_net);
        iv_head = view.findViewById(R2.id.iv_head);
        tv_body_fat = view.findViewById(R2.id.tv_body_fat);
        tv_bone_weight = view.findViewById(R2.id.tv_bone_weight);
        tv_muscle_rate = view.findViewById(R2.id.tv_muscle_rate);
        tv_weight_unit = view.findViewById(R2.id.tv_weight_unit);
        tv_body_fat_unit = view.findViewById(R2.id.tv_body_fat_unit);
        tv_bone_weight_unit = view.findViewById(R2.id.tv_bone_weight_unit);
        tv_muscle_rate_unit = view.findViewById(R2.id.tv_muscle_unit);
        click_kitchen_scale = view.findViewById(R2.id.click_kitchen_scale);
        mRoleImage = view.findViewById(R2.id.mRoleImage);
        mTimeText = view.findViewById(R2.id.mTimeText);
        mShareView = view.findViewById(R2.id.mShareView);
        mHistoryView = view.findViewById(R2.id.mHistoryView);
        mBluetoothIcon = view.findViewById(R2.id.mBluetoothIcon);
        mBluetoothStateText = view.findViewById(R2.id.bluetooth_state_text);
        mIconGo = view.findViewById(R2.id.icon_go);
        mBluetoothStateLayout = view.findViewById(R2.id.mBluetoothStateLayout);
        mSetWeightGoalView = view.findViewById(R2.id.mSetWeightGoalView);
        mWeightGoalText = view.findViewById(R2.id.mWeightGoalText);
        mWeightText = view.findViewById(R2.id.mWeightText);
        mBmiText = view.findViewById(R2.id.mBmiText);
        mBmiLevelText = view.findViewById(R2.id.mBmiLevelText);
        mCompareLastText = view.findViewById(R2.id.mCompareLastText);
        mCompareLastWeight = view.findViewById(R2.id.mCompareLastWeight);
        mCompareLastWeightUnit = view.findViewById(R2.id.mCompareLastWeightUnit);
        mCompareGoalLayout = view.findViewById(R2.id.mCompareGoalLayout);
        mCompareGoalText = view.findViewById(R2.id.mCompareGoalText);
        mCompareGoalWeight = view.findViewById(R2.id.mCompareGoalWeight);
        mCompareGoalWeightUnit = view.findViewById(R2.id.mCompareGoalWeightUnit);
        mWeightGoalProgressBar = view.findViewById(R2.id.mWeightGoalProgressBar);
        mBlueLayout = view.findViewById(R2.id.mBlueLayout);
        mWeightUnit1 = view.findViewById(R2.id.mWeightUnit1);
        mFatUnit1 = view.findViewById(R2.id.mFatUnit1);
        mBoneUnit1 = view.findViewById(R2.id.mBoneUnit1);
        mMuscleUnit1 = view.findViewById(R2.id.mMuscleUnit1);
        mToReportPage = view.findViewById(R2.id.mToReportPage);
        mWholeLayout = view.findViewById(R2.id.mWholeLayout);
        mAddWeight = view.findViewById(R2.id.mAddWeight);
        mBmiLayout = view.findViewById(R2.id.mBmiLayout);
        mCompareLayout = view.findViewById(R2.id.mCompareLayout);
        mHasDataBottom = view.findViewById(R2.id.mHasDataBottom);
        mNoDataIcon = view.findViewById(R2.id.mNoDataIcon);
        mNoDataBottom = view.findViewById(R2.id.mNoDataBottom);
        mDynamicView = view.findViewById(R2.id.mDynamicView);
        mBmiText_one = view.findViewById(R2.id.mBmiText_one);
        mBmiLevelText_one = view.findViewById(R2.id.mBmiLevelText_one);
        tv_score = view.findViewById(R2.id.tv_score);
        mCompareLastText_new = view.findViewById(R2.id.mCompareLastText_new);
        mCompareLastWeight_new = view.findViewById(R2.id.mCompareLastWeight_new);
        tv_tips = view.findViewById(R2.id.tv_tips);
        tv_user_name = view.findViewById(R2.id.tv_user_name);
        tv_more_data = view.findViewById(R2.id.tv_more_data);
        tv_more_data.setOnClickListener(this);
        mTrends = view.findViewById(R2.id.mTrends);
        tv_add_member = view.findViewById(R2.id.tv_add_member);
        tv_recommend = view.findViewById(R2.id.tv_recommend);
        tv_recommend_two = view.findViewById(R2.id.tv_recommend_two);
        tv_add_member.setOnClickListener(this);
        ll_fragment_loading = view.findViewById(R2.id.ll_fragment_loading);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        rcy_menu = view.findViewById(R2.id.rcy_menu);
        rcy_menu.setLayoutManager(gridLayoutManager);
        cookBookAdapter = new CookBookOneAdapter(getContext());
        cookBookNoDataAdapter = new CookBookNoDataAdapter(getContext());
        cookBookThreeAdapter = new CookBookThreeAdapter(getContext());
        cookBookAdapter.setOnItemClickListener(this);
        cookBookNoDataAdapter.setOnItemClickListener1(this);
        cookBookThreeAdapter.setOnItemClickListener2(this);
        Log.e("AYD", "初始化");
        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 10);
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 20);
        itemDecoration = new RecyclerViewSpacesItemDecoration(stringIntegerHashMap);
        rcy_menu.addItemDecoration(itemDecoration);
        iv_head.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                getActivity().startActivity(new Intent(getActivity(), NetActivity.class));
            }
        }));
    }

    public void setUserData(UserData userData, int pos) {
        this.userData = userData;
        this.position = pos;
//        getUserLastWeight(UserUtils.get().userId(), userData.getData().getMemberList().get(pos).getFamilyMemeberId());
    }


    /**
     * 匹配弹窗点击事件
     *
     * @param pos
     */
    @Override
    public void itemOnClickListener(int pos) {
        _pos = pos;
        AccountEntity af = new AccountEntity();
        af.setId(100);
        af.setAccess_token("sss");
        af.setWeixin("123123");
        Account.getInstance(getActivity()).setAccountInfo(af);
        roleInfo = new RoleInfo();
        roleInfo.setSex(userData.getData().getMemberList().get(pos).getSex());
        roleInfo.setNickname(userData.getData().getMemberList().get(pos).getNickName());
        roleInfo.setHeight(Integer.parseInt(userData.getData().getMemberList().get(pos).getHeight()));
        roleInfo.setUseId(userData.getData().getMemberList().get(pos).getFamilyMemeberId());
        roleInfo.setAccount_id(195871);
        roleInfo.setBirthday(userData.getData().getMemberList().get(pos).getBirthday());
        roleInfo.setId(1993456);
        roleInfo.setCurrent_state(1);
        roleInfo.setCreate_time("2018-11-29 11:01:11");
        roleInfo.setWeight_init(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));
        roleInfo.setWeight_goal(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));
        roleInfo.setRole_type(0);
        Account.getInstance(getActivity()).setRoleInfo(roleInfo);
        Log.e("AYD", "onSuccess: " + roleInfo.toString());
        WeighDataParser.create(getActivity()).fillFatWithSmoothImpedance(entity, roleInfo);

        age = WeighDataParser.getCalAge(roleInfo, entity);
        height = WeighDataParser.getCalHeight(roleInfo, entity);
        String sexStr = WeighDataParser.getCalSex(roleInfo, entity);
        sex = (byte) (sexStr.equals("女") ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, entity.getWeight(), sex, age, entity.getR1());
        util = new BuildItemsUtil(getContext(), mCurrentWeightEntity, roleInfo, csAlgoBuilder);
        IndexDataItem boneItem = util.buildBoneItem();
        IndexDataItem muscleItem = util.buildMuscleItem();
        IndexDataItem muscleWeightItem = util.buildMuscleWeightItem();
        IndexDataItem metabolismItem = util.buildMetabolismItem();
        IndexDataItem corpulentItem = util.buildCorpulentItem();
        IndexDataItem bmiItem = util.buildBMIItem();
        IndexDataItem waterItem = util.buildWaterItem();
        IndexDataItem waterWeightItem = util.buildWaterWeightItem();
        IndexDataItem visceraItem = util.buildVisceraItem();
        IndexDataItem axungeItem = util.buildAxungeItem();
        mAbnormalIndexList.add(metabolismItem);
        mAbnormalIndexList.add(boneItem);
        mAbnormalIndexList.add(muscleItem);
        mAbnormalIndexList.add(muscleWeightItem);
        mAbnormalIndexList.add(visceraItem);
        mAbnormalIndexList.add(waterItem);
        mAbnormalIndexList.add(waterWeightItem);
        mAbnormalIndexList.add(corpulentItem);
        mAbnormalIndexList.add(bmiItem);
        mAbnormalIndexList.add(axungeItem);
//        sort(mAbnormalIndexList);
        inList(mAbnormalIndexList);
        metabolismLevel = metabolismItem.mLevelTextRes;
        boneLevel = boneItem.mLevelTextRes;
        muscleLevel = muscleItem.mLevelTextRes;
        muscleWeightLevel = muscleWeightItem.mLevelTextRes;
        visceraLevel = visceraItem.mLevelTextRes;
        waterLevel = waterItem.mLevelTextRes;
        waterContainLevel = waterWeightItem.mLevelTextRes;
        ciroulentLevel = corpulentItem.mLevelTextRes;
        bmiLevels = bmiItem.mLevelTextRes;
        axungeLevel = axungeItem.mLevelTextRes;

        metabolismMax = metabolismItem.mLevelMaxtRes;
        boneMax = boneItem.mLevelMaxtRes;
        muscleMax = muscleItem.mLevelMaxtRes;
        muscleWeightMax = muscleWeightItem.mLevelMaxtRes;
        visceraMax = visceraItem.mLevelMaxtRes;
        waterMax = waterItem.mLevelMaxtRes;
        waterContainMax = waterWeightItem.mLevelMaxtRes;
        ciroulenMax = corpulentItem.mLevelMaxtRes;
        bmiMax = bmiItem.mLevelMaxtRes;
        axungeMax = axungeItem.mLevelMaxtRes;

        metabolismValue = metabolismItem.value;
        boneValue = boneItem.value;
        muscleValue = muscleItem.value;
        muscleWeightValue = muscleWeightItem.value;
        visceraValue = visceraItem.value;
        waterValue = waterItem.value;
        waterContainValue = waterWeightItem.value;
        ciroulenValue = corpulentItem.value;
        bmiValue = bmiItem.value;
        axungeValue = axungeItem.value;

        //体重
        weight = entity.getWeight();
        // 脂肪率
        axunge = entity.getAxunge();
        // 骨量
        bone = entity.getBone();
        // 肌肉
        muscle = entity.getMuscle() / 2;

        bmi = entity.getBmi();
        userName = roleInfo.getNickname();
        bmiLevel = WeighDataParser.getBmiLevel(entity) - 1;
        mBmiText_one.setText("BMI:" + df.format(bmi));
        mBmiLevelText_one.setText(WeighDataParser.StandardSet.BMI.getStandards()[bmiLevel]);
        tv_user_name.setText(userName);
        score = 0;
        if (entity.getR1() > 0) {
            if (age > 5) {
                score = (int) csAlgoBuilder.getScore();
            }
        } else if (entity.getAxunge() > 0) {
            score = weighDataParser.calculateCode(entity, roleInfo);
        }
        if (score > 0) {
            tv_score.setText("身体得分：" + score + "分。");
        } else {
            tv_score.setText("");
        }

        if (entity.getSex() == 2) {
            tips = "海尔体脂秤采用的是生物电阻抗法来测量，当亲踩上秤时会有非常微弱的电流通过身体测到身体的电阻值，通过专业的生理算法得到相关的数据，为了追求完美的怀孕状态，我们建议你穿鞋称重，称重时尽量站在体重秤中间，保持身体平衡。";
            tv_tips.setText(tips);
        } else if (age < 18) {
            tips = "未成年人的身体处于快速变化阶段，身体成分和成年人有较大差异，对身体的测量结果有偏差，建议您仅关注体重变化，其他指标作为参考";
            tv_tips.setText(tips);
        } else {
            // 显示肥胖度的提示
            float corpulent;
            if (entity.getR1() > 0) {
                corpulent = csAlgoBuilder.getOD();
            } else {
                corpulent = CsAlgoBuilder.calOD(csAlgoBuilder.getH(), entity.getWeight(), csAlgoBuilder.getSex(),
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
            tips = getString(WeighDataParser.StandardSet.CORPULENT.getTips()[level],
                    StandardUtil.getWeightExchangeValue(getContext(), csAlgoBuilder.getBW(), "", (byte) 5) + mCurrentWeightUnit);
            tv_tips.setText(WeighDataParser.StandardSet.CORPULENT.getTips()[level]);
            tv_tips.setText(tips);
        }
        if (weight <= 0) {
            tv_weight.setText("一 一");
            tv_weight_unit.setText("");
        } else {
            tv_weight.setText(weight + "");
            tv_weight_unit.setText("KG");
        }
        if (axunge <= 0) {
            tv_body_fat.setText("一 一");
            tv_body_fat_unit.setText("");
        } else {
            tv_body_fat.setText(df.format(axunge) + "");
            tv_body_fat_unit.setText("%");
        }
        if (bone <= 0) {
            tv_bone_weight.setText("一 一");
            tv_bone_weight_unit.setText("");
        } else {
            tv_bone_weight.setText(df.format(bone) + "");
            tv_bone_weight_unit.setText("KG");
        }
        if (muscle <= 0) {
            tv_muscle_rate.setText("一 一");
            tv_muscle_rate_unit.setText("");
        } else {
            tv_muscle_rate.setText(df.format(muscle) + "");
            tv_muscle_rate_unit.setText("%");
        }

        if (weight > lastWeight) {
            lastHeavy = "你的体重比上次重了" + StandardUtil.getWeightExchangeValue(getContext(), (weight - lastWeight), "", (byte) 1) + "KG";
            mCompareLastWeight_new.setText(lastHeavy);
        } else if (weight < lastWeight) {
            lastHeavy = "你的体重比上次轻了" + StandardUtil.getWeightExchangeValue(getContext(), (lastWeight - weight), "", (byte) 1) + "KG";
            mCompareLastWeight_new.setText(lastHeavy);
        } else {
            lastHeavy = "你的体重比上次轻了0";
            mCompareLastWeight_new.setText(lastHeavy);
        }
        tv_recommend_two.setVisibility(View.GONE);
        weightDialog.setLoading(true);
        sort(mAbnormalIndexList);
//        upData(UserUtils.get().userId(), roleInfo.getUseId(),
//                SpUtils.getInstance(getContext()).getMac(),
//                SpUtils.getInstance(getContext()).getProvinceId(),
//                SpUtils.getInstance(getContext()).getProvince(),
//                SpUtils.getInstance(getContext()).getCityId(),
//                SpUtils.getInstance(getContext()).getCity(), "", "",
//                sex + "", userData.getData().getMemberList().get(pos).getBirthday() + "", height + "", score + "", weight + ""
//                , mAbnormalIndexList.get(0).valueText + "," + metabolismLevel + "," + metabolismMax + "," + metabolismValue
//                , mAbnormalIndexList.get(1).valueText + "," + boneLevel + "," + boneMax + "," + boneValue
//                , mAbnormalIndexList.get(2).valueText + "," + muscleLevel + "," + muscleMax + "," + muscleValue
//                , mAbnormalIndexList.get(3).valueText + "," + muscleWeightLevel + "," + muscleWeightMax + "," + muscleWeightValue
//                , mAbnormalIndexList.get(4).valueText + "," + visceraLevel + "," + visceraMax + "," + visceraValue
//                , mAbnormalIndexList.get(5).valueText + "," + waterLevel + "," + waterMax + "," + waterValue
//                , mAbnormalIndexList.get(6).valueText + "," + waterContainLevel + "," + waterContainMax + "," + waterContainValue
//                , mAbnormalIndexList.get(7).valueText + "," + ciroulentLevel + "," + ciroulenMax + "," + ciroulenValue
//                , mAbnormalIndexList.get(8).valueText + "," + bmiLevels + "," + bmiMax + "," + bmiValue
//                , mAbnormalIndexList.get(9).valueText + "," + axungeLevel + "," + axungeMax + "," + axungeValue + "," + lastHeavy
//                , tips);
        viewPagerCurrentListener.setViewPagerCurrent(pos);
    }

    protected void initValue() {
        if (null == getActivity()) {
            return;
        }
        roleInfo = Account.getInstance(getActivity()).getRoleInfo();
        entity = mCurrentWeightEntity;
        setWeightGoalViews();
//        int bluetoothIcon = ((HomeFragment) getParentFragment()).getBluetoothIcon();
//        setMsgLayout(bluetoothIcon);
        double weight = Double.valueOf(entity.getDisplayWeight(getContext(), mCurrentWeightUnit));
        DecimalFormat df = new DecimalFormat("#.00");//此为保留1位小数，若想保留2位小数，则填写#.00  ，以此类推
        String temp = df.format(weight);
        weight = Double.valueOf(temp);
        tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
        mactchUse(UserUtils.get().userId(), SpUtils.getInstance(getContext()).getUserid(), weight);
    }

    private void inList(List<IndexDataItem> items) {
        for (IndexDataItem item : items) {
            Log.e("上传顺序", "fragment--value--->" + item.nameRes + "--->" + item.valueText);
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
        upData(UserUtils.get().userId(), roleInfo.getUseId(),
                SpUtils.getInstance(getContext()).getMac(),
                SpUtils.getInstance(getContext()).getProvinceId(),
                SpUtils.getInstance(getContext()).getProvince(),
                SpUtils.getInstance(getContext()).getCityId(),
                SpUtils.getInstance(getContext()).getCity(), "", "",
                sex + "", userData.getData().getMemberList().get(_pos).getBirthday() + "", height + "", score + "", weight + ""
                , mAbnormalIndexList.get(0).valueText + "," + metabolismLevel + "," + metabolismMax + "," + metabolismValue
                , mAbnormalIndexList.get(1).valueText + "," + boneLevel + "," + boneMax + "," + boneValue
                , mAbnormalIndexList.get(2).valueText + "," + muscleLevel + "," + muscleMax + "," + muscleValue
                , mAbnormalIndexList.get(3).valueText + "," + muscleWeightLevel + "," + muscleWeightMax + "," + muscleWeightValue
                , mAbnormalIndexList.get(4).valueText + "," + visceraLevel + "," + visceraMax + "," + visceraValue
                , mAbnormalIndexList.get(5).valueText + "," + waterLevel + "," + waterMax + "," + waterValue
                , mAbnormalIndexList.get(6).valueText + "," + waterContainLevel + "," + waterContainMax + "," + waterContainValue
                , mAbnormalIndexList.get(7).valueText + "," + ciroulentLevel + "," + ciroulenMax + "," + ciroulenValue
                , mAbnormalIndexList.get(8).valueText + "," + bmiLevels + "," + bmiMax + "," + bmiValue
                , mAbnormalIndexList.get(9).valueText + "," + axungeLevel + "," + axungeMax + "," + axungeValue + "," + lastHeavy
                , tips);
        Log.e("上传顺序---->items", "metabolism" + mAbnormalIndexList.get(0).valueText + "," + metabolismLevel + "," + metabolismMax + "," + metabolismValue + "\n"
                + "boneWeight" + mAbnormalIndexList.get(1).valueText + "," + boneLevel + "," + boneMax + "," + boneValue + "\n"
                + "muscleRate" + mAbnormalIndexList.get(2).valueText + "," + muscleLevel + "," + muscleMax + "," + muscleValue + "\n"
                + "muscleweight" + mAbnormalIndexList.get(3).valueText + "," + muscleWeightLevel + "," + muscleWeightMax + "," + muscleWeightValue + "\n"
                + "viscealfat" + mAbnormalIndexList.get(4).valueText + "," + visceraLevel + "," + visceraMax + "," + visceraValue + "\n"
                + "water" + mAbnormalIndexList.get(5).valueText + "," + waterLevel + "," + waterMax + "," + waterValue + "\n"
                + "waterWeight" + mAbnormalIndexList.get(6).valueText + "," + waterContainLevel + "," + waterContainMax + "," + waterContainValue + "\n"
                + "obesity" + mAbnormalIndexList.get(7).valueText + "," + ciroulentLevel + "," + ciroulenMax + "," + ciroulenValue + "\n"
                + "bmi" + mAbnormalIndexList.get(8).valueText + "," + bmiLevels + "," + bmiMax + "," + bmiValue + "\n"
                + "axunge" + mAbnormalIndexList.get(9).valueText + "," + axungeLevel + "," + axungeMax + "," + axungeValue);
    }


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("AYD", "----Fragment------onResume----");
//        initView(myView);
//        if (NewMainActivity.userDataList.size() != SpUtils.getInstance(getActivity()).getListSize())
//            if (SpUtils.getInstance(getActivity()).getIsDialogAdd()) {
//                if (pos == 0) {
//                    SpUtils.getInstance(getActivity()).putListSize(NewMainActivity.userDataList.size());
//                    setFirstUserData();
//                }
//            }
        onActivityResume();
    }

    @Override
    public void onActivityResume() {
        try {
            String unitText = StandardUtil.getWeightExchangeUnit(getContext());
            if (!unitText.equals(mCurrentWeightUnit)) {
                mCurrentWeightUnit = unitText;
                // refreshUnitText();
                initValue();
                return;
            }

            Account account = Account.getInstance(getContext());
            RoleInfo roleInfo = account.getRoleInfo();
            float weightGoal = roleInfo.getWeight_goal();
            if (weightGoal != mCurRoleWeightGoal) {
                setWeightGoalViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置目标体重界面
     */
    private void setWeightGoalViews() {
        RoleInfo roleInfo = Account.getInstance(getActivity()).getRoleInfo();
        float weightGoal = roleInfo.getWeight_goal();
        mCurRoleWeightGoal = weightGoal;

        if (weightGoal > 0) {
            // 有目标体重
            mSetWeightGoalView.setVisibility(View.GONE);
            mWeightGoalText.setVisibility(View.VISIBLE);

            String displayGoalWeight = StandardUtil.getWeightExchangeValue(getContext(), weightGoal, "", (byte) 1);
            mWeightGoalText.setText(displayGoalWeight + mCurrentWeightUnit);
        } else {
            // 无目标体重
            mSetWeightGoalView.setVisibility(View.VISIBLE);
            mWeightGoalText.setVisibility(View.GONE);
        }

        WeightEntity entity = mCurrentWeightEntity;
        if (null != entity && weightGoal > 0) {
            float curWeight = entity.getWeight();
            float cur = curWeight;
            float delta = cur - weightGoal;
            if (delta > 0) {
                cur = weightGoal - delta;
            }
            float scale = cur / weightGoal;
            mWeightGoalProgressBar.setProgress((int) (scale * 100));

            if (delta > 0) {
                mCompareGoalText.setText(R.string.weight_goal_reduce);
                mCompareGoalWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), delta, "", (byte) 1));
            } else {
                mCompareGoalText.setText(R.string.weight_goal_increment);
                mCompareGoalWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (-delta), "", (byte) 1));
            }
        } else {
            mCompareGoalText.setText(R.string.weight_goal_reduce);
            mCompareGoalWeight.setText("一 一");
        }
        mCompareGoalWeightUnit.setText(mCurrentWeightUnit);
    }


    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param temp 临时体重
     */

    @Override
    public void setTempBluetoothWeight(WeightEntity temp) {
//        if (mNoDataIcon.getVisibility() == View.VISIBLE) {
//            mNoDataIcon.setVisibility(View.GONE);
//            mWeightText.setVisibility(View.VISIBLE);
//        }
//        mWeightText.setText(temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
        entity = temp;
        if (!temp.getDisplayWeight(getContext(), mCurrentWeightUnit).equals("0.00")) {
            if (weightDialog == null) return;
            weightDialog.show();
            weightDialog.setProgressVisibility(true);
            weightDialog.setMatchVisibility(false);
            weightDialog.setResultVisibility(false);
            weightDialog.setWeigth(temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
            weightDialog.setTitle("身体成分测量中...");
            SpUtils.getInstance(getActivity()).putBean(temp);
        } else {
            if (weightDialog == null) return;
            weightDialog.dismiss();
        }
        Log.e("AYD", "setTempBluetoothWeight: " + temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("tag", "----fragment--  onDestroyView");
        weightDialog = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GOAL_WEIGHT_SETTINGS && resultCode == Activity.RESULT_OK) {
            setWeightGoalViews();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.tv_add_member:
                Intent intent = new Intent();
                intent.setAction("com.unilife.fridge.app.family.add");
                startActivity(intent);
                SpUtils.getInstance(getContext()).putClick("click");
                break;
            case R2.id.tv_more_data:
                toMoreActivity();
                break;
        }
    }

    private void toMoreActivity() {
        Intent intent = new Intent(getActivity(), BodyFatMoreDataActivity.class);
        intent.putExtra("useId", userData.getData().getMemberList().get(position).getFamilyMemberId());
        intent.putExtra("useName", userData.getData().getMemberList().get(position).getNickName());
        intent.putExtra("pos", position);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_bottom_in, 0);
//        getActivity().finish();
    }

    /**
     * 获取用户最后一次称重数据
     *
     * @param famaliyId
     * @param userId
     */
    long startTime3 = System.currentTimeMillis(); //起始时间

    public void getUserLastWeight(final String famaliyId, final String userId) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("famaliyId", famaliyId);
        jsonObject.put("userId", userId);
        jsonObject.put("macId", SpUtils.getInstance(getContext()).getMac());
        Log.e("AYD", "onSuccess---->" + userId);
        OkGo.<String>post(ConstantUrl.GET_USER_LAST_WEIGHT)
                .upJson(String.valueOf(jsonObject))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("jsonObject", "---" + String.valueOf(jsonObject));
                        Log.e("jsonObject", "---" + response.body());
                        if (ll_fragment_loading == null) return;
                        ll_fragment_loading.setVisibility(View.GONE);
                        SpUtils.getInstance(getActivity()).cleanDialog();
                        lastWeightModel = new Gson().fromJson(response.body(), LastWeightModel.class);
                        if (lastWeightModel.getData() == null) {
                            if (userData.getData().getMemberList().size() == 0) return;
                            tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
                            tv_recommend.setText("为" + userData.getData().getMemberList().get(position).getNickName() + "推荐的健康菜谱");
                            tv_weight.setText("一 一");
                            tv_weight_unit.setText("");
                            tv_body_fat.setText("一 一");
                            tv_body_fat_unit.setText("");
                            tv_bone_weight.setText("一 一");
                            tv_bone_weight_unit.setText("");
                            tv_muscle_rate.setText("一 一");
                            tv_muscle_rate_unit.setText("");
                            mCompareLastWeight_new.setText(" ");
                            mBmiText_one.setText("一 一");
                            mBmiLevelText_one.setText("一 一");
                            tv_score.setText("");
                            tv_tips.setText("使用体脂秤秤重，我们会根据你的健康信息，给出健康及营养建议哟～");
                            tv_recommend_two.setVisibility(View.VISIBLE);
                            getNoDataMenu(famaliyId, userId, SpUtils.getInstance(getContext()).getMac());
                        } else {
//                            for (int i = 0; i < lastWeightModel.getData().size(); i++) {
                            //.get(lastWeightModel.getData().size() - 1);
                            lastWeight = Float.parseFloat(lastWeightModel.getData().getWeight());
                            String boneWeight = lastWeightModel.getData().getBoneWeight();
                            String[] boneWeightArray = boneWeight.split(",");
                            String muscleRate = lastWeightModel.getData().getMuscleRate();
                            String[] muscleRateArray = muscleRate.split(",");
                            String bmi = lastWeightModel.getData().getBmi();
                            String[] bmiArray = bmi.split(",");
                            String score = lastWeightModel.getData().getScore();
                            String axunge = lastWeightModel.getData().getBody();
                            String[] axungeArray = axunge.split(",");
                            if (userData.getData().getMemberList().size() == 0) return;
                            tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
                            tv_recommend.setText("为" + userData.getData().getMemberList().get(position).getNickName() + "推荐的健康菜谱");
                            tv_recommend_two.setVisibility(View.GONE);
                            tv_weight.setText(lastWeight + "");
                            tv_weight_unit.setText("KG");
                            if (axungeArray[0].equals("0") || axungeArray[0].equals("0.0") || axungeArray[0].equals("-1%")) {
                                tv_body_fat.setText("一 一");
                            } else {
                                tv_body_fat.setText(axungeArray[0].substring(0, axungeArray[0].length() - 1));
                                tv_body_fat_unit.setText("%");
                            }
                            if (boneWeightArray[0].equals("0") || boneWeightArray[0].equals("0.0")) {
                                tv_bone_weight.setText("一 一");
                            } else {
                                tv_bone_weight.setText(boneWeightArray[0]);
                                tv_bone_weight_unit.setText("KG");
                            }
                            if (muscleRateArray[0].equals("0") || muscleRateArray[0].equals("0.0") || muscleRateArray[0].equals("0%")) {
                                tv_muscle_rate.setText("一 一");
                            } else {
                                tv_muscle_rate.setText(muscleRateArray[0].substring(0, muscleRateArray[0].length() - 1));
                                tv_muscle_rate_unit.setText("%");
                            }
                            mCompareLastWeight_new.setText(axungeArray[4] + "。");
//                            bmiLevel = WeighDataParser.getBmiLevel(entity) - 1;
                            mBmiText_one.setText("BMI:" + bmiArray[0]);
                            mBmiLevelText_one.setText(bmiArray[1]);
                            tv_score.setText("身体得分：" + score + "分。");
                            tv_tips.setText(lastWeightModel.getData().getHealith());
                            Log.e("AYD", "getRecipes().size()--->" + lastWeightModel.getData().getRecipes().size());
                            cookBookThreeAdapter.setMenuModels(lastWeightModel.getData().getRecipes());
                            rcy_menu.setAdapter(cookBookThreeAdapter);
                            long endTime = System.currentTimeMillis(); //结束时间
                            long runTime = endTime - startTime3;
                            Log.e("time", "获取最后一次执行时间" + String.format("方法使用时间 %d ms", runTime));
//                        }
                        }
                    }
                });
    }

    /**
     * 上传体脂数据
     */
    long startTime = System.currentTimeMillis(); //起始时间

    private void upData(final String famaliyId, final String userId, String mac_id, String province_id, String province_name,
                        String city_id, String city_name, String county_id, String county_name, String sex, String age, String height,
                        String score, final String weight, String metabolism, String boneWeight, String muscleRate, String muscleWeight,
                        String visceralFat, String water, String waterWeight, String obesity, final String bmi, final String axunge, String tips) {
        final JSONObject json = new JSONObject();
        json.put("famaliyId", famaliyId);
        json.put("userId", userId);
        json.put("macId", mac_id);
        json.put("provinceId", province_id);
        json.put("provinceName", province_name);
        json.put("cityId", city_id);
        json.put("cityName", city_name);
        json.put("countyId", county_id);
        json.put("countyName", county_name);
        json.put("sex", sex);
        json.put("age", age);
        json.put("height", height);
        json.put("score", score);
        json.put("weight", weight);
        json.put("metabolism", metabolism);
        json.put("boneWeight", boneWeight);
        json.put("muscleRate", muscleRate);
        json.put("muscleWeight", muscleWeight);
        json.put("visceralFat", visceralFat);
        json.put("water", water);
        json.put("waterWeight", waterWeight);
        json.put("obesity", obesity);
        json.put("bmi", bmi);
        json.put("body", axunge);
        json.put("healith", tips);
        Log.e("AYD", "json" + String.valueOf(json));
        OkGo.<String>post(ConstantUrl.UPDATA_URL)
                .upJson(String.valueOf(json))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        MenuModel menuModel = new Gson().fromJson(response.body(), MenuModel.class);
                        if (SpUtils.getInstance(getActivity()).getIsDialogAdd()) {
                            Log.e("AYD", "json" + String.valueOf(json));
                            weightDialog.dismiss();
                            getUserLastWeight(famaliyId, userId);
                        } else {
                            Log.e("AYD", "response" + response.body());
                            if (menuModel.getCode().equals("200")) {
                                Log.e("AYD", "updateSuccess");
                                if (weightDialog == null) return;
                                weightDialog.setLoading(false);
                                weightDialog.setResultVisibility(true);
                                weightDialog.setProgressVisibility(false);
                                weightDialog.setMatchVisibility(false);
                                weightDialog.setTitle("海尔智能体脂秤-Q81");
                                String[] axungeArray = axunge.split(",");
                                weightDialog.setPersonlData("BMI: " + entity.getBmi(), userName, weight,
                                        axungeArray[0] + "", df.format(bone) + "", df.format(muscle) + "");
                                if (recipes == null) return;
                                recipes = menuModel.getData().getData().getRecipes();
                                cookBookAdapter.setMenuModels(menuModel.getData().getData().getRecipes());
                                rcy_menu.setAdapter(cookBookAdapter);
                                long endTime = System.currentTimeMillis(); //结束时间
                                long runTime = endTime - startTime;
                                Log.e("time", "上传数据执行时间" + String.format("方法使用时间 %d ms", runTime));
                            } else {
                                weightDialog.dismiss();
                                weightDialog.setLoading(false);
                                BMToastUtil.showToastShort(getActivity(), "上传数据失败");
                            }
                        }
                    }
                });
    }


    /**
     * 匹配查询家庭成员
     *
     * @param famaliyId
     * @param userIds
     * @param weight
     */
    private void mactchUse(String famaliyId, String userIds, final double weight) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("famaliyId", famaliyId);
        jsonObject.put("userIds", userIds);
        jsonObject.put("weight", weight);
        JSONObject json = new JSONObject();
        json.put("data", jsonObject);
        OkGo.<String>post(ConstantUrl.MATCH_MEMBERS_URL)
                .upJson(String.valueOf(json))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        MatchModel matchModel = new Gson().fromJson(response.body(), MatchModel.class);
                        if (weightDialog == null) return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //此时已在主线程中，更新UI
                                weightDialog.setUserData(userData);
                                weightDialog.setMatchVisibility(true);
                                weightDialog.setProgressVisibility(false);
                                String wei = formatter.format(weight);
                                weightDialog.setMatchWeight(wei);
                                weightDialog.setResultVisibility(false);
                                weightDialog.setTitle("未匹配到家庭成员");
                            }
                        });
                    }
                });
    }

    long startTime1 = System.currentTimeMillis(); //起始时间

    private void getNoDataMenu(String famaliyId, String userId, String macId) {
        final JSONObject json = new JSONObject();
        json.put("famaliyId", famaliyId);
        json.put("userId", userId);
        json.put("macId", macId);
        OkGo.<String>post(ConstantUrl.GET_NO_DATA_EMNU)
                .upJson(String.valueOf(json))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("shuju", "--->" + response.body());
                        try {
                            noDataModel = new Gson().fromJson(response.body(), NoDataModel.class);
                        } catch (Exception e) {
                            Log.e("数据解析", "" + e);
                        }
                        if (noDataModel.getData() == null) return;
                        cookBookNoDataAdapter.setMenuModels(noDataModel.getData().getRecipes());
                        rcy_menu.setAdapter(cookBookNoDataAdapter);
                        long endTime = System.currentTimeMillis(); //结束时间
                        long runTime = endTime - startTime1;
                        Log.e("time", "冷启动获取数据执行时间" + String.format("方法使用时间 %d ms", runTime));
                    }
                });
    }


    @Override
    public void setItemClickListener(int pos) {
        if (recipes == null) return;
        ComponentName componetName = new ComponentName(
                "com.unilife.fridge.haierbase.recipe",
                "com.unilife.fridge.haierbase.um_library_recipe.activity.RecipeDetailsActivity");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", recipes.get(pos).getRecipename());
        bundle.putString("pageSource", "firstRecipe");
        bundle.putString("recipeId", recipes.get(pos).getRecipeid());
        bundle.putString("recipeSource", "douguo");
        intent.putExtras(bundle);
        intent.setComponent(componetName);
        getActivity().startActivity(intent);
//        SpUtils.getInstance(getContext()).putPos(position);
    }

    @Override
    public void setItemClickListener1(int pos) {
        if (noDataModel.getData() == null) return;
        ComponentName componetName = new ComponentName(
                "com.unilife.fridge.haierbase.recipe",
                "com.unilife.fridge.haierbase.um_library_recipe.activity.RecipeDetailsActivity");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", noDataModel.getData().getRecipes().get(pos).getRecipename());
        bundle.putString("pageSource", "firstRecipe");
        bundle.putString("recipeId", noDataModel.getData().getRecipes().get(pos).getRecipeid());
        bundle.putString("recipeSource", "douguo");
        intent.putExtras(bundle);
        intent.setComponent(componetName);
        getActivity().startActivity(intent);
//        SpUtils.getInstance(getContext()).putPos(position);
    }

    @Override
    public void setItemClickListener2(int pos) {
        if (lastWeightModel.getData() == null) return;
        ComponentName componetName = new ComponentName(
                "com.unilife.fridge.haierbase.recipe",
                "com.unilife.fridge.haierbase.um_library_recipe.activity.RecipeDetailsActivity");
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("recipe_name", lastWeightModel.getData().getRecipes().get(pos).getRecipename());
        bundle.putString("pageSource", "firstRecipe");
        bundle.putString("recipeId", lastWeightModel.getData().getRecipes().get(pos).getRecipeid());
        bundle.putString("recipeSource", "douguo");
        intent.putExtras(bundle);
        intent.setComponent(componetName);
        getActivity().startActivity(intent);
//        SpUtils.getInstance(getContext()).putPos(position);
    }
}
