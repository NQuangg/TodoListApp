package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.logging.Handler;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        Thread background = new Thread() {
            public void run() {
                try {
                    sleep(SPLASH_DISPLAY_LENGTH);

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);

                    finish();
                } catch (Exception e) {
                }
            }
        };


        background.start();
    }
}