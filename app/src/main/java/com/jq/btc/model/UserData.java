package com.jq.btc.model;

import java.util.List;

/**
 * Create by AYD on 2018/12/12
 */
public class UserData {

    /**
     * ok : true
     * code : 200
     * message : 成功
     * data : {"memberList":[{"familyMemeberId":"201812111459031880","familyMemberId":"201812111459031880","nickName":"hhg","sex":"男","birthday":"1994-12-11","height":"175","weight":"73","avatarUrl":"","preferences":[{"preferenceId":"1","preferenceName":"饮食偏好","is_require":true,"tags":[{"preferenceTagId":"6","preferenceTagName":"家常味","is_choose":true},{"preferenceTagId":"7","preferenceTagName":"酸","is_choose":false},{"preferenceTagId":"8","preferenceTagName":"清淡","is_choose":false},{"preferenceTagId":"9","preferenceTagName":"辣","is_choose":false},{"preferenceTagId":"10","preferenceTagName":"甜","is_choose":false}]},{"preferenceId":"2","preferenceName":"饮食养生","is_require":false,"tags":[{"preferenceTagId":"11","preferenceTagName":"减肥","is_choose":false},{"preferenceTagId":"12","preferenceTagName":"降血糖","is_choose":false},{"preferenceTagId":"13","preferenceTagName":"保肝补血","is_choose":false},{"preferenceTagId":"14","preferenceTagName":"健脾养胃","is_choose":false},{"preferenceTagId":"15","preferenceTagName":"补钙","is_choose":false}]},{"preferenceId":"3","preferenceName":"视频偏好","is_require":false,"tags":[{"preferenceTagId":"16","preferenceTagName":"都市篇","is_choose":true},{"preferenceTagId":"17","preferenceTagName":"剧情片","is_choose":false},{"preferenceTagId":"18","preferenceTagName":"家庭剧","is_choose":false},{"preferenceTagId":"19","preferenceTagName":"真人秀","is_choose":false},{"preferenceTagId":"20","preferenceTagName":"动漫","is_choose":false}]},{"preferenceId":"4","preferenceName":"音乐偏好","is_require":false,"tags":[{"preferenceTagId":"21","preferenceTagName":"70后","is_choose":false},{"preferenceTagId":"22","preferenceTagName":"流行","is_choose":false},{"preferenceTagId":"23","preferenceTagName":"摇滚","is_choose":false},{"preferenceTagId":"24","preferenceTagName":"民谣","is_choose":false},{"preferenceTagId":"25","preferenceTagName":"轻音乐","is_choose":false}]},{"preferenceId":"5","preferenceName":"音频偏好","is_require":false,"tags":[{"preferenceTagId":"26","preferenceTagName":"电台","is_choose":false},{"preferenceTagId":"27","preferenceTagName":"有声书","is_choose":false},{"preferenceTagId":"28","preferenceTagName":"相声","is_choose":false},{"preferenceTagId":"29","preferenceTagName":"资讯","is_choose":false},{"preferenceTagId":"30","preferenceTagName":"娱乐","is_choose":false}]},{"preferenceId":"31","preferenceName":"电台偏好","is_require":false,"tags":[{"preferenceTagId":"32","preferenceTagName":"相声小品","is_choose":false},{"preferenceTagId":"33","preferenceTagName":"中国之声","is_choose":false},{"preferenceTagId":"34","preferenceTagName":"评书","is_choose":false},{"preferenceTagId":"35","preferenceTagName":"财经","is_choose":false},{"preferenceTagId":"36","preferenceTagName":"健康","is_choose":false}]}]},{"familyMemeberId":"201812111500551883","familyMemberId":"201812111500551883","nickName":"xxx","sex":"男","birthday":"2007-12-11","height":"150","weight":"50","avatarUrl":"","preferences":[{"preferenceId":"1","preferenceName":"饮食偏好","is_require":true,"tags":[{"preferenceTagId":"6","preferenceTagName":"家常味","is_choose":false},{"preferenceTagId":"7","preferenceTagName":"酸","is_choose":false},{"preferenceTagId":"8","preferenceTagName":"清淡","is_choose":true},{"preferenceTagId":"9","preferenceTagName":"辣","is_choose":false},{"preferenceTagId":"10","preferenceTagName":"甜","is_choose":false}]},{"preferenceId":"2","preferenceName":"饮食养生","is_require":false,"tags":[{"preferenceTagId":"11","preferenceTagName":"减肥","is_choose":false},{"preferenceTagId":"12","preferenceTagName":"降血糖","is_choose":false},{"preferenceTagId":"13","preferenceTagName":"保肝补血","is_choose":false},{"preferenceTagId":"14","preferenceTagName":"健脾养胃","is_choose":false},{"preferenceTagId":"15","preferenceTagName":"补钙","is_choose":false}]},{"preferenceId":"3","preferenceName":"视频偏好","is_require":false,"tags":[{"preferenceTagId":"16","preferenceTagName":"都市篇","is_choose":false},{"preferenceTagId":"17","preferenceTagName":"剧情片","is_choose":false},{"preferenceTagId":"18","preferenceTagName":"家庭剧","is_choose":false},{"preferenceTagId":"19","preferenceTagName":"真人秀","is_choose":false},{"preferenceTagId":"20","preferenceTagName":"动漫","is_choose":false}]},{"preferenceId":"4","preferenceName":"音乐偏好","is_require":false,"tags":[{"preferenceTagId":"21","preferenceTagName":"70后","is_choose":false},{"preferenceTagId":"22","preferenceTagName":"流行","is_choose":false},{"preferenceTagId":"23","preferenceTagName":"摇滚","is_choose":false},{"preferenceTagId":"24","preferenceTagName":"民谣","is_choose":false},{"preferenceTagId":"25","preferenceTagName":"轻音乐","is_choose":false}]},{"preferenceId":"5","preferenceName":"音频偏好","is_require":false,"tags":[{"preferenceTagId":"26","preferenceTagName":"电台","is_choose":false},{"preferenceTagId":"27","preferenceTagName":"有声书","is_choose":false},{"preferenceTagId":"28","preferenceTagName":"相声","is_choose":false},{"preferenceTagId":"29","preferenceTagName":"资讯","is_choose":false},{"preferenceTagId":"30","preferenceTagName":"娱乐","is_choose":false}]},{"preferenceId":"31","preferenceName":"电台偏好","is_require":false,"tags":[{"preferenceTagId":"32","preferenceTagName":"相声小品","is_choose":false},{"preferenceTagId":"33","preferenceTagName":"中国之声","is_choose":false},{"preferenceTagId":"34","preferenceTagName":"评书","is_choose":false},{"preferenceTagId":"35","preferenceTagName":"财经","is_choose":false},{"preferenceTagId":"36","preferenceTagName":"健康","is_choose":false}]}]}]}
     */

