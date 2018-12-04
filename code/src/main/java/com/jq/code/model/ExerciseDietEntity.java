package com.jq.code.model;

import android.os.Parcel;

import com.jq.code.model.sport.SubmitFoodEntity;
import com.jq.code.model.sport.SubmitSportEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */

public class ExerciseDietEntity extends PutBase {
    private int exCalory; //运动消耗
    private int totalIntake; //总摄入
    private int bfCalory; //早餐摄入
    private int lcCalory;  //午餐摄入
    private int dnCalory; //晚餐摄入
    private int skCalory; //零食摄入
    private int metabolism; //基础代谢
    private List<SubmitSportEntity> sports;
    private List<SubmitFoodEntity> foods;

    @Override
    public String toString() {
        return "ExerciseDietEntity{" +
                "totalIntake=" + totalIntake +
                ", exCalory=" + exCalory +
                ", bfCalory=" + bfCalory +
                ", lcCalory=" + lcCalory +
                ", dnCalory=" + dnCalory +
                ", skCalory=" + skCalory +
                ", metabolism=" + metabolism +
                ", sports=" + sports +
                ", foods=" + foods +
                "} " + super.toString();
    }

    public List<SubmitSportEntity> getSports() {
        return sports;
    }

    public void setSports(List<SubmitSportEntity> sports) {
        this.sports = sports;
        if (sports == null) return;
        exCalory = 0;
        for (SubmitSportEntity entity : sports) {
            exCalory += entity.getCalory();
        }
    }

    public List<SubmitFoodEntity> getFoods() {
        return foods;
    }

    public void setFoods(List<SubmitFoodEntity> foods) {
        this.foods = foods;
        if (foods == null) return;
        totalIntake = 0; //总摄入
        bfCalory = 0; //早餐摄入
        lcCalory = 0;  //午餐摄入
        dnCalory = 0; //晚餐摄入
        skCalory = 0; //零食摄入
        for (SubmitFoodEntity foodEntity : foods) {
            if (foodEntity.getFtype().equals(SubmitFoodEntity.Type.BREAKFAST.getName())) {
                bfCalory += foodEntity.getCalory();
            } else if (foodEntity.getFtype().equals(SubmitFoodEntity.Type.LUNCH.getName())) {
                lcCalory += foodEntity.getCalory();
            } else if (foodEntity.getFtype().equals(SubmitFoodEntity.Type.DINNER.getName())) {
                dnCalory += foodEntity.getCalory();
            } else if (foodEntity.getFtype().equals(SubmitFoodEntity.Type.SNACKS.getName())) {
                skCalory += foodEntity.getCalory();
            }
        }
        totalIntake = bfCalory + lcCalory + dnCalory + skCalory;
    }

    public int getMetabolism() {
        metabolism = 0;
        if (sports != null && !sports.isEmpty()) {
            int i = 0;
            for (SubmitSportEntity sportEntity : sports) {
                if (sportEntity.getMetabolism() > 0) {
                    metabolism += sportEntity.getMetabolism();
                    i++;
                }
            }
            if (i > 0) metabolism /= i;
        }
        if (metabolism != 0) return metabolism;
        if (foods != null && !foods.isEmpty()) {
            int i = 0;
            for (SubmitFoodEntity foodEntity : foods) {
                if (foodEntity.getMetabolism() > 0) {
                    metabolism += foodEntity.getMetabolism();
                    i++;
                }
            }
            if (i > 0) metabolism /= i;
        }
        return metabolism;
    }

    public void setMetabolism(int metabolism) {
        this.metabolism = metabolism;
    }

    public int getTotalIntake() {
        return totalIntake;
    }

    public void setTotalIntake(int totalIntake) {
        this.totalIntake = totalIntake;
    }


    public int getExCalory() {
        return exCalory;
    }

    public void setExCalory(int exCalory) {
        this.exCalory = exCalory;
    }

    public int getBfCalory() {
        return bfCalory;
    }

    public void setBfCalory(int bfCalory) {
        this.bfCalory = bfCalory;
    }

    public int getLcCalory() {
        return lcCalory;
    }

    public void setLcCalory(int lcCalory) {
        this.lcCalory = lcCalory;
    }

    public int getDnCalory() {
        return dnCalory;
    }

    public void setDnCalory(int dnCalory) {
        this.dnCalory = dnCalory;
    }

    public int getSkCalory() {
        return skCalory;
    }

    public void setSkCalory(int skCalory) {
        this.skCalory = skCalory;
    }


    public ExerciseDietEntity() {
        setMtype(TYPE_EXERCISE_FOOD);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.totalIntake);
        dest.writeInt(this.exCalory);
        dest.writeInt(this.bfCalory);
        dest.writeInt(this.lcCalory);
        dest.writeInt(this.dnCalory);
        dest.writeInt(this.skCalory);
        dest.writeInt(this.metabolism);
        dest.writeTypedList(this.sports);
        dest.writeTypedList(this.foods);
    }

    protected ExerciseDietEntity(Parcel in) {
        super(in);
        this.totalIntake = in.readInt();
        this.exCalory = in.readInt();
        this.bfCalory = in.readInt();
        this.lcCalory = in.readInt();
        this.dnCalory = in.readInt();
        this.skCalory = in.readInt();
        this.metabolism = in.readInt();
        this.sports = in.createTypedArrayList(SubmitSportEntity.CREATOR);
        this.foods = in.createTypedArrayList(SubmitFoodEntity.CREATOR);
    }

    public static final Creator<ExerciseDietEntity> CREATOR = new Creator<ExerciseDietEntity>() {
        @Override
        public ExerciseDietEntity createFromParcel(Parcel source) {
            return new ExerciseDietEntity(source);
        }

        @Override
        public ExerciseDietEntity[] newArray(int size) {
            return new ExerciseDietEntity[size];
        }
    };
}
