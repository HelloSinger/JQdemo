package com.jq.code.code.business;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.jq.code.R;

import java.util.Timer;
import java.util.TimerTask;

public class VerifyCode {

	private Activity mContext;
	private static Timer mTimer;
	private static final int TIME_SEND = 0;
	private static final int TIME_END = 1;
	private TextView mTextView;

	public VerifyCode(Activity context, TextView textView) {
		mContext = context;
		mTextView = textView;
		mTimer = new Timer();
	}

	@SuppressLint("HandlerLeak")
	Handler timeHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TIME_SEND:
				mTextView.setText(msg.arg1 + "s");
				break;
			case TIME_END:
				mTextView.setText(R.string.registerGetVerification);
				mTextView.setClickable(true);
				break;
			default:
				break;
			}
		}
    };

	public void setText(String text){
		if(mTextView != null){
			mTextView.setText(text);
		}
	}

	public void setText(int text){
		if(mTextView != null){
			mTextView.setText(text);
		}
	}

	public void setClickable(boolean isclickable){
		if(mTextView != null){
			mTextView.setClickable(isclickable);
		}
	}

	public void cancelTask(){
		if(mTimer != null){
			mTimer.cancel();
		}
	}

	/**
	 * 开始计时
	 */
	public void startTimer() {
		mTimer.schedule(new TimerTask() {
			int sum = 60;
			@Override
			public void run() {
				while (sum >= 0 && !mContext.isFinishing()) {
					try {
						Thread.sleep(1000);
						sum--;
						Message message = new Message();
						message.what = TIME_SEND;
						message.arg1 = sum;
						timeHandler.sendMessage(message);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				timeHandler.sendEmptyMessage(TIME_END);
			}
		}, 100);
	}
}
