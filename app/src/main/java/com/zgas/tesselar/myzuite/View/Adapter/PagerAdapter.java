package com.zgas.tesselar.myzuite.View.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by project on 05/10/2016.
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }

    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
