package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import com.jq.btc.app.R;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.BGlucoseEntity;
import com.jq.code.view.text.CustomTextView;

import java.util.List;


public class BGlucoseTrendDetalisListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> dates;
    private List<List<BGlucoseEntity>> putBases;

    public BGlucoseTrendDetalisListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> dates, List<List<BGlucoseEntity>> putBases) {
        this.dates = dates;
        this.putBases = putBases;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return dates == null ? 0 : dates.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (putBases == null) return 0;
        if (putBases.isEmpty()) return 0;
        return putBases.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dates.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return putBases.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GViewHolder gViewHolder = null;
        if (convertView == null) {
            gViewHolder = new GViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.trend_detalis_item_head, parent, false);
            gViewHolder.type = (CustomTextView) convertView.findViewById(R.id.trend_details_head_type);
            gViewHolder.date = (CustomTextView) convertView.findViewById(R.id.trend_details_head_date);
            convertView.setTag(gViewHolder);
        } else {
            gViewHolder = (GViewHolder) convertView.getTag();
        }
        gViewHolder.date.setText(TimeUtil.dateFormatChange(dates.get(groupPosition), TimeUtil.TIME_FORMAT2, TimeUtil.TIME_FORMAT6));
        gViewHolder.type.setText("mmol/L");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bglucose_trend_detalis_item, parent, false);
            holder.comentText = (CustomTextView) convertView.findViewById(R.id.comentText);
            holder.valueText = (CustomTextView) convertView.findViewById(R.id.valueText);
            holder.timeText = (CustomTextView) convertView.findViewById(R.id.timeText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BGlucoseEntity showInfo = putBases.get(groupPosition).get(childPosition);
        holder.comentText.setText(BGlucoseEntity.BGlucoseType.create().get(showInfo.getDescription() - 1));
        holder.valueText.setText(showInfo.getBsl() + "");
        holder.timeText.setText(showInfo.getMeasure_time().substring(11, 16));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private class ViewHolder {
        CustomTextView comentText;
        CustomTextView valueText;
        CustomTextView timeText;
    }

    private class GViewHolder {
        CustomTextView date, type;
    }
}
