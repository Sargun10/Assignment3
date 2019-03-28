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

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity implements CommunicationFragments {
    public final static String EXTRA_STUDENT_LIST = "extra_student_list";
    public final static String EXTRA_INDEX = "index";
    public final static String EXTRA_SELECTED_STUDENT = "extra_selected_student";
    public final static String EXTRA_IS_FROM_VIEW = "is_from_view";
    public final static String EXTRA_IS_FROM_EDIT = "is_from_edit";
    public final static String EXTRA_IS_FROM_ADD = "is_from_add";
    public final static String EXTRA_IS_FROM_DELETE = "is_from_delete";
    private static final int ADD_STUDENT_FRAG = 1;
    private static final int STUD_LIST_FRAG = 0;
    public static final String BUNDLE_ARRAY_LIST = "studentList";
    private ViewPager viewPager;
    private Intent intent = getIntent();
    private TabLayout tabLayout;
    private ArrayList<Student> studentArrayList = new ArrayList<>();

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
                BaseFragment fragment = (BaseFragment) StudentListActivity.this.getSupportFragmentManager().getFragments().get(i);
                Bundle bundle;
                if (fragment.getArguments() != null) {
                    bundle = fragment.getArguments();

                } else {
                    bundle = new Bundle();
                }
                bundle.putParcelableArrayList(BUNDLE_ARRAY_LIST, studentArrayList);
                fragment.setArguments(bundle);
                Log.d("----------", "onPageSelected: ");
                fragment.refresh();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(Color.parseColor(R.color.colorPrimary)),;


    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem()>=1){
            viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
        }
        else {
            super.onBackPressed();
        }
    }

    public void changeTab() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        } else if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        }
    }

    private void setViewPager(ViewPager viewPager) {
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), studentArrayList);

        viewPager.setAdapter(myPagerAdapter);
    }
    @Override
    public void communicateAddStudent(Student student) {
        studentArrayList.add(student);
        Log.d("h", "communicateAddStudent: ");
        changeTab();
    }

    @Override
    public void communicateEditStudent(Bundle bundle) {
        studentArrayList=bundle.getParcelableArrayList(BUNDLE_ARRAY_LIST);
        Student student = bundle.getParcelable(EXTRA_SELECTED_STUDENT);
        int index = bundle.getInt(EXTRA_INDEX);
        Log.d("aaaaaaaaa", "communicateEditStudent: "+studentArrayList.size());
        Log.d("aaaaaaaaaa", "communicateEditStudent: "+index);

        studentArrayList.remove(index);
        Log.d("aaaaaaaaaa", "communicateEditStudent: "+studentArrayList.size());

        studentArrayList.add(index, student);
        changeTab();
    }

    @Override
    public void callOtherFragToAdd() {
        changeTab();

    }

    @Override
    public void getMode(Bundle bundle) {
        String tag = "android:switcher:" + R.id.viewPager + ":" + 1;
        AddStudentFragment addStudentFragment = (AddStudentFragment) getSupportFragmentManager().getFragments().get(1);
        if (addStudentFragment != null) {
            addStudentFragment.getMode(bundle);
            studentArrayList = bundle.getParcelableArrayList(BUNDLE_ARRAY_LIST);
        }
    }
}


