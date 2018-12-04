package com.jq.btlib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

import com.jq.btlib.util.LogUtil;
import com.jq.btlib.util.ThreadUtil;

import java.util.List;

/**
 * Created by lixun on 2016/3/11.
 */
public class LollipopBluetoothLeScannerImpl extends BluetoothLeScannerCompat{
    private BluetoothAdapter mBluetoothAdapter;

    public LollipopBluetoothLeScannerImpl(){
        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public boolean startLeScanInternal() {
        ScanSettings.Builder localBuilder = new ScanSettings.Builder();
//        if ((mBluetoothAdapter.isOffloadedScanBatchingSupported()))
//            localBuilder.setReportDelay(500L);
        localBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);

        if(mBluetoothAdapter!=null) {
            BluetoothLeScanner localBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if(localBluetoothLeScanner!=null){
                localBluetoothLeScanner.startScan(null, localBuilder.build(), callback);
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    @Override
    public void stopLeScanInternal() {
        if(mBluetoothAdapter!=null){
            BluetoothLeScanner localBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if(localBluetoothLeScanner!=null){
                localBluetoothLeScanner.stopScan(callback);
            }
        }

    }

    private ScanCallback callback=new ScanCallback() {

        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            super.onScanResult(callbackType, result);
            final BluetoothDevice device=result.getDevice();

            if (device.getAddress() == null) {return;}

            ThreadUtil.executeThread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        handleBroadcastInfo(device, result.getRssi(), result.getScanRecord().getBytes());
                    }
                }
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            for(final ScanResult result:results){
                final BluetoothDevice device=result.getDevice();
                if (device.getAddress() == null) {return;}

                ThreadUtil.executeThread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (this) {
                            handleBroadcastInfo(device, result.getRssi(), result.getScanRecord().getBytes());
                        }
                    }
                });
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtil.e(TAG, "onScanFailed:" + errorCode);
        }
    };
}
