package com.example.assignment3.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment3.ActivityAddStudent;
import com.example.assignment3.MainActivity;
import com.example.assignment3.R;
import com.example.assignment3.Student;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ArrayList<Student> studentsArrayList;
    Context context;
    int position;

    public StudentAdapter(ArrayList<Student> studentsArrayList, Context context) {
        this.studentsArrayList = studentsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.student_viewholder_layout, viewGroup, false);


        return new StudentViewHolder(view, context, studentsArrayList);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder viewHolder, int i) {

        int position = viewHolder.getAdapterPosition();
        Student student = studentsArrayList.get(position);
        String name = student.getName();
        String rollNo = student.getRollNo();
        viewHolder.textViewName2.setText(name);
        viewHolder.textViewRollno2.setText(rollNo);
    }

    @Override
    public int getItemCount() {
        return studentsArrayList.size();
    }


    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName, textViewName2;
        TextView textViewRollNo, textViewRollno2;
        ArrayList<Student> studentArrayList ;
        Context context;
        final static String VIEW_STUDENT_ACTIVITY="View";
        final static String EDIT_STUDENT_ACTIVITY="Edit";
        final static String DELETE_STUDENT_ACTIVITY="Delete";
        public StudentViewHolder(@NonNull View itemView, Context context, ArrayList<Student> studentArrayList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.studentArrayList = studentArrayList;
            this.context = context;
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewName2 = itemView.findViewById(R.id.textViewName2);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNo);
            textViewRollno2 = itemView.findViewById(R.id.textViewRollNo2);
        }

        @Override
        public void onClick(View v) {
             position = getAdapterPosition();
            final Student student = this.studentArrayList.get(position);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Select an option : ");
            String[] options={"View","Edit","Delete"};


            alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case 0:
                            Intent intentView=new Intent(context,ActivityAddStudent.class);
                            intentView.putExtra("name",student.getName());
                            intentView.putExtra("rollNo",student.getRollNo());
                            intentView.putExtra("View",VIEW_STUDENT_ACTIVITY);
                            context.startActivity(intentView);
                            break;
                        case 1:
                            Activity activity= (Activity)context;
                            Intent intentEdit=new Intent(context,ActivityAddStudent.class);
                            intentEdit.putExtra("name",student.getName());
                            intentEdit.putExtra("rollNo",student.getRollNo());
                            intentEdit.putExtra("index",getAdapterPosition());
                            intentEdit.putExtra("Edit",EDIT_STUDENT_ACTIVITY);
                            activity.startActivityForResult(intentEdit,2);
                            break;
                        case 2:
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            MainActivity m= (MainActivity)context;
                                            m.delete(getAdapterPosition());

                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                            ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                            break;
                    }
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}
