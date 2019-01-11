package com.jq.btc.homePage.home.haier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.ConstantUrl;
import com.jq.btc.account.role.STWeightGoalActivity;
import com.jq.btc.account.role.WeightGoalActivity;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.bluettooth.report.haier.BodyFatMoreDataActivity;
import com.jq.btc.bluettooth.report.haier.HaierReportActivity;
import com.jq.btc.bluettooth.report.haier.item.BuildItemsUtil;
import com.jq.btc.bluettooth.report.haier.item.IndexDataItem;
import com.jq.btc.dialog.WeightDialog;
import com.jq.btc.dialog.haier.AddWeightDialogFragment;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.NewMainActivity;
import com.jq.btc.homePage.home.utils.ScrollJudge;
import com.jq.btc.kitchenscale.Kitchen_Weigh_Activity;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btc.model.LastWeightModel;
import com.jq.btc.model.MatchModel;
import com.jq.btc.model.UpdataModel;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.SpUtils;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.business.SoundPlayer;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.growingio.android.sdk.utils.ThreadUtils.runOnUiThread;

/**
 * 正常模式的动态页
 * Created by lijh on 2017/7/19.
 */

@SuppressLint("ValidFragment")
public class NormalFragment extends BaseFragment implements View.OnClickListener, WeightDialog.DiaLogRecyItemOnClick {
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

    private int sx;
    private int sy;
    private SharedPreferences sp;
    private RadioButton radioButton;

    private WeightEntity mWeightEntity;
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
    private int pos;
    private UserData userData;
    WeightEntity entity;
    WeighDataParser weighDataParser;
    BuildItemsUtil util;
    NumberFormat formatter;
    String userName;

    private TextView tv_weight, tv_body_fat, tv_bone_weight, tv_muscle_rate;
    private TextView tv_weight_unit, tv_body_fat_unit, tv_bone_weight_unit, tv_muscle_rate_unit;

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

    public interface ViewPagerCurrentListener {
        void setViewPagerCurrent(int pos);
    }

