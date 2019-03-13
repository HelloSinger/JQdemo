package com.jq.btc.bluettooth;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.btc.CSApplication;
import com.jq.btc.adapter.ViewPagerAdapter;
import com.jq.btc.app.R;
import com.jq.btc.homePage.NewMainActivity;
import com.jq.btc.kitchenscale.Kitchen_Weigh_Activity;
import com.jq.btc.kitchenscale.ble.BLEDevice;
import com.jq.btc.kitchenscale.ble.BleConnectionCallback;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btc.kitchenscale.ble.BleReadOrWriteCallback;
import com.jq.btc.kitchenscale.ble.BleScanResultCallback;
import com.jq.btc.utils.SpUtils;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.text.CustomTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.jq.btc.app.R2.id.iv_bound;

public class BoundDeviceActivity extends Activity implements OnClickListener, OnPageChangeListener, BLEController.OnBlEChangeListener {

    private static final String TAG = "===";
    private List<View> mPagerList = null;
    private ViewHolder mViewHolder;
    private boolean isStartSearch = true;
    private Account mAccount;
    private BLEController mBleController;
    private ScaleInfo mScaleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mAccount = Account.getInstance(this);

//        if (getIntent().getIntExtra("where", 0) == 2) {
//            openBluetoothScanDevice();
//        }
//         蓝牙初始化
        mBleController = BLEController.create(this);
        mBleController.setBound(true);
        mBleController.setOnBLEChangeListener(this);
        initView();
        EventBus.getDefault().register(this);

        reSearch();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void takes(String se) {
        if (getIntent().getIntExtra("where", 0) == 2) {
            showBoundView();
            stopAnimation();
            String[] split = se.split("x");
            if (split[1].isEmpty()) {
                mViewHolder.value.setText("");
                mViewHolder.unit.setText("g");
                mViewHolder.unit.setVisibility(View.GONE);
//                mViewHolder.value.setBackgroundResource(R.mipmap.scale);
                mViewHolder.tip.setText("");
            } else {
                mViewHolder.unit.setVisibility(View.VISIBLE);
                mViewHolder.unit.setText("g");
                mViewHolder.value.setBackgroundColor(Color.TRANSPARENT);
                mViewHolder.value.setText(split[1]);
                mViewHolder.tip.setText("已经发现智能营养秤，请确认秤显示的数值和搜索数值一样" + "\n" + "即可确认绑定");
            }
        }
    }

    private void initView() {
        mViewHolder = new ViewHolder();
        mViewHolder.viewpager = (ViewPager) findViewById(R.id.guide_viewpager);
        mPagerList = new ArrayList<>();
        //addOpenBLE();
        addBoundBLE();
        mViewHolder.viewpager.setAdapter(new ViewPagerAdapter(mPagerList));
        mViewHolder.viewpager.setOnPageChangeListener(this);
        mViewHolder.viewpager.setCurrentItem(mPagerList.size() - 1);
    }

    /**
     * 添加蓝牙教程View
     */
    private void addOpenBLE() {
        LinearLayout openBle = new LinearLayout(this);
        openBle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//        openBle.setBackgroundResource(imageID[0]);
        mPagerList.add(openBle);
    }

    /**
     * 添加蓝牙搜索绑定view
     */
    private void addBoundBLE() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_bound_device, null);
//        mViewHolder.buyBt = (CustomTextView) view.findViewById(R.id.bound_buy);
        mViewHolder.mWholeLayout = view.findViewById(R.id.mWholeLayout);
        mViewHolder.mBackView = view.findViewById(R.id.backImager);
        mViewHolder.mBlueToothDisable = view.findViewById(R.id.mBlueToothDisable);
