package com.example.assignment3.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.assignment3.R;
import com.example.assignment3.adapter.MyPagerAdapter;
import com.example.assignment3.communicator.CommunicationFragments;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.fragment.BaseFragment;
import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity implements CommunicationFragments {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Student> studentArrayList ;
    AddStudentFragment addStudentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        DbHelper dbHelper = new DbHelper(this);
        //  dbHelper.createTable();

        studentArrayList = dbHelper.getAllStudents();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(Constants.IS_FROM_ADD,true);
                bundle.putParcelableArrayList(Constants.BUNDLE_ARRAY_LIST,studentArrayList);
                addStudentFragment = (AddStudentFragment) getSupportFragmentManager().getFragments().get(1);
                Log.d("----", "onPageScrollStateChanged: "+ "here");
                if (addStudentFragment != null) {
                    addStudentFragment.getMode(bundle);
                }
//                if(i==1){
//                    addStudentFragment.clearFields();
//                }
//                else
//                    bundle.putBoolean(Constants.IS_FROM_ADD,true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }

            });
        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()>=1){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            addStudentFragment.clearFields();
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * to move to next tab
     * toggling between two tabs
     */
    public void changeTab() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        } else if (viewPager.getCurrentItem() == 1) {
            AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().getFragments().get(1);
            addStudentFragment.clearFields();
            viewPager.setCurrentItem(0);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), studentArrayList);
        viewPager.setAdapter(myPagerAdapter);
    }
    @Override
    public void communicateAddStudent(Student student) {
        Log.d("----------", "communicateAddStudent: "+studentArrayList.size());
        studentArrayList.add(student);
        Log.d("h", "communicateAddStudent: ");
        changeTab();
    }

    @Override
    public void communicateEditStudent(Bundle bundle) {
        studentArrayList=bundle.getParcelableArrayList(Constants.BUNDLE_ARRAY_LIST);
        Student student = bundle.getParcelable(Constants.SELECTED_STUDENT);
        int index = bundle.getInt(Constants.INDEX);
        Log.d("----------", "communicateEditStudent: "+student.getName());
        studentArrayList.remove(index);
        studentArrayList.add(index,student);
        changeTab();
    }

    @Override
    public void callOtherFragToAdd() {
        changeTab();
    }

    @Override
    public void getMode(Bundle bundle) {
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().getFragments().get(1);
        if (addStudentFragment != null) {
            addStudentFragment.getMode(bundle);
            studentArrayList = bundle.getParcelableArrayList(Constants.BUNDLE_ARRAY_LIST);
        }
    }

    @Override
    public void refreshStudent() {
        StudentListFragment studentListFragment = new StudentListFragment();
        studentListFragment.notifyAddedList();
        changeTab();

    }
}


