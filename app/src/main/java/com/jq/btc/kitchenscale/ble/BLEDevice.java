package com.jq.btc.kitchenscale.ble;

import android.bluetooth.BluetoothDevice;
import android.os.ParcelUuid;

import java.io.Serializable;

/**
 *  BLE设备的实体类
 */
public class BLEDevice implements Serializable {
    private BluetoothDevice bluetoothDevice;
    private boolean conntect;
    private int rssi;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }


    public boolean check_uuid (String uuid){
        //TODO 检查是否有指定的UUID
        boolean hasuuid = false;
        if(bluetoothDevice != null){
            ParcelUuid[]  uuids = bluetoothDevice.getUuids();

            if(uuids != null && uuids.length > 0){
                for (ParcelUuid temp : uuids){
                    if(temp.equals(uuid)){
                        hasuuid = true;
                    }
                }
            }

        }
        return hasuuid;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public boolean isConntect() {
        return conntect;
    }

    public void setConntect(boolean conntect) {
        this.conntect = conntect;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}

