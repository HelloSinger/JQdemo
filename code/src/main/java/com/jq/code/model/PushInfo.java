package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PushInfo implements Parcelable {

    private int company_id;
    private String sex;
    private int id;
    private String cover;
    private String localUrl;
    private String uri;
    private int categories;
    private String title;
    private long ts;
    private boolean preview;
    private int ncomments;
    private int nlikes;
    private String fav;
    private int pv ;

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    @Override
    public String toString() {
        return "PushInfo{" +
                "company_id=" + company_id +
                ", sex='" + sex + '\'' +
                ", id=" + id +
                ", cover='" + cover + '\'' +
                ", localUrl='" + localUrl + '\'' +
                ", uri='" + uri + '\'' +
                ", categories=" + categories +
                ", title='" + title + '\'' +
                ", ts=" + ts +
                ", preview=" + preview +
                ", ncomments=" + ncomments +
                ", nlikes=" + nlikes +
                ", fav='" + fav + '\'' +
                '}';
    }

    public String getFav() {
        return fav;
    }

    public boolean isFaved() {
        return !(fav == null || fav.equals("n"));
    }

    public void setFaved(boolean isFaved) {
        fav = isFaved ? "y" : "n";
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public int getNlikes() {
        return nlikes;
    }

    public void setNlikes(int nlikes) {
        this.nlikes = nlikes;
    }

    public int getNcomments() {
        return ncomments;
    }

    public void setNcomments(int ncomments) {
        this.ncomments = ncomments;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PushInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.company_id);
        dest.writeString(this.sex);
        dest.writeInt(this.id);
        dest.writeString(this.cover);
        dest.writeString(this.localUrl);
        dest.writeString(this.uri);
        dest.writeInt(this.categories);
        dest.writeString(this.title);
        dest.writeLong(this.ts);
        dest.writeByte(this.preview ? (byte) 1 : (byte) 0);
        dest.writeInt(this.ncomments);
        dest.writeInt(this.nlikes);
        dest.writeString(this.fav);
        dest.writeInt(this.pv);
    }

    protected PushInfo(Parcel in) {
        this.company_id = in.readInt();
        this.sex = in.readString();
        this.id = in.readInt();
        this.cover = in.readString();
        this.localUrl = in.readString();
        this.uri = in.readString();
        this.categories = in.readInt();
        this.title = in.readString();
        this.ts = in.readLong();
        this.preview = in.readByte() != 0;
        this.ncomments = in.readInt();
        this.nlikes = in.readInt();
        this.fav = in.readString();
        this.pv = in.readInt() ;
    }

    public static final Creator<PushInfo> CREATOR = new Creator<PushInfo>() {
        @Override
        public PushInfo createFromParcel(Parcel source) {
            return new PushInfo(source);
        }

        @Override
        public PushInfo[] newArray(int size) {
            return new PushInfo[size];
        }
    };
}
