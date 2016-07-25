package com.std.logisticapp.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.std.logisticapp.ui.fragment.DeliveryListFragment;

import java.util.List;

/**
 * Created by Eric on 2016/5/12.
 */
public class DeliveryViewPagerAdapter extends FragmentPagerAdapter {
    FragmentManager mFragmentManager;
    //private static final String[] TITLES = new String[]{"全部","配送","催单","延期","投诉"};
    private static final String[] TITLES = new String[]{"配送","催单","延期","投诉"};
    public Fragment currentFragment;
    String mSearch;

    public DeliveryViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.mFragmentManager = fm;
    }

    public void setSearch(String search) {
        mSearch = search;
    }

    @Override
    public Fragment getItem(int position) {
        DeliveryListFragment fragment = new DeliveryListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Title",getPageTitle(position).toString());
        bundle.putInt("Position",position%TITLES.length);
        fragment.setArguments(bundle);
        return fragment;
    }

    public Object instantiateItem(ViewGroup container, int position) {
        DeliveryListFragment fragment = (DeliveryListFragment)super.instantiateItem(container, position);
        fragment.resetFragmentData(mSearch);
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position%TITLES.length];
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (DeliveryListFragment) object;
        super.setPrimaryItem(container, position, object);
    }
}
