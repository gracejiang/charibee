package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    EditText etEmail;
    EditText etPassword;
    Button btnLogin;
    Button btnRegister;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference rootRef = database.getReference();
    DatabaseReference usersRef = rootRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // check if user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            goMainActivity();
        }

        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_button);
        btnRegister = findViewById(R.id.login_register);

        // login a user
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (validLoginUser(email, password)) {
                    loginUser(email, password);
                }
            }
        });

        // register new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if (validRegisterUser(email, password)) {
                    registerUser(email, password);
                }
            }
        });

    }

    // checks if fields are valid to login
    private boolean validLoginUser(String email, String password) {
        if (email == null || email.length() == 0) {
            makeMessage("Please enter a valid email.");
            return false;
        } else if (password == null|| password.length() == 0) {
            makeMessage("Please enter a valid password.");
            return false;
        }
        return true;
    }

    // checks if fields are valid to register
    private boolean validRegisterUser(String email, String password) {
        if (email == null || email.length() == 0) {
            makeMessage("Please enter a valid email.");
            return false;
        } else if (password == null|| password.length() < 6) {
            makeMessage("Your password must be at least 6 characters long.");
            return false;
        }
        return true;
    }

    // logs the user in
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            goMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Invalid email/password. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // registers a new user
    private void registerUser(final String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            String userId = currentUser.getUid();
                            DatabaseReference userRef = usersRef.child(userId);
                            userRef.child("numPoints").setValue(0);
                            userRef.child("profilePicUrl").setValue("");
                            goMainActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
}