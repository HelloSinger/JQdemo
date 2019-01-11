package com.jq.code.code.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hfei on 2015/10/28.
 */
public class PrefsUtil {

    public static final String TAG = "PrefsUtil";
    public static final String SP_NAME = "jq";
    private static SharedPreferences sprefer = null;
    private static SharedPreferences.Editor sp_edit = null;

    /**
     * 析构函数
     */
    public PrefsUtil(Context context) {
        sprefer = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp_edit = sprefer.edit();
    }

    private static PrefsUtil prefsUtil;

    public static PrefsUtil newInstance(Context context) {
        if (prefsUtil == null) {
            synchronized (PrefsUtil.class) {
                if (prefsUtil == null) {
                    prefsUtil = new PrefsUtil(context);
                }
            }
        }
        return prefsUtil;
    }

    /**
     * 获得键值对
     *
     * @param key
     * @param value
     * @return
     */
    public int getValue(String key, int value) {
        if (sprefer != null) {
            return sprefer.getInt(key, value);
        }
        return value;
    }

    /**
     * 获得键值对
     *
     * @param key
     * @param value
     * @return
     */
    public String getValue(String key, String value) {
        if (sprefer != null) {
            return sprefer.getString(key, value);
        }
        return value;
    }

    /**
     * 获得键值对
     *
     * @param key
     * @param value
     * @return
     */
    public long getValue(String key, long value) {
        if (sprefer != null) {
            return sprefer.getLong(key, value);
        }
        return value;
    }

    /**
     * 获得键值对
     *
     * @param key
     * @param value
     * @return
     */
    public float getValue(String key, float value) {
        if (sprefer != null) {
            return sprefer.getFloat(key, value);
        }
        return value;
    }

    public void clear() {
        if (sp_edit != null) {
            sp_edit.clear();
            sp_edit.commit();
        }
    }

    /**
     * 获得键值对
     *
     * @param key
     * @param value
     * @return
     */
    public boolean getValue(String key, boolean value) {
        if (sprefer != null) {
            return sprefer.getBoolean(key, value);
        }
        return value;
    }

    /**
     * 设置键值対
     *
     * @param key
     * @param value
     */
    public void setValue(String key, int value) {
        if (sp_edit != null) {
            sp_edit.putInt(key, value);
            sp_edit.commit();
        }
    }

    /**
     * 设置键值対
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        if (sp_edit != null) {
            sp_edit.putString(key, value);
            sp_edit.commit();
        }
    }

    /**
     * 删除某一条
     * @param key
     */
    public void reMove(String key) {
        if (sp_edit != null) {
            sp_edit.remove(key);
            sp_edit.commit();
        }
    }

    /**
     * 设置键值対
     *
     * @param key
     * @param value
     */
    public void setValue(String key, long value) {
        if (sp_edit != null) {
            sp_edit.putLong(key, value);
            sp_edit.commit();
        }
    }

    /**
     * 设置键值対
     *
     * @param key
     * @param value
     */
    public void setValue(String key, float value) {
        if (sp_edit != null) {
            sp_edit.putFloat(key, value);
            sp_edit.commit();
        }
    }

    /**
     * 设置键值対
     *
     * @param key
     * @param value
     */
    public void setValue(String key, boolean value) {
        if (sp_edit != null) {
            sp_edit.putBoolean(key, value);
        }
    }

    /**
     * 检测当前软件版本
     */
    public static String getVersionName(Context context) throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(
                context.getPackageName(), 0);
        String version = packInfo.versionName;
        return version;
    }

    /**
     * MD5加密
     *
     * @param string
     * @return
     */
    public static String md5(String string) {

        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();

    }
}
