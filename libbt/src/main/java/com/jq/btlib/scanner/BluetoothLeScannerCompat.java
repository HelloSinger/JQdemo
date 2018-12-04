package com.jq.btlib.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import com.jq.btlib.protocal.chipseaBroadcastFrame;
import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.util.ConfigurableDeviceUtil;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.btlib.util.LogUtil;

/**
 * Created by lixun on 2016/3/11.
 */
public abstract class BluetoothLeScannerCompat {
    protected static final String TAG = "CsBtUtil_v11";
    private boolean mScanning=false;
    private final Handler mHandler;
    private int mRefreshInterval=1000;

    private CsBtUtil_v11.OnBluetoothListener bluetoothListener = null;
    public void setBluetoothListener(CsBtUtil_v11.OnBluetoothListener listener){
        bluetoothListener=listener;
    }

    private CsBtUtil_v11.SeachDeviceCallback searchCallBack=null;
    private String mBoundMac="";

    protected BluetoothLeScannerCompat(){
        mHandler=new Handler();
    }


    public void starSeachBindDevice(String sMac,CsBtUtil_v11.SeachDeviceCallback callback){
        if(mScanning) {
            LogUtil.w(TAG,"Scan thread is running..");
            return;
        }

        mScanning=true;
        searchCallBack=callback;
        mBoundMac=sMac;

        if(ConfigurableDeviceUtil.IsContinuousScann(this instanceof LollipopBluetoothLeScannerImpl)){
            mHandler.post(mScanRunnable);
        }
        else {
            startLeScanInternal();
        }
    }

    public void stopSeachBindDevice(){
        mScanning=false;
        searchCallBack=null;
        mBoundMac="";
        mHandler.removeCallbacks(mScanRunnable);
        stopLeScanInternal();
    }

    /**
     * @Description 启动BLE扫描,默认扫描所有BLE设备
     */
    public void startSearching(){
        if(mScanning) {
            LogUtil.w(TAG,"Scan thread is running..");
            return;
        }
        mScanning=true;
        searchCallBack=null;
        if(ConfigurableDeviceUtil.IsContinuousScann(this instanceof LollipopBluetoothLeScannerImpl)) {
            mHandler.post(mScanRunnable);
        }
        else {
            startLeScanInternal();
        }
    }


    /**
     * @Description 停止搜索
     */
    public void stopSearching(){
        mScanning=false;
        mHandler.removeCallbacks(mScanRunnable);
        stopLeScanInternal();
    }

    private Runnable mScanRunnable=new Runnable() {
        @Override
        public void run() {
            try {
                if (isBluetoothEnable()) {
                    stopLeScanInternal();
                } else {
                    return;
                }
                if (isBluetoothEnable()) {
                    startLeScanInternal();
                } else {
                    return;
                }

                mHandler.postDelayed(this, mRefreshInterval);
            }catch (Exception ex){
                LogUtil.e(TAG,ex.getMessage());
            }
        }
    };

    protected abstract boolean startLeScanInternal();


    protected abstract void stopLeScanInternal();


    protected boolean isBluetoothEnable() {
        if (BluetoothAdapter.getDefaultAdapter() != null) {
            int bleState=BluetoothAdapter.getDefaultAdapter().getState();
            return (BluetoothAdapter.getDefaultAdapter().isEnabled() && bleState==BluetoothAdapter.STATE_ON);
        }
        return false;
    }


