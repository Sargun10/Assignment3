package com.example.assignment3;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment3.util.Student;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {


    Button buttonSave;
    EditText editTextName, editTextRollNo;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    Validate validate = new Validate();
    private String rollNo;
    private String name;

    // to init array list with get serializable extra
    private void init() {
        if (getIntent().hasExtra(getString(R.string.stuArr))) {
            studentArrayList = (ArrayList<Student>) getIntent().getSerializableExtra("stuArr");
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

        if (getIntent().hasExtra(getString(R.string.View))) {
            buttonSave.setVisibility(View.INVISIBLE);
            editTextName.setEnabled(false);
            editTextRollNo.setEnabled(false);
            getDataFromEditTexts();
            editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else if (getIntent().hasExtra(getString(R.string.Edit))) {
            //int position=getIntent().getIntExtra("index",-1);
            getDataFromEditTexts();
            buttonSave.setText("Save Changes ");

        }

    }

    // to set text in edit texts in case of both edit and view
    private void getDataFromEditTexts() {
        editTextName.setText(getIntent().getStringExtra(getString(R.string.Name)));
        editTextRollNo.setText(getIntent().getStringExtra(getString(R.string.RollNo)));
    }

    //on click of save button data is fetched from edit text and validations are applied
    /* string name should not be null and has only alphabets
    roll number must be unique
    in case of edit index is checked and fetched and then intent is fired
    in case of adding a student only validations are done
     */
    public void onClickSave(View view) {
        Intent intentSave = new Intent(this, StudentListActivity.class);
        int option = getIntent().getIntExtra("Option", 0);
        name = editTextName.getText().toString();
        rollNo = editTextRollNo.getText().toString();
        if (option == StudentListActivity.REQUEST_CODE_ADD) {

            if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
                editTextName.requestFocus();
                editTextName.setError("enter valid name ");
            } else {
                if (rollNo.length() == 0 || !validate.uniqueValidation(studentArrayList, rollNo)) {
                    editTextRollNo.requestFocus();
                    editTextRollNo.setError("Not a valid Roll Number");
                } else {
                    setResultOnValidation(intentSave, name, rollNo);
                }
            }
        }
        else {
            if (getIntent().hasExtra("index")) {
                int index = getIntent().getIntExtra("index", 0);
                intentSave.putExtra(getString(R.string.index), index);

                if (validate.uniqueValidation(studentArrayList, rollNo, index)) {
                    Log.i("asdfg","shnhdfvfjvndfjvndfh");
                    setResultOnValidation(intentSave, name, rollNo);
                } else {
                    Log.i("asdfg","aaaaaaaaaaaaa");
                    editTextRollNo.requestFocus();
                    editTextRollNo.setError("enter valid roll no");
                }
            }

        }
    }

    //setting result for activity on result
    public void setResultOnValidation(Intent intent, String name, String rollNo) {
        Log.d("ggjv","ghg");
        intent.putExtra(getString(R.string.Name), name);
        intent.putExtra(getString(R.string.RollNo), rollNo);
        setResult(RESULT_OK, intent);
        finish();
    }


}

