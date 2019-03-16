package com.example.assignment3.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment3.R;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.database.table.StudentTable;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {


    private Button buttonSave;
    private EditText editTextName, editTextRollNo;
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    Validate validate = new Validate();
    private String rollNo, name;
    private int mSelectedPosition;
    private boolean mIsFromAdd = false, mIsFromEdit = false, mIsFromView = false;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.d("ssss","aaa");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        initViews();
        getSetIntentData();
        dbHelper = new DbHelper(this);
        studentArrayList.addAll(dbHelper.getAllStudents());
    }

    /**
     * initializing views
     */
    private void initViews() {
        buttonSave = findViewById(R.id.buttonSave);
        editTextName = findViewById(R.id.editTextName);
        editTextRollNo = findViewById(R.id.editTextRollNo);
    }


    private void getSetIntentData() {
        mSelectedPosition = getIntent().getIntExtra(StudentListActivity.EXTRA_INDEX, -1);
        mIsFromAdd = getIntent().getBooleanExtra(StudentListActivity.EXTRA_IS_FROM_ADD, false);
        mIsFromView = getIntent().getBooleanExtra(StudentListActivity.EXTRA_IS_FROM_VIEW, false);
        mIsFromEdit = getIntent().getBooleanExtra(StudentListActivity.EXTRA_IS_FROM_EDIT, false);

        if (getIntent().hasExtra(StudentListActivity.EXTRA_STUDENT_LIST)) {
            studentArrayList = getIntent().getParcelableArrayListExtra(StudentListActivity.EXTRA_STUDENT_LIST);
        }

        if (mIsFromView) {
            Student student = getIntent().getParcelableExtra(StudentListActivity.EXTRA_SELECTED_STUDENT);
            rollNo = student.getRollNo();
            name = student.getName();
            buttonSave.setVisibility(View.INVISIBLE);
            editTextName.setEnabled(false);
            editTextRollNo.setEnabled(false);
            getDataFromEditTexts();
            editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (mIsFromEdit) {
            Student student = getIntent().getParcelableExtra(StudentListActivity.EXTRA_SELECTED_STUDENT);
            rollNo = student.getRollNo();
            name = student.getName();
            getDataFromEditTexts();
            buttonSave.setText(getString(R.string.Save_changes));
        }
    }

    // to set text in edit texts in case of both edit and view
    private void getDataFromEditTexts() {
        DbHelper dbHelper=new DbHelper(this);
        Student student=dbHelper.getStudent(Integer.parseInt(rollNo));
        editTextName.setText(student.getName());
        editTextRollNo.setText(student.getRollNo());
    }

    /**
     * on click of save button data is fetched from edit text and validations are applied
     * string name should not be null and has only alphabets
     * roll number must be unique
     * in case of edit index is checked and fetched and then intent is fired
     * in case of adding a student only validations are done
     */
    public void onClickSave(View view) {
        Intent intentSave = new Intent(this, StudentListActivity.class);
        name = editTextName.getText().toString();
        rollNo = editTextRollNo.getText().toString();
        if (mIsFromAdd) {
            if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
                editTextName.requestFocus();
                editTextName.setError(getString(R.string.Valid_name));
            } else {
                if (rollNo.length() == 0 || !validate.uniqueValidation(studentArrayList, rollNo)) {
                    editTextRollNo.requestFocus();
                    editTextRollNo.setError(getString(R.string.Valid_RollNo));
                } else {
                    Student student=new Student(name,rollNo);
//                    Log.d("STUDENT", "onClickSave: " + student + student.getName() + student.getRollNo());
                   dbHelper.insertQuery(student);
                    setResultOnValidation(intentSave, name, rollNo);
                }
            }
        } else {
            intentSave.putExtra(StudentListActivity.EXTRA_INDEX, mSelectedPosition);

            if (!validate.uniqueValidation(studentArrayList, rollNo, mSelectedPosition)) {
                Student student=new Student(name,rollNo);
                dbHelper.updateQuery(student,studentArrayList.get(mSelectedPosition).getRollNo());
                setResultOnValidation(intentSave, name, rollNo);
            } else {
                editTextRollNo.requestFocus();
                editTextRollNo.setError(getString(R.string.Valid_Roll_No));
            }
        }
    }

    //setting result for activity on result
    public void setResultOnValidation(Intent intent, String name, String rollNo) {
        intent.putExtra(StudentListActivity.EXTRA_SELECTED_STUDENT, new Student(name, rollNo));
        setResult(RESULT_OK, intent);
        finish();
    }
}

