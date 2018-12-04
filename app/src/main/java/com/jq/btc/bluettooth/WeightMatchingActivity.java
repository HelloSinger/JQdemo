package com.jq.btc.bluettooth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jq.btc.adapter.MatchingGridAdapter;
import com.jq.btc.adapter.MatchingRecyclerAdapter;
import com.jq.btc.app.R;
import com.jq.btc.helper.HomeDataSet;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btc.homePage.LocalBroadcastUtil;
import com.jq.btlib.util.ThreadUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.db.WeightTmpDB;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightTmpEntity;
import com.jq.code.view.CustomToast;
import com.jq.code.view.activity.CommonActivity;
import com.jq.code.view.complexlistview.RecyclerItemDecoration;
import com.jq.code.view.text.CustomTextView;

import java.util.ArrayList;

public class WeightMatchingActivity extends CommonActivity {
    public static final String TAG = WeightMatchingActivity.class.getSimpleName();
    public static final int RESULT_FALG = 1001;
    private RecyclerView recyclerView;
    private MatchingRecyclerAdapter recyclerAdapter;
    private CheckBox checkBox;
    private CustomTextView unit;
    private LinearLayout gridView;
    private MatchingGridAdapter gridAdapter;
    private ArrayList<RoleInfo> roleInfos;
    private ArrayList<PutBase> entities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        setContentSubView(R.layout.activity_weight_matching, getResources().getColor(R.color.mainColor), getString(R.string.matchingWeightData));
        setRightText(R.string.delete);
        checkBox = (CheckBox) findViewById(R.id.weight_match_check);
        unit = (CustomTextView) findViewById(R.id.weight_match_unit);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerAdapter = new MatchingRecyclerAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.divider_layout));
        recyclerView.setAdapter(recyclerAdapter);

        gridView = (LinearLayout) findViewById(R.id.gridView);
        roleInfos = Account.getInstance(this).findRoleALL();
        gridAdapter = new MatchingGridAdapter(this, roleInfos);
        unit.setText("(" + StandardUtil.getWeightExchangeUnit(this) + ")");
        initGridview();
        initAdapter();
        checkChangeListener();
    }

    private void checkChangeListener() {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recyclerAdapter.checkAll(isChecked);
            }
        });
    }

    @Override
    protected void onRightClick() {
        if (recyclerAdapter.getSelected().isEmpty()) {
            CustomToast.showToast(WeightMatchingActivity.this, R.string.match_data_delete_tip, Toast.LENGTH_SHORT);
            return;
        }
        deleteList(null);
    }

    private ArrayList<WeightTmpEntity> tmpEntities;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            recyclerAdapter.setData(tmpEntities);
            return true;
        }
    });

    private void initAdapter() {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                tmpEntities = WeightTmpDB.getInstance(WeightMatchingActivity.this).findALL(Account.getInstance(WeightMatchingActivity.this).getAccountInfo().getId());
                handler.sendEmptyMessage(0);
            }
        });
    }

    private void initGridview() {
        gridView.removeAllViews();
        int count = gridAdapter.getCount();
        if (count <= 0) return;
        for (int i = 0; i < count; i++) {
            View view = gridAdapter.getView(i, null, gridView);
            view.setTag(i);
            gridView.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.valueOf(v.getTag().toString());
                    ArrayList<PutBase> weightEntities = new ArrayList<>();
                    ArrayList<WeightTmpEntity> selected = recyclerAdapter.getSelected();
                    if (selected.isEmpty()) {
                        CustomToast.showToast(WeightMatchingActivity.this, R.string.match_data_get_tip,Toast.LENGTH_SHORT);
                        return;
                    }
                    for (WeightTmpEntity tmpEntity : selected) {
                        weightEntities.add(tmpEntity.getWeightEntity());
                    }
                     entities = weightEntities;
                    if (position == gridAdapter.getCount() - 1) {
                        startAddRoleActivity();
                    } else {
                        fillData(gridAdapter.getItem(position));
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == RESULT_FALG) {
            if (data != null) {
                fillData((RoleInfo) data.getParcelableExtra(RoleInfo.ROLE_KEY));
                gridAdapter.update(Account.getInstance(this).findRoleALL());
                initGridview();
            }
        }
    }

    private void deleteList(RoleInfo roleInfo) {
        ArrayList<WeightTmpEntity> selected = recyclerAdapter.getSelected();
        recyclerAdapter.deleteNotify();
        if (roleInfo != null && Account.getInstance(this).getRoleInfo().getId() == roleInfo.getId()) {
            HomeDataSet.putBasesfilter(entities);
        }
        WeightTmpDB.getInstance(this).remove(selected);
    }
    public void startAddRoleActivity(){
//        if (RoleDB.getInstance(this).getRoleCount(Account.getInstance(this).getAccountInfo().getId()) >= 8) {
//            CustomToast.showToast(this, getString(R.string.myselfNoAdd),Toast.LENGTH_SHORT);
//            return;
//        }
    }
    private void fillData(RoleInfo roleInfo) {
        WeighDataParser.create(this).fillFatWithImpedance(entities, roleInfo);
        WeightDataDB.getInstance(this).create(entities);
        deleteList(roleInfo);
        LocalBroadcastUtil.notifyAction(this, LocalBroadcastUtil.ACTION_TEMP_WEIGHT_DATA_MATCH_ROLE);
    }
}
