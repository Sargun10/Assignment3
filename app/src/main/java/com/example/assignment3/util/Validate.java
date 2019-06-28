package com.example.assignment3.util;

import android.util.Log;

import com.example.assignment3.model.Student;

import java.util.ArrayList;

public class Validate {
    /*
    to check that roll no is unique in the list on adding a new student
    @param arraylist to check unique roll no
    @param roll no to check this roll no in array list
    @return boolean value if roll no is unique or not
     */
    public boolean uniqueValidation(ArrayList<Student> studentArrayList, String rollNo){
        boolean isRollNoUnique=true;
        if(studentArrayList.size()==0){
            isRollNoUnique=true;
        }
        else{
            for (Student student:studentArrayList){
                if(student.getRollNo().equals(rollNo)){
                    isRollNoUnique=false;
                    break;
                }
            }
        }
        return isRollNoUnique;
    }

    /**
     * to check if name has only alphabets
     * @param name
     * @return
     */
    public boolean isStringOnly(String name){
            return name.matches("^[a-zA-Z\\s]*$");
    }
}
