package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2019/2/27
 */
public class NoDataModel {


    /**
     * ok : true
     * code : 200
     * message : 获取成功
     * data : {"recipes":[{"recipename":"椰汁杂粮粥","recipetag":"煮,主食,粥,早餐食谱,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/8/e/5/8e745a8074ba369703a4d3530c0daf45.jpg","recipeid":"1092387","scene":"早"},{"recipename":"降压果汁（仙人掌芹菜汁）","recipetag":"榨汁,甜,香,爽口,甜品,饮品,小吃,早餐食谱,下午茶,朋友聚餐,一家三口,零食","recipeimage":"http://i1.douguo.net/upload/caiku/c/d/2/cde6590d01a3877f85b6535d471b0102.jpeg","recipeid":"1409824","scene":"早"},{"recipename":"樱桃果酱","recipetag":"甜,果酱,面包机食谱,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/b/3/7/b30c0f8ec18724a920cc4935a8868ba7.jpg","recipeid":"976443","scene":"早"},{"recipename":"火腿奶酪鸡蛋饼","recipetag":"烙,香,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/b/d/9/bdfe11af14be893d13c9b8d5634be409.jpg","recipeid":"805038","scene":"早"},{"recipename":"京酱肉丝","recipetag":"家常菜,炒,下饭菜,肉类,午餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/1/f/3/1f7c393976041edc9ee0f7bb8ebbad63.jpg","recipeid":"1149401","scene":"中"},{"recipename":"番茄灌沙拉","recipetag":"西餐,香,凉菜,午餐,晚餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/4/6/b/462e7bee1637cad9c3b75d4e8f7a868b.jpg","recipeid":"1098797","scene":"中"},{"recipename":"红酒黑胡椒牛排","recipetag":"西餐,家常菜,煎,香,肉类,午餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/4/5/04134132b77fe370f8e892527da37e15.jpg","recipeid":"186170","scene":"中"},{"recipename":"团圆饭必备-腊制品","recipetag":"家常菜,午餐,晚餐,肉类","recipeimage":"http://i1.douguo.net/upload/caiku/5/b/e/5b352ffdd03d5cb9299161ce31737e5e.jpg","recipeid":"1233874","scene":"中"},{"recipename":"滑蛋三明治","recipetag":"主食,三明治,早餐食谱,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/3/c/d/3c4884d1b6c26fb8f26f8766249d216d.jpg","recipeid":"1211241","scene":"晚"},{"recipename":"侗寨美味--酥炸小鱼","recipetag":"家常菜,炸,香酥,水产,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/2/b/1/2b32e4f5a2474c7596921e4225716101.jpg","recipeid":"1185245","scene":"晚"},{"recipename":"干炒牛河","recipetag":"河粉,炒,香,主食,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/6/7/0/67ad49eff345a6923587af1591856b10.jpg","recipeid":"1140324","scene":"晚"},{"recipename":"自制酱料 -- 姜蒜蓉","recipetag":"家常菜,蒜香,禽类,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/6/c/a/6ce3ead427f3768f633a3b6963287a3a.jpg","recipeid":"1300066","scene":"晚"}],"fridgemac":"10D07A8354DD","userid":"201902271247424302","famaliyid":"201803301407178685"}
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
         * recipes : [{"recipename":"椰汁杂粮粥","recipetag":"煮,主食,粥,早餐食谱,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/8/e/5/8e745a8074ba369703a4d3530c0daf45.jpg","recipeid":"1092387","scene":"早"},{"recipename":"降压果汁（仙人掌芹菜汁）","recipetag":"榨汁,甜,香,爽口,甜品,饮品,小吃,早餐食谱,下午茶,朋友聚餐,一家三口,零食","recipeimage":"http://i1.douguo.net/upload/caiku/c/d/2/cde6590d01a3877f85b6535d471b0102.jpeg","recipeid":"1409824","scene":"早"},{"recipename":"樱桃果酱","recipetag":"甜,果酱,面包机食谱,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/b/3/7/b30c0f8ec18724a920cc4935a8868ba7.jpg","recipeid":"976443","scene":"早"},{"recipename":"火腿奶酪鸡蛋饼","recipetag":"烙,香,早餐食谱","recipeimage":"http://i1.douguo.net/upload/caiku/b/d/9/bdfe11af14be893d13c9b8d5634be409.jpg","recipeid":"805038","scene":"早"},{"recipename":"京酱肉丝","recipetag":"家常菜,炒,下饭菜,肉类,午餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/1/f/3/1f7c393976041edc9ee0f7bb8ebbad63.jpg","recipeid":"1149401","scene":"中"},{"recipename":"番茄灌沙拉","recipetag":"西餐,香,凉菜,午餐,晚餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/4/6/b/462e7bee1637cad9c3b75d4e8f7a868b.jpg","recipeid":"1098797","scene":"中"},{"recipename":"红酒黑胡椒牛排","recipetag":"西餐,家常菜,煎,香,肉类,午餐,朋友聚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/4/5/04134132b77fe370f8e892527da37e15.jpg","recipeid":"186170","scene":"中"},{"recipename":"团圆饭必备-腊制品","recipetag":"家常菜,午餐,晚餐,肉类","recipeimage":"http://i1.douguo.net/upload/caiku/5/b/e/5b352ffdd03d5cb9299161ce31737e5e.jpg","recipeid":"1233874","scene":"中"},{"recipename":"滑蛋三明治","recipetag":"主食,三明治,早餐食谱,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/3/c/d/3c4884d1b6c26fb8f26f8766249d216d.jpg","recipeid":"1211241","scene":"晚"},{"recipename":"侗寨美味--酥炸小鱼","recipetag":"家常菜,炸,香酥,水产,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/2/b/1/2b32e4f5a2474c7596921e4225716101.jpg","recipeid":"1185245","scene":"晚"},{"recipename":"干炒牛河","recipetag":"河粉,炒,香,主食,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/6/7/0/67ad49eff345a6923587af1591856b10.jpg","recipeid":"1140324","scene":"晚"},{"recipename":"自制酱料 -- 姜蒜蓉","recipetag":"家常菜,蒜香,禽类,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/6/c/a/6ce3ead427f3768f633a3b6963287a3a.jpg","recipeid":"1300066","scene":"晚"}]
         * fridgemac : 10D07A8354DD
         * userid : 201902271247424302
         * famaliyid : 201803301407178685
         */

        private String fridgemac;
        private String userid;
        private String famaliyid;
        private List<RecipesBean> recipes;

        public String getFridgemac() {
            return fridgemac;
        }

        public void setFridgemac(String fridgemac) {
            this.fridgemac = fridgemac;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getFamaliyid() {
            return famaliyid;
        }

        public void setFamaliyid(String famaliyid) {
            this.famaliyid = famaliyid;
        }

        public List<RecipesBean> getRecipes() {
            return recipes;
        }

        public void setRecipes(List<RecipesBean> recipes) {
            this.recipes = recipes;
        }

        public static class RecipesBean {
            /**
             * recipename : 椰汁杂粮粥
             * recipetag : 煮,主食,粥,早餐食谱,晚餐
             * recipeimage : http://i1.douguo.net/upload/caiku/8/e/5/8e745a8074ba369703a4d3530c0daf45.jpg
             * recipeid : 1092387
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
}
