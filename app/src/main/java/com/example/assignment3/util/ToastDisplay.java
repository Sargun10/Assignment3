package com.example.assignment3.util;

import android.content.Context;
import android.widget.Toast;

public class ToastDisplay {
    public static void displayToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
