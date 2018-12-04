package com.jq.btc.dialog.haier;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btlib.util.ThreadUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CustomToast;
import com.jq.code.view.ruler.RulerWheel;
import com.jq.code.view.text.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * 手动添加体重对话框
 *
 * @author 李金华
 */
public class AddWeightDialogFragment extends DialogFragment {

    private static final int ROLE_ADD_REQUEST = 1 ;

    @BindView(R2.id.mDaka)
    View mDaka;
    @BindView(R2.id.mCancel)
    ImageView mCancel;
    @BindView(R2.id.mWeightValue)
    CustomTextView mWeightValue;
    @BindView(R2.id.mUnit)
    TextView mUnit;
    @BindView(R2.id.mAddWeightRuler)
    RulerWheel mAddWeightRuler;
    @BindView(R2.id.mSureBtn)
    TextView mSureBtn;
    @BindView(R2.id.mAddWeightLayout)
    View mAddWeightLayout;
    @BindView(R2.id.mSelectRoleLayout)
    View mSelectRoleLayout;
    @BindView(R2.id.mRolesGridView)
    GridView mRolesGridView;

    Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mCallback) {
            mCallback.onCancel();
        }
        unbinder.unbind();
    }

    public interface Callback {
        /**
         * 完成手动添加体重
         *
         * @param newWeightEntity 新添加体重
         */
        void onAddWeightFinish(WeightEntity newWeightEntity);

        void onCancel();

        void onRoleChange();

        /** 点击打卡 */
        void onClickDaka();
    }

    private Callback mCallback;

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    private Activity mActivity;
    /** 传进来的最后一条体重 */
    private WeightEntity mLastWeightEntity;
    /**
     * 显示值
     */
    private float mShowValue;

    public void setLastWeightEntity(WeightEntity mCurrentWeightEntity) {
        this.mLastWeightEntity = mCurrentWeightEntity;
    }

    private ArrayList<RoleInfo> roleInfos;
    private RolesAdapter adapter;
    /** 生成的体重 */
    private WeightEntity mCreatedWeightEntity;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(R.layout.dialogfragment_add_weight, container, false);
        unbinder = ButterKnife.bind(this, view);
        //添加这一行
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(layoutParams);

        //设置dialog的 进出 动画
        getDialog().getWindow().setWindowAnimations(R.style.animate_dialog);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));

        initView();
        initValue();

        return view;
    }

    private void initView() {
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.mainColor));
        float roundRadius = getResources().getDisplayMetrics().density * 6;
        gd.setCornerRadius(roundRadius);
        mSureBtn.setBackground(gd);

        GradientDrawable dakaBg = new GradientDrawable();
        dakaBg.setColor(getResources().getColor(R.color.mainColor));
        dakaBg.setCornerRadius(getResources().getDisplayMetrics().density * 90);
        mDaka.setBackground(dakaBg);
    }

    private void initValue() {
        if (Account.getInstance(mActivity).isAccountLogined()) {
            ThreadUtil.executeThread(new Runnable() {
                @Override
                public void run() {
                    roleInfos = Account.getInstance(mActivity).findRoleALL();
                }
            });
        }
        int unit = Config.getInstance(mActivity).getIntWeightUnit();
        int rulerMax = 200;
        float ruleValue = 0;
        if (mLastWeightEntity != null) {
            ruleValue = mLastWeightEntity.getWeight();
        } else {
            String sex = Account.getInstance(mActivity).getRoleInfo().getSex();
            if (sex.equals("男")) {
                ruleValue = 60;
            } else {
                ruleValue = 50;
            }
        }
        if (unit == Config.JIN) {
            // 以斤为单位
            rulerMax = rulerMax * 2;
            ruleValue = ruleValue * 2;
        }
        mUnit.setText(StandardUtil.getWeightExchangeUnit(mActivity));
        mShowValue = (int) ruleValue;
        mAddWeightRuler.setValue(mShowValue, rulerMax);
        mWeightValue.setText(String.valueOf(mShowValue));
        mAddWeightRuler.setScrollingListener(new RulerWheel.OnWheelScrollListener() {
            @Override
            public void onChanged(RulerWheel wheel, int oldValue, int newValue) {
                mShowValue = (((float) newValue) / 10);
                mWeightValue.setText(String.valueOf(mShowValue));
            }

            @Override
            public void onScrollingStarted(RulerWheel wheel) {

            }

            @Override
            public void onScrollingFinished(RulerWheel wheel) {

            }
        });
    }

    @OnClick({R2.id.mDaka, R2.id.mCancel, R2.id.mSureBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R2.id.mDaka:
                dismiss();
                break;
            case R2.id.mCancel:
                dismiss();
                if (null != mCallback) {
                    mCallback.onCancel();
                }
                break;
            case R2.id.mSureBtn:
                onConfirm();
                break;
        }
    }

    private void onConfirm() {
        int uint = Config.getInstance(mActivity).getIntWeightUnit();
        if (mShowValue <= 0.0f) {
            CustomToast.showToast(mActivity, R.string.mygoalweight_nozero, Toast.LENGTH_SHORT);
            return;
        }
        // 重量属性值
        byte scaleProperty = 1;
        String scaleValue = "" + mShowValue;
        // kg值
        float value = mShowValue;
        if (uint == Config.JIN) {
            value = mShowValue / 2;
            scaleProperty = 9;
        }
        WeightEntity roleDataInfo = WeighDataParser.create(mActivity).onJustWeight(value, scaleValue, scaleProperty);
        mCreatedWeightEntity = roleDataInfo;
        if (!Account.getInstance(mActivity).isAccountLogined()) {
            roleDataInfo.setRole_id(Account.getInstance(mActivity).getRoleInfo().getId());

            RoleInfo roleInfo = Account.getInstance(mActivity).getRoleInfo();
            int roleType = roleInfo.getRole_type();

            if(roleType == 1) {
                //孕妇模式
                roleDataInfo.setSex(2);
            }

            if (null != mCallback) {
                mCallback.onAddWeightFinish(roleDataInfo);
            }
            dismiss();

            ThreadUtil.executeThread(new Runnable() {
                @Override
                public void run() {
                    WeightDataDB.getInstance(mActivity).create(mCreatedWeightEntity);
                }
            });
        } else {
            // 匹配角色
            mAddWeightLayout.setVisibility(View.GONE);
            mSelectRoleLayout.setVisibility(View.VISIBLE);

            if (null == roleInfos) {
                roleInfos = Account.getInstance(mActivity).findRoleALL();
            }

            adapter = new RolesAdapter();
            mRolesGridView.setAdapter(adapter);
            mRolesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == roleInfos.size()) {
                    } else {
                        RoleInfo roleInfo = roleInfos.get(position);
                        selectRole(roleInfo);
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == ROLE_ADD_REQUEST){
            if(data != null){
                RoleInfo roleInfo = data.getParcelableExtra(RoleInfo.ROLE_KEY) ;
                selectRole(roleInfo);
            }
        }
    }

    private void selectRole(RoleInfo roleInfo){
        WeighDataParser.create(getContext()).fillFatWithImpedance(mCreatedWeightEntity, roleInfo);
        if (roleInfo.getId() != Account.getInstance(mActivity).getRoleInfo().getId()) {
            // 切换角色了
            Account.getInstance(mActivity).setRoleInfo(roleInfo);

            int roleType = roleInfo.getRole_type();
            if(roleType == 1) {
                //孕妇模式
                mCreatedWeightEntity.setSex(2);
            }
            WeightDataDB.getInstance(mActivity).create(mCreatedWeightEntity);
            if(null != mCallback) {
                mCallback.onRoleChange();
            }
//            dismiss(); 会崩溃
        } else {
            if(null != mCallback) {
                mCallback.onAddWeightFinish(mCreatedWeightEntity);
            }
            ThreadUtil.executeThread(new Runnable() {
                @Override
                public void run() {
                    WeightDataDB.getInstance(mActivity).create(mCreatedWeightEntity);
                }
            });
            dismiss();
        }
    }

    private class RolesAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            int rc = roleInfos == null ? 0 : roleInfos.size();
            if(rc == 8) {
                // 最多8个角色，不显示加号了
                return 8;
            } else {
                return rc + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mActivity).inflate(R.layout.dialogfragment_add_weight_role_unit, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.mRoleImage = (ImageView) convertView.findViewById(R.id.mRoleImage);
                holder.mNickName = (TextView) convertView.findViewById(R.id.mNickName);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position < roleInfos.size()) {
                RoleInfo roleInfo = roleInfos.get(position);
                holder.mNickName.setText(roleInfo.getNickname());
            } else {
                holder.mRoleImage.setImageResource(R.mipmap.role_add);
                holder.mNickName.setText(R.string.reportNewRole);
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView mRoleImage;
        TextView mNickName;
    }
}
