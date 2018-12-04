package com.jq.btc.receiver;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jq.btc.helper.CsBtEngine;
import com.jq.btlib.util.CsBtUtil;

public class BluetoothReceiver extends BroadcastReceiver {

	private CsBtEngine.OnCsBtBusinessListerner bluetoothListener;

	public void setOnBluetoothListener(CsBtEngine.OnCsBtBusinessListerner bluetoothListener) {
		this.bluetoothListener = bluetoothListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
		String msg = null;
		switch (state) {
		case BluetoothAdapter.STATE_TURNING_ON:
			msg = "turning on";
			break;
		case BluetoothAdapter.STATE_ON:
			msg = "on";
			if (bluetoothListener != null) {
				bluetoothListener.bluetoothStateChange(CsBtUtil.STATE_OPENED);
			}
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			msg = "turning off";
			break;
		case BluetoothAdapter.STATE_OFF:
			msg = "off";
			if (bluetoothListener != null) {
				bluetoothListener.bluetoothStateChange(CsBtUtil.STATE_CLOSE);
			}
			break;
		}
		System.out.println(msg);
	}
}
