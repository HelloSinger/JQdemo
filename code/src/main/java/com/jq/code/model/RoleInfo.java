package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 用户信息类
 */
public class RoleInfo implements Parcelable {

    /**
     *
     */
    public static final String ROLE_KEY = RoleInfo.class.getSimpleName();
    private int id;
    private int account_id;
    private String nickname;
    private int height;
    private String sex;
    private String birthday;
    private float weight_goal;
    private String create_time;
    private String modify_time;
    private int current_state;
    private String period_time;
    private String icon_content;
    private String icon_image_path;
    private String icon_image_create_time;
    private String sync_time;
    private float weight_init;
    /**
     * 1代表孕妇， 0代表非孕妇
     */
    private int role_type;
    private String useId;


    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public int getRole_type() {
        return role_type;
    }

    public void setRole_type(int role_type) {
        this.role_type = role_type;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "id=" + id +
                ", account_id=" + account_id +
                ", nickname='" + nickname + '\'' +
                ", height=" + height +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", weight_goal=" + weight_goal +
                ", create_time='" + create_time + '\'' +
                ", modify_time='" + modify_time + '\'' +
                ", current_state=" + current_state +
                ", period_time='" + period_time + '\'' +
                ", icon_content='" + icon_content + '\'' +
                ", icon_image_path='" + icon_image_path + '\'' +
                ", icon_image_create_time='" + icon_image_create_time + '\'' +
                ", sync_time='" + sync_time + '\'' +
                ", weight_init=" + weight_init +
                '}';
    }

    public float getWeight_init() {
        return weight_init;
    }

    public void setWeight_init(float weight_init) {
        this.weight_init = weight_init;
    }

    public float getWeight_goal() {
        return weight_goal;
    }

    public void setWeight_goal(float weight_goal) {
        this.weight_goal = weight_goal;
    }

    public String getSync_time() {
        return sync_time;
    }

    public void setSync_time(String sync_time) {
        this.sync_time = sync_time;
    }

    public String getIcon_image_path() {
        return icon_image_path;
    }

    public void setIcon_image_path(String icon_image_path) {
        this.icon_image_path = icon_image_path;
    }

    public String getIcon_image_create_time() {
        return icon_image_create_time;
    }

    public void setIcon_image_create_time(String icon_image_create_time) {
        this.icon_image_create_time = icon_image_create_time;
    }

    public String getIcon_content() {
        return icon_content;
    }

    public void setIcon_content(String icon_content) {
        this.icon_content = icon_content;
    }

    public String getPeriod_time() {
        return period_time;
    }

    public void setPeriod_time(String period_time) {
        this.period_time = period_time;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getModify_time() {
        return modify_time;
    }

    public void setModify_time(String modify_time) {
        this.modify_time = modify_time;
    }

    public int getCurrent_state() {
        return current_state;
    }

    public void setCurrent_state(int current_state) {
        this.current_state = current_state;
    }

    public RoleInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.account_id);
        dest.writeString(this.nickname);
        dest.writeInt(this.height);
        dest.writeString(this.sex);
        dest.writeString(this.birthday);
        dest.writeFloat(this.weight_goal);
        dest.writeString(this.create_time);
        dest.writeString(this.modify_time);
        dest.writeInt(this.current_state);
        dest.writeString(this.period_time);
        dest.writeString(this.icon_content);
        dest.writeString(this.icon_image_path);
        dest.writeString(this.icon_image_create_time);
        dest.writeString(this.sync_time);
        dest.writeFloat(this.weight_init);
        dest.writeInt(this.role_type);
    }

    protected RoleInfo(Parcel in) {
        this.id = in.readInt();
        this.account_id = in.readInt();
        this.nickname = in.readString();
        this.height = in.readInt();
        this.sex = in.readString();
        this.birthday = in.readString();
        this.weight_goal = in.readFloat();
        this.create_time = in.readString();
        this.modify_time = in.readString();
        this.current_state = in.readInt();
        this.period_time = in.readString();
        this.icon_content = in.readString();
        this.icon_image_path = in.readString();
        this.icon_image_create_time = in.readString();
        this.sync_time = in.readString();
        this.weight_init = in.readFloat();
        this.role_type = in.readInt();
    }

    public static final Creator<RoleInfo> CREATOR = new Creator<RoleInfo>() {
        @Override
        public RoleInfo createFromParcel(Parcel source) {
            return new RoleInfo(source);
        }

        @Override
        public RoleInfo[] newArray(int size) {
            return new RoleInfo[size];
        }
    };
}
