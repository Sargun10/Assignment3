package com.example.assignment3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {


    private ArrayList<Student> studentArrayList;
    TextView textViewName,textViewRollNo,textViewName2,textViewRollNo2;
    ImageView imageView;
    StudentAdapter myAdapter;
    int ppos;

    private RecyclerView studentsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textViewName=findViewById(R.id.textViewName);
        imageView=findViewById(R.id.imageView);
        textViewRollNo=findViewById(R.id.textViewRollNo);
        textViewName2=findViewById(R.id.textViewName2);
        textViewRollNo2=findViewById(R.id.textViewRollNo2);
        setContentView(R.layout.activity_main);
        studentsList=findViewById(R.id.recyclerViewStudents);
        studentArrayList=new ArrayList<Student>();
        myAdapter = new StudentAdapter(studentArrayList,this);
        studentsList.setAdapter(myAdapter);
    }

    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,ActivityAddStudent.class);
        startActivityForResult(intent,1);
    }
@Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        int flag=1;
    super.onActivityResult(requestCode,resultCode,data);
    if(resultCode==RESULT_OK){
        String name,rollNo;
        if(requestCode==1){
            name=data.getStringExtra("Name");
            rollNo=data.getStringExtra("RollNo");
            for(Student student:studentArrayList){
                if(rollNo.equals(student.getRollNo())){
                    flag=0;

                }
            }
            if(flag==1){
                Student student=new Student(name,rollNo);
                studentArrayList.add(student);
                myAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Student added !!", Toast.LENGTH_SHORT).show();
            }

        }
        if(requestCode==2){

                int index = data.getIntExtra("index", 0);
            name=data.getStringExtra("Name");
            rollNo=data.getStringExtra("RollNo");
            Student student=studentArrayList.get(index);
            student.setName(name);
            student.setRollNo(rollNo);
            for(Student student1:studentArrayList){
                if(rollNo.equals(student1.getRollNo())){
                    flag=0;

                }
            }
            if(flag==1){
                Student student1=new Student(name,rollNo);
                studentArrayList.add(student1);
                myAdapter.notifyDataSetChanged();

                Toast.makeText(this, "Changes Saved !!", Toast.LENGTH_SHORT).show();
            }

        }

        }
    }

    public void delete(int p){
        studentArrayList.remove(p);
        myAdapter.notifyDataSetChanged();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_resource_file,menu);
        MenuItem item=menu.findItem(R.id.switchItem1);
        MenuItem sortByName=menu.findItem(R.id.sortByName);
        MenuItem sortByRollNo=menu.findItem(R.id.sortByRollNo);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this);
        studentsList.setLayoutManager(linearLayoutManager);
        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList,new SortByName());
                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Sorted By Name !",Toast.LENGTH_SHORT);
                return true ;
            }
        });
        sortByRollNo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Collections.sort(studentArrayList,new SortByRollNo());
                myAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this,"Sorted By Roll No !", Toast.LENGTH_SHORT);
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
                    studentsList.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
            }
                else{
                    studentsList.setLayoutManager(linearLayoutManager);
                }
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

}

