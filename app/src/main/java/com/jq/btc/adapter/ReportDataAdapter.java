package com.jq.btc.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jq.btc.app.R;
import com.jq.code.code.util.ViewUtil;
import com.jq.code.view.text.CustomTextView;

import java.util.List;
import java.util.Map;


public class ReportDataAdapter extends BaseAdapter {

    private List<Map<String, Object>> dataMap;
    private LayoutInflater mInflater;
    private Context context;
    private int itemHeight;

    public ReportDataAdapter(Context context, List<Map<String, Object>> dataMap) {
        this.dataMap = dataMap;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }


    @Override
    public int getCount() {
        return dataMap == null ? 0 : dataMap.size();
    }

    @Override
    public Object getItem(int position) {
        return dataMap == null ? null : dataMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_roledata_gridview, parent, false);
            holder.name = (CustomTextView) convertView.findViewById(R.id.item_roledata_name);
            holder.standard = (CustomTextView) convertView.findViewById(R.id.item_roledata_standard);
            holder.value = (CustomTextView) convertView.findViewById(R.id.item_roledata_value);
            holder.value1 = (CustomTextView) convertView.findViewById(R.id.item_roledata_value1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(dataMap.get(position).get("name").toString());
        holder.standard.setText(dataMap.get(position).get("standardName").toString());
        int standardColor = Integer.valueOf(dataMap.get(position).get("standardColor").toString());
        if (standardColor != 0) {
            holder.standard.setTextColor(context.getResources().getColor(R.color.white));
            holder.standard.setPadding(ViewUtil.dip2px(context,10f),ViewUtil.dip2px(context,2F),ViewUtil.dip2px(context,10),ViewUtil.dip2px(context,2));
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setCornerRadius(50);
            shape.setColor(context.getResources().getColor(standardColor));
            holder.standard.setBackground(shape);
        }else {
            holder.standard.setTextColor(context.getResources().getColor(R.color.text_gray));
            holder.standard.setPadding(ViewUtil.dip2px(context,20f),ViewUtil.dip2px(context,2F),ViewUtil.dip2px(context,20f),ViewUtil.dip2px(context,2));
            holder.standard.setText("- -");
            holder.standard.setBackgroundResource(R.drawable.gray_stroke_50);
        }
        if (context.getString(R.string.reportNoData).equals(dataMap.get(position).get("value").toString())) {
            holder.value1.setVisibility(View.VISIBLE);
            holder.value.setVisibility(View.INVISIBLE);
            holder.value1.setText(dataMap.get(position).get("value").toString());
        } else {
            holder.value1.setVisibility(View.INVISIBLE);
            holder.value.setVisibility(View.VISIBLE);
            holder.value.setText(dataMap.get(position).get("value").toString());
        }
        if (itemHeight != 0) {
            ViewGroup.LayoutParams layoutParams = convertView.getLayoutParams();
            layoutParams.height = itemHeight;
            convertView.setLayoutParams(layoutParams);
        } else {
            itemHeight = ViewUtil.getMeasureheight(convertView);
        }
        return convertView;
    }

    public void update(List<Map<String, Object>> dataMap) {
        this.dataMap = dataMap;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        CustomTextView name, standard, value, value1;
    }
}
