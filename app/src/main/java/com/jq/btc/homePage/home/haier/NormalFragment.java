package com.jq.btc.homePage.home.haier;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.jq.btc.bluettooth.BLEController;
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
import com.jq.btc.model.UsersDataBean;
import com.jq.btc.utils.BMToastUtil;
import com.jq.btc.utils.NetWorkUtils;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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

    private RelativeLayout rl_data;
    private LinearLayout ll_no_net;
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

    private LinearLayout ll_menu;
    private RelativeLayout ll_failed;

    private UsersDataBean usersDataBean;


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
        Log.e("fragment", "onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("fragment", "onViewCreated");
        myView = view;
        initView(view);
        mCompareLastWeight_new.setVisibility(View.GONE);
//        initValue();
        if (!NetWorkUtils.isNetworkAvailable(getContext())) {
            BMToastUtil.showToastShort(getContext(), "当前无网络");
        } else {
//            Log.e("AYDfragment", userData.getData() + "");
            try {
                if (usersDataBean.getData() == null) return;
//                getUserLastWeight(UserUtils.get().userId(), userData.getData().getMemberList().get(0).getFamilyMemeberId());
                getUserLastWeight(usersDataBean.getData().get(0).getGroupId()
                        , usersDataBean.getData().get(0).getMemberInfoId() + "");
            } catch (Exception e) {
                Log.e("AYD", "onViewCreated: " + e);
            }
        }
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
        ll_failed = view.findViewById(R2.id.ll_failed);
        ll_menu = view.findViewById(R2.id.ll_menu);
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

    public void setUserBean(UsersDataBean userBean, int pos) {
        this.usersDataBean = userBean;
        this.position = pos;
    }


    /**
     * 匹配弹窗点击事件
     *
     * @param pos
     */
    @Override
    public void itemOnClickListener(int pos) {
        _pos = pos;
//        AccountEntity af = new AccountEntity();
//        af.setId(100);
//        af.setAccess_token("sss");
//        af.setWeixin("123123");
//        Account.getInstance(getActivity()).setAccountInfo(af);
        roleInfo = new RoleInfo();
//        roleInfo.setSex(userData.getData().getMemberList().get(pos).getSex());
//        roleInfo.setNickname(userData.getData().getMemberList().get(pos).getNickName());
//        roleInfo.setHeight(Integer.parseInt(userData.getData().getMemberList().get(pos).getHeight()));
//        roleInfo.setUseId(userData.getData().getMemberList().get(pos).getFamilyMemeberId());
//        roleInfo.setBirthday(userData.getData().getMemberList().get(pos).getBirthday());
        int sex = usersDataBean.getData().get(pos).getSex();
        if (sex == 1) {
            roleInfo.setSex("男");
        } else {
            roleInfo.setSex("女");
        }
        roleInfo.setNickname(usersDataBean.getData().get(pos).getNickName());
        roleInfo.setHeight((int) usersDataBean.getData().get(pos).getHeight());
        roleInfo.setUseId(usersDataBean.getData().get(pos).getMemberInfoId() + "");
        roleInfo.setBirthday(stampToDate(String.valueOf(usersDataBean.getData().get(pos).getBirthday())));

        Log.e("Sr", stampToDate(String.valueOf(usersDataBean.getData().get(pos).getBirthday())) + "");
        roleInfo.setAccount_id(195871);
        roleInfo.setId(1993456);
        roleInfo.setCurrent_state(1);
        roleInfo.setCreate_time("2018-11-29 11:01:11");
//        roleInfo.setWeight_init(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));
//        roleInfo.setWeight_goal(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));

        roleInfo.setWeight_init((float) usersDataBean.getData().get(pos).getWeight());
        roleInfo.setWeight_goal((float) usersDataBean.getData().get(pos).getWeight());

        roleInfo.setRole_type(0);
//        Account.getInstance(getActivity()).setRoleInfo(roleInfo);
        Log.e("AYD", "onSuccess: " + roleInfo.toString());
        WeighDataParser.create(getActivity()).fillFatWithSmoothImpedance(entity, roleInfo);
        age = WeighDataParser.getCalAge(roleInfo, entity);
        height = WeighDataParser.getCalHeight(roleInfo, entity);
        String sexStr = WeighDataParser.getCalSex(roleInfo, entity);
        this.sex = (byte) (sexStr.equals("女") ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, entity.getWeight(), this.sex, age, entity.getR1());
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
        mAbnormalIndexList.clear();
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

//        if (lastWeight == 0.0) {
//            lastHeavy = " ";
//            mCompareLastWeight_new.setText(lastHeavy);
////            lastWeight = weight;
//        } else {
//            Log.e("Ayd", "las" + lastWeight);
//            mCompareLastWeight_new.setVisibility(View.VISIBLE);
//            if (weight > SpUtils.getInstance(getContext()).getLastWeight()) {
//                lastHeavy = "你的体重比上次重了" + StandardUtil.getWeightExchangeValue(getContext(), (weight - SpUtils.getInstance(getContext()).getLastWeight()), "", (byte) 1) + "KG";
//                mCompareLastWeight_new.setText(lastHeavy);
////            }
//            } else if (weight < SpUtils.getInstance(getContext()).getLastWeight()) {
//                lastHeavy = "你的体重比上次轻了" + StandardUtil.getWeightExchangeValue(getContext(), (SpUtils.getInstance(getContext()).getLastWeight() - weight), "", (byte) 1) + "KG";
//                mCompareLastWeight_new.setText(lastHeavy);
//            } else if (weight == SpUtils.getInstance(getContext()).getLastWeight()) {
//                lastHeavy = "你的体重比上次轻了0";
//                mCompareLastWeight_new.setText(lastHeavy);
//            }
//        }
        tv_recommend_two.setVisibility(View.GONE);
        weightDialog.setLoading(true);
        inList(mAbnormalIndexList);
        sort(mAbnormalIndexList);

    }

    protected void initValue() {
        if (null == getActivity()) {
            return;
        }
        roleInfo = Account.getInstance(getActivity()).getRoleInfo();
        entity = mCurrentWeightEntity;
//        setWeightGoalViews();
//        int bluetoothIcon = ((HomeFragment) getParentFragment()).getBluetoothIcon();
//        setMsgLayout(bluetoothIcon);
        double weight = Double.valueOf(entity.getDisplayWeight(getContext(), mCurrentWeightUnit));
        DecimalFormat df = new DecimalFormat("#.00");//此为保留1位小数，若想保留2位小数，则填写#.00  ，以此类推
        String temp = df.format(weight);
        weight = Double.valueOf(temp);
//        tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
        tv_user_name.setText(usersDataBean.getData().get(position).getNickName());
        matchDialog(weight);
//        mactchUse(UserUtils.get().userId(), SpUtils.getInstance(getContext()).getUserid(), weight);
    }

    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    private void matchDialog(double weight) {
//        weightDialog.setUserData(userData);
        weightDialog.setUsersDataBean(usersDataBean);
        weightDialog.setMatchVisibility(true);
        weightDialog.setProgressVisibility(false);
        String wei = formatter.format(weight);
        weightDialog.setMatchWeight(wei);
        weightDialog.setResultVisibility(false, false);
        weightDialog.setTitle("未匹配到家庭成员");
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
        upData(/*UserUtils.get().userId()*/usersDataBean.getData().get(_pos).getGroupId(), roleInfo.getUseId(),
                SpUtils.getInstance(getContext()).getMac(),
                SpUtils.getInstance(getContext()).getProvinceId(),
                SpUtils.getInstance(getContext()).getProvince(),
                SpUtils.getInstance(getContext()).getCityId(),
                SpUtils.getInstance(getContext()).getCity(), "", "",
                sex + "", /*userData.getData().getMemberList().get(_pos).getBirthday()*/
                stampToDateNO(String.valueOf(usersDataBean.getData().get(_pos).getBirthday())) + "", height + "", score + "", weight + ""
                , items.get(0).valueText + "," + metabolismLevel + "," + metabolismMax + "," + metabolismValue
                , items.get(1).valueText + "," + boneLevel + "," + boneMax + "," + boneValue
                , items.get(2).valueText + "," + muscleLevel + "," + muscleMax + "," + muscleValue
                , items.get(3).valueText + "," + muscleWeightLevel + "," + muscleWeightMax + "," + muscleWeightValue
                , items.get(4).valueText + "," + visceraLevel + "," + visceraMax + "," + visceraValue
                , items.get(5).valueText + "," + waterLevel + "," + waterMax + "," + waterValue
                , items.get(6).valueText + "," + waterContainLevel + "," + waterContainMax + "," + waterContainValue
                , items.get(7).valueText + "," + ciroulentLevel + "," + ciroulenMax + "," + ciroulenValue
                , items.get(8).valueText + "," + bmiLevels + "," + bmiMax + "," + bmiValue
                , items.get(9).valueText + "," + axungeLevel + "," + axungeMax + "," + axungeValue + "," + lastHeavy
                , tips);
        Log.e("上传顺序---->items", "\n" + "metabolism:" + mAbnormalIndexList.get(0).valueText + "," + metabolismLevel + "," + metabolismMax + "," + metabolismValue + "\n"
                + "boneWeight:" + mAbnormalIndexList.get(1).valueText + "," + boneLevel + "," + boneMax + "," + boneValue + "\n"
                + "muscleRate:" + mAbnormalIndexList.get(2).valueText + "," + muscleLevel + "," + muscleMax + "," + muscleValue + "\n"
                + "muscleweight:" + mAbnormalIndexList.get(3).valueText + "," + muscleWeightLevel + "," + muscleWeightMax + "," + muscleWeightValue + "\n"
                + "viscealfat:" + mAbnormalIndexList.get(4).valueText + "," + visceraLevel + "," + visceraMax + "," + visceraValue + "\n"
                + "water:" + mAbnormalIndexList.get(5).valueText + "," + waterLevel + "," + waterMax + "," + waterValue + "\n"
                + "waterWeight:" + mAbnormalIndexList.get(6).valueText + "," + waterContainLevel + "," + waterContainMax + "," + waterContainValue + "\n"
                + "obesity:" + mAbnormalIndexList.get(7).valueText + "," + ciroulentLevel + "," + ciroulenMax + "," + ciroulenValue + "\n"
                + "bmi:" + mAbnormalIndexList.get(8).valueText + "," + bmiLevels + "," + bmiMax + "," + bmiValue + "\n"
                + "axunge:" + mAbnormalIndexList.get(9).valueText + "," + axungeLevel + "," + axungeMax + "," + axungeValue);
    }

    private void sort1(List<IndexDataItem> items) {
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

    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragment", "onResume");
        initViews(myView);
        onActivityResume();
    }

    private void initViews(View view) {
        Log.e("onResume", "初始化");
        df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(1);
        formatter = new DecimalFormat("0.##");
//        weightDialog = new WeightDialog(getActivity(), R.style.WeightDialog);
//        weightDialog.setCanceledOnTouchOutside(false);
//        weightDialog.setDiaLogRecyItemOnClick(this);
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
        ll_failed = view.findViewById(R2.id.ll_failed);
        ll_menu = view.findViewById(R2.id.ll_menu);
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
        iv_head.setOnTouchListener(new OnDoubleClickListener(new OnDoubleClickListener.DoubleClickCallback() {
            @Override
            public void onDoubleClick() {
                getActivity().startActivity(new Intent(getActivity(), NetActivity.class));
            }
        }));
    }

    @Override
    public void onActivityResume() {
        Log.e("fragment", "onActivityResume:");
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
//                setWeightGoalViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        BLEController mBleController = BLEController.create(getContext());
        Log.e("AYD", mBleController.isBluetoothEnable() + "");
        entity = temp;
        if (!temp.getDisplayWeight(getContext(), mCurrentWeightUnit).equals("0.00")) {
            if (weightDialog == null) return;
            weightDialog.show();
            weightDialog.setProgressVisibility(true);
            weightDialog.setMatchVisibility(false);
            weightDialog.setResultVisibility(false, false);
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
        Log.d("fragment", "onDestroyView");
        weightDialog = null;
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_GOAL_WEIGHT_SETTINGS && resultCode == Activity.RESULT_OK) {
////            setWeightGoalViews();
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }


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
//        intent.putExtra("useId", userData.getData().getMemberList().get(position).getFamilyMemberId());
//        intent.putExtra("useName", userData.getData().getMemberList().get(position).getNickName());
        intent.putExtra("famaliyId", usersDataBean.getData().get(position).getGroupId());
        intent.putExtra("useId", usersDataBean.getData().get(position).getMemberInfoId() + "");
        intent.putExtra("useName", usersDataBean.getData().get(position).getNickName());
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
                        Log.e("最后一次数据", "--->" + response.body() + "\n"
                                + "参数--->:" + String.valueOf(jsonObject) + "\n"
                                + "URL--->:" + ConstantUrl.GET_USER_LAST_WEIGHT);
                        try {
                            lastWeightModel = new Gson().fromJson(response.body(), LastWeightModel.class);

                            if (lastWeightModel.getData().getWeight() == null) {
//                                if (userData.getData().getMemberList().size() == 0) return;
                                if (usersDataBean.getData().size() == 0) return;
//                                tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
//                                tv_recommend.setText("为" + userData.getData().getMemberList().get(position).getNickName() + "推荐的健康菜谱");
                                ll_fragment_loading.setVisibility(View.GONE);
                                tv_user_name.setText(usersDataBean.getData().get(position).getNickName());
                                tv_recommend.setText("为" + usersDataBean.getData().get(position).getNickName() + "推荐的健康菜谱");

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
//                                getNoDataMenu(famaliyId, userId, SpUtils.getInstance(getContext()).getMac());

                                lastWeightModel.getData().getRecipes().get(0).setRecipename("蜂蜜烤面包片");
                                lastWeightModel.getData().getRecipes().get(0).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae50169c23424a90f84.jpg");
                                lastWeightModel.getData().getRecipes().get(0).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(0).setRecipeid("22298");

                                //晶钻Q3
                                lastWeightModel.getData().getRecipes().get(1).setRecipename("南瓜银耳莲子羹");
                                lastWeightModel.getData().getRecipes().get(1).setRecipeimage("http://eco.haier.com/group1/M00/01/4A/Cp8ljlrRYOGAZ41QAABcpeZb5QM363.jpg");
                                lastWeightModel.getData().getRecipes().get(1).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(1).setRecipeid("22209");

                                //OBT600-8GU1
                                lastWeightModel.getData().getRecipes().get(4).setRecipename("蒜蓉扇贝");
                                lastWeightModel.getData().getRecipes().get(4).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae48183693374af0169946d1e910a38.jpg");
                                lastWeightModel.getData().getRecipes().get(4).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(4).setRecipeid("22285");

                                //OBT600-8GU1
                                lastWeightModel.getData().getRecipes().get(5).setRecipename("锡纸金针菇");
                                lastWeightModel.getData().getRecipes().get(5).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae5016a04c0c3da1690.jpg");
                                lastWeightModel.getData().getRecipes().get(5).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(5).setRecipeid("22313");

                                //馨厨V8
//                                lastWeightModel.getData().getRecipes().get(6).setRecipename("菠萝蔬菜饮");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipeimage("http://eco.haier.com/group1/M00/02/7A/Cp8ljlwkhW2AXyaXAAHSMPgSzWI199.png");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipetag("智慧菜谱");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipeid("22238");
                                lastWeightModel.getData().getRecipes().get(6).setRecipename("西芹彩椒思慕雪");
                                lastWeightModel.getData().getRecipes().get(6).setRecipeimage("http://data.haier.net/oss/M00/00/0E/Csd_llzvVgqABdWpAAEAa2x5YyM321.jpg");
                                lastWeightModel.getData().getRecipes().get(6).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(6).setRecipeid("22205");


                                //
                                lastWeightModel.getData().getRecipes().get(8).setRecipename("香烤排骨");
                                lastWeightModel.getData().getRecipes().get(8).setRecipeimage("http://eco.haier.com/group1/M00/02/82/Cp8ljlyHXdOACzHUAAAzLdY0y6I923.jpg");
                                lastWeightModel.getData().getRecipes().get(8).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(8).setRecipeid("22043");

                                //馨厨V8
                                lastWeightModel.getData().getRecipes().get(9).setRecipename("浓香鲫鱼汤");
                                lastWeightModel.getData().getRecipes().get(9).setRecipeimage("http://eco.haier.com/group1/M00/02/85/Cp8ljlyIdBmAM2qOAABGEihh8W0671.jpg");
                                lastWeightModel.getData().getRecipes().get(9).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(9).setRecipeid("22218");

                                cookBookThreeAdapter.setMenuModels(lastWeightModel.getData().getRecipes());
                                rcy_menu.setAdapter(cookBookThreeAdapter);
                            } else {
//                            for (int i = 0; i < lastWeightModel.getData().size(); i++) {
                                //.get(lastWeightModel.getData().size() - 1);
                                if (ll_fragment_loading == null) return;
                                ll_fragment_loading.setVisibility(View.GONE);
                                lastWeight = Float.parseFloat(lastWeightModel.getData().getWeight());
                                Log.e("上一次体重", "onSuccess:" + lastWeight);
                                String boneWeight = lastWeightModel.getData().getBoneWeight();
                                String[] boneWeightArray = boneWeight.split(",");
                                String muscleRate = lastWeightModel.getData().getMuscleRate();
                                String[] muscleRateArray = muscleRate.split(",");
                                String bmi = lastWeightModel.getData().getBmi();
                                String[] bmiArray = bmi.split(",");
                                String score = lastWeightModel.getData().getScore();
                                String axunge = lastWeightModel.getData().getBody();
                                String[] axungeArray = axunge.split(",");
//                                if (userData.getData().getMemberList().size() == 0) return;
//                                tv_user_name.setText(userData.getData().getMemberList().get(position).getNickName());
//                                tv_recommend.setText("为" + userData.getData().getMemberList().get(position).getNickName() + "推荐的健康菜谱");
                                if (usersDataBean.getData().size() == 0) return;
                                tv_user_name.setText(usersDataBean.getData().get(position).getNickName());
                                tv_recommend.setText("为" + usersDataBean.getData().get(position).getNickName() + "推荐的健康菜谱");

                                tv_recommend_two.setVisibility(View.GONE);
                                tv_weight.setText(lastWeight + "");
                                tv_weight_unit.setText("KG");
                                if (axungeArray[0].equals("0") || axungeArray[0].equals("0.0") || axungeArray[0].equals("-1%") || axungeArray[0].equals("0%")) {
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
                                //OBT600-8GU1
                                lastWeightModel.getData().getRecipes().get(0).setRecipename("蜂蜜烤面包片");
                                lastWeightModel.getData().getRecipes().get(0).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae50169c23424a90f84.jpg");
                                lastWeightModel.getData().getRecipes().get(0).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(0).setRecipeid("22298");

                                //晶钻Q3
                                lastWeightModel.getData().getRecipes().get(1).setRecipename("南瓜银耳莲子羹");
                                lastWeightModel.getData().getRecipes().get(1).setRecipeimage("http://eco.haier.com/group1/M00/01/4A/Cp8ljlrRYOGAZ41QAABcpeZb5QM363.jpg");
                                lastWeightModel.getData().getRecipes().get(1).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(1).setRecipeid("22209");

                                //OBT600-8GU1
                                lastWeightModel.getData().getRecipes().get(4).setRecipename("蒜蓉扇贝");
                                lastWeightModel.getData().getRecipes().get(4).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae48183693374af0169946d1e910a38.jpg");
                                lastWeightModel.getData().getRecipes().get(4).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(4).setRecipeid("22285");

                                //OBT600-8GU1
                                lastWeightModel.getData().getRecipes().get(5).setRecipename("锡纸金针菇");
                                lastWeightModel.getData().getRecipes().get(5).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae5016a04c0c3da1690.jpg");
                                lastWeightModel.getData().getRecipes().get(5).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(5).setRecipeid("22313");

                                //馨厨V8
//                                lastWeightModel.getData().getRecipes().get(6).setRecipename("菠萝蔬菜饮");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipeimage("http://eco.haier.com/group1/M00/02/7A/Cp8ljlwkhW2AXyaXAAHSMPgSzWI199.png");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipetag("智慧菜谱");
//                                lastWeightModel.getData().getRecipes().get(6).setRecipeid("22238");
                                lastWeightModel.getData().getRecipes().get(6).setRecipename("西芹彩椒思慕雪");
                                lastWeightModel.getData().getRecipes().get(6).setRecipeimage("http://data.haier.net/oss/M00/00/0E/Csd_llzvVgqABdWpAAEAa2x5YyM321.jpg");
                                lastWeightModel.getData().getRecipes().get(6).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(6).setRecipeid("22205");

                                //
                                lastWeightModel.getData().getRecipes().get(8).setRecipename("香烤排骨");
                                lastWeightModel.getData().getRecipes().get(8).setRecipeimage("http://eco.haier.com/group1/M00/02/82/Cp8ljlyHXdOACzHUAAAzLdY0y6I923.jpg");
                                lastWeightModel.getData().getRecipes().get(8).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(8).setRecipeid("22043");

                                //馨厨V8
//                                lastWeightModel.getData().getRecipes().get(9).setRecipename("浓香鲫鱼汤");
//                                lastWeightModel.getData().getRecipes().get(9).setRecipeimage("http://eco.haier.com/group1/M00/02/85/Cp8ljlyIdBmAM2qOAABGEihh8W0671.jpg");
//                                lastWeightModel.getData().getRecipes().get(9).setRecipetag("智慧菜谱");
//                                lastWeightModel.getData().getRecipes().get(9).setRecipeid("22278");
                                lastWeightModel.getData().getRecipes().get(9).setRecipename("浓香鲫鱼汤");
                                lastWeightModel.getData().getRecipes().get(9).setRecipeimage("http://eco.haier.com/group1/M00/02/85/Cp8ljlyIdBmAM2qOAABGEihh8W0671.jpg");
                                lastWeightModel.getData().getRecipes().get(9).setRecipetag("智慧菜谱");
                                lastWeightModel.getData().getRecipes().get(9).setRecipeid("22218");


                                cookBookThreeAdapter.setMenuModels(lastWeightModel.getData().getRecipes());
                                rcy_menu.setAdapter(cookBookThreeAdapter);
//                        }
                            }
                        } catch (Exception e) {
                            Log.e("Exception", "" + e);
                        }
                    }
                });
    }

    /**
     * 上传体脂数据
     */
    private void upData(final String famaliyId, final String userId, String mac_id, String
            province_id, String province_name,
                        String city_id, String city_name, String county_id, String county_name, String
                                sex, String age, String height,
                        String score, final String weight, String metabolism, String boneWeight, String
                                muscleRate, String muscleWeight,
                        String visceralFat, String water, String waterWeight, String obesity,
                        final String bmi, final String axunge, String tips) {
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
                        Log.e("上传数据:", "" + response.body() + "\n"
                                + "参数:" + String.valueOf(json) + "\n"
                                + "Url--->" + ConstantUrl.UPDATA_URL);
                        if (SpUtils.getInstance(getContext()).getIsDialogAdd()) {
                            Log.e("AYD", "json" + String.valueOf(json));
                            weightDialog.dismiss();
                            getUserLastWeight(famaliyId, userId);
                            SpUtils.getInstance(getContext()).cleanDialog();
                        } else {
                            if (menuModel.getCode().equals("200")) {
                                viewPagerCurrentListener.setViewPagerCurrent(_pos);
                                Log.e("AYD", "response---->" + response.body());
                                if (weightDialog == null) return;
                                weightDialog.setLoading(false);
                                weightDialog.setResultVisibility(true, true);
                                weightDialog.setProgressVisibility(false);
                                weightDialog.setMatchVisibility(false);
                                weightDialog.setTitle("海尔智能体脂秤-Q81");
                                String[] axungeArray = axunge.split(",");
                                weightDialog.setPersonlData("BMI: " + entity.getBmi(), userName, weight,
                                        axungeArray[0] + "", df.format(bone) + "", df.format(muscle) + "");
                                if (recipes == null) return;

                                //OBT600-8GU1

                                menuModel.getData().getData().getRecipes().get(0).setRecipename("蜂蜜烤面包片");
                                menuModel.getData().getData().getRecipes().get(0).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae50169c23424a90f84.jpg");
                                menuModel.getData().getData().getRecipes().get(0).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(0).setRecipeid("22298");

                                //晶钻Q3
                                menuModel.getData().getData().getRecipes().get(1).setRecipename("南瓜银耳莲子羹");
                                menuModel.getData().getData().getRecipes().get(1).setRecipeimage("http://eco.haier.com/group1/M00/01/4A/Cp8ljlrRYOGAZ41QAABcpeZb5QM363.jpg");
                                menuModel.getData().getData().getRecipes().get(1).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(1).setRecipeid("22209");

                                //OBT600-8GU1
                                menuModel.getData().getData().getRecipes().get(4).setRecipename("蒜蓉扇贝");
                                menuModel.getData().getData().getRecipes().get(4).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae48183693374af0169946d1e910a38.jpg");
                                menuModel.getData().getData().getRecipes().get(4).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(4).setRecipeid("22285");

                                //OBT600-8GU1
                                menuModel.getData().getData().getRecipes().get(5).setRecipename("锡纸金针菇");
                                menuModel.getData().getData().getRecipes().get(5).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae5016a04c0c3da1690.jpg");
                                menuModel.getData().getData().getRecipes().get(5).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(5).setRecipeid("22313");

                                //馨厨V8
                                menuModel.getData().getData().getRecipes().get(6).setRecipename("西芹彩椒思慕雪");
                                menuModel.getData().getData().getRecipes().get(6).setRecipeimage("http://data.haier.net/oss/M00/00/0E/Csd_llzvVgqABdWpAAEAa2x5YyM321.jpg");
                                menuModel.getData().getData().getRecipes().get(6).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(6).setRecipeid("22205");

                                //
                                menuModel.getData().getData().getRecipes().get(8).setRecipename("香烤排骨");
                                menuModel.getData().getData().getRecipes().get(8).setRecipeimage("http://eco.haier.com/group1/M00/02/82/Cp8ljlyHXdOACzHUAAAzLdY0y6I923.jpg");
                                menuModel.getData().getData().getRecipes().get(8).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(8).setRecipeid("22043");

                                //馨厨V8
                                menuModel.getData().getData().getRecipes().get(9).setRecipename("浓香鲫鱼汤");
                                menuModel.getData().getData().getRecipes().get(9).setRecipeimage("http://eco.haier.com/group1/M00/02/85/Cp8ljlyIdBmAM2qOAABGEihh8W0671.jpg");
                                menuModel.getData().getData().getRecipes().get(9).setRecipetag("智慧菜谱");
                                menuModel.getData().getData().getRecipes().get(9).setRecipeid("22218");

                                cookBookAdapter.setMenuModels(menuModel.getData().getData().getRecipes());
                                rcy_menu.setAdapter(cookBookAdapter);
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
        final JSONObject json = new JSONObject();
        json.put("data", jsonObject);
        OkGo.<String>post(ConstantUrl.MATCH_MEMBERS_URL)
                .upJson(String.valueOf(json))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
//                        MatchModel matchModel = new Gson().fromJson(response.body(), MatchModel.class);
                        Log.e("匹配成员:", "" + response.body() + "\n"
                                + "参数:" + String.valueOf(json) + "\n"
                                + "Url" + ConstantUrl.MATCH_MEMBERS_URL);
                        if (weightDialog == null) return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //此时已在主线程中，更新UI
//                                weightDialog.setUserData(userData);
                                weightDialog.setMatchVisibility(true);
                                weightDialog.setProgressVisibility(false);
                                String wei = formatter.format(weight);
                                weightDialog.setMatchWeight(wei);
                                weightDialog.setResultVisibility(false, false);
                                weightDialog.setTitle("未匹配到家庭成员");
                            }
                        });
                    }
                });
    }

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
                        Log.e("默认菜谱:", "--->" + response.body() + "\n"
                                + "参数--->:" + String.valueOf(json) + "\n"
                                + "URL--->:" + ConstantUrl.GET_NO_DATA_EMNU);
                        if (response.code() == 200) {
                            if (ll_fragment_loading == null) return;
                            ll_fragment_loading.setVisibility(View.GONE);
                            try {
                                noDataModel = new Gson().fromJson(response.body(), NoDataModel.class);
                            } catch (Exception e) {
                                Log.e("数据解析", "" + e);
                            }
                            if (noDataModel.getData() == null) return;
                            if (noDataModel.getCode().equals("500")) {
                                ll_failed.setVisibility(View.VISIBLE);
                                ll_menu.setVisibility(View.GONE);
                                return;
                            }
                            //OBT600-8GU1;OBT600-8HGU1
                            noDataModel.getData().getRecipes().get(0).setRecipename("蜂蜜烤面包片");
                            noDataModel.getData().getRecipes().get(0).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae50169c23424a90f84.jpg");
                            noDataModel.getData().getRecipes().get(0).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(0).setRecipeid("22298");

                            //晶钻Q3
                            noDataModel.getData().getRecipes().get(1).setRecipename("南瓜银耳莲子羹");
                            noDataModel.getData().getRecipes().get(1).setRecipeimage("http://eco.haier.com/group1/M00/01/4A/Cp8ljlrRYOGAZ41QAABcpeZb5QM363.jpg");
                            noDataModel.getData().getRecipes().get(1).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(1).setRecipeid("22209");

                            //OBT600-8GU1;OBT600-8HGU1
                            noDataModel.getData().getRecipes().get(4).setRecipename("蒜蓉扇贝");
                            noDataModel.getData().getRecipes().get(4).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae48183693374af0169946d1e910a38.jpg");
                            noDataModel.getData().getRecipes().get(4).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(4).setRecipeid("22285");

                            //OBT600-8GU1;OBT600-8HGU1
                            noDataModel.getData().getRecipes().get(5).setRecipename("锡纸金针菇");
                            noDataModel.getData().getRecipes().get(5).setRecipeimage("http://data.haier.net/oss/M00/00/FF/8ae4828369337ae5016a04c0c3da1690.jpg");
                            noDataModel.getData().getRecipes().get(5).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(5).setRecipeid("22313");

                            //馨厨V8
                            noDataModel.getData().getRecipes().get(6).setRecipename("菠萝蔬菜饮");
                            noDataModel.getData().getRecipes().get(6).setRecipeimage("http://eco.haier.com/group1/M00/02/7A/Cp8ljlwkhW2AXyaXAAHSMPgSzWI199.png");
                            noDataModel.getData().getRecipes().get(6).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(6).setRecipeid("22238");

                            //OBT600-8HGU1
                            noDataModel.getData().getRecipes().get(8).setRecipename("香烤排骨");
                            noDataModel.getData().getRecipes().get(8).setRecipeimage("http://eco.haier.com/group1/M00/02/82/Cp8ljlyHXdOACzHUAAAzLdY0y6I923.jpg");
                            noDataModel.getData().getRecipes().get(8).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(8).setRecipeid("22043");

                            //馨厨V8
                            noDataModel.getData().getRecipes().get(9).setRecipename("浓香鲫鱼汤");
                            noDataModel.getData().getRecipes().get(9).setRecipeimage("http://eco.haier.com/group1/M00/02/85/Cp8ljlyIdBmAM2qOAABGEihh8W0671.jpg");
                            noDataModel.getData().getRecipes().get(9).setRecipetag("智慧菜谱");
                            noDataModel.getData().getRecipes().get(9).setRecipeid("22278");
                            cookBookNoDataAdapter.setMenuModels(noDataModel.getData().getRecipes());
                            rcy_menu.setAdapter(cookBookNoDataAdapter);
                        } else {
                            ll_failed.setVisibility(View.VISIBLE);
                            ll_menu.setVisibility(View.GONE);
                        }
                    }
                });
    }


    @Override
    public void setItemClickListener(int pos) {
        if (recipes == null) return;
        if (pos == 0) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22298");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 1) {
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22209");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (pos == 4) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22285");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 5) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22313");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 6) {
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22205");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 8) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22043");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 9) {
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22218");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
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

    }

    @Override
    public void setItemClickListener1(int pos) {
        if (noDataModel.getData() == null) return;
        if (pos == 0) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22298");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 1) {
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22209");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (pos == 4) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22285");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 5) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22313");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 6) {
//            Intent intent = new Intent();
//            intent.setAction("com.android.brokenmachine.cookbook.detail");
//            intent.putExtra("i03", "22238");
//            intent.putExtra("current_type_id", "2010338b2020c0342809ccc721c671000000087ac6d57fcf66e1b311781aa6c0");
//            intent.putExtra("device_type", "馨厨V8");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22205");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 8) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22043");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 9) {
//            Intent intent = new Intent();
//            intent.setAction("com.android.brokenmachine.cookbook.detail");
//            intent.putExtra("i03", "22278");
//            intent.putExtra("current_type_id", "2010338b2020c0342809ccc721c671000000087ac6d57fcf66e1b311781aa6c0");
//            intent.putExtra("device_type", "馨厨V8");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22218");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
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
    }

    @Override
    public void setItemClickListener2(int pos) {
        if (lastWeightModel.getData() == null) return;
        if (pos == 0) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22298");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 1) {
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22209");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else if (pos == 4) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22285");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 5) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22313");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 6) {
//            Intent intent = new Intent();
//            intent.setAction("com.android.brokenmachine.cookbook.detail");
//            intent.putExtra("i03", "22238");
//            intent.putExtra("current_type_id", "2010338b2020c0342809ccc721c671000000087ac6d57fcf66e1b311781aa6c0");
//            intent.putExtra("device_type", "馨厨V8");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22205");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 8) {
            Intent intent = new Intent();
            intent.setAction("com.android.cookbook.detail");
            intent.putExtra("i03", "22043");
            intent.putExtra("typeId", "201043cd34d384341e01764fc9144be7f5ac8dce0b95128926ae6269d8f30ac0");
            intent.putExtra("devcieType", "OBT600-8HGU1");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (pos == 9) {
//            Intent intent = new Intent();
//            intent.setAction("com.android.brokenmachine.cookbook.detail");
//            intent.putExtra("i03", "22278");
//            intent.putExtra("current_type_id", "2010338b2020c0342809ccc721c671000000087ac6d57fcf66e1b311781aa6c0");
//            intent.putExtra("device_type", "馨厨V8");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
            Intent intent = new Intent();
            intent.setAction("com.android.brokenmachine.cookbook.detail");
            intent.putExtra("i03", "22218");
            intent.putExtra("current_type_id", "2010338b2020c0342809744bdb436a000000e260f4651650cc2fbd1f7b625cc0");
            intent.putExtra("device_type", "晶钻Q3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
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
        }
//        SpUtils.getInstance(getContext()).putPos(position);
    }


    public void setFirstData(WeightEntity entity) {
        mCurrentWeightEntity = entity;
        df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(1);
        AccountEntity af = new AccountEntity();
        af.setId(100);
        af.setAccess_token("sss");
        af.setWeixin("123123");
        Account.getInstance(getActivity()).setAccountInfo(af);
        RoleInfo roleInfo = new RoleInfo();
        int sex = usersDataBean.getData().get(0).getSex();
        if (sex == 1) {
            roleInfo.setSex("男");
        } else {
            roleInfo.setSex("女");
        }
        roleInfo.setNickname(usersDataBean.getData().get(0).getNickName());
        roleInfo.setHeight((int) usersDataBean.getData().get(0).getHeight());
        roleInfo.setUseId(usersDataBean.getData().get(0).getMemberInfoId() + "");
        roleInfo.setBirthday(stampToDate(String.valueOf(usersDataBean.getData().get(0).getBirthday())));

        roleInfo.setAccount_id(195871);
        roleInfo.setId(1993456);
        roleInfo.setCurrent_state(1);
        roleInfo.setCreate_time("2018-11-29 11:01:11");
//        roleInfo.setWeight_init(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));
//        roleInfo.setWeight_goal(Float.parseFloat(userData.getData().getMemberList().get(pos).getWeight()));
        roleInfo.setWeight_init((float) usersDataBean.getData().get(0).getWeight());
        roleInfo.setWeight_goal((float) usersDataBean.getData().get(0).getWeight());
        roleInfo.setRole_type(0);
//        roleInfo.setSex(userData.getData().getMemberList().get(0).getSex());
//        roleInfo.setNickname(userData.getData().getMemberList().get(0).getNickName());
//        roleInfo.setHeight(Integer.parseInt(userData.getData().getMemberList().get(0).getHeight()));
//        roleInfo.setUseId(userData.getData().getMemberList().get(0).getFamilyMemeberId());
//        roleInfo.setAccount_id(195871);
//        roleInfo.setBirthday(userData.getData().getMemberList().get(0).getBirthday());
//        roleInfo.setId(1993456);
//        roleInfo.setCurrent_state(1);
//        roleInfo.setCreate_time("2018-11-29 11:01:11");
//        roleInfo.setWeight_init(Float.parseFloat(userData.getData().getMemberList().get(0).getWeight()));
//        roleInfo.setWeight_goal(Float.parseFloat(userData.getData().getMemberList().get(0).getWeight()));
//        roleInfo.setRole_type(0);
        Account.getInstance(getActivity()).setRoleInfo(roleInfo);
        Log.e("AYD", "roleInfo---->: " + roleInfo.toString());
        WeighDataParser.create(getActivity()).fillFatWithSmoothImpedance(entity, roleInfo);
        age = WeighDataParser.getCalAge(roleInfo, entity);
        height = WeighDataParser.getCalHeight(roleInfo, entity);
        String sexStr = WeighDataParser.getCalSex(roleInfo, entity);
        this.sex = (byte) (sexStr.equals("女") ? 0 : 1);
        CsAlgoBuilder csAlgoBuilder = new CsAlgoBuilder(height, entity.getWeight(), this.sex, age, entity.getR1());
        Log.e("AYD", "mCurrentWeightEntity---->: " + entity.toString());
        util = new BuildItemsUtil(CSApplication.getInstance(), mCurrentWeightEntity, roleInfo, csAlgoBuilder);
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
        sort1(mAbnormalIndexList);
        upData(UserUtils.get().userId(), roleInfo.getUseId(),
                SpUtils.getInstance(getContext()).getMac(),
                SpUtils.getInstance(getContext()).getProvinceId(),
                SpUtils.getInstance(getContext()).getProvince(),
                SpUtils.getInstance(getContext()).getCityId(),
                SpUtils.getInstance(getContext()).getCity(), "", "",
                sex + "", stampToDateNO(userData.getData().getMemberList().get(0).getBirthday()), height + "", score + "", weight + ""
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
    }

    public static String stampToDateNO(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

}
