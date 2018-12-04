package com.jq.code.model.sport;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/13.
 */

public class BiteEnty implements Parcelable {
    private long id;
    private String name;
    private boolean is_liquid;
    private float calory;
    private boolean isCheck;
    private String units;
    private List<BiteUnit> unitList;
    private String brand;
    private float niacin;
    private float natrium;
    private float copper;
    private float zinc;
    private float selenium;
    private String type;
    private float fat;
    private float magnesium;
    private float calcium;
    private float kalium;
    private float iron;
    private float cholesterol;
    private float weight;
    private String thumb_image_url;
    private float fiber_dietary;
    private float lactoflavin;
    private float vitamin_a;
    private int usedegree;
    private float vitamin_e;
    private float protein;
    private float vitamin_c;
    private float manganese;
    private float carbohydrate;
    private float thiamine;
    private float iodine;
    private long update_time;

    public long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(long update_time) {
        this.update_time = update_time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean is_liquid() {
        return is_liquid;
    }

    public void setIs_liquid(boolean is_liquid) {
        this.is_liquid = is_liquid;
    }

    public float getCalory() {
        return calory;
    }

    public void setCalory(float calory) {
        this.calory = calory;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public List<BiteUnit> getUnitList() {
        return unitList;
    }

    public void setUnitList(List<BiteUnit> unitList) {
        this.unitList = unitList;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public float getNiacin() {
        return niacin;
    }

    public void setNiacin(float niacin) {
        this.niacin = niacin;
    }

    public float getNatrium() {
        return natrium;
    }

    public void setNatrium(float natrium) {
        this.natrium = natrium;
    }

    public float getCopper() {
        return copper;
    }

    public void setCopper(float copper) {
        this.copper = copper;
    }

    public float getZinc() {
        return zinc;
    }

    public void setZinc(float zinc) {
        this.zinc = zinc;
    }

    public float getSelenium() {
        return selenium;
    }

    public void setSelenium(float selenium) {
        this.selenium = selenium;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getMagnesium() {
        return magnesium;
    }

    public void setMagnesium(float magnesium) {
        this.magnesium = magnesium;
    }

    public float getCalcium() {
        return calcium;
    }

    public void setCalcium(float calcium) {
        this.calcium = calcium;
    }

    public float getKalium() {
        return kalium;
    }

    public void setKalium(float kalium) {
        this.kalium = kalium;
    }

    public float getIron() {
        return iron;
    }

    public void setIron(float iron) {
        this.iron = iron;
    }

    public float getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(float cholesterol) {
        this.cholesterol = cholesterol;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getThumb_image_url() {
        return thumb_image_url;
    }

    public void setThumb_image_url(String thumb_image_url) {
        this.thumb_image_url = thumb_image_url;
    }

    public float getFiber_dietary() {
        return fiber_dietary;
    }

    public void setFiber_dietary(float fiber_dietary) {
        this.fiber_dietary = fiber_dietary;
    }

    public float getLactoflavin() {
        return lactoflavin;
    }

    public void setLactoflavin(float lactoflavin) {
        this.lactoflavin = lactoflavin;
    }

    public float getVitamin_a() {
        return vitamin_a;
    }

    public void setVitamin_a(float vitamin_a) {
        this.vitamin_a = vitamin_a;
    }

    public int getUsedegree() {
        return usedegree;
    }

    public void setUsedegree(int usedegree) {
        this.usedegree = usedegree;
    }

    public float getVitamin_e() {
        return vitamin_e;
    }

    public void setVitamin_e(float vitamin_e) {
        this.vitamin_e = vitamin_e;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getVitamin_c() {
        return vitamin_c;
    }

    public void setVitamin_c(float vitamin_c) {
        this.vitamin_c = vitamin_c;
    }

    public float getManganese() {
        return manganese;
    }

    public void setManganese(float manganese) {
        this.manganese = manganese;
    }

    public float getCarbohydrate() {
        return carbohydrate;
    }

    public void setCarbohydrate(float carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    public float getThiamine() {
        return thiamine;
    }

    public void setThiamine(float thiamine) {
        this.thiamine = thiamine;
    }

    public float getIodine() {
        return iodine;
    }

    public void setIodine(float iodine) {
        this.iodine = iodine;
    }

    @Override
    public String toString() {
        return "BiteEnty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", is_liquid=" + is_liquid +
                ", calory=" + calory +
                ", isCheck=" + isCheck +
                ", units='" + units + '\'' +
                ", unitList=" + unitList +
                ", brand='" + brand + '\'' +
                ", niacin=" + niacin +
                ", natrium=" + natrium +
                ", copper=" + copper +
                ", zinc=" + zinc +
                ", selenium=" + selenium +
                ", type='" + type + '\'' +
                ", fat=" + fat +
                ", magnesium=" + magnesium +
                ", calcium=" + calcium +
                ", kalium=" + kalium +
                ", iron=" + iron +
                ", cholesterol=" + cholesterol +
                ", weight=" + weight +
                ", thumb_image_url='" + thumb_image_url + '\'' +
                ", fiber_dietary=" + fiber_dietary +
                ", lactoflavin=" + lactoflavin +
                ", vitamin_a=" + vitamin_a +
                ", usedegree=" + usedegree +
                ", vitamin_e=" + vitamin_e +
                ", protein=" + protein +
                ", vitamin_c=" + vitamin_c +
                ", manganese=" + manganese +
                ", carbohydrate=" + carbohydrate +
                ", thiamine=" + thiamine +
                ", iodine=" + iodine +
                ", update_time=" + update_time +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BiteEnty biteEnty = (BiteEnty) o;

        return id == biteEnty.id;

    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }


    public BiteEnty() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.is_liquid ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.calory);
        dest.writeByte(this.isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this.units);
        dest.writeList(this.unitList);
        dest.writeString(this.brand);
        dest.writeFloat(this.niacin);
        dest.writeFloat(this.natrium);
        dest.writeFloat(this.copper);
        dest.writeFloat(this.zinc);
        dest.writeFloat(this.selenium);
        dest.writeString(this.type);
        dest.writeFloat(this.fat);
        dest.writeFloat(this.magnesium);
        dest.writeFloat(this.calcium);
        dest.writeFloat(this.kalium);
        dest.writeFloat(this.iron);
        dest.writeFloat(this.cholesterol);
        dest.writeFloat(this.weight);
        dest.writeString(this.thumb_image_url);
        dest.writeFloat(this.fiber_dietary);
        dest.writeFloat(this.lactoflavin);
        dest.writeFloat(this.vitamin_a);
        dest.writeInt(this.usedegree);
        dest.writeFloat(this.vitamin_e);
        dest.writeFloat(this.protein);
        dest.writeFloat(this.vitamin_c);
        dest.writeFloat(this.manganese);
        dest.writeFloat(this.carbohydrate);
        dest.writeFloat(this.thiamine);
        dest.writeFloat(this.iodine);
        dest.writeLong(this.update_time);
    }

    protected BiteEnty(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.is_liquid = in.readByte() != 0;
        this.calory = in.readFloat();
        this.isCheck = in.readByte() != 0;
        this.units = in.readString();
        this.unitList = new ArrayList<BiteUnit>();
        in.readList(this.unitList, BiteUnit.class.getClassLoader());
        this.brand = in.readString();
        this.niacin = in.readFloat();
        this.natrium = in.readFloat();
        this.copper = in.readFloat();
        this.zinc = in.readFloat();
        this.selenium = in.readFloat();
        this.type = in.readString();
        this.fat = in.readFloat();
        this.magnesium = in.readFloat();
        this.calcium = in.readFloat();
        this.kalium = in.readFloat();
        this.iron = in.readFloat();
        this.cholesterol = in.readFloat();
        this.weight = in.readFloat();
        this.thumb_image_url = in.readString();
        this.fiber_dietary = in.readFloat();
        this.lactoflavin = in.readFloat();
        this.vitamin_a = in.readFloat();
        this.usedegree = in.readInt();
        this.vitamin_e = in.readFloat();
        this.protein = in.readFloat();
        this.vitamin_c = in.readFloat();
        this.manganese = in.readFloat();
        this.carbohydrate = in.readFloat();
        this.thiamine = in.readFloat();
        this.iodine = in.readFloat();
        this.update_time = in.readLong();
    }

    public static final Creator<BiteEnty> CREATOR = new Creator<BiteEnty>() {
        @Override
        public BiteEnty createFromParcel(Parcel source) {
            return new BiteEnty(source);
        }

        @Override
        public BiteEnty[] newArray(int size) {
            return new BiteEnty[size];
        }
    };
}
