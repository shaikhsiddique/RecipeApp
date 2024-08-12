package com.example.recipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.Activities.Admin.AdminActivity;
import com.example.recipeapp.Activities.UserPanel.UserActivity;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.example.recipeapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth firebaseAuth;
    private AppUtils appUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(LoginActivity.this, getResources().getColor(R.color.purple_500));

        firebaseAuth = FirebaseAuth.getInstance();

        binding.newAccount.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        binding.login.setOnClickListener(view -> {
            if (binding.email.getText().toString().equals("admin@gmail.com") &&
                    binding.password.getText().toString().equals("admin@123")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
            } else {
                login();
            }
        });

    }

    private void login() {

        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();



            // Validate the inputs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            } else{



            // Authenticate user with Firebase
            firebaseAuth.signInWithEmailAndPassword(binding.email.getText().toString().trim(), binding.password.getText().toString().trim())
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Login success
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(this, UserActivity.class));
                            // You can navigate to another activity or perform additional actions here
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("TAG", Objects.requireNonNull(task.getException().getMessage()));
                        }
                    });
            }
    }
}
