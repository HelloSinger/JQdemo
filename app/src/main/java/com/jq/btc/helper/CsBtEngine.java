package com.jq.btc.helper;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jq.btlib.model.device.CsFatConfirm;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.protocal.chipseaBroadcastFrame;
import com.jq.btlib.protocal.syncChipseaInstruction;
import com.jq.btlib.protocal.syncJdInstruction;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.btlib.util.CsBtUtil_v11.OnBluetoothListener;
import com.jq.btlib.util.ThreadUtil;
import com.jq.code.code.business.Account;
import com.jq.code.code.business.Config;
import com.jq.code.code.business.DelayTimer;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.db.RoleDB;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.db.WeightTmpDB;
import com.jq.code.code.util.LogUtil;
import com.jq.code.code.util.TimeUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.WeightTmpEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by hfei on 2015/10/28.
 */
public class CsBtEngine implements OnBluetoothListener {

    private static final String TAG = CsBtEngine.class.getSimpleName();
    private static CsBtEngine instance;
    private static CsBtUtil_v11 mCsBtUtil;
    private ArrayList<com.jq.btlib.protocal.RoleInfo> mRoleList;
    private String mac;
    private String protocalType;
    private OnCsBtBusinessListerner listerner;
    private BluetoothGattCharacteristic write;
    private int deviceType;
    private Handler mHandler;
    private Context context;
    private boolean jdSyncRoleResponse;
    private boolean mUserInfoSyncing = false; //是否正在同步用户
    private boolean mRecWeightData = false; //收到称重数据


    public CsBtEngine(Context context) {
        this.context = context;
        mCsBtUtil = new CsBtUtil_v11();
        mCsBtUtil.setBluetoothListener(this);
        mHandler = new Handler();
    }

    public static CsBtEngine getInstance(Context context) {
        if (instance == null) {
            synchronized (CsBtEngine.class) {
                if (instance == null) {
                    instance = new CsBtEngine(context);
                }
            }
        }
        return instance;
    }

    public void openBluetooth(boolean isSilent) {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.openBluetooth(isSilent);
    }

    public int getCurBluetoothState() {
        if (mCsBtUtil == null) {
            return 1;
        }
        return mCsBtUtil.getCurBluetoothState();
    }


    /**
     * @Description 判断蓝牙是否已经使能
     */
    public boolean isBluetoothEnable() {
        if (mCsBtUtil == null) {
            return false;
        }
        return mCsBtUtil.isBluetoothEnable();
    }

    public boolean isConnected() {
        if (mCsBtUtil == null) {
            return false;
        }
        return mCsBtUtil.isConnected();
    }

    public void stopSearch() {
        if (mCsBtUtil == null || !mCsBtUtil.isBluetoothEnable()) {
            return;
        }
        mCsBtUtil.stopSearching();
    }

    public void startSearch() {
        if (mCsBtUtil == null || !mCsBtUtil.isBluetoothEnable()) {
            return;
        }
        mCsBtUtil.startSearching();
    }

    public void sync(Runnable r) {
        if (mHandler != null) {
            synchronized (mHandler) {
                mHandler.postDelayed(r, 150);
            }
        }
    }

    /**
     * @Description 关闭蓝牙
     */
    public void closeBluetooth() {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.closeBluetooth();
    }

    public void closeGATT(boolean isUnbound) {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.forceClose(isUnbound);
    }

    public void registerReceiver(Context context) {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.registerReceiver(context);
    }

    public void unregisterReceiver(Context context) {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.unregisterReceiver(context);
    }

    public void stopAutoConnect() {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.stopAutoConnect();
    }

    public void forceCloseBLE() {
        if (mCsBtUtil == null) {
            return;
        }
        mCsBtUtil.disconnectGATT();
    }

