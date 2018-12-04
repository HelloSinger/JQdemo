package com.jq.btc.bluettooth;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.widget.Toast;

import com.jq.btc.app.R;
import com.jq.btc.helper.CsBtEngine;
import com.jq.btc.helper.WeighDataParser;
import com.jq.btlib.model.device.CsFatConfirm;
import com.jq.btlib.model.device.CsFatScale;
import com.jq.btlib.protocal.chipseaBroadcastFrame;
import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.business.ScaleParser;
import com.jq.code.code.util.LogUtil;
import com.jq.code.code.util.StandardUtil;
import com.jq.code.model.PutBase;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.view.CustomToast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hfei on 2016/5/28.
 */
public class BLEController {

    private static final String TAG = BLEController.class.getSimpleName();
    private int currentImge = 0;
    private String msg = "";
    private Context context;
    private static BLEController mBleController;
    private CsBtEngine mCsBtEngine;
    private boolean isBound = false; //是否是绑定秤模式
    private ScaleInfo mScaleInfo;
    private ScaleParser mScalePresser;
    private long mPrevMatchUserId = 0; //上一次匹配用户id
    private Date mLastWeightingDate = new Date();
    private int measureSeqNo=0;

    private CsBtEngine.OnCsBtBusinessListerner businessListerner = new CsBtEngine.OnCsBtBusinessListerner() {
        /**
         * 广播数据回调
         *
         * @param frame
         */
        @Override
        public void broadcastChipseaData(chipseaBroadcastFrame frame) {
            if (isBound) {
                onBLEbound(frame);
            } else {
                onBLEData(frame);
            }
        }

        /**
         * 透传数据回调
         *
         * @param fatScale
         */
        @Override
        public void specialFatScaleInfo(CsFatScale fatScale) {
            WeightEntity roleDataInfo = WeighDataParser.create(context).csFatRoleDataInfo(fatScale);
            // 锁屏之后处理 -- 弹窗匹配
            if (fatScale.getLockFlag() == 1) {
                if (mScalePresser.getScale().getProcotalType().equals(CsBtUtil_v11.Protocal_Type.ALIBABA.toString())
                        || mScalePresser.getScale().getProcotalType().equals(CsBtUtil_v11.Protocal_Type.OKOKCloud.toString())) {
                    if (!fatScale.getWeighingDate().equals(mLastWeightingDate)) {
                        isFatFirst = true;
                        mLastWeightingDate = fatScale.getWeighingDate();
                    }
                }
                if (isFatFirst) {
                    mPrevMatchUserId = fatScale.getRoleId();
                    if (!mScalePresser.getScale().getProcotalType().equals(CsBtUtil_v11.Protocal_Type.JD.toString())) {
                        isFatFirst = false;
                    }
                    mCsBtEngine.syncWriteHistory();
                    if (l != null) {
                        l.onDataChange(true, roleDataInfo);
                    }
                } else {
                    //解决对新用户和未匹配用户的二次锁定
                    if (mPrevMatchUserId != fatScale.getRoleId()) {
                        mPrevMatchUserId = fatScale.getRoleId();
                        if (l != null) {
                            l.onDataChange(true, roleDataInfo);
                        }
                    }
                }
            } else {
                isFatFirst = true;
                if (l != null) {
                    l.onDataChange(false, roleDataInfo);
                }
            }
        }

        @Override
        public void bluetoothStateChange(int state) {
            LogUtil.e("bluetoothStateChange", "state:" + state);
            setState(state);
        }

        @Override
        public void matchUserMsg(CsFatConfirm fatConfirm) {
            if (l != null) {
                WeightEntity roleDataInfo =
                        WeighDataParser.create(context).csWeightRoleDataInfo((float) fatConfirm.getWeight(), fatConfirm.getScaleWeight(),
                                fatConfirm.getScaleProperty(),0,1);
                l.onMachRole(roleDataInfo, fatConfirm.getMatchedRoleList());
            }
        }

        @Override
        public void syncHistoryEnd(List<PutBase> weightEntities) {
            if (l != null) l.syncHistoryEnd(weightEntities);
        }
    };

