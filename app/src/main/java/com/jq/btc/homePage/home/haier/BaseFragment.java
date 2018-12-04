package com.jq.btc.homePage.home.haier;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.jq.btc.app.R;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.report.haier.HaierReportActivity;
import com.jq.btc.dialog.RoleListPop;
import com.jq.code.code.business.Account;
import com.jq.code.code.db.RoleDB;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CustomToast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lijh on 2017/7/19.
 */

public abstract class BaseFragment extends Fragment {
    private static final int ROLE_ADD_REQUEST = 14;

    protected String mCurrentWeightUnit;
    protected WeightEntity mCurrentWeightEntity;
    protected WeightEntity mLastWeightEntity;

    public void setWeightEntity(WeightEntity weightEntity, WeightEntity lastEntity) {
        mCurrentWeightEntity = weightEntity;
        mLastWeightEntity = lastEntity;
        initValue();
    }

    public abstract void setWaveViewVisible(boolean visible);
    /**
     * 点击弹出角色列表
     *
     * @param mUserHeadImageView 头像控件
     */
    protected void showRolesList(View mUserHeadImageView, RoleListPop.OnCloseCallback closeCallback) {
        // 点击弹出角色列表
        RoleListPop roleListPop = new RoleListPop(getActivity(), Account.getInstance(getActivity()).findRoleALL());
        roleListPop.setRoleCheckImp(new RoleListPop.RoleCheckImp() {
            @Override
            public void checkRoleResult(RoleInfo roleInfo) {
                if (roleInfo == null) {
                    if (RoleDB.getInstance(getActivity()).getRoleCount(Account.getInstance(getActivity()).getAccountInfo().getId()) >= 8) {
                        CustomToast.showToast(getContext(), getString(R.string.myselfNoAdd), Toast.LENGTH_SHORT);
                        return;
                    }
                } else {
                    Account.getInstance(getContext()).setRoleInfo(roleInfo);
                    doChangeRole();
                }
            }
        });
        roleListPop.setCloseCallback(closeCallback);
        roleListPop.showPop(mUserHeadImageView);
    }

    public abstract void onActivityResume();

    private Timer timer = new Timer();
    private boolean enable = true;
    protected void toReportActivity() {
        if(null == mCurrentWeightEntity) {
            return;
        }

        if(!enable) {
            return;
        }
        enable = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                enable = true;
            }
        }, 1500);

        Intent intent = new Intent(getActivity(), HaierReportActivity.class);
        intent.putExtra(HaierReportActivity.INTENT_KEY_WEIGHT, mCurrentWeightEntity);
        intent.putExtra(HaierReportActivity.INTENT_KEY_LAST_WEIGHT, mLastWeightEntity);
        intent.putExtra(HaierReportActivity.INTENT_KEY_FROM_HOME, true);
//        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_bottom_in, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ROLE_ADD_REQUEST) {
            if (data != null) {
                RoleInfo roleInfo = data.getParcelableExtra(RoleInfo.ROLE_KEY);
                Account.getInstance(getContext()).setRoleInfo(roleInfo);
                doChangeRole();
            }
        }
    }

    protected void doChangeRole() {
        // 同步角色信息到1.2协议秤
        BLEController.create(getContext()).reSelecteRole12();
        HomeFragment homeFragment = (HomeFragment) getParentFragment();
        homeFragment.onRoleChange();
    }

    protected abstract void initValue();

    /**
     * 设置蓝牙状态栏信息
     *
     * @param bluetoothIcon 蓝牙图标
     */
    public abstract void setMsgLayout(int bluetoothIcon);

    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param temp 临时体重
     */
    public abstract void setTempBluetoothWeight(WeightEntity temp);
}
