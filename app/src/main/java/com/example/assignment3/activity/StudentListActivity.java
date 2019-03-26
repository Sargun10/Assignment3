package com.example.assignment3.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.assignment3.R;
import com.example.assignment3.adapter.MyPagerAdapter;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;
import com.example.assignment3.service.BgService;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.SortByName;
import com.example.assignment3.util.SortByRollNo;
import com.example.assignment3.util.ToastDisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentListActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener,AddStudentFragment.OnFragmentInteractionListener {
    public final static String EXTRA_STUDENT_LIST = "extra_student_list";
    public final static String EXTRA_INDEX = "index";
    public final static String EXTRA_SELECTED_STUDENT = "extra_selected_student";
    public final static String EXTRA_IS_FROM_VIEW = "is_from_view";
    public final static String EXTRA_IS_FROM_EDIT = "is_from_edit";
    public final static String EXTRA_IS_FROM_ADD = "is_from_add";
    public final static String EXTRA_IS_FROM_DELETE = "is_from_delete";
    private static final int ADD_STUDENT_FRAG = 1;
    private static final int STUD_LIST_FRAG = 0;
    private ViewPager viewPager;
    private Intent intent = getIntent();
    private TabLayout tabLayout;
    private ArrayList<Student> studentArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
//        DbHelper dbHelper=new DbHelper(this);
//        //  dbHelper.createTable();
//
//        studentArrayList=dbHelper.getAllStudents();
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (viewPager.getCurrentItem() == 1) {

                }

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(Color.parseColor(R.color.colorPrimary)),;


    }

//    public void changeTab() {
//        if (viewPager.getCurrentItem() == 0) {
//            viewPager.setCurrentItem(1);
//        } else if (viewPager.getCurrentItem() == 1) {
//            viewPager.setCurrentItem(0);
//        }
//    }

    private void setViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(myPagerAdapter);
    }

//    public void showData() {
//        getSupportFragmentManager().findFragmentByTag("Add Student");
//    }
//
//    @Override
//    public void addData(Bundle bundle) {
////        String tag = "android:switcher:" + R.id.viewPager + ":" + 0;
////        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
////        if (addStudentFragment != null) {
////            addStudentFragment.addStudent(bundle);
////            changeTab();
////        }
//
//    }
//
//    @Override
//    public void editData(Bundle bundle) {
//        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
//        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().findFragmentByTag(tag);
//        if (addStudentFragment != null) {
//            addStudentFragment.editStudent(bundle);
//            changeTab();
//        }
//    }


    @Override
    public void addData() {
        viewPager.setCurrentItem(ADD_STUDENT_FRAG);
    }

    @Override
    public void editData(int position) {

    }

    @Override
    public void updateData() {
//        viewPager.getAdapter().notifyDataSetChanged();
//        viewPager.setCurrentItem(STUD_LIST_FRAG);


    }
}


