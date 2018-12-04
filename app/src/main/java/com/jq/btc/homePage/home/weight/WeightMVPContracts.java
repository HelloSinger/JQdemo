package com.jq.btc.homePage.home.weight;

import android.content.Context;

import com.jq.btc.homePage.home.LoadDataCallback;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.List;

/**
 * Created by lijh on 2017/5/31.
 */

public interface WeightMVPContracts {
    interface View {
        /**
         * 显示体重列表数据
         * @param dataList 体重列表数据
         */
        void showWeightDataList(List<WeightEntity> dataList);

        /**
         * 处理加载对话框
         * @param show true 显示对话框， false 隐藏对话框
         */
        void setLoadingUI(boolean show);

        void removeItem(int dataPosition);

        void showNextPage(List<WeightEntity> dataList);
        void showHandAddWeight(List<WeightEntity> dataList);
    }

    interface Presenter {
        void initData();
        void loadNextPageData();
        void setContext(Context context);
        void removeItem(int dataPosition);
        /** 获取正在显示的是哪个角色信息 */
        RoleInfo getShowingRoleInfo();
        /** 加载手动添加的体重 */
        void loadHandAddWeight();
    }

    interface Model {
        void initWeightData(LoadDataCallback callback);
        void setContext(Context context);
        void removeItem(int dataPosition);
        void loadNextPageData(LoadDataCallback callback);
        /** 获取正在显示的是哪个角色信息 */
        RoleInfo getShowingRoleInfo();
        /** 加载手动添加的体重 */
        void loadHandAddWeight(LoadDataCallback callback);
    }
}