//        mViewHolder.mValueLayout = view.findViewById(R.id.mValueLayout);
        mViewHolder.boundedLL = view.findViewById(R.id.bounded_ll);
        mViewHolder.title = view.findViewById(R.id.bound_title);
        mViewHolder.tv_title_two = view.findViewById(R.id.tv_title_two);
        mViewHolder.research = view.findViewById(R.id.bound_device_research);
        mViewHolder.sure = view.findViewById(R.id.bound_device_sure);
        mViewHolder.tip = view.findViewById(R.id.bound_tip);
        mViewHolder.value = view.findViewById(R.id.bound_value);
        mViewHolder.unit = view.findViewById(R.id.bound_unit);
        mViewHolder.animtor = view.findViewById(R.id.bound_animation);
        mViewHolder.un_bound_ll = view.findViewById(R.id.un_bound_ll);
        mViewHolder.tv_bound_B = view.findViewById(R.id.tv_bound_B);
        mViewHolder.iv_bound = view.findViewById(R.id.iv_bound);
//        mViewHolder.animtor.setImageResource(R.drawable.ble_bound_animation);
        mViewHolder.animtor.setImageResource(R.mipmap.icon_body_fat_scale);
        mViewHolder.mBackView.setOnClickListener(this);
        mViewHolder.research.setOnClickListener(this);
        mViewHolder.sure.setOnClickListener(this);
        mViewHolder.un_bound_ll.setOnClickListener(this);
//        mViewHolder.buyBt.setOnClickListener(this);
        mPagerList.add(view);

        mViewHolder.mWholeLayout.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);

        if (mBleController != null) {
            if (mBleController.isBluetoothEnable()) {
                mViewHolder.mBlueToothDisable.setVisibility(View.GONE);
            } else {
                mViewHolder.mBlueToothDisable.setVisibility(View.VISIBLE);
            }
        }

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(R.color.mainColor));
        float roundRadius = getResources().getDisplayMetrics().density * 3;
        gd.setCornerRadius(roundRadius);
//        mViewHolder.research.setBackground(gd);
//        mViewHolder.sure.setBackground(gd);

        GradientDrawable gdValue = new GradientDrawable();
        gdValue.setColor(0x0d32beff);
        gdValue.setCornerRadius(getResources().getDisplayMetrics().density * 90);
