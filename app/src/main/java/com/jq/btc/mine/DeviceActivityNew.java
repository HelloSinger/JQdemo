package com.jq.btc.mine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.btc.app.R2;
import com.jq.btc.bluettooth.BoundDeviceActivity;
import com.jq.btc.helper.CsBtEngine;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.ActivityUtil;
import com.jq.code.code.util.ScreenUtils;
import com.jq.code.model.ScaleInfo;
import com.jq.code.view.dialog.TipDialog;
import com.wyh.slideAdapter.ItemBind;
import com.wyh.slideAdapter.ItemView;
import com.wyh.slideAdapter.SlideAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hfei on 2015/10/20.
 */
public class DeviceActivityNew extends Activity {

    @BindView(R2.id.device_add_click)
    ImageView device_add_click;
    @BindView(R2.id.device_recycler)
    RecyclerView device_recycler;
    @BindView(R2.id.backImager)
    ImageView backImager;
    @BindView(R2.id.mTitle)
    TextView mTitle;
    @BindView(R2.id.mBoundDevice)
    TextView mBoundDevice;
    @BindView(R2.id.mDeviceInfo)
    TextView mDeviceInfo;
    @BindView(R2.id.mUnBoundDevice)
    TextView mUnBoundDevice;
    @BindView(R2.id.mBoundedLayout)
    RelativeLayout mBoundedLayout;
    @BindView(R2.id.mWholeLayout)
    LinearLayout mWholeLayout;
    private ArrayList<String>list = new ArrayList<>();
    private SlideAdapter adapter ;

    private TipDialog mTipDialog;
    private CsBtEngine mCsBtEngine;
    private  ItemBind<String>item;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtil.getInstance().addActivity(this);
        setContentView(R.layout.activity_device_new);
        ButterKnife.bind(this);

        mCsBtEngine = CsBtEngine.getInstance(this);
        initView();
        ScreenUtils.setScreenFullStyle(this, Color.WHITE);
    }

    private void initView() {
        list.clear();
        device_recycler.setLayoutManager(new LinearLayoutManager(this));

        mWholeLayout.setPadding(0, ScreenUtils.getStatusBarHeight(this), 0, 0);

        if (ScaleParser.getInstance(this).isBluetoothBounded()) {
            list.add("1");//体脂称
        }
        SharedPreferences sh = getSharedPreferences("kitchens", Context.MODE_PRIVATE);
        final String kitchen_name = sh.getString("kitchen_name", "");
        String kitchen_mac = sh.getString("kitchen_mac", "");
        if(!kitchen_mac.equals("")){
            list.add("2");//营养称
        }

        if(list.size()>0){
//            mBoundedLayout.setVisibility(View.GONE);
            device_recycler.setVisibility(View.VISIBLE);
        }else {
//            mBoundedLayout.setVisibility(View.VISIBLE);
            device_recycler.setVisibility(View.GONE);
        }
//        GradientDrawable gd = new GradientDrawable();
//        gd.setColor(getResources().getColor(R.color.mainColor));
//        float roundRadius = getResources().getDisplayMetrics().density * 90;
//        gd.setCornerRadius(roundRadius);
//        mBoundDevice.setBackground(gd);
//        mDeviceInfo.setBackground(gd);
//
//        GradientDrawable gd2 = new GradientDrawable();
//        gd2.setColor(getResources().getColor(R.color.white));
//        gd2.setCornerRadius(roundRadius);
//        gd2.setStroke((int)(getResources().getDisplayMetrics().density * 0.5f), getResources().getColor(R.color.mainColor));
//        mUnBoundDevice.setBackground(gd2);

        item = new ItemBind<String>() {
            @Override
            public void onBind(final ItemView itemView, String s,  int i) {
                ImageView imgs = itemView.getView(R.id.menu_imgs);
                imgs.setVisibility(View.GONE);
                TextView tv = itemView.getView(R.id.menu_delets_text);
                tv.setVisibility(View.VISIBLE);
                tv.setText("解绑");
                if(list.get(i).equals("1")){
                    itemView.setText(R.id.device_names,"智能体脂称");
                    itemView.setImageResource(R.id.device_img,R.drawable.fat_imgs);
                }else {
                    itemView.setText(R.id.device_names,"智能营养称");
                    itemView.setImageResource(R.id.device_img,R.drawable.kitchen_imgs);
                }

                final int finalI = i;
                itemView.setOnClickListener(R.id.menu_delets, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBLETip(finalI);
                    }
                });
                RelativeLayout r = itemView.getView(R.id.device_clicks);
                r.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showPopMenu(v, finalI);
                        return true;
                    }
                });
                r.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(list.get(finalI).equals("1")){
                            new DeviceInfoDialog(DeviceActivityNew.this,2).showDialog();
                        }else {
                            new DeviceInfoDialog(DeviceActivityNew.this,1).showDialog();
                        }
                    }
                });
            }
        };

        adapter = SlideAdapter.load(list).item(R.layout.item_device,0,0,R.layout.menu_delet,0.2f)
                .bind(item).into(device_recycler);

        adapter.notifyDataSetChanged();
    }

    @OnClick({R2.id.device_add_click,R2.id.backImager, R2.id.mBoundDevice, R2.id.mDeviceInfo, R2.id.mUnBoundDevice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R2.id.backImager:
                finish();
                break;
            case R2.id.device_add_click:
                // 绑定设备
                startActivity(new Intent(this, BoundDeviceActivity.class));
                break;
            case R2.id.mDeviceInfo:
                // 设备信息
                new DeviceInfoDialog(this,1).showDialog();
                break;
            case R2.id.mUnBoundDevice:
                // 解绑设备
                showBLETip(0);
                break;
        }
    }

    private void showBLETip(int i) {
        final int finalI = i;
            mTipDialog = new TipDialog(this);
            mTipDialog.setText(R.string.settingDeviceUnboundTip);
            mTipDialog.setRightButtonText(R.string.unbound);
            mTipDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v("===333",""+finalI);
                    if(list.get(finalI).equals("1")){
                        // 重新设置tag
                        ScaleParser.getInstance(DeviceActivityNew.this).setBluetoothState(ScaleParser.STATE_BLUETOOTH_UNBOUND);
                        mCsBtEngine.stopSearch();
                        mCsBtEngine.stopAutoConnect();
                        mCsBtEngine.closeGATT(true);
                        ScaleParser.getInstance(DeviceActivityNew.this).setScale(new ScaleInfo());
                        list.remove(finalI);
                        adapter.notifyItemRemoved(finalI);
                    }else {
                        SharedPreferences sh = getSharedPreferences("kitchens", Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sh.edit();
                        edit.clear();
                        edit.commit();
                        list.remove(finalI);
                        adapter.notifyItemRemoved(finalI);
                    }
                    adapter.notifyDataSetChanged();
                    mTipDialog.dismiss();
                }
            });
        mTipDialog.showDialog();
    }

    public void showPopMenu(View view,final int pos){
        PopupMenu popupMenu = new PopupMenu(this,view);
        popupMenu.getMenuInflater().inflate(R.menu.item_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                showBLETip(pos);
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }
}
