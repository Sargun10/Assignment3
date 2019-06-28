package com.example.assignment3.database.table;

public class StudentTable {
    public final static String COL_NAME="Name";
    public final static String COL_ROLL_NO="_id";
    public final static String  TABLE_NAME="StudentTable";
    public static final String createTableQuery= " CREATE TABLE " + StudentTable.TABLE_NAME + "(" + StudentTable.COL_ROLL_NO + " INTEGER PRIMARY KEY, " + StudentTable.COL_NAME + " TEXT)";

}
