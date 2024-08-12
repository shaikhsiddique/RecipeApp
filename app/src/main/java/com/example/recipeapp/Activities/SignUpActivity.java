package com.example.recipeapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipeapp.Activities.UserPanel.UserActivity;
import com.example.recipeapp.Model.User;
import com.example.recipeapp.R;
import com.example.recipeapp.Utils.AppUtils;
import com.example.recipeapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private AppUtils appUtils;

    private ActivitySignUpBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        appUtils = new AppUtils();
        getWindow().getContext().getColor(R.color.purple_500);
        appUtils.setStatusBarColor(SignUpActivity.this, getResources().getColor(R.color.purple_500));

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        binding.haveAnAccount.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        binding.createMyAccount.setOnClickListener(view -> signUp());

        // Set onClickListener for Google Sign-In button
        binding.googleAuthentication.setOnClickListener(view -> signInWithGoogle());

        // Configure Google Sign-In
        configureGoogleSignIn();
    }

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("548448357915-ec62jj6bs29ruvqacfbre0o1kapp37qj.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Log.w("GoogleSignIn", "signInResult: failed code=" + e.getStatusCode());
            Toast.makeText(SignUpActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(SignUpActivity.this, "Google Sign-In successful", Toast.LENGTH_SHORT).show();

                        // Save user data to the database
                        saveUserData(user.getEmail(), user.getDisplayName(), "");
                        startActivity(new Intent(this, UserActivity.class));

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                        Toast.makeText(SignUpActivity.this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUp() {
        final String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        final String username = binding.username.getText().toString().trim();

        // Validate the inputs
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new user with email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-up success
                        Toast.makeText(SignUpActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();

                        // Save user data to the database
                        saveUserData(email, username, password);
                        startActivity(new Intent(this, UserActivity.class));


                    } else {
                        // If sign-up fails, display a message to the user.
                        Toast.makeText(SignUpActivity.this, "Sign-up failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String email, String username, String password) {
        // Get the user ID of the current authenticated user
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Create a User object with email and username
        User user = new User(username, email, password);

        // Save the user data to the "users" node in the database
        databaseReference.child(userId).setValue(user);
    }


}
