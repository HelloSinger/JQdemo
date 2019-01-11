package com.jq.btc.homePage.home.haier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.ConstantUrl;
import com.jq.btc.InitActivity;
import com.jq.btc.adapter.ViewPagerMainAdapter;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.bluettooth.WeightMatchingActivity;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.NewMainActivity;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.SpUtils;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.business.SoundPlayer;
import com.jq.code.code.db.RoleDB;
import com.jq.code.code.db.WeightTmpDB;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

/**
 * 海尔动态页
 * Created by lijh on 2017/7/19.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R2.id.mRealContent)
    FrameLayout mRealContent;
    @BindView(R2.id.vp_main)
    ViewPager vp_main;
    Unbinder unbinder;
    UserData userData;
    private ViewPagerMainAdapter viewPagerMainAdapter;
    private List<NormalFragment> fragments;

    private Handler mHandler = new Handler();
    private RoleInfo mCurRoleInfo;
    private int mCurRoleType = -1;
    private int mCurRoleId = -1;
    private BaseFragment mCurFragment;
    private WeightEntity curEntity;
    private WeightEntity lastEntity;
    private boolean mShouldRefresh = false;
    private RadioButton radioButton;
    private NewMainActivity context;


    @SuppressLint("ValidFragment")
    public HomeFragment(RadioButton radioButton, NewMainActivity context) {
        this.radioButton = radioButton;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_home, null, false);
        unbinder = ButterKnife.bind(this, view);
        getUseList();
        initValue();
        return view;
    }

    private void initValue() {
        loadDataAndShowView();
    }

    private void loadDataAndShowView() {
        mShouldRefresh = false;
        showFragment(curEntity, lastEntity);
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                showFragment(curEntity, lastEntity);
//            }
//        });
    }

    private void showFragment(WeightEntity currentEntity, WeightEntity lastEntity) {
        NormalFragment fragment = null;
        fragments = new ArrayList<>();

//        for (int i = 0; i <2; i++) {
//            fragment = new NormalFragment(radioButton, this);
//            fragments.add(fragment);
//        }
        fragment.setWeightEntity(currentEntity, lastEntity);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        if (mCurFragment != null) {
//            transaction.hide(mCurFragment);
//            transaction.remove(mCurFragment);
//        }
//        if (fragment.isAdded()) {
//            transaction.show(fragment);
//        } else {
//            transaction.add(R.id.mRealContent, fragment);
//            transaction.show(fragment);
//        }
        mCurFragment = fragment;
//        transaction.commitAllowingStateLoss();
        viewPagerMainAdapter = new ViewPagerMainAdapter(getFragmentManager(), fragments);
        vp_main.setAdapter(viewPagerMainAdapter);

    }

    public void setOnResume() {
        doRefreshIfNeeded();
    }

    public void setWaveViewVisible(boolean visible) {
        if (null != mCurFragment) {
//            mCurFragment.setWaveViewVisible(visible);
        }
    }

    /**
     * 当前角色被新增了一条体重
     *
     * @param entity          体重
     * @param isFromBluetooth 是否来自蓝牙
     */
    public void onWeightAdded(WeightEntity entity, boolean isFromBluetooth) {
        lastEntity = curEntity;
        curEntity = entity;
//        if (null != mCurFragment && mCurFragment.isAdded()) {
        mCurFragment.setWeightEntity(entity, lastEntity);
//        }
    }

    public void onRoleChange() {
        loadDataAndShowView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getUseList() {
        JSONObject use = new JSONObject();
        use.put("user_id", UserUtils.get().userId());
        JSONObject data = new JSONObject();
        data.put("app", use);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("verify_info", data);
        Log.e("AYD", "--->" + jsonObject.toJSONString());
        OkGo.<String>post(ConstantUrl.GET_USER_URL)
                .upJson(String.valueOf(jsonObject))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        userData = new Gson().fromJson(response.body(), UserData.class);
                        Log.e("AYD", "onSuccess: " + userData.getData().getMemberList().size());

                    }
                });
    }

    // ------------------------------ 蓝牙 start --------------------------------------------
    private static final int ADD_ROLE_REQUEST = 12;
    private static final int REQUEST_CODE_LOCATION_SETTINGS = 88;
    /**
     * 蓝牙管理类
     */
    private BLEController mBleController;
    private SoundPlayer mSoundPlayer;
    /**
     * 是否正在弹窗让用户选择多用户匹配
     */
    private boolean mV14MatchingUser = false;
    /**
     * 从蓝牙获得的体重信息？
     */
    private WeightEntity mMatchWeight;
    /**
     * 蓝牙状态标志
     */
    private int mBluetoothState;
    private int mBluetoothIcon;
    /**
     * 蓝牙状态消息
     */
    private String mBluetoothMsg;
    private int historyCount;

    private ScaleParser mScalePresser;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 0;
    private BleHelper mBle;
    private boolean mScanning;
    Handler myhandler = new Handler();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBleController = BLEController.create(getContext());
        mSoundPlayer = new SoundPlayer(getContext(), "Tethys.ogg");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSoundPlayer != null) {
            mSoundPlayer.release();
            mSoundPlayer = null;
        }

