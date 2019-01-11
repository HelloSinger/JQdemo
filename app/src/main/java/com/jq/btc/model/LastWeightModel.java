package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/25
 */
public class LastWeightModel {


    /**
     * code : 200
     * message : 查询成功
     * data : [{"id":43,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"26.3","metabolism":"948.6","boneWeight":"1.0","muscleRate":"45.6%","muscleWeight":"12.0","visceralFat":"1","water":"85%","waterWeight":"22.4","obesity":"-62.4%","bmi":"8.1","isDel":0,"createTime":"Dec 24, 2018 6:13:56 PM"},{"id":44,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"23.0","metabolism":"907.5","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.4","visceralFat":"1","water":"85%","waterWeight":"19.5","obesity":"-67.1%","bmi":"7.1","isDel":0,"createTime":"Dec 24, 2018 6:17:49 PM"},{"id":45,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:26:43 PM"},{"id":46,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:27:48 PM"},{"id":47,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:32:17 PM"},{"id":48,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"22.8","metabolism":"901","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.3","visceralFat":"1","water":"85%","waterWeight":"19.4","obesity":"-67.4%","bmi":"7","isDel":0,"createTime":"Dec 24, 2018 6:35:33 PM"},{"id":49,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"22.8","metabolism":"901","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.3","visceralFat":"1","water":"85%","waterWeight":"19.4","obesity":"-67.4%","bmi":"7","isDel":0,"createTime":"Dec 24, 2018 6:37:01 PM"}]
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
         * id : 43
         * famaliyId : 201805021447134581
         * userId : 201811131357391261
         * score : 62
         * weight : 26.3
         * metabolism : 948.6
         * boneWeight : 1.0
         * muscleRate : 45.6%
         * muscleWeight : 12.0
         * visceralFat : 1
         * water : 85%
         * waterWeight : 22.4
         * obesity : -62.4%
         * bmi : 8.1
         * isDel : 0
         * createTime : Dec 24, 2018 6:13:56 PM
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
        private String body;
        private String healith;

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getHealith() {
            return healith;
        }

        public void setHealith(String healith) {
            this.healith = healith;
        }

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

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", famaliyId='" + famaliyId + '\'' +
                    ", userId='" + userId + '\'' +
                    ", score='" + score + '\'' +
                    ", weight='" + weight + '\'' +
                    ", metabolism='" + metabolism + '\'' +
                    ", boneWeight='" + boneWeight + '\'' +
                    ", muscleRate='" + muscleRate + '\'' +
                    ", muscleWeight='" + muscleWeight + '\'' +
                    ", visceralFat='" + visceralFat + '\'' +
                    ", water='" + water + '\'' +
                    ", waterWeight='" + waterWeight + '\'' +
                    ", obesity='" + obesity + '\'' +
                    ", bmi='" + bmi + '\'' +
                    ", isDel=" + isDel +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LastWeightModel{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