    private byte[] getManufacturerData(byte[] data){
        byte[] bRet=null;
        try {
            int iStartIndex = 0;
            while (iStartIndex < data.length) {
                int iCmdLen = data[iStartIndex]; //命令长度
                iStartIndex += 1;
                byte[] bCmd = BytesUtil.subBytes(data, iStartIndex, iCmdLen);
                iStartIndex += iCmdLen;

                if (bCmd != null) {
                    byte iCmd = bCmd[0]; //命令字
                    switch (iCmd) {
                        case (byte) 0xff: //用户自定义数据
                            bRet= new byte[bCmd.length - 1];
                            bRet = BytesUtil.subBytes(bCmd, 1, bRet.length);

                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return bRet;
    }

    private ScanMsg getProtocalTypeByBroadcast(byte[] data){
        ScanMsg retScan=new ScanMsg();
        retScan.protocalType=CsBtUtil_v11.Protocal_Type.UNKNOWN;
        try {
            boolean bFound = false;
            int iStartIndex = 0;
            while (iStartIndex < data.length) {
                int iCmdLen = data[iStartIndex]; //命令长度
                iStartIndex += 1;
                byte[] bCmd = BytesUtil.subBytes(data, iStartIndex, iCmdLen);
                iStartIndex += iCmdLen;

                if (bCmd != null) {
                    byte iCmd = bCmd[0]; //命令字
                    switch (iCmd) {
                        case 0x01: //蓝牙标识
                            break;
                        case 0x02:
                            for (int i = 1; i < bCmd.length; i++) {
                                if ((i + 1) <= bCmd.length) {
                                    if (bCmd[i] == 0x02 && bCmd[i + 1] == (byte) 0xa6) {
                                        retScan.protocalType = CsBtUtil_v11.Protocal_Type.LEXIN;
                                        bFound = true;
                                    }
                                }
                            }
                            break;
                        case 0x03: //UUID
                            for (int i = 1; i < bCmd.length; i++) {
                                if ((i + 1) <= bCmd.length) {
                                    if (bCmd[i] == 0x18 && bCmd[i + 1] == (byte) 0xd6) {
                                        retScan.protocalType = CsBtUtil_v11.Protocal_Type.JD;
                                        bFound = true;
                                    }
                                }
                            }
                            break;
                        case (byte) 0xff: //用户自定义数据
                            retScan.manufacturerData = new byte[bCmd.length - 1];
                            retScan.manufacturerData = BytesUtil.subBytes(bCmd, 1, retScan.manufacturerData.length);
                            if (bCmd[1] == (byte) 0xCA) {
                                retScan.protocalType = CsBtUtil_v11.Protocal_Type.OKOK;
                                bFound = true; //直接退出
                                break;
                            }else if(bCmd[1]==(byte)0xA8 && bCmd[2]==(byte)0x01){
                                retScan.protocalType=CsBtUtil_v11.Protocal_Type.ALIBABA;
                                bFound = true; //直接退出
                                break;
                            }else if(bCmd[1]==(byte)0xFF && bCmd[2]==(byte)0xF0){
                                if(bCmd[3]==0x03){
                                    retScan.protocalType = CsBtUtil_v11.Protocal_Type.OKOKCloudV3;
                                    bFound = true; //直接退出
                                }else {
                                    retScan.protocalType = CsBtUtil_v11.Protocal_Type.OKOKCloud;
                                    bFound = true; //直接退出
                                }
                                break;
                            }
                    }
                    if (bFound) {break;}
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return retScan;
    }
    public String HToB(String a) {
        String b = Integer.toBinaryString(Integer.valueOf(toD(a, 16)));
        return b;
    }

    String toD(String a, int b) {
        int r = 0;
        for (int i = 0; i < a.length(); i++) {
            r = (int) (r + formatting(a.substring(i, i + 1))* Math.pow(b, a.length() - i - 1));
        }
        return String.valueOf(r);
    }
    public int formatting(String a) {
        int i = 0;
        for (int u = 0; u < 10; u++) {
            if (a.equals(String.valueOf(u))) {i = u;
            }
        }
        if (a.equals("a")) {
            i = 10;
        }
        if (a.equals("b")) {i = 11;}
        if (a.equals("c")) {i = 12;}
        if (a.equals("d")) {i = 13;}
        if (a.equals("e")) {i = 14;}
        if (a.equals("f")) {i = 15;}
        return i;
    }

    protected void handleBroadcastInfo(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
        LogUtil.i(TAG, "current device MAC:" + device.getAddress() + " Name:" + device.getName());
        StringBuilder sb = new StringBuilder();
        String rules = null;
        for (int i = 0; i < scanRecord.length; i++) {
            sb.append(String.format("%02x", scanRecord[i]));
            if(i == 9){
                String se= HToB(String.format("%02x", scanRecord[i]));
                rules = String.format("%08d",Long.valueOf(se));
            }
        }

        String substring = sb.substring(10, 12);
        String substring1 = sb.substring(18, 20);

        //广播秤试用
        String substring_1 = sb.substring(22, 24);
        String substring_2 = sb.substring(30, 32);

        String substringss = sb.substring(20, 24);
        String wieght = Integer.valueOf(substringss,16).toString();

        ScanMsg scanMsg=new ScanMsg();
        if(substring.equals("cb") && substring1.equals("02")){
            LogUtil.v("===macss", "current " + sb.toString());
            chipseaBroadcastFrame frame= null;
                frame = new chipseaBroadcastFrame(device.getAddress(),rules+"x"+wieght);
            frame.name = device.getName();
            frame.procotalType="sd";
            if (bluetoothListener != null) {
                bluetoothListener.broadcastChipseaData(frame);
            }
        }
        else if(substring_1.equals("cb") && substring_2.equals("02")){//广播秤试用
            String rules1 = null;
            for (int i = 0; i < scanRecord.length; i++) {
                sb.append(String.format("%02x", scanRecord[i]));
                if(i == 14){
                    String se= HToB(String.format("%02x", scanRecord[i]));
                     rules1 = String.format("%08d",Long.valueOf(se));
                }
            }
            String substringss1 = sb.substring(32, 36);
            String wieght1 = Integer.valueOf(substringss1,16).toString();
            chipseaBroadcastFrame frame= null;
            frame = new chipseaBroadcastFrame(device.getAddress(),rules1+"x"+wieght1);
            frame.name = device.getName();
            frame.procotalType="sd";
            if (bluetoothListener != null) {
                bluetoothListener.broadcastChipseaData(frame);
            }
        }
        else {
            if (searchCallBack != null && mBoundMac != null) {
                if (device.getAddress().equalsIgnoreCase(mBoundMac)) {
                    searchCallBack.success(getManufacturerData(scanRecord));
                    return;
                }
            } else {
                scanMsg = getProtocalTypeByBroadcast(scanRecord);
            }

            if (scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.OKOK || scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.OKOKCloud || scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.OKOKCloudV3) {
                chipseaBroadcastFrame frame;
                try {
                    frame = new chipseaBroadcastFrame(scanMsg.manufacturerData, device.getAddress());
                    frame.name = device.getName();
                    frame.procotalType = scanMsg.protocalType.toString();
                    if (bluetoothListener != null) {
                        bluetoothListener.broadcastChipseaData(frame);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.JD || scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.ALIBABA || scanMsg.protocalType == CsBtUtil_v11.Protocal_Type.LEXIN) {
                chipseaBroadcastFrame frame = new chipseaBroadcastFrame(device.getAddress());
                frame.name = device.getName();
                frame.procotalType = scanMsg.protocalType.toString();
                if (bluetoothListener != null) {
                    bluetoothListener.broadcastChipseaData(frame);
                }
            }
        }
    }

    private class ScanMsg{
        public CsBtUtil_v11.Protocal_Type protocalType;
        public byte[] manufacturerData;
    }

}
