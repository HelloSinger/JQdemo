package com.jq.btlib.util;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import com.jq.btlib.model.BtGattAttr;
import com.jq.btlib.model.TaskData;
import com.jq.btlib.model.device.CsFatConfirm;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.model.exception.FrameFormatIllegalException;
import com.jq.btlib.protocal.chipseaBroadcastFrame;
import com.jq.btlib.protocal.chipseaStraightFrame;
import com.jq.btlib.protocal.enumProcessResult;
import com.jq.btlib.protocal.iStraightFrame;
import com.jq.btlib.protocal.lxStraightFrame;
import com.jq.btlib.protocal.straightFrameFactory;

/**
 * @author lixun
 * @version 1.1
 * @ClassName CsBtUtil_v11
 * @Description OKOK 蓝牙android SDK库 解决蓝牙连接慢 增加获取历史记录 增加JD微联协议解析
 * @Date 2015年8月24日
 */

public class CsBtUtil_v11 {

    /**
     * 标识
     **/
    private static final String TAG = "==CsBtUtil_v11";
    public static final String SP_NAME = "chipsea_btLib";
    public static final String SP_DEVICE_PREFIX = "device:";

    public enum Protocal_Type {
        OKOK, JD, ALIBABA, OKOKCloud,OKOKCloudV3,LEXIN, UNKNOWN
    }

    public enum Down_Instruction_Type {
        Sync_UserInfo
    }

    public enum Weight_Unit {
        KG, JIN, LB, ST,
    }

    public enum Weight_Digit {
        ZERO, ONE, TWO,
    }

    public enum Synchronization_Task_Key {
        Time, CSDownData,BodyMeasurement, BodyHistory, CSNotify, LXUPI,LXUPN,LXDOWNN,Unknown,
    }


    public final static int STATE_BLE_CLOSE = 0; // （硬件）蓝牙关闭
    public final static int STATE_BLE_OPEN = 1; // （硬件）蓝牙打开
    public final static int STATE_CLOSE = 2; // 关闭
    public final static int STATE_BROADCAST = 3; // 接受广播中
    public final static int STATE_CONNECTING = 4;// 正在连接
    public final static int STATE_CONNECTED = 5; // 建立连接
    public final static int STATE_WAIT_CONNECT = 6; //等待连接
    public final static int STATE_CONNECTED_CALCULATTING = 7; //计算中
    public final static int STATE_CONNECTED_WAITSCALE = 8; //等待称重


    private BluetoothAdapter mBtAdapter = null; // 蓝牙的适配器
    private BluetoothDevice mBtDevice = null;
    private BluetoothGatt mBtGatt; // 本手机蓝牙作为中央来使用和处理数据
    private BluetoothGattCharacteristic mWriteCharacteristic; // 用来保存写入到设备数据！
    private BluetoothGattCharacteristic mNotifyCharacteristic; // 用来通知，有数据改变~！
    private Context context;
    private static OnBluetoothListener bluetoothListener = null;

    // 自动重联请求时钟
    private final int mRefreshInterval = 1500;
    private Handler mTimerhandler;
    private int mCurState = STATE_CLOSE;
    private String mBindedDeviceMac = "";
    private Protocal_Type mCurProtocalType = Protocal_Type.OKOK;
    private boolean mIsForceClose = false;

    private AsynBLETaskCallback mTaskCallback = null;
    private CharacteristicUtil mCharacteristicUtil = null;

    private byte[] mManufacturerData; //连接设备时的自定义广播数据


    //连接模工 FSAC--先扫描再连接 Alway_Conn一直连接
    public enum CONNECT_MODE {
        FSAC, Alway_Conn
    }

