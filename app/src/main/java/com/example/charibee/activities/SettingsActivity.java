package com.example.charibee.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.service.R;
import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {

    // current parse user
    ParseUser currentUser;

    // ui views
    private EditText etNewPassword;
    private EditText etConfirmNewPassword;
    private Button btnCancel;
    private Button btnSave;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // current user
        currentUser = ParseUser.getCurrentUser();

        // bind ui views
        etNewPassword = findViewById(R.id.settings_new_password);
        etConfirmNewPassword = findViewById(R.id.settings_confirm_new_password);
        btnCancel = findViewById(R.id.settings_cancel_btn);
        btnSave = findViewById(R.id.settings_save_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // admin
        // admin view
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            btnSave.setBackgroundColor(getResources().getColor(R.color.dark_red));
        }

        // set toolbar
        setSupportActionBar(toolbar);
        setTitle("Settings");

        // when cancel buton clicked
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goProfileFragment();
            }
        });

        // when save button clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newPassword = etNewPassword.getText().toString();
                String confirmPassworld = etConfirmNewPassword.getText().toString();

                if (validPasswords(newPassword, confirmPassworld)) {
                    updatePassword(newPassword);
                    goProfileFragment();
                    makeMessage("Your password was updated.");
                }
            }
        });
    }

    private boolean validPasswords(String newPw, String confirmPw) {
        if (!newPw.equals(confirmPw)) {
            makeMessage("Please make sure your passwords match.");
            return false;
        } else if (newPw.length() == 0) {
            makeMessage("Password field cannot be blank.");
            return false;
        }
        return true;
    }

    private void updatePassword(String password) {
        currentUser.setPassword(password);
        currentUser.saveInBackground();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // return to profile fragment
    private void goProfileFragment() {
        finish();
    }
}