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


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class BgIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.assignment3.service.action.FOO";
    private static final String ACTION_BAZ = "com.example.assignment3.service.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.assignment3.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.assignment3.service.extra.PARAM2";

    public BgIntentService() {
        super("BgIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BgIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, BgIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        intent.setAction(AddStudentFragment.INTENT_SERVICE_FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        DbHelper dbHelper = new DbHelper(this);
        dbHelper.getWritableDatabase();
        String previousStudentId = new String();
        if(intent.hasExtra(AddStudentFragment.PREVIOUS_STUDENT_ID)) {
            previousStudentId = intent.getStringExtra(AddStudentFragment.PREVIOUS_STUDENT_ID);
        }
        String mode = intent.getStringExtra(AddStudentFragment.MODE);
        Student student = intent.getParcelableExtra(AddStudentFragment.PRESENT_STUDENT);
        switch (mode) {
            case StudentListActivity.EXTRA_IS_FROM_ADD:
                dbHelper.insertQuery(student);
                break;
            case StudentListActivity.EXTRA_IS_FROM_EDIT:
                dbHelper.updateQuery(student,previousStudentId);
                break;
            case StudentListActivity.EXTRA_IS_FROM_DELETE:
                dbHelper.deleteQuery(student);
                break;
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