    /**
     * @Description android GATT连接成功后回调函数 [1. 数据改变回调函数 2. 数据连接状态的回调函数 3.
     * 设备发现回调函数 4.读设备函数回调函数 5. 写设备函数回调函数 6. 读取信号回调函数]
     */
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            LogUtil.i(TAG, " onConnectionStateChange status:" + status + " newState:" + newState);
            if (status == 133) {
                mCurState = STATE_CLOSE;
                gatt.close();
                if (bluetoothListener != null) {
                    bluetoothListener.bluetoothStateChange(mCurState);
                }
            } else {
                if (newState == BluetoothProfile.STATE_CONNECTED && status == BluetoothGatt.GATT_SUCCESS) { // 连接状态
                    LogUtil.i(TAG, "Connected to GATT server. ");
                    mCurState = STATE_CONNECTING;
                    mTimerhandler.removeCallbacks(runnableReConnect);
                    // Attempts to discover services after successful connection.
                    boolean result = mBtGatt.discoverServices(); // 尝试去发现设备
                    LogUtil.i(TAG, "Attempting to start service discovery:"
                            + result);
                    if (bluetoothListener != null) {
                        bluetoothListener.bluetoothStateChange(mCurState);
                    }

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    LogUtil.i(TAG, "Disconnected from GATT server.");
                    gatt.close();
                    mCurState = STATE_CLOSE;
                    if (bluetoothListener != null) {
                        bluetoothListener.bluetoothStateChange(mCurState);
                    }
                }
            }


        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            LogUtil.i(TAG, "onServicesDiscovered function");

