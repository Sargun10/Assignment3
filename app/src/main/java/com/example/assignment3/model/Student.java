package com.example.assignment3.model;

import java.io.Serializable;

public class Student implements Serializable {

    private String name;
    private String rollNo;
    public Student(String name,String rollNo){
        this.name=name;
        this.rollNo=rollNo;
    }
//setters and getters
    public void setName(String name) {
        this.name = name;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }
}
