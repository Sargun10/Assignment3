package com.example.assignment3.comparator;

import com.example.assignment3.Student;

import java.util.Comparator;

public class SortByName implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        return(o1.getName().compareToIgnoreCase(o2.getName()));

    }
}
