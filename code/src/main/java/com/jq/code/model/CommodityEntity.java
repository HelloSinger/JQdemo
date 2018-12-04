package com.jq.code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/12/13.
 */

public class CommodityEntity implements Parcelable {

    String coupon;
    float commission;
    float discounted_price;
    String title;
    long ts;
    String coupon_url;
    String link;
    float discount;
    float price;
    String img;

    @Override
    public String toString() {
        return "CommodityEntity{" +
                "coupon='" + coupon + '\'' +
                ", commission=" + commission +
                ", discounted_price=" + discounted_price +
                ", title='" + title + '\'' +
                ", ts=" + ts +
                ", coupon_url='" + coupon_url + '\'' +
                ", link='" + link + '\'' +
                ", discount=" + discount +
                ", price=" + price +
                ", img='" + img + '\'' +
                '}';
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public float getCommission() {
        return commission;
    }

    public void setCommission(float commission) {
        this.commission = commission;
    }

    public float getDiscounted_price() {
        return discounted_price;
    }

    public void setDiscounted_price(float discounted_price) {
        this.discounted_price = discounted_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getCoupon_url() {
        return coupon_url;
    }

    public void setCoupon_url(String coupon_url) {
        this.coupon_url = coupon_url;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.coupon);
        dest.writeFloat(this.commission);
        dest.writeFloat(this.discounted_price);
        dest.writeString(this.title);
        dest.writeLong(this.ts);
        dest.writeString(this.coupon_url);
        dest.writeString(this.link);
        dest.writeFloat(this.discount);
        dest.writeFloat(this.price);
        dest.writeString(this.img);
    }

    public CommodityEntity() {
    }

    protected CommodityEntity(Parcel in) {
        this.coupon = in.readString();
        this.commission = in.readFloat();
        this.discounted_price = in.readFloat();
        this.title = in.readString();
        this.ts = in.readLong();
        this.coupon_url = in.readString();
        this.link = in.readString();
        this.discount = in.readFloat();
        this.price = in.readFloat();
        this.img = in.readString();
    }

    public static final Creator<CommodityEntity> CREATOR = new Creator<CommodityEntity>() {
        @Override
        public CommodityEntity createFromParcel(Parcel source) {
            return new CommodityEntity(source);
        }

        @Override
        public CommodityEntity[] newArray(int size) {
            return new CommodityEntity[size];
        }
    };
}
