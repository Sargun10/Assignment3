package com.example.assignment3.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assignment3.AddStudentActivity;
import com.example.assignment3.StudentListActivity;
import com.example.assignment3.R;
import com.example.assignment3.util.Student;

import java.util.ArrayList;
/*This adapter class has recycler view and viewholder that display students added.
/*@param arraylist stydentarraylist that stores Student objects having name and roll no
/*@param context of the activity from which adapter is called
/*@param position stores the adapter position
 */
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private ArrayList<Student> studentsArrayList;
    private Context context;
    private int position;

    public StudentAdapter(ArrayList<Student> studentsArrayList, Context context) {
        this.studentsArrayList = studentsArrayList;
        this.context = context;
    }
/*onCreateViewHolder inflates layout of viewholder on the screen
@param viewGroup and i viewGroup is parent on which layout is inflated
@return object of studentViewHolder
 */
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View view = inflater.inflate(R.layout.student_viewholder_layout, viewGroup, false);


        return new StudentViewHolder(view, context, studentsArrayList);
    }
/*onBindViewHolder binds layout file with the viewholder of recycler view
    @param viewHolder and i viewholder that is clicked

 */
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder viewHolder, int i) {

        int position = viewHolder.getAdapterPosition();
        Student student = studentsArrayList.get(position);
        String name = student.getName();
        String rollNo = student.getRollNo();
        viewHolder.textViewName2.setText(name);
        viewHolder.textViewRollno2.setText(rollNo);
    }
    /*getitemcount keeps track of the size of the recycler view

    @return size of student array list
     */
    @Override
    public int getItemCount() {
        return studentsArrayList.size();
    }

    /*studentviewholder class is viewholder that is made clickable
    it has arraylist,textviews,context,students
     */
    public class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewName, textViewName2;
        TextView textViewRollNo, textViewRollno2;
        ArrayList<Student> studentArrayList ;
        Context context;
        Student student;
        final String VIEW_STUDENT_ACTIVITY="View";
        final String EDIT_STUDENT_ACTIVITY="Edit";

        /*constructor to setlistener on itemview, have context of calling activity,student arraylist

 */
        public StudentViewHolder(@NonNull View itemView, Context context, ArrayList<Student> studentArrayList) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.studentArrayList = studentArrayList;
            this.context = context;
            initViews();
        }
    /*initviews are finding views of xml file
 */

        private void initViews() {
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewName2 = itemView.findViewById(R.id.textViewName2);
            textViewRollNo = itemView.findViewById(R.id.textViewRollNo);
            textViewRollno2 = itemView.findViewById(R.id.textViewRollNo2);
        }
        /*onClick of the viewHolder dialog box is opened with options view,edit,delete
        case 0 is view mode case 1 is edit mode and case 2 is delete
        @param view which is clicked
         */
        @Override
        public void onClick(View v) {
             position = getAdapterPosition();
             student = this.studentArrayList.get(position);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            String[] options={"View","Edit","Delete"};
            alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
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
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            StudentListActivity activity=(StudentListActivity)context;
                                            activity.delete(getAdapterPosition());
                                            Toast.makeText(activity,"student record deleted !!",Toast.LENGTH_SHORT).show();
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
        /*viewMode creates intent from studentlistactivity to addstudentactivity to display student info

         */
        private void viewMode() {
            Intent intentView=new Intent(context, AddStudentActivity.class);
            intentView.putExtra("Name",student.getName());
            intentView.putExtra("RollNo",student.getRollNo());
            intentView.putExtra("View",VIEW_STUDENT_ACTIVITY);
            context.startActivity(intentView);
        }
        /*editMode creates intent to addstudentactivity to edit student info
        and save changes then startactivityresult takes intent and request code for edit

         */
        private void editMode() {
            Activity activity= (Activity)context;
            Intent intentEdit=new Intent(activity, AddStudentActivity.class);
            intentEdit.putExtra("Name",student.getName());
            intentEdit.putExtra("RollNo",student.getRollNo());
            int index =0;
            for(int i=0;i<studentArrayList.size();i++){
                if(studentArrayList.get(i).getRollNo() == student.getRollNo()){
                    index = i;
                }
            }
            intentEdit.putExtra("index",index);
            intentEdit.putExtra("Edit",EDIT_STUDENT_ACTIVITY);
            intentEdit.putExtra("stuArr",studentArrayList);
            activity.startActivityForResult(intentEdit,StudentListActivity.REQUEST_CODE_EDIT);
            Log.d("aaaaa","Sss");
        }
    }
}
