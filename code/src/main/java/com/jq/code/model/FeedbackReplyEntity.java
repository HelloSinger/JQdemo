package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/9/7.
 */
public class FeedbackReplyEntity implements Parcelable {
    String auid;
    long adminid;
    String rtype;
    long nts;
    long ts;
    String ua;
    String content;
    long fid;
    long id;
    long account_id;

    @Override
    public String toString() {
        return "FeedbackReplyEntity{" +
                "auid='" + auid + '\'' +
                ", adminid=" + adminid +
                ", rtype='" + rtype + '\'' +
                ", nts=" + nts +
                ", ts=" + ts +
                ", ua='" + ua + '\'' +
                ", content='" + content + '\'' +
                ", fid=" + fid +
                ", id=" + id +
                ", account_id=" + account_id +
                '}';
    }


    public long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(long account_id) {
        this.account_id = account_id;
    }

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    public long getAdminid() {
        return adminid;
    }

    public void setAdminid(long adminid) {
        this.adminid = adminid;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public long getNts() {
        return nts;
    }

    public void setNts(long nts) {
        this.nts = nts;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedbackReplyEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.auid);
        dest.writeLong(this.adminid);
        dest.writeString(this.rtype);
        dest.writeLong(this.nts);
        dest.writeLong(this.ts);
        dest.writeString(this.ua);
        dest.writeString(this.content);
        dest.writeLong(this.fid);
        dest.writeLong(this.id);
        dest.writeLong(this.account_id);
    }

    protected FeedbackReplyEntity(Parcel in) {
        this.auid = in.readString();
        this.adminid = in.readLong();
        this.rtype = in.readString();
        this.nts = in.readLong();
        this.ts = in.readLong();
        this.ua = in.readString();
        this.content = in.readString();
        this.fid = in.readLong();
        this.id = in.readLong();
        this.account_id = in.readLong();
    }

    public static final Creator<FeedbackReplyEntity> CREATOR = new Creator<FeedbackReplyEntity>() {
        @Override
        public FeedbackReplyEntity createFromParcel(Parcel source) {
            return new FeedbackReplyEntity(source);
        }

        @Override
        public FeedbackReplyEntity[] newArray(int size) {
            return new FeedbackReplyEntity[size];
        }
    };
}
