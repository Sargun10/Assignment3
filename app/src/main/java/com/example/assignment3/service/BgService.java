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
import com.example.assignment3.util.Constants;

public class BgService extends Service {
    public BgService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DbHelper dbHelper= new DbHelper(this);
        intent.setAction(Constants.SERVICE_FILTER_ACTION_KEY);
        String previousRollNo = new String();
        if(intent.hasExtra(Constants.PREVIOUS_STUDENT_ID)) {
            previousRollNo = intent.getStringExtra(Constants.PREVIOUS_STUDENT_ID);
        }
        String mode = intent.getStringExtra(Constants.MODE);
        Log.d("---------", "onStartCommand: "+mode);
        Student student = intent.getParcelableExtra(Constants.PRESENT_STUDENT);
        switch (mode) {
            case Constants.IS_FROM_ADD:
                dbHelper.insertQuery(student);
                break;
            case Constants.IS_FROM_EDIT:
                Log.d("--------", "onStartCommand: update query service");
                dbHelper.updateQuery(student,previousRollNo);
                break;
            case Constants.IS_FROM_DELETE:
                dbHelper.deleteQuery(student);
                break;
            default:
                break;
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
