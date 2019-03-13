package com.example.assignment3.comparator;

import com.example.assignment3.util.Student;

import java.util.Comparator;

public class SortByRollNo implements Comparator<Student> {
//sorting by roll no gives result by subtracting rollno of two objects
    @Override
    public int compare(Student o1, Student o2) {

        int rollNo1=Integer.parseInt(o1.getRollNo());
        int rollNo2=Integer.parseInt(o2.getRollNo());
        return (rollNo1-rollNo2);
//        return(o1.getRollNo().compareTo(o2.getRollNo()));
    }
}
