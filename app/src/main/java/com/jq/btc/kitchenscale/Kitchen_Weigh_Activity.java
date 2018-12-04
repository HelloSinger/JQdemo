package com.jq.btc.kitchenscale;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jq.btc.CSApplication;
import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.kitchenscale.ble.BLEDevice;
import com.jq.btc.kitchenscale.ble.BleConnectionCallback;
import com.jq.btc.kitchenscale.ble.BleHelper;
import com.jq.btc.kitchenscale.ble.BleReadOrWriteCallback;
import com.jq.btc.kitchenscale.ble.BleScanResultCallback;
import com.jq.btc.kitchenscale.entity.BleState;
import com.jq.btc.kitchenscale.view.MyGridViewFood;
import com.jq.code.code.business.SoundPlayer;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.wyh.slideAdapter.SlideAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CM on 2018/9/18.
 */

public class Kitchen_Weigh_Activity extends Activity {

    @BindView(R2.id.kitchen_states)//蓝牙状态提示
            TextView kitchen_states;

    @BindView(R2.id.text_state1)
    TextView text_state1;


    @BindView(R2.id.text_calory)//总热量
            TextView text_calory;

    @BindView(R2.id.kitchen_foodname_click)
    TextView kitchen_foodname_click;

    @BindView(R2.id.kitchen_gridview)
    MyGridViewFood kitchen_gridview;

    @BindView(R2.id.kitchen_checknums)
    TextView kitchen_checknums;

    @BindView(R2.id.kitchen_checklick)
    RelativeLayout kitchen_checklick1;

    @BindView(R2.id.kitchen_units)
    TextView kitchen_units;

    private String calorys;

    private SlideAdapter adapter;
    private List<String> list_grid = new ArrayList<>();
    private PopupWindow window;
    private TextView kitchen_numclicks;
    private int types_state;//是否选择食物标识
    private int types_state1;//是否已经添加过的标识
    private int  type_kitchen, type_addnums, weightnums,type_refresh,type_fu;
    private String foodids;
    private Bundle bundle;
    private Double weight_kit,weight_g;
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

    private ScaleInfo mScaleInfo;
    private String unit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        kitchen_numclicks = findViewById(R.id.kitchen_numclicks);
        openBluetoothScanDevice();

        EventBus.getDefault().register(this);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void takes(String se) {
        kitchen_news(se);
    }

