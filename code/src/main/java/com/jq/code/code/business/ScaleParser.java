package com.jq.code.code.business;

import android.content.Context;
import android.util.Log;

import com.jq.btlib.util.CsBtUtil_v11;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.ScaleInfo;
import com.jq.code.model.json.JsonProductInfo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

/**
 * Created by hfei on 2015/12/28.
 */
public class ScaleParser extends PrefsUtil {

    private static ScaleParser instance;

    private ScaleParser(Context context) {
        super(context);
    }

    public static ScaleParser getInstance(Context context) {
        if (instance == null) {
            synchronized (ScaleParser.class) {
                if (instance == null) {
                    instance = new ScaleParser(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    /**
     * 获取产品信息
     *
     * @return
     */
    public JsonProductInfo getProductInfo() {
        return getProductMap().get(getScale().getProduct_id());
    }

    /**
     * 保存产品ID与log键值对
     *
     * @param map
     */
    public void setProductMap(Map<String, JsonProductInfo> map) {
        setValue("cur_fproduct_info_map", JsonMapper.toJson(map));
    }

    /**
     * 保存产品ID与log键值对
     */
    public Map<String, JsonProductInfo> getProductMap() {
        String map = getValue("cur_fproduct_info_map", "{}");
        return JsonMapper.fromJson(map, new TypeReference<Map<String, JsonProductInfo>>() {
        });
    }

    /**
     * 设置当前设备信息
     */
    public void setScale(ScaleInfo info) {
        setValue("cur_scale_info", JsonMapper.toJson(info));
    }

    /**
     * 是否为OKOK 1.2协议秤
     *
     * @return
     */
    public boolean isOKOKV12() {
        int version = getScale().getVersion();
        return getScale().getProcotalType().equals(CsBtUtil_v11.Protocal_Type.OKOK.toString()) && version == 0x10;
    }

    /**
     * 得到当前设备信息
     */
    public ScaleInfo getScale() {
        String cur_scale_info = getValue("cur_scale_info", "{}");
        if (cur_scale_info.equals("{}")) {
            ScaleInfo info = new ScaleInfo();
            info.setId(Integer.parseInt(getValue("cur_scale_id", "0")));
            info.setName(getValue("cur_scale_name", "0"));
            info.setMac(getValue("cur_scale_mac", "0"));
            info.setType_id(Integer.parseInt(getValue("cur_scale_type_id", "-1")));
            info.setProduct_id(Integer.parseInt(getValue("cur_scale_product_id",
                    "0")));
            info.setLast_time(getValue("cur_scale_last_time", "0"));
            info.setAccount_id(Integer.parseInt(getValue("cur_scale_account_id",
                    "0")));
            info.setVersion(Integer.parseInt(getValue("cur_scale_version", "0")));
            info.setProcotalType(getValue("cur_scale_procotalType", "0"));
            return info;
        }
        Log.v("===getscale",""+JsonMapper.fromJson(cur_scale_info, ScaleInfo.class).toString());
        return JsonMapper.fromJson(cur_scale_info, ScaleInfo.class);
    }


    public int getCompanyId() {
        JsonProductInfo productInfo = getProductInfo();
        return getProductInfo() == null ? AccountEntity.company_id : productInfo.getCompany_id();
    }

    private static final String KEY_BLUETOOTH_STATE = "cur_bluetooth_state"; // 蓝牙绑定状态key
    public static final int STATE_BLUETOOTH_UNBOUND = 0; // 蓝牙未绑定
    public static final int STATE_BLUETOOTH_BOUND = 1; // 蓝牙绑定

    /**
     * 获取蓝牙绑定状态
     */
    public int getBluetoothState() {
        return getValue(KEY_BLUETOOTH_STATE, STATE_BLUETOOTH_UNBOUND);
    }

    /**
     * 设置蓝牙绑定状态
     */
    public void setBluetoothState(int state) {
        setValue(KEY_BLUETOOTH_STATE, state);
    }


    /**
     * 蓝牙是否绑定
     *
     * @return true为绑定，false 未绑定
     */
    public boolean isBluetoothBounded() {
        if (getScale().getMac() == null || getScale().getMac().equals("null") ||
                getScale().getMac().isEmpty()) {
            setBluetoothState(STATE_BLUETOOTH_UNBOUND);
        }
        return getBluetoothState() == STATE_BLUETOOTH_BOUND;
    }


    /**
     * 判断是否为脂肪秤
     *
     * @return true为脂肪秤，false默认为人体秤
     */
    public boolean isFatScale() {
        return getScale().getType_id() == 1;
    }
}
