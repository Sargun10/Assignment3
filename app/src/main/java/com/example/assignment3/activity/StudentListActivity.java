package com.example.assignment3.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.assignment3.R;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.database.table.StudentTable;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.SortByName;
import com.example.assignment3.util.SortByRollNo;
import com.example.assignment3.util.ToastDisplay;

import java.util.ArrayList;
import java.util.Collections;

public class StudentListActivity extends AppCompatActivity implements StudentAdapter.clickRvItem {
    public final static String EXTRA_STUDENT_LIST = "extra_student_list";
    public final static String EXTRA_INDEX = "index";
    public final static String EXTRA_SELECTED_STUDENT = "extra_selected_student";
    public final static String EXTRA_IS_FROM_VIEW = "is_from_view";
    public final static String EXTRA_IS_FROM_EDIT = "is_from_edit";
    public final static String EXTRA_IS_FROM_ADD = "is_from_add";
    private ArrayList<Student> studentArrayList=new ArrayList<>() ;
    private StudentAdapter mStudentAdapter;
    private DbHelper dbHelper;
    public RecyclerView rvStudents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        DbHelper dbHelper=new DbHelper(this);
      //  dbHelper.createTable();

        studentArrayList=dbHelper.getAllStudents();
        initViews();
    }

    /**
     * initializing views
     */
    private void initViews() {
        rvStudents = findViewById(R.id.recyclerViewStudents);
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        mStudentAdapter = new StudentAdapter(studentArrayList,this);
        rvStudents.setAdapter(mStudentAdapter);
    }

    /**
     * onclick creates a new intent to addstudent activity
     * sending array list to fetch in another activity
     *
     * @param view the view of the add student button
     */
    public void onClickAddStudent(View view) {
        Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
        intent.putExtra(EXTRA_STUDENT_LIST, studentArrayList);
        intent.putExtra(EXTRA_INDEX, Constants.REQUEST_CODE_ADD);
        intent.putExtra(EXTRA_IS_FROM_ADD, true);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD);
    }

    //this is to fetch result when control comes back from dest. class
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DbHelper dbHelper = new DbHelper(this);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            Student student = data.getParcelableExtra(EXTRA_SELECTED_STUDENT);

            if (requestCode == Constants.REQUEST_CODE_ADD) {
                studentArrayList.add(student);

                Log.d("-----", "added student "+studentArrayList);
                mStudentAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(this, getString(R.string.Student_added));
            } else if (requestCode == Constants.REQUEST_CODE_EDIT) {
                int index = data.getIntExtra(EXTRA_INDEX, 0);
                Student previousStudent = studentArrayList.get(index);
                previousStudent.setName(student.getName());
                previousStudent.setRollNo(student.getRollNo());
                mStudentAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(this, getString(R.string.update_details));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource_file, menu);
        MenuItem item = menu.findItem(R.id.switchItem1);
        MenuItem sortByName = menu.findItem(R.id.sortByName);
        MenuItem sortByRollNo = menu.findItem(R.id.sortByRollNo);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudentListActivity.this);
        rvStudents.setLayoutManager(linearLayoutManager);
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, new SortByName());
                mStudentAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(StudentListActivity.this, getString(R.string.Sorted_name));
                return true;
            }
        });
        sortByRollNo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, new SortByRollNo());
                mStudentAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(StudentListActivity.this, getString(R.string.Sorted_RollNo));
                return true;
            }
        });
        item.setActionView(R.layout.switch_layout);
        Switch switchLayout = menu.findItem(R.id.switchItem1).getActionView().findViewById(R.id.switchFirst);
        switchLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rvStudents.setLayoutManager(new GridLayoutManager(StudentListActivity.this, 2));
                } else {
                    rvStudents.setLayoutManager(linearLayoutManager);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemClick(final int position) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String[] options = {"View", "Edit", "Delete"};
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        viewMode(studentArrayList.get(position));
                        break;
                    case 1:
                        editMode(studentArrayList.get(position));
                        break;
                    case 2:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        DbHelper dbHelper=new DbHelper(StudentListActivity.this);
                                        Student student=studentArrayList.get(position);
                                        dbHelper.deleteQuery(student);
                                        studentArrayList.remove(position);
                                        mStudentAdapter.notifyDataSetChanged();
                                        ToastDisplay.displayToast(StudentListActivity.this, getString(R.string.Student_deleted));
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        dialog.dismiss();
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder ab = new AlertDialog.Builder(StudentListActivity.this);
                        ab.setMessage(getString(R.string.Delete_confirm)).setPositiveButton(getString(R.string.Yes), dialogClickListener)
                                .setNegativeButton(getString(R.string.No), dialogClickListener).show();
                        break;
                }
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void viewMode(final Student student) {
        Intent intentView = new Intent(this, AddStudentActivity.class);
        intentView.putExtra(EXTRA_SELECTED_STUDENT, student);
        intentView.putExtra(EXTRA_IS_FROM_VIEW, true);
        startActivity(intentView);
    }

    /**
     * editMode creates intent to addstudentactivity to edit student info
     * and save changes then startactivityresult takes intent and request code for edit
     */
    private void editMode(final Student student) {
        Intent intentEdit = new Intent(this, AddStudentActivity.class);
        intentEdit.putExtra(EXTRA_SELECTED_STUDENT, student);
        int index = 0;
        for (int i = 0; i < studentArrayList.size(); i++) {
            if (studentArrayList.get(i).getRollNo() == student.getRollNo()) {
                index = i;
            }
        }
        intentEdit.putExtra(EXTRA_INDEX, index);
        intentEdit.putExtra(EXTRA_IS_FROM_EDIT, true);
        intentEdit.putParcelableArrayListExtra(EXTRA_STUDENT_LIST, studentArrayList);
        startActivityForResult(intentEdit, Constants.REQUEST_CODE_EDIT);
    }
}


