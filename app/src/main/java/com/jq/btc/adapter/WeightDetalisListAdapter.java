package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.jq.btc.app.R;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.text.CustomTextView;

import java.util.List;


public class WeightDetalisListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> dates;
    private List<List<WeightEntity>> putBases;
    private String unit;

    public WeightDetalisListAdapter(Context context) {
        this.context = context;
        unit = StandardUtil.getWeightExchangeUnit(context);
    }

    public void setData(List<String> dates, List<List<WeightEntity>> putBases) {
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
    public WeightEntity getChild(int groupPosition, int childPosition) {
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
            gViewHolder.date = (CustomTextView) convertView.findViewById(R.id.trend_details_head_date);
            gViewHolder.type = (CustomTextView) convertView.findViewById(R.id.trend_details_head_type);
            convertView.setTag(gViewHolder);
        } else {
            gViewHolder = (GViewHolder) convertView.getTag();
        }
        gViewHolder.date.setText(TimeUtil.dateFormatChange(dates.get(groupPosition), TimeUtil.TIME_FORMAT2, TimeUtil.TIME_FORMAT6));
        gViewHolder.type.setText(unit);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_weight_detalis, parent, false);
            holder.valueText = (CustomTextView) convertView.findViewById(R.id.valueText);
            holder.timeText = (CustomTextView) convertView.findViewById(R.id.timeText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WeightEntity showInfo = putBases.get(groupPosition).get(childPosition);
        holder.valueText.setText(StandardUtil.getWeightExchangeValue(context, showInfo.getWeight(),
                showInfo.getScaleweight(), showInfo.getScaleproperty()) +"");
        holder.timeText.setText(showInfo.getWeight_time().substring(11, 16));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        CustomTextView valueText;
        CustomTextView timeText;
    }

    private class GViewHolder {
        CustomTextView date, type;
    }
}
