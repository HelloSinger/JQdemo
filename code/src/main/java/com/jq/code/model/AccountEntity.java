package com.jq.code.model;


public class AccountEntity {

    public static final String TYPE_PHONE = "phone";
    public static final String TYPE_QQ = "qq";
    public static final String TYPE_SINA = "sina_blog";
    public static final String TYPE_WECHAT = "weixin";
    public static final String TYPE_HAIER = "haier";
    public final static int company_id = 15;

    private String email;
    private String phone;
    private String last_login;
    private int days;
    private int weight_unit;
    private String qq;
    private String sina_blog;
    private String weixin;
    private String haier ;
    /** 个性签名 */
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHaier() {
        return haier;
    }

    public void setHaier(String haier) {
        this.haier = haier;
    }

    private int grade_id;
    private String register_time;
    private int id;
    private String password;
    private int length_unit;
    private String verify_code;

    public String getMtypes() {
        return mtypes;
    }

    public void setMtypes(String mtypes) {
        this.mtypes = mtypes;
    }

    private String type; //账号类型为qq, sina_blog,weixin或phone
    private String access_token; //qq或新浪的access_token
    private String wdata; //flag: 存在时返回account,role,remind数据，否则只返回account数据
    private String mtypes; //在动态显示的类别
    @Override
    public String toString() {
        return "AccountEntity{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", last_login='" + last_login + '\'' +
                ", days=" + days +
                ", weight_unit=" + weight_unit +
                ", qq='" + qq + '\'' +
                ", sina_blog='" + sina_blog + '\'' +
                ", weixin='" + weixin + '\'' +
                ", grade_id=" + grade_id +
                ", register_time='" + register_time + '\'' +
                ", id=" + id +
                ", password='" + password + '\'' +
                ", length_unit=" + length_unit +
                ", verify_code='" + verify_code + '\'' +
                ", type=" + type +
                ", access_token='" + access_token + '\'' +
                ", wdata='" + wdata + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getWdata() {
        return wdata;
    }

    public void setWdata(String wdata) {
        this.wdata = wdata;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getWeight_unit() {
        return weight_unit;
    }

    public void setWeight_unit(int weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getSina_blog() {
        return sina_blog;
    }

    public void setSina_blog(String sina_blog) {
        this.sina_blog = sina_blog;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLength_unit() {
        return length_unit;
    }

    public void setLength_unit(int length_unit) {
        this.length_unit = length_unit;
    }

}
