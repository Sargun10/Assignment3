package com.example.assignment3.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles = {"Students", "Add/Edit"};
ArrayList<Student> studentArrayList;
    public MyPagerAdapter(FragmentManager fm,ArrayList<Student> studentArrayList) {
        super(fm);
        this.studentArrayList=studentArrayList;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            Fragment fragment=new StudentListFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList("studentList",studentArrayList);
            fragment.setArguments(bundle);
            Log.d("tttttttttttttt","ponmchhc g");
            return fragment;}
        if (position == 1)
        {Fragment fragment=new AddStudentFragment();
            Bundle bundle=new Bundle();
            bundle.putParcelableArrayList("studentList",studentArrayList);
            fragment.setArguments(bundle);
            Log.d("tttttttttttttt","ponmchhc g");
            return fragment;}
//            return new AddStudentFragment();
        else return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
