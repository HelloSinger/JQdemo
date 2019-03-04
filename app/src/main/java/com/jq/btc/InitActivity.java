package com.jq.btc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.haiersmart.user.sdk.UserUtils;
import com.jq.btc.app.R;
import com.jq.btc.bluettooth.BLEController;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.homePage.NewMainActivity;
import com.jq.btc.model.UserData;
import com.jq.btc.utils.BMToastUtil;
import com.jq.btc.utils.NetWorkUtils;
import com.jq.btc.utils.ShareUtils;
import com.jq.btc.utils.SpUtils;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.code.util.CacheUtil;
import com.jq.code.code.util.FileUtil;
import com.jq.code.code.util.LogUtil;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.json.JsonProductInfo;
import com.jq.code.view.activity.SimpleActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import static com.jq.btc.utils.NetWorkUtils.isNetworkAvailable;


public class InitActivity extends SimpleActivity implements View.OnClickListener {
    private static final String TAG = InitActivity.class.getSimpleName();
    private static final int TIME_OUT = 2 * 1000;
    private ImageView boot;
    private boolean isKeyBack;
    private ImageView iv_back;
    private boolean isBound = false;
    private String[] dangerousPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 7;
    private Context _context;
    private BLEController mBleController;
    private String useIds;
    private LinearLayout ll_bind_device, ll_unserstand_scene;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        _context = this;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String province = bundle.getString("province");
            if (!province.equals(SpUtils.getInstance(_context).getProvince())) {
                SpUtils.getInstance(_context).putProvince(province);
                Log.e("AYD", "--->" + province);
            }
            String city = bundle.getString("city");
            if (!city.equals(SpUtils.getInstance(_context).getCity())) {
                SpUtils.getInstance(_context).putCity(city);
                Log.e("AYD", "--->" + city);
            }
            String cityId = bundle.getString("cityId");
            if (!cityId.equals(SpUtils.getInstance(_context).getCityId())) {
                SpUtils.getInstance(_context).putCityId(cityId);
                Log.e("AYD", "--->" + cityId);
            }
//            String deviceId = bundle.getString("deviceId");
            String deviceId = getLocalMacAddress(this).toUpperCase();
            if (!deviceId.equals(SpUtils.getInstance(_context).getMac())) {
                SpUtils.getInstance(_context).putMac(deviceId);
                Log.e("AYD", "--->" + deviceId);
            }

        }
        isBound = SpUtils.getInstance(_context).getIsFirst();
        if (isBound) {
            startActivity(new Intent(InitActivity.this, NewMainActivity.class));
            finish();
        }
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        Log.e("fenbianlv", "onCreate: " + width + "---" + height);

        mBleController = BLEController.create(this);
        mBleController.setBound(true);
        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, dangerousPermission);
            if (hasSelfPermission) {
                boot = findViewById(R.id.boot_img);
                initBoot();
                init();
            } else {
                requestPermissions(dangerousPermission, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
        } else {
            boot = findViewById(R.id.boot_img);
            iv_back = findViewById(R.id.iv_back);
            ll_unserstand_scene = findViewById(R.id.ll_unserstand_scene);
            ll_bind_device = findViewById(R.id.ll_bind_device);
            ll_bind_device.setOnClickListener(this);
            iv_back.setOnClickListener(this);
            ll_unserstand_scene.setOnClickListener(this);
            initBoot();
            init();
        }


    }

    /**
     * 开机启动必须初始化函数
     */
    public void initBoot() {
        /**
         * 1. 检测sdcard中是否有chipsea/download目录，没有创建，有忽略
         */
        if (!FileUtil.isFileExist(FileUtil.CHIPSEA_ROOT_DIR)) {
            LogUtil.i(TAG, "目录不存在,准备创建...");
            FileUtil.createSDDir(FileUtil.CHIPSEA_ROOT_DIR);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 初始化
     */
    private void init() {
        // 判断版本是否低于4.3，不低于则开启页面跳转任务，低于则弹出提示框
        if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setBootImage();
        } else {
            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getResources().getString(R.string.versionErr));
            alertDialog.setMessage(getResources().getString(
                    R.string.versionHead) + android.os.Build.VERSION.RELEASE
                    + getResources().getString(R.string.versionFoot));
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    alertDialog.dismiss();
                    InitActivity.this.finish();
                }
            }, TIME_OUT);
        }
    }

    /**
     * 设置启动画面
     */
    private void setBootImage() {
        Bitmap bitmap = null;
        if (ScaleParser.getInstance(this).isBluetoothBounded()) {
            Map<String, JsonProductInfo> productMap = ScaleParser.getInstance(this).getProductMap();
            String url = "";
            if (productMap != null) {
                JsonProductInfo productInfo =
                        productMap.get(ScaleParser.getInstance(this).getScale().getProduct_id() + "");
                if (productInfo != null) {
                    url = "cbootimg-" + productInfo.getCompany_id() + "/"
                            + ScreenUtils.getScreenMode(this);
                }
            }
            CacheUtil cacheUtil = CacheUtil.getInstance(this);
            // 缓存获取启动页
            bitmap = cacheUtil.getBitmapFromMemCache(url.replace("/", ""));
            if (bitmap == null) {
                bitmap = cacheUtil.getImageFromFile(url.replace("/", ""));
            }
        }
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.haier_welcome_img);
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isKeyBack = true;
            ActivityUtil.getInstance().AppExit(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void judgeUpgrades() {
        Account account = Account.getInstance(this);
        if (account.getCurrVersion() == null) {
            account.clear();
            try {
                account.setCurrVersion(PrefsUtil.getVersionName(this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                boolean hasSelfPermission = PermissionUtils.verifyPermissions(grantResults);
                if (hasSelfPermission) {
                    boot = (ImageView) findViewById(R.id.boot_img);
                    initBoot();
                    init();
                } else {
                    finish();
                }
            }
            break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_bind_device:
                if (mBleController != null) {
                    if (mBleController.isBluetoothEnable()) {
                        if (NetWorkUtils.isNetworkAvailable(_context)) {
                            startActivity(new Intent(this, BoundDeviceActivity.class).putExtra("where", 2));
                            finish();
                        } else {
                            BMToastUtil.showToastShort(_context, "当前无网络");
                        }
                    } else {
                        BMToastUtil.showToastShort(_context, "请先开启蓝牙");
                    }
                }
                break;
            case R.id.ll_unserstand_scene:
                startActivity(new Intent(this, BodyFatScaleInduceActivity.class));
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        String mac;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mac = getMachineHardwareAddress();
        } else {
            WifiManager wifi = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            mac = info.getMacAddress().replace(":", "");
        }

        return mac;
    }

    /**
     * 获取设备的mac地址和IP地址（android6.0以上专用）
     *
     * @return
     */
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress == null) continue;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        if (iF != null && iF.getName().equals("wlan0")) {
            hardWareAddress = hardWareAddress.replace(":", "");
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }
}