    //新秤协议   试用
    private void kitchen_news(String se) {
        unit = "g";
        String[] split = se.split("x");
        String substringw = split[0].substring(0, 1);
        String substring_state = split[0].substring(7, 8);
        String substring = split[0].substring(1, 5);
        String substring_unit = split[0].substring(5, 7);
        if (substringw.equals("1")) {
            type_fu = 1;
            return;
        } else {
            type_fu = 0;
        }
        if(substring.equals("0000")){
            unit = "g";
        }else if(substring.equals("0001")){//ml水
            unit = "ml";
        }else if(substring.equals("0010")){ // ml milk
            unit = "ml(milk)";
        }else if(substring.equals("0011")){//oz
            unit = "oz";
        }else if(substring.equals("0110")){//lb
            unit = "lb";
        }else if(substring.equals("0111")){//lb:oz
            unit = "lb:oz";
            int w = Integer.valueOf(split[1].substring(0,2),16);
            int s = Integer.valueOf(split[1].substring(2,4),16);
            kitchen_numclicks.setText(w+":"+s*0.1);
            kitchen_units.setText(unit);
        }else if(substring.equals("1000")){//kg
            unit =  "kg";
        }

        if(!substring.equals("0111")) {
            int ws = Integer.valueOf(split[1], 16);
            if (substring_unit.equals("00")) {
                kitchen_numclicks.setText(""+ws);
            } else if (substring_unit.equals("01")) {
                kitchen_numclicks.setText(String.format("%.1f",ws*0.1));
            } else if (substring_unit.equals("10")) {
                kitchen_numclicks.setText(String.format("%.2f",ws*0.01));
            } else if (substring_unit.equals("11")) {
                kitchen_numclicks.setText(String.format("%.3f",ws*0.001));
            }
            kitchen_units.setText(unit);
        }

        String substring_unit1 = split[3].substring(5, 7);
        int weight_gs = Integer.valueOf(split[2], 16);
        if (substring_unit1.equals("00")) {
            weight_g = (double)weight_gs;
        } else if (substring_unit1.equals("01")) {
            weight_g = weight_gs*0.1;
        } else if (substring_unit1.equals("10")) {
            weight_g = weight_gs*0.01;
        } else if (substring_unit1.equals("11")) {
            weight_g = weight_gs*0.001;
        }

        if (substring_state.equals("1") && types_state == 1) {
//
            type_refresh = 1;
            type_kitchen = 1;
            kitchen_gridview.setVisibility(View.VISIBLE);
//
            double v = Double.valueOf(calorys) * ( weight_g / 100);
            text_calory.setText(String.format("%.1f",(v))+"千卡");
            text_calory.setVisibility(View.VISIBLE);
            text_state1.setVisibility(View.VISIBLE);

            if(type_refresh == 1){

            }
        } else {
            type_kitchen = 0;
            type_refresh = 0;
            kitchen_gridview.setVisibility(View.GONE);
            text_state1.setVisibility(View.GONE);

            text_calory.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void takes1(BleState se) {
        if(type_fu == 0) {
            if (se.getState() == 11) {
                kitchen_states.setText("设备已连接");
            } else if (se.getState() == 12) {
                kitchen_states.setText("等待设备连接...");
            } else if (se.getState() == 15) {
                kitchen_states.setText("正在连接...");
            } else if (se.getState() == 16) {
                kitchen_states.setText("连接失败，请重试");
            }
        }else {
            kitchen_states.setText("负数,请归零营养称");
            kitchen_numclicks.setText("--");
        }
    }


    @Override
    protected void onDestroy() {
        if (currDevice != null && currDevice.isConntect()) {
            currDevice.setConntect(false);
            mBle.disconnect(currDevice.getBluetoothDevice().getAddress());
        }
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        openBluetoothScanDevice();
//    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R2.id.back_kitchen})
    public void click(View view) {
        switch (view.getId()) {
            case R2.id.back_kitchen:
                finish();
                break;
        }
    }



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
            edit.putString("kitchen_name", currDevice.getBluetoothDevice().getName());
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
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                currDevice.setConntect(true);
                mBle.discoverServices(currDevice.getBluetoothDevice().getAddress(), bleReadOrWriteCallback);
            } else {
                currDevice.setConntect(false);
                mBle.startScan(resultCallback);
//                mBle.requestConnect(currDevice.getBluetoothDevice().getAddress(),connectionCallback,true);
            }
        }

        @Override
        public void onFail(int errorCode) {

        }
    };
    private BleScanResultCallback resultCallback = new BleScanResultCallback() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onFindDevice(BluetoothDevice device, int rssi, byte[] scanRecord) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < scanRecord.length; i++) {
                sb.append(String.format("%02x", scanRecord[i]));
            }
//           if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//                //检查下当前是否在进行扫描 如果是则先停止
//                if (mBle != null && mScanning) {
//                    mBle.stopScan();
//                    mScanning = false;
//                }
//            }
            String substring = sb.substring(10, 14);
            String substring1 = sb.substring(18, 20);

            if(substring.equals("cb18") && substring1.equals("02")){
                currDevice = new BLEDevice();
                currDevice.setBluetoothDevice(device);
                mBle.stopScan();
                mScanning = false;
                mBle.requestConnect(currDevice.getBluetoothDevice().getAddress(), connectionCallback, true);
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
//                    Toast.makeText(Kitchen_Weigh_Activity.this, "开启蓝牙扫描失败，请检查蓝牙是否正常工作！", Toast.LENGTH_LONG).show();
//                    Toast.makeText(Kitchen_Weigh_Activity.this, "如果长时间无法连接绑定，请重启app再尝试", Toast.LENGTH_LONG).show();
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
//            //蓝牙没打开则去打开蓝牙
//            boolean openresult = toEnable(BluetoothAdapter.getDefaultAdapter());
//            if (!openresult) {
//                Toast.makeText(Kitchen_Weigh_Activity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
//                return;
//            }
//            //停个半秒再检查一次
            kitchen_states.setText("您的手机蓝牙未打开,请开启蓝牙");
            new Thread(
                    new Runnable() {
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

                                } else {
                                    i++;
                                }

                            }
                            //发现蓝牙打开了，则进行开启扫描的步骤
                            scanDevice();
                        }
                    }
            ).start();
//            Kitchen_Weigh_Activity.this.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            });
        } else {
            //检查下当前是否在进行扫描 如果是则先停止
            if (mBle != null && mScanning) {
                mBle.stopScan();
                mScanning = false;
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
        int checkCallPhonePermission = ContextCompat.checkSelfPermission(Kitchen_Weigh_Activity.this, Manifest.permission.BLUETOOTH);
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Kitchen_Weigh_Activity.this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_FINE_LOCATION);
            Toast.makeText(Kitchen_Weigh_Activity.this, "打开蓝牙失败，请检查是否禁用了蓝牙权限", Toast.LENGTH_LONG).show();
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
            e.printStackTrace();

        }
        return result;

    }
}