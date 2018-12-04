package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.code.model.blood.BGlucoseHelpEntity;

import java.util.List;

public class BGlucoseClassifyAdapter extends BaseAdapter {

    private Context mContext;
    private List<Object>  objects ;
    private View topView;
    public BGlucoseClassifyAdapter(Context mContext, List<Object>  objects){
        this.mContext = mContext ;
        this.objects = objects ;
        topView = LayoutInflater.from(mContext).inflate(R.layout.bglucose_classify_list_top, null);
    }

    @Override
    public int getCount() {
        return objects.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == 0){
            convertView = topView;
        }else{
            Object obj = objects.get(position - 1) ;
            if(obj instanceof String){
                String str = (String) obj;
                convertView = LayoutInflater.from(mContext).inflate(R.layout.bglucose_classify_list_item_top, null);
                TextView strText = (TextView) convertView.findViewById(R.id.strText);
                strText.setText(str);
            }else{
                ViewHolder holder = null;
                BGlucoseHelpEntity entity = (BGlucoseHelpEntity) obj;
                if(convertView != null && convertView.getTag() instanceof ViewHolder){
                    holder = (ViewHolder) convertView.getTag();
                }else{
                    holder = new ViewHolder() ;
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.bglucose_classify_list_item, null);
                    holder.titleText = (TextView) convertView.findViewById(R.id.titleText) ;
                    holder.contentText = (TextView) convertView.findViewById(R.id.contentText) ;
                    convertView.setTag(holder);
                }
                holder.titleText.setText(entity.getTitle());
                holder.contentText.setText(entity.getContent());
            }
        }

        return convertView;
    }

    private class ViewHolder {
        TextView titleText ;
        TextView contentText;
    }
}
