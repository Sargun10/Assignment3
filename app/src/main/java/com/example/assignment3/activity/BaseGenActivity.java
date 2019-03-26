package com.example.assignment3.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.assignment3.R;
import com.example.assignment3.model.Student;

import java.util.ArrayList;

public abstract class BaseGenActivity extends AppCompatActivity {

    private ArrayList<Student> studentArrayList;

    public BaseGenActivity(ArrayList<Student> studentArrayList) {
        this.studentArrayList = studentArrayList;
    }

    public ArrayList<Student> getStudentArrayList() {
        return studentArrayList;
    }
}
