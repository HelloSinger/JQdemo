package com.jq.btc.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.WeightTmpEntity;

import java.util.ArrayList;

/**
 * Created by xulj on 2016/9/8.
 */
public class MatchingRecyclerAdapter extends RecyclerView.Adapter<MatchingRecyclerAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<WeightTmpEntity> tmpEntities;
    private ArrayList<Boolean> ischeckeds = new ArrayList<>();

    public MatchingRecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<WeightTmpEntity> tmpEntities) {
        this.tmpEntities = tmpEntities;
        ischeckeds.clear();
        if (tmpEntities != null) {
            for (int i = 0; i < tmpEntities.size(); i++) {
                ischeckeds.add(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.matching_recyclerview_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public void onBindViewHolder(MyViewHolder tempHolder, int position) {
        tempHolder.refreshData(position);
    }

    @Override
    public int getItemCount() {
        return tmpEntities == null ? 0 : tmpEntities.size();
    }

    public void checkAll(boolean isChecked) {
        if (ischeckeds != null) {
            for (int i = 0; i < ischeckeds.size(); i++) {
                ischeckeds.set(i, isChecked);
            }
        }
        notifyDataSetChanged();
    }

    ArrayList<WeightTmpEntity> deleteEntities = new ArrayList<>();

    public ArrayList<WeightTmpEntity> getSelected() {
        deleteEntities.clear();
        if (ischeckeds != null) {
            for (int i = 0; i < ischeckeds.size(); i++) {
                if (ischeckeds.get(i)) {
                    deleteEntities.add(tmpEntities.get(i));
                }
            }
        }
        return deleteEntities;
    }

    public void deleteNotify() {
        for (int i = 0; i < deleteEntities.size(); i++) {
            for (int j = 0; j < tmpEntities.size(); j++) {
                if (deleteEntities.get(i).getId() == tmpEntities.get(j).getId()) {
                    tmpEntities.remove(j);
                    ischeckeds.remove(j);
                    notifyItemRemoved(j);
                    notifyItemRangeChanged(j, tmpEntities.size() - j);
                }
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView timeText;
        TextView weightText;
        Context context;
        CheckBox checkBox;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            dateText = (TextView) itemView.findViewById(R.id.dateText);
            timeText = (TextView) itemView.findViewById(R.id.timeText);
            weightText = (TextView) itemView.findViewById(R.id.weightText);
            checkBox = (CheckBox) itemView.findViewById(R.id.weightCheckbox);
        }

        public void refreshData(final int position) {
            WeightTmpEntity tmpEntity = tmpEntities.get(position);
            String weight_time = tmpEntity.getWeight_time();
            if (weight_time != null) {
                String[] split = weight_time.split(" ");
                dateText.setText(split[0]);
                timeText.setText(split[1]);
            }
            checkBox.setChecked(ischeckeds.get(position));
            weightText.setText(StandardUtil.getWeightExchangeValueforVer2(context, tmpEntity.getWeight(), tmpEntity.getScaleweight(), tmpEntity.getScaleproperty()));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ischeckeds.set(position, isChecked);
                }
            });
        }
    }
}
