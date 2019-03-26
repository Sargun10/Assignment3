package com.example.assignment3.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragmentArrayList;
    private ArrayList<String> fragmentTitleArrayList;
    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentArrayList=new ArrayList<>();
        fragmentTitleArrayList=new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }
    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitleArrayList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleArrayList.get(position);
    }
}
