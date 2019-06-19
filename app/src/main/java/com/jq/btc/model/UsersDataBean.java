package com.jq.btc.model;

import java.util.List;

/**
 * @Author: AnYaodong
 * @CreateDate: 2019/5/5 14:17
 * @UpdateRemark: 更新说明
 */
public class UsersDataBean {


    /**
     * ok : true
     * code : 200
     * message : 家庭成员信息获取成功
     * data : [{"birthday":807321600000,"healthEvaluateLevel":"A+","preferenceInfo":{"douguo":[],"wangyiyunyinyue":[],"youku":[],"qingting":[]},"groupId":"1555924765382","healthEvaluateContent":"非常健康，数一数二的健康状态","memberLabelInfo":{"mxb":["高血压","高血脂"],"kwph":["川菜","湘菜","粤菜","鲁菜"]},"isMaster":true,"cellphone":"18354222312","height":171.8,"currentHealth":"zqtz","memberInfoId":508,"identityName":"群主","nickName":"父亲","sex":1,"weight":66.5,"updateTime":1555924868000,"userId":86401,"recommendHealth":"zqtz","headImgUrl":"http://www.google.com","createTime":1555924868000,"chooseHealthTime":1555924765461},{"birthday":807321600000,"healthEvaluateLevel":"A+","preferenceInfo":{"douguo":[],"wangyiyunyinyue":[{"type":"wangyiyunyinyue","categoryName":"欧美","collect":false,"categoryId":"语种","selected":false}],"youku":[{"type":"youku","categoryName":"电影","collect":false,"categoryId":"Movies","selected":false}],"qingting":[{"type":"qingting","categoryName":"相声小品","collect":false,"categoryId":"527","selected":false}]},"groupId":"1555924765382","additional":"{\"faceId\":\"123456789\",\"faceIdState\":true,\"remoteControlState\":false,\"sprotRecordState\":false}","remoteControlState":false,"faceId":"123456789","faceIdState":true,"healthEvaluateContent":"非常健康，数一数二的健康状态","memberLabelInfo":{"mxb":["高血压","高血脂","高血糖","骨质疏松","脂肪肝"],"kwph":["川菜","湘菜","粤菜","鲁菜"]},"isMaster":false,"sprotRecordState":false,"height":172,"currentHealth":"zqtz","memberInfoId":509,"identityName":"哥哥","nickName":"gege","sex":1,"preference":"{\"youku\":[{\"categoryId\":\"Movies\",\"categoryName\":\"电影\",\"type\":\"youku\",\"selected\":false,\"collect\":false}],\"wangyiyunyinyue\":[{\"categoryId\":\"语种\",\"categoryName\":\"欧美\",\"type\":\"wangyiyunyinyue\",\"selected\":false,\"collect\":false}],\"qingting\":[{\"categoryId\":\"527\",\"categoryName\":\"相声小品\",\"type\":\"qingting\",\"selected\":false,\"collect\":false}]}","weight":64.2,"updateTime":1555925415000,"recommendHealth":"zqtz","headImgUrl":"http://www.baidu.com","createTime":1555925415000,"chooseHealthTime":1555925312402}]
     */

    private String ok;
    private String code;
    private String message;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * birthday : 807321600000
         * healthEvaluateLevel : A+
         * preferenceInfo : {"douguo":[],"wangyiyunyinyue":[],"youku":[],"qingting":[]}
         * groupId : 1555924765382
         * healthEvaluateContent : 非常健康，数一数二的健康状态
         * memberLabelInfo : {"mxb":["高血压","高血脂"],"kwph":["川菜","湘菜","粤菜","鲁菜"]}
         * isMaster : true
         * cellphone : 18354222312
         * height : 171.8
         * currentHealth : zqtz
         * memberInfoId : 508
         * identityName : 群主
         * nickName : 父亲
         * sex : 1
         * weight : 66.5
         * updateTime : 1555924868000
         * userId : 86401
         * recommendHealth : zqtz
         * headImgUrl : http://www.google.com
         * createTime : 1555924868000
         * chooseHealthTime : 1555924765461
         * additional : {"faceId":"123456789","faceIdState":true,"remoteControlState":false,"sprotRecordState":false}
         * remoteControlState : false
         * faceId : 123456789
         * faceIdState : true
         * sprotRecordState : false
         * preference : {"youku":[{"categoryId":"Movies","categoryName":"电影","type":"youku","selected":false,"collect":false}],"wangyiyunyinyue":[{"categoryId":"语种","categoryName":"欧美","type":"wangyiyunyinyue","selected":false,"collect":false}],"qingting":[{"categoryId":"527","categoryName":"相声小品","type":"qingting","selected":false,"collect":false}]}
         */

