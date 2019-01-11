package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/18
 */
public class MatchModel {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":130,"famaliyId":"201611301119574263","userId":"201812261632201415","score":"0","weight":"8.1","metabolism":"0","boneWeight":"0.0","muscleRate":"0%","muscleWeight":"0.0","visceralFat":"0","water":"0%","waterWeight":"0.0","obesity":"-87.7%","bmi":"2.7","isDel":0,"createTime":"Dec 28, 2018 11:04:12 AM"}]
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
         * id : 130
         * famaliyId : 201611301119574263
         * userId : 201812261632201415
         * score : 0
         * weight : 8.1
         * metabolism : 0
         * boneWeight : 0.0
         * muscleRate : 0%
         * muscleWeight : 0.0
         * visceralFat : 0
         * water : 0%
         * waterWeight : 0.0
         * obesity : -87.7%
         * bmi : 2.7
         * isDel : 0
         * createTime : Dec 28, 2018 11:04:12 AM
         */

        private int id;
        private String famaliyId;
        private String userId;
        private String score;
        private String weight;
        private String metabolism;
        private String boneWeight;
        private String muscleRate;
        private String muscleWeight;
        private String visceralFat;
        private String water;
        private String waterWeight;
        private String obesity;
        private String bmi;
        private int isDel;
        private String createTime;

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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getMetabolism() {
            return metabolism;
        }

        public void setMetabolism(String metabolism) {
            this.metabolism = metabolism;
        }

        public String getBoneWeight() {
            return boneWeight;
        }

        public void setBoneWeight(String boneWeight) {
            this.boneWeight = boneWeight;
        }

        public String getMuscleRate() {
            return muscleRate;
        }

        public void setMuscleRate(String muscleRate) {
            this.muscleRate = muscleRate;
        }

        public String getMuscleWeight() {
            return muscleWeight;
        }

        public void setMuscleWeight(String muscleWeight) {
            this.muscleWeight = muscleWeight;
        }

        public String getVisceralFat() {
            return visceralFat;
        }

        public void setVisceralFat(String visceralFat) {
            this.visceralFat = visceralFat;
        }

        public String getWater() {
            return water;
        }

        public void setWater(String water) {
            this.water = water;
        }

        public String getWaterWeight() {
            return waterWeight;
        }

        public void setWaterWeight(String waterWeight) {
            this.waterWeight = waterWeight;
        }

        public String getObesity() {
            return obesity;
        }

        public void setObesity(String obesity) {
            this.obesity = obesity;
        }

        public String getBmi() {
            return bmi;
        }

        public void setBmi(String bmi) {
            this.bmi = bmi;
        }

        public int getIsDel() {
            return isDel;
        }

        public void setIsDel(int isDel) {
            this.isDel = isDel;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
