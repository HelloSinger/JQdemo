package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/25
 */
public class LastWeightModel {
    /**
     * ok : true
     * code : 200
     * message : 获取成功
     * data : {"id":653,"famaliyId":"201805021447134581","userId":"201901311621163242","macId":"DEF201921","provinceId":"","provinceName":"","cityId":"","cityName":"","countyId":"","countyName":"","sex":"1","age":"1993-1-31","height":"175","score":"61","weight":"34.75","metabolism":"1145.4,未达标,3100.0,1145.4","boneWeight":"1.0,偏低,5.0,1.0","muscleRate":"46.1%,优,45.0,46.06","muscleWeight":"16.0,不足,45.0,16.005852","visceralFat":"1,标准,14.0,1.0","water":"73.5%,优,66.0,73.46","waterWeight":"25.5,不足,66.0,25.52735","obesity":"-47.7%,消瘦,50.0,-47.74436","bmi":"11.3,偏瘦,28.0,11.3","body":"5%,偏瘦,45.0,5.0,你的体重比上次轻了1.2KG","healith":"你的理想体重是66.50KG，现在的你太瘦弱了，建议每天3餐，保证必要的营养摄入，并针对自身体质特征进行调养，增强身体的消化吸收力，充分吸收食物的营养，让多余的能量转化为肌肉和脂肪，以此来得到体重的增长。","isDel":0,"createTime":"Feb 27, 2019 10:23:43 AM","recipes":[{"recipename":"茶香鲜肉雪菜糯米圆子","recipetag":"粤菜,煮,炖,拌,香,清淡,爽口,肉类,煲汤,小吃,零食,早餐食谱,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/7/6/e/769543ef2f5bddeebe7f4bf1f149c6ee.jpg","recipeid":"1421448","scene":"早"},{"recipename":"新蜜汁烤土豆","recipetag":"小吃,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/9/2/b/92532deab5cf1d2acefe92a27ed4a2cb.jpg","recipeid":"1243790","scene":"早"},{"recipename":"菠菜火腿香菇蛋花粥","recipetag":"煮,主食,粥,早餐食谱,宵夜","recipeimage":"http://i1.douguo.net/upload/caiku/b/1/a/b1ee54862659731107606dc7a025f6ea.jpeg","recipeid":"1349889","scene":"早"},{"recipename":"#东菱破壁机#之山药鲜鱼汤","recipetag":"香,饮品,早餐食谱,下午茶","recipeimage":"http://i1.douguo.net/upload/caiku/1/4/a/147f4d5ab4a02accf6bee3750d5b600a.jpg","recipeid":"1354871","scene":"早"},{"recipename":"韩式拌饭","recipetag":"韩国料理,拌,主食,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/8/6/8/8609a4d18266912bf5c796f80ba1d968.jpg","recipeid":"1176211","scene":"午"},{"recipename":"泰式炒河粉","recipetag":"泰国菜,炒,主食,河粉,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/3/f/1/3f6d3e2fa7b10f1c9990d3f805f02bb1.jpg","recipeid":"1165159","scene":"午"},{"recipename":"酱焖排骨","recipetag":"家常菜,焖,香,肉类,午餐,晚餐,健康食谱","recipeimage":"http://i1.douguo.net/upload/caiku/d/6/a/d6a28dabb791adb86817b68a17cd376a.jpg","recipeid":"332995","scene":"午"},{"recipename":"青椒河虾","recipetag":"家常菜,炒,香,水产,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/9/8/c/9819c6257bca496563a2d56f07c068bc.jpg","recipeid":"1152816","scene":"午"},{"recipename":"酸菜鱼","recipetag":"家常菜,煮,水产,晚餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/c/6/7/c6b5f332dc89a0948fe099b730d1fe97.jpg","recipeid":"1200890","scene":"晚"},{"recipename":"野菜包子","recipetag":"蒸,主食,包子,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/1/5/5/1566f6f9935d034b86dd80db950cfd95.jpg","recipeid":"1132126","scene":"晚"},{"recipename":"家常糊塌子","recipetag":"煎,煮,拌,烙,香,五香,主食,早餐食谱,午餐,晚餐,京菜,咸","recipeimage":"http://i1.douguo.net/upload/caiku/9/3/a/933302a6a2e29d7d25afd47a7fa1946a.jpg","recipeid":"1138775","scene":"晚"},{"recipename":"南瓜手擀面（附三丁浇头的做法）","recipetag":"煮,香,主食,面条,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/5/9/d/59b2e8f636aed73fea1afaa14af7839d.jpg","recipeid":"1181161","scene":"晚"}]}
     */

