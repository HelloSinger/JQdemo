package com.jq.btc.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.jq.code.model.WeightEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 个人信息sp储存工具
 * Created by shi shuyang on 2016/8/17.
 */
public class SpUtils {

    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences.Editor mEditor = null;
    private static SpUtils spUtils = null;

    public static final String SHAREDPREFERENCES_USERINFO = "user_info";//sharedPreferences总的名称

    public static SpUtils getInstance(Context context) {
        if (spUtils == null) {
            synchronized (SpUtils.class) {
                if (spUtils == null) {
                    spUtils = new SpUtils(context);
                }
            }
        }
        return spUtils;
    }


    private SpUtils(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(SHAREDPREFERENCES_USERINFO, Context.MODE_PRIVATE);
            mEditor = sharedPreferences.edit();
        }
    }

    private SpUtils() {

    }

    //清除sp
    public void clearSpUserinfo() {
        mEditor.clear();
        mEditor.commit();
    }

    public boolean setIsFirst(boolean b) {
        mEditor.putBoolean("isFirst", b);
        return mEditor.commit();
    }

    public boolean getIsFirst() {
        return sharedPreferences.getBoolean("isFirst", false);
    }

    public void cleanMak() {
        mEditor.remove("isFirst");
        mEditor.commit();
    }

    //userid
    public boolean setUserid(String userid) {
        mEditor.putString("USE_ID", userid);
        return mEditor.commit();
    }

    public String getUserid() {
        return sharedPreferences.getString("USE_ID", "");
    }

    public boolean isDiaLogAddUser(boolean b) {
        mEditor.putBoolean("isDialogAdd", b);
        return mEditor.commit();
    }

    public boolean getIsDialogAdd() {
        return sharedPreferences.getBoolean("isDialogAdd", false);
    }

    public void cleanDialog() {
        mEditor.remove("isDialogAdd");
        mEditor.commit();
    }

    public void putBean(WeightEntity obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                mEditor.putString("USER_BEAN", string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("the obj must implement Serializble");
        }

    }

    public WeightEntity getBean() {
        WeightEntity obj = null;
        try {
//            String base64 = context.getSharedPreferences(context).getString(key, "");
            String base64 = sharedPreferences.getString("USER_BEAN", "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (WeightEntity) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void cleanBean() {
        mEditor.remove("USER_BEAN");
        mEditor.commit();
    }

    public boolean putListSize(int listSize) {
        mEditor.putInt("LIST_SIZE", listSize);
        return mEditor.commit();
    }

    public Integer getListSize() {
        return sharedPreferences.getInt("LIST_SIZE", -1);
    }


    public boolean putProvince(String province) {
        mEditor.putString("PROVINCE", province);
        return mEditor.commit();
    }

    public String getProvince() {

        return sharedPreferences.getString("PROVINCE", "");
    }

    public boolean putProvinceId(String provinceId) {
        mEditor.putString("PROVINCEID", provinceId);
        return mEditor.commit();
    }

    public String getProvinceId() {

        return sharedPreferences.getString("PROVINCEID", "");
    }

    public boolean putCity(String city) {
        mEditor.putString("CITY", city);
        return mEditor.commit();
    }

    public String getCity() {
        return sharedPreferences.getString("CITY", "");
    }

    public boolean putCityId(String cityId) {
        mEditor.putString("CITYID", cityId);
        return mEditor.commit();
    }

    public String getCityId() {
        return sharedPreferences.getString("CITYID", "");
    }

    public boolean putMac(String macid) {
        mEditor.putString("MAC", macid);
        return mEditor.commit();
    }

    public String getMac() {
        return sharedPreferences.getString("MAC", "");
    }

    public boolean putPos(int pos) {
        mEditor.putInt("POS", pos);
        return mEditor.commit();
    }

    public Integer getPos() {
        return sharedPreferences.getInt("POS", -1);
    }

    public void cleanPos() {
        mEditor.remove("POS");
        mEditor.commit();
    }
    //   //手机号
//   public boolean setRealName(String realname){
//
//       mEditor.putString(SpCnostant.SP_REAL_NAME,realname);
//       return mEditor.commit();
//   }
//
//    public String getRealName(){
//        return sharedPreferences.getString(SpCnostant.SP_REAL_NAME,"");
//    }
//
//    //昵称
//    public boolean setNickName(String nickname){
//        mEditor.putString(SpCnostant.SP_USER_NICK_NAME,nickname);
//        return mEditor.commit();
//    }
//
//    public String getNickName(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_NICK_NAME,"");
//    }
//
//    //token值
//    public boolean setToken(String token){
//        mEditor.putString(SpCnostant.SP_ACCESS_TOKEN,token);
//        return mEditor.commit();
//    }
//
//    public String getToken(){
//        return sharedPreferences.getString(SpCnostant.SP_ACCESS_TOKEN,"");
//    }
//
//
//    //token值
//    public boolean setSDToken(String SDtoken){
//        mEditor.putString(SpCnostant.SP_SDACCESS_TOKEN,SDtoken);
//        return mEditor.commit();
//    }
//
//    public String getSDToken(){
//        return sharedPreferences.getString(SpCnostant.SP_SDACCESS_TOKEN,"");
//    }
//
//
//

//
//    //u+ id
//    public boolean setUOpenId(String u_open_id){
//        mEditor.putString(SpCnostant.SP_U_OPEN_ID,u_open_id);
//        return mEditor.commit();
//    }
//
//    /**
//     *   保存当前设备的 mac 地址
//     * @param macId
//     */
//    public void setMacId(String macId){
//        mEditor.putString(SpCnostant.SP_MAC_ID,macId);
//        mEditor.commit();
//    }
//
//    /**
//     *  获取当前设备的mac 地址
//     * @return
//     */
//    public String getMacId (){
//        return sharedPreferences.getString(SpCnostant.SP_MAC_ID,"");
//    }
//
//    /**
//     * 保存当前设备type_id
//     * @param fridgeTypeId
//     */
//    public void setFridgeTypeId(String fridgeTypeId){
//        mEditor.putString(SpCnostant.FRIDGE_TYPE_ID,fridgeTypeId);
//        mEditor.commit();
//    }
//
//    /**
//     *  获取当前设备type_id
//     * @return
//     */
//    public String getFridgeTypeId(){
//        return sharedPreferences.getString(SpCnostant.FRIDGE_TYPE_ID,"");
//    }
//
//    /**
//     *  保存当前设备的device_id
//     * @param deviceId
//     */
//    public void setDeviceId(String deviceId){
//        mEditor.putString(SpCnostant.DEVICE_ID,deviceId);
//        mEditor.commit();
//    }
//
//    /**
//     *  获取当前设备的 deviceId
//     * @return
//     */
//    public String getDeviceId(){
//        return sharedPreferences.getString(SpCnostant.DEVICE_ID,"");
//    }
//
//    public String getUOpenId(){
//        return sharedPreferences.getString(SpCnostant.SP_U_OPEN_ID,"");
//    }
//
//    //城市
//    public boolean setCity(String city){
//        mEditor.putString(SpCnostant.SP_USER_CITY,city);
//        return mEditor.commit();
//    }
//
//    public String getCity(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_CITY,"");
//    }
//
//    //性别
//    public boolean setSex(String sex){
//        if(TextUtils.isEmpty(sex)){
//            mEditor.putInt(SpCnostant.SP_USER_SEX,0);
//        } else if (sex.equals("male")){
//            mEditor.putInt(SpCnostant.SP_USER_SEX,1);
//        } else if (sex.equals("female")){
//            mEditor.putInt(SpCnostant.SP_USER_SEX,2);
//        } else {
//            mEditor.putInt(SpCnostant.SP_USER_SEX,0);
//        }
//
//        return mEditor.commit();
//    }
//
//    public int getSex(){
//        return sharedPreferences.getInt(SpCnostant.SP_USER_SEX,2);
//    }
//
//    //生日
//    public boolean setBirthday(String birthday){
//        mEditor.putString(SpCnostant.SP_USER_BIRTHDAY,birthday);
//        return mEditor.commit();
//    }
//
//    public String getBirthday(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_BIRTHDAY,"");
//    }
//
//    //头像
//    public boolean setHeadimg(String head_img){
//        mEditor.putString(SpCnostant.SP_USER_FACE_IMAGE,head_img);
//        return mEditor.commit();
//    }
//
//    public String getHeadimg(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_FACE_IMAGE,"");
//    }
//
//    public boolean setLogin(boolean islogin){
//        mEditor.putBoolean(SpCnostant.SP_IS_LOGIN,islogin);
//        return mEditor.commit();
//    }
//
//    public boolean getLogin(){
//        return sharedPreferences.getBoolean(SpCnostant.SP_IS_LOGIN,false);
//    }
//
//    //当前城市位置
//    public boolean setCurCityPos(int cityPos){
//        mEditor.putInt(SpCnostant.SP_USER_CURRENT_CITY_POS,cityPos);
//        return mEditor.commit();
//    }
//
//    public int getCurCityPos(){
//        return sharedPreferences.getInt(SpCnostant.SP_USER_CURRENT_CITY_POS,0);
//    }
//
//    //当前城市
//    public boolean setCurCity(String cityPos){
//        mEditor.putString(SpCnostant.SP_USER_CURRENT_CITY_POS,cityPos);
//        return mEditor.commit();
//    }
//
//    public String getCurCity(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_CURRENT_CITY_POS,"");
//    }
//    //定位城市位置
//    public boolean setLocaCityPos(int cityPos){
//        mEditor.putInt(SpCnostant.SP_USER_LOCATION_CITY_POS,cityPos);
//        return mEditor.commit();
//    }
//
//    public int getLocaCityPos(){
//        return sharedPreferences.getInt(SpCnostant.SP_USER_LOCATION_CITY_POS,0);
//    }
//    //当前省份位置
//    public boolean setCurProPos(int provincePos){
//        mEditor.putInt(SpCnostant.SP_USER_CURRENT_PROVINCE_POS,provincePos);
//        return mEditor.commit();
//    }
//
//    public int getCurProPos(){
//        return sharedPreferences.getInt(SpCnostant.SP_USER_CURRENT_PROVINCE_POS,0);
//    }
//    //当前省份
//    public boolean setCurPro(String province){
//        mEditor.putString(SpCnostant.SP_USER_CURRENT_PROVINCE,province);
//        return mEditor.commit();
//    }
//
//    public String getCurPro(){
//        return sharedPreferences.getString(SpCnostant.SP_USER_CURRENT_PROVINCE,"");
//    }
//
//    //定位省份位置
//    public boolean setLocaProPos(int provincePos){
//        mEditor.putInt(SpCnostant.SP_USER_LOCATION_PROVINCE_POS,provincePos);
//        return mEditor.commit();
//    }
//
//    public int getLocaProPos(){
//        return sharedPreferences.getInt(SpCnostant.SP_USER_LOCATION_PROVINCE_POS,0);
//    }
//
//    //是否改变城市
//    public boolean setIsChangeCity(boolean isChange){
//        mEditor.putBoolean(SpCnostant.SP_USER_IS_CHANGE_CITY,isChange);
//        return mEditor.commit();
//    }
//
//    public boolean getIsChangeCity(){
//        return sharedPreferences.getBoolean(SpCnostant.SP_USER_IS_CHANGE_CITY,false);
//    }
//
//    //发送重置密码剩余秒数
//    public boolean setRestSeconds(int seconds){
//        mEditor.putInt(SpCnostant.SP_SEND_RESET_PWD_REST_SECONDS,seconds);
//        return mEditor.commit();
//    }
//
//    public int getRestSeconds(){
//        return sharedPreferences.getInt(SpCnostant.SP_SEND_RESET_PWD_REST_SECONDS,0);
//    }
//
//    //微信登录用openid
//    public boolean setOpenId(String openid){
//
//        mEditor.putString(SpCnostant.SP_WX_OPENID,openid);
//        return mEditor.commit();
//    }
//
//    public String getOpenId(){
//        return sharedPreferences.getString(SpCnostant.SP_WX_OPENID,"");
//    }
//
//    //微信登录用openid
//    public boolean setSocial_access_token(String social_access_token){
//
//        mEditor.putString(SpCnostant.SP_WX_ACCESS_TOKEN,social_access_token);
//        return mEditor.commit();
//    }
//
//    public String getSocial_access_token(){
//        return sharedPreferences.getString(SpCnostant.SP_WX_ACCESS_TOKEN,"");
//    }
//
//    //refresh_token
//    public boolean setRefreshToken(String refresh_token){
//
//        mEditor.putString(SpCnostant.SP_REFRESH_TOKEN,refresh_token);
//        return mEditor.commit();
//    }
//
//    public String getRefreshToken(){
//        return sharedPreferences.getString(SpCnostant.SP_REFRESH_TOKEN,"");
//    }

//    public static void putIsRememberPwdStatus(Context context,String spFileName,String key,Object value) {
//        SharedPreferences sp =
//    }

//    public void resetUserInfo() {
//        setToken("");
//        setSDToken("");
//        setRefreshToken("");
//        setUserid("");
//        setUOpenId("");
//        setOpenId("");
//        setNickName("");
//        setRealName("");
//        setBirthday("");
//        setCity("");
//        setCurCity("");
//        setHeadimg("");
//        setLogin(false);
//    }
//
//    public void setUserOpenId(String openId) {
//
//    }
}