        private long birthday;
        private String healthEvaluateLevel;
        private PreferenceInfoBean preferenceInfo;
        private String groupId;
        private String healthEvaluateContent;
        private MemberLabelInfoBean memberLabelInfo;
        private boolean isMaster;
        private String cellphone;
        private double height;
        private String currentHealth;
        private int memberInfoId;
        private String identityName;
        private String nickName;
        private int sex;
        private double weight;
        private long updateTime;
        private int userId;
        private String recommendHealth;
        private String headImgUrl;
        private long createTime;
        private long chooseHealthTime;
        private String additional;
        private boolean remoteControlState;
        private String faceId;
        private boolean faceIdState;
        private boolean sprotRecordState;
        private String preference;

        public long getBirthday() {
            return birthday;
        }

        public void setBirthday(long birthday) {
            this.birthday = birthday;
        }

        public String getHealthEvaluateLevel() {
            return healthEvaluateLevel;
        }

        public void setHealthEvaluateLevel(String healthEvaluateLevel) {
            this.healthEvaluateLevel = healthEvaluateLevel;
        }

        public PreferenceInfoBean getPreferenceInfo() {
            return preferenceInfo;
        }

        public void setPreferenceInfo(PreferenceInfoBean preferenceInfo) {
            this.preferenceInfo = preferenceInfo;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getHealthEvaluateContent() {
            return healthEvaluateContent;
        }

        public void setHealthEvaluateContent(String healthEvaluateContent) {
            this.healthEvaluateContent = healthEvaluateContent;
        }

        public MemberLabelInfoBean getMemberLabelInfo() {
            return memberLabelInfo;
        }

        public void setMemberLabelInfo(MemberLabelInfoBean memberLabelInfo) {
            this.memberLabelInfo = memberLabelInfo;
        }

        public boolean isIsMaster() {
            return isMaster;
        }

        public void setIsMaster(boolean isMaster) {
            this.isMaster = isMaster;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public String getCurrentHealth() {
            return currentHealth;
        }

        public void setCurrentHealth(String currentHealth) {
            this.currentHealth = currentHealth;
        }

        public int getMemberInfoId() {
            return memberInfoId;
        }

        public void setMemberInfoId(int memberInfoId) {
            this.memberInfoId = memberInfoId;
        }

        public String getIdentityName() {
            return identityName;
        }

        public void setIdentityName(String identityName) {
            this.identityName = identityName;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getRecommendHealth() {
            return recommendHealth;
        }

        public void setRecommendHealth(String recommendHealth) {
            this.recommendHealth = recommendHealth;
        }

        public String getHeadImgUrl() {
            return headImgUrl;
        }

        public void setHeadImgUrl(String headImgUrl) {
            this.headImgUrl = headImgUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getChooseHealthTime() {
            return chooseHealthTime;
        }

        public void setChooseHealthTime(long chooseHealthTime) {
            this.chooseHealthTime = chooseHealthTime;
        }

        public String getAdditional() {
            return additional;
        }

        public void setAdditional(String additional) {
            this.additional = additional;
        }

        public boolean isRemoteControlState() {
            return remoteControlState;
        }

        public void setRemoteControlState(boolean remoteControlState) {
            this.remoteControlState = remoteControlState;
        }

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public boolean isFaceIdState() {
            return faceIdState;
        }

        public void setFaceIdState(boolean faceIdState) {
            this.faceIdState = faceIdState;
        }

        public boolean isSprotRecordState() {
            return sprotRecordState;
        }

        public void setSprotRecordState(boolean sprotRecordState) {
            this.sprotRecordState = sprotRecordState;
        }

        public String getPreference() {
            return preference;
        }

        public void setPreference(String preference) {
            this.preference = preference;
        }

        public static class PreferenceInfoBean {
            private List<?> douguo;
            private List<?> wangyiyunyinyue;
            private List<?> youku;
            private List<?> qingting;

            public List<?> getDouguo() {
                return douguo;
            }

            public void setDouguo(List<?> douguo) {
                this.douguo = douguo;
            }

            public List<?> getWangyiyunyinyue() {
                return wangyiyunyinyue;
            }

            public void setWangyiyunyinyue(List<?> wangyiyunyinyue) {
                this.wangyiyunyinyue = wangyiyunyinyue;
            }

            public List<?> getYouku() {
                return youku;
            }

            public void setYouku(List<?> youku) {
                this.youku = youku;
            }

            public List<?> getQingting() {
                return qingting;
            }

            public void setQingting(List<?> qingting) {
                this.qingting = qingting;
            }
        }

        public static class MemberLabelInfoBean {
            private List<String> mxb;
            private List<String> kwph;

            public List<String> getMxb() {
                return mxb;
            }

            public void setMxb(List<String> mxb) {
                this.mxb = mxb;
            }

            public List<String> getKwph() {
                return kwph;
            }

            public void setKwph(List<String> kwph) {
                this.kwph = kwph;
            }
        }
    }

}
