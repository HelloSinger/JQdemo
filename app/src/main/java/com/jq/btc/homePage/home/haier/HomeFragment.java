package com.jq.btc.homePage.home.haier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.bluettooth.WeightMatchingActivity;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.NewMainActivity;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.DelayTimer;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.business.SoundPlayer;
import com.jq.code.code.db.RoleDB;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.db.WeightTmpDB;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CustomToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * 海尔动态页
 * Created by lijh on 2017/7/19.
 */

@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R2.id.mRealContent)
    FrameLayout mRealContent;
    @BindView(R2.id.mWholeLayout)
    LinearLayout mWholeLayout;
    Unbinder unbinder;

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
    public HomeFragment(RadioButton radioButton,NewMainActivity context){
        this.radioButton = radioButton;
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haier_home, null, false);
        unbinder = ButterKnife.bind(this, view);
        initValue();

//        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(LocalBroadcastUtil.ACTION_WEIGHTS_SYNC);
//        intentFilter.addAction(LocalBroadcastUtil.ACTION_TEMP_WEIGHT_DATA_MATCH_ROLE);
//        intentFilter.addAction(LocalBroadcastUtil.ACTION_DELETE_WEIGHT);
//        intentFilter.addAction(LocalBroadcastUtil.ACTION_ROLE_PREGNANT_MODE_CHANGE);
//
//        localBroadcastManager.registerReceiver(mLocalReceiver, intentFilter);
        return view;
    }

    private void initValue() {
//        Account account = Account.getInstance(getActivity());
//        if (account.isAccountLogined()) {
//            boolean accountLoaded = Account.getInstance(getActivity()).isCurrentAccountDataLoaded();
//            // 是否该账号的全量数据已加载
//            if (accountLoaded) {
//                loadDataAndShowView();
//            } else {
//            }
//        } else {
            // 访客模式（未登录模式）
            loadDataAndShowView();
//        }
    }

    private void loadDataAndShowView() {
        mShouldRefresh = false;
//        ThreadUtil.executeThread(new Runnable() {
//            @Override
//            public void run() {
//                Account account = Account.getInstance(getActivity());
//                mCurRoleInfo = account.getRoleInfo();
//                int roleType = mCurRoleInfo.getRole_type();
//                int roleId = mCurRoleInfo.getId();
//                List<WeightEntity> weightEntities = WeightDataDB.getInstance(getActivity()).loadLatestWeight(account.getAccountInfo().getId(), mCurRoleInfo.getId(), 2);
//                WeightEntity first = null != weightEntities && weightEntities.size() > 0 ? weightEntities.get(0) : null;
//                WeightEntity second = null != weightEntities && weightEntities.size() > 1 ? weightEntities.get(1) : null;
//                // 比较下两个体重是否一样，都一样就不必刷新。
//                boolean same1 = false;
//                boolean same2 = false;
//                if (first != null && curEntity != null) {
//                    same1 = (first.getRole_id() == curEntity.getRole_id()) && (first.getWeight_time().equals(curEntity.getWeight_time()));
//                } else if (first == null && curEntity == null) {
//                    same1 = true;
//                } else {
//                    same1 = false;
//                }
//                if (!same1) {
//                    curEntity = first;
//                }
//
//                if (second != null && lastEntity != null) {
//                    same2 = (second.getRole_id() == lastEntity.getRole_id()) && (second.getWeight_time().equals(lastEntity.getWeight_time()));
//                } else if (second == null && lastEntity == null) {
//                    same2 = true;
//                } else {
//                    same2 = false;
//                }
//                if (!same2) {
//                    lastEntity = second;
//                }
//
//                boolean same3 = false;
//                if(mCurRoleType != roleType) {
//                    mCurRoleType = roleType;
//                    same3 = false;
//                } else {
//                    same3 = true;
//                }
//
//                boolean same4 = false;
//                if(mCurRoleId != roleId) {
//                    mCurRoleId = roleId;
//                    same4 = false;
//                } else {
//                    same4 = true;
//                }
//
//                if (!same1 || !same2 || !same3 || !same4 || null == mCurFragment) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showFragment(curEntity, lastEntity);
                        }
                    });
