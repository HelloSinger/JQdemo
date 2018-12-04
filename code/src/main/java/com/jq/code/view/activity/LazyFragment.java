package com.jq.code.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import com.jq.code.R;
import com.jq.code.code.util.LogUtil;
import com.jq.code.code.util.NetworkHintUtil;
import com.jq.code.view.CustomToast;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by hfei on 2015/10/19.
 */
public class LazyFragment extends Fragment {

    private static final String TAG = LazyFragment.class.getSimpleName();
    protected View mParentView;

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i(TAG, "onHiddenChanged()");
        LogUtil.i(TAG, getClass().getSimpleName() + "ishidden:" + hidden);
    }

    @Override
    public void onResume() {
        LogUtil.i(TAG, "onResume()");
        super.onResume();
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
    }
    public boolean judgeNetWork(){
        return NetworkHintUtil.judgeNetWork(getActivity()) ;
    }
    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause()");
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_right_in,
                R.anim.slide_left_out);
    }

    public void showToast(String text) {
        CustomToast.showToast(getActivity(), text, Toast.LENGTH_SHORT);
    }

    public void showToast(int resId) {
        CustomToast.showToast(getActivity(), resId, Toast.LENGTH_SHORT);
    }

    public void showToastLong(int resId) {
        CustomToast.showToast(getActivity(), resId, Toast.LENGTH_LONG);
    }
    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected  void lazyLoad(){};
    protected void onInvisible(){}
}
