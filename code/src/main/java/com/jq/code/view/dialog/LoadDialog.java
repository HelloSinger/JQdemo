package com.jq.code.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.jq.code.R;
import com.jq.code.view.gif.GifImageView;

import java.io.IOException;
import java.io.InputStream;

public class LoadDialog extends Dialog {

	private ViewHolder mViewHolder;
	private Context context;

	public LoadDialog(Context context) {
		super(context);
		this.context = context;
	}

	protected LoadDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public LoadDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.item_issucess_dialog);
		this.setCanceledOnTouchOutside(false);
		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (mViewHolder != null) {
					mViewHolder.gifImageView.stopAnimation();
				}
			}
		});
		initViews();
	}

	private void initViews() {
		mViewHolder = new ViewHolder();
		mViewHolder.gifImageView = (GifImageView) findViewById(R.id.gifImageView);
		try {
			InputStream in = context.getAssets().open("load.gif");
			int length = in.available();
			byte[] data = new byte[length];
			in.read(data);
			mViewHolder.gifImageView.setBytes(data);
			mViewHolder.gifImageView.startAnimation();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		super.show();
		if(mViewHolder != null) {
			mViewHolder.gifImageView.startAnimation();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if(mViewHolder != null){
			mViewHolder.gifImageView.stopAnimation();
		}
	}

	class ViewHolder {
		GifImageView gifImageView;
	}

	/**
	 * 获得显示的dialog
	 * */
	public static LoadDialog getShowDialog(Context context) {

		LoadDialog dialog = new LoadDialog(context, R.style.dialog_style);
		Window dialogWindow = dialog.getWindow();

		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);

		lp.x = 0; // 新位置X坐标
		lp.y = 0; // 新位置Y坐标

		lp.alpha = 1f; // 透明度
		dialogWindow.setAttributes(lp);
		return dialog;
	}

	/**
	 * 获得显示的dialog
	 * */
	public static LoadDialog getShowDialog(Context context, int x, int y) {

		LoadDialog dialog = new LoadDialog(context, R.style.dialog_style);
		// dialog宽高是通过layout中直接可以设置的
		Window dialogWindow = dialog.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.x = x; // 新位置X坐标
		lp.y = y; // 新位置Y坐标
		lp.alpha = 1f; // 透明度
		dialogWindow.setAttributes(lp);
		dialog.show();
		return dialog;
	}
}
