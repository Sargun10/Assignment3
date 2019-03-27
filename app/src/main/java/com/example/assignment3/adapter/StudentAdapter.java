package com.example.assignment3.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.assignment3.R;
import com.example.assignment3.model.Student;

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
    private clickRvItem mListener;

    public StudentAdapter(ArrayList<Student> studentsArrayList,Context context) {
        Log.d("-------------", "strt ");
        this.context=context;
        this.studentsArrayList = studentsArrayList;
    }
/*onCreateViewHolder inflates layout of viewholder on the screen
@param viewGroup and i viewGroup is parent on which layout is inflated
@return object of studentViewHolder
 */
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.student_viewholder_layout, viewGroup, false);


        return new StudentViewHolder(view);
    }
/*onBindViewHolder binds layout file with the viewholder of recycler view
    @param viewHolder and i viewholder that is clicked

 */
    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder viewHolder, int i) {

        viewHolder.textViewName2.setText(studentsArrayList.get(i).getName());
        viewHolder.textViewRollno2.setText(studentsArrayList.get(i).getRollNo());

    }
    /*getitemcount keeps track of the size of the recycler view

    @return size of student array list
     */
    @Override
    public int getItemCount() {
        return studentsArrayList==null ? 0:studentsArrayList.size();
    }

    /*studentviewholder class is viewholder that is made clickable
    it has arraylist,textviews,context,students
     */
    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName2;
        private TextView textViewRollno2;

        /*constructor to setlistener on itemview, have context of calling activity,student arraylist

 */
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
           // itemView.setOnClickListener(this);
            initViews(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition());
                }
            });
        }
    /*initviews are finding views of xml file
 */

        private void initViews(View itemView) {
            textViewName2 = itemView.findViewById(R.id.textViewName2);
            textViewRollno2 = itemView.findViewById(R.id.textViewRollNo2);
        }
        /*onClick of the viewHolder dialog box is opened with options view,edit,delete
        case 0 is view mode case 1 is edit mode and case 2 is delete
        @param view which is clicked
         */
    }
    public interface clickRvItem{
//        boolean onCreateOptionsMenu(Menu menu);

        void onItemClick(int position);
    }

    public void setOnClickListenerRv(clickRvItem listener) {
        mListener = listener;
    }
}
