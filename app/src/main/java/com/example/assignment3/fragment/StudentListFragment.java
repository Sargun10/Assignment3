package com.example.assignment3.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.example.assignment3.R;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.activity.ViewStudentActivity;
import com.example.assignment3.adapter.StudentAdapter;
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
 * {@link StudentListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.

 * create an instance of this fragment.
 */
public class StudentListFragment extends Fragment implements StudentAdapter.clickRvItem {
    //implements StudentAdapter.clickRvItem{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ARRAY_LIST = "arg_array_list";
    private static final String ARG_PARAM2 = "param2";
    public static final String VIEW_NAME = "view_name";
    public static final String VIEW_ROLL = "view_roll";
    public static final String VIEW = "view";
    public static final String EDIT = "edit";
    public static final String NORMAL = "normal";
    public static final String MODE = "mode";
    public static final String NAME = "name";
    public static final String ROLL_NO = "rollNo";
    public static final String NAME_MATCH = "^[a-zA-Z\\s]+$";
    public static final String ROLL_MATCH = "[+-]?[0-9][0-9]*";

    public static final String TABLE_NAME = "student";
    public static final String COLUMN_ROLL_NO = "roll";
    public static final String COLUMN_NAME = "name";

    public static final int VIEW_DATA = 0;
    public static final int EDIT_DATA = 1;
    public static final int DELETE_DATA = 2;


    public static final int ASYNC = 0;
    public static final int SERVICE = 1;
    public static final int INTENT_SERVICE = 2;
    public static final String OPERATION = "operation";
    public static final String STUDENT_LIST_FROM_MAIN = "student_list";

    public static int NUM_ITEMS = 2;
    public static final int STUDENT_LIST = 0;
    public static final int ADD_STUDENT = 1;


    // TODO: Rename and change types of parameters
    private ArrayList<Student> mArrayList;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayList<Student> studentArrayList = new ArrayList<>();
    private StudentAdapter mStudentAdapter;
    private Context mContext;
    public RecyclerView rvStudents;
    private Button btnAdd;

    public StudentListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DbHelper dbHelper = new DbHelper(getActivity());


        studentArrayList = dbHelper.getAllStudents();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StudentListFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public StudentListFragment() {
//
////        Bundle args = new Bundle();
////        args.putParcelableArrayList(ARG_ARRAY_LIST, studentArrayList);
//////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
////        return fragment;
//    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_list, container, false);
        initViews(view);
        mContext = getActivity();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * initializing views
     */
    public void initViews(View view) {
        btnAdd = view.findViewById(R.id.buttonAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(StudentListActivity.EXTRA_IS_FROM_ADD, "Add");
                bundle.putParcelableArrayList("studentList", mArrayList);
                mListener.addData();
            }
        });
        rvStudents = view.findViewById(R.id.recyclerViewStudents);
        setRecyclerAdapter();
    }

    private void setRecyclerAdapter() {
        mStudentAdapter = new StudentAdapter(studentArrayList, mContext);
        rvStudents.setAdapter(mStudentAdapter);
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

//
//    public void addStudent(Bundle bundle){
//        if(bundle.getString(MODE).equals(NORMAL)) {
//            Student student = new Student(bundle.getString(NAME), bundle.getString(ROLL_NO));
//            studentArrayList.add(student);
//            mStudentAdapter.notifyDataSetChanged();
//        }else if(bundle.getString(MODE).equals(EDIT)){
////            Student student=studentArrayList.get();
//            Student student=new Student();
//            student.setRollNo(bundle.getString(ROLL_NO));
//            student.setName(bundle.getString(NAME));
//            mStudentAdapter.notifyDataSetChanged();
//        }
//    }
    public void onItemClick(final int position) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        String[] options = {"View", "Edit", "Delete"};
        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
//                        viewMode(studentArrayList.get(position));
                        break;
                    case 1:
//                        editMode(studentArrayList.get(position));
                        break;
                    case 2:
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
//                                        Student student=studentArrayList.get(position);
//                                        Intent dIntent=new Intent(mContext, BgService.class);
//                                        dIntent.putExtra(StudentListActivity.EXTRA_IS_FROM_DELETE,true);
//                                        studentArrayList.remove(position);

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


//    private void viewMode(final Student student) {
//        Intent intentView = new Intent(mContext, ViewStudentActivity.class);
//        intentView.putExtra(StudentListActivity.EXTRA_SELECTED_STUDENT, student);
//        intentView.putExtra(StudentListActivity.EXTRA_IS_FROM_VIEW, true);
//        startActivity(intentView);
//    }

    /**
     * editMode creates intent to addstudentactivity to edit student info
     * and save changes then startactivityresult takes intent and request code for edit
//     */
//    private void editMode(final Student student) {
//        Intent intentEdit = new Intent(mContext, ViewStudentActivity.class);
//        intentEdit.putExtra(StudentListActivity.EXTRA_SELECTED_STUDENT, student);
//        int index = 0;
//        for (int i = 0; i < studentArrayList.size(); i++) {
//            if (studentArrayList.get(i).getRollNo() == student.getRollNo()) {
//                index = i;
//            }
//        }
//        intentEdit.putExtra(StudentListActivity.EXTRA_INDEX, index);
//        intentEdit.putExtra(StudentListActivity.EXTRA_IS_FROM_EDIT, true);
//        intentEdit.putParcelableArrayListExtra(StudentListActivity.EXTRA_STUDENT_LIST, studentArrayList);
//        startActivityForResult(intentEdit, Constants.REQUEST_CODE_EDIT);
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name

        void addData();

        void editData(int position);

    }
}
