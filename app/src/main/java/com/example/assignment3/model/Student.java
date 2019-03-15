package com.example.assignment3.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {

    private String name, rollNo;

    protected Student(Parcel in) {
        name = in.readString();
        rollNo = in.readString();
    }

    public Student(final String name, final String rollNo) {
        this.name = name;
        this.rollNo = rollNo;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setRollNo(final String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(name);
        parcel.writeString(rollNo);
    }
}