    public NormalFragment(UserData userData, int pos) {
        this.userData = userData;
        this.pos = pos;
    }

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
        df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(1);
        formatter = new DecimalFormat("0.##");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weightDialog = new WeightDialog(getActivity(), R.style.WeightDialog);
        weightDialog.setCanceledOnTouchOutside(false);
        weightDialog.setDiaLogRecyItemOnClick(this);
        tv_weight = view.findViewById(R2.id.tv_weight);
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
        tv_add_member.setOnClickListener(this);
//        initValue();
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
                        MatchModel matchModel = new Gson().fromJson(response.body(), MatchModel.class);
//                        if (matchModel.getCode().equals("200")) {
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
//                    }
                });
    }


    /**
     * 匹配弹窗点击事件
     *
     * @param pos
     */
    @Override
    public void itemOnClickListener(int pos) {
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
        viewPagerCurrentListener.setViewPagerCurrent(pos);
        int age = WeighDataParser.getCalAge(roleInfo, entity);
        int height = WeighDataParser.getCalHeight(roleInfo, entity);
        String sexStr = WeighDataParser.getCalSex(roleInfo, entity);
        byte sex = (byte) (sexStr.equals("女") ? 0 : 1);
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
        sort(mAbnormalIndexList);
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
        int score = 0;
        if (entity.getR1() > 0) {
            if (age > 5) {
                score = (int) csAlgoBuilder.getScore();
            }
        } else if (entity.getAxunge() > 0) {
            score = weighDataParser.calculateCode(entity, roleInfo);
        }
        if (score > 0) {
            tv_score.setText(score + ",");
        } else {
            tv_score.setText("- -");
        }

        if (entity.getSex() == 2) {
            tv_tips.setText(R.string.HaierReport_index_hint1);
        } else if (age < 18) {
            tv_tips.setText(R.string.age_tip);
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
        // 体脂率、水分比例、肌肉率
        int[] icons = new int[]{WeighDataParser.StandardSet.WEIGHT.getIcon(), WeighDataParser.StandardSet.AXUNGE.getIcon(), WeighDataParser.StandardSet.BONE.getIcon(), WeighDataParser.StandardSet.MUSCLE.getIcon()};
        String[] names = new String[]{getString(WeighDataParser.StandardSet.WEIGHT.getName()), getString(WeighDataParser.StandardSet.AXUNGE.getName()), getString(WeighDataParser.StandardSet.BONE.getName()), getString(WeighDataParser.StandardSet.MUSCLE.getName())};
        View[] views = new View[]{mWeightUnit1, mFatUnit1, mBoneUnit1, mMuscleUnit1};
//        for (int i = 0; i < views.length; i++) {
//            View v = views[i];
//            UnitHolder holder = new UnitHolder();
//            v.setTag(holder);
//            holder.mTypeIcon1 = v.findViewById(R.id.mTypeIcon1);
//            holder.mTypeName1 = v.findViewById(R.id.mTypeName1);
//            holder.mTypeValue1 = v.findViewById(R.id.mTypeValue1);
//            holder.mUnitUnit1 = v.findViewById(R.id.mUnitUnit1);
//            holder.mTypeIcon1.setImageResource(icons[i]);
//            holder.mTypeName1.setText(names[i]);
//        }

        UnitHolder weightHolder = (UnitHolder) mWeightUnit1.getTag();
        UnitHolder axungeHolder = (UnitHolder) mFatUnit1.getTag();
        UnitHolder boneHolder = (UnitHolder) mBoneUnit1.getTag();
        UnitHolder muscleHolder = (UnitHolder) mMuscleUnit1.getTag();

        if (weight <= 0) {
//            weightHolder.mTypeValue1.setText("- -");
//            weightHolder.mUnitUnit1.setText("");
            tv_weight.setText("- -");
            tv_weight_unit.setText("");
        } else {
//            weightHolder.mTypeValue1.setText(weight + "");
//            weightHolder.mUnitUnit1.setText(mCurrentWeightUnit);
            tv_weight.setText(weight + "");
            tv_weight_unit.setText("KG");
        }
        if (axunge <= 0) {
//            axungeHolder.mTypeValue1.setText("- -");
            tv_body_fat.setText("- -");
            tv_body_fat_unit.setText("");
        } else {
//            axungeHolder.mTypeValue1.setText(df.format(axunge) + "%");
            tv_body_fat.setText(df.format(axunge) + "");
            tv_body_fat_unit.setText("%");
        }
        if (bone <= 0) {
//            boneHolder.mTypeValue1.setText("- -");
//            boneHolder.mUnitUnit1.setText("");
            tv_bone_weight.setText("- -");
            tv_bone_weight_unit.setText("");
        } else {
//            boneHolder.mTypeValue1.setText(df.format(bone));
//            boneHolder.mUnitUnit1.setText(mCurrentWeightUnit);
            tv_bone_weight.setText(df.format(bone) + "");
            tv_bone_weight_unit.setText("KG");
        }
        if (muscle <= 0) {
//            muscleHolder.mTypeValue1.setText("- -");
            tv_muscle_rate.setText("- -");
            tv_muscle_rate_unit.setText("");
        } else {
//            muscleHolder.mTypeValue1.setText(df.format(muscle) + "%");
            tv_muscle_rate.setText(df.format(muscle) + "");
            tv_muscle_rate_unit.setText("%");
        }

        if (weight > lastWeight) {
//            mCompareLastText_new.setText(R.string.weight_more_than_last);
            lastHeavy = "你的体重比上次重了" + StandardUtil.getWeightExchangeValue(getContext(), (weight - lastWeight), "", (byte) 1) + "KG";
            mCompareLastWeight_new.setText(lastHeavy);
        } else if (weight < lastWeight) {
//            mCompareLastText_new.setText(R.string.weight_less_than_last);
            lastHeavy = "你的体重比上次轻了" + StandardUtil.getWeightExchangeValue(getContext(), (lastWeight - weight), "", (byte) 1) + "KG";
            mCompareLastWeight_new.setText(lastHeavy);
        } else {
//            mCompareLastText_new.setText(R.string.weight_less_than_last);
            lastHeavy = "你的体重比上次轻了0";
            mCompareLastWeight_new.setText(lastHeavy);
        }

        upData(UserUtils.get().userId(), roleInfo.getUseId(), score + "", weight + ""
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


        Log.e("AYD", "onSuccess:bmi " + bmi);
        Log.e("AYD", "onSuccess: score" + score);
        Log.e("AYD", "onSuccess:age " + age);
        Log.e("AYD", "onSuccess: height" + height);
        Log.e("AYD", "onSuccess:weight " + weight);
        Log.e("AYD", "onSuccess: bone" + bone);
        Log.e("AYD", "onSuccess: axunge" + axunge);
        Log.e("AYD", "onSuccess:muscle " + muscle);
        Log.e("AYD", "onSuccess: " + roleInfo.getUseId());

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
        tv_user_name.setText(userData.getData().getMemberList().get(pos).getNickName());
        getUserLastWeight(UserUtils.get().userId(), roleInfo.getUseId());
        mactchUse(UserUtils.get().userId(), SpUtils.getInstance(getContext()).getUserid(), weight);


//        if (null != entity) {
//            Log.e("AYD", "------>" + entity.toString());
//
//            mWeightText.setVisibility(View.VISIBLE);
//            mBmiLayout.setVisibility(View.VISIBLE);
//            mNoDataIcon.setVisibility(View.GONE);
//
//            mCompareLayout.setVisibility(View.VISIBLE);
//            mHasDataBottom.setVisibility(View.VISIBLE);
//
//            mNoDataBottom.setVisibility(View.GONE);
//
//            try {
//                //weight_time=2017-06-01 16:16:15
//                String weightTime = entity.getWeight_time();
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date weightDate = sdf.parse(weightTime);
//                // "04月25日 13:25"
//                SimpleDateFormat textFormat = new SimpleDateFormat("MM月dd日 HH:mm");
//                String text = textFormat.format(weightDate);
//                mTimeText.setText("上次称重时间: " + "\n" + text);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            bmi = entity.getBmi();
//            mWeightText.setText(entity.getDisplayWeight(getContext(), mCurrentWeightUnit));
//            Log.e("AYD", "-----?" + entity.getDisplayWeight(getContext(), mCurrentWeightUnit));
//            df = NumberFormat.getNumberInstance();
//            df.setMaximumFractionDigits(1);
//            bmiLevel = WeighDataParser.getBmiLevel(entity) - 1;
//            mBmiText_one.setText("BMI:" + df.format(bmi));
//            mBmiLevelText_one.setText(WeighDataParser.StandardSet.BMI.getStandards()[bmiLevel]);
//            tv_user_name.setText(userData.getData().getMemberList().get(pos).getNickName());
//
//
//            // 身体得分
//            int age = WeighDataParser.getCalAge(roleInfo, entity);
//            int height = WeighDataParser.getCalHeight(roleInfo, entity);
//            String sexStr = WeighDataParser.getCalSex(roleInfo, entity);
//            byte sex = (byte) (sexStr.equals("女") ? 0 : 1);
//            csAlgoBuilder = new CsAlgoBuilder(height, entity.getWeight(), sex, age, entity.getR1());
//
//
//            if (entity.getSex() == 2) {
//                tv_tips.setText(R.string.HaierReport_index_hint1);
//            } else if (age < 18) {
//                tv_tips.setText(R.string.age_tip);
//            } else {
//                // 显示肥胖度的提示
//                float corpulent;
//                if (entity.getR1() > 0) {
//                    corpulent = csAlgoBuilder.getOD();
//                } else {
//                    corpulent = CsAlgoBuilder.calOD(csAlgoBuilder.getH(), entity.getWeight(), csAlgoBuilder.getSex(),
//                            csAlgoBuilder.getAge());
//                }
//                float[] levelNums = WeighDataParser.StandardSet.CORPULENT.getLevelNums();
//                int level = 0;
//                for (int i = 0; i < levelNums.length; i++) {
//                    if (corpulent < levelNums[i]) {
//                        break;
//                    } else {
//                        level++;
//                    }
//                }
//                tv_tips.setText(WeighDataParser.StandardSet.CORPULENT.getTips()[level]);
//                tv_tips.setText(getString(WeighDataParser.StandardSet.CORPULENT.getTips()[level],
//                        StandardUtil.getWeightExchangeValue(getContext(), csAlgoBuilder.getBW(), "", (byte) 5) + mCurrentWeightUnit));
//            }
//
//            // 与上次体重比较
//            curWeight = entity.getWeight();
//
//            if (null != mLastWeightEntity) {
//                float lastWeight = mLastWeightEntity.getWeight();
//                if (curWeight > lastWeight) {
//                    mCompareLastText_new.setText(R.string.weight_more_than_last);
//                    mCompareLastWeight_new.setText(StandardUtil.getWeightExchangeValue(getContext(), (curWeight - lastWeight), "", (byte) 1) + "KG");
//                } else if (curWeight < lastWeight) {
//                    mCompareLastText_new.setText(R.string.weight_less_than_last);
//                    mCompareLastWeight_new.setText(StandardUtil.getWeightExchangeValue(getContext(), (lastWeight - curWeight), "", (byte) 1) + "KG");
//                } else {
//                    mCompareLastText_new.setText(R.string.weight_less_than_last);
//                    mCompareLastWeight_new.setText("0");
//                }
//            } else {
//                mCompareLastText_new.setText(R.string.weight_less_than_last);
//                mCompareLastWeight_new.setText("- -");
//            }
//            mCompareLastWeightUnit.setText(mCurrentWeightUnit);
//
//            // 体脂率、水分比例、肌肉率
//            int[] icons = new int[]{WeighDataParser.StandardSet.WEIGHT.getIcon(), WeighDataParser.StandardSet.AXUNGE.getIcon(), WeighDataParser.StandardSet.BONE.getIcon(), WeighDataParser.StandardSet.MUSCLE.getIcon()};
//            String[] names = new String[]{getString(WeighDataParser.StandardSet.WEIGHT.getName()), getString(WeighDataParser.StandardSet.AXUNGE.getName()), getString(WeighDataParser.StandardSet.BONE.getName()), getString(WeighDataParser.StandardSet.MUSCLE.getName())};
//            View[] views = new View[]{mWeightUnit1, mFatUnit1, mBoneUnit1, mMuscleUnit1};
//            for (int i = 0; i < views.length; i++) {
//                View v = views[i];
//                UnitHolder holder = new UnitHolder();
//                v.setTag(holder);
//                holder.mTypeIcon1 = v.findViewById(R.id.mTypeIcon1);
//                holder.mTypeName1 = v.findViewById(R.id.mTypeName1);
//                holder.mTypeValue1 = v.findViewById(R.id.mTypeValue1);
//                holder.mUnitUnit1 = v.findViewById(R.id.mUnitUnit1);
////                holder.mLevelText = (TextView) v.findViewById(R.id.mLevelText);
//
//                holder.mTypeIcon1.setImageResource(icons[i]);
//                holder.mTypeName1.setText(names[i]);
//            }
//            //体重
//            weight = entity.getWeight();
//            // 脂肪率
//            axunge = entity.getAxunge();
//            // 骨量
//            bone = entity.getBone();
//            // 肌肉
//            muscle = entity.getMuscle() / 2;
//            UnitHolder weightHolder = (UnitHolder) mWeightUnit1.getTag();
//            UnitHolder axungeHolder = (UnitHolder) mFatUnit1.getTag();
//            UnitHolder boneHolder = (UnitHolder) mBoneUnit1.getTag();
//            UnitHolder muscleHolder = (UnitHolder) mMuscleUnit1.getTag();
//
//            if (weight <= 0) {
//                weightHolder.mTypeValue1.setText("- -");
//                weightHolder.mUnitUnit1.setText("");
////                weightHolder.mLevelText.setVisibility(View.INVISIBLE);
//            } else {
//                weightHolder.mTypeValue1.setText(weight + "");
//                weightHolder.mUnitUnit1.setText(mCurrentWeightUnit);
//            }
//
//            if (axunge <= 0) {
//                axungeHolder.mTypeValue1.setText("- -");
////                axungeHolder.mLevelText.setVisibility(View.INVISIBLE);
//            } else {
//                axungeHolder.mTypeValue1.setText(df.format(axunge) + "%");
////                int axungeLevel = WeighDataParser.getAxungeLevel(roleInfo, entity) - 1;
////                GradientDrawable gd = (GradientDrawable) axungeHolder.mLevelText.getBackground();
////                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.AXUNGE.getColor()[axungeLevel]));
////                axungeHolder.mLevelText.setText(WeighDataParser.StandardSet.AXUNGE.getStandards()[axungeLevel]);
//            }
//
//            if (bone <= 0) {
//                boneHolder.mTypeValue1.setText("- -");
//                boneHolder.mUnitUnit1.setText("");
////                boneHolder.mLevelText.setVisibility(View.INVISIBLE);
//            } else {
//                boneHolder.mTypeValue1.setText(df.format(bone));
//                boneHolder.mUnitUnit1.setText(mCurrentWeightUnit);
//                float[] boneStandard = WeighDataParser.getBoneStandardRange(WeighDataParser.getCalSex(roleInfo, entity),
//                        WeighDataParser.getCalAge(roleInfo, entity));
////                float[] levelNums = new float[boneStandard.length - 2];
////                System.arraycopy(boneStandard, 1, levelNums, 0, levelNums.length);
////                // 计算级别
////                int boneLevel = 0;
////                for (int i = 0; i < levelNums.length; i++) {
////                    if (bone < levelNums[i]) {
////                        break;
////                    } else {
////                        boneLevel++;
////                    }
////                }
////
////                GradientDrawable gd = (GradientDrawable) boneHolder.mLevelText.getBackground();
////                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.BONE.getColor()[boneLevel]));
////                boneHolder.mLevelText.setText(WeighDataParser.StandardSet.BONE.getStandards()[boneLevel]);
//            }
//            if (muscle <= 0) {
//                muscleHolder.mTypeValue1.setText("- -");
////                muscleHolder.mLevelText1.setVisibility(View.INVISIBLE);
//            } else {
//                muscleHolder.mTypeValue1.setText(df.format(muscle) + "%");
////                int muscleLevel = WeighDataParser.getMuscleLevel(roleInfo, entity) - 1;
////                GradientDrawable gd = (GradientDrawable) muscleHolder.mLevelText.getBackground();
////                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.MUSCLE.getColor()[muscleLevel]));
////                muscleHolder.mLevelText.setText(WeighDataParser.StandardSet.MUSCLE.getStandards()[muscleLevel]);
//            }
//
//            getUserLastWeight(UserUtils.get().userId(), roleInfo.getUseId());
//        } else {
//            mWeightText.setVisibility(View.GONE);
//            mBmiLayout.setVisibility(View.GONE);
//            mNoDataIcon.setVisibility(View.VISIBLE);
//
//            mCompareLayout.setVisibility(View.GONE);
//            mHasDataBottom.setVisibility(View.GONE);
//
//            mNoDataBottom.setVisibility(View.VISIBLE);
//        }
    }

    private void inList(List<IndexDataItem> items) {
        for (IndexDataItem item : items) {
            Log.e("AYD", "value--->" + item.nameRes + "--->" + item.valueText);
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


    /**
     * 创建算法对象
     */
    private void buildSuanfa() {
        RoleInfo curRoleInfo = Account.getInstance(getActivity()).getRoleInfo();
        int age = WeighDataParser.getCalAge(curRoleInfo, mCurrentWeightEntity);
        int height = WeighDataParser.getCalHeight(curRoleInfo, mCurrentWeightEntity);
        String sexStr = WeighDataParser.getCalSex(curRoleInfo, mCurrentWeightEntity);
        byte sex = (byte) (sexStr.equals("女") ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, mCurrentWeightEntity.getWeight(), sex, age, mCurrentWeightEntity.getR1());
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
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
     * 当体重单位变化时，刷新控件
     */
    private void refreshUnitText() {
        float curWeight = mCurrentWeightEntity.getWeight();
        if (null != mLastWeightEntity) {
            float lastWeight = mLastWeightEntity.getWeight();
            if (curWeight > lastWeight) {
                mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (curWeight - lastWeight), "", (byte) 1));
            } else if (curWeight < lastWeight) {
                mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (lastWeight - curWeight), "", (byte) 1));
            } else {
                mCompareLastWeight.setText("0");
            }
        } else {
            mCompareLastWeight.setText("- -");
        }

        mCompareLastWeightUnit.setText(mCurrentWeightUnit);
        setWeightGoalViews();
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
            mCompareGoalWeight.setText("- -");
        }
        mCompareGoalWeightUnit.setText(mCurrentWeightUnit);
    }

