package com.jq.btc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.jq.btc.app.R;
import com.jq.code.model.sport.BiteUnit;
import com.jq.code.view.flowLayout.FlowLayout;
import com.jq.code.view.flowLayout.TagAdapter;
import com.jq.code.view.flowLayout.TagFlowLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */

public class BiteTagFlowLayoutAdapter extends TagAdapter<BiteUnit> {
    private Context mContext ;
    private TagFlowLayout layout ;
    public BiteTagFlowLayoutAdapter(Context context ,TagFlowLayout layout, List<BiteUnit> datas) {
        super(datas);
        this.layout = layout ;
        this.mContext = context ;
    }

    @Override
    public View getView(FlowLayout parent, int position, BiteUnit unit) {
        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        TextView tv = (TextView) mInflater.inflate(R.layout.bite_taglayout_item, layout,false);
        tv.setText(unit.getUnit());
        return tv;
    }
}
