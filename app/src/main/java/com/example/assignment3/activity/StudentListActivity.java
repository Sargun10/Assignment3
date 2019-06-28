package com.example.assignment3.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.assignment3.R;
import com.example.assignment3.adapter.MyPagerAdapter;
import com.example.assignment3.communicator.CommunicationFragments;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;
import com.example.assignment3.service.BgAsync;
import com.example.assignment3.util.Constants;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity implements CommunicationFragments {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Student> studentArrayList ;
    private AddStudentFragment addStudentFragment;
    private StudentListFragment studentListFragment;
    private  DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        dbHelper = new DbHelper(this);
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
                if (addStudentFragment != null) {
                    addStudentFragment.getMode(bundle);
                    addStudentFragment.scrolled();
                }
                if(i==1){
                    addStudentFragment.clearFields();
                }
                else
                    bundle.putBoolean(Constants.IS_FROM_ADD,true);
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
            viewPager.setCurrentItem(0);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), studentArrayList);
        viewPager.setAdapter(myPagerAdapter);
    }
    @Override
    public void communicateAddOrUpdateStudent(Student student, String mode) {
        studentListFragment = (StudentListFragment) getSupportFragmentManager().getFragments().get(0);
        studentListFragment.addOrUpdateStudentInList(student,mode);

    }

    @Override
    public void changeFragTab() {
        changeTab();
    }

    @Override
    public void getMode(Bundle bundle) {
        addStudentFragment = (AddStudentFragment) getSupportFragmentManager().getFragments().get(1);
        if (addStudentFragment != null) {
            addStudentFragment.getMode(bundle);
            studentArrayList = bundle.getParcelableArrayList(Constants.BUNDLE_ARRAY_LIST);
            addStudentFragment.scrolled();
        }
    }
//
//    @Override
//    public void refreshStudent() {
//        studentListFragment = new StudentListFragment();
//        studentListFragment.notifyAddedList();
//        changeTab();
//
//    }
}


