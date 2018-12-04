package com.jq.btc.homePage.home.haier;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

import com.jq.btc.account.role.STWeightGoalActivity;
import com.jq.btc.account.role.WeightGoalActivity;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.dialog.haier.AddWeightDialogFragment;
import com.jq.btc.helper.CsBtEngine;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.home.utils.ScrollJudge;
import com.jq.btc.kitchenscale.Kitchen_Weigh_Activity;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CircleImageView;
import com.jq.code.view.WeightGoalProgressBar;
import com.jq.code.view.text.CustomTextView;
import com.jq.code.view.waveview.DynamicWave2;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 正常模式的动态页
 * Created by lijh on 2017/7/19.
 */

@SuppressLint("ValidFragment")
public class NormalFragment extends BaseFragment implements View.OnTouchListener {
    private static final int REQUEST_GOAL_WEIGHT_SETTINGS = 89;

    @BindView(R2.id.click_kitchen_scale)
    ImageView click_kitchen_scale;
    @BindView(R2.id.mRoleImage)
    CircleImageView mRoleImage;
    @BindView(R2.id.mTimeText)
    TextView mTimeText;
    @BindView(R2.id.mShareView)
    ImageView mShareView;
    @BindView(R2.id.mHistoryView)
    ImageView mHistoryView;
    @BindView(R2.id.mBluetoothIcon)
    ImageView mBluetoothIcon;
    @BindView(R2.id.bluetooth_state_text)
    TextView mBluetoothStateText;
    @BindView(R2.id.icon_go)
    View mIconGo;
    @BindView(R2.id.mBluetoothStateLayout)
    LinearLayout mBluetoothStateLayout;
    @BindView(R2.id.mSetWeightGoalView)
    TextView mSetWeightGoalView;
    @BindView(R2.id.mWeightGoalText)
    CustomTextView mWeightGoalText;
    @BindView(R2.id.mWeightText)
    CustomTextView mWeightText;
    @BindView(R2.id.mBmiText)
    CustomTextView mBmiText;
    @BindView(R2.id.mBmiLevelText)
    TextView mBmiLevelText;
    @BindView(R2.id.mCompareLastText)
    TextView mCompareLastText;
    @BindView(R2.id.mCompareLastWeight)
    CustomTextView mCompareLastWeight;
    @BindView(R2.id.mCompareLastWeightUnit)
    TextView mCompareLastWeightUnit;
    @BindView(R2.id.mCompareGoalLayout)
    View mCompareGoalLayout;
    @BindView(R2.id.mCompareGoalText)
    TextView mCompareGoalText;
    @BindView(R2.id.mCompareGoalWeight)
    CustomTextView mCompareGoalWeight;
    @BindView(R2.id.mCompareGoalWeightUnit)
    TextView mCompareGoalWeightUnit;
    @BindView(R2.id.mWeightGoalProgressBar)
    WeightGoalProgressBar mWeightGoalProgressBar;
    @BindView(R2.id.mBlueLayout)
    LinearLayout mBlueLayout;
    @BindView(R2.id.mFatUnit)
    View mFatUnit;
    @BindView(R2.id.mBoneUnit)
    View mBoneUnit;
    @BindView(R2.id.mMuscleUnit)
    View mMuscleUnit;
    @BindView(R2.id.mToReportPage)
    ImageView mToReportPage;
    @BindView(R2.id.mWholeLayout)
    View mWholeLayout;
    @BindView(R2.id.mAddWeight)
    ImageView mAddWeight;
    @BindView(R2.id.mBmiLayout)
    View mBmiLayout;
    @BindView(R2.id.mCompareLayout)
    View mCompareLayout;
    @BindView(R2.id.mHasDataBottom)
    View mHasDataBottom;
    @BindView(R2.id.mNoDataIcon)
    View mNoDataIcon;
    @BindView(R2.id.mNoDataBottom)
    View mNoDataBottom;
    @BindView(R2.id.mDynamicView)
    DynamicWave2 mDynamicView;

