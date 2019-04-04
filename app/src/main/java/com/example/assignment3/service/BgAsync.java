package com.example.assignment3.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telecom.Call;

import com.example.assignment3.database.DbHelper;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;


public class BgAsync extends AsyncTask<Object,Void,String> {

    private Context context;
    private SQLiteDatabase db;
    private CallBackAsync callBackAsyncListener;
    private Student student;
    private String mode;
    public BgAsync(Context context,CallBackAsync callBackAsync) {
        this.context=context;
        this.callBackAsyncListener=callBackAsync;
    }

    @Override
    protected String doInBackground(Object... objects) {
        student = (Student) objects[0];
        mode = (String) objects[1];
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
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        callBackAsyncListener.asyncCallBack();


    }

    public interface CallBackAsync{
        void asyncCallBack();
    }
}
