package com.jq.btc.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.jq.btc.adapter.PopRoleListAdapter;
import com.jq.btc.app.R;
import com.jq.code.model.RoleInfo;
import com.jq.code.view.dialog.BasePopudialog;

import java.util.ArrayList;
import java.util.List;

public class RoleListPop extends BasePopudialog implements PopupWindow.OnDismissListener, AdapterView.OnItemClickListener, View.OnClickListener {

    public interface OnCloseCallback {
        void onClose();
    }

    private OnCloseCallback closeCallback;

    public void setCloseCallback(OnCloseCallback closeCallback) {
        this.closeCallback = closeCallback;
    }

    private PopRoleListAdapter adapter;
    private ListView listView;
    private View decorView;
    private ImageView closeImg,addImg  ;
    private List<RoleInfo> roleInfos ;

    public RoleListPop(Activity context , ArrayList<RoleInfo> roleInfos) {
        super(context);
        this.roleInfos = roleInfos ;
        setBackgroundDrawable(new ColorDrawable());
        decorView = context.getWindow().getDecorView();
        setOnDismissListener(this);
        View rootView = LayoutInflater.from(context).inflate(R.layout.pop_role_list, null);
        this.setContentView(rootView);
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        this.setWidth(width/2);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        listView = (ListView) rootView.findViewById(R.id.listview);
        if(Build.VERSION.SDK_INT<= Build.VERSION_CODES.LOLLIPOP){
            rootView.setPadding(0,dip2px(context,20),0,0);
        }else {
            rootView.setPadding(0,dip2px(context,10),0,0);
        }
        addImg = (ImageView) rootView.findViewById(R.id.addImg);
        closeImg = (ImageView) rootView.findViewById(R.id.closeImg);
        adapter = new PopRoleListAdapter(context,  roleInfos);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        closeImg.setOnClickListener(this);
        addImg.setOnClickListener(this);
        this.setAnimationStyle(R.style.popwindow_anim_style);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void showPop(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - this.getHeight());
        setWindowAlpha(0.2f);
    }

    private void setWindowAlpha(float alpha) {
        decorView.setAlpha(alpha);
    }



    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        setWindowAlpha(0.2f);
    }


    @Override
    public void onDismiss() {
        setWindowAlpha(1f);

        if(null != closeCallback) {
            closeCallback.onClose();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(checkImp != null){
            checkImp.checkRoleResult(roleInfos.get(position));
        }
        dismiss();
    }

    @Override
    public void onClick(View v) {
        if(v == addImg){
            if(checkImp != null){
                checkImp.checkRoleResult(null);
            }
        }
        dismiss();
    }

    public interface RoleCheckImp {
        void checkRoleResult(RoleInfo roleInfo) ;
    }
    private RoleCheckImp checkImp  ;

    public void setRoleCheckImp(RoleCheckImp dateImp) {
        this.checkImp = dateImp;
    }


}
