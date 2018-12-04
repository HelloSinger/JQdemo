package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import com.jq.btc.app.R;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.BPressEntity;
import com.jq.code.view.text.CustomTextView;

import java.util.List;


public class BPressTrendDetalisListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> dates;
    private List<List<BPressEntity>> putBases;

    public BPressTrendDetalisListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<String> dates, List<List<BPressEntity>> putBases) {
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
        gViewHolder.type.setText("SYS/DIA");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.bpress_trend_detalis_item, parent, false);
            holder.SYSText = (CustomTextView) convertView.findViewById(R.id.SYSText);
            holder.DIAText = (CustomTextView) convertView.findViewById(R.id.DIAText);
            holder.timeText = (CustomTextView) convertView.findViewById(R.id.timeText);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BPressEntity showInfo = putBases.get(groupPosition).get(childPosition);
        holder.SYSText.setText(showInfo.getSys() + "");
        holder.DIAText.setText(showInfo.getDia() + "");
        holder.timeText.setText(showInfo.getMeasure_time().substring(11, 16));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        CustomTextView SYSText;
        CustomTextView DIAText;
        CustomTextView timeText;
    }

    private class GViewHolder {
        CustomTextView date, type;
    }
}
