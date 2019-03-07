package com.example.assignment3.comparator;

import android.content.Intent;

import com.example.assignment3.Student;

import java.util.Comparator;

public class SortByRollNo implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        return(o1.getRollNo().compareTo(o2.getRollNo()));
    }
}