//    @Override
//    public void setMsgLayout(int bluetoothIcon) {
//        String s = ((HomeFragment) getParentFragment()).getMsg();
//        int bluetoothIconRes;
//        if (bluetoothIcon == R.mipmap.ble_connected || bluetoothIcon == R.mipmap.bt_connected) {
//            // 已连接标志
//            bluetoothIconRes = R.mipmap.bt_connected;
//        } else {
//            bluetoothIconRes = R.mipmap.bt_not_connected;
//        }
//        if (getContext().getString(R.string.matchDataTip).equals(s)) {
//            bluetoothIconRes = R.mipmap.icon_tishi;
//        }
//        mBluetoothIcon.setImageResource(bluetoothIconRes);
//        if (null == s || s.trim().length() == 0) {
//            mBluetoothStateLayout.setVisibility(View.GONE);
//        } else {
//            mBluetoothStateLayout.setVisibility(View.VISIBLE);
//            final String msg = s;
//            mBluetoothStateText.setText(msg);
//            if (getContext().getString(R.string.reportBoundTip).equals(msg)) {
//                // 显示箭头，且点击去绑定
//                mIconGo.setVisibility(View.VISIBLE);
//            } else if (getContext().getString(R.string.locationServiceNotOpen).equals(msg)) {
//                // 显示箭头，且点击打开设置
//                mIconGo.setVisibility(View.VISIBLE);
//            } else if (getContext().getString(R.string.matchDataTip).equals(msg)) {
//                // 显示箭头，且点击打开 认领体重数据界面
//                mIconGo.setVisibility(View.VISIBLE);
//            } else {
//                mIconGo.setVisibility(View.GONE);
//            }
//            View.OnClickListener listener = ((HomeFragment) getParentFragment()).new OnBlueToothLayoutClick(msg);
//            mBluetoothStateLayout.setOnClickListener(listener);
//        }
//    }

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
        if (temp.getDisplayWeight(getContext(), mCurrentWeightUnit).equals("0.00")) {
            weightDialog.dismiss();
        } else {
            weightDialog.show();
            weightDialog.setProgressVisibility(true);
            weightDialog.setMatchVisibility(false);
            weightDialog.setResultVisibility(false);
            weightDialog.setWeigth(temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
            weightDialog.setTitle("身体成分测量中...");
        }
        Log.e("AYD", "setTempBluetoothWeight: " + temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("tag", "  onDestroyView");
    }


    private static class UnitHolder {
        ImageView mTypeIcon1;
        TextView mTypeName1;
        TextView mTypeValue1;
        TextView mUnitUnit1;
        TextView mLevelText1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GOAL_WEIGHT_SETTINGS && resultCode == Activity.RESULT_OK) {
            setWeightGoalViews();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //设置目标
    private void onWeightGoalOrInit() {
        int weightUnit = Config.getInstance(getActivity()).getIntWeightUnit();
        Intent data = new Intent();
        if (weightUnit == Config.ST) {
            data.setClass(getActivity(), STWeightGoalActivity.class);
        } else {
            data.setClass(getActivity(), WeightGoalActivity.class);
        }
        data.putExtra(RoleInfo.ROLE_KEY, Account.getInstance(getContext()).getRoleInfo());
        data.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityForResult(data, REQUEST_GOAL_WEIGHT_SETTINGS);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.tv_add_member:
                Intent intent = new Intent();
                intent.setAction("com.unilife.fridge.app.family.add");
                startActivity(intent);
                break;

            case R2.id.tv_more_data:
                toMoreActivity();
                break;
        }
    }

    @OnClick({R2.id.mTrends, R2.id.mRoleImage, R2.id.mShareView, R2.id.mHistoryView, R2.id.mSetWeightGoalView
            , R2.id.mAddWeight, R2.id.mToReportPage, R2.id.tv_add_member, R2.id.tv_more_data})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R2.id.mSetWeightGoalView:
                // 点击“设置目标体重”
                onWeightGoalOrInit();
                break;
            case R2.id.mAddWeight:
                // 手动添加体重
                AddWeightDialogFragment addWeightDialogFragment = new AddWeightDialogFragment();
                addWeightDialogFragment.show(getChildFragmentManager(), "AddWeightDialogFragment");
                mAddWeight.setVisibility(View.GONE);
                addWeightDialogFragment.setLastWeightEntity(mCurrentWeightEntity);
                addWeightDialogFragment.setCallback(new AddWeightDialogFragment.Callback() {

                    @Override
                    public void onAddWeightFinish(WeightEntity newWeightEntity) {
                        mAddWeight.setVisibility(View.VISIBLE);
                        setWeightEntity(newWeightEntity, mCurrentWeightEntity);
                    }

                    @Override
                    public void onCancel() {
                        mAddWeight.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onRoleChange() {
                        mAddWeight.setVisibility(View.VISIBLE);
//                        doChangeRole();
                    }

                    @Override
                    public void onClickDaka() {
                        mAddWeight.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case R2.id.mToReportPage:
                // 点击 查看身体报告
                toReportActivity();
                break;


        }
    }

    private void toMoreActivity() {
//        Intent intent = new Intent(getActivity(), HaierReportActivity.class);
        Intent intent = new Intent(getActivity(), BodyFatMoreDataActivity.class);
        intent.putExtra(HaierReportActivity.INTENT_KEY_WEIGHT, mCurrentWeightEntity);
        intent.putExtra(HaierReportActivity.INTENT_KEY_LAST_WEIGHT, mLastWeightEntity);
        intent.putExtra(HaierReportActivity.INTENT_KEY_FROM_HOME, true);
        intent.putExtra("useId", userData.getData().getMemberList().get(pos).getFamilyMemberId());
        intent.putExtra("useName", userData.getData().getMemberList().get(pos).getNickName());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_bottom_in, 0);
    }

    /**
     * 获取用户最后一次称重数据
     *
     * @param famaliyId
     * @param userId
     */
    public void getUserLastWeight(String famaliyId, String userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("famaliyId", famaliyId);
        jsonObject.put("userId", userId);
        Log.e("AYD", "onSuccess---->" + userId);
        OkGo.<String>post(ConstantUrl.GET_USER_LAST_WEIGHT)
                .upJson(String.valueOf(jsonObject))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body() == null) return;
                        LastWeightModel lastWeightModel = new Gson().fromJson(response.body(), LastWeightModel.class);
                        for (int i = 0; i < lastWeightModel.getData().size(); i++) {
                            lastWeight = Float.parseFloat(lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getWeight());
                            String boneWeight = lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getBoneWeight();
                            String[] boneWeightArray = boneWeight.split(",");
                            String muscleRate = lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getMuscleRate();
                            String[] muscleRateArray = muscleRate.split(",");
                            String bmi = lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getBmi();
                            String[] bmiArray = bmi.split(",");
                            String score = lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getScore();
                            String axunge = lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getBody();
                            String[] axungeArray = axunge.split(",");

                            tv_user_name.setText(userData.getData().getMemberList().get(pos).getNickName());
                            tv_weight.setText(lastWeight + "");
                            tv_weight_unit.setText("KG");
                            tv_body_fat.setText(axungeArray[0]);
                            tv_body_fat_unit.setText("");
                            tv_bone_weight.setText(boneWeightArray[0]);
                            tv_bone_weight_unit.setText("KG");
                            tv_muscle_rate.setText(muscleRateArray[0]);
                            tv_muscle_rate_unit.setText("");
                            mCompareLastWeight_new.setText(axungeArray[4] + "");
//                            bmiLevel = WeighDataParser.getBmiLevel(entity) - 1;
                            mBmiText_one.setText("BMI:" + bmiArray[0]);
                            mBmiLevelText_one.setText(bmiArray[1]);
                            tv_score.setText(score);
                            tv_tips.setText(lastWeightModel.getData().get(lastWeightModel.getData().size() - 1).getHealith());
                        }

                    }
                });
    }


    public boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    /**
     * 上传体脂数据
     */
    private void upData(String famaliyId, final String userId, String score, final String weight, String metabolism,
                        String boneWeight, String muscleRate, String muscleWeight, String visceralFat,
                        String water, String waterWeight, String obesity, final String bmi, final String axunge, String tips) {
        JSONObject json = new JSONObject();
        json.put("famaliyId", famaliyId);
        json.put("userId", userId);
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

        Log.e("AYD", "onSuccess---->" + userId);
        Log.e("AYD", "onSuccess---->" + json.toJSONString());
        OkGo.<String>post(ConstantUrl.UPDATA_URL)
                .upJson(String.valueOf(json))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        UpdataModel updataModel = new Gson().fromJson(response.body(), UpdataModel.class);
                        if (updataModel.getCode().equals("200")) {
                            Log.e("AYD", "updateSuccess");
                            weightDialog.setResultVisibility(true);
                            weightDialog.setProgressVisibility(false);
                            weightDialog.setMatchVisibility(false);
                            weightDialog.setTitle("海尔智能体脂秤-Q81W");
                            String[] axungeArray = axunge.split(",");
                            weightDialog.setPersonlData("BMI: " + entity.getBmi(), userName, weight,
                                    axungeArray[0] + "", df.format(bone) + "", df.format(muscle) + "");
//                            getUserLastWeight(UserUtils.get().userId(), userId);
                        } else {
                            Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
