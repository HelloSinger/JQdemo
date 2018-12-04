package com.jq.code.view.wheel.adapter;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jq.code.view.text.CustomTextView;
import com.jq.code.view.wheel.TosGallery;
import java.util.List;


public class WheelViewStringTextAdapter extends BaseAdapter {

	private List<String> textData;
	private Context context;
	private int mCenterIndex = -1;
	private int mCenterColor;
	private int mOthersColor;
	private int gravity = Gravity.CENTER;
	private int mHeight = 110;

	public WheelViewStringTextAdapter(Context context,  List<String> textData) {
		this.context = context;
		this.textData = textData ;
	}

	@Override
	public int getCount() {
		return textData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return textData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	public void setTextColor(int vulue, int centerColor, int othersColor) {
		mCenterIndex = vulue;
		mCenterColor = centerColor;
		mOthersColor = othersColor;
		notifyDataSetChanged();
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CustomTextView txtView = null;

		if (null == convertView) {
			convertView = new CustomTextView(context);
			convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
			txtView = (CustomTextView) convertView;
		}

		String text = textData.get(position);
		if (null == txtView) {
			txtView = (CustomTextView) convertView;
		}
		txtView.setGravity(gravity);
		if (mCenterIndex == position) {
			txtView.setTextColor(mCenterColor);
			txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 21);
		} else {
			txtView.setTextColor(mOthersColor);
			txtView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 19);
		}
		txtView.setText(text);
		return convertView;
	}
}
