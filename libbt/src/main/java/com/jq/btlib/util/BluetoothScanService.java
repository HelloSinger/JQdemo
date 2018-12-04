package com.jq.btlib.util;

import android.os.Build;

import com.jq.btlib.scanner.BluetoothLeScannerCompat;
import com.jq.btlib.scanner.JBBluetoothLeScannerImpl;
import com.jq.btlib.scanner.LollipopBluetoothLeScannerImpl;

/**
 * Created by lixun on 2015/12/25.
 */
public class BluetoothScanService {
    private static final String TAG = "CsBtUtil_v11";
    private static BluetoothLeScannerCompat instanace=null;

    public static BluetoothLeScannerCompat getInstanace(){
        if(instanace==null){
            if(IsUseLollipopSDK()){
                instanace=new LollipopBluetoothLeScannerImpl();
                LogUtil.i(TAG,"LollipopBluetoothLeScannerImpl created");
            }else{
                instanace=new JBBluetoothLeScannerImpl();
                LogUtil.i(TAG,"JBBluetoothLeScannerImpl created");
            }
        }
        return instanace;
    }

    public static boolean IsUseLollipopSDK(){
        boolean bRet=false;

        if(Build.VERSION.SDK_INT>=21){
//            BluetoothAdapter adapter=BluetoothAdapter.getDefaultAdapter();
//            if(adapter!=null){
//                if(adapter.getBluetoothLeScanner()!=null){
//                    bRet=true;
//                }
//            }else{
//                bRet=true;
//            }
            bRet=true;
        }
        return bRet;
    }
}
