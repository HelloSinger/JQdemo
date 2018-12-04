package com.jq.code.code.business;

import android.content.Context;

import com.jq.code.code.db.PushDataDB;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.model.CategoryInfo;
import com.jq.code.model.PushInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;

/**
 * Created by hfei on 2016/5/9.
 */
public class PushParser extends PrefsUtil {

    private static PushParser instance;
    private PushDataDB mPushDataDBUtil;

    /**
     * 析构函数
     *
     * @param context
     */
    private PushParser(Context context) {
        super(context);
        mPushDataDBUtil = PushDataDB.getInstance(context);
    }

    public static PushParser getInstance(Context context) {
        if (instance == null) {
            synchronized (PushParser.class) {
                if(null == instance) {
                    instance = new PushParser(context.getApplicationContext());
                }
            }
        }
        return instance;
    }


    private static final String KEY_PUSH_STATE = "cur_push_state"; // push状态key
    public static final int STATE_PUSH_NEW = 0; // 新的push
    public static final int STATE_PUSH_OPENED = 1; // push被打开

    /**
     * 获取APP状态
     */
    public int getPushstate() {
        return getValue(KEY_PUSH_STATE, STATE_PUSH_OPENED);
    }

    /**
     * 设置APP状态
     */
    public void setPushstate(int state) {
        setValue(KEY_PUSH_STATE, state);
    }


    /**
     * 设置推送标签信息
     */
    public void setCategoryInfo(ArrayList<CategoryInfo> categoryInfos) {
        setValue("cur_categoryInfo", JsonMapper.toJson(categoryInfos));
    }

    /**
     * 得到推送标签信息
     */
    public ArrayList<CategoryInfo> getCategoryInfo() {
        String cate = getValue("cur_categoryInfo", "{}");
        return JsonMapper.fromJson(cate, new TypeReference<ArrayList<CategoryInfo>>() {});
    }


    /**
     * 保存推送信息
     *
     * @param info
     */
    public void storePushInfo(PushInfo info) {
        mPushDataDBUtil.createPushInfo(info);
    }

    /**
     * 改推送信息是否存在
     * @param info
     * @return true 存在 false不存在
     */
    public boolean isExist(PushInfo info){
        return mPushDataDBUtil.isExist(info);
    }
}
