package com.example.assignment3.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.assignment3.R;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.fragment.StudentListFragment;
import com.example.assignment3.model.Student;
import com.example.assignment3.service.BgAsync;
import com.example.assignment3.service.BgIntentService;
import com.example.assignment3.service.BgService;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;
import java.util.List;

public class ViewStudentActivity extends AppCompatActivity implements StudentListFragment.OnFragmentInteractionListener {


    private Bundle bundle;
    private Intent intent;
    private AddStudentFragment studentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        init();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Student student = bundle.getParcelable(StudentListActivity.EXTRA_SELECTED_STUDENT);
        studentFragment.viewStudent(student);
    }

    private void init() {

        bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        studentFragment = new AddStudentFragment();
        fragmentTransaction.add(R.id.myContainer, studentFragment, "");
        fragmentTransaction.commit();

    }

    @Override
    public void addData() {

    }

    @Override
    public void editData(int position) {

    }
}