    private BLEController(Context context) {
        mCsBtEngine = CsBtEngine.getInstance(context);
        mCsBtEngine.setOnCsBtBusinessListerner(businessListerner);
        mScalePresser = ScaleParser.getInstance(context);
        this.context = context;
    }

    public static BLEController create(Context context) {
        if (mBleController == null) {
            synchronized (BLEController.class) {
                if (mBleController == null) {
                    mBleController = new BLEController(context.getApplicationContext());
                }
            }
        }
        return mBleController;
    }

    /**
     * 初始化时蓝牙状态
     */
    public void initState() {
        int state = mCsBtEngine.getCurBluetoothState();
        if (mCsBtEngine.isBluetoothEnable()) {
            if (mScalePresser.isBluetoothBounded()) {
                if (mScalePresser.isFatScale()) {
                    setState(state, getBLEStateDescription(state), getBLEStateImage(state));
                } else {
                    setState(state, "", R.mipmap.ble_connecting_1);
                }
            } else {
                setState(state, context.getString(R.string.reportBoundTip), -1);
            }
        } else {
            setState(state, context.getString(R.string.reportStateClose), -1);
        }
    }

    /**
     * 是否为绑定状态
     *
     * @return
     */
    public boolean isBound() {
        return isBound;
    }

    /**
     * 设置绑定状态
     *
     * @param bound
     */
    public void setBound(boolean bound) {
        isBound = bound;
    }

    /**
     * 重新绑定
     */
    public void reBound() {
        mScaleInfo = null;
    }

    public boolean isBluetoothEnable() {
        return mCsBtEngine.isBluetoothEnable();
    }

    public boolean isLocationEnable(Context context) {
        if(Build.VERSION.SDK_INT>=23 && ( Build.MANUFACTURER.equalsIgnoreCase("OPPO") || Build.MANUFACTURER.equalsIgnoreCase("VIVO") || Build.MANUFACTURER.equalsIgnoreCase("HTC") )){
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (networkProvider || gpsProvider) return true;
            return false;
        }else{
            return true;
        }

    }


    /**
     * 开始扫描
     */
    public void connectBluetooth() {
        if (!mScalePresser.isBluetoothBounded()) return;
        if (mScalePresser.isFatScale()) {
            mCsBtEngine.stopSearch();
            if (!mCsBtEngine.isConnected()) {
                mCsBtEngine.connectDevice(mScalePresser.getScale().getMac(), mScalePresser.getScale().getProcotalType());
            }
        } else {
            startSearch();
        }
    }

    /**
     * 断开连接
     */
    public void disconnectBluetooth() {
        mCsBtEngine.stopSearch();
        mCsBtEngine.stopAutoConnect();
    }


    /**
     * 开始扫描
     */
    public void startSearch() {
        //停止搜索
        mCsBtEngine.stopSearch();
        mCsBtEngine.startSearch();
    }

    /**
     * 停止扫描
     */
    public void stopSeach() {
        if (mCsBtEngine != null) {
            mCsBtEngine.stopSearch();
        }
    }

    public void registerReceiver(Context context) {
        mCsBtEngine.registerReceiver(context);
    }

    public void unregisterReceiver(Context context) {
        mCsBtEngine.unregisterReceiver(context);
    }

    private ArrayList<WeightEntity> roleDataHistory = new ArrayList<WeightEntity>();

    /**
     * 获取历史数据
     *
     * @return
     */
    public ArrayList<WeightEntity> getRoleDataHistory() {
        return roleDataHistory;
    }

    private boolean isFatFirst = true; //脂肪秤开始锁定状态标志位
    private boolean isWeightStartLock = true; //人体秤开始锁定状态标志位


