package com.jq.btc.homePage;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jq.btc.adapter.ViewPagerMainAdapter;
import com.jq.btc.app.R;
import com.jq.btc.homePage.home.haier.HomeFragment;
import com.jq.btc.homePage.home.haier.NormalFragment;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.view.CustomToast;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xulj on 2016/5/9.
 */
public class NewMainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private RadioGroup mTabRg;
    private int showTabIndex = -1;
    private List<Fragment> mFragments;
    private Fragment curFragment;
    private int mPreRadioId = R.id.dynamic_rb;
    private HomeFragment homeFragment;
    private NormalFragment normalFragment;
    private RadioButton trend_rb, dynamic_rb, trend_shop, find_rb, me_rb;
    private ImageView iv_back;

    private ViewPagerMainAdapter viewPagerMainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        setContentView(R.layout.main);
        initView();
    }

    public void clickable() {
        me_rb.setClickable(true);
        find_rb.setClickable(true);
        trend_rb.setClickable(true);
        trend_shop.setClickable(true);
        dynamic_rb.setClickable(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (showTabIndex == 0) {
            homeFragment.setOnResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        trend_rb = findViewById(R.id.trend_rb);
        dynamic_rb = findViewById(R.id.dynamic_rb);
        trend_shop = findViewById(R.id.trend_shop);
        find_rb = findViewById(R.id.find_rb);
        me_rb = findViewById(R.id.me_rb);
        mTabRg = findViewById(R.id.main_tab);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        mTabRg.setOnCheckedChangeListener(this);
        mFragments = new ArrayList<>();
        homeFragment = new HomeFragment(trend_rb, this);
        mFragments.add(homeFragment);
        setFragment(0);
    }

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return resources;
    }

    public void setFragment(int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (curFragment != null) {
            transaction.hide(curFragment);
        }
        if (mFragments.get(index).isAdded()) {
            transaction.show(mFragments.get(index));
        } else {
            transaction.add(R.id.realtabcontent, mFragments.get(index));
            transaction.show(mFragments.get(index));
        }
        curFragment = mFragments.get(index);
        transaction.commitAllowingStateLoss();

        if (index == 0) {
            homeFragment.setWaveViewVisible(true);
        } else {
            homeFragment.setWaveViewVisible(false);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.dynamic_rb) {
            showTabIndex = 0;
        }

//        if (checkedId == R.id.dynamic_rb) {
//            showTabIndex = 0;
//        }
//        else if (checkedId == R.id.trend_rb) {
//            showTabIndex = 1;
//        } else if (checkedId == R.id.find_rb) {
//            showTabIndex = 2;
//        } else if (checkedId == R.id.me_rb) {
//            showTabIndex = 3;
//        }

//        if (!Account.getInstance(this).isAccountLogined() && (showTabIndex == 1 || showTabIndex == 2)) {
//            RegisterAndLoginTipDialog.getDialog(this).showDialog();
//            RadioButton radio = (RadioButton) findViewById(mPreRadioId);
//            if (radio != null) {
//                radio.setChecked(true);
//            }
//            return;
//        }
        mPreRadioId = checkedId;
        setFragment(showTabIndex);
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
        }
    }
}