    Unbinder unbinder;

    @BindView(R2.id.mTrends)
    ImageView mTrends;

    private int sx;
    private int sy;
    private SharedPreferences sp;
    private RadioButton radioButton;

    private HomeFragment contextss;
    @SuppressLint("ValidFragment")
    public NormalFragment(RadioButton radioButton,HomeFragment contextss){
        this.radioButton = radioButton;
        this.contextss = contextss;
    }

    /**
     * 当前角色的目标体重
     */
    private float mCurRoleWeightGoal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_normal, null, false);
        unbinder = ButterKnife.bind(this, view);

        mCurrentWeightUnit = StandardUtil.getWeightExchangeUnit(getContext());
        initView();
        initValue();

        SharedPreferences sp = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
        int yy = sp.getInt("lasty", 634);
        int xx = sp.getInt("lastx", 1115);
        click_kitchen_scale.setOnTouchListener(this);
        click_kitchen_scale.setTop(yy);
        click_kitchen_scale.setLeft(xx);
        final GestureDetector mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            private ScrollJudge scrollJudge = new ScrollJudge();

            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // if (Math.abs(e1.getRawX() - e2.getRawX()) > 250) {
                // // System.out.println("水平方向移动距离过大");
                // return true;
                // }

                // 手势向下 down
                if ((e2.getRawY() - e1.getRawY()) > 200) {

                    return true;
                }
                // 手势向上 up
                if (scrollJudge.shouldUp(e1, e2, velocityX, velocityY)) {
                    toReportActivity();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                if(distanceY < -200) {
//                    toReportActivity();
//                    return true;
//                }
//                return super.onScroll(e1, e2, distanceX, distanceY);
//            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });

        return view;
    }


    int dx;
    int dy;

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            // 如果手指放在imageView上拖动
            case R2.id.click_kitchen_scale:
                // event.getRawX(); //获取手指第一次接触屏幕在x方向的坐标
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 获取手指第一次接触屏幕
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
                        click_kitchen_scale.setImageResource(R.drawable.kitchen_scale);
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指在屏幕上移动对应的事件
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        // 获取手指移动的距离
                        dx = x - sx;
                        dy = y - sy;
                        // 得到imageView最开始的各顶点的坐标
                        int l = click_kitchen_scale.getLeft();
                        int r = click_kitchen_scale.getRight();
                        int t = click_kitchen_scale.getTop();
                        int b = click_kitchen_scale.getBottom();
                        // 更改imageView在窗体的位置
                        click_kitchen_scale.layout(l + dx, t + dy, r + dx, b + dy);
                        // 获取移动后的位置
                        sx = (int) event.getRawX();
                        sy = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:// 手指离开屏幕对应事件
                        int lasty = click_kitchen_scale.getTop();
                        int lastx = click_kitchen_scale.getLeft();
                        SharedPreferences sp1 = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
                        int yy = sp1.getInt("lasty", 634);
                        int xx = sp1.getInt("lastx", 1115);
                        if (lastx - xx < 10 && lasty - yy < 10) {
                            SharedPreferences sh = getActivity().getSharedPreferences("kitchens", Context.MODE_PRIVATE);
                            String kitchen_name = sh.getString("kitchen_name", "00");
                            if (!kitchen_name.equals("00")) {
                                startActivity(new Intent(getActivity(), Kitchen_Weigh_Activity.class).putExtra("where", 1));
                            } else {
                                startActivity(new Intent(getActivity(), BoundDeviceActivity.class).putExtra("where", 2));
                            }
                        }
                        // 记录最后图片在窗体的位置

                        click_kitchen_scale.setImageResource(R.drawable.kitchen_scale);

                        SharedPreferences sp = getActivity().getSharedPreferences("position", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lasty", lasty);
                        editor.putInt("lastx", lastx);
                        editor.commit();
                        break;
                }
                break;
        }
        return true;// 不会中断触摸事件的返回
    }


    private void initView() {
        mWholeLayout.setPadding(0, ScreenUtils.getStatusBarHeight(getContext()), 0, 0);
//        mShareView.setOnClickListener(new OnShareListener(getActivity()));
        upAnim();
    }

    protected void initValue() {
        if (null == getActivity()) {
            return;
        }


        RoleInfo roleInfo = Account.getInstance(getActivity()).getRoleInfo();

        setWeightGoalViews();
        int bluetoothIcon = ((HomeFragment) getParentFragment()).getBluetoothIcon();
        setMsgLayout(bluetoothIcon);
        WeightEntity entity = mCurrentWeightEntity;
        if (null != entity) {
            mWeightText.setVisibility(View.VISIBLE);
            mBmiLayout.setVisibility(View.VISIBLE);
            mNoDataIcon.setVisibility(View.GONE);

            mCompareLayout.setVisibility(View.VISIBLE);
            mHasDataBottom.setVisibility(View.VISIBLE);

            mNoDataBottom.setVisibility(View.GONE);

            try {
                //weight_time=2017-06-01 16:16:15
                String weightTime = entity.getWeight_time();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date weightDate = sdf.parse(weightTime);
                // "04月25日 13:25"
                SimpleDateFormat textFormat = new SimpleDateFormat("MM月dd日 HH:mm");
                String text = textFormat.format(weightDate);
                mTimeText.setText("上次称重时间: " + "\n" + text);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            float bmi = entity.getBmi();
            mWeightText.setText(entity.getDisplayWeight(getContext(), mCurrentWeightUnit));
            NumberFormat df = NumberFormat.getNumberInstance();
            df.setMaximumFractionDigits(1);
            mBmiText.setText("BMI:" + df.format(bmi));
            int bmiLevel = WeighDataParser.getBmiLevel(entity) - 1;
            mBmiLevelText.setText(WeighDataParser.StandardSet.BMI.getStandards()[bmiLevel]);

            // 与上次体重比较
            float curWeight = entity.getWeight();
            if (null != mLastWeightEntity) {
                float lastWeight = mLastWeightEntity.getWeight();
                if (curWeight > lastWeight) {
                    mCompareLastText.setText(R.string.weight_more_than_last);
                    mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (curWeight - lastWeight), "", (byte) 1));
                } else if (curWeight < lastWeight) {
                    mCompareLastText.setText(R.string.weight_less_than_last);
                    mCompareLastWeight.setText(StandardUtil.getWeightExchangeValue(getContext(), (lastWeight - curWeight), "", (byte) 1));
                } else {
                    mCompareLastText.setText(R.string.weight_less_than_last);
                    mCompareLastWeight.setText("0");
                }
            } else {
                mCompareLastText.setText(R.string.weight_less_than_last);
                mCompareLastWeight.setText("- -");
            }
            mCompareLastWeightUnit.setText(mCurrentWeightUnit);

            // 体脂率、水分比例、肌肉率
            int[] icons = new int[]{WeighDataParser.StandardSet.AXUNGE.getIcon(), WeighDataParser.StandardSet.BONE.getIcon(), WeighDataParser.StandardSet.MUSCLE.getIcon()};
            String[] names = new String[]{getString(WeighDataParser.StandardSet.AXUNGE.getName()), getString(WeighDataParser.StandardSet.BONE.getName()), getString(WeighDataParser.StandardSet.MUSCLE.getName())};
            View[] views = new View[]{mFatUnit, mBoneUnit, mMuscleUnit};
            for (int i = 0; i < views.length; i++) {
                View v = views[i];
                UnitHolder holder = new UnitHolder();
                v.setTag(holder);
                holder.mTypeIcon = (ImageView) v.findViewById(R.id.mTypeIcon);
                holder.mTypeName = (TextView) v.findViewById(R.id.mTypeName);
                holder.mTypeValue = (TextView) v.findViewById(R.id.mTypeValue);
                holder.mUnitUnit = (TextView) v.findViewById(R.id.mUnitUnit);
                holder.mLevelText = (TextView) v.findViewById(R.id.mLevelText);

                holder.mTypeIcon.setImageResource(icons[i]);
                holder.mTypeName.setText(names[i]);
            }
            // 脂肪率
            float axunge = entity.getAxunge();
            // 骨量
            float bone = entity.getBone();
            // 肌肉
            float muscle = entity.getMuscle() / 2;
            UnitHolder axungeHolder = (UnitHolder) mFatUnit.getTag();
            UnitHolder boneHolder = (UnitHolder) mBoneUnit.getTag();
            UnitHolder muscleHolder = (UnitHolder) mMuscleUnit.getTag();
            if (axunge <= 0) {
                axungeHolder.mTypeValue.setText("- -");
                axungeHolder.mLevelText.setVisibility(View.INVISIBLE);
            } else {
                axungeHolder.mTypeValue.setText(df.format(axunge) + "%");
                int axungeLevel = WeighDataParser.getAxungeLevel(roleInfo, entity) - 1;
                GradientDrawable gd = (GradientDrawable) axungeHolder.mLevelText.getBackground();
                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.AXUNGE.getColor()[axungeLevel]));
                axungeHolder.mLevelText.setText(WeighDataParser.StandardSet.AXUNGE.getStandards()[axungeLevel]);
            }

            if (bone <= 0) {
                boneHolder.mTypeValue.setText("- -");
                boneHolder.mUnitUnit.setText("");
                boneHolder.mLevelText.setVisibility(View.INVISIBLE);
            } else {
                boneHolder.mTypeValue.setText(df.format(bone));
                boneHolder.mUnitUnit.setText(mCurrentWeightUnit);
                float[] boneStandard = WeighDataParser.getBoneStandardRange(WeighDataParser.getCalSex(roleInfo, entity),
                        WeighDataParser.getCalAge(roleInfo, entity));
                float[] levelNums = new float[boneStandard.length - 2];
                System.arraycopy(boneStandard, 1, levelNums, 0, levelNums.length);
                // 计算级别
                int boneLevel = 0;
                for (int i = 0; i < levelNums.length; i++) {
                    if (bone < levelNums[i]) {
                        break;
                    } else {
                        boneLevel++;
                    }
                }

                GradientDrawable gd = (GradientDrawable) boneHolder.mLevelText.getBackground();
                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.BONE.getColor()[boneLevel]));
                boneHolder.mLevelText.setText(WeighDataParser.StandardSet.BONE.getStandards()[boneLevel]);
            }

            if (muscle <= 0) {
                muscleHolder.mTypeValue.setText("- -");
                muscleHolder.mLevelText.setVisibility(View.INVISIBLE);
            } else {
                muscleHolder.mTypeValue.setText(df.format(muscle) + "%");
                int muscleLevel = WeighDataParser.getMuscleLevel(roleInfo, entity) - 1;
                GradientDrawable gd = (GradientDrawable) muscleHolder.mLevelText.getBackground();
                gd.setColor(getResources().getColor(WeighDataParser.StandardSet.MUSCLE.getColor()[muscleLevel]));
                muscleHolder.mLevelText.setText(WeighDataParser.StandardSet.MUSCLE.getStandards()[muscleLevel]);
            }
        } else {
            mWeightText.setVisibility(View.GONE);
            mBmiLayout.setVisibility(View.GONE);
            mNoDataIcon.setVisibility(View.VISIBLE);

            mCompareLayout.setVisibility(View.GONE);
            mHasDataBottom.setVisibility(View.GONE);

            mNoDataBottom.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void setMsgLayout(int bluetoothIcon) {
        String s = ((HomeFragment) getParentFragment()).getMsg();

        int bluetoothIconRes;
        if (bluetoothIcon == R.mipmap.ble_connected || bluetoothIcon == R.mipmap.bt_connected) {
            // 已连接标志
            bluetoothIconRes = R.mipmap.bt_connected;
        } else {
            bluetoothIconRes = R.mipmap.bt_not_connected;
        }
        if (getContext().getString(R.string.matchDataTip).equals(s)) {
            bluetoothIconRes = R.mipmap.icon_tishi;
        }
        mBluetoothIcon.setImageResource(bluetoothIconRes);

        if (null == s || s.trim().length() == 0) {
            mBluetoothStateLayout.setVisibility(View.GONE);
        } else {
            mBluetoothStateLayout.setVisibility(View.VISIBLE);

            final String msg = s;
            mBluetoothStateText.setText(msg);
            if (getContext().getString(R.string.reportBoundTip).equals(msg)) {
                // 显示箭头，且点击去绑定
                mIconGo.setVisibility(View.VISIBLE);
            } else if (getContext().getString(R.string.locationServiceNotOpen).equals(msg)) {
                // 显示箭头，且点击打开设置
                mIconGo.setVisibility(View.VISIBLE);
            } else if (getContext().getString(R.string.matchDataTip).equals(msg)) {
                // 显示箭头，且点击打开 认领体重数据界面
                mIconGo.setVisibility(View.VISIBLE);
            } else {
                mIconGo.setVisibility(View.GONE);
            }
            View.OnClickListener listener = ((HomeFragment) getParentFragment()).new OnBlueToothLayoutClick(msg);
            mBluetoothStateLayout.setOnClickListener(listener);
        }
    }

    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param temp 临时体重
     */
    @Override
    public void setTempBluetoothWeight(WeightEntity temp) {
        if (mNoDataIcon.getVisibility() == View.VISIBLE) {
            mNoDataIcon.setVisibility(View.GONE);
            mWeightText.setVisibility(View.VISIBLE);
        }
        mWeightText.setText(temp.getDisplayWeight(getContext(), mCurrentWeightUnit));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class UnitHolder {
        ImageView mTypeIcon;
        TextView mTypeName;
        TextView mTypeValue;
        TextView mUnitUnit;
        TextView mLevelText;
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

    private void upAnim() {
        boolean b = false;
        if (b) {
            return;
        }
        ObjectAnimator animatorScaleX = ObjectAnimator.ofFloat(mToReportPage, "scaleX", 1, 0.8f);
        animatorScaleX.setRepeatCount(-1);
        animatorScaleX.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator animatorScaleY = ObjectAnimator.ofFloat(mToReportPage, "scaleY", 1, 0.8f);
        animatorScaleY.setRepeatCount(-1);
        animatorScaleY.setRepeatMode(ValueAnimator.RESTART);

        ObjectAnimator animatorTranslationY = ObjectAnimator.ofFloat(mToReportPage, "translationY", 0, -12f);
        animatorTranslationY.setRepeatCount(-1);
        animatorTranslationY.setRepeatMode(ValueAnimator.RESTART);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorScaleX, animatorScaleY, animatorTranslationY);
        animatorSet.setDuration(2000);
        animatorSet.start();
    }


    public void setWaveViewVisible(boolean visible) {
        if (null == mDynamicView) {
            return;
        }
        if (visible) {
            mDynamicView.setVisibility(View.VISIBLE);
        } else {
            mDynamicView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mDynamicView.setVisibility(View.INVISIBLE);
                }
            }, 50);
        }
    }

    @OnClick({R2.id.mTrends, R2.id.mRoleImage, R2.id.mShareView, R2.id.mHistoryView, R2.id.mSetWeightGoalView, R2.id.mAddWeight, R2.id.mToReportPage})
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
                        doChangeRole();
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

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //  解绑设备
//        CsBtEngine mCsBtEngine = CsBtEngine.getInstance(getActivity());
//        ScaleParser.getInstance(getActivity()).setBluetoothState(ScaleParser.STATE_BLUETOOTH_UNBOUND);
//        mCsBtEngine.stopSearch();
//        mCsBtEngine.stopAutoConnect();
//        mCsBtEngine.closeGATT(true);
//        ScaleParser.getInstance(getActivity()).setScale(new ScaleInfo());
//
//    }
}
