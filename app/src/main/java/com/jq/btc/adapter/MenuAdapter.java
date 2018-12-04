package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jq.btc.app.R;
import com.jq.code.view.text.CustomTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单栏
 * Created by hfei on 2015/10/15.
 */
public class MenuAdapter extends BaseAdapter{

    private List<Integer> menuName;
    private List<Integer> menuDrawble;
    private LayoutInflater mInflater;
    private boolean isServerPointVisiable;

    public MenuAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        initList();
    }

    /**
     * 初始化菜单列表
     */
    private void initList() {
        menuName = new ArrayList<Integer>();
        menuName.add(R.string.menuWeightDynamic);
        menuName.add(R.string.menuWeighttrend);
        menuName.add(R.string.menuFamily);
        menuName.add(R.string.menuServer);
        menuName.add(R.string.menuSetting);

        menuDrawble = new ArrayList<Integer>();
        menuDrawble.add(R.mipmap.menu_dym);
        menuDrawble.add(R.mipmap.menu_trend);
        menuDrawble.add(R.mipmap.menu_family);
        menuDrawble.add(R.mipmap.menu_server);
        menuDrawble.add(R.mipmap.menu_setting);
    }

    @Override
    public int getCount() {
        return menuName == null?0:menuName.size();
    }

    @Override
    public Object getItem(int position) {
        return menuName == null?"":menuName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setPointVisiable(boolean visiable){
        isServerPointVisiable = visiable;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_menu,parent,false);
            viewHolder.headImage = (ImageView) convertView.findViewById(R.id.menu_head_image);
            viewHolder.headName = (CustomTextView) convertView.findViewById(R.id.menu_head_name);
            viewHolder.statePoint = (ImageView) convertView.findViewById(R.id.menu_server_flag);
            convertView.setTag(viewHolder);
        }else{
            viewHolder  = (ViewHolder) convertView.getTag();
        }

        viewHolder.headImage.setImageResource(menuDrawble.get(position));
        viewHolder.headName.setText(menuName.get(position));
       if(isServerPointVisiable && menuName.get(position) == R.string.menuServer){
           viewHolder.statePoint.setVisibility(View.VISIBLE);
       }else{
           viewHolder.statePoint.setVisibility(View.INVISIBLE);
       }
        return convertView;
    }

    private class ViewHolder{
        ImageView headImage;
        CustomTextView headName;
        ImageView statePoint;
    }
}
