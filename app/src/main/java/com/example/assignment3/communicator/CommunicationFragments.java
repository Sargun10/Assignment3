package com.example.assignment3.communicator;

import android.os.Bundle;

import com.example.assignment3.model.Student;

public interface CommunicationFragments {

    public void communicateAddStudent(Student student);

    public void communicateEditStudent(Bundle bundle);

    public void callOtherFragToAdd();

    public void getMode(Bundle bundle);
}
