package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/18
 */
public class MoreDataModel {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":7,"score":66.5,"weight":123,"metabolism":0,"boneWeight":0,"muscleRate":0,"muscleWeight":0,"visceralFat":0,"water":0,"waterWeight":0,"obesity":333,"bmi":0,"isDel":0,"create_time":"Dec 6, 2018 9:46:59 AM"},{"id":10,"score":66.5,"weight":0,"metabolism":0,"boneWeight":0,"muscleRate":0,"muscleWeight":0,"visceralFat":0,"water":0,"waterWeight":0,"obesity":0,"bmi":0,"isDel":0,"create_time":"Dec 9, 2018 4:53:27 PM"},{"id":11,"score":66.7,"weight":0,"metabolism":0,"boneWeight":0,"muscleRate":0,"muscleWeight":0,"visceralFat":0,"water":0,"waterWeight":0,"obesity":0,"bmi":0,"isDel":0,"create_time":"Dec 10, 2018 6:16:28 PM"}]
     */

    private String code;
    private String message;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 7
         * score : 66.5
         * weight : 123
         * metabolism : 0
         * boneWeight : 0
         * muscleRate : 0
         * muscleWeight : 0
         * visceralFat : 0
         * water : 0
         * waterWeight : 0
         * obesity : 333
         * bmi : 0
         * isDel : 0
         * create_time : Dec 6, 2018 9:46:59 AM
         */

        private int id;
        private double score;
        private int weight;
        private int metabolism;
        private int boneWeight;
        private int muscleRate;
        private int muscleWeight;
        private int visceralFat;
        private int water;
        private int waterWeight;
        private int obesity;
        private int bmi;
        private int isDel;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getMetabolism() {
            return metabolism;
        }

        public void setMetabolism(int metabolism) {
            this.metabolism = metabolism;
        }

        public int getBoneWeight() {
            return boneWeight;
        }

        public void setBoneWeight(int boneWeight) {
            this.boneWeight = boneWeight;
        }

        public int getMuscleRate() {
            return muscleRate;
        }

        public void setMuscleRate(int muscleRate) {
            this.muscleRate = muscleRate;
        }

        public int getMuscleWeight() {
            return muscleWeight;
        }

        public void setMuscleWeight(int muscleWeight) {
            this.muscleWeight = muscleWeight;
        }

        public int getVisceralFat() {
            return visceralFat;
        }

        public void setVisceralFat(int visceralFat) {
            this.visceralFat = visceralFat;
        }

        public int getWater() {
            return water;
        }

        public void setWater(int water) {
            this.water = water;
        }

        public int getWaterWeight() {
            return waterWeight;
        }

        public void setWaterWeight(int waterWeight) {
            this.waterWeight = waterWeight;
        }

        public int getObesity() {
            return obesity;
        }

        public void setObesity(int obesity) {
            this.obesity = obesity;
        }

        public int getBmi() {
            return bmi;
        }

        public void setBmi(int bmi) {
            this.bmi = bmi;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }
}
