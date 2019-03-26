package com.example.assignment3.service;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.assignment3.activity.ViewStudentActivity;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.database.table.StudentTable;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.model.Student;

public class BgService extends Service {
    public BgService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        intent.setAction(AddStudentFragment.SERVICE_FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        DbHelper dbHelper;
        String previousRollNo = new String();
        if(intent.hasExtra(AddStudentFragment.PREVIOUS_STUDENT_ID)) {
            previousRollNo = intent.getStringExtra(AddStudentFragment.PREVIOUS_STUDENT_ID);
        }
        String mode = intent.getStringExtra(AddStudentFragment.MODE);
        Student student = intent.getParcelableExtra(AddStudentFragment.PRESENT_STUDENT);
        switch (mode) {
            case StudentListActivity.EXTRA_IS_FROM_ADD:
                dbHelper= new DbHelper(this);
                dbHelper.insertQuery(student);
                break;
            case StudentListActivity.EXTRA_IS_FROM_EDIT:
                dbHelper = new DbHelper(this);
                dbHelper.updateQuery(student,previousRollNo);
                break;
            case StudentListActivity.EXTRA_IS_FROM_DELETE:
                dbHelper = new DbHelper(this);
                dbHelper.deleteQuery(student);
                break;
        }
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
