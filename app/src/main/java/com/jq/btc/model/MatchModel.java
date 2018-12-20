package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/18
 */
public class MatchModel {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":5,"famaliyId":"236623","userId":"456","score":66.5,"weight":48,"metabolism":6.6,"boneWeight":0,"muscleRate":0,"muscleWeight":0,"visceralFat":0,"water":111,"waterWeight":0,"obesity":0,"bmi":444.4,"isDel":0,"create_time":"Dec 7, 2018 9:37:26 AM"},{"id":6,"famaliyId":"236623","userId":"1245","score":66.5,"weight":666,"metabolism":0,"boneWeight":0,"muscleRate":0,"muscleWeight":0,"visceralFat":0,"water":0,"waterWeight":0,"obesity":0,"bmi":0,"isDel":0,"create_time":"Dec 9, 2018 9:43:38 AM"}]
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
         * id : 5
         * famaliyId : 236623
         * userId : 456
         * score : 66.5
         * weight : 48
         * metabolism : 6.6
         * boneWeight : 0
         * muscleRate : 0
         * muscleWeight : 0
         * visceralFat : 0
         * water : 111
         * waterWeight : 0
         * obesity : 0
         * bmi : 444.4
         * isDel : 0
         * create_time : Dec 7, 2018 9:37:26 AM
         */

        private int id;
        private String famaliyId;
        private String userId;
        private double score;
        private int weight;
        private double metabolism;
        private int boneWeight;
        private int muscleRate;
        private int muscleWeight;
        private int visceralFat;
        private int water;
        private int waterWeight;
        private int obesity;
        private double bmi;
        private int isDel;
        private String create_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFamaliyId() {
            return famaliyId;
        }

        public void setFamaliyId(String famaliyId) {
            this.famaliyId = famaliyId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public double getMetabolism() {
            return metabolism;
        }

        public void setMetabolism(double metabolism) {
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

        public double getBmi() {
            return bmi;
        }

        public void setBmi(double bmi) {
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
