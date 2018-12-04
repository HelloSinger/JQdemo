package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jq.btc.app.R;
import com.jq.code.model.DeviceEntity;
import com.jq.code.view.text.CustomTextView;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    private Context mContext;
    private List<DeviceEntity> mEntities;

    public DeviceAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<DeviceEntity> mEntities) {
        this.mEntities = mEntities;
    }

    @Override
    public int getCount() {
        return mEntities == null ? 0 : mEntities.size();
    }

    @Override
    public DeviceEntity getItem(int position) {
        return mEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.device_list_item, null);
            holder.typeNameView = (CustomTextView) convertView.findViewById(R.id.device_list_type_name);
            holder.opreatBtView = (CustomTextView) convertView.findViewById(R.id.device_list_opreat_bt);
            holder.devicebt = (CustomTextView)convertView.findViewById(R.id.device_list_device_bt);
            holder.unboundBtView = (CustomTextView) convertView.findViewById(R.id.device_list_unbund_bt);
            holder.state = (CustomTextView) convertView.findViewById(R.id.device_state);
            holder.typeImgView = (ImageView) convertView.findViewById(R.id.device_list_type_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.typeNameView.setText(mEntities.get(position).getTypeName());
        holder.typeImgView.setImageResource(mEntities.get(position).getTypeImge());
        holder.opreatBtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(l != null){
                   l.onOpreate(v,position);
               }
            }
        });
        holder.devicebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l != null){
                    l.onOpreate(v,position);
                }
            }
        });
        holder.unboundBtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(l != null){
                    l.onBound(v,position);
                }
            }
        });
        if(mEntities.get(position).isBound()){
            holder.devicebt.setVisibility(View.VISIBLE);
            holder.opreatBtView.setVisibility(View.INVISIBLE);
            holder.unboundBtView.setVisibility(View.VISIBLE);
            holder.state.setVisibility(View.VISIBLE);
        }else{
            if(position == 0){
                holder.opreatBtView.setText(mEntities.get(position).getName());
                holder.devicebt.setVisibility(View.INVISIBLE);
                holder.opreatBtView.setVisibility(View.VISIBLE);
                holder.unboundBtView.setVisibility(View.GONE);
                holder.state.setVisibility(View.GONE);
            }else{
                holder.devicebt.setVisibility(View.GONE);
                holder.opreatBtView.setVisibility(View.GONE);
                holder.state.setVisibility(View.GONE);
                holder.unboundBtView.setVisibility(View.VISIBLE);
                holder.unboundBtView.setBackground(null);
                holder.unboundBtView.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                holder.unboundBtView.setText(mEntities.get(position).getName());
            }

        }
        return convertView;
    }

    class ViewHolder {
        CustomTextView typeNameView, opreatBtView, unboundBtView,state,devicebt;
        ImageView typeImgView;
    }
    private OnDeviceClickListener l;
    public void addOnDeviceClickListener(OnDeviceClickListener l){
        this.l = l;
    }

    public interface OnDeviceClickListener{
        void onOpreate(View view,int position);
        void onBound(View view,int position);
    }
}



