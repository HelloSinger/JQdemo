package com.jq.btc.kitchenscale.ble;

/**
 * Created by 601042 on 2017/5/26.
 */
public interface GetDataCallback {
    void onGetData(String characteristicUUid, byte[] data);

}
