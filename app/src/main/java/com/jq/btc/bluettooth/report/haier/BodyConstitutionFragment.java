package com.jq.btc.bluettooth.report.haier;

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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.report.haier.item.ReportDetalis;
import com.jq.btc.bluettooth.report.haier.item.Rn8Item;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.home.utils.ScrollJudge;
import com.jq.btc.utils.ShareUtils;
import com.jq.code.code.algorithm.CsAlgoBuilder;
import com.jq.code.code.business.Account;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.PieChart;
import com.jq.code.view.text.CustomTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lijh on 2017/7/26.
 * 体质报告
 */

public class BodyConstitutionFragment extends Fragment {

    @BindView(R2.id.mScrollView)
    ScrollView mScrollView;
    @BindView(R2.id.mBgLayout)
    View mBgLayout;
    @BindView(R2.id.mWeightText)
    CustomTextView mWeightText;
    @BindView(R2.id.mCompareLastText)
    TextView mCompareLastText;
    @BindView(R2.id.mCompareLastWeight)
    CustomTextView mCompareLastWeight;
    @BindView(R2.id.mCompareGoalText)
    TextView mCompareGoalText;
    @BindView(R2.id.mCompareGoalWeight)
    CustomTextView mCompareGoalWeight;
    @BindView(R2.id.icon)
    ImageView icon;
    @BindView(R2.id.hint)
    TextView hint;
    @BindView(R2.id.xiaoqingHint)
    CustomTextView xiaoqingHint;
    @BindView(R2.id.t1)
    TextView t1;
    @BindView(R2.id.weight2)
    CustomTextView weight2;
    @BindView(R2.id.mComparePerfectWeight)
    CustomTextView mComparePerfectWeight;
    @BindView(R2.id.mComparePerfectWeightHint)
    TextView mComparePerfectWeightHint;
    @BindView(R2.id.mPerfectWeightProgressBar)
    ProgressBar mPerfectWeightProgressBar;
    @BindView(R2.id.weightStandardIcon)
    View weightStandardIcon;
    @BindView(R2.id.vWeight)
    View vWeight;
    @BindView(R2.id.mPerfectWeight)
    CustomTextView mPerfectWeight;
    @BindView(R2.id.t2)
    TextView t2;
    @BindView(R2.id.muscle)
    CustomTextView muscle;
    @BindView(R2.id.mCompareMuscle)
    CustomTextView mCompareMuscle;
    @BindView(R2.id.mCompareMuscleHint)
    TextView mCompareMuscleHint;
    @BindView(R2.id.mMuscleProgressBar)
    ProgressBar mMuscleProgressBar;
    @BindView(R2.id.muscleStandardIcon)
    View muscleStandardIcon;
    @BindView(R2.id.vMuscle)
    View vMuscle;
    @BindView(R2.id.mPerfectMuscle)
    CustomTextView mPerfectMuscle;
    @BindView(R2.id.t3)
    TextView t3;
    @BindView(R2.id.fatWeight)
    CustomTextView fatWeight;
    @BindView(R2.id.mCompareFatWeight)
    CustomTextView mCompareFatWeight;
    @BindView(R2.id.mCompareFatWeightHint)
    TextView mCompareFatWeightHint;
    @BindView(R2.id.mFatWeightProgressBar)
    ProgressBar mFatWeightProgressBar;
    @BindView(R2.id.fatStandardIcon)
    View fatStandardIcon;
    @BindView(R2.id.vFat)
    View vFat;
    @BindView(R2.id.mPerfectFat)
    CustomTextView mPerfectFat;
    @BindView(R2.id.mPieChart)
    PieChart mPieChart;
    @BindView(R2.id.mWeight3)
    TextView mWeight3;
    @BindView(R2.id.mUnit3)
    TextView mUnit3;
    @BindView(R2.id.mFatLayout)
    View mFatLayout;
    @BindView(R2.id.mMuscleLayout)
    View mMuscleLayout;
    @BindView(R2.id.mUnit4)
    TextView mUnit4;
    @BindView(R2.id.mUnit5)
    TextView mUnit5;
    @BindView(R2.id.mUnit6)
    TextView mUnit6;
    @BindView(R2.id.mUnit7)
    TextView mUnit7;
    @BindView(R2.id.mUnit8)
    TextView mUnit8;
    @BindView(R2.id.mUnit9)
    TextView mUnit9;
    @BindView(R2.id.mUnit10)
    TextView mUnit10;
    @BindView(R2.id.mUnit11)
    TextView mUnit11;
    @BindView(R2.id.mUnit12)
    TextView mUnit12;

