package com.example.assignment3.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.assignment3.database.DbHelper;
import com.example.assignment3.fragment.AddStudentFragment;
import com.example.assignment3.model.Student;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.util.Constants;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BgIntentService extends IntentService {


    public BgIntentService() {
        super("BgIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        intent.setAction(Constants.INTENT_SERVICE_FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        DbHelper dbHelper = new DbHelper(this);
        dbHelper.getWritableDatabase();
        String previousStudentId = new String();
        if(intent.hasExtra(Constants.PREVIOUS_STUDENT_ID)) {
            previousStudentId = intent.getStringExtra(Constants.PREVIOUS_STUDENT_ID);
        }
        String mode = intent.getStringExtra(Constants.MODE);
        Student student = intent.getParcelableExtra(Constants.PRESENT_STUDENT);
        switch (mode) {
            case Constants.IS_FROM_ADD:
                dbHelper.insertQuery(student);
                break;
            case Constants.IS_FROM_EDIT:
                dbHelper.updateQuery(student,previousStudentId);
                break;
            case Constants.IS_FROM_DELETE:
                dbHelper.deleteQuery(student);
                break;
        }
    }

}
