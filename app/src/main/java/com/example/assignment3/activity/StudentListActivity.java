package com.example.assignment3.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment3.R;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.SortByName;
import com.example.assignment3.util.SortByRollNo;
import com.example.assignment3.model.Student;
import com.example.assignment3.util.ToastDisplay;

import java.util.ArrayList;
import java.util.Collections;

public class StudentListActivity extends AppCompatActivity implements StudentAdapter.clickRvItem {
    private ArrayList<Student> studentArrayList;
    private TextView textViewName, textViewRollNo;
    private ImageView imageView;
    private StudentAdapter myAdapter;
    private String name, rollNo;
    private int position;
    private Student student;
    private RecyclerView studentsList;
    private Intent intent;

    /*onCreate of activity is init views ,recycler view ,arraylist and adapter
        @param bundle to save instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
        initViews();
        studentArrayList = new ArrayList<Student>();
        myAdapter = new StudentAdapter(studentArrayList, this);
        studentsList.setAdapter(myAdapter);
    }

    //initializing views and student arraylist
    private void initViews() {
        imageView = findViewById(R.id.imageView);
        textViewName = findViewById(R.id.textViewName);
        textViewRollNo = findViewById(R.id.textViewRollNo);
        studentsList = findViewById(R.id.recyclerViewStudents);
    }

    //onclick creates a new intent to addstudent activity
    //sending array list to fetch in another activity
    public void onClick(View view) {
        intent.putExtra(Constants.EXTRA_STUARR, studentArrayList);
        intent.putExtra(Constants.EXTRA_INDEX, Constants.REQUEST_CODE_ADD);
        startActivityForResult(intent, Constants.REQUEST_CODE_ADD);
    }

    //this is to fetch result when control comes back from dest. class
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            return;
        }
        name = data.getStringExtra(Constants.EXTRA_NAME);
        rollNo = data.getStringExtra(Constants.EXTRA_ROLLNO);

        if (resultCode == RESULT_OK) {

            if (requestCode == Constants.REQUEST_CODE_ADD) {
                student = new Student(name, rollNo);
                studentArrayList.add(student);
                myAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(this,getString(R.string.Student_added));
            }
            if (requestCode == Constants.REQUEST_CODE_EDIT) {
                int index = data.getIntExtra(Constants.EXTRA_INDEX, 0);
                // Log.d("aaaa",String.valueOf(index));
                student = studentArrayList.get(index);
                student.setName(name);
                student.setRollNo(rollNo);
                myAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(this,getString(R.string.update_details));
            }
        }
    }

//menu bar on the screen with toggle to grid view and has list view by default
    // sort by name and sort by roll no as menu options

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource_file, menu);
        MenuItem item = menu.findItem(R.id.switchItem1);
        MenuItem sortByName = menu.findItem(R.id.sortByName);
        MenuItem sortByRollNo = menu.findItem(R.id.sortByRollNo);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StudentListActivity.this);
        studentsList.setLayoutManager(linearLayoutManager);
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, new SortByName());
                myAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(StudentListActivity.this,getString(R.string.Sorted_name));
                return true;
            }
        });
        sortByRollNo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList, new SortByRollNo());
                myAdapter.notifyDataSetChanged();
                ToastDisplay.displayToast(StudentListActivity.this,getString(R.string.Sorted_RollNo));
                return true;
            }
        });
        item.setActionView(R.layout.switch_layout);
        Switch switchLayout = menu.findItem(R.id.switchItem1).getActionView().findViewById(R.id.switchFirst);
//        Switch switchLayout=findViewById(R.id.switchFirst);

        switchLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    studentsList.setLayoutManager(new GridLayoutManager(StudentListActivity.this, 2));
                } else {
                    studentsList.setLayoutManager(linearLayoutManager);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onItemClick(final int position) {
        this.position = position;
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        String[] options = {"View", "Edit", "Delete"};
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        viewMode();
                        break;
                    case 1:
                        editMode();
                        break;
                    case 2:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        studentArrayList.remove(position);
                                        myAdapter.notifyDataSetChanged();
                                        ToastDisplay.displayToast(StudentListActivity.this,getString(R.string.Student_deleted));
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



    /*viewMode creates intent from studentlistactivity to addstudentactivity to display student info

     */
    private void viewMode() {
        Intent intentView=new Intent(this, AddStudentActivity.class);
        intentView.putExtra(Constants.EXTRA_NAME,student.getName());
        intentView.putExtra(Constants.EXTRA_ROLLNO,student.getRollNo());
        intentView.putExtra(Constants.VIEW_STUDENT_ACTIVITY,Constants.VIEW_STUDENT_ACTIVITY);
        startActivity(intentView);
    }
    /*editMode creates intent to addstudentactivity to edit student info
    and save changes then startactivityresult takes intent and request code for edit

     */
    private void editMode() {
        Intent intentEdit=new Intent(this, AddStudentActivity.class);
        intentEdit.putExtra(Constants.EXTRA_NAME,student.getName());
        intentEdit.putExtra(Constants.EXTRA_ROLLNO,student.getRollNo());
        int index =0;
        for(int i=0;i<studentArrayList.size();i++){
            if(studentArrayList.get(i).getRollNo() == student.getRollNo()){
                index = i;
            }
        }
        intentEdit.putExtra(Constants.EXTRA_INDEX,index);
        intentEdit.putExtra(Constants.EDIT_STUDENT_ACTIVITY,Constants.EDIT_STUDENT_ACTIVITY);
        intentEdit.putExtra(Constants.EXTRA_STUARR,studentArrayList);
        startActivityForResult(intentEdit,Constants.REQUEST_CODE_EDIT);
    }
}