//        mViewHolder.mValueLayout.setBackground(gdValue);
    }

    private void setAnimator() {
        final ObjectAnimator animator = ObjectAnimator.ofFloat(mViewHolder.iv_bound, "rotation", 0f, 360f);
        LinearInterpolator lin = new LinearInterpolator();
        animator.setInterpolator(lin);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setAnimator();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(5000);
        animator.start();
    }

    @Override
    public void onMachRole(WeightEntity entity, ArrayList<Integer> matchedRoleList) {

    }

    @Override
    public void onDataChange(boolean isLook, WeightEntity data) {

    }

    @Override
    public void onStateChange(int state, String msg, int currentImge) {
        if (state == CsBtUtil_v11.STATE_BLE_CLOSE) {
            isStartSearch = false;
        } else if (state == CsBtUtil_v11.STATE_BLE_OPEN) {
            if (mBleController != null) {
                mBleController.reBound();
            }
            isStartSearch = true;
            Log.e("AYD", "-------1");
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBleController != null) {
                    if (mBleController.isBluetoothEnable()) {
                        mViewHolder.mBlueToothDisable.setVisibility(View.GONE);
                    } else {
                        mViewHolder.mBlueToothDisable.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void onBound(final ScaleInfo scaleInfo, final String value, final int tip) {
        mScaleInfo = scaleInfo;
        if (mScaleInfo != null) {
            SharedPreferences sh1 = getSharedPreferences("kitchens1", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sh1.edit();
            if (mScaleInfo.getName() == null || mScaleInfo.getName().equals("null")) {
                edit.putString("kitchen_name1", "UNKNOWN");
            } else {
                edit.putString("kitchen_name1", mScaleInfo.getName());
            }
            edit.commit();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (BoundDeviceActivity.this.isFinishing()) {
                        return;
                    }
                    if (isStartSearch) {
                        showBoundView();
                        stopAnimation();
                        isStartSearch = false;
                    }
                    if (value.isEmpty()) {
                        mViewHolder.value.setText("0.00");
                        mViewHolder.unit.setText("");
                        mViewHolder.unit.setVisibility(View.GONE);
//                        mViewHolder.value.setBackgroundResource(R.mipmap.scale);
                        if (scaleInfo.getKitchens().equals("1")) {
                            mViewHolder.tip.setText("已经搜索到智能营养秤，请确认绑定");
                        } else {
                            mViewHolder.tip.setText("已经搜索到智能体脂秤，请确认绑定");
                        }

                    } else {
                        mViewHolder.unit.setVisibility(View.VISIBLE);
                        if (scaleInfo.getKitchens().equals("1")) {
                            mViewHolder.unit.setText("g");
                            mViewHolder.tip.setText("已经发现智能营养秤，请确认秤显示的数值和搜索数值一样" + "\n" + "即可确认绑定");
                        } else {
                            mViewHolder.unit.setText(StandardUtil.getWeightExchangeUnit(BoundDeviceActivity.this));
                            mViewHolder.tv_bound_B.setText("绑定设备");
                            mViewHolder.tip.setText("已经发现智能体脂秤，请确认秤显示的数值和搜索数值一样" + "\n" + "即可确认绑定");
                        }
                        mViewHolder.value.setBackgroundColor(Color.TRANSPARENT);
                        mViewHolder.value.setText(value);
                    }
                }
            });
        }
    }

    @Override
    public void syncHistoryEnd(List<PutBase> putBases) {

    }

    class ViewHolder {
        ViewPager viewpager;
        View mWholeLayout;
        View mBackView;
        View mBlueToothDisable;
        ImageView animtor;
        LinearLayout boundedLL;
        TextView title, tv_title_two;

        TextView research;
        TextView sure;
        TextView tip;
        View mValueLayout;
        TextView value;
        TextView unit;
        LinearLayout un_bound_ll;
        TextView tv_bound_B;
        ImageView iv_bound;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bitmap drawingCache = mViewHolder.animtor.getDrawingCache();
        if (drawingCache != null) {
            drawingCache.recycle();
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mPagerList.size(); i++) {
                    mPagerList.get(i).setDrawingCacheEnabled(true);
                    Bitmap bitmap = mPagerList.get(i).getDrawingCache();
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
            }
        });
        EventBus.getDefault().unregister(this);
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBleController != null) {
            mBleController.registerReceiver(this);
            if (null == mScaleInfo) {
                mBleController.startSearch();
                startAnimation();
            }
        }
    }

    private AnimationDrawable animationDrawable;

    private void startAnimation() {
        mViewHolder.title.setVisibility(View.VISIBLE);
        mViewHolder.tv_title_two.setVisibility(View.VISIBLE);
        mViewHolder.tv_bound_B.setText("蓝牙连接");
        mViewHolder.title.setText(R.string.tutorialboundTitle);
//        mViewHolder.tv_title_two.setText(R.string.tv_title_two);
        mViewHolder.animtor.setVisibility(View.VISIBLE);
        mViewHolder.boundedLL.setVisibility(View.GONE);
//        mViewHolder.buyBt.setVisibility(View.VISIBLE);

//        animationDrawable = (AnimationDrawable) mViewHolder.animtor.getDrawable();
//        animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBleController != null) {
            mBleController.unregisterReceiver(this);
            isStartSearch = true;
            Log.e("AYD", "-------2");
            mBleController.stopSeach();
            stopAnimation();
        }
    }

    private void stopAnimation() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable = null;
        }
    }

    private void showBoundView() {
        mViewHolder.title.setVisibility(View.GONE);
        mViewHolder.tv_title_two.setVisibility(View.GONE);
//        mViewHolder.title.setText(R.string.HaierBluetooth_bluetooth_confirming);
//        mViewHolder.buyBt.setVisibility(View.GONE);
        mViewHolder.boundedLL.setVisibility(View.VISIBLE);
        setAnimator();
        mViewHolder.animtor.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {

        if (v == mViewHolder.un_bound_ll) {
            SpUtils.getInstance(BoundDeviceActivity.this).cleanMak();
            finish();
        } else if (v == mViewHolder.research) {
            reSearch();
        } else if (v == mViewHolder.sure) {

            if (mScaleInfo.getKitchens().equals("1")) {
                SharedPreferences sh = getSharedPreferences("kitchens", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sh.edit();
                edit.putString("kitchen_name", mScaleInfo.getName());
                edit.putString("kitchen_mac", mScaleInfo.getMac());
                edit.commit();
                startActivity(new Intent(BoundDeviceActivity.this,
                        Kitchen_Weigh_Activity.class).putExtra("where", 1));
                this.finish();
            } else {
                mScaleInfo.getName();
                bound();
            }
        } else if (mViewHolder.mBackView == v) {
            finish();
        }

    }

    private void bound() {
        // 绑定当前设备
        mScaleInfo.setAccount_id(mAccount.getAccountInfo().getId());
        mScaleInfo.setLast_time(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        ScaleParser.getInstance(BoundDeviceActivity.this).setScale(mScaleInfo);
        // 设置蓝牙状态
        ScaleParser.getInstance(BoundDeviceActivity.this).setBluetoothState(ScaleParser.STATE_BLUETOOTH_BOUND);
        //跳转页面
        toActivity();
    }

    private void reSearch() {
        //重新搜索
        startAnimation();
        if (mBleController != null) {
            mBleController.reBound();
            isStartSearch = true;
            Log.e("AYD", "-------3");
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == (mPagerList.size() - 1)) {
            if (mBleController != null) {
                mBleController.reBound();
                isStartSearch = true;
            }
        }
    }

    private void toActivity() {
        SpUtils.getInstance(this).setIsFirst(true);
        Intent intent = new Intent();
        intent.setClass(this, NewMainActivity.class);
        intent.putExtra("mak", 11);
        startActivity(intent);
        ActivityUtil.getInstance().finishAllActivity();
        this.finish();
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }
//    _______________________________________________________

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 0;
    private BleHelper mBle;
    private boolean mScanning;
    Handler myhandler = new Handler();

    List<BluetoothGattService> servicesList = new ArrayList<>();

    private BleReadOrWriteCallback bleReadOrWriteCallback = new BleReadOrWriteCallback() {
        @Override
        public void onReadSuccess() {

        }

        @Override
        public void onReadFail(int errorCode) {

        }

        @Override
        public void onWriteSuccess() {

        }

        @Override
        public void onWriteFail(int errorCode) {

        }

        @Override
        public void onServicesDiscovered(int state) {
            SharedPreferences sh = getSharedPreferences("kitchens", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sh.edit();
            if (currDevice.getBluetoothDevice().getName().equals("null") || currDevice.getBluetoothDevice().getName() == null) {
                edit.putString("kitchen_name", "UNKNOWN");
            } else {
                edit.putString("kitchen_name", currDevice.getBluetoothDevice().getName());
            }
            edit.putString("kitchen_mac", currDevice.getBluetoothDevice().getAddress());
            edit.commit();
            servicesList = CSApplication.getInstance().getmBle().getServicesList();
            for (int i = 0; i < servicesList.size(); i++) {
                CSApplication.getInstance().getmBle().characteristicNotification(servicesList.get(i).getUuid().toString());
            }
        }

        @Override
        public void onDiscoverServicesFail(int errorCode) {

        }
    };
    BLEDevice currDevice;

    private BleConnectionCallback connectionCallback = new BleConnectionCallback() {
        @Override
        public void onConnectionStateChange(int status, int newState) {
            Log.e(TAG, "连接状态发生变化：" + status + "     " + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                currDevice.setConntect(true);
                mBle.discoverServices(currDevice.getBluetoothDevice().getAddress(), bleReadOrWriteCallback);
            } else {
                currDevice.setConntect(false);
            }
        }

        @Override
        public void onFail(int errorCode) {
            Log.e(TAG, "连接失败：" + errorCode);

        }
    };
    private BleScanResultCallback resultCallback = new BleScanResultCallback() {
        @Override
        public void onSuccess() {

            Log.d(TAG, "开启扫描成功");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "开启扫描失败");
        }

        @Override
        public void onFindDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < scanRecord.length; i++) {
                sb.append(String.format("%02x", scanRecord[i]));
            }
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                //检查下当前是否在进行扫描 如果是则先停止
                if (mBle != null && mScanning) {
                    mBle.stopScan();
                }
            }
            String substring = sb.substring(10, 14);
            String substring1 = sb.substring(18, 20);
            if (substring.equals("cb18") && substring1.equals("02")) {
                currDevice = new BLEDevice();
                currDevice.setBluetoothDevice(device);
//                mBle.requestConnect(currDevice.getBluetoothDevice().getAddress(), connectionCallback, true);

            }
        }
    };

    private void scanLeDevice(final boolean enable) {
        //获取ble操作类
        mBle = CSApplication.getInstance().getmBle();
        if (mBle == null) {
            return;
        }
        if (enable) {
            //开始扫描
            if (mBle != null) {
                boolean startscan = mBle.startScan(resultCallback);
                if (!startscan) {
                    Toast.makeText(BoundDeviceActivity.this, "开启蓝牙扫描失败，请检查蓝牙是否正常工作！", Toast.LENGTH_LONG).show();
                    Toast.makeText(BoundDeviceActivity.this, "如果长时间无法连接绑定，请重启app再尝试", Toast.LENGTH_LONG).show();
                    return;
                }
                mScanning = true;
                //扫描一分钟后停止扫描
//                myhandler.postDelayed(stopRunnable, 3000);
            }
        } else {
            //停止扫描
            mScanning = false;
            if (mBle != null) {
                mBle.stopScan();
                myhandler.removeCallbacksAndMessages(null);
            }
        }

    }

    private Runnable stopRunnable = new Runnable() {
        @Override
        public void run() {
            mBle.stopScan();
            mScanning = false;

        }
    };


    /**
     * 检测蓝牙是否打开
     */
    void openBluetoothScanDevice() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            //蓝牙没打开则去打开蓝牙
            boolean openresult = toEnable(BluetoothAdapter.getDefaultAdapter());
            if (!openresult) {
                Toast.makeText(BoundDeviceActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
                return;
            }
            //停个半秒再检查一次
            SystemClock.sleep(500);
            BoundDeviceActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    while (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (i >= 15) {
                            Toast.makeText(BoundDeviceActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
                            break;
                        } else {
                            i++;
                        }

                    }
                    //发现蓝牙打开了，则进行开启扫描的步骤
                    scanDevice();
                }
            });
        } else {
            //检查下当前是否在进行扫描 如果是则先停止
            if (mBle != null && mScanning) {
                mBle.stopScan();
            }
            scanDevice();
        }
    }

    @UiThread
    void scanDevice() {
        //如果此时发蓝牙工作还是不正常 则打开手机的蓝牙面板让用户开启
        if (mBle != null && !mBle.adapterEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        myhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //检查一下去那些，如果没有则动态请求一下权限
                requestPermission();
                //开启扫描
                scanLeDevice(true);
            }
        }, 500);
    }

    synchronized private void requestPermission() {
        //TODO 向用户请求权限
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(BoundDeviceActivity.this, Manifest.permission.BLUETOOTH);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BoundDeviceActivity.this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_FINE_LOCATION);
            Toast.makeText(BoundDeviceActivity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
            return;
        } else {

        }
    }


    private boolean toEnable(BluetoothAdapter bluertoothadapter) {
        //TODO 启动蓝牙
        boolean result = false;
        try {
            for (Method temp : Class.forName(bluertoothadapter.getClass().getName()).getMethods()) {
                if (temp.getName().equals("enableNoAutoConnect")) {
                    result = (boolean) temp.invoke(bluertoothadapter);
                }
            }
        } catch (Exception e) {
            //反射调用失败就启动通过enable()启动;
            result = bluertoothadapter.enable();
            Log.d(TAG, "启动蓝牙的结果:" + result);
            e.printStackTrace();

        }
        return result;

    }


}