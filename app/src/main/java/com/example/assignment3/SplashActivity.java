package com.example.assignment3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.assignment3.activity.StudentListActivity;

public class SplashActivity extends AppCompatActivity {
    public final static long SPLASH_TIME = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        startTimer();
    }

    /**
     * start timer for delay in the current screen
     */
    private void startTimer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextScreenAndFinish();
            }
        }, SPLASH_TIME);
    }

    /**
     * show the respective next screen and finish the current one
     */
    private void showNextScreenAndFinish() {
        startActivity(new Intent(SplashActivity.this, StudentListActivity.class));
        finish();
    }
}
