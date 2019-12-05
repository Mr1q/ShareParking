package com.example.qjh.comprehensiveactivity.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.qjh.comprehensiveactivity.R;
import com.example.qjh.comprehensiveactivity.adapter.ViewpageAdapter;
import com.example.qjh.comprehensiveactivity.controler.BaseActivity;
import com.example.qjh.comprehensiveactivity.fragment.ControlCarFragment;
import com.example.qjh.comprehensiveactivity.fragment.HomeFragment;
import com.example.qjh.comprehensiveactivity.fragment.MapFragment;
import com.example.qjh.comprehensiveactivity.fragment.ThirdFragment;
import com.example.qjh.comprehensiveactivity.fragment.UserFragment;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;


public class TotalActivity extends BaseActivity implements  ViewPager.OnPageChangeListener{
    private ViewPager viewPager;
    private BottomNavigationView navigation_home;
    private MenuItem menuItem;
    private ViewpageAdapter viewpageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_total);
        initData();


        navigation_home.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigation_home.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigation_home.getMenu().getItem(i);
                menuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        viewpageAdapter = new ViewpageAdapter(getSupportFragmentManager());

        viewpageAdapter.AddFragment(new HomeFragment());  //测试
        viewpageAdapter.AddFragment(new MapFragment());  //测试
        viewpageAdapter.AddFragment(new ControlCarFragment());  //控制蓝牙小车移动
        viewpageAdapter.AddFragment(new UserFragment());  //用户界面
        viewPager.setAdapter(viewpageAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);
    }

    private void initData() {
        viewPager = (ViewPager) findViewById(R.id.vp);
        navigation_home = (BottomNavigationView) findViewById(R.id.navigation_home);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    CreamUtils.translucentStatusBar(TotalActivity.this,false);
                    return true;
                case R.id.navigation_share:
                    viewPager.setCurrentItem(1);
                    CreamUtils.translucentStatusBar(TotalActivity.this,true);
                    return true;
                case R.id.navigation_connect:
                    viewPager.setCurrentItem(2);
                    CreamUtils.translucentStatusBar(TotalActivity.this,false);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    CreamUtils.translucentStatusBar(TotalActivity.this,false);
                    return true;
            }
            return false;
        }
    };




    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        Log.d("current id","id:"+i);
        switch (i)
        {
            case 0:
                CreamUtils.translucentStatusBar(TotalActivity.this,false);
                break;
            case 1:
                CreamUtils.translucentStatusBar(TotalActivity.this,true);
                break;
            case 2:
                CreamUtils.translucentStatusBar(TotalActivity.this,false);
                break;
            case 3:
                CreamUtils.translucentStatusBar(TotalActivity.this,false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {


    }
}
