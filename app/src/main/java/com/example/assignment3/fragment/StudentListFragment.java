package com.example.assignment3.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.assignment3.R;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.activity.ViewStudentActivity;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.communicator.CommunicationFragments;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.model.Student;
import com.example.assignment3.service.BgService;
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.SortByName;
import com.example.assignment3.util.SortByRollNo;
import com.example.assignment3.util.ToastDisplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * <p>
 * create an instance of this fragment.
 */
public class StudentListFragment extends BaseFragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private ArrayList<Student> mArrayList;
    private String mParam2;

    private CommunicationFragments mListener;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private StudentAdapter mStudentAdapter;
    private Context mContext;
    public RecyclerView rvStudents;
    private Button btnAdd;

    public StudentListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
        if (context instanceof CommunicationFragments) {
            mListener = (CommunicationFragments) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        initViews(view);
        mContext = getActivity();
        refresh();
        handleRecyclerClick();
        return view;
    }

    public void handleRecyclerClick(){
        mStudentAdapter.setOnClickListenerRv(new StudentAdapter.clickRvItem() {
            @Override
            public void onItemClick(final int position) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                String[] options = {"View", "Edit", "Delete"};
                alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                viewMode(studentArrayList.get(position));
                                break;
                            case 1:
                                editMode(position);
                                break;
                            case 2:
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                        Intent dIntent=new Intent(mContext, BgService.class);
                                        dIntent.putExtra(StudentListActivity.EXTRA_IS_FROM_DELETE,true);
                                        studentArrayList.remove(position);

                                                mStudentAdapter.notifyDataSetChanged();
                                                ToastDisplay.displayToast(mContext, getString(R.string.Student_deleted));
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                dialog.dismiss();
                                                break;
                                        }
                                    }
                                };
                                AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
                                ab.setMessage(getString(R.string.Delete_confirm)).setPositiveButton(getString(R.string.Yes), dialogClickListener)
                                        .setNegativeButton(getString(R.string.No), dialogClickListener).show();
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


            }
        });
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void refresh() {

        studentArrayList = getArguments().getParcelableArrayList("studentList");
        mStudentAdapter = new StudentAdapter(studentArrayList, mContext);
        rvStudents.setAdapter(mStudentAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext.getApplicationContext());
        rvStudents.setLayoutManager(mLayoutManager);
        rvStudents.setItemAnimator(new DefaultItemAnimator());
        mStudentAdapter.notifyDataSetChanged();

    }

    /**
     * initializing views
     */
    public void initViews(View view) {
        btnAdd = view.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putBoolean(StudentListActivity.EXTRA_IS_FROM_ADD,true);
                bundle.putParcelableArrayList(StudentListActivity.BUNDLE_ARRAY_LIST,studentArrayList);
                StudentListFragment.this.setArguments(bundle);
                mListener.callOtherFragToAdd();
                mListener.getMode(bundle);

            }
        });

        rvStudents = view.findViewById(R.id.recyclerViewStudents);

    }
    /**
     * onclick creates a new intent to addstudent activity
     * sending array list to fetch in another activity
     */

    //this is to fetch result when control comes back from dest. class
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        DbHelper dbHelper = new DbHelper(mContext);
//        if (resultCode == RESULT_OK) {
//            if (data == null) {
//                return;
//            }
//            Student student = data.getParcelableExtra(StudentListActivity.EXTRA_SELECTED_STUDENT);
//
//            if (requestCode == Constants.REQUEST_CODE_ADD) {
//                studentArrayList.add(student);
//                Log.d("-----", "added student "+studentArrayList);
//                mStudentAdapter.notifyDataSetChanged();
//                ToastDisplay.displayToast(mContext, getString(R.string.Student_added));
//            } else if (requestCode == Constants.REQUEST_CODE_EDIT) {
//                int index = data.getIntExtra(StudentListActivity.EXTRA_INDEX, 0);
//                Student previousStudent = studentArrayList.get(index);
//                previousStudent.setName(student.getName());
//                previousStudent.setRollNo(student.getRollNo());
//                mStudentAdapter.notifyDataSetChanged();
//                ToastDisplay.displayToast(mContext, getString(R.string.update_details));
//            }
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater menuInflater = getActivity().getMenuInflater();
//        menuInflater.inflate(R.menu.menu_resource_file, menu);
//        MenuItem item = menu.findItem(R.id.switchItem1);
//        MenuItem sortByName = menu.findItem(R.id.sortByName);
//        MenuItem sortByRollNo = menu.findItem(R.id.sortByRollNo);
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        rvStudents.setLayoutManager(linearLayoutManager);
//        sortByName.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Collections.sort(studentArrayList, new SortByName());
//                mStudentAdapter.notifyDataSetChanged();
//                ToastDisplay.displayToast(mContext, getString(R.string.Sorted_name));
//                return true;
//            }
//        });
//        sortByRollNo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Collections.sort(studentArrayList, new SortByRollNo());
//                mStudentAdapter.notifyDataSetChanged();
//                ToastDisplay.displayToast(mContext, getString(R.string.Sorted_RollNo));
//                return true;
//            }
//        });
//        item.setActionView(R.layout.switch_layout);
//        Switch switchLayout = menu.findItem(R.id.switchItem1).getActionView().findViewById(R.id.switchFirst);
//        switchLayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    rvStudents.setLayoutManager(new GridLayoutManager(mContext, 2));
//                } else {
//                    rvStudents.setLayoutManager(linearLayoutManager);
//                }
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }



   private void viewMode(final Student student) {
       Intent intentView = new Intent(mContext, ViewStudentActivity.class);
       intentView.putExtra(StudentListActivity.EXTRA_SELECTED_STUDENT, student);
       intentView.putExtra(StudentListActivity.EXTRA_IS_FROM_VIEW, true);
       startActivity(intentView);
   }

    /**
     * editMode creates intent to addstudentactivity to edit student info
     * and save changes then startactivityresult takes intent and request code for edit
     //     */
   private void editMode(final int position) {
       mListener.callOtherFragToAdd();
       Bundle bundle=new Bundle();
       Student student=studentArrayList.get(position);
//       int index = 0;
//       for (int i = 0; i < studentArrayList.size(); i++) {
//           if (studentArrayList.get(i).getRollNo() == student.getRollNo()) {
//               index = i;
//           }
//       }
       bundle.putParcelable(StudentListActivity.EXTRA_SELECTED_STUDENT, student);
       bundle.putInt(StudentListActivity.EXTRA_INDEX, position);
       bundle.putBoolean(StudentListActivity.EXTRA_IS_FROM_EDIT,true);
//       bundle.putBoolean(StudentListActivity.EXTRA_IS_FROM_EDIT, true);
//       bundle.putParcelableArrayList(StudentListActivity.EXTRA_STUDENT_LIST, studentArrayList);
   //    mListener.communicateEditStudent(bundle);
       mListener.getMode(bundle);
       Log.d("----------", "editMode: " );
//       mListener.callOtherFragToAdd();

   }
}
