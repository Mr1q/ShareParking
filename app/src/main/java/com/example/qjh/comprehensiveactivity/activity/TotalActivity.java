package com.example.qjh.comprehensiveactivity.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
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
import com.example.qjh.comprehensiveactivity.fragment.SurroundFragment;
import com.example.qjh.comprehensiveactivity.fragment.ThirdFragment;
import com.example.qjh.comprehensiveactivity.fragment.UserFragment;
import com.example.qjh.comprehensiveactivity.utils.CreamUtils;


public class TotalActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private BottomNavigationView navigation_home;
    private MenuItem menuItem;
    private ViewpageAdapter viewpageAdapter;
    private UserFragment users = new UserFragment();
    private HomeFragment homeFragment = new HomeFragment();
    private ControlCarFragment controlCarFragment = new ControlCarFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_total);
        CreamUtils.translucentStatusBars(TotalActivity.this);
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
        homeFragment.setOnItemClick(new HomeFragment.OnItemClickListener() {
            @Override
            public void OnItemClick(int id) {
                switch (id) {
                    case R.id.ly_bookparlot:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.ly_controlcar:
                        viewPager.setCurrentItem(2);
                        break;
                }

            }
        });


        viewpageAdapter.AddFragment(homeFragment);  //测试
//        viewpageAdapter.AddFragment(new MapFragment());  //测试
        viewpageAdapter.AddFragment(new SurroundFragment());  //测试
        viewpageAdapter.AddFragment(controlCarFragment);  //控制蓝牙小车移动
        viewpageAdapter.AddFragment(users);  //用户界面
        viewPager.setAdapter(viewpageAdapter);
        viewPager.setOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(3);

    }

    private void initData() {
        viewPager = (ViewPager) findViewById(R.id.vp);
        navigation_home = (BottomNavigationView) findViewById(R.id.navigation_home);

        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        if (intent != null) {
            bundle.putString(LoginActivity.EXTRA_KEY_User_USERNAME, intent.getStringExtra(LoginActivity.EXTRA_KEY_User_USERNAME));
            bundle.putString(LoginActivity.EXTRA_KEY_User_PHONE, intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHONE));
            bundle.putString(LoginActivity.EXTRA_KEY_User_ADDRESS, intent.getStringExtra(LoginActivity.EXTRA_KEY_User_ADDRESS));
            bundle.putString(LoginActivity.EXTRA_KEY_User_NAME, intent.getStringExtra(LoginActivity.EXTRA_KEY_User_NAME));
            bundle.putString(LoginActivity.EXTRA_KEY_User_PHOTO, intent.getStringExtra(LoginActivity.EXTRA_KEY_User_PHOTO));
            users.setArguments(bundle);
        }
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };
        int[] colors = new int[]{getResources().getColor(R.color.Black),
                getResources().getColor(R.color.Blue)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        navigation_home.setItemTextColor(csl);
        navigation_home.setItemIconTintList(csl);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    CreamUtils.translucentStatusBars(TotalActivity.this);
                    return true;
                case R.id.navigation_share:
                    viewPager.setCurrentItem(1);
                    CreamUtils.translucentStatusBar(TotalActivity.this, false);
                    return true;
                case R.id.navigation_connect:
                    viewPager.setCurrentItem(2);
                    CreamUtils.translucentStatusBar(TotalActivity.this, false);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    CreamUtils.translucentStatusBar(TotalActivity.this, false);
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
        Log.d("current id", "id:" + i);
        switch (i) {
            case 0:
                CreamUtils.translucentStatusBars(TotalActivity.this);
                break;
            case 1:
                CreamUtils.translucentStatusBar(TotalActivity.this, false);
                break;
            case 2:
                CreamUtils.translucentStatusBar(TotalActivity.this, false);
                break;
            case 3:
                CreamUtils.translucentStatusBar(TotalActivity.this, false);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {


    }
}
