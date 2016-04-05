package com.xm.zeus.view.home.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：小孩子xm on 2016-04-04 17:16
 * 邮箱：1065885952@qq.com
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<String> titles;
    private List<Fragment> pages;

    public HomePagerAdapter(FragmentManager fm, List<String> titles, List<Fragment> pages) {
        super(fm);
        this.titles = titles;
        this.pages = pages;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
