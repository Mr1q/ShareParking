package com.example.qjh.comprehensiveactivity.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.qjh.comprehensiveactivity.fragment.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewpageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    public ViewpageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }


    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }



    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public  void AddFragment(Fragment fragment)
    {
        mFragmentList.add(fragment);
    }

}
