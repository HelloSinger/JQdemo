package com.jq.code.model.sport;

import android.content.Context;
import android.os.Parcel;

import com.jq.code.R;
import com.jq.code.model.DataType;
import com.jq.code.model.PutBase;

/**
 * Created by Administrator on 2016/10/23.
 */

public class SubmitFoodEntity extends PutBase {

    public enum Type {
        BREAKFAST("breakfast", R.string.sportBreakfast),
        LUNCH("lunch", R.string.sportLunch),
        DINNER("dinner", R.string.sportSupper),
        SNACKS("snacks", R.string.sportExtraMeal);
        String name;
        int mapId;

        Type(String name, int mapId) {
            this.name = name;
            this.mapId = mapId;
        }

        public String getName() {
            return name;
        }

        public int getMapId() {
            return mapId;
        }

        public static Type fromString(String typeName) {
            return Type.valueOf(typeName.toUpperCase());
        }

        public static String changeStr(Context context, String type) {
            String str = "";
            if (type.equals(BREAKFAST.getName())) {
                str = context.getString(BREAKFAST.getMapId());
            } else if (type.equals(LUNCH.getName())) {
                str = context.getString(LUNCH.getMapId());
            } else if (type.equals(DINNER.getName())) {
                str = context.getString(DINNER.getMapId());
            } else if (type.equals(SNACKS.getName())) {
                str = context.getString(SNACKS.getMapId());
            }
            return str;
        }
    }

    private long _id;
    private String name;
    private float quantity;
    private String date;
    private long food_id;
    private float calory;
    private String unit;
    private String ftype;
    private int metabolism;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        setMeasure_time(date);
        setWeight_time(date);
    }

    public long getFood_id() {
        return food_id;
    }

    public void setFood_id(long food_id) {
        this.food_id = food_id;
    }

    public float getCalory() {
        return calory;
    }

    public void setCalory(float calory) {
        this.calory = calory;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


    public int getMetabolism() {
        return metabolism;
    }

    public void setMetabolism(int metabolism) {
        this.metabolism = metabolism;
    }

    public SubmitFoodEntity() {
        setMtype(DataType.FOOD.getType());
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    @Override
    public String toString() {
        return "SubmitFoodEntity{" +
                "_id=" + _id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", date='" + date + '\'' +
                ", food_id=" + food_id +
                ", calory=" + calory +
                ", unit='" + unit + '\'' +
                ", ftype='" + ftype + '\'' +
                ", metabolism=" + metabolism +
                "} " + super.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeLong(this._id);
        dest.writeString(this.name);
        dest.writeFloat(this.quantity);
        dest.writeString(this.date);
        dest.writeLong(this.food_id);
        dest.writeFloat(this.calory);
        dest.writeString(this.unit);
        dest.writeString(this.ftype);
        dest.writeInt(this.metabolism);
    }

    protected SubmitFoodEntity(Parcel in) {
        super(in);
        this._id = in.readLong();
        this.name = in.readString();
        this.quantity = in.readFloat();
        this.date = in.readString();
        this.food_id = in.readLong();
        this.calory = in.readFloat();
        this.unit = in.readString();
        this.ftype = in.readString();
        this.metabolism = in.readInt();
    }

    public static final Creator<SubmitFoodEntity> CREATOR = new Creator<SubmitFoodEntity>() {
        @Override
        public SubmitFoodEntity createFromParcel(Parcel source) {
            return new SubmitFoodEntity(source);
        }

        @Override
        public SubmitFoodEntity[] newArray(int size) {
            return new SubmitFoodEntity[size];
        }
    };
}
