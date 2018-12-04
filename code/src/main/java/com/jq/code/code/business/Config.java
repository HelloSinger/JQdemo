package com.jq.code.code.business;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.jq.code.code.db.RemindWeightDB;
import com.jq.code.code.db.RoleDB;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.model.Constant;
import com.jq.code.model.DataType;
import com.jq.code.model.HeadInfo;
import com.jq.code.model.PutBase;
import com.jq.code.model.RemindeWeightTimeInfo;

import java.util.ArrayList;

/**
 * Created by hfei on 2016/5/9.
 */
public class Config extends PrefsUtil {

    private static Config instance;
    public static boolean isTypeChange = false;
    private Context context;
    private RemindWeightDB mRemindWeightDBUtil;
    private RoleDB mRoleDBUtil;

    public static Config getInstance(Context context) {
        if (instance == null) {
            synchronized (Config.class) {
                if(instance == null) {
                    instance = new Config(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    /**
     * 析构函数
     *
     * @param context
     */
    public Config(Context context) {
        super(context);
        this.context = context;
        mRemindWeightDBUtil = RemindWeightDB.getInstance(context);
        mRoleDBUtil = RoleDB.getInstance(context);
    }

    public void clearTables(long accountId) {
        mRoleDBUtil.removeRoleByAccountId(accountId);
        mRemindWeightDBUtil.removeRemindByAccountId(accountId);
    }

    /**
     * 初始化app打开时间
     */
    public void initAppOpenTs() {
        if (((System.currentTimeMillis() - getAppOpenTs()) / Constant.ONE_DAY_MS) <= 3) {
            setValue("cs_open_ts", System.currentTimeMillis());
        }
    }

    public int daysOfAppOpenLast() {
        int days = (int) ((System.currentTimeMillis() - getAppOpenTs()) / Constant.ONE_DAY_MS);
        if (days > 3) {
            setValue("cs_open_ts", System.currentTimeMillis());
        }
        return days;
    }

    private long getAppOpenTs() {
        return getValue("cs_open_ts", System.currentTimeMillis());
    }

    /**
     * 保存闹钟时间
     *
     * @param timeInfos
     * @param accountId
     */
    public void storeRemindWeightTime(ArrayList<RemindeWeightTimeInfo> timeInfos, int accountId) {
        if (timeInfos == null || timeInfos.size() == 0) {
            timeInfos = new ArrayList<>();
            RemindeWeightTimeInfo timeInfo1 = new RemindeWeightTimeInfo();
            timeInfo1.setIs_open(0);
            timeInfo1.setAccount_id(accountId);
            timeInfo1.setId(1);
            timeInfo1.setRemind_time("08:00");
            timeInfos.add(timeInfo1);

            RemindeWeightTimeInfo timeInfo2 = new RemindeWeightTimeInfo();
            timeInfo2.setIs_open(0);
            timeInfo2.setAccount_id(accountId);
            timeInfo2.setId(2);
            timeInfo2.setRemind_time("12:00");
            timeInfos.add(timeInfo2);

            RemindeWeightTimeInfo timeInfo3 = new RemindeWeightTimeInfo();
            timeInfo3.setIs_open(0);
            timeInfo3.setAccount_id(accountId);
            timeInfo3.setId(3);
            timeInfo3.setRemind_time("18:00");
            timeInfos.add(timeInfo3);
        }
        for (int i = 0; i < timeInfos.size(); i++) {
            mRemindWeightDBUtil.createWeightRemind(timeInfos.get(i));
        }
    }

    /**
     * 设置重量单位
     *
     * @param weightUnit
     */
    public void setWeightUnit(int weightUnit) {
        setIntWeightUnit(weightUnit);
    }

    public int getWeightUnit() {
        return getIntWeightUnit();
    }

    /**
     * 设置长度单位
     *
     * @param lengthUnit
     */
    public void setLengthUnit(int lengthUnit) {
        setIntLengthUnit(lengthUnit);
    }

    public int getLengthUnit() {
        return getIntLengthUnit();
    }


    /**
     * 设置https请求头信息
     */
    public void setHeadInfo(HeadInfo headInfo) {
        setValue("cur_head_info", JsonMapper.toJson(headInfo));
    }

    /**
     * 获得https请求头信息
     */
    public HeadInfo getHeadInfo() {
        return JsonMapper.fromJson(getValue("cur_head_info", "{}"), HeadInfo.class);
    }

    public String getUser_agent() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getPackageName() + "/");
        builder.append(PrefsUtil.getVersionName(context));
        builder.append(" (Android;" + Build.MODEL);
        builder.append(";" + Build.VERSION.RELEASE + ")");
        return builder.toString();
    }

    public String getUser_agentforFeedback() throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(context.getPackageName() + "/");
        builder.append(PrefsUtil.getVersionName(context));
        builder.append(" (Android;" + Build.MODEL);
        builder.append(";" + Build.VERSION.RELEASE);
        builder.append(";" + Build.BRAND);
        builder.append(";" + Build.DEVICE);
        builder.append(";" + Build.DISPLAY);
        builder.append(";" + Build.MANUFACTURER);
        builder.append(")");
        return builder.toString();
    }

    public String getDeviceId() {
        //TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //return TelephonyMgr.getDeviceId();
        return "";
    }
    public String getcs_scale() {
        SharedPreferences sh1 = context.getSharedPreferences("kitchens1",Context.MODE_PRIVATE);
        String name = sh1.getString("kitchen_name1", "0");
        SharedPreferences sh = context.getSharedPreferences("kitchens",Context.MODE_PRIVATE);
        String cs_scale = sh.getString("kitchen_name", "0");
        return name+","+cs_scale;
    }

    private static final String KEY_LENGTH_UNIT = "cur_length_int_set_unit"; // 长度单位key
    public static final int METRIC = 1400;  //公制（公斤，米）
    public static final int INCH = 1401;  //英制(磅，英尺，英寸)
    public static final int JIN = 1402; //斤
    public static final int ST = 1403; //英石

    /**
     * 设置长度单位
     */
    public void setIntLengthUnit(int unit) {
        setValue(KEY_LENGTH_UNIT, unit);
    }

    /**
     * 获取长度单位
     */
    public int getIntLengthUnit() {
        return getValue(KEY_LENGTH_UNIT, METRIC);
    }

    private static final String KEY_WEIGHT_UNIT = "cur_weight_int_set_unit"; // 重量单位key

    /**
     * 设置重量单位
     */
    public void setIntWeightUnit(int unit) {
        setValue(KEY_WEIGHT_UNIT, unit);
    }

    /**
     * 获取重量单位
     */
    public int getIntWeightUnit() {
        return getValue(KEY_WEIGHT_UNIT, METRIC);
//        return METRIC;
    }


    private static final String KEY_GUIDE_ACTIVITY = "cur_guide_view";

    /**
     * 判断activity是否引导过
     *
     * @return 是否已经引导过 true引导过了 false未引导
     */
    public boolean isGuided(String className) {
        if (className == null || "".equalsIgnoreCase(className))
            return false;
        String[] classNames = getValue(KEY_GUIDE_ACTIVITY, "").split(":");
        for (String string : classNames) {
            if (className.equalsIgnoreCase(string)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置该activity被引导过了。 将类名已  |a|b|c这种形式保存为value，因为偏好中只能保存键值对
     *
     * @param className
     */
    public void setIsGuided(String className) {
        if (className == null || "".equalsIgnoreCase(className))
            return;
        String classNames = getValue(KEY_GUIDE_ACTIVITY, "");
        StringBuilder sb = new StringBuilder(classNames).append(":").append(className);//添加值
        setValue(KEY_GUIDE_ACTIVITY, sb.toString());
    }

    /**
     * 动态显示类别
     */
    private static final String KEY_DYNAMIC_TYPE = "cur_dynamic_type";

    public void initDynamiaTypeFromSrv(String types) {
        StringBuilder builder = new StringBuilder();
        if (TextUtils.isEmpty(types)) {
            builder.append(DataType.WEIGHT.getType() + "," + DataType.FOOD.getType() + "," + DataType.EXERCISE.getType());
        } else if (types.equals(PutBase.TYPE_ALL)) {
            builder.append(DataType.WEIGHT.getType() + "," + DataType.BP.getType() + "," + DataType.BSL.getType() + "," + DataType.FOOD.getType() + "," + DataType.EXERCISE.getType());
        } else {
            String[] strings = types.split(",");
            if (strings.length > 0) {
                for (int i = 0; i < strings.length; i++) {
                    if (!isTypeExist(strings[i])) continue;
                    builder.append(strings[i]);
                    if (i < strings.length - 1) {
                        builder.append(",");
                    }
                }
            }
        }
        setValue(KEY_DYNAMIC_TYPE, builder.toString());
    }

    private boolean isTypeExist(String type) {
        DataType[] values = DataType.values();
        for (DataType dataType : values) {
            if (dataType.getType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public void setDynamicType(String type) {
        if (TextUtils.isEmpty(type)) return;
        String types = getValue(KEY_DYNAMIC_TYPE, DataType.WEIGHT.getType());
        String[] strings = types.split(",");
        if (strings.length > 0) {
            for (String s : strings) {
                if (s.equals(type)) {
                    return;
                }
            }
        }
        isTypeChange = true;
        StringBuilder builder = new StringBuilder(types).append(",").append(type);
        setValue(KEY_DYNAMIC_TYPE, builder.toString());
    }

    public void removeDynamicType(String type) {
        if (TextUtils.isEmpty(type)) return;
        String types = getDynamicType();
        String[] strings = types.split(",");
        if (strings.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String s : strings) {
                if (!s.equals(type)) {
                    builder.append(s);
                    builder.append(",");
                } else {
                    isTypeChange = true;
                }
            }
            setValue(KEY_DYNAMIC_TYPE, builder.toString().endsWith(",") ? builder.toString().substring(0, builder.toString().length() - 1) : builder.toString());
        }
    }

    public boolean hasType(String type) {
        boolean has = false;
        String types = getDynamicType();
        if (types.equals(PutBase.TYPE_ALL)) {
            has = true;
        } else {
            String[] strings = types.split(",");
            for (String s : strings) {
                if (s.equals(type)) {
                    has = true;
                    break;
                }
            }
        }
        return has;
    }

    /** 获得“我的-》设置-》显示类别”设置的显示类别 */
    public String[] getDynamicTypeList() {
        return getDynamicType().split(",");
    }

    public String getDynamicType() {
        return getValue(KEY_DYNAMIC_TYPE, DataType.WEIGHT.getType());
    }

    public void resetDynamicType() {
        setDynamicType(DataType.BP.getType());
        setDynamicType(DataType.BSL.getType());
    }

    private static final String KEY_NETWORK_STATE = "cur_network_state"; // 网络状态key
    public static final int STATE_NETWORK_DEFAULT = 0; // 默认状态
    public static final int STATE_NETWORK_WIFI = 1; // 仅wifi

    /**
     * 获取网络状态
     */
    public int getNetworkState() {
        return getValue(KEY_NETWORK_STATE, STATE_NETWORK_DEFAULT);
    }

    /**
     * 设置网络状态
     */
    public void setNetworkState(int state) {
        setValue(KEY_NETWORK_STATE, state);
    }

}
