package com.jq.btlib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.jq.btlib.util.ThreadUtil;

/**
 * Created by lixun on 2016/3/11.
 */
public class JBBluetoothLeScannerImpl extends BluetoothLeScannerCompat {
    private BluetoothAdapter mBluetoothAdapter;

    public JBBluetoothLeScannerImpl(){
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }
    @Override
    public boolean startLeScanInternal() {
        if(mBluetoothAdapter!=null){
            return mBluetoothAdapter.startLeScan(mLeScanCallback);
        }else{
            return false;
        }

    }

    @Override
    public void stopLeScanInternal() {
        if(mBluetoothAdapter!=null){
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             final byte[] scanRecord) {
            if (device == null) {
                return;
            }
            if (device.getAddress() == null) {
                return;}

            ThreadUtil.executeThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        handleBroadcastInfo(device, rssi, scanRecord);
                    }
                }
            });
        }
    };


}
