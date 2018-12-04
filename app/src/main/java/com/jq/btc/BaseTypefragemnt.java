package com.jq.btc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.jq.btc.homePage.holder.BaseTipViewHolder;
import com.jq.code.code.util.LogUtil;
import com.jq.code.model.TypeProperty;
import com.jq.code.view.CustomToast;

/**
 * Created by xulj on 2016/4/15.
 */
public class BaseTypefragemnt extends Fragment {
    public static final String TAG = BaseTypefragemnt.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.i(TAG, "onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.i(TAG, "onActivityCreated()");
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        LogUtil.i(TAG, "onCreateAnimation()");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.i(TAG, "setUserVisibleHint()");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i(TAG, "onHiddenChanged()");
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        LogUtil.i(TAG, "onAttach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG, "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i(TAG, "onDestroyView()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG, "onCreate()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.i(TAG, "onDetach()");
    }

    @Override
    public void onResume() {
        LogUtil.i(TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause()");
    }

    public void showToast(String text) {
        CustomToast.showToast(getActivity(), text, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }


    protected TypeProperty property;

    public TypeProperty getProperty() {
        return property;
    }

    private BaseTipViewHolder tipViewHolder;

    public BaseTipViewHolder getTipViewHolder() {
        return tipViewHolder == null ? BaseTipViewHolder.getDefault(getContext()) : tipViewHolder;
    }

    public void setTipViewHolder(BaseTipViewHolder tipViewHolder) {
        this.tipViewHolder = tipViewHolder;
    }
}