//                }
//            }
//        });
    }

    private void showFragment(WeightEntity currentEntity, WeightEntity lastEntity) {
        BaseFragment fragment;


            fragment = new NormalFragment(radioButton,this);

        fragment.setWeightEntity(currentEntity, lastEntity);

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (mCurFragment != null) {
            transaction.hide(mCurFragment);
            transaction.remove(mCurFragment);
        }
        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(R.id.mRealContent, fragment);
            transaction.show(fragment);
        }
        mCurFragment = fragment;
        transaction.commitAllowingStateLoss();
    }

    public void setOnResume() {
        doRefreshIfNeeded();
    }

    public void setWaveViewVisible(boolean visible) {
        if(null != mCurFragment) {
            mCurFragment.setWaveViewVisible(visible);
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

//    private BroadcastReceiver mLocalReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action.equals(LocalBroadcastUtil.ACTION_WEIGHTS_SYNC)) {
//                // 从服务端更新体重信息完成
//                mShouldRefresh = true;
//            } else if (LocalBroadcastUtil.ACTION_DELETE_WEIGHT.equals(action)) {
//                // 删除体重
//                mShouldRefresh = true;
//            } else if (LocalBroadcastUtil.ACTION_TEMP_WEIGHT_DATA_MATCH_ROLE.equals(action)) {
//                // 认领未匹配角色的体重数据完成，刷新整体界面（因为不确定匹配 的角色是否当前角色）
//                mShouldRefresh = true;
//            } else if(LocalBroadcastUtil.ACTION_ROLE_PREGNANT_MODE_CHANGE.equals(action)) {
//                // 有角色的孕妇模式改变了
//                Account account = Account.getInstance(getActivity());
//                int curRoleId = account.getRoleInfo().getId();
//                int rId = intent.getIntExtra("dataKey", curRoleId);
//                if(rId == curRoleId) {
//                    // 当前角色的孕妇模式改变了
//                    mShouldRefresh = true;
//                }
//            }
//        }
//    };

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
//        mScalePresser = ScaleParser.getInstance(getContext());
//        SharedPreferences sh = getActivity().getSharedPreferences("kitchens",Context.MODE_PRIVATE);
//        sh.getString("kitchen_name","00");
//        if(!getMsg().equals("00")){
//            openBluetoothScanDevice();
//        }

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
                mCurFragment.setMsgLayout(mBluetoothIcon);
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
        if(mBleController != null) {
            mBleController.registerReceiver(getActivity());
            mBleController.setOnBLEChangeListener(onBlEChangeListener);
            mBleController.connectBluetooth();
            mBleController.setBound(false);
            mBleController.setReConnectable(true);
            mBleController.initState();
        }
    }

    public String getMsg() {
        if(mBleController != null) {
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
        }else {
            return getContext().getString(R.string.reportBoundTip);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if(mBleController != null) {
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
//        if (mShouldRefresh) {
            // 需要刷新
            loadDataAndShowView();
//        } else if(null != mCurFragment) {
//            mCurFragment.onActivityResume();
//        }
    }

    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param isLock
     * @param entity 体重数据
     */
    private void onShowBluetoothTempWeightData(boolean needShowAnim, boolean isLock, WeightEntity entity) {
        if (null != mCurFragment && mCurFragment.isAdded()) {
            mCurFragment.setTempBluetoothWeight(entity);
        }
    }

    /**
     * 播放提示音
     *
     * @param isChangeRole 是否切换角色
     */
    private void lockAnim(boolean isChangeRole) {
        if (isChangeRole) {
            mSoundPlayer.play();
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            mSoundPlayer.play();
        }
    }

    /**
     * 把蓝牙得到的一条体重数据，用来匹配某个角色
     *
     * @param roleDataInfo 体重数据
     */
    private void onWeight(WeightEntity roleDataInfo) {
        RoleInfo roleInfo = Account.getInstance(getContext()).getRoleInfo();
        addNewRoleData(roleDataInfo, roleInfo);
    }

    private void showMatchDailog(ArrayList<RoleInfo> roleInfos, WeightEntity entity) {
//        mMatchWeight = entity;
//        if (matchRoleDialog != null) {
//            if (matchRoleDialog.isShowing()) {
//                matchRoleDialog.dismiss();
//            }
//            matchRoleDialog = null;
//        }
//        matchRoleDialog = new MatchRoleDialog(getContext(), mMatchWeight, roleInfos, true);
//        matchRoleDialog.setOnGridItemClicklisenter(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (getActivity() == null) return;
//                RoleInfo roleInfo = matchRoleDialog.getRole(position);
//                if (roleInfo == null) {
//                    statrtAddRoleActivity();
//                } else {
//                }
//                matchRoleDialog.dismiss();
//            }
//        });
//        matchRoleDialog.showDialog();
    }

    public void statrtAddRoleActivity() {
        if (RoleDB.getInstance(getActivity()).getRoleCount(Account.getInstance(getActivity()).getAccountInfo().getId()) >= 8) {
            CustomToast.showToast(getContext(), getString(R.string.myselfNoAdd), Toast.LENGTH_SHORT);
            return;
        }
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
                lockAnim(false);
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

    private DelayTimer checker = new DelayTimer(new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
//            if (matchRoleDialog != null) {
//                if (matchRoleDialog.getRole() != null) {
//                    mBleController.reSelecteRole(matchRoleDialog.getRole().getId());
//                }
//                matchRoleDialog.dismiss();
//            }
            return true;
        }
    }));

    public int getBluetoothIcon() {
        return mBluetoothIcon;
    }

    private void setBLEState(int currentImage) {
        mBluetoothIcon = currentImage;
        if (mBluetoothMsg == null) return;
        if (getContext() == null) return;

        if (null != mCurFragment && mCurFragment.isAdded()) {
            mCurFragment.setMsgLayout(currentImage);
        }
    }

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
            if(state == 2||state == 0){
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
                    setBLEState(currentImge);
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
                mCurFragment.setMsgLayout(mBluetoothIcon);
            }
        }
    };

    public void setGridus(){
        context.clickable();
    }
    // ------------------------------ 蓝牙 end --------------------------------------------


