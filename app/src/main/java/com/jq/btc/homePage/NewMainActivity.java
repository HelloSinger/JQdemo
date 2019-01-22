package com.jq.btc.homePage;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.ConstantUrl;
import com.jq.btc.adapter.ViewPagerMainAdapter;
import com.jq.btc.app.R;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.bluettooth.WeightMatchingActivity;
import com.jq.btc.dialog.WeightDialog;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.home.haier.BaseFragment;
import com.jq.btc.homePage.home.haier.NormalFragment;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.SpUtils;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.business.SoundPlayer;
import com.jq.code.code.db.RoleDB;
import com.jq.code.code.db.WeightTmpDB;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CustomToast;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.umeng.analytics.MobclickAgent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by xulj on 2016/5/9.
 * 体脂秤首页
 */
public class NewMainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener
        , View.OnClickListener, ViewPager.OnPageChangeListener, NormalFragment.ViewPagerCurrentListener {
    private RadioGroup mTabRg;
    private int showTabIndex = -1;
    private List<NormalFragment> mFragments;
    private Fragment curFragment;
    private int mPreRadioId = R.id.dynamic_rb;
    private NormalFragment normalFragment;
    private RadioButton trend_rb, dynamic_rb, trend_shop, find_rb, me_rb;
    private ImageView iv_back;
    private BaseFragment baseFragment;
    private ViewPagerMainAdapter viewPagerMainAdapter;
    private UserData userData;
    private WeightDialog weightDialog;
    private ViewPager vp;
    private NumberFormat df;
    private LinearLayout ll_unserstand_scene;
    private ImageView iv_left, iv_right;
    private RelativeLayout rl_left, rl_right;
    private int pos;
    private WeightEntity curEntity;
    private WeightEntity lastEntity;
    private String useIds;
    List<UserData.DataBean.MemberListBean> userDataList;
    private TextView tv_no_user;
    private TextView tv_ble;

    private LinearLayout ll_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("AYD", "onCreate");
        setContentView(R.layout.main);
        ActivityUtil.getInstance().addActivity(this);
        mBleController = BLEController.create(this);
        mBleController.setOnBLEChangeListener(onBlEChangeListener);
        mBleController.setBound(true);
        mSoundPlayer = new SoundPlayer(this, "Tethys.ogg");
        userDataList = new ArrayList<>();
        mFragments = new ArrayList<>();
        initView();
//        getUseList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        if (showTabIndex == 0) {
//            homeFragment.setOnResume();
//        }
//        initBlutooth();
        Log.e("AYD", "------onResume");
//        showLoadProgress();
        getUseList();
        onBlEChangeListener.syncHistoryEnd(null);
        doRefreshIfNeeded();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initBlutooth();
    }

    private void initView() {
        df = NumberFormat.getNumberInstance();
        df.setMaximumFractionDigits(1);
        weightDialog = new WeightDialog(this, R.style.WeightDialog);
        weightDialog.setCanceledOnTouchOutside(false);
        vp = findViewById(R.id.vp);
        vp.addOnPageChangeListener(this);
        trend_rb = findViewById(R.id.trend_rb);
        dynamic_rb = findViewById(R.id.dynamic_rb);
        trend_shop = findViewById(R.id.trend_shop);
        find_rb = findViewById(R.id.find_rb);
        me_rb = findViewById(R.id.me_rb);
        mTabRg = findViewById(R.id.main_tab);
        iv_back = findViewById(R.id.iv_back);
        iv_left = findViewById(R.id.iv_left);
        rl_left = findViewById(R.id.rl_left);
        rl_right = findViewById(R.id.rl_right);
        tv_ble = findViewById(R.id.tv_ble);
        ll_loading = findViewById(R.id.ll_loading);
        ll_unserstand_scene = findViewById(R.id.ll_unserstand_scene);
        ll_unserstand_scene.setOnClickListener(this);
        rl_left.setOnClickListener(this);
        iv_right = findViewById(R.id.iv_right);
        tv_no_user = findViewById(R.id.tv_no_user);
        rl_right.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        mTabRg.setOnCheckedChangeListener(this);
        mFragments = new ArrayList<>();
        if (mBleController != null) {
            if (mBleController.isBluetoothEnable()) {
                tv_ble.setVisibility(View.GONE);
            } else {
                tv_ble.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.dynamic_rb) {
            showTabIndex = 0;
        }
        mPreRadioId = checkedId;
//        setFragment(showTabIndex);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            ActivityUtil.getInstance().AppExit(this);
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
        CustomToast.showToast(this, getString(R.string.keyback), Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_left:
                if (pos != 0) {
                    iv_left.setBackgroundResource(R.mipmap.icon_left_select);
                    iv_right.setBackgroundResource(R.mipmap.icon_right_select);
                    pos--;
                    vp.setCurrentItem(pos, true);
                }
                break;
            case R.id.rl_right:
                if (pos != userData.getData().getMemberList().size() - 1) {
                    iv_left.setBackgroundResource(R.mipmap.icon_left_select);
                    iv_right.setBackgroundResource(R.mipmap.icon_right_select);
                    pos++;
                    vp.setCurrentItem(pos, true);
                }
                break;
            case R.id.ll_unserstand_scene:
                SpUtils.getInstance(NewMainActivity.this).cleanMak();
                System.exit(0);
                break;
        }
    }

    /**
     * 请求冰箱用户列表
     */
    private void getUseList() {
        final JSONObject use = new JSONObject();
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
                        ll_loading.setVisibility(View.GONE);
                        userDataList.clear();
                        mFragments.clear();
                        userData = new Gson().fromJson(response.body(), UserData.class);
                        userDataList = userData.getData().getMemberList();
                        Log.e("AYD", "act:--- " + userDataList.size());
                        Collections.reverse(userDataList);
                        if (userDataList.size() == 0) {
                            tv_no_user.setVisibility(View.VISIBLE);
                        } else {
                            if (userDataList.size() > 1) {
                                rl_right.setVisibility(View.VISIBLE);
                                rl_left.setVisibility(View.VISIBLE);
                            }
                            ArrayList<String> useid = new ArrayList<>();
                            for (int i = 0; i < userDataList.size(); i++) {
                                useid.add(userDataList.get(i).getFamilyMemeberId());
                            }
                            String str = useid.toString();
                            int len = str.length() - 1;
                            useIds = str.substring(1, len).replace(" ", "");
                            SpUtils.getInstance(NewMainActivity.this).setUserid(useIds);
                            Log.e("AYD----->", "" + useIds);
                            for (int i = 0; i < userDataList.size(); i++) {
//                                normalFragment = new NormalFragment(userData, i);
                                normalFragment = new NormalFragment();
                                normalFragment.setUserData(userData, i);
                                mFragments.add(normalFragment);
                            }
                            viewPagerMainAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), mFragments);
                            vp.setAdapter(viewPagerMainAdapter);
                            viewPagerMainAdapter.notifyDataSetChanged();
                            initBlutooth();
                        }
                    }
                });
    }

    //------------------------------------------
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
    public void onDestroy() {
        super.onDestroy();
        if (mBleController != null) {
            mBleController.unregisterReceiver(NewMainActivity.this);
            mBleController.setBound(false);
            mBleController.setReConnectable(false);
            mBleController.disconnectBluetooth();
        }
        Log.e("AYD", "onDestroy");
        if (mSoundPlayer != null) {
            mSoundPlayer.release();
            mSoundPlayer = null;
        }

//        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLocalReceiver);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("AYD", "onPageSelected: " + position);
        pos = position;
        mFragments.get(pos).getUserLastWeight(UserUtils.get().userId(),
                userData.getData().getMemberList().get(pos).getFamilyMemeberId());
        if (position == 0) {
            iv_left.setBackgroundResource(R.mipmap.icon_left_normal);
        } else {
            iv_left.setBackgroundResource(R.mipmap.icon_left_select);
        }
        if (position == userData.getData().getMemberList().size() - 1) {
            iv_right.setBackgroundResource(R.mipmap.icon_right_normal);
        } else {
            iv_right.setBackgroundResource(R.mipmap.icon_right_select);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void setViewPagerCurrent(int pos) {
        vp.setCurrentItem(pos);
    }


    public class OnBlueToothLayoutClick implements View.OnClickListener {
        private String msg;

        public OnBlueToothLayoutClick(String msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(View v) {
            if (getString(R.string.reportBoundTip).equals(msg)) {
                // 显示箭头，且点击去绑定
                startActivity(new Intent(NewMainActivity.this, BoundDeviceActivity.class));
            } else if (getString(R.string.locationServiceNotOpen).equals(msg)) {
                // 显示箭头，且点击打开设置
                Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
            } else if (getString(R.string.matchDataTip).equals(msg)) {
                // 显示箭头，且点击打开 认领体重数据界面
                startActivity(new Intent(NewMainActivity.this, WeightMatchingActivity.class));
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
//            if (null != mCurFragment && mCurFragment.isAdded()) {
//                mCurFragment.setMsgLayout(mBluetoothIcon);
//            }
//        } else
        if (resultCode == RESULT_OK && requestCode == ADD_ROLE_REQUEST) {
            if (data != null) {
                RoleInfo roleInfo = Account.getInstance(NewMainActivity.this).getRoleInfo();
                addNewRoleData(mMatchWeight, roleInfo);
                mBleController.reSendAllRoleInfoV11();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void initBlutooth() {
        if (mBleController != null) {
            mBleController.registerReceiver(NewMainActivity.this);
            mBleController.setOnBLEChangeListener(onBlEChangeListener);
            mBleController.connectBluetooth();
            mBleController.setBound(true);
            mBleController.setReConnectable(true);
            mBleController.initState();
        }
    }

    public String getMsg() {
        if (mBleController != null) {
            if (!mBleController.isLocationEnable(NewMainActivity.this)) {
                return getString(R.string.locationServiceNotOpen);
            }
            if (!mBleController.isBluetoothEnable()) {
                return getString(R.string.reportStateClose);
            }

            if (!ScaleParser.getInstance(NewMainActivity.this).isBluetoothBounded()) {
                return getString(R.string.reportBoundTip);
            }

            //接入乐心协议后调整优先级，乐心的数据全部采用认领模式
            if (WeightTmpDB.getInstance(NewMainActivity.this).getCount(Account.getInstance(NewMainActivity.this).getAccountInfo().getId()) > 0) {
                return getString(R.string.matchDataTip);
            }

            if (historyCount > 0) {
                return String.format(getString(R.string.weighBleTip0), historyCount);
            }

            return mBluetoothMsg;
        } else {
            return getString(R.string.reportBoundTip);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("AYD", "------onPause");
        initBlutooth();
//        if (mBleController != null) {
//            mBleController.unregisterReceiver(NewMainActivity.this);
//            mBleController.setBound(false);
//            mBleController.setReConnectable(false);
//            mBleController.disconnectBluetooth();
//        }
        MobclickAgent.onPause(this);
    }


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (mBleController == null) return;
//        if (isVisibleToUser) {
//            onBlEChangeListener.syncHistoryEnd(null);
//            mBleController.initState();
//        } else {
//            mBleController.disconnectBluetooth();
//        }
//    }


//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            doRefreshIfNeeded();
//        }
//    }

    private void doRefreshIfNeeded() {
//        loadDataAndShowView();
    }

    /**
     * 显示蓝牙秤得到的未锁定的体重数据，即临时的未锁定的体重
     *
     * @param isLock
     * @param entity 体重数据
     */
    private void onShowBluetoothTempWeightData(boolean needShowAnim, boolean isLock, final WeightEntity entity) {
        mFragments.get(pos).setTempBluetoothWeight(entity);
//        mFragments.get(position).setTempBluetoothWeight(entity);

//        for (NormalFragment fragment : fragments) {
//            fragment.setTempBluetoothWeight(entity);
//        }

//        NormalFragment fragment = (NormalFragment) fragments.get(0);
////        if (null != mCurFragment && mCurFragment.isAdded()) {
//        fragment.setTempBluetoothWeight(entity);
//        }
    }

    /**
     * 把蓝牙得到的一条体重数据，用来匹配某个角色
     *
     * @param roleDataInfo 体重数据
     */
    private void onWeight(WeightEntity roleDataInfo) {
        RoleInfo roleInfo = Account.getInstance(NewMainActivity.this).getRoleInfo();
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
        WeighDataParser.create(NewMainActivity.this).fillFatWithSmoothImpedance(roleDataInfo, roleInfo);
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
        mFragments.get(pos).setWeightEntity(entity, lastEntity);
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
            LogUtil.e("AYD", "Match not to role");
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
                RoleInfo roleInfo = RoleDB.getInstance(NewMainActivity.this).findRoleById(Account.getInstance(NewMainActivity.this).getAccountInfo().getId(), id);
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
            if (this == null) return;
            if (!mV14MatchingUser) {
                mV14MatchingUser = true;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("AYD---->", "act" + entity + "---->" + matchedRoleList);
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
            if (this == null) return;
            if (!isLock) {
                mV14MatchingUser = false;
            }
            if (!Account.getInstance(NewMainActivity.this).isAccountLogined()) {
                data.setAxunge(0);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (this == null) return;
                    Log.e("AYD----->1", "act" + data);

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
            if (this == null) {
                return;
            }

            mBluetoothState = state;
            mBluetoothMsg = msg;

            if (state == CsBtUtil_v11.STATE_BLE_CLOSE || state == CsBtUtil_v11.STATE_BLE_OPEN || state == CsBtUtil_v11.STATE_CLOSE) {
                mV14MatchingUser = false;
            }

            runOnUiThread(new Runnable() {
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
//            if (null != mCurFragment && mCurFragment.isAdded()) {
//                mCurFragment.setMsgLayout(mBluetoothIcon);
//            }
        }
    };

}
