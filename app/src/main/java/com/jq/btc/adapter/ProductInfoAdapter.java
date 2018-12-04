package com.jq.btc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jq.btc.app.R;
import com.jq.code.model.ProductInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 角色信息
 * 
 * @author hfei
 * 
 */
public class ProductInfoAdapter extends BaseAdapter {

	private ArrayList<Map<String, String>> mList;
	private LayoutInflater inflater;
	private Context context;

	public ProductInfoAdapter(Context context, ProductInfo info) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		initList(info);
	}
	public void update(ProductInfo productInfo) {
		initList(productInfo);
		notifyDataSetChanged();
	}
	private void initList(ProductInfo info) {
		mList = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", context.getString(R.string.settingCompanyName));
		map.put("content", info.getName());
		mList.add(map);

		map = new HashMap<String, String>();
		map.put("name", context.getString(R.string.settingCompanyPhone));
		map.put("content", info.getPhone());
		mList.add(map);

		map = new HashMap<String, String>();
		map.put("name", context.getString(R.string.settingCompanyAddr));
		map.put("content", info.getAddress());
		mList.add(map);
	}

	@Override
	public int getCount() {

		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.item_product_info_listview, null);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView content = (TextView) view.findViewById(R.id.content);
		ImageView arrowImg = (ImageView) view.findViewById(R.id.arrowImg);
		name.setText(mList.get(position).get("name") + "");
		content.setText(mList.get(position).get("content") + "");
		if(mList.get(position).get("name").equals(context.getString(R.string.settingCompanyPhone)) && !TextUtils.isEmpty(mList.get(position).get("content")) ){
			arrowImg.setVisibility(View.VISIBLE);
		}else {
			arrowImg.setVisibility(View.GONE);
		}
		return view;
	}

}
