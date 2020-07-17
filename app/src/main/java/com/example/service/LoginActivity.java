package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    // ui views
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // binds ui elements
        etEmail = findViewById(R.id.login_username);
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

        ParseUser.logInInBackground(email, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    goMainActivity();
                } else {
                    makeMessage("Invalid username or password. Please try again.");
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