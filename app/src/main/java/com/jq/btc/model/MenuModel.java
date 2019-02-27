package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2019/2/25
 */
public class MenuModel {


    /**
     * ok : true
     * code : 200
     * message : 获取成功
     * data : {"data":{"recipes":[{"recipename":"蛋奶土豆饼","recipetag":"家常菜,煎,主食,饼,早餐食谱,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/2/a/f/2aebe952109a02d73438abb032a7772f.jpg","recipeid":"1109087","scene":"早"},{"recipename":"核桃草莓奶","recipetag":"饮品,早餐食谱,学龄前","recipeimage":"http://i1.douguo.net/upload/caiku/d/0/4/d0c02381e9388dabbbe99e49e3306ce4.jpg","recipeid":"1092345","scene":"早"},{"recipename":"茄汁蛋泡饭","recipetag":"家常菜,早餐食谱,单身食谱,健康食谱","recipeimage":"http://i1.douguo.net/upload/caiku/3/c/b/3c72f354235989fa965aa7501e0d20eb.jpg","recipeid":"90244","scene":"早"},{"recipename":"有料的面包更好吃【肉松面包】#东菱魔法云面包机#","recipetag":"烘焙,面包,烤箱食谱,早餐食谱,下午茶","recipeimage":"http://i1.douguo.net/upload/caiku/4/6/7/46bd993cea7ff0c5d76e8f3b8c161437.jpg","recipeid":"1271480","scene":"早"},{"recipename":"烧豆腐","recipetag":"粤菜,家常菜,炒,蚝香,咸鲜,下饭菜,豆制品,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/1/3/3/13622f67f8b04fd3b8163d508d80f6a3.jpeg","recipeid":"1425432","scene":"午"},{"recipename":"海鲜意大利面","recipetag":"煮,拌,主食,意大利面,午餐","recipeimage":"http://i1.douguo.net/upload/caiku/5/5/6/55e058ce4d895569308b4b3cdfe8bd26.jpg","recipeid":"1160004","scene":"午"},{"recipename":"千层牛肉馅饼","recipetag":"鲁菜,家常菜,烙,咸,香,主食,饼,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/5/a/f/5afd27a6b772e9f4fdcb3b16c3d15acf.jpeg","recipeid":"1424719","scene":"午"},{"recipename":"【酸汤（酸辣）金针肥牛】","recipetag":"家常菜,煮,酸辣,肉类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/8/d/a871625fb0465d8943fac13ce95abffd.jpg","recipeid":"1230318","scene":"午"},{"recipename":"美味酸辣蕨根粉","recipetag":"拌,酸辣,小吃,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/9/9/09bc521c65d75dcb3c662f4f0f7df6a9.jpg","recipeid":"1178630","scene":"晚"},{"recipename":"陈皮兔","recipetag":"家常菜,炒,下饭菜,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/9/b/3/9b64b43e5a372fce326c3eb5b3470fa3.jpeg","recipeid":"1292201","scene":"晚"},{"recipename":"（懒人美食）瘦肉蛋羹","recipetag":"家常菜,蒸,香,蛋类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/5/5/a56ae1dfcf85267b21c7fb74c0401145.jpg","recipeid":"1134591","scene":"晚"},{"recipename":"香辣鸡翅","recipetag":"家常菜,炸,香辣,禽类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/5/5/05f043b08357e49b248c82a15453a1f5.jpg","recipeid":"1080209","scene":"晚"}],"fridgemac":"DEF201921","userid":"201901121422302358","famaliyid":"201810261526170986"},"success":true,"retInfo":"操作成功","retCode":"1000"}
     */

    private String ok;
    private String code;
    private String message;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * data : {"recipes":[{"recipename":"蛋奶土豆饼","recipetag":"家常菜,煎,主食,饼,早餐食谱,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/2/a/f/2aebe952109a02d73438abb032a7772f.jpg","recipeid":"1109087","scene":"早"},{"recipename":"核桃草莓奶","recipetag":"饮品,早餐食谱,学龄前","recipeimage":"http://i1.douguo.net/upload/caiku/d/0/4/d0c02381e9388dabbbe99e49e3306ce4.jpg","recipeid":"1092345","scene":"早"},{"recipename":"茄汁蛋泡饭","recipetag":"家常菜,早餐食谱,单身食谱,健康食谱","recipeimage":"http://i1.douguo.net/upload/caiku/3/c/b/3c72f354235989fa965aa7501e0d20eb.jpg","recipeid":"90244","scene":"早"},{"recipename":"有料的面包更好吃【肉松面包】#东菱魔法云面包机#","recipetag":"烘焙,面包,烤箱食谱,早餐食谱,下午茶","recipeimage":"http://i1.douguo.net/upload/caiku/4/6/7/46bd993cea7ff0c5d76e8f3b8c161437.jpg","recipeid":"1271480","scene":"早"},{"recipename":"烧豆腐","recipetag":"粤菜,家常菜,炒,蚝香,咸鲜,下饭菜,豆制品,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/1/3/3/13622f67f8b04fd3b8163d508d80f6a3.jpeg","recipeid":"1425432","scene":"午"},{"recipename":"海鲜意大利面","recipetag":"煮,拌,主食,意大利面,午餐","recipeimage":"http://i1.douguo.net/upload/caiku/5/5/6/55e058ce4d895569308b4b3cdfe8bd26.jpg","recipeid":"1160004","scene":"午"},{"recipename":"千层牛肉馅饼","recipetag":"鲁菜,家常菜,烙,咸,香,主食,饼,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/5/a/f/5afd27a6b772e9f4fdcb3b16c3d15acf.jpeg","recipeid":"1424719","scene":"午"},{"recipename":"【酸汤（酸辣）金针肥牛】","recipetag":"家常菜,煮,酸辣,肉类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/8/d/a871625fb0465d8943fac13ce95abffd.jpg","recipeid":"1230318","scene":"午"},{"recipename":"美味酸辣蕨根粉","recipetag":"拌,酸辣,小吃,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/9/9/09bc521c65d75dcb3c662f4f0f7df6a9.jpg","recipeid":"1178630","scene":"晚"},{"recipename":"陈皮兔","recipetag":"家常菜,炒,下饭菜,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/9/b/3/9b64b43e5a372fce326c3eb5b3470fa3.jpeg","recipeid":"1292201","scene":"晚"},{"recipename":"（懒人美食）瘦肉蛋羹","recipetag":"家常菜,蒸,香,蛋类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/5/5/a56ae1dfcf85267b21c7fb74c0401145.jpg","recipeid":"1134591","scene":"晚"},{"recipename":"香辣鸡翅","recipetag":"家常菜,炸,香辣,禽类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/5/5/05f043b08357e49b248c82a15453a1f5.jpg","recipeid":"1080209","scene":"晚"}],"fridgemac":"DEF201921","userid":"201901121422302358","famaliyid":"201810261526170986"}
         * success : true
         * retInfo : 操作成功
         * retCode : 1000
         */

