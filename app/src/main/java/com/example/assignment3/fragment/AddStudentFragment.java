package com.example.assignment3.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.assignment3.R;
import com.example.assignment3.activity.StudentListActivity;
import com.example.assignment3.adapter.StudentAdapter;
import com.example.assignment3.communicator.CommunicationFragments;
import com.example.assignment3.database.DbHelper;
import com.example.assignment3.model.Student;
import com.example.assignment3.service.BgAsync;
import com.example.assignment3.service.BgIntentService;
import com.example.assignment3.service.BgService;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AddStudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class AddStudentFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SERVICE_FILTER_ACTION_KEY = "filter_action_service";
    public static final String INTENT_SERVICE_FILTER_ACTION_KEY = "filter_action_intent_service";
    public static String PRESENT_STUDENT = "presentStudent";
    public static String MODE = "mode";
    public static boolean isFromEdit = false;
    public static String PREVIOUS_STUDENT_ID = "previousStudentId";
    private Button buttonSave;
    private EditText editTextName, editTextRollNo;
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private Context mContext;
    Validate validate = new Validate();
    private String rollNo, name;
    public static int mSelectedPosition;
    public static boolean mIsFromAdd = false, mIsFromEdit = false, mIsFromView = false;
    private DbHelper dbHelper;
    private MyReceiver myReceiver;
    AlertDialog mAlert;
    StudentAdapter mStudentAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CommunicationFragments mListener;

    public AddStudentFragment() {
        // Required empty public constructor
    }

    public static AddStudentFragment newInstance(String param1, String param2) {
        AddStudentFragment fragment = new AddStudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (context instanceof CommunicationFragments) {
            mListener = (CommunicationFragments) getActivity();
        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        dbHelper = new DbHelper(mContext);
        studentArrayList.addAll(dbHelper.getAllStudents());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews(View view) {
        buttonSave = view.findViewById(R.id.buttonSave);
        editTextName = view.findViewById(R.id.editTextName);
        editTextRollNo = view.findViewById(R.id.editTextRollNo);

    }

    public void getMode(Bundle bundle) {
        mSelectedPosition = bundle.getInt(StudentListActivity.EXTRA_INDEX, -1);
        mIsFromAdd = bundle.getBoolean(StudentListActivity.EXTRA_IS_FROM_ADD, false);
        mIsFromView = bundle.getBoolean(StudentListActivity.EXTRA_IS_FROM_VIEW, false);
        mIsFromEdit = bundle.getBoolean(StudentListActivity.EXTRA_IS_FROM_EDIT, false);

        Log.d("----------", "getMode: ");
        if (mIsFromAdd) {
            addStudent();
        } else if (mIsFromEdit) {
            editStudent(mSelectedPosition);
        }
    }

    private void editStudent(final int position) {
        Log.d("-------------", String.valueOf(position)+"            "+studentArrayList.size());
        editTextName.setText(studentArrayList.get(position).getName());
        editTextRollNo.setText(studentArrayList.get(position).getRollNo());

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("h", "onClick: up");
                //Validation lgauni
                name = editTextName.getText().toString();
                rollNo = editTextRollNo.getText().toString();

                Student student = studentArrayList.get(position);
                student.setName(name);
                student.setRollNo(rollNo);

//                mListener.callOtherFragToAdd();
                serviceDialogBox(student, StudentListActivity.EXTRA_IS_FROM_EDIT, StudentListActivity.EXTRA_SELECTED_STUDENT);
                editTextName.setFocusable(false);
                editTextRollNo.setFocusable(false);
            }
        });

    }

    private void addStudent() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("h", "onClick: ");
                name = editTextName.getText().toString();
                rollNo = editTextRollNo.getText().toString();
                Student student = new Student(name, rollNo);
                serviceDialogBox(student, StudentListActivity.EXTRA_IS_FROM_ADD, StudentListActivity.EXTRA_SELECTED_STUDENT);
                editTextName.setFocusable(false);
                editTextRollNo.setFocusable(false);
            }
        });
    }

    /**
     * initializing views
     */


