package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.btc.app.R;

public class BloodPressureClassifyAdapter extends BaseAdapter {

    private Context mContext;
    private final int[] classufyStr = new int[]{R.string.bpressIdeal,R.string.bpressNormal,R.string.systolicBPNormal,R.string.mildHypertension,R.string.moderateHypertension,R.string.severeHypertension} ;
    private final String[] SYSStr = {"<120","<130","130- 139","140- 159","160- 179",">180"} ;
    private final String[] DIAStr = {"<80","<85","85-89","90-99","100- 109",">110"} ;
    private final Integer[] colorArray = {R.color.bloodpressure_level1,R.color.bloodpressure_level2,R.color.bloodpressure_level3
            ,R.color.bloodpressure_level4,R.color.bloodpressure_level5,R.color.bloodpressure_level6} ;
    public BloodPressureClassifyAdapter(Context context) {
            this.mContext = context ;
    }

    @Override
    public int getCount() {
        return classufyStr.length + 2;
    }

    @Override
    public Object getItem(int position) {
        return classufyStr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(position == 0){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bloodpressure_classify_top, null);
        }else if(position == classufyStr.length + 1){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.bloodpressure_classify_bottom, null);
        }else{
//            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.bloodpressure_classify_list_item, null);
                holder.classifyText = (TextView) convertView.findViewById(R.id.classifyText);
                holder.SYSText = (TextView) convertView.findViewById(R.id.SYSText);
                holder.DIAText = (TextView) convertView.findViewById(R.id.DIAText);
                holder.colorImage = (ImageView) convertView.findViewById(R.id.colorImage);
                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
            holder.classifyText.setText(classufyStr[position-1]);
            holder.SYSText.setText(SYSStr[position-1]);
            holder.DIAText.setText(DIAStr[position-1]);
            holder.colorImage.setBackgroundResource(colorArray[position-1]);
        }

        return convertView;
    }

    private class ViewHolder {
        TextView classifyText ;
        TextView SYSText;
        TextView DIAText;
        ImageView colorImage ;
    }
}
