package com.jq.btc;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.jq.btc.app.R;
import com.jq.btc.homePage.NewMainActivity;
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

import java.util.Map;


public class InitActivity extends SimpleActivity {
    private static final String TAG = InitActivity.class.getSimpleName();
    private static final int TIME_OUT = 2 * 1000;
    private ImageView boot;
    private boolean isKeyBack;
    private String[] dangerousPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 7;
    private Context _context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        _context = this;

        if (Build.VERSION.SDK_INT >= 23) {
            boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, dangerousPermission);
            if (hasSelfPermission) {
                boot = (ImageView) findViewById(R.id.boot_img);
                initBoot();
                init();
            } else {
                requestPermissions(dangerousPermission, ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
        } else {
            boot = (ImageView) findViewById(R.id.boot_img);
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
        if (android.os.Build.VERSION.SDK_INT  > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setBootImage();
        } else {
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .create();
            alertDialog.setTitle(this.getResources().getString(
                    R.string.versionErr));
            alertDialog.setMessage(this.getResources().getString(
                    R.string.versionHead)
                    + android.os.Build.VERSION.RELEASE
                    + this.getResources().getString(R.string.versionFoot));
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

        if (bitmap != null) {
            boot.setImageBitmap(bitmap);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isKeyBack) {
                        showViewpager(true);
                    }
                }
            }, TIME_OUT);
        } else {
            showViewpager(false);
        }
    }

    public void showViewpager(boolean isAnima) {
            toActivity(isAnima);
    }

    private void toActivity(boolean isAnima) {
        judgeUpgrades();
        AccountEntity af = new AccountEntity();
        af.setId(100);
        af.setAccess_token("sss");
        af.setWeixin("123123");
        Account.getInstance(this).setAccountInfo(af);
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setSex("男");
        roleInfo.setNickname("测试");
        roleInfo.setHeight(180);
        roleInfo.setAccount_id(195871);
        roleInfo.setBirthday("1990-09-09");
        roleInfo.setId(1993456);
        roleInfo.setCurrent_state(1);
        roleInfo.setCreate_time("2018-11-29 11:01:11");
        roleInfo.setWeight_init(70);
        roleInfo.setWeight_goal(70);
        roleInfo.setRole_type(0);
        Account.getInstance(this).setRoleInfo(roleInfo);
        // 初始化单位
        Intent intent = new Intent();
                intent.setClass(InitActivity.this, NewMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        this.finish();
        if (isAnima) {
            overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
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
                }else{
                    finish();
                }
            }
            break;
        }

    }

}
