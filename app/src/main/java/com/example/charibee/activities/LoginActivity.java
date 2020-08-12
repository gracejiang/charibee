package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service.R;
import com.example.charibee.data.Data;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    // ui views
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // binds ui elements
        etUsername = findViewById(R.id.login_username);
        etPassword = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.login_submit_btn);

        // login a user when login button pressed
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (validLoginUser(username, password)) {
                    loginUser(username.toLowerCase(), password);
                }

                Log.i(TAG, "clicked login");
            }
        });

    }

    // checks if fields are valid to login
    private boolean validLoginUser(String username, String password) {
        if (username == null || username.length() == 0) {
            makeMessage("Please enter a valid username.");
            return false;
        } else if (password == null|| password.length() == 0) {
            makeMessage("Please enter a valid password.");
            return false;
        }
        return true;
    }

    // logs the user in
    private void loginUser(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Data.updateCurrUser();
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
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        this.finish();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }

}