        private DataBean data;
        private boolean success;
        private String retInfo;
        private String retCode;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public static class DataBean {
            /**
             * recipes : [{"recipename":"蛋奶土豆饼","recipetag":"家常菜,煎,主食,饼,早餐食谱,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/2/a/f/2aebe952109a02d73438abb032a7772f.jpg","recipeid":"1109087","scene":"早"},{"recipename":"核桃草莓奶","recipetag":"饮品,早餐食谱,学龄前","recipeimage":"http://i1.douguo.net/upload/caiku/d/0/4/d0c02381e9388dabbbe99e49e3306ce4.jpg","recipeid":"1092345","scene":"早"},{"recipename":"茄汁蛋泡饭","recipetag":"家常菜,早餐食谱,单身食谱,健康食谱","recipeimage":"http://i1.douguo.net/upload/caiku/3/c/b/3c72f354235989fa965aa7501e0d20eb.jpg","recipeid":"90244","scene":"早"},{"recipename":"有料的面包更好吃【肉松面包】#东菱魔法云面包机#","recipetag":"烘焙,面包,烤箱食谱,早餐食谱,下午茶","recipeimage":"http://i1.douguo.net/upload/caiku/4/6/7/46bd993cea7ff0c5d76e8f3b8c161437.jpg","recipeid":"1271480","scene":"早"},{"recipename":"烧豆腐","recipetag":"粤菜,家常菜,炒,蚝香,咸鲜,下饭菜,豆制品,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/1/3/3/13622f67f8b04fd3b8163d508d80f6a3.jpeg","recipeid":"1425432","scene":"午"},{"recipename":"海鲜意大利面","recipetag":"煮,拌,主食,意大利面,午餐","recipeimage":"http://i1.douguo.net/upload/caiku/5/5/6/55e058ce4d895569308b4b3cdfe8bd26.jpg","recipeid":"1160004","scene":"午"},{"recipename":"千层牛肉馅饼","recipetag":"鲁菜,家常菜,烙,咸,香,主食,饼,午餐,晚餐,一家三口","recipeimage":"http://i1.douguo.net/upload/caiku/5/a/f/5afd27a6b772e9f4fdcb3b16c3d15acf.jpeg","recipeid":"1424719","scene":"午"},{"recipename":"【酸汤（酸辣）金针肥牛】","recipetag":"家常菜,煮,酸辣,肉类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/8/d/a871625fb0465d8943fac13ce95abffd.jpg","recipeid":"1230318","scene":"午"},{"recipename":"美味酸辣蕨根粉","recipetag":"拌,酸辣,小吃,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/9/9/09bc521c65d75dcb3c662f4f0f7df6a9.jpg","recipeid":"1178630","scene":"晚"},{"recipename":"陈皮兔","recipetag":"家常菜,炒,下饭菜,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/9/b/3/9b64b43e5a372fce326c3eb5b3470fa3.jpeg","recipeid":"1292201","scene":"晚"},{"recipename":"（懒人美食）瘦肉蛋羹","recipetag":"家常菜,蒸,香,蛋类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/a/5/5/a56ae1dfcf85267b21c7fb74c0401145.jpg","recipeid":"1134591","scene":"晚"},{"recipename":"香辣鸡翅","recipetag":"家常菜,炸,香辣,禽类,午餐,晚餐","recipeimage":"http://i1.douguo.net/upload/caiku/0/5/5/05f043b08357e49b248c82a15453a1f5.jpg","recipeid":"1080209","scene":"晚"}]
             * fridgemac : DEF201921
             * userid : 201901121422302358
             * famaliyid : 201810261526170986
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
                public RecipesBean(String recipename, String recipetag, String recipeimage, String recipeid, String scene) {
                    this.recipename = recipename;
                    this.recipetag = recipetag;
                    this.recipeimage = recipeimage;
                    this.recipeid = recipeid;
                    this.scene = scene;
                }

                /**
                 * recipename : 蛋奶土豆饼
                 * recipetag : 家常菜,煎,主食,饼,早餐食谱,晚餐
                 * recipeimage : http://i1.douguo.net/upload/caiku/2/a/f/2aebe952109a02d73438abb032a7772f.jpg
                 * recipeid : 1109087
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
}