//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLocalReceiver);
    }

    public class OnBlueToothLayoutClick implements View.OnClickListener {
        private String msg;

        public OnBlueToothLayoutClick(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View v) {
            if (getContext().getString(R.string.reportBoundTip).equals(msg)) {
                // 显示箭头，且点击去绑定
                startActivity(new Intent(getActivity(), BoundDeviceActivity.class));
            } else if (getContext().getString(R.string.locationServiceNotOpen).equals(msg)) {
                // 显示箭头，且点击打开设置
                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
            } else if (getContext().getString(R.string.matchDataTip).equals(msg)) {
                // 显示箭头，且点击打开 认领体重数据界面
                startActivity(new Intent(getActivity(), WeightMatchingActivity.class));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (null != mCurFragment && mCurFragment.isAdded()) {
//                mCurFragment.setMsgLayout(mBluetoothIcon);
            }
        } else if (resultCode == RESULT_OK && requestCode == ADD_ROLE_REQUEST) {
            if (data != null) {
                RoleInfo roleInfo = Account.getInstance(getContext()).getRoleInfo();
                addNewRoleData(mMatchWeight, roleInfo);
                mBleController.reSendAllRoleInfoV11();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initBlutooth() {
        if (mBleController != null) {
            mBleController.registerReceiver(getActivity());
            mBleController.setOnBLEChangeListener(onBlEChangeListener);
            mBleController.connectBluetooth();
            mBleController.setBound(false);
            mBleController.setReConnectable(true);
            mBleController.initState();
        }
    }

    public String getMsg() {
        if (mBleController != null) {
            if (!mBleController.isLocationEnable(getContext())) {
                return getContext().getString(R.string.locationServiceNotOpen);
            }
            if (!mBleController.isBluetoothEnable()) {
                return getContext().getString(R.string.reportStateClose);
            }

            if (!ScaleParser.getInstance(getContext()).isBluetoothBounded()) {
                return getContext().getString(R.string.reportBoundTip);
            }

            //接入乐心协议后调整优先级，乐心的数据全部采用认领模式
            if (WeightTmpDB.getInstance(getContext()).getCount(Account.getInstance(getContext()).getAccountInfo().getId()) > 0) {
                return getContext().getString(R.string.matchDataTip);
            }

            if (historyCount > 0) {
                return String.format(getContext().getString(R.string.weighBleTip0), historyCount);
            }

            return mBluetoothMsg;
        } else {
            return getContext().getString(R.string.reportBoundTip);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mBleController != null) {
            mBleController.unregisterReceiver(getActivity());
            mBleController.setBound(false);
            mBleController.setReConnectable(false);
            mBleController.disconnectBluetooth();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mBleController == null) return;
        if (isVisibleToUser) {
            onBlEChangeListener.syncHistoryEnd(null);
            mBleController.initState();
        } else {
            mBleController.disconnectBluetooth();
        }
    }

    @Override
    public void onResume() {
        initBlutooth();
        onBlEChangeListener.syncHistoryEnd(null);
        super.onResume();
        doRefreshIfNeeded();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            doRefreshIfNeeded();
        }
    }

    private void doRefreshIfNeeded() {
        loadDataAndShowView();
    }

    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param isLock
     * @param entity 体重数据
     */
    private void onShowBluetoothTempWeightData(boolean needShowAnim, boolean isLock, WeightEntity entity) {
        for (NormalFragment fragment : fragments) {
            fragment.setTempBluetoothWeight(entity);
        }

//        NormalFragment fragment = (NormalFragment) fragments.get(0);
////        if (null != mCurFragment && mCurFragment.isAdded()) {
//        fragment.setTempBluetoothWeight(entity);
//        }
    }

    /**
     * 播放提示音
     *
     * @param isChangeRole 是否切换角色
     */
//    private void lockAnim(boolean isChangeRole) {
//        if (isChangeRole) {
//            mSoundPlayer.play();
//            try {
//                Thread.sleep(600);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } else {
//            mSoundPlayer.play();
//        }
//    }

    /**
     * 把蓝牙得到的一条体重数据，用来匹配某个角色
     *
     * @param roleDataInfo 体重数据
     */
    private void onWeight(WeightEntity roleDataInfo) {
        RoleInfo roleInfo = Account.getInstance(getContext()).getRoleInfo();
        addNewRoleData(roleDataInfo, roleInfo);
    }


    /**
     * 把蓝牙得到的、并匹配好某个角色的体重数据，保存到数据库和上传到服务器，并实际添加到列表中
     *
     * @param roleDataInfo 得到的蓝牙体重
     * @param roleInfo     匹配的角色数据
     */
    private void addNewRoleData(final WeightEntity roleDataInfo, RoleInfo roleInfo) {
//        if (!Account.getInstance(getActivity()).isAccountLogined()) {
//            roleDataInfo.setAccount_id(Account.getInstance(getActivity()).getAccountInfo().getId());
//            roleDataInfo.setRole_id(roleInfo.getId());
//            roleDataInfo.setBmi(0);
//
//            lockAnim(false);
//            onWeightAdded(roleDataInfo, true);
//        } else {
        WeighDataParser.create(getContext()).fillFatWithSmoothImpedance(roleDataInfo, roleInfo);
//
//            ThreadUtil.executeThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (getActivity() != null) {
//
//                    }
//                }
//            });
//            LocalBroadcastUtil.notifyBlueToothAddWeight(getActivity());
//            if (Account.getInstance(getContext()).getRoleInfo().getId() != roleInfo.getId()) {
//                lockAnim(true);
//                Account.getInstance(getContext()).setRoleInfo(roleInfo);
//                onRoleChange();
//            } else {
//        lockAnim(false);
        onWeightAdded(roleDataInfo, true);
//            }
//        }
    }

    private void onMachWeightEntity(WeightEntity entity, ArrayList<Integer> matchedRoleList) {
        if (matchedRoleList == null) return;
        mMatchWeight = entity;
//        if (matchRoleDialog != null) {
//            if (matchRoleDialog.isShowing()) {
//                matchRoleDialog.dismiss();
//            }
//            matchRoleDialog = null;
//        }

        List<RoleInfo> roleInfos = findRolesbyID(matchedRoleList);
        if (roleInfos.isEmpty()) {
            LogUtil.e(TAG, "Match not to role");
        } else {
//            matchRoleDialog = new MatchRoleDialog(getContext(), mMatchWeight, roleInfos, checker, false);
//            matchRoleDialog.setOnGridItemClicklisenter(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mBleController.reSelecteRole(matchRoleDialog.getRole(position).getId());
//                    matchRoleDialog.dismiss();
//                }
//            });
//            matchRoleDialog.showDialog();
        }
    }

    private List<RoleInfo> findRolesbyID(ArrayList<Integer> matchedRoleList) {
        List<RoleInfo> roleInfos = new ArrayList<>();
        if (matchedRoleList != null && !matchedRoleList.isEmpty()) {
            for (Integer id : matchedRoleList) {
                RoleInfo roleInfo = RoleDB.getInstance(getActivity()).findRoleById(Account.getInstance(getActivity()).getAccountInfo().getId(), id);
                roleInfos.add(roleInfo);
            }
        }
        return roleInfos;
    }

//    private void setBLEState(int currentImage) {
//        mBluetoothIcon = currentImage;
//        if (mBluetoothMsg == null) return;
//        if (getContext() == null) return;
//
//        if (null != mCurFragment && mCurFragment.isAdded()) {
//            mCurFragment.setMsgLayout(currentImage);
//        }
//    }

    private BLEController.OnBlEChangeListener onBlEChangeListener = new BLEController.OnBlEChangeListener() {

        /**
         * 匹配用户回调（用于OKOK V1.5秤）
         * @param entity 当前测量结果(包含体重和单位)
         * @param matchedRoleList 相近体重用户列表
         */
        public void onMachRole(final WeightEntity entity, final ArrayList<Integer> matchedRoleList) {
            if (getActivity() == null) return;
            if (!mV14MatchingUser) {
                mV14MatchingUser = true;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onMachWeightEntity(entity, matchedRoleList);
                    }
                });
            }
        }

        /**
         * 测量结果回调(对所有秤有效)
         * @param isLock 表示当前测量结果是否锁定
         * @param data 测量结果（非锁定仅包含体重、单位；锁定数据包含体重、单位、脂肪等）
         */
        public void onDataChange(final boolean isLock, final WeightEntity data) {
            if (getActivity() == null) return;
            if (!isLock) {
                mV14MatchingUser = false;
            }
            if (!Account.getInstance(getContext()).isAccountLogined()) {
                data.setAxunge(0);
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (getContext() == null) return;
                    onShowBluetoothTempWeightData(true, isLock, data);
                    if (isLock) {
                        onWeight(data);
                    }
                }
            });
        }

        /**
         * 蓝牙状态改变回调
         * @param state 当前蓝牙状态
         * @param msg 蓝牙状态消息
         * @param currentImge 蓝牙状态对应图标
         */
        @Override
        public void onStateChange(final int state, final String msg, final int currentImge) {
            if (state == 2 || state == 0) {
                if (mBle != null) {
                    mBle.stopScan();
                    myhandler.removeCallbacksAndMessages(null);
                }
            }
            if (getActivity() == null) {
                return;
            }

            mBluetoothState = state;
            mBluetoothMsg = msg;

            if (state == CsBtUtil_v11.STATE_BLE_CLOSE || state == CsBtUtil_v11.STATE_BLE_OPEN || state == CsBtUtil_v11.STATE_CLOSE) {
                mV14MatchingUser = false;
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    setBLEState(currentImge);
                }
            });
        }

        @Override
        public void onBound(ScaleInfo scaleInfo, String value, int tip) {
        }

        /**
         * 同步历史数据回调
         * @param weightEntities 历史数据
         */
        public void syncHistoryEnd(List<PutBase> weightEntities) {
            if (weightEntities != null && !weightEntities.isEmpty()) {
                historyCount = weightEntities.size();
            }
            if (null != mCurFragment && mCurFragment.isAdded()) {
//                mCurFragment.setMsgLayout(mBluetoothIcon);
            }
        }
    };


}
