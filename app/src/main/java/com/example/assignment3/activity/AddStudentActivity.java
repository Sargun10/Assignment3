package com.example.assignment3.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment3.R;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {


    private Button buttonSave;
    private EditText editTextName, editTextRollNo;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    Validate validate = new Validate();
    private String rollNo;
    private String name;

    // to init array list with get serializable extra
    private void init() {
        if (getIntent().hasExtra(Constants.EXTRA_STUARR) ){
            studentArrayList = (ArrayList<Student>) getIntent().getSerializableExtra(Constants.EXTRA_STUARR);
        }

        buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
    }

    // it has two parts one is on viewing student info another is editing its info
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("ssss","aaa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        init();

        if (getIntent().hasExtra(Constants.VIEW_STUDENT_ACTIVITY)) {
            buttonSave.setVisibility(View.INVISIBLE);
            editTextName.setEnabled(false);
            editTextRollNo.setEnabled(false);
            getDataFromEditTexts();
            editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (getIntent().hasExtra(Constants.EDIT_STUDENT_ACTIVITY)) {
            //int position=getIntent().getIntExtra("index",-1);
            getDataFromEditTexts();
            buttonSave.setText(getString(R.string.Save_changes));

        }

    }

    // to set text in edit texts in case of both edit and view
    private void getDataFromEditTexts() {
        editTextName.setText(getIntent().getStringExtra(Constants.EXTRA_NAME));
        editTextRollNo.setText(getIntent().getStringExtra(Constants.EXTRA_ROLLNO));
    }

    //on click of save button data is fetched from edit text and validations are applied
    /* string name should not be null and has only alphabets
    roll number must be unique
    in case of edit index is checked and fetched and then intent is fired
    in case of adding a student only validations are done
     */
    public void onClickSave(View view) {
        Intent intentSave = new Intent(this, StudentListActivity.class);
        int option = getIntent().getIntExtra(Constants.EXTRA_OPTION, 0);
        name = editTextName.getText().toString();
        rollNo = editTextRollNo.getText().toString();
        if (option == Constants.REQUEST_CODE_ADD) {

            if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
                editTextName.requestFocus();
                editTextName.setError(getString(R.string.Valid_name));
            } else {
                if (rollNo.length() == 0 || !validate.uniqueValidation(studentArrayList, rollNo)) {
                    editTextRollNo.requestFocus();
                    editTextRollNo.setError(getString(R.string.Valid_RollNo));
                } else {
                    setResultOnValidation(intentSave, name, rollNo);
                }
            }
        }
        else {
            if (getIntent().hasExtra(Constants.EXTRA_INDEX)) {
                int index = getIntent().getIntExtra(Constants.EXTRA_INDEX, 0);
                intentSave.putExtra(getString(R.string.index), index);

                if (validate.uniqueValidation(studentArrayList, rollNo, index)) {
                    setResultOnValidation(intentSave, name, rollNo);
                } else {
                    editTextRollNo.requestFocus();
                    editTextRollNo.setError(getString(R.string.Valid_Roll_No));
                }
            }

        }
    }

    //setting result for activity on result
    public void setResultOnValidation(Intent intent, String name, String rollNo) {
        intent.putExtra(Constants.EXTRA_NAME, name);
        intent.putExtra(Constants.EXTRA_ROLLNO, rollNo);
        setResult(RESULT_OK, intent);
        finish();
    }


}

