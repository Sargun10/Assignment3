package com.example.assignment3;

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

import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.comparator.SortByName;
import com.example.assignment3.comparator.SortByRollNo;
import com.example.assignment3.util.Student;

import java.util.ArrayList;
import java.util.Collections;

public class StudentListActivity extends AppCompatActivity {


    private ArrayList<Student> studentArrayList;
    TextView textViewName,textViewRollNo;
    ImageView imageView;
    StudentAdapter myAdapter;
    private String name,rollNo;
    private Student student;
    private RecyclerView studentsList;
    public final static int REQUEST_CODE_ADD=1;
    public final static int REQUEST_CODE_EDIT=2;
/*onCreate of activity is init views ,recycler view ,arraylist and adapter
    @param bundle to save instance
 */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        studentArrayList=new ArrayList<Student>();
        myAdapter = new StudentAdapter(studentArrayList,this);
        studentsList.setAdapter(myAdapter);
    }
//initializing views and student arraylist
    private void initViews() {
        imageView=findViewById(R.id.imageView);
        textViewName=findViewById(R.id.textViewName);
        textViewRollNo=findViewById(R.id.textViewRollNo);
        setContentView(R.layout.activity_student_list);
        studentsList=findViewById(R.id.recyclerViewStudents);
    }
//onclick creates a new intent to addstudent activity
    //sending array list to fetch in another activity
    public void onClick(View view) {
        Intent intent=new Intent(StudentListActivity.this, AddStudentActivity.class);
        intent.putExtra(getString(R.string.stuArr),studentArrayList);
        intent.putExtra("Option",REQUEST_CODE_ADD);
        startActivityForResult(intent,REQUEST_CODE_ADD);
    }
    //this is to fetch result when control comes back from dest. class
@Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
    super.onActivityResult(requestCode,resultCode,data);
    name=data.getStringExtra(getString(R.string.Name));
    rollNo=data.getStringExtra(getString(R.string.RollNo));
    if(resultCode==RESULT_OK){
        if(requestCode==REQUEST_CODE_ADD){
            student=new Student(name,rollNo);
            studentArrayList.add(student);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Student added !!", Toast.LENGTH_SHORT).show();
        }
        if(requestCode==REQUEST_CODE_EDIT){
            int index = data.getIntExtra(getString(R.string.index), 0);
           // Log.d("aaaa",String.valueOf(index));
            student=studentArrayList.get(index);
            student.setName(name);
            student.setRollNo(rollNo);
            myAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Student details updated !!", Toast.LENGTH_SHORT).show();
            }
        }
    }
//delete mode
    public void delete(int p){
        studentArrayList.remove(p);
        myAdapter.notifyDataSetChanged();

    }

//menu bar on the screen with toggle to grid view and has list view by default
    // sort by name and sort by roll no as menu options

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource_file,menu);
        MenuItem item=menu.findItem(R.id.switchItem1);
        MenuItem sortByName=menu.findItem(R.id.sortByName);
        MenuItem sortByRollNo=menu.findItem(R.id.sortByRollNo);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(StudentListActivity.this);
        studentsList.setLayoutManager(linearLayoutManager);
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList,new SortByName());
                myAdapter.notifyDataSetChanged();
                Toast.makeText(StudentListActivity.this,"Sorted By Name !",Toast.LENGTH_SHORT).show();
                return true ;
            }
        });
        sortByRollNo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList,new SortByRollNo());
                myAdapter.notifyDataSetChanged();
                Toast.makeText(StudentListActivity.this,"Sorted By Roll No !", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        item.setActionView(R.layout.switch_layout);
       Switch switchLayout=menu.findItem(R.id.switchItem1).getActionView().findViewById(R.id.switchFirst);
//        Switch switchLayout=findViewById(R.id.switchFirst);

        switchLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    studentsList.setLayoutManager(new GridLayoutManager(StudentListActivity.this,2));
            }
                else{
                    studentsList.setLayoutManager(linearLayoutManager);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