    private boolean ok;
    private int code;
    private String message;
    private DataBean data;

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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
        private List<MemberListBean> memberList;

        public List<MemberListBean> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListBean> memberList) {
            this.memberList = memberList;
        }

        public static class MemberListBean {
            /**
             * familyMemeberId : 201812111459031880
             * familyMemberId : 201812111459031880
             * nickName : hhg
             * sex : 男
             * birthday : 1994-12-11
             * height : 175
             * weight : 73
             * avatarUrl :
             * preferences : [{"preferenceId":"1","preferenceName":"饮食偏好","is_require":true,"tags":[{"preferenceTagId":"6","preferenceTagName":"家常味","is_choose":true},{"preferenceTagId":"7","preferenceTagName":"酸","is_choose":false},{"preferenceTagId":"8","preferenceTagName":"清淡","is_choose":false},{"preferenceTagId":"9","preferenceTagName":"辣","is_choose":false},{"preferenceTagId":"10","preferenceTagName":"甜","is_choose":false}]},{"preferenceId":"2","preferenceName":"饮食养生","is_require":false,"tags":[{"preferenceTagId":"11","preferenceTagName":"减肥","is_choose":false},{"preferenceTagId":"12","preferenceTagName":"降血糖","is_choose":false},{"preferenceTagId":"13","preferenceTagName":"保肝补血","is_choose":false},{"preferenceTagId":"14","preferenceTagName":"健脾养胃","is_choose":false},{"preferenceTagId":"15","preferenceTagName":"补钙","is_choose":false}]},{"preferenceId":"3","preferenceName":"视频偏好","is_require":false,"tags":[{"preferenceTagId":"16","preferenceTagName":"都市篇","is_choose":true},{"preferenceTagId":"17","preferenceTagName":"剧情片","is_choose":false},{"preferenceTagId":"18","preferenceTagName":"家庭剧","is_choose":false},{"preferenceTagId":"19","preferenceTagName":"真人秀","is_choose":false},{"preferenceTagId":"20","preferenceTagName":"动漫","is_choose":false}]},{"preferenceId":"4","preferenceName":"音乐偏好","is_require":false,"tags":[{"preferenceTagId":"21","preferenceTagName":"70后","is_choose":false},{"preferenceTagId":"22","preferenceTagName":"流行","is_choose":false},{"preferenceTagId":"23","preferenceTagName":"摇滚","is_choose":false},{"preferenceTagId":"24","preferenceTagName":"民谣","is_choose":false},{"preferenceTagId":"25","preferenceTagName":"轻音乐","is_choose":false}]},{"preferenceId":"5","preferenceName":"音频偏好","is_require":false,"tags":[{"preferenceTagId":"26","preferenceTagName":"电台","is_choose":false},{"preferenceTagId":"27","preferenceTagName":"有声书","is_choose":false},{"preferenceTagId":"28","preferenceTagName":"相声","is_choose":false},{"preferenceTagId":"29","preferenceTagName":"资讯","is_choose":false},{"preferenceTagId":"30","preferenceTagName":"娱乐","is_choose":false}]},{"preferenceId":"31","preferenceName":"电台偏好","is_require":false,"tags":[{"preferenceTagId":"32","preferenceTagName":"相声小品","is_choose":false},{"preferenceTagId":"33","preferenceTagName":"中国之声","is_choose":false},{"preferenceTagId":"34","preferenceTagName":"评书","is_choose":false},{"preferenceTagId":"35","preferenceTagName":"财经","is_choose":false},{"preferenceTagId":"36","preferenceTagName":"健康","is_choose":false}]}]
             */

            private String familyMemeberId;
            private String familyMemberId;
            private String nickName;
            private String sex;
            private String birthday;
            private String height;
            private String weight;
            private String avatarUrl;
            private List<PreferencesBean> preferences;

            public String getFamilyMemeberId() {
                return familyMemeberId;
            }

            public void setFamilyMemeberId(String familyMemeberId) {
                this.familyMemeberId = familyMemeberId;
            }

            public String getFamilyMemberId() {
                return familyMemberId;
            }

            public void setFamilyMemberId(String familyMemberId) {
                this.familyMemberId = familyMemberId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getBirthday() {
                return birthday;
            }

            public void setBirthday(String birthday) {
                this.birthday = birthday;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getWeight() {
                return weight;
            }

            public void setWeight(String weight) {
                this.weight = weight;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public List<PreferencesBean> getPreferences() {
                return preferences;
            }

            public void setPreferences(List<PreferencesBean> preferences) {
                this.preferences = preferences;
            }

            public static class PreferencesBean {
                /**
                 * preferenceId : 1
                 * preferenceName : 饮食偏好
                 * is_require : true
                 * tags : [{"preferenceTagId":"6","preferenceTagName":"家常味","is_choose":true},{"preferenceTagId":"7","preferenceTagName":"酸","is_choose":false},{"preferenceTagId":"8","preferenceTagName":"清淡","is_choose":false},{"preferenceTagId":"9","preferenceTagName":"辣","is_choose":false},{"preferenceTagId":"10","preferenceTagName":"甜","is_choose":false}]
                 */

                private String preferenceId;
                private String preferenceName;
                private boolean is_require;
                private List<TagsBean> tags;

                public String getPreferenceId() {
                    return preferenceId;
                }

                public void setPreferenceId(String preferenceId) {
                    this.preferenceId = preferenceId;
                }

                public String getPreferenceName() {
                    return preferenceName;
                }

                public void setPreferenceName(String preferenceName) {
                    this.preferenceName = preferenceName;
                }

                public boolean isIs_require() {
                    return is_require;
                }

                public void setIs_require(boolean is_require) {
                    this.is_require = is_require;
                }

                public List<TagsBean> getTags() {
                    return tags;
                }

                public void setTags(List<TagsBean> tags) {
                    this.tags = tags;
                }

                public static class TagsBean {
                    /**
                     * preferenceTagId : 6
                     * preferenceTagName : 家常味
                     * is_choose : true
                     */

                    private String preferenceTagId;
                    private String preferenceTagName;
                    private boolean is_choose;

                    public String getPreferenceTagId() {
                        return preferenceTagId;
                    }

                    public void setPreferenceTagId(String preferenceTagId) {
                        this.preferenceTagId = preferenceTagId;
                    }

                    public String getPreferenceTagName() {
                        return preferenceTagName;
                    }

                    public void setPreferenceTagName(String preferenceTagName) {
                        this.preferenceTagName = preferenceTagName;
                    }

                    public boolean isIs_choose() {
                        return is_choose;
                    }

                    public void setIs_choose(boolean is_choose) {
                        this.is_choose = is_choose;
                    }
                }
            }
        }
    }
}
