package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.code.model.RoleInfo;
import com.jq.code.view.CircleImageView;

import java.util.List;


public class MatchingGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<RoleInfo> mRoleInfos;

    public MatchingGridAdapter(Context context, List<RoleInfo> roleInfos) {
        mContext = context;
        mRoleInfos = roleInfos;
    }

    public void update(List<RoleInfo> roleInfos) {
        mRoleInfos = roleInfos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mRoleInfos == null ? 0 : mRoleInfos.size() + 1;
    }

    @Override
    public RoleInfo getItem(int position) {
        return mRoleInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.matching_grid_item, parent, false);
            holder.nameText = (TextView) convertView.findViewById(R.id.nameText);
            holder.headImg = (CircleImageView) convertView.findViewById(R.id.headImg);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() - 1) {
            holder.nameText.setText(R.string.reportNewRole);
            holder.headImg.setImageResource(R.mipmap.role_add);
        } else {
            RoleInfo roleInfo = mRoleInfos.get(position);
            holder.nameText.setText(roleInfo.getNickname());
        }
        return convertView;
    }

    private class ViewHolder {
        private CircleImageView headImg;
        private TextView nameText;
    }
}