            if (status == BluetoothGatt.GATT_SUCCESS) { // 连接成功
                LogUtil.i(TAG, "connetting device sucess");
                List<BluetoothGattService> mbtgatt = mBtGatt.getServices();
                // 必须在设备被找到后才可以使用getServices！
                displayGattServicesEx(mbtgatt);
            } else {
                LogUtil.i(TAG, "onServicesDiscovered received: " + status);
                mCurState = STATE_CLOSE;
                gatt.disconnect();
                if (bluetoothListener != null) {
                    bluetoothListener.bluetoothStateChange(mCurState);
                }
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            LogUtil.i(TAG, " onCharacteristicRead status:" + status + " descriptor:" + characteristic.getUuid().toString());
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            LogUtil.i(TAG, " onCharacteristicWrite status:" + status + " descriptor:" + characteristic.getUuid().toString());

            if (mTaskCallback == null) {
                return;
            }
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mTaskCallback.success("");
            } else {
                mTaskCallback.failed();
            }
        }

        // 连接之后,对数据格式的判断和解析
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            handleConnectedInfo(characteristic);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.i(TAG, "onReadRemoteRssi ");
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.i(TAG, " onDescriptorWrite status:" + status + " descriptor:" + descriptor.getCharacteristic().getUuid().toString());

            if (mTaskCallback == null) {
                return;
            }
            if (status == BluetoothGatt.GATT_SUCCESS) {
                mTaskCallback.success("");
            } else {
                mTaskCallback.failed();
            }
        }

    };

    /**
     * 析构函数
     */
    public CsBtUtil_v11() {
        this.mBtAdapter = BluetoothAdapter.getDefaultAdapter(); // 得到默认的蓝牙适配器
        this.mCharacteristicUtil = new CharacteristicUtil(this);
    }

    public int getCurBluetoothState() {
        return mCurState;
    }

    /**
     * 注销广播
     */
    public void registerReceiver(Context context) {
        mIsForceClose = false;
        this.context = context;

        if (VerifyUtil.compareVersion(android.os.Build.VERSION.RELEASE, "4.4") < 0) {
            if (context != null)
                mTimerhandler = new Handler(context.getMainLooper());
            LogUtil.i(TAG, "use UI thread to connect: " + android.os.Build.VERSION.RELEASE);
        } else {
            mTimerhandler = new Handler();
        }
        context.registerReceiver(blueStateBroadcastReceiver, new IntentFilter(
                BluetoothAdapter.ACTION_STATE_CHANGED));


    }

    /**
     * @Description 强制关闭GATT, 不会触发STATE_CLOSE事件, 用于蓝牙被手动关闭后的处理
     */
    public void disconnectGATT() {
        if (mBtGatt != null) {
            mBtGatt.disconnect();
            LogUtil.i(TAG, "disconnectGATT");
        }
        mCurState = STATE_CLOSE;
    }

    /**
     * 注销广播
     */
    public void unregisterReceiver(Context context) {
        try {
            context.unregisterReceiver(blueStateBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Receiver not registered")) {
                // Ignore this exception. This is exactly what is desired
            } else {
                // unexpected, re-throw
                throw e;
            }
        }
    }

    /**
     * @Description android 蓝牙事件接受函数，用于接受手机蓝牙手动开启关闭事件
     */
    BroadcastReceiver blueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i(TAG, "blueState: STATE_OFF");
                    if (bluetoothListener != null) {
                        bluetoothListener.BluetoothTurnOff();
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i(TAG, "blueState: STATE_TURNING_ON");
                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i(TAG, "blueState: STATE_ON");
                    if (bluetoothListener != null) {
                        bluetoothListener.BluetoothTurnOn();
                    }
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i(TAG, "blueState: STATE_TURNING_OFF");
                    if (bluetoothListener != null) {
                        bluetoothListener.BluetoothTurningOff();
                    }
                    break;
                default:
                    break;
            }
        }
    };


    private boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh");
            if (localMethod != null) {
                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt)).booleanValue();
                return bool;
            }
        } catch (Exception localException) {
            Log.e(TAG, "An exception occured while refreshing device");
        }
        return false;
    }

    private boolean connectGATT() {

        boolean bAdapterNull = (mBtAdapter == null);
        boolean bDeviceNull = (mBtDevice == null);
        boolean bBtGATTNull = (mBtGatt == null);

        Log.i(TAG, "[connectGATT] bAdapterNull:" + bAdapterNull
                + " bDeviceNull:" + bDeviceNull + " bBtGATTNull:" + bBtGATTNull
                + " DeviceMac:" + mBindedDeviceMac);

        if (bAdapterNull || mBindedDeviceMac == null) {
            LogUtil.e(TAG, "connectGATT param error");
            return false;
        }

        if (!isBluetoothEnable()) {
            LogUtil.e(TAG, "ble closed");
            return false;
        }

        if (bDeviceNull) {
            mBtDevice = mBtAdapter.getRemoteDevice(mBindedDeviceMac);
        } else {
            if (!mBtDevice.getAddress().equalsIgnoreCase(mBindedDeviceMac)) {
                mBtDevice = mBtAdapter.getRemoteDevice(mBindedDeviceMac);
                LogUtil.w(TAG, "Bluetooth changed, re-get device.");
            }
        }

        if (mBtDevice == null) {
            LogUtil.e(TAG, "Device not found. Unable to connect.");
            return false;
        }

        if (!bBtGATTNull) {
            //mBtGatt.disconnect();
            mBtGatt.close();
            LogUtil.e(TAG, "GATT has been closed manually");
            mBtGatt = null;
        }
        mBtGatt = mBtDevice.connectGatt(context, false, mGattCallback);
        //refreshDeviceCache(mBtGatt); //强制刷新蓝牙cache

        this.mCurState = STATE_WAIT_CONNECT;
        if (bluetoothListener != null) {
            bluetoothListener.bluetoothStateChange(mCurState);
        }
        if (mBtGatt != null) {
            LogUtil.i(TAG, "got BluetoothGatt!");
        } else {
            return false;
        }
        return true;

    }

    /**
     * @return 若是返回true, 否则返回false
     * @Description 判断当前蓝牙是否处于蓝牙已连接状态
     */
    public boolean isConnected() {
        boolean bRet = false;
        if (mCurState == STATE_CONNECTED || mCurState == STATE_CONNECTING
                || mCurState == STATE_CONNECTED_CALCULATTING || mCurState == STATE_CONNECTED_WAITSCALE) {
            LogUtil.i(TAG, "isConnected:" + mCurState);
            bRet = true;
        }
        return bRet;
    }

    Runnable runnableReConnect = new Runnable() {
        @Override
        public void run() {
            if (!isConnected()) {
                connectGATT();
                mTimerhandler.postDelayed(this, mRefreshInterval);
            }
        }
    };


    /**
     * @param address 绑定的蓝牙mac地址
     * @param pType   蓝牙秤的协议类型 (OKOK/JD/阿里小智/小米)
     * @Description 自动重连接, 当蓝牙连接断开后，可调用此函数自动重联
     * http://blog.csdn.net/huningjun/article/details/46340295
     */
    public void autoConnect(final String address, final Protocal_Type pType) {
        if (mBtAdapter == null || address == null) {
            LogUtil.e(TAG, "autoConnect param error");
            return;
        }

        if (mIsForceClose) {
            LogUtil.e(TAG, "Force closed");
            return;
        }

        if (pType == Protocal_Type.UNKNOWN) {
            LogUtil.e(TAG, "Unkown protocal, stop auto connect");
            return;
        }

        if (!isBluetoothEnable()) {
            LogUtil.e(TAG, "Bluetooth is closed, stop auto connect");
            return;
        }

        if (isConnected()) {
            LogUtil.e(TAG, "Current state is connected, stop auto connect");
            return;
        }

        mBindedDeviceMac = address;
        mCurProtocalType = pType;

        if (mCharacteristicUtil != null) {
            mCharacteristicUtil.clearTask();
        }
        if (ConfigurableDeviceUtil.getConnectMode() == CONNECT_MODE.Alway_Conn) {
            LogUtil.i(TAG, "(Alway_Conn)Device address:" + mBindedDeviceMac + " Type:" + mCurProtocalType.toString());
            mTimerhandler.removeCallbacks(runnableReConnect);
            mTimerhandler.postDelayed(runnableReConnect, mRefreshInterval);
        } else {
            LogUtil.i(TAG, "(FSAC)Device address:" + mBindedDeviceMac + " Type:" + mCurProtocalType.toString());
            BluetoothScanService.getInstanace().starSeachBindDevice(mBindedDeviceMac, new SeachDeviceCallback() {
                @Override
                public void success(byte[] manufacturerData) {
                    BluetoothScanService.getInstanace().stopSeachBindDevice();
                    mManufacturerData=manufacturerData;
                    connectGATT();
                }
            });
        }

    }

    /**
     * @return 若是返回true, 否则返回false
     * @Description 停止自动重连接
     */
    public void stopAutoConnect() {
        if (mTimerhandler != null) {
            mTimerhandler.removeCallbacks(runnableReConnect);
        }
        if (ConfigurableDeviceUtil.getConnectMode() == CONNECT_MODE.FSAC) {
            BluetoothScanService.getInstanace().stopSeachBindDevice();
        }
    }

    /**
     * @param
     * @Description 设置蓝牙库回调函数
     */
    public void setBluetoothListener(OnBluetoothListener listener) {
        CsBtUtil_v11.bluetoothListener = listener;
        BluetoothScanService.getInstanace().setBluetoothListener(listener);
    }

    /**
     * 打开蓝牙 用户可操作提示框，需要 Activity 的对象
     *
     * @param activity
     */
    public void openBluetooth(Activity activity, int requestCode) {
        if (!mBtAdapter.isEnabled()) {
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enabler, requestCode);
        }
    }

    /**
     * @param isSilent false-为通知的方式打开蓝牙 ture-为静默打开蓝牙
     * @Description 打开蓝牙
     */
    public void openBluetooth(boolean isSilent) {
        if (!mBtAdapter.isEnabled()) {
            // 方法一打开蓝牙 -- 打开的时候会有提示是否打开蓝牙
            if (!isSilent) {
                Intent enabler = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                context.startActivity(enabler);
            } else {
                mBtAdapter.enable();
            }
        }
    }

    /**
     * @Description 关闭蓝牙
     */
    public void closeBluetooth() {
        if (mBtAdapter != null) {
            mBtAdapter.disable();
        }
    }

    /**
     * @Description 判断蓝牙是否已经使能
     */
    public boolean isBluetoothEnable() {
        if (mBtAdapter != null) {
            int bleState = mBtAdapter.getState();
            return (mBtAdapter.isEnabled() && bleState == BluetoothAdapter.STATE_ON);
        }
        return false;
    }


    /**
     * @Description 启动BLE扫描, 默认扫描所有BLE设备
     */
    public boolean startSearching() {
        BluetoothScanService.getInstanace().startSearching();
        return true;
    }

    /**
     * @Description 停止搜索
     */
    public void stopSearching() {

        BluetoothScanService.getInstanace().stopSearching();
    }


    /**
     * @param isUnbind 是否是解绑操作
     * @Description 强制关闭GATT, 不会触发STATE_CLOSE事件
     */
    public void forceClose(boolean isUnbind) {
        mIsForceClose = true;

//		if (mBtAdapter == null) return;

        if (mBtGatt != null) {
            mBtGatt.disconnect();
            //mBtGatt.close();
            mBtGatt = null;
        }
        mCurState = STATE_CLOSE;

        if (isUnbind) {
            if (mBtDevice != null) {
                mBtDevice = null;
                LogUtil.i(TAG, "forceClose set Bluetooth device to null");
            }
        }
    }


    /**
     * @Description 关闭蓝牙GATT通道并释放资源
     */
    public void closeBluetoothGatt() {
        if (mBtGatt == null) {
            return;
        }
        mBtGatt.close();
        mBtGatt = null;
        mCurState = STATE_CLOSE;
        if (bluetoothListener != null) {
            bluetoothListener.bluetoothStateChange(mCurState);
        }
    }

    /**
     * 写函数，用来把信息写入到远程设备中
     *
     * @param value 指令内容
     * @Description 通过蓝牙GATT 写通道向BLE设备下传指令
     */
    public void writeCharacteristic(byte[] value, Down_Instruction_Type instructionType) {
        writeCharacteristic(mWriteCharacteristic, value, instructionType);
    }

    public void writeDescriptor(Synchronization_Task_Key key, AsynBLETaskCallback callback) {
        String sCharUUID = "";
        String sServiceUUID = BtGattAttr.BODY_COMPOSITION;
        boolean bIndicate = true;
        if (key == Synchronization_Task_Key.BodyMeasurement) {
            sCharUUID = BtGattAttr.BODY_COMPOSITION_MEASUREMENT;
        } else if (key == Synchronization_Task_Key.BodyHistory) {
            sCharUUID = BtGattAttr.BODY_COMPOSITION_HISTORY;
        } else if (key == Synchronization_Task_Key.CSNotify) {
            bIndicate = false;
            sServiceUUID = BtGattAttr.CHIPSEA_SERVICE_UUID;
            sCharUUID = BtGattAttr.CHIPSEA_CHAR_RX_UUID;
        }else if (key==Synchronization_Task_Key.LXUPI){
            bIndicate = true;
            sServiceUUID = BtGattAttr.LX_SERVICE_UUID;
            sCharUUID = BtGattAttr.LX_UP_I_UUID;
        }else if (key==Synchronization_Task_Key.LXUPN){
            bIndicate = false;
            sServiceUUID = BtGattAttr.LX_SERVICE_UUID;
            sCharUUID = BtGattAttr.LX_UP_N_UUID;
        }else if (key==Synchronization_Task_Key.LXDOWNN){
            bIndicate = false;
            sServiceUUID = BtGattAttr.LX_SERVICE_UUID;
            sCharUUID = BtGattAttr.LX_DOWN_ACK_UUID;
        }

        boolean bRet = false;
        BluetoothGattService gattService = mBtGatt.getService(UUID.fromString(sServiceUUID));
        if (gattService != null) {
            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(UUID.fromString(sCharUUID));
            if (gattCharacteristic != null) {
                mTaskCallback = callback;
                bRet = enableCharacteristicNotification(gattCharacteristic, bIndicate);
            }
        }
        if (!bRet) {
            if (mTaskCallback != null) {
                mTaskCallback.failed();
            }
        }
    }

    public void writeCharacteristic(final TaskData data, AsynBLETaskCallback callback) {

        String sCharUUID = "";
        String sServiceUUID = BtGattAttr.CURRENT_TIME_SERVICE;
        if (data.Key == Synchronization_Task_Key.Time) {
            sCharUUID = BtGattAttr.CURRENT_TIME;
        }else if(data.Key==Synchronization_Task_Key.CSDownData){
            sServiceUUID=BtGattAttr.ISSC_SERVICE_UUID;
            sCharUUID=BtGattAttr.ISSC_CHAR_TX_UUID;
        }

        boolean bRet = false;
        BluetoothGattService gattService = mBtGatt.getService(UUID.fromString(sServiceUUID));
        if (gattService != null) {
            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(UUID.fromString(sCharUUID));
            if (gattCharacteristic != null) {
                mTaskCallback = callback;
                bRet = writeCharacteristic(gattCharacteristic, data.Buffer, Down_Instruction_Type.Sync_UserInfo);
                LogUtil.i(TAG, "writeCharacteristic:" + data.Key + " succ:" + bRet);
            }
        }
        if (!bRet) {
            if (mTaskCallback != null) {
                mTaskCallback.failed();
            }
        }
    }


    /**
     * @param characteristic 写通道characteristic
     * @param value          指令内容
     * @Description 通过蓝牙GATT 写通道向BLE设备下传指令
     */
    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] value, Down_Instruction_Type instructionType) {
        if (mBtGatt == null) {
            LogUtil.e(TAG, "mBtGatt error");
            return false;
        }
        if (mBtAdapter == null) {
            LogUtil.e(TAG, "mBtAdapter error");
            return false;
        }
        if (characteristic == null) {
            LogUtil.e(TAG, "WriteCharacteristic error");
            return false;
        }

        characteristic.setValue(value);
        characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        boolean bRet = mBtGatt.writeCharacteristic(characteristic);
        LogUtil.i(TAG, "writeCharacteristic --> " + BytesUtil.bytesToHexString(value));
        return bRet;
    }

    /**
     * @param isRead 是否是读通道
     * @return UUID
     * @Description 根据当前的协议类型及读写通道标志获取characteristic的UUID
     */
    private String getCharacteristicUUID(boolean isRead) {
        String sUUID = "";
        switch (mCurProtocalType) {
            case OKOK:
                if (isRead) {
                    sUUID = BtGattAttr.CHIPSEA_CHAR_RX_UUID;
                } else {
                    sUUID = BtGattAttr.CHIPSEA_CHAR_TX_UUID;
                }
                break;
            case JD:
                if (isRead) {
                    sUUID = BtGattAttr.JD_CHAR_RX_UUID;
                } else {
                    sUUID = BtGattAttr.JD_CHAR_TX_UUID;
                }
                break;
        }
        return sUUID;
    }

    /**
     * @return UUID
     * @Description 根据当前的协议类型及读写通道标志获取service的UUID
     */
    private String getServiceUUID() {
        String sUUID = "";
        switch (mCurProtocalType) {
            case OKOK:
                sUUID = BtGattAttr.CHIPSEA_SERVICE_UUID;
                break;
            case JD:
                sUUID = BtGattAttr.JD_SERVICE_UUID;
                break;
        }
        return sUUID;
    }

    private void displayGattServicesEx(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;

        if (mCurProtocalType == Protocal_Type.ALIBABA) {
            //同步使能体脂服务
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.BodyMeasurement, true));
            mCharacteristicUtil.doTask();

            mCurState = STATE_CONNECTED;
            if (bluetoothListener != null) {
                bluetoothListener.bluetoothStateChange(mCurState);
                bluetoothListener.bluetoothWriteChannelDone(null);
            }
        } else if (mCurProtocalType == Protocal_Type.OKOKCloud || mCurProtocalType == Protocal_Type.OKOKCloudV3) {
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.CSNotify, true));
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.BodyMeasurement, true));
            mCharacteristicUtil.doTask();

            mCurState = STATE_CONNECTED;
            if (bluetoothListener != null) {
                bluetoothListener.bluetoothStateChange(mCurState);
                bluetoothListener.bluetoothWriteChannelDone(null);
            }
        }else if(mCurProtocalType == Protocal_Type.LEXIN){
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.LXUPI, true));
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.LXUPN, true));
            mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.LXDOWNN, true));
            mCharacteristicUtil.doTask();

            mCurState = STATE_CONNECTED;
            if (bluetoothListener != null) {
                bluetoothListener.bluetoothStateChange(mCurState);
                bluetoothListener.bluetoothWriteChannelDone(null);
            }
        } else {
            String sUUID = getServiceUUID();
            for (BluetoothGattService gattService : gattServices) {
                if (gattService.getUuid().toString().compareToIgnoreCase(sUUID) == 0) {
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();

                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        final int charaProp = gattCharacteristic.getProperties();
                        String sCharacteristicUUId = gattCharacteristic.getUuid().toString();

                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            if (sCharacteristicUUId.compareToIgnoreCase(getCharacteristicUUID(true)) == 0) {
                                mNotifyCharacteristic = gattCharacteristic;
                                setCharacteristicNotification(mNotifyCharacteristic, true);
                                LogUtil.i(TAG, "-------- got NOTIFY characteristic --------");
                            }
                        }

                        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE) > 0 || (charaProp | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                            if (sCharacteristicUUId.compareToIgnoreCase(getCharacteristicUUID(false)) == 0) {
                                mWriteCharacteristic = gattCharacteristic;
                                mCurState = STATE_CONNECTED;
                                if ((bluetoothListener != null) && (mWriteCharacteristic != null)) {
                                    bluetoothListener.bluetoothStateChange(mCurState);
                                    bluetoothListener.bluetoothWriteChannelDone(mWriteCharacteristic);
                                }
                                LogUtil.i(TAG, "-------- got WRITE characteristic --------");
                            }
                        }
                    }

                    break;
                }
            }
        }
    }

    /**
     * 用以设置接受提示，设置了这个之后， 当有数据接受的时候，会自动触发回调函数
     * */

    /**
     * @param characteristic 读特征
     * @param enabled        是否打开Notification
     * @Description 设置Characteristic属性Notification
     */
    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBtAdapter == null || mBtGatt == null) {
            LogUtil.e(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBtGatt.setCharacteristicNotification(characteristic, enabled);

        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
                .fromString(BtGattAttr.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            descriptor
                    .setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            if (mCurProtocalType == Protocal_Type.JD) {
                descriptor
                        .setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                LogUtil.i(TAG, "setCharacteristicIndication done!");
            }
            mBtGatt.writeDescriptor(descriptor);
            LogUtil.i(TAG, "setCharacteristicNotification done!");
        }
    }

    /**
     * @param characteristic     读特征
     * @param enableIndification 是否打开indification
     * @Description 设置Characteristic属性Notification
     */
    private boolean enableCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enableIndification) {
        if (mBtAdapter == null || mBtGatt == null) {
            LogUtil.e(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        boolean bRet = false;
        mBtGatt.setCharacteristicNotification(characteristic, true);
        BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(BtGattAttr.CLIENT_CHARACTERISTIC_CONFIG));
        if (descriptor != null) {
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            if (enableIndification) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
            }
            bRet = mBtGatt.writeDescriptor(descriptor);
            LogUtil.i(TAG, "enableCharacteristicNotification done!");
        }
        return bRet;
    }

    /**
     * @Description 使能阿里智能秤历史数据服务
     */
    public void enableHistroryService() {
        mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.BodyHistory, true));
        mCharacteristicUtil.doTask();
    }

    public void syncCloudData(byte[] data){
        mCharacteristicUtil.addTask(new TaskData(Synchronization_Task_Key.CSDownData, data));
        mCharacteristicUtil.doTask();
    }


    /**
     * @Description 通过蓝牙标准时间通道设置秤时钟
     */
    public void syncTime2Scale() {
        Calendar c = Calendar.getInstance();
        byte[] bTime = new byte[7];
        byte[] bYear = BytesUtil.shortToByteArray((short) c.get(Calendar.YEAR));
        bTime[0] = bYear[1];
        bTime[1] = bYear[0];
        bTime[2] = (byte) (c.get(Calendar.MONTH) + 1);
        bTime[3] = (byte) c.get(Calendar.DAY_OF_MONTH);
        bTime[4] = (byte) c.get(Calendar.HOUR_OF_DAY);
        bTime[5] = (byte) c.get(Calendar.MINUTE);
        bTime[6] = (byte) c.get(Calendar.SECOND);
        mCharacteristicUtil.addTask(new TaskData(CsBtUtil_v11.Synchronization_Task_Key.Time, bTime));
        mCharacteristicUtil.doTask();
    }

    /**
     * @Description 通过蓝牙标准时间通道设置秤时钟
     */
    public void syncTime2ScaleEx() {
        byte[] bTime = new byte[4];
        int curTime=(int)(System.currentTimeMillis() /1000);
        BytesUtil.putInt(bTime, curTime, 0);
        mCharacteristicUtil.addTask(new TaskData(CsBtUtil_v11.Synchronization_Task_Key.Time, bTime));
        mCharacteristicUtil.doTask();
    }



    /**
     * @param characteristic 读特征
     * @Description 透传处理函数
     */
    private void handleConnectedInfo(BluetoothGattCharacteristic characteristic) {

        byte[] data = characteristic.getValue();
        String s1 = BytesUtil.bytesToPrintString(data);
        LogUtil.i(TAG, "收到透传数据:" + s1);

        if (data != null && data.length > 0) {
            try {
                iStraightFrame frame = straightFrameFactory.getInstance(mCurProtocalType);
                enumProcessResult processResult = frame.process(data, characteristic.getUuid().toString());
                if (processResult == enumProcessResult.Received_Scale_Data) {
                    if (bluetoothListener != null) {
                        CsFatScale fatScale = straightFrameFactory.getInstance(mCurProtocalType).getFatScale();
                        if (fatScale.getLockFlag() == 1) {
                            if (fatScale.getAxunge() > 0) {
                                if (fatScale.ori_visceral_fat == 25600) {
                                    setDeviceInfo(2);
                                } else {
                                    setDeviceInfo(1);
                                }
                            }

                            if (mCurState == STATE_CONNECTED_CALCULATTING || mCurState == STATE_CONNECTED_WAITSCALE) {
                                mCurState = STATE_CONNECTED;
                                if (bluetoothListener != null) {
                                    bluetoothListener.bluetoothStateChange(mCurState);
                                }
                            }

                        } else {
                            if (fatScale.getWeight() > 0) {
                                if (mCurState == STATE_CONNECTED || mCurState == STATE_CONNECTED_WAITSCALE) {
                                    mCurState = STATE_CONNECTED_CALCULATTING;
                                    if (bluetoothListener != null) {
                                        bluetoothListener.bluetoothStateChange(mCurState);
                                    }
                                }
                            } else {
                                if (mCurState == STATE_CONNECTED || mCurState == STATE_CONNECTED_CALCULATTING) {
                                    mCurState = STATE_CONNECTED_WAITSCALE;
                                    if (bluetoothListener != null) {
                                        bluetoothListener.bluetoothStateChange(mCurState);
                                    }
                                }
                            }
                        }

                        if (fatScale.getWeight() < 6000) {
                            LogUtil.i(TAG,"123:"+ fatScale.toString());
                            if(fatScale.isHistoryData()){
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date minDate = df.parse("2010-01-01 00:00:00");
                                    if(fatScale.getWeighingDate().getTime()<minDate.getTime() || fatScale.getWeighingDate().getTime()>System.currentTimeMillis()){
                                        LogUtil.e(TAG,"Illegal data:" + fatScale.toString());
                                    }else{
                                        bluetoothListener.specialFatScaleInfo(fatScale);
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                bluetoothListener.specialFatScaleInfo(fatScale);
                            }

                        }
                    }
                } else if (processResult == enumProcessResult.Sync_JDRole_Resp) {
                    if (bluetoothListener != null) {
                        bluetoothListener.jdSyncRoleResponse();
                    }
                } else if (processResult == enumProcessResult.Match_User_Msg) {
                    if (frame instanceof chipseaStraightFrame) {
                        chipseaStraightFrame chipseaFrame = (chipseaStraightFrame) frame;
                        if (bluetoothListener != null) {
                            bluetoothListener.matchUserMsg(chipseaFrame.getFatConfirm());
                        }
                    }
                }
            } catch (FrameFormatIllegalException e) {
                e.printStackTrace();
            }
        }
    }

    public void startLeXinMeasure(){
        lxStraightFrame frame = (lxStraightFrame)straightFrameFactory.getInstance(Protocal_Type.LEXIN);
        frame.Init(mBindedDeviceMac,mManufacturerData,mBtGatt,mCharacteristicUtil);
    }

    /**
     * @param mac 蓝牙mac地址
     * @return 0:新设备(标记未知) 1:正常设备  2:威盛康故障设备
     * @Description 获取指定蓝牙设备的flag标记
     */
    public int getDeviceInfo(String mac) {
        int iDeviceType = 0;

        SharedPreferences settings = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        iDeviceType = settings.getInt(SP_DEVICE_PREFIX + mac, 0);

        return iDeviceType;
    }

    private void setDeviceInfo(int iType) {
        if (mBindedDeviceMac == null) return;
        if (mBindedDeviceMac.length() == 0) return;

        SharedPreferences settings = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SP_DEVICE_PREFIX + mBindedDeviceMac, iType);
        editor.commit();
    }


    public interface SeachDeviceCallback {
        void success(byte[] manufacturerData);
    }

    public interface AsynBLETaskCallback {
        void success(Object value);

        void failed();
    }

    public interface OnBluetoothListener {

        /**
         * @param mac      蓝牙BLE设备的mac地址
         * @param chipName 蓝牙BLE芯片名称
         * @Description 广播事件(不解析广播数据，第一时间通知)
         */
        void broadcastData(String mac, String chipName);

        /**
         * @param frame 解析后的广播数据
         * @Description OKOK广播事件
         */
        void broadcastChipseaData(chipseaBroadcastFrame frame);

        /**
         * @param fatScale 解析后的称重数据
         * @Description 所有协议的透传事件
         */
        void specialFatScaleInfo(CsFatScale fatScale);

        /**
         * @param fatConfirm 解析后的匹配数据
         * @Description 相似体重用户智能匹配
         */
        void matchUserMsg(CsFatConfirm fatConfirm);


        /**
         * @Description 京东同步用户应答
         */
        void jdSyncRoleResponse();

        /**
         * @param write 已就绪的写特征对象
         * @Description 写特征就绪事件
         */
        void bluetoothWriteChannelDone(BluetoothGattCharacteristic write);

        /**
         * @param state 状态码
         * @Description BLE状态变更事件
         */
        void bluetoothStateChange(int state);

        /**
         * @Description 蓝牙被手动关闭事件
         */
        void BluetoothTurnOff();


        /**
         * @Description 蓝牙被手动打开事件
         */
        void BluetoothTurnOn();

        void BluetoothTurningOff();
    }

}