    public void connectDevice(String mac, String protocalType) {
        if (mCsBtUtil == null || !mCsBtUtil.isBluetoothEnable() || mac == null) {
            return;
        }
        this.mac = mac;
        if(protocalType != null){
            this.protocalType = protocalType;
        }else {
            protocalType = "sd";
            this.protocalType ="sd";
        }
        LogUtil.i(TAG, "Connect Device protocalType===>" + protocalType);
        mUserInfoSyncing = false;
        if (protocalType.equals(CsBtUtil_v11.Protocal_Type.JD.toString())) {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.JD);
        } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.ALIBABA.toString())) {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.ALIBABA);
        } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloud.toString())) {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOKCloud);
        } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloudV3.toString())) {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOKCloudV3);
        } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.LEXIN.toString())) {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.LEXIN);
        }else {
            mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOK);
        }
    }

    @Override
    public void broadcastData(String mac, String chipName) {

    }

    @Override
    public void broadcastChipseaData(chipseaBroadcastFrame frame) {
        if (listerner != null) {
            listerner.broadcastChipseaData(frame);
        }
    }

    @Override
    public void specialFatScaleInfo(CsFatScale fatScale) {
        if (fatScale.getLockFlag() == 1) {
            if (deviceType == 0) {
                deviceType = mCsBtUtil.getDeviceInfo(ScaleParser.getInstance(context).getScale().getMac());
                if (deviceType == 2) {
                    fatScale.cleanFatInfo();
                    fatScale.setLockFlag((byte) 0);
                    writeRoleInfo();
                }
            }
            if (!fatScale.isHistoryData()) {
//                int iVersion = ScalePresser.getInstance(context).getScale().getVersion();
//                if (iVersion == 0x11) {
//                    syncWriteHistory();
//                }
            } else {
                parseHistoryData(fatScale);
            }
        }
        if (listerner != null && !fatScale.isHistoryData()) {
            mRecWeightData = true;
            listerner.specialFatScaleInfo(fatScale);
        }
    }

    @Override
    public void matchUserMsg(CsFatConfirm fatConfirm) {
        if (listerner != null) {
            listerner.matchUserMsg(fatConfirm);
        }
    }

    /**
     * 清除历史数据个数
     */
    public void clearHistory() {
        curRoleMatchHistoryDataList.clear();
    }

    /**
     * 保存到数据库并计算日周月数据
     *
     * @param fatScale
     */
    private void parseHistoryData(CsFatScale fatScale) {
        if (fatScale.getWeighingDate().getTime() < 1262275200) {
            return;
        }
        if (hasSyncHistoryEnd) {
            hasSyncHistoryEnd = false;
            clearHistory();
        }
        WeightEntity roleDataInfo = WeighDataParser.create(context).csFatRoleDataInfo(fatScale);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        roleDataInfo.setWeight_time(formatter.format(fatScale.getWeighingDate()));
        if (fatScale.getRoleId() > 0) {
            RoleInfo roleInfo = RoleDB.getInstance(context).findRoleById(fatScale.getRoleId());
            if (roleInfo != null) {
                float bmi = WeighDataParser.create(context).getBMI(roleInfo.getHeight(), roleDataInfo.getWeight());
                roleDataInfo.setBmi(bmi > 40 ? 40 : bmi);
                roleDataInfo.setAccount_id(roleInfo.getAccount_id());
                LogUtil.i(TAG, "ID:" + roleInfo.getId() + ";角色昵称：" + roleInfo.getNickname());
                curRoleMatchHistoryDataList.add(roleDataInfo);
                WeightDataDB.getInstance(context).create(roleDataInfo);
            }
        } else {
            parseHistoryDataWithImpedance(roleDataInfo);
        }
        delayTimer.check(2 * 1000);
    }

    private List<PutBase> curRoleMatchHistoryDataList = new ArrayList<>();
    private String mLastWeight_time = "";
    private boolean hasSyncHistoryEnd = true;

    private DelayTimer delayTimer = new DelayTimer(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hasSyncHistoryEnd = true;
            if (listerner != null)
                listerner.syncHistoryEnd(curRoleMatchHistoryDataList);
        }
    });

    /**
     * 解析带电阻数据
     *
     * @param roleDataInfo
     */
    private void parseHistoryDataWithImpedance(WeightEntity roleDataInfo) {
        if (roleDataInfo.getWeight_time().equals(mLastWeight_time)) {
            return;
        } else {
            mLastWeight_time = roleDataInfo.getWeight_time();
        }
        ArrayList<RoleInfo> roleALL = Account.getInstance(context).findRoleALL();
        ArrayList<RoleInfo> tmpRole = new ArrayList<>();
        for (RoleInfo info : roleALL) {
            WeightEntity entity = WeightDataDB.getInstance(context).findLastRoleDataByRoleId(info);
            if (entity != null) {
                boolean isWeightTrue = Math.abs(roleDataInfo.getWeight() - entity.getWeight()) <= 3;
                if (entity.getR1() == 0 || roleDataInfo.getR1() == 0) {
                    if (isWeightTrue) {
                        tmpRole.add(info);
                    }
                } else {
                    if (isWeightTrue && Math.abs(roleDataInfo.getR1() - entity.getR1()) <= 15) {
                        tmpRole.add(info);
                    }
                }
            }
        }
        if (tmpRole.size() == 1) {
            WeighDataParser.create(context).fillFatWithImpedance(roleDataInfo, tmpRole.get(0));
            curRoleMatchHistoryDataList.add(roleDataInfo);
            WeightDataDB.getInstance(context).create(roleDataInfo);
        } else {
            WeightTmpEntity tmpEntity = new WeightTmpEntity();
            tmpEntity.setImpedance(roleDataInfo.getR1());
            tmpEntity.setAccount_id(Account.getInstance(context).getAccountInfo().getId());
            tmpEntity.setWeight(roleDataInfo.getWeight());
            tmpEntity.setWeight_time(roleDataInfo.getWeight_time());
            tmpEntity.setScaleweight(roleDataInfo.getScaleweight());
            tmpEntity.setScaleproperty(roleDataInfo.getScaleproperty());
            WeightTmpDB.getInstance(context).create(tmpEntity);
        }
    }

    /**
     * 同步历史数据
     */
    public void syncWriteHistory() {
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1; i++) {
//                    try {
//                        Thread.sleep(150);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    if (write != null)
                        if (mCsBtUtil != null) {
                            LogUtil.i(TAG, "syncWriteHistory");
                            ScaleInfo scale = ScaleParser.getInstance(context).getScale();
                            if (scale.getVersion() == 0x11 && scale.getProcotalType().equals(CsBtUtil_v11.Protocal_Type.OKOK.toString())) {
                                mCsBtUtil.writeCharacteristic(write, syncChipseaInstruction.sycnRetrieveHistoryData_v11(), CsBtUtil_v11.Down_Instruction_Type.Sync_UserInfo);
                            }
                        }
                }
            }
        });
    }

    @Override
    public void bluetoothWriteChannelDone(
            final BluetoothGattCharacteristic write) {
        if (mCsBtUtil == null) {
            return;
        }
        getRoleInfo();
        this.write = write;
        deviceType = mCsBtUtil.getDeviceInfo(ScaleParser.getInstance(context).getScale().getMac());
        int version = ScaleParser.getInstance(context).getScale().getVersion();
        LogUtil.i(TAG,
                "本地协议版本===================V" + Integer.toHexString(version));
        if (protocalType.equals(CsBtUtil_v11.Protocal_Type.JD.toString())) {
            witeJDRoleInfo();
        } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.ALIBABA.toString()) || protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloud.toString())
                || protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloudV3.toString())) {
            mCsBtUtil.syncTime2Scale();
            writeWeightUnit();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCsBtUtil.enableHistroryService();
        }else if(protocalType.equals(CsBtUtil_v11.Protocal_Type.LEXIN.toString())){
            mCsBtUtil.startLeXinMeasure();
        }else if(protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloudV3.toString())){
            mCsBtUtil.syncTime2ScaleEx();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mCsBtUtil.enableHistroryService();
        } else {
            if (version == 0x11) {
                writeRoleInfoWithV11(2);
            } else {
                writeRoleInfo();
            }
        }
    }


    /**
     * 下发京东协议
     */
    private void witeJDRoleInfo() {
        LogUtil.i(TAG, "JD==============================v10");
        final RoleInfo roleInfo = Account.getInstance(context).getRoleInfo();
        if (roleInfo == null) return;
        byte sex_tmp = 0;
        if (roleInfo.getSex().equals("男")) {
            sex_tmp = 1;
        }
        jdSyncRoleResponse = false;
        final long start = System.currentTimeMillis();
        final byte sex = sex_tmp;
        final byte height = (byte) (roleInfo.getHeight() & 0xff);
        final byte age = (byte) (TimeUtil.getAgeThroughBirthday(roleInfo.getBirthday()) & 0xff);
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                while (!jdSyncRoleResponse
                        && (System.currentTimeMillis() - start) < (30 * 1000)) {
                    LogUtil.e("jdSyncRoleResponse", jdSyncRoleResponse + "");
                    try {
                        Thread.sleep(150);
                        if (write != null)
                            if (mCsBtUtil != null) {
                                mCsBtUtil.writeCharacteristic(write, syncJdInstruction.syncUserInformation(sex, age, height), CsBtUtil_v11.Down_Instruction_Type.Sync_UserInfo);
                            }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void reSendAllRoleInfoV11() {
        if (mCsBtUtil == null) {
            return;
        }
        if (!mCsBtUtil.isConnected()) {
            return;
        }

        if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOK.toString())) {
            int version = ScaleParser.getInstance(context).getScale().getVersion();
            if (version == 0x11) {
                getRoleInfo();
                writeRoleInfoWithV11(1);
            }
        }

    }


    /**
     * 根据协议v11下发所有角色数据
     */
    private void writeRoleInfoWithV11(final int iCount) {
        LogUtil.i(TAG, "OKOK=============================v11");
        if (mRoleList == null) {
            return;
        }
        final ArrayList<byte[]> bs = syncChipseaInstruction.sycnAllRoleInfo_v11(mRoleList);
        for (int i = 0; i < mRoleList.size(); i++) {
            LogUtil.i(TAG, " ==" + mRoleList.get(i).toString());
        }
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                mUserInfoSyncing = true;
                for (int i = 0; i < iCount; i++) {
                    for (int j = 0; j < bs.size(); j++) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        byte[] b = bs.get(j);
                        if (write != null)
                            if (mCsBtUtil != null) {
                                mCsBtUtil.writeCharacteristic(write, b, CsBtUtil_v11.Down_Instruction_Type.Sync_UserInfo);
                            }
                    }
                }
                mUserInfoSyncing = false;
            }
        });
    }

