package com.jq.btlib.protocal;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import com.jq.btlib.model.BtGattAttr;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.util.BytesUtil;
import com.jq.btlib.util.CharacteristicUtil;
import com.jq.btlib.util.LogUtil;
import com.jq.btlib.util.ThreadUtil;
import com.jq.btlib.util.syncTaskUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/6/30.
 */

public class lxStraightFrame implements iStraightFrame  {


    private static final String TAG = "CsBtUtil_v11";
    private syncTaskUtil mSyncTask=new syncTaskUtil();
    private BluetoothGatt mBtGatt;
    private CharacteristicUtil mTaskQueue;

    //设备是否已注册
    private boolean mblnHasReg=false;
    //设备是否已经登录
    private boolean mblnHasLogin=false;
    //设备已经初始化
    private boolean mblnInit=false;
    //超时时间
    private static final long LX_OVERTIME=3000;
    //缓存登陆验证码
    private byte[] mVerCode=new byte[6];

    private CsFatScale fatScale = null;

    @Override
    public enumProcessResult process(byte[] data, String uuid) throws FrameFormatIllegalException {
        enumProcessResult processResult=enumProcessResult.Wait_Scale_Data;

        if(uuid.equalsIgnoreCase(BtGattAttr.LX_UP_I_UUID) || uuid.equalsIgnoreCase(BtGattAttr.LX_UP_N_UUID)){
            //a620 指令数据  a621 分包数据
            if(data[0]==0x10){
                if(data[2]==0x00 && data[3]==0x02){
                    //注册设备应答
                    LogUtil.i(TAG,"收到注册设备应答");
                    writeBleData(new byte[]{0x00,0x01,0x01},2);
                    mblnHasReg=true;
                }else if (data[2]==0x00 && data[3]==0x07){
                    //登陆请求
                    LogUtil.i(TAG,"收到登陆请求");
                    writeBleData(new byte[]{0x00,0x01,0x01},2);
                    mblnHasReg=true;
                    mblnHasLogin=true;

                    mVerCode=BytesUtil.subBytes(data,4,6);
                }else if(data[2]==0x00 && data[3]==0x09){
                    LogUtil.i(TAG,"收到初始化请求");
                    writeBleData(new byte[]{0x00,0x01,0x01},2);
                    mblnHasReg=true;
                    mblnHasLogin=true;
                    mblnInit=true;
                }else if(data[2]==0x48 && data[3]==0x02){
                    LogUtil.i(TAG,"收到测量数据");
                    writeBleData(new byte[]{0x00,0x01,0x01},2);

                    int iIndex=4;
                    int iAfterPkgNumber=BytesUtil.bytesToInt(BytesUtil.subBytes(data,iIndex,2));
                    iIndex+=2;

                    byte[] bFlag=BytesUtil.subBytes(data,iIndex,4);
                    //标记位
                    byte[] bFlag0=BytesUtil.getBooleanArray(bFlag[3]);
                    byte[] bFlag1=BytesUtil.getBooleanArray(bFlag[2]);
                    byte[] bFlag2=BytesUtil.getBooleanArray(bFlag[1]);
                    byte[] bFlag3=BytesUtil.getBooleanArray(bFlag[0]);
                    iIndex+=4;

                    int iWeight=BytesUtil.bytesToInt(BytesUtil.subBytes(data,iIndex,2));
                    iIndex+=2;
                    fatScale = new CsFatScale();
                    fatScale.setHistoryData(true);
                    fatScale.setLockFlag((byte)1);
                    //单位为kg,小数位为0.01
                    fatScale.setWeight((iWeight / 10.0f));
                    fatScale.setScaleWeight(fatScale.getWeight() + "");
                    fatScale.setScaleProperty((byte)5);

                    if(bFlag0[2]==1){
                        //User Number
                        iIndex+=1;
                    }

                    if(bFlag0[3]==1){
                        //UTC
                        long scaleUTC=(long)BytesUtil.bytesToInt(BytesUtil.subBytes(data,iIndex,4))*1000;
                        Date dScale= new Date(scaleUTC);
                        fatScale.setWeighingDate(dScale);

                        iIndex+=1;
                    }
                    processResult=enumProcessResult.Received_Scale_Data;

                }
            }
        }else if(uuid.equalsIgnoreCase(BtGattAttr.LX_DOWN_ACK_UUID)) {
            //a625 设备端ACK
            mSyncTask.operationCompleted("");
        }

        return processResult;
    }

