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
import com.example.assignment3.util.Constants;
import com.example.assignment3.util.Validate;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */

public class AddStudentFragment extends BaseFragment {
    private Button buttonSave;
    private EditText editTextName, editTextRollNo;
    private ArrayList<Student> studentArrayList = new ArrayList<Student>();
    private Context mContext;
    Validate validate = new Validate();
    private String rollNo, name;
    private static int mSelectedPosition;
    private static boolean mIsFromAdd, mIsFromEdit;
    private DbHelper dbHelper;
    private MyReceiver myReceiver;
    AlertDialog mAlert;
    Student student;
    private CommunicationFragments mListener;
    public AddStudentFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        this.mContext = context;
        super.onAttach(context);
        if (context instanceof CommunicationFragments) {
            mListener = (CommunicationFragments)mContext;
        }
      else {
          throw new RuntimeException(context.toString()
                  + " must implement Communication Fragments");
      }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(mContext);
        studentArrayList.addAll(dbHelper.getAllStudents());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_student, container, false);
        initViews(view);
       // addStudent();
        refresh();
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
        handleEvents();

    }


    public void getMode(Bundle bundle) {
        mSelectedPosition = bundle.getInt(Constants.INDEX, -1);
        mIsFromAdd = bundle.getBoolean(Constants.IS_FROM_ADD, false);
        mIsFromEdit = bundle.getBoolean(Constants.IS_FROM_EDIT, false);
//        if (mIsFromAdd) {
//            clearFields();
//        }
        if (mIsFromEdit) {
            student = bundle.getParcelable(Constants.SELECTED_STUDENT);
            getDataFromEditTexts(student);
        }
    }
    /**
     * on click of save button data is fetched from edit text and validations are applied
     * string name should not be null and has only alphabets
     * roll number must be unique
     *
     * in case of adding a student validations are done
     */

    public void handleEvents() {

        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                name = editTextName.getText().toString();
                rollNo = editTextRollNo.getText().toString();
                if (mIsFromAdd) {
                    if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
                        editTextName.requestFocus();
                        editTextName.setError(getString(R.string.Valid_name));
                    } else {
                        if (rollNo.length() == 0 || !validate.uniqueValidation(studentArrayList, rollNo)) {
                            editTextRollNo.requestFocus();
                            editTextRollNo.setError(getString(R.string.Valid_RollNo));
                        } else {
//                            clearFields();
                            editTextRollNo.setEnabled(true);
                            Student student = new Student(name, rollNo);
                            editTextName.setFocusable(false);
                            editTextRollNo.setFocusable(false);
                            serviceDialogBox(student, Constants.IS_FROM_ADD, rollNo);
                        }
                    }
                } else if(mIsFromEdit){
                    if (name.trim().length() == 0 || !validate.isStringOnly(name)) {
                        editTextName.requestFocus();
                        editTextName.setError(getString(R.string.Valid_name));
                    }
                        else {
                        Log.d("---------", "mIsFromEditttttttt "+student);
                            Student newstudent = new Student(name, rollNo);
                            serviceDialogBox(newstudent, Constants.IS_FROM_EDIT,student.getRollNo());
                            Bundle bundle = new Bundle();
                            bundle.putInt(Constants.INDEX, mSelectedPosition);
                            bundle.putParcelable(Constants.SELECTED_STUDENT, student);
                        }
                    }
                }

        });
    }
    public void viewStudent(Student student) {

        rollNo = student.getRollNo();
        name = student.getName();
        buttonSave.setVisibility(View.INVISIBLE);
        editTextName.setEnabled(false);
        editTextRollNo.setEnabled(false);
        getDataFromEditTexts(student);
        editTextName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        editTextRollNo.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }


    // to set text in edit texts in case of both edit and view
    private void getDataFromEditTexts(Student student) {
        editTextName.setText(student.getName());
        editTextRollNo.setText(student.getRollNo());
        editTextRollNo.setEnabled(false);
    }

    /**
     * this displays service dialog box as to what bg service to use to save data
     * @param presentStudent
     * @param mode
     * @param previousStudentPosition
     */
    public void serviceDialogBox(final Student presentStudent, final String mode, final String previousStudentPosition) {

        final String[] items = {"Services", "Intent Services", "Async Task"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select Option to save student details");
        editTextName.setEnabled(true);
        editTextRollNo.setEnabled(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.BUNDLE_ARRAY_LIST,studentArrayList);
                switch (which) {
                    case 0:
                        Intent serviceIntent = new Intent(mContext, BgService.class);
                        serviceIntent.putExtra(Constants.PRESENT_STUDENT, presentStudent);
                        serviceIntent.putExtra(Constants.MODE, mode);
                        serviceIntent.putExtra(Constants.PREVIOUS_STUDENT_ID, previousStudentPosition);
                        mContext.startService(serviceIntent);
                        if(mIsFromAdd){
                            Log.d("----------", "onClick: "+presentStudent.getName());
                            mListener.communicateAddStudent(presentStudent);
                            mListener.callOtherFragToAdd();
                        }
                        else if(mIsFromEdit){
                            Log.d("aaaaaaa", "onClick: "+mode);
                            bundle.putInt(Constants.INDEX,mSelectedPosition);
                            bundle.putParcelable(Constants.SELECTED_STUDENT, presentStudent);
                            mListener.communicateEditStudent(bundle);
                        }
                        break;
                    case 1:
                        Intent iSIntent = new Intent(mContext, BgIntentService.class);
                        iSIntent.putExtra(Constants.PRESENT_STUDENT, presentStudent);
                        iSIntent.putExtra(Constants.MODE, mode);
                        iSIntent.putExtra(Constants.PREVIOUS_STUDENT_ID, previousStudentPosition);
                        getActivity().startService(iSIntent);
                        if(mIsFromAdd){
                            mListener.communicateAddStudent(presentStudent);
                            mListener.callOtherFragToAdd();
                        }
                        else if(mIsFromEdit){
                        Log.d("aaaaaaa", "onClick: "+mode);
                            bundle.putInt(Constants.INDEX,mSelectedPosition);
                        bundle.putParcelable(Constants.SELECTED_STUDENT, presentStudent);
                        mListener.communicateEditStudent(bundle);
                    }
                        break;
                    case 2:
                        BgAsync bgAsync = new BgAsync(mContext);
                        bgAsync.execute(presentStudent, mode, previousStudentPosition);
                        if(mIsFromAdd){
                            mListener.communicateAddStudent(presentStudent);
                            mListener.callOtherFragToAdd();
                        }
                        else if(mIsFromEdit){
                        Log.d("aaaaaaa", "onClick: "+mode);
                        bundle.putInt(Constants.INDEX,mSelectedPosition);
                        bundle.putParcelable(Constants.SELECTED_STUDENT, presentStudent);
                        mListener.communicateEditStudent(bundle);
                    }
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
        intentFilter.addAction(Constants.SERVICE_FILTER_ACTION_KEY);
        intentFilter.addAction(Constants.INTENT_SERVICE_FILTER_ACTION_KEY);
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
        studentArrayList = getArguments().getParcelableArrayList(Constants.BUNDLE_ARRAY_LIST);

    }

    public void clearFields() {
        editTextRollNo.setText("");
        editTextName.setText("");
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constants.SERVICE_FILTER_ACTION_KEY) || intent.getAction().equals(Constants.INTENT_SERVICE_FILTER_ACTION_KEY)) {
                mAlert.dismiss();
            }
        }
    }

}