    private void onBLEbound(chipseaBroadcastFrame frame) {
        //updated by lixun; 以下写法会导致mScaleInfo一致被变更，绑定错误的蓝牙秤
        //if (mScaleInfo != null && mScaleInfo.getMac() != null && mScaleInfo.getMac().equals(frame.mac)) {
        boolean bPush = false;

        if (mScaleInfo != null) {
            if (mScaleInfo.getMac() != null && mScaleInfo.getMac().equals(frame.mac)) {
                bPush = true;
            }
        } else {
            if (mScaleInfo == null) mScaleInfo = new ScaleInfo();
            LogUtil.e("Cs", "---new ScaleInfo:" + frame.mac);
            mScaleInfo.setMac(frame.mac);
            mScaleInfo.setName(frame.name);
            mScaleInfo.setVersion(frame.version);
            mScaleInfo.setType_id(frame.deviceType);
            mScaleInfo.setProduct_id(frame.productId);
            mScaleInfo.setProcotalType(frame.procotalType);
            mScaleInfo.setFushu(""+frame.type_fu);
            mScaleInfo.setStatue(frame.status);
            if(frame.kitchens == 1){
                mScaleInfo.setKitchens("1");
            }else {
                mScaleInfo.setKitchens("0");
            }
            bPush = true;
        }

        if (bPush) {
            String value = "";
            int tip = R.string.tutorialBoundTip1;
            if (frame.weight > 0) {
                if(frame.kitchens == 1){
                    value = "" + frame.weight ;
                }else {
                    value = StandardUtil.getWeightExchangeValue(context,
                            (float) frame.weight, frame.scaleWeight,
                            frame.scaleProperty);
                }
                tip = R.string.tutorialBoundTip;
            }
            if (l != null) {
                l.onBound(mScaleInfo, value, tip);
            }
        }
    }

    private void onBLEData(chipseaBroadcastFrame frame) {
        ScaleInfo info = mScalePresser.getScale();

        if (info == null) return;
        if (info.getMac() == null) return;

            WeightEntity roleDataInfo = WeighDataParser.create(context).csWeightRoleDataInfo((float) frame.weight, frame.scaleWeight, frame.scaleProperty, frame.r1, frame.deviceType);
            // 是当前绑定的秤
            if (info.getMac().equals(frame.mac)) {
                // 锁屏之后处理 -- 弹窗匹配
                if (frame.cmdId == 1) {
                    if (isWeightStartLock || (measureSeqNo != frame.measureSeqNo)) {
                        isWeightStartLock = false;
                        measureSeqNo = frame.measureSeqNo;
                        if (l != null) {
                            l.onDataChange(true, roleDataInfo);
                        }
                    }
                } else {
                    isWeightStartLock = true;
                    if (l != null) {
                        l.onDataChange(false, roleDataInfo);
                    }
                }
            }
    }

    /**
     * 设置蓝牙断开后能否重链接
     *
     * @param isReconectable
     */
    public void setReConnectable(boolean isReconectable) {
        mCsBtEngine.setReConnectable(isReconectable);
    }

    public void reSendAllRoleInfoV11() {
        mCsBtEngine.reSendAllRoleInfoV11();
    }

    /**
     * 判断蓝牙断开后能否重链接
     *
     * @return
     */
    public boolean isReconectable() {
        return mCsBtEngine.isReconectable();
    }


//    /**
//     * 重新选择下发角色
//     *
//     * @param role_id
//     */
//    public void reSelecteRole(int role_id) {
//        mCsBtEngine.writeSelecedRole(role_id);
//    }

    /**
     * 重新选择下发角色
     */
    public void reSelecteRole12() {
        mCsBtEngine.writeSelecedRole12();
    }

    /**
     * 获取蓝牙当前状态
     *
     * @return
     */
    public String getBLEStateDescription(int state) {
        String sRet = "";
        switch (state) {
            case CsBtUtil_v11.STATE_CLOSE:
                sRet = context.getString(R.string.reportStateWatting);
                break;
            case CsBtUtil_v11.STATE_WAIT_CONNECT:
                sRet = context.getString(R.string.reportStateWatting);
                break;
            case CsBtUtil_v11.STATE_CONNECTING:
                sRet = context.getString(R.string.reportStateConnecting);
                break;
            case CsBtUtil_v11.STATE_CONNECTED:
                sRet = context.getString(R.string.reportStateConnected);
                break;
            case CsBtUtil_v11.STATE_CONNECTED_CALCULATTING:
                sRet = context.getString(R.string.reportStateCalculatting);
                break;
            case CsBtUtil_v11.STATE_CONNECTED_WAITSCALE:
                sRet = context.getString(R.string.reportStateWaitScale);
                break;
        }
        return sRet;
    }

