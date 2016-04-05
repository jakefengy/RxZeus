package com.xm.zeus.view.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xm.zeus.R;
import com.xm.zeus.view.home.adapter.HomePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Activity_Home extends AppCompatActivity {

    //
    @Bind(R.id.home_content)
    ViewPager viewPager;
    @Bind(R.id.home_toolbar)
    Toolbar toolbar;
    @Bind(R.id.home_tab)
    TabLayout tab;

    // children
    private Fragment fragmentRecent, fragmentMe, fragmentContacts;

    public static Intent getHomeIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, Activity_Home.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initToolbar();
        initTabPager();
    }

    private void initToolbar() {
        toolbar.setTitle("微微助理");
    }

    private void initTabPager() {
        List<Fragment> pages = new ArrayList<>();

        fragmentRecent = Fragment_Recent.newInstance();
        fragmentContacts = Fragment_Contacts.newInstance();
        fragmentMe = Fragment_Me.newInstance();

        pages.add(fragmentRecent);
        pages.add(fragmentContacts);
        pages.add(fragmentMe);

        List<String> titles = new ArrayList<>();
        titles.add("沟通");
        titles.add("通讯录");
        titles.add("我");

        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager(), titles, pages);
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