    private String ok;
    private String code;
    private String message;
    private DataBean data;

    public String getOk() {
        return ok;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 653
         * famaliyId : 201805021447134581
         * userId : 201901311621163242
         * macId : DEF201921
         * provinceId :
         * provinceName :
         * cityId :
         * cityName :
         * countyId :
         * countyName :
         * sex : 1
         * age : 1993-1-31
         * height : 175
         * score : 61
         * weight : 34.75
         * metabolism : 1145.4,未达标,3100.0,1145.4
         * boneWeight : 1.0,偏低,5.0,1.0
         * muscleRate : 46.1%,优,45.0,46.06
         * muscleWeight : 16.0,不足,45.0,16.005852
         * visceralFat : 1,标准,14.0,1.0
         * water : 73.5%,优,66.0,73.46
         * waterWeight : 25.5,不足,66.0,25.52735
         * obesity : -47.7%,消瘦,50.0,-47.74436
         * bmi : 11.3,偏瘦,28.0,11.3
         * body : 5%,偏瘦,45.0,5.0,你的体重比上次轻了1.2KG
         * healith : 你的理想体重是66.50KG，现在的你太瘦弱了，建议每天3餐，保证必要的营养摄入，并针对自身体质特征进行调养，增强身体的消化吸收力，充分吸收食物的营养，让多余的能量转化为肌肉和脂肪，以此来得到体重的增长。
         * isDel : 0
         * createTime : Feb 27, 2019 10:23:43 AM
         * recipes : [{"recipename":"茶香鲜肉雪菜糯米圆子","recipetag":"粤菜,煮,炖,拌,香,清淡,爽口,肉类,煲汤,小吃,零食,早餐食谱,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/7/6/e/769543ef2f5bddeebe7f4bf1f149c6ee.jpg","recipeid":"1421448","scene":"早"},{"recipename":"新蜜汁烤土豆","recipetag":"小吃,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/9/2/b/92532deab5cf1d2acefe92a27ed4a2cb.jpg","recipeid":"1243790","scene":"早"},{"recipename":"菠菜火腿香菇蛋花粥","recipetag":"煮,主食,粥,早餐食谱,宵夜","recipeimage":"http://i1.douguo.net/upload/caiku/b/1/a/b1ee54862659731107606dc7a025f6ea.jpeg","recipeid":"1349889","scene":"早"},{"recipename":"#东菱破壁机#之山药鲜鱼汤","recipetag":"香,饮品,早餐食谱,下午茶","recipeimage":"http://i1.douguo.net/upload/caiku/1/4/a/147f4d5ab4a02accf6bee3750d5b600a.jpg","recipeid":"1354871","scene":"早"},{"recipename":"韩式拌饭","recipetag":"韩国料理,拌,主食,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/8/6/8/8609a4d18266912bf5c796f80ba1d968.jpg","recipeid":"1176211","scene":"午"},{"recipename":"泰式炒河粉","recipetag":"泰国菜,炒,主食,河粉,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/3/f/1/3f6d3e2fa7b10f1c9990d3f805f02bb1.jpg","recipeid":"1165159","scene":"午"},{"recipename":"酱焖排骨","recipetag":"家常菜,焖,香,肉类,午餐,晚餐,健康食谱","recipeimage":"http://i1.douguo.net/upload/caiku/d/6/a/d6a28dabb791adb86817b68a17cd376a.jpg","recipeid":"332995","scene":"午"},{"recipename":"青椒河虾","recipetag":"家常菜,炒,香,水产,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/9/8/c/9819c6257bca496563a2d56f07c068bc.jpg","recipeid":"1152816","scene":"午"},{"recipename":"酸菜鱼","recipetag":"家常菜,煮,水产,晚餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/c/6/7/c6b5f332dc89a0948fe099b730d1fe97.jpg","recipeid":"1200890","scene":"晚"},{"recipename":"野菜包子","recipetag":"蒸,主食,包子,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/1/5/5/1566f6f9935d034b86dd80db950cfd95.jpg","recipeid":"1132126","scene":"晚"},{"recipename":"家常糊塌子","recipetag":"煎,煮,拌,烙,香,五香,主食,早餐食谱,午餐,晚餐,京菜,咸","recipeimage":"http://i1.douguo.net/upload/caiku/9/3/a/933302a6a2e29d7d25afd47a7fa1946a.jpg","recipeid":"1138775","scene":"晚"},{"recipename":"南瓜手擀面（附三丁浇头的做法）","recipetag":"煮,香,主食,面条,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/5/9/d/59b2e8f636aed73fea1afaa14af7839d.jpg","recipeid":"1181161","scene":"晚"}]
         */

        private int id;
        private String famaliyId;
        private String userId;
        private String macId;
        private String provinceId;
        private String provinceName;
        private String cityId;
        private String cityName;
        private String countyId;
        private String countyName;
        private String sex;
        private String age;
        private String height;
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
        private String body;
        private String healith;
        private int isDel;
        private String createTime;
        private List<RecipesBean> recipes;

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

        public String getMacId() {
            return macId;
        }

        public void setMacId(String macId) {
            this.macId = macId;
        }

        public String getProvinceId() {
            return provinceId;
        }

        public void setProvinceId(String provinceId) {
            this.provinceId = provinceId;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public String getCountyName() {
            return countyName;
        }

        public void setCountyName(String countyName) {
            this.countyName = countyName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
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

        public List<RecipesBean> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<RecipesBean> recipes) {
            this.recipes = recipes;
        }

        public static class RecipesBean {
            /**
             * recipename : 茶香鲜肉雪菜糯米圆子
             * recipetag : 粤菜,煮,炖,拌,香,清淡,爽口,肉类,煲汤,小吃,零食,早餐食谱,午餐,晚餐
             * recipeimage : http://i1.douguo.net/upload/caiku/7/6/e/769543ef2f5bddeebe7f4bf1f149c6ee.jpg
             * recipeid : 1421448
             * scene : 早
             */

            private String recipename;
            private String recipetag;
            private String recipeimage;
            private String recipeid;
            private String scene;

            public String getRecipename() {
                return recipename;
            }

            public void setRecipename(String recipename) {
                this.recipename = recipename;
            }

            public String getRecipetag() {
                return recipetag;
            }

            public void setRecipetag(String recipetag) {
                this.recipetag = recipetag;
            }

            public String getRecipeimage() {
                return recipeimage;
            }

            public void setRecipeimage(String recipeimage) {
                this.recipeimage = recipeimage;
            }

            public String getRecipeid() {
                return recipeid;
            }

            public void setRecipeid(String recipeid) {
                this.recipeid = recipeid;
            }

            public String getScene() {
                return scene;
            }

            public void setScene(String scene) {
                this.scene = scene;
            }
        }
    }


//    /**
//     * code : 200
//     * message : 查询成功
//     * data : [{"id":43,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"26.3","metabolism":"948.6","boneWeight":"1.0","muscleRate":"45.6%","muscleWeight":"12.0","visceralFat":"1","water":"85%","waterWeight":"22.4","obesity":"-62.4%","bmi":"8.1","isDel":0,"createTime":"Dec 24, 2018 6:13:56 PM"},{"id":44,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"23.0","metabolism":"907.5","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.4","visceralFat":"1","water":"85%","waterWeight":"19.5","obesity":"-67.1%","bmi":"7.1","isDel":0,"createTime":"Dec 24, 2018 6:17:49 PM"},{"id":45,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:26:43 PM"},{"id":46,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:27:48 PM"},{"id":47,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"20.6","metabolism":"877.4","boneWeight":"1.0","muscleRate":"45.1%","muscleWeight":"9.3","visceralFat":"1","water":"85%","waterWeight":"17.5","obesity":"-70.6%","bmi":"6.4","isDel":0,"createTime":"Dec 24, 2018 6:32:17 PM"},{"id":48,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"22.8","metabolism":"901","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.3","visceralFat":"1","water":"85%","waterWeight":"19.4","obesity":"-67.4%","bmi":"7","isDel":0,"createTime":"Dec 24, 2018 6:35:33 PM"},{"id":49,"famaliyId":"201805021447134581","userId":"201811131357391261","score":"62","weight":"22.8","metabolism":"901","boneWeight":"1.0","muscleRate":"45.3%","muscleWeight":"10.3","visceralFat":"1","water":"85%","waterWeight":"19.4","obesity":"-67.4%","bmi":"7","isDel":0,"createTime":"Dec 24, 2018 6:37:01 PM"}]
//     */
//
//    private String code;
//    private String message;
//    private List<DataBean> data;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }
//
//    public static class DataBean {
//        /**
//         * id : 43
//         * famaliyId : 201805021447134581
//         * userId : 201811131357391261
//         * score : 62
//         * weight : 26.3
//         * metabolism : 948.6
//         * boneWeight : 1.0
//         * muscleRate : 45.6%
//         * muscleWeight : 12.0
//         * visceralFat : 1
//         * water : 85%
//         * waterWeight : 22.4
//         * obesity : -62.4%
//         * bmi : 8.1
//         * isDel : 0
//         * createTime : Dec 24, 2018 6:13:56 PM
//         */
//
//        private int id;
//        private String famaliyId;
//        private String userId;
//        private String score;
//        private String weight;
//        private String metabolism;
//        private String boneWeight;
//        private String muscleRate;
//        private String muscleWeight;
//        private String visceralFat;
//        private String water;
//        private String waterWeight;
//        private String obesity;
//        private String bmi;
//        private int isDel;
//        private String createTime;
//        private String body;
//        private String healith;
//
//        public String getBody() {
//            return body;
//        }
//
//        public void setBody(String body) {
//            this.body = body;
//        }
//
//        public String getHealith() {
//            return healith;
//        }
//
//        public void setHealith(String healith) {
//            this.healith = healith;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getFamaliyId() {
//            return famaliyId;
//        }
//
//        public void setFamaliyId(String famaliyId) {
//            this.famaliyId = famaliyId;
//        }
//
//        public String getUserId() {
//            return userId;
//        }
//
//        public void setUserId(String userId) {
//            this.userId = userId;
//        }
//
//        public String getScore() {
//            return score;
//        }
//
//        public void setScore(String score) {
//            this.score = score;
//        }
//
//        public String getWeight() {
//            return weight;
//        }
//
//        public void setWeight(String weight) {
//            this.weight = weight;
//        }
//
//        public String getMetabolism() {
//            return metabolism;
//        }
//
//        public void setMetabolism(String metabolism) {
//            this.metabolism = metabolism;
//        }
//
//        public String getBoneWeight() {
//            return boneWeight;
//        }
//
//        public void setBoneWeight(String boneWeight) {
//            this.boneWeight = boneWeight;
//        }
//
//        public String getMuscleRate() {
//            return muscleRate;
//        }
//
//        public void setMuscleRate(String muscleRate) {
//            this.muscleRate = muscleRate;
//        }
//
//        public String getMuscleWeight() {
//            return muscleWeight;
//        }
//
//        public void setMuscleWeight(String muscleWeight) {
//            this.muscleWeight = muscleWeight;
//        }
//
//        public String getVisceralFat() {
//            return visceralFat;
//        }
//
//        public void setVisceralFat(String visceralFat) {
//            this.visceralFat = visceralFat;
//        }
//
//        public String getWater() {
//            return water;
//        }
//
//        public void setWater(String water) {
//            this.water = water;
//        }
//
//        public String getWaterWeight() {
//            return waterWeight;
//        }
//
//        public void setWaterWeight(String waterWeight) {
//            this.waterWeight = waterWeight;
//        }
//
//        public String getObesity() {
//            return obesity;
//        }
//
//        public void setObesity(String obesity) {
//            this.obesity = obesity;
//        }
//
//        public String getBmi() {
//            return bmi;
//        }
//
//        public void setBmi(String bmi) {
//            this.bmi = bmi;
//        }
//
//        public int getIsDel() {
//            return isDel;
//        }
//
//        public void setIsDel(int isDel) {
//            this.isDel = isDel;
//        }
//
//        public String getCreateTime() {
//            return createTime;
//        }
//
//        public void setCreateTime(String createTime) {
//            this.createTime = createTime;
//        }
//
//        @Override
//        public String toString() {
//            return "DataBean{" +
//                    "id=" + id +
//                    ", famaliyId='" + famaliyId + '\'' +
//                    ", userId='" + userId + '\'' +
//                    ", score='" + score + '\'' +
//                    ", weight='" + weight + '\'' +
//                    ", metabolism='" + metabolism + '\'' +
//                    ", boneWeight='" + boneWeight + '\'' +
//                    ", muscleRate='" + muscleRate + '\'' +
//                    ", muscleWeight='" + muscleWeight + '\'' +
//                    ", visceralFat='" + visceralFat + '\'' +
//                    ", water='" + water + '\'' +
//                    ", waterWeight='" + waterWeight + '\'' +
//                    ", obesity='" + obesity + '\'' +
//                    ", bmi='" + bmi + '\'' +
//                    ", isDel=" + isDel +
//                    ", createTime='" + createTime + '\'' +
//                    '}';
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "LastWeightModel{" +
//                "code='" + code + '\'' +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}';
//    }



}
