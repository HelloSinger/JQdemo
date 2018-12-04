package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jq.btc.app.R;

/**
 * Created by hfei on 2015/10/19.
 */
public class RoleGridviewAdapter extends BaseAdapter{
    private Context context;

    public RoleGridviewAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.role_gridview_item,parent,false);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }



    private class  ViewHolder{

    }
}
