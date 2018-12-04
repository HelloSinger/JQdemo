package com.jq.code.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.jq.code.R;
import com.jq.code.view.text.CustomTextView;


public class TokenDialog extends BaseDialog implements OnClickListener {

	private CustomTextView sure;
	public TokenDialog(Context context) {
		super(context);
		View vv = LayoutInflater.from(context).inflate(R.layout.item_token_view, null);
		addView(vv);
		sure = (CustomTextView) vv.findViewById(R.id.tip_sure);
		sure.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view == sure){
			if(l != null){
				l.onClick(sure);
			}
		}
		dismiss();
	}
}
