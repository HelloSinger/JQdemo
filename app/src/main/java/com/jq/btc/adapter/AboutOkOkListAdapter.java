package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.view.text.CustomTextView;

public class AboutOkOkListAdapter extends BaseAdapter {
    private int[] titleStr = new int[]{R.string.settingAgreement};
    private int[] icon = {R.mipmap.setting_protect};
    private Context mContext;

    public AboutOkOkListAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return titleStr.length + 1;
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
        if (position == 0) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.about_okok_top, null);
            CustomTextView varsion = (CustomTextView) convertView.findViewById(R.id.about_okok_version);
            try {
                varsion.setText("当前版本：" + PrefsUtil.getVersionName(mContext));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.about_okok_list_item, null);
            ImageView imagIcon = (ImageView) convertView.findViewById(R.id.icon);
            TextView title = (TextView) convertView.findViewById(R.id.title);
            imagIcon.setImageResource(icon[position - 1]);
            title.setText(titleStr[position - 1]);
        }
        return convertView;
    }
}



