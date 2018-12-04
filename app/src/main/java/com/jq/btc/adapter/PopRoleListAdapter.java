package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jq.btc.app.R;
import com.jq.code.model.RoleInfo;
import com.jq.code.view.CircleImageView;
import com.jq.code.view.text.CustomTextView;

import java.util.List;

/**
 * Created by hfei on 2015/10/19.
 */
public class PopRoleListAdapter extends BaseAdapter {
    private Context context ;
    private List<RoleInfo> roleInfos ;

    public PopRoleListAdapter(Context context, List<RoleInfo> roleInfos) {
        this.context = context;
       this.roleInfos = roleInfos ;
    }
    @Override
    public int getCount() {
        return roleInfos.size() ;
    }

    @Override
    public Object getItem(int position) {
        return roleInfos.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null ;
        if(convertView  == null){
            holder = new ViewHolder() ;
            convertView = LayoutInflater.from(context).inflate(R.layout.role_pop_list_item,null) ;
            holder.headImager = (CircleImageView) convertView.findViewById(R.id.headImager);
            holder.nameText = (CustomTextView) convertView.findViewById(R.id.nameText);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        RoleInfo roleInfo = roleInfos.get(position) ;
        holder.nameText.setText(roleInfo.getNickname());
        return convertView;
    }
    private class ViewHolder {
        CircleImageView headImager ;
        CustomTextView nameText ;
    }

}
