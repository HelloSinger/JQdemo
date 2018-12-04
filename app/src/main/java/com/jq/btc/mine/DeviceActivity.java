package com.jq.btc.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.jq.btc.adapter.DeviceAdapter;
import com.jq.btc.app.R;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.helper.CsBtEngine;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.model.DeviceEntity;
import com.jq.code.model.ScaleInfo;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.dialog.TipDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hfei on 2015/10/20.
 */
public class DeviceActivity extends CommonActivity implements DeviceAdapter.OnDeviceClickListener {

    private static final String TAG = DeviceActivity.class.getSimpleName();
    private ViewHolder mViewHolder;
    private TipDialog mTipDialog;
    private CsBtEngine mCsBtEngine;
    private Account mAccount;
    private DeviceAdapter mDeviceAdapter;

    private class ViewHolder {
        ListView deviceListView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        setContentSubView(R.layout.activity_device, R.string.settingDevice);
        initView();
    }

    private void initView() {
        mCsBtEngine = CsBtEngine.getInstance(this);
        mAccount = Account.getInstance(this);
        mDeviceAdapter = new DeviceAdapter(this);
        mDeviceAdapter.addOnDeviceClickListener(this);
        mViewHolder = new ViewHolder();
        mViewHolder.deviceListView = (ListView) findViewById(R.id.device_list);
        mViewHolder.deviceListView.setAdapter(mDeviceAdapter);

        DeviceEntity entity = new DeviceEntity(getString(R.string.intelligentWeightScale), R.mipmap.weight_flag_img, getString(R.string.settingBoundOKOK), ScaleParser.getInstance(this).isBluetoothBounded());
//        DeviceEntity entity1 = new DeviceEntity(getString(R.string.bloodGlucoseMeter), R.mipmap.blood_glucose_img, getString(R.string.comingSoon), false);
//        DeviceEntity entity2 = new DeviceEntity(getString(R.string.sphygmomanometer), R.mipmap.blood_press_img, getString(R.string.comingSoon), false);
        List<DeviceEntity> entities = new ArrayList<DeviceEntity>();
        entities.add(entity);
//        entities.add(entity1);
//        entities.add(entity2);
        mDeviceAdapter.setData(entities);

    }

    @Override
    public void onOpreate(View view, int position) {
        switch (position) {
            case 0:
                if (mDeviceAdapter.getItem(position).isBound()) {
                    new DeviceInfoDialog(this,1).showDialog();
                } else {
                    startActivity(new Intent(this, BoundDeviceActivity.class));
                }
                break;
        }

    }

    @Override
    public void onBound(View view, int position) {
        switch (position) {
            case 0:
                showBLETip();
                break;
        }
    }

    private void showBLETip() {
        if (mTipDialog == null) {
            mTipDialog = new TipDialog(this);
            mTipDialog.setText(R.string.settingDeviceUnboundTip);
            mTipDialog.setRightButtonText(R.string.unbound);
            mTipDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 重新设置tag
                    ScaleParser.getInstance(DeviceActivity.this).setBluetoothState(ScaleParser.STATE_BLUETOOTH_UNBOUND);
                    mCsBtEngine.stopSearch();
                    mCsBtEngine.stopAutoConnect();
                    mCsBtEngine.closeGATT(true);
                    ScaleParser.getInstance(DeviceActivity.this).setScale(new ScaleInfo());
                    mTipDialog.dismiss();
                    finish();
                }
            });
        }
        mTipDialog.showDialog();
    }
}