//    /**
//     * 下发选定角色ID
//     *
//     * @param id
//     */
//    public void writeSelecedRole(final int id) {
//        mRecWeightData = false;
//        ThreadUtil.executeThread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i < 10; i++) {
//                    if (mCsBtUtil != null && !mUserInfoSyncing) {
//                        if (mRecWeightData) break;
//                        mCsBtUtil.writeCharacteristic(write, syncChipseaInstruction.syncSelectedUserId(id), CsBtUtil_v11.Down_Instruction_Type.Sync_UserInfo);
//                        try {
//                            Thread.sleep(800);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//    }

    public void writeWeightUnit(){
        if (mCsBtUtil == null) {
            return;
        }
        if (!mCsBtUtil.isConnected()) {
            return;
        }

        int version = ScaleParser.getInstance(context).getScale().getVersion();
        if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloud.toString())) {
            if (version == 0x02) {
                int weightUnit = Config.getInstance(context).getIntWeightUnit();
                final byte bUnit;
                if (weightUnit == Config.JIN) {
//                    bUnit = 0x02;
                    bUnit = 0x00;
                } else {
                    bUnit = 0x00;
                }
                final byte[] bData = new byte[2];
                bData[0] = 0x01;
                bData[1] = bUnit;
                //发两次，客户反馈有一点的机率会无法切换
                mCsBtUtil.syncCloudData(bData);
                mCsBtUtil.syncCloudData(bData);
            }
        }
    }

    /**
     * OKOK 协议1.2重新下发角色数据
     */
    public void writeSelecedRole12() {
        if (protocalType == null || getCurBluetoothState() != CsBtUtil_v11.STATE_CONNECTED) return;
        if (isOKOKV12()) {
            writeRoleInfo();
        }
    }

    /**
     * 是否为OKOK 1.2协议秤
     *
     * @return
     */
    public boolean isOKOKV12() {
        int version = ScaleParser.getInstance(context).getScale().getVersion();
        return protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOK.toString()) && version == 0x10;
    }

    /**
     * 根据协议v10下发角色数据
     */
    private void writeRoleInfo() {
        LogUtil.i(TAG, "OKOK==============================v10");
        final RoleInfo roleInfo = Account.getInstance(context).getRoleInfo();
        if (roleInfo == null) return;
        byte sex_tmp = 0;
        if (deviceType == 2) {
            if (roleInfo.getSex().equals("男")) {
                sex_tmp = 0;
            } else {
                sex_tmp = 1;
            }
        } else {
            if (roleInfo.getSex().equals("女")) {
                sex_tmp = 0;
            } else {
                sex_tmp = 1;
            }
        }
        final byte sex = sex_tmp;
        final byte height = (byte) (roleInfo.getHeight() & 0xff);
        ThreadUtil.executeThread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (i < 2) {
                    try {
                        Thread.sleep(150);
                        if (write != null)
                            if (mCsBtUtil != null) {
                                mCsBtUtil.writeCharacteristic(write, syncChipseaInstruction.syncRoleInfo_v10(roleInfo.getId(), sex, height, Account.getInstance(context).getRoleInfo().getBirthday()),
                                                CsBtUtil_v11.Down_Instruction_Type.Sync_UserInfo);
                            }
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 封装下传角色数据
     */
    private void getRoleInfo() {
        ArrayList<RoleInfo> roleInfos = RoleDB.getInstance(context).findRoleAllByAccountId(Account.getInstance(context).getAccountInfo().getId());
        WeightDataDB dataDBUtil = WeightDataDB.getInstance(context);
        mRoleList = new ArrayList<>();
        for (int i = 0; i < roleInfos.size(); i++) {
            com.jq.btlib.protocal.RoleInfo info = new com.jq.btlib.protocal.RoleInfo();
            info.age = (byte) (TimeUtil.getAgeThroughBirthday(roleInfos.get(i).getBirthday()) & 0xff);
            info.height = (byte) (roleInfos.get(i).getHeight() & 0xff);
            info.roleId = roleInfos.get(i).getId();
            if (roleInfos.get(i).getSex().equals("男")) {
                info.sex = (byte) 1;
            } else {
                info.sex = (byte) 0;
            }
            WeightEntity dataInfo = dataDBUtil
                    .findLastRoleDataByRoleId(roleInfos.get(i));
            if (dataInfo != null) {
                info.weight = (short) (dataInfo.getWeight() * 10);
            } else {
                info.weight = 0;
            }
            mRoleList.add(info);
        }
        // LogUtil.e("getRoleInfo", mRoleList.toString());
    }

    @Override
    public void BluetoothTurnOff() {
        if (ScaleParser.getInstance(context).isFatScale()) {
            stopAutoConnect();
            forceCloseBLE();
        }
        if (listerner != null) {
            listerner.bluetoothStateChange(CsBtUtil_v11.STATE_BLE_CLOSE);
        }
    }

    @Override
    public void BluetoothTurnOn() {
        LogUtil.i(TAG, "Device Info: " + ScaleParser.getInstance(context).getScale().toString());
        if (ScaleParser.getInstance(context).isFatScale()) {
            connectDevice(ScaleParser.getInstance(context).getScale().getMac(), ScaleParser.getInstance(context).getScale().getProcotalType());
        } else {
            stopSearch();
            startSearch();
        }
        if (listerner != null) {
            listerner.bluetoothStateChange(CsBtUtil_v11.STATE_BLE_OPEN);
        }
    }


    private boolean isReconectable = true;

    /**
     * 设置蓝牙断开后能否重链接
     *
     * @param isReconectable
     */
    public void setReConnectable(boolean isReconectable) {
        this.isReconectable = isReconectable;
    }

    /**
     * 判断蓝牙断开后能否重链接
     *
     * @return
     */
    public boolean isReconectable() {
        return isReconectable;
    }

    @Override
    public void bluetoothStateChange(int state) {
        if (state == CsBtUtil_v11.STATE_CONNECTED) {
            WriteLog("蓝牙GATT连接成功");
        }
        if (state == CsBtUtil_v11.STATE_CLOSE) {
            if (mac == null) {
                return;
            }
            if (mCsBtUtil.isBluetoothEnable() && isReconectable()) {
                if (protocalType.equals(CsBtUtil_v11.Protocal_Type.JD.toString())) {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.JD);
                } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.ALIBABA.toString())) {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.ALIBABA);
                } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloud.toString())) {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOKCloud);
                }else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.OKOKCloudV3.toString())) {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOKCloudV3);
                } else if (protocalType.equals(CsBtUtil_v11.Protocal_Type.LEXIN.toString())) {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.LEXIN);
                }else {
                    mCsBtUtil.autoConnect(mac, CsBtUtil_v11.Protocal_Type.OKOK);
                }
            }

            WriteLog("蓝牙GATT连接断开,自动尝试重新连接");
        }
        if (listerner != null) {
            listerner.bluetoothStateChange(state);
        }
    }

    private void WriteLog(String string) {
        LogUtil.i(TAG, string);
    }

    public void setOnCsBtBusinessListerner(OnCsBtBusinessListerner listerner) {
        this.listerner = listerner;
    }

    public interface OnCsBtBusinessListerner {
        void broadcastChipseaData(chipseaBroadcastFrame frame);

        void specialFatScaleInfo(CsFatScale fatScale);

        void bluetoothStateChange(int state);

        void matchUserMsg(CsFatConfirm fatConfirm);

        /**
         * 同步历史数据
         *
         * @param weightEntities 当前角色已匹配历史数据
         */
        void syncHistoryEnd(List<PutBase> weightEntities);
    }

    @Override
    public void BluetoothTurningOff() {
        stopSearch();
    }

    @Override
    public void jdSyncRoleResponse() {
        jdSyncRoleResponse = true;
    }

}