//                if (mIsFromAdd) {
//                    if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
//                        editTextName.requestFocus();
//                        editTextName.setError(getString(R.string.Valid_name));
//                    } else {
//                        if (rollNo.length() == 0 || !validate.uniqueValidation(studentArrayList, rollNo)) {
//                            editTextRollNo.requestFocus();
//                            editTextRollNo.setError(getString(R.string.Valid_RollNo));
//                        }
//                        else {
//                            Student student=new Student(name,rollNo);
//                            serviceDialogBox(student,StudentListActivity.EXTRA_IS_FROM_ADD,StudentListActivity.EXTRA_SELECTED_STUDENT);
//                            mListener.updateData();
////                            setResultOnValidation(intentSave, name, rollNo);
//
//                        }
//                    }
//                } else {
//                    mListener.updateData();
//                    intentSave.putExtra(StudentListActivity.EXTRA_INDEX, mSelectedPosition);
//
//                    if (!validate.uniqueValidation(studentArrayList, rollNo, mSelectedPosition)) {
//                        Student student=new Student(name,rollNo);
//                        Log.d("---------------" ,"onClickSave: after calling dialog "+studentArrayList.get(mSelectedPosition).getRollNo());
//
//                        serviceDialogBox(student,StudentListActivity.EXTRA_IS_FROM_EDIT,studentArrayList.get(mSelectedPosition).getRollNo());
////                dbHelper.updateQuery(student,studentArrayList.get(mSelectedPosition).getRollNo());
//                        setResultOnValidation(intentSave, name, rollNo);
//                    } else {
//                        editTextRollNo.requestFocus();
//                        editTextRollNo.setError(getString(R.string.Valid_Roll_No));
//                    }
//                }
    public void viewStudent(Student student) {
        rollNo = student.getRollNo();
        name = student.getName();
        buttonSave.setVisibility(View.INVISIBLE);
        editTextName.setEnabled(false);
        editTextRollNo.setEnabled(false);
        getDataFromEditTexts();
        editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    // to set text in edit texts in case of both edit and view
    private void getDataFromEditTexts() {
        DbHelper dbHelper = new DbHelper(mContext);
        Student student = dbHelper.getStudent(Integer.parseInt(rollNo));
        editTextName.setText(student.getName());
        editTextRollNo.setText(student.getRollNo());
    }

    /**
     * on click of save button data is fetched from edit text and validations are applied
     * string name should not be null and has only alphabets
     * roll number must be unique
     * in case of edit index is checked and fetched and then intent is fired
     * in case of adding a student only validations are done
     */

    public void serviceDialogBox(final Student presentStudent, final String mode, final String previousStudentPosition) {

        final String[] items = {"Services", "Intent Services", "Async Task"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Option to save student details");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(StudentListActivity.BUNDLE_ARRAY_LIST,studentArrayList);
                switch (which) {
                    case 0:
                        Intent serviceIntent = new Intent(mContext, BgService.class);
                        serviceIntent.putExtra(PRESENT_STUDENT, presentStudent);
                        serviceIntent.putExtra(MODE, mode);
                        serviceIntent.putExtra(PREVIOUS_STUDENT_ID, previousStudentPosition);
                        getActivity().startService(serviceIntent);
                        mListener.communicateAddStudent(presentStudent);

                        bundle.putParcelableArrayList(StudentListActivity.EXTRA_STUDENT_LIST, studentArrayList);
                        mListener.communicateEditStudent(bundle);
//                        getActivity().finish();
                        break;
                    case 1:
                        Intent iSIntent = new Intent(mContext, BgIntentService.class);
                        iSIntent.putExtra(PRESENT_STUDENT, presentStudent);
                        iSIntent.putExtra(MODE, mode);
                        iSIntent.putExtra(PREVIOUS_STUDENT_ID, previousStudentPosition);
                        getActivity().startService(iSIntent);
                        mListener.communicateAddStudent(presentStudent);

                        bundle = new Bundle();
                        bundle.putParcelableArrayList(StudentListActivity.EXTRA_STUDENT_LIST, studentArrayList);
                        mListener.communicateEditStudent(bundle);
                        break;
                    case 2:
                        BgAsync bgAsync = new BgAsync(mContext);
                        bgAsync.execute(presentStudent, mode, previousStudentPosition);
                        mListener.communicateAddStudent(presentStudent);
                        bundle.putParcelableArrayList(StudentListActivity.EXTRA_STUDENT_LIST, studentArrayList);
                        mListener.communicateEditStudent(bundle);
//                        getActivity().finish();
                        break;
                }

            }
        });
        mAlert = builder.create();
        mAlert.show();

    }

    private void setReceiver() {
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SERVICE_FILTER_ACTION_KEY);
        intentFilter.addAction(INTENT_SERVICE_FILTER_ACTION_KEY);
        LocalBroadcastManager.getInstance(mContext).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        setReceiver();
    }

    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(myReceiver);
        super.onStop();
    }

    @Override
    public void refresh() {
        studentArrayList = getArguments().getParcelableArrayList(StudentListActivity.BUNDLE_ARRAY_LIST);

        if (getArguments().getBoolean(StudentListActivity.EXTRA_IS_FROM_EDIT)) {
//            Student student = getArguments().getParcelable(StudentListActivity.EXTRA_SELECTED_STUDENT);
//            editTextName.setText(student.getName());
//            editTextRollNo.setText(student.getRollNo());
            buttonSave.setText("Edit");
            editStudent(getArguments().getInt(StudentListActivity.EXTRA_INDEX));

        }

    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SERVICE_FILTER_ACTION_KEY) || intent.getAction().equals(INTENT_SERVICE_FILTER_ACTION_KEY)) {
                mAlert.dismiss();
                //getActivity().finish();
            }
        }
    }

}