    @Override
    public CsFatScale getFatScale() {
        return fatScale;
    }

    //00--下行结束指令 01--下午分包  02--上行Ack
    private void writeBleData(byte[] data,int type){

        if (mBtGatt == null) {
            LogUtil.e(TAG, "mBtGatt error");
            return;
        }

        String sCharUUID = "";
        String sServiceUUID = BtGattAttr.LX_SERVICE_UUID;
        if (type==0) {
            sCharUUID = BtGattAttr.LX_DOWN_WI_UUID;
        }else if(type==1){
            sCharUUID = BtGattAttr.LX_DOWN_WO_UUID;
        }else{
            sCharUUID = BtGattAttr.LX_UP_ACK_UUID;
        }
        BluetoothGattService gattService = mBtGatt.getService(UUID.fromString(sServiceUUID));
        if (gattService != null) {
            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(UUID.fromString(sCharUUID));
            if (gattCharacteristic != null) {
                gattCharacteristic.setValue(data);
                gattCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                boolean bRet = mBtGatt.writeCharacteristic(gattCharacteristic);
                LogUtil.i(TAG, "writeCharacteristic ret:" + bRet + " type:" + type + " Data:" + BytesUtil.bytesToHexString(data));
            }
        }
    }

    private boolean sendDataWrap(byte[] data,int type){
        boolean bRet=false;
        for(int i=0;i<3;i++){
            writeBleData(data, 0);
            LogUtil.i(TAG,"第" + (i+1) + "次发送");
            if (mSyncTask.startOperation(1)) {
                bRet=true;
                break;
            }
        }
        return bRet;
    }


    public void Init(final String sDeviceMac,final byte[] brocastData,BluetoothGatt gatt,CharacteristicUtil queue) {
        mblnHasReg = false;
        mblnHasLogin = false;
        mblnInit = false;
        mBtGatt=gatt;
        mTaskQueue=queue;

        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {

                long start = System.currentTimeMillis();
                while (mTaskQueue.size()!=0){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if ((System.currentTimeMillis() - start) > LX_OVERTIME) {
                        LogUtil.e(TAG, "等待通道使能超时,退出");
                        return;
                    }
                }

                byte[] data = syncLXInstruction.Register(sDeviceMac);
                if (brocastData != null && brocastData.length > 5 && brocastData[4] == 0x00) {
                    //设备未注册
                    LogUtil.i(TAG, "发送注册指令...");
                    if (!sendDataWrap(data,0)) {
                        LogUtil.e(TAG, "发送注册超时,退出");
                        return;
                    }

                    start = System.currentTimeMillis();
                    while (!mblnHasReg) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if ((System.currentTimeMillis() - start) > LX_OVERTIME) {
                            LogUtil.e(TAG, "等待注册应答超时,退出");
                            return;
                        }
                    }
                }

                start = System.currentTimeMillis();
                while (!mblnHasLogin) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if ((System.currentTimeMillis() - start) > LX_OVERTIME) {
                        LogUtil.e(TAG, "等待设备登陆超时,退出");
                        return;
                    }
                }

                data = syncLXInstruction.LoginReply(mVerCode);
                LogUtil.i(TAG, "发送登陆回复...");
                if (!sendDataWrap(data,0)) {
                    LogUtil.e(TAG, "登陆回复超时,退出");
                    return;
                }

                start = System.currentTimeMillis();
                while (mblnInit) {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if ((System.currentTimeMillis() - start) > LX_OVERTIME) {
                        LogUtil.e(TAG, "等待设备初始化超时,退出");
                        return;
                    }
                }
                data = syncLXInstruction.InitReply();
                LogUtil.i(TAG, "发送初始化回复...");
                if (!sendDataWrap(data,0)) {
                    LogUtil.e(TAG, "初始化回复超时,退出");
                    return;
                }

                data = syncLXInstruction.GetMeasureData();
                LogUtil.i(TAG, "发送测量数据通知...");
                if (!sendDataWrap(data,0)) {
                    LogUtil.e(TAG, "测量数据通知回复超时,退出");
                    return;
                }

            }
        });
    }
}
