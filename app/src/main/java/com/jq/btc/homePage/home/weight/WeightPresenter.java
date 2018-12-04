package com.jq.btc.homePage.home.weight;

import android.content.Context;

import com.jq.btc.homePage.home.LoadDataCallback;
import com.jq.code.model.RoleInfo;
import com.jq.code.model.WeightEntity;

import java.util.List;

/**
 * Created by lijh on 2017/5/31.
 */

public class WeightPresenter implements WeightMVPContracts.Presenter {

    private final WeightMVPContracts.View mView;
    private final WeightMVPContracts.Model nModel;
    private boolean mIsLoadingNextPage = false;

    public WeightPresenter(WeightMVPContracts.View view) {
        mView = view;
        nModel = new WeightModel();
    }

    @Override
    public void setContext(Context context) {
        nModel.setContext(context);
    }

    @Override
    public void initData() {
        LoadDataCallback callback = new LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mView.setLoadingUI(false);
                mView.showWeightDataList((List<WeightEntity>)data);
            }

            @Override
            public void onFailure(String msg, int code) {
                mView.setLoadingUI(false);
            }
        };

        mView.setLoadingUI(true);
        nModel.initWeightData(callback);
    }

    @Override
    public RoleInfo getShowingRoleInfo() {
        return nModel.getShowingRoleInfo();
    }

    @Override
    public void loadNextPageData() {
        if(mIsLoadingNextPage) {
            return;
        }

        nModel.loadNextPageData(new LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mView.showNextPage((List<WeightEntity>)data);
                mIsLoadingNextPage = false;
            }

            @Override
            public void onFailure(String msg, int code) {
                mIsLoadingNextPage = false;
            }
        });
        mIsLoadingNextPage = true;
    }

    @Override
    public void loadHandAddWeight() {
        nModel.loadHandAddWeight(new LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mView.showHandAddWeight((List<WeightEntity>)data);
            }

            @Override
            public void onFailure(String msg, int code) {
            }
        });
    }

    @Override
    public void removeItem(int dataPosition) {
        nModel.removeItem(dataPosition);
        mView.removeItem(dataPosition);
    }
}
