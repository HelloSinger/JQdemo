package com.jq.btlib;

import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.device.CsWeigher;
import com.jq.btlib.util.CsBtUtil;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements CsBtUtil.OnBluetoothListener {

	
	
	CsBtUtil aaa = null;
	String mac = null;
	BluetoothGattCharacteristic mWriteCharacteristic;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		
		Button bn = (Button) findViewById(R.id.bn);
		Button bn1 = (Button) findViewById(R.id.bn1);
		Button bn2 = (Button)findViewById(R.id.bn2);
		Button bn3 = (Button)findViewById(R.id.bn3);
		Button bn4 = (Button)findViewById(R.id.bn4);
		aaa = new CsBtUtil(this);
		aaa.setBluetoothListener(this);
		
		bn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.openBluetooth(true);
				aaa.startSearching();
			}
		});
		
		bn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aaa.stopSearching();
			}
		});
		
		bn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("0000000000","MAC --->  "+mac);
				aaa.stopSearching();
				if(mac != null)
					aaa.connectBTDevice(mac);
			}
		});
		
		bn3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("0000000000","MAC --->  "+mac);
				aaa.disconnectBTDevice();
				aaa.startSearching();
			}
		});		
		bn4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				byte[] values = null;
				if((mWriteCharacteristic != null)&&aaa.isConnected())
					values =  new byte[] { (byte) 0xca,
						(byte) 0x10, (byte) 0x0e, 0x01, 0x0e, 0x0c,
						0x0c, 0x0c, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x01,
						0x0c, 0x2, 0x1,0x0, 0x00 };
					aaa.writeCharacteristic(mWriteCharacteristic, values);
			}
		});
	}

	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		aaa.stopSearching();
	}

	@Override
	public void broadcastData(String mac, byte[] advData) {
		// TODO Auto-generated method stub
		Log.i("XXX", "MAC-"+mac+" advData-"+advData);
		this.mac = mac;
	}

	@Override
	public void broadcastWeigherInfo(CsWeigher weigher, boolean isLock) {
		// TODO Auto-generated method stub
		Log.i("XXX", "weigher-"+weigher);
	}

	@Override
	public void broadcastFatScaleInfo(CsFatScale fatScale, boolean isLock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void specialFatScaleInfo(CsFatScale fatScale, boolean isLock) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void didConnectedGetWriteNotifyCharacteristic(BluetoothGattCharacteristic notify,
			BluetoothGattCharacteristic write){
		
//		byte[] value =  new byte[] { (byte) 0xca,
//				(byte) 0x10, (byte) 0x0e, 0x01, 0x0e, 0x0c,
//				0x0c, 0x0c, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x01,
//				0x0c, 0x2, 0x1,0x0, 0x00 };
//		aaa.writeCharacteristic(write, value); //不能在这里写！！！
		mWriteCharacteristic = write;
		
	}

	@Override
	public void disconnectBluetooth() {
		// TODO Auto-generated method stub
		Log.i("","disconnectBluetooth");
	}


	@Override
	public void bluetoothStateChange(int state) {
		// TODO Auto-generated method stub
		
	}

}
