
package com.example.recipeapp.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import com.example.recipeapp.Activities.UserPanel.UserActivity;
import com.example.recipeapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().getContext().getColor(R.color.purple_500);
        // Use a handler to delay the transition to the main activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    Intent mainIntent = new Intent(SplashActivity.this, UserActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Intent mainIntent = new Intent(SplashActivity.this, SignUpActivity.class);
                    startActivity(mainIntent);
                    finish();                }
                // Start the main activity after the splash delay

            }
        }, 3000);
        setStatusBarColor(getResources().getColor(R.color.purple_200));

    }
    private void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }
}