    /**
     * 获取蓝牙图标
     *
     * @return
     */
    public int getBLEStateImage(int state) {
        int sRet = -1;
        switch (state) {
            case CsBtUtil_v11.STATE_CLOSE:
                break;
            case CsBtUtil_v11.STATE_WAIT_CONNECT:
                break;
            case CsBtUtil_v11.STATE_CONNECTING:
                sRet = R.mipmap.ble_connected;
                break;
            case CsBtUtil_v11.STATE_CONNECTED:
                sRet = R.mipmap.ble_connected;
                break;
            case CsBtUtil_v11.STATE_CONNECTED_CALCULATTING:
                sRet = R.mipmap.ble_connected;
                break;
            case CsBtUtil_v11.STATE_CONNECTED_WAITSCALE:
                sRet = R.mipmap.ble_connected;
                break;
        }
        return sRet;
    }

    private void setState(int state) {
        LogUtil.e("setState", "state:" + state);
        currentImge = R.mipmap.ble_connecting_1;
        switch (state) {
            case CsBtUtil_v11.STATE_BLE_CLOSE:
                msg = context.getString(R.string.reportStateClose);
                currentImge = -1;
                break;
            case CsBtUtil_v11.STATE_BLE_OPEN:
                if (mScalePresser.isFatScale()) {
                    msg = context.getString(R.string.reportStateWatting);
                    currentImge = -1;
                } else if (mScalePresser.getScale().getType_id() == 0) {
                    msg = "";
                    currentImge = -1;
                } else {
                    msg = context.getString(R.string.reportBoundTip);
                    currentImge = -1;
                }
                break;
            case CsBtUtil_v11.STATE_CLOSE:
                isFatFirst = true;
//                if (mCsBtEngine.getRoleDataHistoryCount() > 0) {
//                    show(context, String.format(context.getString(R.string.syncHistoryData), mCsBtEngine.getRoleDataHistoryCount()));
//                    mCsBtEngine.clearHistory();
//                }
                msg = context.getString(R.string.reportStateWatting);
                currentImge = -1;
                //CsBtEngine.bluetoothStateChange已经断线重连接处理
//                if (mCsBtEngine != null) {
//                    mCsBtEngine.connectDevice(mScalePresser.getScale().getMac(),
//                            mScalePresser.getScale().getProcotalType());
//                }
                break;
            case CsBtUtil_v11.STATE_WAIT_CONNECT:
                msg = context.getString(R.string.reportStateWatting);
                currentImge = -1;
                break;

            case CsBtUtil_v11.STATE_CONNECTING:
                msg = context.getString(R.string.reportStateConnecting);
                currentImge = -1;
                break;

            case CsBtUtil_v11.STATE_CONNECTED:
                msg = context.getString(R.string.reportStateConnected);
                currentImge = R.mipmap.ble_connected;
                break;
            case CsBtUtil_v11.STATE_CONNECTED_CALCULATTING: // 测量中
                msg = context.getString(R.string.reportStateCalculatting);
                currentImge = R.mipmap.ble_connected;
                break;
            case CsBtUtil_v11.STATE_CONNECTED_WAITSCALE: // 请上秤测量
                msg = context.getString(R.string.reportStateWaitScale);
                currentImge = R.mipmap.ble_connected;
                break;
            default:
                break;
        }
        setState(state, msg, currentImge);

    }

    private void setState(int state, String msg, int currentImge) {
        if (l != null) {
            LogUtil.e("onStateChange111111111", "state:" + state);
            l.onStateChange(state, msg, currentImge);
        }
    }

    public void show(Context context, Object paramObject) {
        CustomToast.showToast(context, paramObject.toString(), Toast.LENGTH_SHORT);
    }

    private OnBlEChangeListener l;

    public void setOnBLEChangeListener(OnBlEChangeListener l) {
        this.l = l;
    }

    public interface OnBlEChangeListener {

        void onMachRole(WeightEntity entity, ArrayList<Integer> matchedRoleList);

        void onDataChange(boolean isLook, WeightEntity data);

        void onStateChange(int state, String msg, int currentImge);

        void onBound(ScaleInfo scaleInfo, String value, int tip);

        void syncHistoryEnd(List<PutBase> weightEntities);
    }
}
