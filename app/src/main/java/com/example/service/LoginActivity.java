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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    // ui views
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // binds ui elements
        etEmail = findViewById(R.id.login_email);
        etPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_submit_btn);

        // login a user when login button pressed
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

    // logs the user in
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                // signs in user
                goMainActivity();
            } else {
                // sign in failure
                Log.e(TAG, "signInWithEmail:failure", task.getException());
                Toast.makeText(LoginActivity.this, "Invalid email/password. Please try again.", Toast.LENGTH_SHORT).show();
            }
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