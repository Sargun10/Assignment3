package com.example.assignment3.communicator;

import android.os.Bundle;

import com.example.assignment3.model.Student;

public interface CommunicationFragments {
    /**
     * this function of interface is to communicate student object
     * from one fragment to another on adding a new student
     * @param student
     */
    public void communicateAddOrUpdateStudent(Student student, String mode);

    /**
     * this function is to change tab after the student has been added or edited
     */
    public void changeFragTab();

    /**
     * this function is get mode in activity whether student is added or edited
     * @param bundle
     */
    public void getMode(Bundle bundle);
}
