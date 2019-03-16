package com.example.assignment3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.database.table.StudentTable;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;

import java.util.ArrayList;

import static com.example.assignment3.database.table.StudentTable.COL_NAME;
import static com.example.assignment3.database.table.StudentTable.COL_ROLL_NO;
import static com.example.assignment3.database.table.StudentTable.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {
    private final static int DB_VERSION=1;

//    String CREATE_TABLE_QUERY;
    private static final String createTableQuery= " CREATE TABLE " + StudentTable.TABLE_NAME + "(" + StudentTable.COL_ROLL_NO + " INTEGER PRIMARY KEY, " + StudentTable.COL_NAME + " TEXT)";
    public DbHelper(Context context) {
        super(context, Constants.DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createTableQuery);
//        createTable();
        Log.d("aaa","after create table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
//    public void createTable(){
//        CREATE_TABLE_QUERY=" CREATE TABLE " + StudentTable.TABLE_NAME + "(" + StudentTable.COL_ROLL_NO + " INTEGER PRIMARY KEY, " + StudentTable.COL_NAME + "TEXT" + ");";
//
//        db.execSQL(CREATE_TABLE_QUERY);
//        Log.d("aaa","after execSql");
//    }
    public void insertQuery(Student student){
        Log.d("STUDENT", "onClickSave: " + student + student.getName() + student.getRollNo());
        ContentValues studentRecord=new ContentValues();
        studentRecord.put(StudentTable.COL_ROLL_NO,student.getRollNo());
        studentRecord.put(StudentTable.COL_NAME,student.getName());
        SQLiteDatabase db=this.getWritableDatabase();
        db.insert(StudentTable.TABLE_NAME,null,studentRecord);

    }
    public ArrayList<Student> getAllStudents(){
        SQLiteDatabase db=this.getWritableDatabase();
        ArrayList<Student> studentArrayList=new ArrayList<>();
        String query = " SELECT * FROM " + StudentTable.TABLE_NAME + " WHERE 1";
        Cursor cursor=db.rawQuery(query,null);

        while(cursor.moveToNext()){
            String name=cursor.getString(1);
            String rollNo=String.valueOf(cursor.getInt(0));
            studentArrayList.add(new Student(name,rollNo));
        }
        cursor.close();
        db.close();
        return studentArrayList;
    }

    public void updateQuery(Student student){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues studentRecord=new ContentValues();
        studentRecord.put(StudentTable.COL_ROLL_NO,Integer.parseInt(student.getRollNo()));
        studentRecord.put(StudentTable.COL_NAME,student.getName());
        String updateQueryPart=StudentTable.COL_ROLL_NO + "=?";
        db.update(StudentTable.TABLE_NAME,studentRecord,updateQueryPart,new String[]{student.getRollNo()});

    }

    public void deleteQuery(Student student){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(StudentTable.TABLE_NAME, StudentTable.COL_ROLL_NO + " = ?",
                new String[]{String.valueOf(student.getRollNo())});
        db.close();
    }
    public Student getStudent(int id){
        String selectQuery="SELECT * FROM "+ StudentTable.TABLE_NAME + " WHERE "+StudentTable.COL_ROLL_NO + "="+ id;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        Student student=new Student();
        while(cursor.moveToNext()){
            student.setName(cursor.getString(cursor.getColumnIndex(StudentTable.COL_NAME)));
            student.setRollNo(cursor.getString(cursor.getColumnIndex(StudentTable.COL_ROLL_NO)));
        }
        cursor.close();
        db.close();
        return student;
    }
}
