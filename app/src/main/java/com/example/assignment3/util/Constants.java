package com.example.assignment3.util;

import com.example.assignment3.database.DbHelper;

public class Constants {
    public final static String DB_NAME="StudentDatabase";
    public final static String INDEX = "index";
    public final static String SELECTED_STUDENT = "extra_selected_student";
    public final static String IS_FROM_VIEW = "is_from_view";
    public final static String IS_FROM_EDIT = "is_from_edit";
    public final static String IS_FROM_ADD = "is_from_add";
    public final static String IS_FROM_DELETE = "is_from_delete";
    public static final int ADD_STUDENT_FRAG = 1;
    public static final int STUD_LIST_FRAG = 0;
    public static final String BUNDLE_ARRAY_LIST = "studentList";
    public static final String SERVICE_FILTER_ACTION_KEY = "filter_action_service";
    public static final String INTENT_SERVICE_FILTER_ACTION_KEY = "filter_action_intent_service";
    public static final int OPTION_VIEW = 0;
    public static final int OPTION_EDIT = 1;
    public static final int OPTION_DELETE = 2;
    public static final int TOTAL_FRAG = 2;
    public static String PRESENT_STUDENT = "presentStudent";
    public static String MODE = "mode";
    public static String PREVIOUS_STUDENT_ID = "previousStudentId";


}