    Unbinder unbinder;
    private ShareUtils mShareUtils;
    /**
     * 当前体重
     */
    private WeightEntity mCurrentWeightEntity;
    /**
     * 上次体重
     */
    private WeightEntity mLastWeightEntity;
    private View view;
    private TextView aLHStandard;
    private TextView aLHValue;
    private TextView aRHStandard;
    private TextView aRHValue;
    private TextView aLFStandard;
    private TextView aLFValue;
    private TextView aRFStandard;
    private TextView aRFValue;
    private TextView aTrunkStandard;
    private TextView aTrunkValue;
    private TextView muscleUnit;
    private TextView mLHStandard;
    private TextView mLHValue;
    private TextView mRHStandard;
    private TextView mRHValue;
    private TextView mLFStandard;
    private TextView mLFValue;
    private TextView mRFStandard;
    private TextView mRFValue;
    private TextView mTrunkStandard;
    private TextView mTrunkValue;
    private LinearLayout rn8Layout;
    private RoleInfo curRoleInfo;

    public void setCurrWeightEntity(WeightEntity curWeightEntity) {
        mCurrentWeightEntity = curWeightEntity;
    }

    public void setLastWeightEntity(WeightEntity weightEntity) {
        mLastWeightEntity = weightEntity;
    }

    private String mCurrentWeightUnit;
    private CsAlgoBuilder csAlgoBuilder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_body_constitution, container, false);
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
                if (mScrollView.getScrollY() > 10) {
                    return false;
                }

                // 手势向下 down
                if (scrollJudge.shouldBottom(e1, e2, velocityX, velocityY)) {
                    ((HaierReportActivity) getActivity()).onFinish();
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
                    if (height == 0) {
                        height = (int) getResources().getDisplayMetrics().density * 50;
                    }
                    if (msg.what == touchEventId) {
                        if (lastY == scroller.getScrollY()) {
                            //停止了，此处你的操作业务
                            if (lastY > height) {
                                ((HaierReportActivity) getActivity()).setScrollUpState(true, BodyConstitutionFragment.this);
                            } else {
                                ((HaierReportActivity) getActivity()).setScrollUpState(false, BodyConstitutionFragment.this);
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
                        handler.sendMessageDelayed(handler.obtainMessage(touchEventId, v), 5);
                        break;
                    default:
                        break;
                }
                return mGestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });

        //八电极view初始化

        aLHStandard = (TextView) view.findViewById(R.id.aLHStandard);
        aLHValue = (TextView) view.findViewById(R.id.aLHValue);
        aRHStandard = (TextView) view.findViewById(R.id.aRHStandard);
        aRHValue = (TextView) view.findViewById(R.id.aRHValue);
        aLFStandard = (TextView) view.findViewById(R.id.aLFStandard);
        aLFValue = (TextView) view.findViewById(R.id.aLFValue);
        aRFStandard = (TextView) view.findViewById(R.id.aRFStandard);
        aRFValue = (TextView) view.findViewById(R.id.aRFValue);
        aTrunkStandard = (TextView) view.findViewById(R.id.aTrunkStandard);
        aTrunkValue = (TextView) view.findViewById(R.id.aTrunkValue);
        muscleUnit = (TextView) view.findViewById(R.id.muscleUnit);
        mLHStandard = (TextView) view.findViewById(R.id.mLHStandard);
        mLHValue = (TextView) view.findViewById(R.id.mLHValue);
        mRHStandard = (TextView) view.findViewById(R.id.mRHStandard);
        mRHValue = (TextView) view.findViewById(R.id.mRHValue);
        mLFStandard = (TextView) view.findViewById(R.id.mLFStandard);
        mLFValue = (TextView) view.findViewById(R.id.mLFValue);
        mRFStandard = (TextView) view.findViewById(R.id.mRFStandard);
        mRFValue = (TextView) view.findViewById(R.id.mRFValue);
        mTrunkStandard = (TextView) view.findViewById(R.id.mTrunkStandard);
        mTrunkValue = (TextView) view.findViewById(R.id.mTrunkValue);
        rn8Layout = (LinearLayout) view.findViewById(R.id.rn8Layout);


    }

    private void initValue() {
        String sDisplayWeight = mCurrentWeightEntity.getDisplayWeight(getContext(), mCurrentWeightUnit);
        mWeightText.setText(sDisplayWeight);

        WeightEntity weightEntity = mCurrentWeightEntity;
        // 与上次体重比较
        float curWeight = weightEntity.getWeight();
        if (null != mLastWeightEntity) {
            float lastWeight = mLastWeightEntity.getWeight();
            if (curWeight > lastWeight) {
                mCompareLastText.setText(R.string.weight_more_than_last);
                mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(),
                        (curWeight - lastWeight), "", (byte) 1));
            } else if (curWeight < lastWeight) {
                mCompareLastText.setText(R.string.weight_less_than_last);
                mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(),
                        (lastWeight - curWeight), "", (byte) 1));
            } else {
                mCompareLastText.setText(R.string.weight_less_than_last);
                mCompareLastWeight.setText("0");
            }
        } else {
            mCompareLastText.setText(R.string.weight_less_than_last);
            mCompareLastWeight.setText("- -");
        }

        // 与体重目标比较
        Account account = Account.getInstance(getContext());
        RoleInfo roleInfo = account.getRoleInfo();
        float weightGoal = roleInfo.getWeight_goal();
        if (weightGoal > 0) {
            float cur = curWeight;
            float delta = cur - weightGoal;
            if (delta > 0) {
                mCompareGoalText.setText(R.string.weight_goal_reduce2);
                mCompareGoalWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), delta, "", (byte) 5));
            } else {
                mCompareGoalText.setText(R.string.weight_goal_increment2);
                mCompareGoalWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (-delta), "", (byte) 5));
            }
        } else {
            mCompareGoalText.setText(R.string.weight_goal_reduce2);
            mCompareGoalWeight.setText("- -");
        }

        // 计算体型
        int age = WeighDataParser.getCalAge(roleInfo, weightEntity);
        int bodilyLevel = -1;
        if (weightEntity.getR1() > 0 && age > 5 || weightEntity.getAxunge() > 0) {
            if (weightEntity.getR1() > 0) {
                bodilyLevel = (int) (csAlgoBuilder.getShape()) + 1;
            } else {
                bodilyLevel = (int) (CsAlgoBuilder.calShape(csAlgoBuilder.getH(), weightEntity.getWeight(), csAlgoBuilder.getSex(), age, weightEntity.getAxunge())) + 1;
            }
            xiaoqingHint.setText(WeighDataParser.StandardSet.BODILY.getTips()[bodilyLevel]);
        }

        if (age < 18) {
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
            xiaoqingHint.setText(getString(WeighDataParser.StandardSet.CORPULENT.getTips()[level],
                    StandardUtil.getWeightExchangeValue(getContext(), csAlgoBuilder.getBW(), "", (byte) 5) + mCurrentWeightUnit));
        }

        // 体重控制

        weight2.setText(sDisplayWeight);
        // 理想体重
        float bw = csAlgoBuilder.getBW();
        mPerfectWeight.setText(getString(R.string.weight_control_perfect_weight, StandardUtil.getWeightExchangeValue(getContext(), bw, "", (byte) 1) + mCurrentWeightUnit));
        float delta = curWeight - bw;
        if (delta > 0) {
            mComparePerfectWeightHint.setText(R.string.weight_control_weight_increment);
            mComparePerfectWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), delta, "", (byte) 1));
        } else {
            mComparePerfectWeightHint.setText(R.string.weight_control_weight_reduce);
            mComparePerfectWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (-delta), "", (byte) 1));
        }

        float percent;
        float minWeight = 0.5f * bw;
        float maxWeight = 1.5f * bw;
        if (curWeight < minWeight) {
            percent = 0;
        } else if (curWeight > maxWeight) {
            percent = 1;
        } else {
            percent = (curWeight - minWeight) / (maxWeight - minWeight);
        }
        vWeight.post(new MyRun(percent, vWeight, mPerfectWeightProgressBar));

        float percentMuscle;
        float muscleWeight = weightEntity.getWeight() * weightEntity.getMuscle() / 200;
        muscle.setText(StandardUtil.getWeightExchangeValue(getContext(), muscleWeight, "", (byte) 1));
        if (age < 18 && weightEntity.getR1() > 0) {
            mPerfectMuscle.setText(getString(R.string.weight_control_perfect_muscle, "--"));
            mCompareMuscle.setText("--");
            percentMuscle = 0;
        } else {
            // 标准肌肉
            float bm;
            if (weightEntity.getR1() > 0) {
                bm = csAlgoBuilder.getBM()/2;
            } else {
                bm = CsAlgoBuilder.getBM(weightEntity.getWeight(), csAlgoBuilder.getSex())/2;
            }

            mPerfectMuscle.setText(getString(R.string.weight_control_perfect_muscle, StandardUtil.getWeightExchangeValue(getContext(), bm, "", (byte) 1) + mCurrentWeightUnit));
            float deltaMuscle = muscleWeight - bm;
            if (deltaMuscle > 0) {
                mCompareMuscleHint.setText(R.string.weight_control_muscle_reduce);
                mCompareMuscle.setText(StandardUtil.getWeightExchangeValue(getContext(), deltaMuscle, "", (byte) 1));
            } else {
                mCompareMuscleHint.setText(R.string.weight_control_muscle_increment);
                mCompareMuscle.setText(StandardUtil.getWeightExchangeValue(getContext(), (-deltaMuscle), "", (byte) 1));
            }

            float minMuscle = 0.5f * bm;
            float maxMuscle = 1.5f * bm;
            if (muscleWeight < minMuscle) {
                percentMuscle = 0;
            } else if (muscleWeight > maxMuscle) {
                percentMuscle = 1;
            } else {
                percentMuscle = (muscleWeight - minMuscle) / (maxMuscle - minMuscle);
            }
        }
        vMuscle.post(new MyRun(percentMuscle, vMuscle, mMuscleProgressBar));


        float percentFatWeight;
        float fw = weightEntity.getWeight() * weightEntity.getAxunge() / 100;
        fatWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), fw, "", (byte) 1));
        if (age < 18 && weightEntity.getR1() > 0) {
            mPerfectFat.setText(getString(R.string.weight_control_perfect_fat, "--"));
            mCompareFatWeight.setText("--");
            percentFatWeight = 0;
        } else {
            // 标准脂肪=脂肪重 + 脂肪控制
            float bf = 0.0f;
            if (weightEntity.getR1() > 0) {
                bf = fw + csAlgoBuilder.getFC();
            } else {
                bf = fw + CsAlgoBuilder.calFC(weightEntity.getWeight(), csAlgoBuilder.getSex(), csAlgoBuilder.getH(), weightEntity.getAxunge(), weightEntity.getMuscle());
            }
            mPerfectFat.setText(getString(R.string.weight_control_perfect_fat, StandardUtil.getWeightExchangeValue(getContext(), bf, "", (byte) 1) + mCurrentWeightUnit));
            float deltaFat = fw - bf;
            if (deltaFat > 0) {
                mCompareFatWeightHint.setText(R.string.weight_control_fat_reduce);
                mCompareFatWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), deltaFat, "", (byte) 1));
            } else {
                mCompareFatWeightHint.setText(R.string.weight_control_fat_increment);
                mCompareFatWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (-deltaFat), "", (byte) 1));
            }


            float minFat = 0.5f * bf;
            float maxFat = 1.5f * bf;
            if (fw < minFat) {
                percentFatWeight = 0;
            } else if (fw > maxFat) {
                percentFatWeight = 1;
            } else {
                percentFatWeight = (fw - minFat) / (maxFat - minFat);
            }
        }
        vFat.post(new MyRun(percentFatWeight, vFat, mFatWeightProgressBar));

        // 饼图
        mShareUtils = new ShareUtils(getActivity(), mCurrentWeightEntity, null);
        mPieChart.setEntites(mShareUtils.getPieEntity());
        mWeight3.setText(sDisplayWeight);
        mUnit3.setText(mCurrentWeightUnit);

        setUnitText();

        setRn8();

    }

    private void setRn8() {

        ReportDetalis reportDetalis = WeighDataParser.create(getActivity()).getReportDetalis(curRoleInfo, mCurrentWeightEntity);

        if(reportDetalis.isHaveRn8()){
            List<Rn8Item> rn8Items = reportDetalis.getRn8Items() ;
            rn8Layout.setVisibility(View.VISIBLE);
            muscleUnit.setText(getResources().getString(R.string.elec_segment_muscle,StandardUtil.getWeightExchangeUnit(getActivity())));
            setRn8ItemView(aLHStandard,aLHValue,rn8Items.get(0));
            setRn8ItemView(aRHStandard,aRHValue,rn8Items.get(1));
            setRn8ItemView(aLFStandard,aLFValue,rn8Items.get(2));
            setRn8ItemView(aRFStandard,aRFValue,rn8Items.get(3));
            setRn8ItemView(aTrunkStandard,aTrunkValue,rn8Items.get(4));

//            float s = rn8Items.get(5).getValue()/2;
//            rn8Items.get(5).setValue(s);
            setRn8ItemView(mLHStandard,mLHValue,rn8Items.get(5));
//            float s1 = rn8Items.get(6).getValue()/2;
//            rn8Items.get(6).setValue(s1);
            setRn8ItemView(mRHStandard,mRHValue,rn8Items.get(6));
//            float s2 = rn8Items.get(7).getValue()/2;
//            rn8Items.get(7).setValue(s2);
            setRn8ItemView(mLFStandard,mLFValue,rn8Items.get(7));
//            float s3 = rn8Items.get(8).getValue()/2;
//            rn8Items.get(8).setValue(s3);
            setRn8ItemView(mRFStandard,mRFValue,rn8Items.get(8));
//            float s4 = rn8Items.get(9).getValue()/2;
//            rn8Items.get(9).setValue(s4);
            setRn8ItemView(mTrunkStandard,mTrunkValue,rn8Items.get(9));
        }else{
            rn8Layout.setVisibility(View.GONE);
        }
    }

    public void setRn8ItemView(TextView standardText,TextView valueText,Rn8Item item){
        if(item.getStandardBg() != -1){
            standardText.setBackgroundResource(item.getStandardBg());
            standardText.setText(item.getStandardText());
        }else {
            standardText.setVisibility(View.INVISIBLE);
        }
        valueText.setText(item.getValueStr(getActivity()));
    }


    private class MyRun implements Runnable {
        private float mPercent;
        private View mV;
        private ProgressBar mProgressBar;

        public MyRun(float percent, View v, ProgressBar progressBar) {
            mPercent = percent;
            mV = v;
            mProgressBar = progressBar;
        }

        @Override
        public void run() {
            int w = mProgressBar.getWidth();
            if (w == 0) {
                mV.post(new MyRun(mPercent, mV, mProgressBar));
                return;
            }
            float density = getResources().getDisplayMetrics().density;

            mProgressBar.setProgress((int) (mPercent * 100));

            int marginLeft = (int) (w * mProgressBar.getProgress() / 100f - 13 * density / 2);
//            int max = (int) (w - 13 * density);
            if (marginLeft < 0) {
                marginLeft = 1;
            }
//            else if (marginLeft > max) {
//                marginLeft = max;
//            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mV.getLayoutParams();
            layoutParams.width = marginLeft;
            mV.setLayoutParams(layoutParams);
        }
    }

    /**
     * 创建算法对象
     */
    private void buildSuanfa() {
        curRoleInfo = Account.getInstance(getActivity()).getRoleInfo();
        WeightEntity weightEntity = mCurrentWeightEntity;
        int age = WeighDataParser.getCalAge(curRoleInfo, weightEntity);
        int height = WeighDataParser.getCalHeight(curRoleInfo, weightEntity);
        String sexStr = WeighDataParser.getCalSex(curRoleInfo, weightEntity);
        byte sex = (byte) (sexStr.equals(getString(R.string.women)) ? 0 : 1);
        csAlgoBuilder = new CsAlgoBuilder(height, weightEntity.getWeight(), sex, age, weightEntity.getR1());
    }

    private void setUnitText() {
        mUnit4.setText(mCurrentWeightUnit);
        mUnit5.setText(mCurrentWeightUnit);
        mUnit6.setText(mCurrentWeightUnit);
        mUnit7.setText(mCurrentWeightUnit);
        mUnit8.setText(mCurrentWeightUnit);
        mUnit9.setText(mCurrentWeightUnit);
        mUnit10.setText(mCurrentWeightUnit);
        mUnit11.setText(mCurrentWeightUnit);
        mUnit12.setText(mCurrentWeightUnit);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
