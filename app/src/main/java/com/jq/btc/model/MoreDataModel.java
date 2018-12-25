package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/18
 */
public class MoreDataModel {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":14,"famaliyId":"201611301119574263","userId":"201812171606391939","score":"61","weight":"31","metabolism":"1095.1","boneWeight":"1","muscleRate":"45.9","muscleWeight":"14.2","visceralFat":"1","water":"77.4","waterWeight":"24","obesity":"-52.9","bmi":"10.2","isDel":0,"createTime":"Dec 18, 2018 4:04:17 PM"},{"id":42,"famaliyId":"201611301119574263","userId":"201812171606391939","score":"61","weight":"20.65","metabolism":"958.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.6","obesity":"-68.6%","bmi":"6.8","isDel":0,"createTime":"Dec 22, 2018 2:55:35 PM"}]
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
         * id : 14
         * famaliyId : 201611301119574263
         * userId : 201812171606391939
         * score : 61
         * weight : 31
         * metabolism : 1095.1
         * boneWeight : 1
         * muscleRate : 45.9
         * muscleWeight : 14.2
         * visceralFat : 1
         * water : 77.4
         * waterWeight : 24
         * obesity : -52.9
         * bmi : 10.2
         * isDel : 0
         * createTime : Dec 18, 2018 4:04:17 PM
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
