package com.jq.code.code.business;

import android.content.Context;
import android.text.TextUtils;

import com.jq.code.code.db.RoleDB;
import com.jq.code.code.db.WeightDataDB;
import com.jq.code.code.util.PrefsUtil;
import com.jq.code.model.AccountEntity;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;
import com.jq.code.model.haierLogon.AccessToken;
import com.jq.code.model.haierLogon.LoginResult;

import java.util.ArrayList;

/**
 * Created by hfei on 2016/5/9.
 */
public class Account extends PrefsUtil {

    private static Account instance;
    private Context context;
    private RoleDB mRoleDBUtil;
    /** 保存已经加载数据完成的账号ID，多个账号用逗号分隔，
     * 例如 “账号1， 账号2， 账号3，...”*/
    public static final String KEY_ACCOUNT_LOAD_DATA_FINISHED = "KEY_ACCOUNT_LOAD_DATA_FINISHED";

    public static Account getInstance(Context context) {
        if (instance == null) {
            synchronized (Account.class) {
                if(null == instance) {
                    instance = new Account(context.getApplicationContext());
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
    public Account(Context context) {
        super(context);
        this.context = context;
        mRoleDBUtil = RoleDB.getInstance(context);
    }

    /**
     * 更新所有角色
     *
     * @param roles
     */
    public void updateRoles(ArrayList<RoleInfo> roles) {
        if (roles == null || roles.size() == 0)
            return;

        int mainRoleId = getMainRoleInfo().getId();
        for(RoleInfo roleInfo:roles) {
            if(mainRoleId == roleInfo.getId()) {
                setMainRoleInfo(roleInfo);
                break;
            }
        }

        //过滤本地有服务器没有的角色
        ArrayList<RoleInfo> localRoles = findRoleALL();
        for (int i = 0; i < localRoles.size(); i++) {
            boolean has = false;
            RoleInfo roleInfo = localRoles.get(i);
            for (int j = 0; j < roles.size(); j++) {
                if (roleInfo.getId() == roles.get(j).getId()) {
                    has = true;
                }
            }
            if (!has) {
                if (roleInfo.getId() == getRoleInfo().getId()) {
                    setRoleInfo(getMainRoleInfo());
                }
                clearAllData(roleInfo);
            }
        }
        //保存服务器数据
        storeRoles(roles);
    }

    /**
     * 查找主角色意外的所有角色
     *
     * @return
     */
    public ArrayList<RoleInfo> findRoles() {
        ArrayList<RoleInfo> roleInfos = findRoleALL();
        if (roleInfos != null && roleInfos.size() > 0) {
            roleInfos.remove(0);
            return roleInfos;
        }
        return null;
    }

    /**
     * 查找所有角色
     *
     * @return
     */
    public ArrayList<RoleInfo> findRoleALL() {
        return mRoleDBUtil.findRoleAllByAccountId(getAccountInfo().getId());
    }

    /**
     * 匹配该重量角色
     *
     * @param weight
     * @return
     */
    public ArrayList<RoleInfo> matchRole(WeightEntity weight) {
        // 临时角色列表
        ArrayList<RoleInfo> tmpRoleInfos = new ArrayList<RoleInfo>();
        // 查找所有角色
        ArrayList<RoleInfo> roleInfos = Account.getInstance(context).findRoleALL();
//        // 遍历所有角色查找匹配角色,如果该角色数据体重与测量体重相差范围在+-3kg以内。则认为是同一个角色
//        for (int i = 0; i < roleInfos.size(); i++) {
//            boolean isMatch = false;
//            WeightEntity dataInfo = WeightDataDB.getInstance(context).findLastRoleDataByRoleId(roleInfos.get(i));
//            if (dataInfo != null) {
//                if (Math.abs(dataInfo.getWeight() - weight.getWeight()) <= 3) {
//                    isMatch = true;
//                    if (weight.getR1() > 0 && dataInfo.getR1() > 0) {
//                        isMatch = Math.abs(dataInfo.getR1() - weight.getR1()) <= 15;
//                    }
//                    if (isMatch)
//                        tmpRoleInfos.add(roleInfos.get(i));
//                }
//            }
//        }
//        return tmpRoleInfos;
        return roleInfos;
    }


    /**
     * 清除该角色信息及改角色所有称量数据
     *
     * @param roleInfo
     */
    public void clearAllData(RoleInfo roleInfo) {
        mRoleDBUtil.removeRole(roleInfo);
        WeightDataDB.getInstance(context).removeRoleDataByRoleId(roleInfo.getId());
    }

    private static final String KEY_APP_STATE = "cur_app_state"; // app打开状态key
    public static final int STATE_DEFAULT = 0; // 默认状态
    public static final int STATE_OPEN_APP_FIRST = 1; // app第一次打开
    public static final int STATE_LOGIN_APP_FIRST = 2; // app第一次登陆

    /**
     * 判断APP是否第一次被打开
     *
     * @return
     */
    public boolean isAppOpenFirst() {
        return getValue(KEY_APP_STATE, STATE_DEFAULT) == STATE_DEFAULT;
    }

    /**
     * 设置APP已经打开过了
     */
    public void setAppOpened() {
        setAppstate(STATE_OPEN_APP_FIRST);
    }

    /**
     * 设置APP状态
     */
    public void setAppstate(int state) {
        setValue(KEY_APP_STATE, state);
    }


    /**
     * 判断APP是否为第一次登录
     *
     * @return
     */
    public boolean isLoginFirst() {
        return getValue(KEY_APP_STATE, STATE_DEFAULT) != STATE_LOGIN_APP_FIRST;
    }

    /**
     * 设置APP已经登录过了
     */
    public void setAppLogined() {
        setAppstate(STATE_LOGIN_APP_FIRST);
    }

    public void setLoadFinishedAccounts(String accountId) {
        String accounts = getValue(KEY_ACCOUNT_LOAD_DATA_FINISHED, "").trim();
        if(TextUtils.isEmpty(accounts)) {
            setValue(KEY_ACCOUNT_LOAD_DATA_FINISHED, accountId);
        } else {
            setValue(KEY_ACCOUNT_LOAD_DATA_FINISHED, accounts + "," + accountId);
        }
    }

    public String getLoadFinishedAccounts() {
        String accounts = getValue(KEY_ACCOUNT_LOAD_DATA_FINISHED, "").trim();
        return accounts;
    }

    /**
     * 设置当前帐号信息
     */
    public void setAccountInfo(AccountEntity account) {
        setValue("cur_account_info", JsonMapper.toJson(account));
    }

    /**
     * 是否当前账号数据已加载
     * @return true 已加载， false 未加载
     */
    public boolean isCurrentAccountDataLoaded() {
        int accountId = getAccountInfo().getId();
        // 是否账号数据已加载？
        boolean accountLoaded = false;
        String acs = getLoadFinishedAccounts();
        try {
            String[] strs = acs.split(",");
            for (String s : strs) {
                int ia = Integer.parseInt(s);
                if (accountId == ia) {
                    accountLoaded = true;
                    break;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return  accountLoaded;
    }

    /**
     * 得到当前帐号信息
     */
    public AccountEntity getAccountInfo() {
        return JsonMapper.fromJson(getValue("cur_account_info", "{}"), AccountEntity.class);
    }


    /**
     * 设置当前角色信息
     */
    public void setRoleInfo(RoleInfo role) {
        setValue("cur_role_info", JsonMapper.toJson(role));
    }

    /**
     * 得到当前角色信息
     */
    public RoleInfo getRoleInfo() {
        return JsonMapper.fromJson(getValue("cur_role_info", "{}"), RoleInfo.class);
    }

    /**
     * 设置当前角色信息
     */
    public void setMainRoleInfo(RoleInfo role) {
        setValue("cur_main_role_info", JsonMapper.toJson(role));
    }

    /**
     * 得到当前角色信息
     */
    public RoleInfo getMainRoleInfo() {
        return JsonMapper.fromJson(getValue("cur_main_role_info", "{}"), RoleInfo.class);
    }
    /**
     * 设置应用级token
     */
    public void setAccessToken(AccessToken token) {
        setValue("cur_access_token", JsonMapper.toJson(token));
    }
    /**
     * 获得应用级token
     */
    public AccessToken getAccessToken() {
        return JsonMapper.fromJson(getValue("cur_access_token", "{}"), AccessToken.class);
    }
    /**
     * 设置应用级token
     */
    public void setLoginResult(LoginResult loginResult) {
        setValue("cur_login_result", JsonMapper.toJson(loginResult));
    }
    /**
     * 获得应用级token
     */
    public LoginResult getLoginResult() {
        return JsonMapper.fromJson(getValue("cur_login_result", "{}"), LoginResult.class);
    }
    /**
    /**
     * 是否该角色为主角色
     *
     * @return
     */
    public boolean isMainRole(RoleInfo roleInfo) {
        if (getMainRoleInfo() == null || roleInfo == null) return false;
        return getMainRoleInfo().getId() == roleInfo.getId();
    }

    /**
     * 是否该角色为主角色
     *
     * @return
     */
    public boolean isMainRole() {
        return getMainRoleInfo().getId() == getRoleInfo().getId();
    }

    /**
     * 判断是否有主角色
     *
     * @return
     */
    public boolean hasMainRole() {
        return getMainRoleInfo().getId() > 0;
    }

    /**
     * 判断是否是访客
     *
     * @return
     */
    public boolean isVisitor() {
        return getMainRoleInfo().getId() == -1;
    }

    /**
     * 根据角色id获取角色
     *
     * @param roleId
     * @return
     */
    public RoleInfo findRole(long account_id, long roleId) {
        return mRoleDBUtil.findRoleById(account_id, roleId);
    }

    /**
     * 保存角色信息到数据库
     */
    public void storeRoles(ArrayList<RoleInfo> roleInfo) {
        mRoleDBUtil.createRole(roleInfo);
    }

    /**
     * 保存角色信息到数据库
     */
    public void storeRole(RoleInfo roleInfo) {
        mRoleDBUtil.createRole(roleInfo);
    }

    /**
     * 更新角色信息
     *
     * @param roleInfo
     */
    public void updateRole(RoleInfo roleInfo) {
        mRoleDBUtil.modifyRole(roleInfo);
    }

    /**
     * 是否是UID登录
     *
     * @return
     */
    public boolean isUIDLogin() {
        return getAccountInfo().getType().equals(AccountEntity.TYPE_PHONE);
    }

    /**
     * 是否是QQ登录
     *
     * @return
     */
    public boolean isQQLogin() {
        return getAccountInfo().getType().equals(AccountEntity.TYPE_QQ);
    }

    /**
     * 是否是Sina登录
     *
     * @return
     */
    public boolean isSinaLogin() {
        return getAccountInfo().getType().equals(AccountEntity.TYPE_SINA);
    }

    public boolean isQQBound() {
        if (getAccountInfo().getQq() == null) {
            return false;
        } else {
            return !(getAccountInfo().getQq().equals("0") || getAccountInfo().getQq().trim().length() == 0);
        }

    }

    public boolean isSinaBound() {
        if (getAccountInfo().getSina_blog() == null) {
            return false;
        } else {
            return !(getAccountInfo().getSina_blog().equals("0") || getAccountInfo().getSina_blog().trim().length() == 0);
        }

    }

    public boolean isPhoneBound() {
        String phone = getAccountInfo().getPhone();
        if (phone == null) {
            return false;
        } else {
            return !(phone.equals("0") || phone.trim().length() == 0);
        }
    }

    public boolean isWeixinBound() {
        if (getAccountInfo().getWeixin() == null) {
            return false;
        } else {
            return !(getAccountInfo().getWeixin().equals("0") || getAccountInfo().getWeixin().trim().length() == 0);
        }


    }


    /**
     * 判断账号是否登陆了
     *
     * @return true 表示登录过，false表示没有
     */
    public boolean isAccountLogined() {
        return getAccountInfo().getId() != 0;
    }
    public String getCurrVersion(){
        return getValue("curr_version_name",null) ;
    }
    public void setCurrVersion(String versionName){
        setValue("curr_version_name",versionName);
    }
    public void clearPreferences(){
        clear();
    }
}