//    List<BluetoothGattService> servicesList = new ArrayList<>();
//
//    private BleReadOrWriteCallback bleReadOrWriteCallback = new BleReadOrWriteCallback() {
//        @Override
//        public void onReadSuccess() {
//
//        }
//
//        @Override
//        public void onReadFail(int errorCode) {
//
//        }
//
//        @Override
//        public void onWriteSuccess() {
//
//        }
//
//        @Override
//        public void onWriteFail(int errorCode) {
//
//        }
//
//        @Override
//        public void onServicesDiscovered(int state) {
//            servicesList = CSApplication.getInstance().getmBle().getServicesList();
//            Log.v("===listv",""+servicesList.size());
//            for (int i = 0; i < servicesList.size(); i++) {
//                boolean bs = CSApplication.getInstance().getmBle().characteristicNotification(servicesList.get(i).getUuid().toString());
//                if(bs){
//                    Log.v("===logs",bs+"||"+i);
//                    startActivity(new Intent(getActivity(),Kitchen_Weigh_Activity.class));
//                }else {
//                    Log.v("===logs",bs+"||"+i);
//                }
//            }
//        }
//
//        @Override
//        public void onDiscoverServicesFail(int errorCode) {
//
//        }
//    };
//    BLEDevice currDevice;
//
//    private BleConnectionCallback connectionCallback = new BleConnectionCallback() {
//        @Override
//        public void onConnectionStateChange(int status, int newState) {
//            Log.e(TAG, "连接状态发生变化：" + status + "     " + newState);
//            if (newState == BluetoothProfile.STATE_CONNECTED) {
//                currDevice.setConntect(true);
//                mBle.discoverServices(currDevice.getBluetoothDevice().getAddress(), bleReadOrWriteCallback);
//
//            } else {
//                currDevice.setConntect(false);
//            }
//        }
//
//        @Override
//        public void onFail(int errorCode) {
//            Log.e(TAG, "连接失败：" + errorCode);
//
//        }
//    };
//    private BleScanResultCallback resultCallback = new BleScanResultCallback() {
//        @Override
//        public void onSuccess() {
//
//            Log.d(TAG, "开启扫描成功");
//        }
//
//        @Override
//        public void onFail() {
//            Log.d(TAG, "开启扫描失败");
//        }
//
//        @Override
//        public void onFindDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
//            currDevice = new BLEDevice();
//            currDevice.setBluetoothDevice(device);
//            Log.d(TAG, "扫描到新设备：" + currDevice.getBluetoothDevice().getName() + "     " + device.getAddress());
//            if(!BluetoothAdapter.getDefaultAdapter().isEnabled()){
//                //检查下当前是否在进行扫描 如果是则先停止
//                if (mBle != null && mScanning) {
//                    mBle.stopScan();
//                }
//            }
//
//            if("Chipsea-BLE".equals(device.getName())){
//                mBle.stopScan();
//                mScanning = false;
//                mBle.requestConnect(currDevice.getBluetoothDevice().getAddress(), connectionCallback, true);
//            }
//        }
//    };
//
//    private void scanLeDevice(final boolean enable) {
//        //获取ble操作类
//        mBle = CSApplication.getInstance().getmBle();
//        if (mBle == null) {
//            return;
//        }
//        if (enable) {
//            //开始扫描
//            if (mBle != null) {
//                boolean startscan = mBle.startScan(resultCallback);
//                if (!startscan) {
//                    Toast.makeText(getActivity(), "开启蓝牙扫描失败，请检查蓝牙是否正常工作！", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                mScanning = true;
//                //扫描一分钟后停止扫描
////                myhandler.postDelayed(stopRunnable, 3000);
//
//            }
//        } else {
//            //停止扫描
//            mScanning = false;
//            if (mBle != null) {
//                mBle.stopScan();
//                myhandler.removeCallbacksAndMessages(null);
//            }
//        }
//
//    }
//
//    private Runnable stopRunnable = new Runnable() {
//        @Override
//        public void run() {
//            mBle.stopScan();
//            mScanning = false;
//
//        }
//    };
//
//
//    /**
//     * 检测蓝牙是否打开
//     */
//    void openBluetoothScanDevice() {
//        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//            //蓝牙没打开则去打开蓝牙
//            boolean openresult = toEnable(BluetoothAdapter.getDefaultAdapter());
//            if (!openresult) {
//                Toast.makeText(getActivity(), "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
//                return;
//            }
//            //停个半秒再检查一次
//            SystemClock.sleep(500);
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    int i = 0;
//                    while (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//                        try {
//                            Thread.sleep(500);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (i >= 15) {
//                            Toast.makeText(getActivity(), "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
//                            break;
//                        } else {
//                            i++;
//                        }
//
//                    }
//                    //发现蓝牙打开了，则进行开启扫描的步骤
//                    scanDevice();
//                }
//            });
//        }else {
//            //检查下当前是否在进行扫描 如果是则先停止
//            if (mBle != null && mScanning) {
//                mBle.stopScan();
//            }
//            scanDevice();
//        }
//    }
//
//    @UiThread
//    void scanDevice() {
//        //如果此时发蓝牙工作还是不正常 则打开手机的蓝牙面板让用户开启
//        if (mBle != null && !mBle.adapterEnabled()) {
//            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//        }
//
//        myhandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //检查一下去那些，如果没有则动态请求一下权限
//                requestPermission();
//                //开启扫描
//                scanLeDevice(true);
//            }
//        }, 500);
//    }
//
//    synchronized private void requestPermission() {
//        //TODO 向用户请求权限
//        int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH);
//        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH}, REQUEST_FINE_LOCATION);
//            Toast.makeText(getActivity(), "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//
//        }
//    }
//
//
//
//    private boolean toEnable(BluetoothAdapter bluertoothadapter) {
//        //TODO 启动蓝牙
//        boolean result = false;
//        try {
//            for (Method temp : Class.forName(bluertoothadapter.getClass().getName()).getMethods()) {
//                if (temp.getName().equals("enableNoAutoConnect")) {
//                    result = (boolean) temp.invoke(bluertoothadapter);
//                }
//            }
//        } catch (Exception e) {
//            //反射调用失败就启动通过enable()启动;
//            result = bluertoothadapter.enable();
//            Log.d(TAG, "启动蓝牙的结果:" + result);
//            e.printStackTrace();
//
//        }
//        return result;
//
//    }


}
