package com.jq.btc.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jq.btc.homePage.home.haier.NormalFragment;

import java.util.List;

/**
 * Create by AYD on 2018/12/5
 */
public class ViewPagerMainAdapter extends FragmentPagerAdapter {

    List<NormalFragment> fragments;
    FragmentManager fragmentManager;


    public ViewPagerMainAdapter(FragmentManager fm, List<NormalFragment> fragments) {
        super(fm);
        this.fragments = fragments;
        this.fragmentManager = fm;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

//    @Override
//    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment fragment = (Fragment) super.instantiateItem(container, position);
//        this.fragmentManager.beginTransaction().show(fragment).commit();
//        return fragment;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
//        Fragment fragment = fragments.get(position);
//        fragmentManager.beginTransaction().hide(fragment).commit();
//    }
}
