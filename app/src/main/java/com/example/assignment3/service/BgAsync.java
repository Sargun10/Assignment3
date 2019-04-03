package com.example.assignment3.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.assignment3.database.DbHelper;
import com.example.assignment3.model.Student;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.util.Constants;


public class BgAsync extends AsyncTask<Object,Void,Void> {

    private Context context;
    private SQLiteDatabase db;
    public BgAsync(Context context) {
        this.context=context;
    }

    @Override
    protected Void doInBackground(Object... objects) {
        Student student = (Student) objects[0];
        String mode = (String) objects[1];
        String previousStudentId = (String) objects[2];
        DbHelper dbHelper=new DbHelper(context);
        switch (mode){
            case Constants.IS_FROM_ADD:
                db=dbHelper.getWritableDatabase();
                dbHelper.insertQuery(student);
                db.close();
                break;
            case Constants.IS_FROM_EDIT:
                db=dbHelper.getWritableDatabase();
                dbHelper.updateQuery(student,previousStudentId);
                db.close();
                break;
            case Constants.IS_FROM_DELETE:
                db=dbHelper.getWritableDatabase();
                dbHelper.deleteQuery(student);
                db.close();
                break;
            default:
                break;
        }
        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
