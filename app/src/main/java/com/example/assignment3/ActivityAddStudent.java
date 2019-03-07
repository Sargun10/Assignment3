package com.example.assignment3;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityAddStudent extends AppCompatActivity  {


    Button buttonSave;
    EditText editTextName,editTextRollNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        buttonSave=findViewById(R.id.buttonSave);
        editTextName=findViewById(R.id.editTextName);
        editTextRollNo=findViewById(R.id.editTextRollNo);

        if(getIntent().hasExtra("View")){
            buttonSave.setVisibility(View.INVISIBLE);
            editTextName.setEnabled(false);
            editTextRollNo.setEnabled(false);
            editTextName.setText(getIntent().getStringExtra("name"));
            editTextRollNo.setText(getIntent().getStringExtra("rollNo"));
            editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        else if(getIntent().hasExtra("Edit")){
            int position=getIntent().getIntExtra("index",-1);
            editTextName.setText(getIntent().getStringExtra("name"));
            editTextRollNo.setText(getIntent().getStringExtra("rollNo"));

            buttonSave.setText("Save Changes ");

        }
    }

        public void onClickSave(View view) {

        Intent intentSave=new Intent();
        String name=editTextName.getText().toString();
        String rollNo=editTextRollNo.getText().toString();

        if(name.length()==0) {
            editTextName.requestFocus();
            editTextName.setError("enter valid name ");

        }
//        for(Student student:)
//        if(rollNo.equals(student.getRollNo())){
//            editTextRollNo.requestFocus();
//            editTextRollNo.setError("enter valid name ");
//        }


        else {
                intentSave.putExtra("Name",name);
                intentSave.putExtra("RollNo",rollNo);
                if(getIntent().hasExtra("index")){
                    intentSave.putExtra("index",getIntent().getIntExtra("index",0));


                }
                setResult(RESULT_OK,intentSave);
                finish();

            }
    }



}
