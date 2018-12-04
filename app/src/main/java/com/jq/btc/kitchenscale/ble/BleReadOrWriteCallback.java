package com.jq.btc.kitchenscale.ble;

/**
 * Created by 601042 on 2017/5/25.
 */
public interface BleReadOrWriteCallback {
    void onReadSuccess();
    void onReadFail(int errorCode);
    void onWriteSuccess();
    void onWriteFail(int errorCode);
    void onServicesDiscovered(int state);
    void onDiscoverServicesFail(int errorCode);
}
