package com.jq.code.model.sport;

import com.jq.code.model.Displayable;
import com.jq.code.model.PutBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Food/sport summary entity
 * Created by garfield on 16-10-28.
 */
public class FeEntity implements Displayable {
    private List<SubmitFoodEntity> foodList = new ArrayList<>();
    private List<SubmitSportEntity> sportList = new ArrayList<>();

    private String date;
    private int calFood;
    private int calSport;
    private int metabolism;
    private int calBreakfast;
    private int calLunch;
    private int calDinner;
    private int calSnacks;

    @Override
    public String toString() {
        return "FeEntity{" +
                "foodList=" + foodList +
                ", sportList=" + sportList +
                ", date='" + date + '\'' +
                ", calFood=" + calFood +
                ", calSport=" + calSport +
                ", metabolism=" + metabolism +
                ", calBreakfast=" + calBreakfast +
                ", calLunch=" + calLunch +
                ", calDinner=" + calDinner +
                ", calSnacks=" + calSnacks +
                '}';
    }

    @Override
    public String getMeasure_time() {
        return date;
    }

    public boolean hasFood(){
        return !foodList.isEmpty();
    }

    public boolean hasSport(){
        return !sportList.isEmpty();
    }

    public void addFood(SubmitFoodEntity food){
        assertDate(food);

        foodList.add(food);
        refreshMetab();
        if(date==null){
            date=food.getDate();
        }
        float cal = food.getCalory();
        calFood+=cal;
        switch(SubmitFoodEntity.Type.fromString(food.getFtype())){
            case BREAKFAST:
                calBreakfast+=cal;
                break;
            case LUNCH:
                calLunch+=cal;
                break;
            case DINNER:
                calDinner+=cal;
                break;
            case SNACKS:
                calSnacks+=cal;
        }

    }

    public void addSport(SubmitSportEntity sport){
        sportList.add(sport);
        refreshMetab();
        if(date==null){
            date=sport.getDate();
        }
        float cal = sport.getCalory();
        calSport+=cal;
    }


    private void refreshMetab() {
        int n=0, sum=0;
        if(hasSport()){
            for (SubmitSportEntity sport: sportList) {
                if(sport.getMetabolism()>0){
                    n++;
                    sum+=sport.getMetabolism();
                }
            }
            metabolism = (int) Math.round(sum/1.0/n);
            return ;
        }else if (hasFood()){
            for (SubmitFoodEntity food: foodList) {
                if(food.getMetabolism()>0){
                    n++;
                    sum+=food.getMetabolism();
                }
            }
            metabolism = (int) Math.round(sum/1.0/n);
        }
    }

    private void assertDate(PutBase fe) {
        if(date!=null && date!=fe.getMeasure_time()){
            throw new RuntimeException("Mismatch date: " + date + "->" + fe.getMeasure_time());
        }
    }

    public List<SubmitFoodEntity> getFoodList() {
        return foodList;
    }

    public List<SubmitSportEntity> getSportList() {
        return sportList;
    }

    public String getDate() {
        return date;
    }

    public int getCalFood() {
        return calFood;
    }

    public int getCalSport() {
        return calSport;
    }

    public int getMetabolism() {
        return metabolism;
    }

    public int getCalBreakfast() {
        return calBreakfast;
    }

    public int getCalLunch() {
        return calLunch;
    }

    public int getCalDinner() {
        return calDinner;
    }

    public int getCalSnacks() {
        return calSnacks;
    }

    public void addFe(PutBase foodOrExercise) {
        if(foodOrExercise instanceof SubmitFoodEntity){
            addFood((SubmitFoodEntity) foodOrExercise);
        }else if(foodOrExercise instanceof SubmitSportEntity){
            addSport((SubmitSportEntity) foodOrExercise);
        }else{
            throw new RuntimeException("add non fe to fe?");
        }
    }
}
