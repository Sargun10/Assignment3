package com.example.assignment3.comparator;

import com.example.assignment3.util.Student;

import java.util.Comparator;

public class SortByName implements Comparator<Student> {
    //sorting by name using compareto between two objects
    @Override
    public int compare(Student o1, Student o2) {
        return(o1.getName().compareToIgnoreCase(o2.getName()));

    }
}
