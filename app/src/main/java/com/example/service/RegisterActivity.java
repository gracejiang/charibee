package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.service.functions.RoleSpinnerClass;
import com.example.service.models.Organization;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";

    // ui views
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    private Spinner spinnerRole;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // binds ui elements
        etFirstName = findViewById(R.id.register_first_name);
        etLastName = findViewById(R.id.register_last_name);
        etUsername = findViewById(R.id.register_username);
        etEmail = findViewById(R.id.register_email);
        etPassword = findViewById(R.id.register_password);
        etPasswordConfirm = findViewById(R.id.register_password_confirm);
        spinnerRole = findViewById(R.id.register_role_spinner);
        btnRegister = findViewById(R.id.register_submit_btn);

        // register new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (validRegisterUser(firstName, lastName, username, email, password, passwordConfirm)) {
                    registerUser(firstName, lastName, username, email, password);
                }
            }
        });

        createRoleAdapter();
    }

    // checks if fields are valid to register
    private boolean validRegisterUser(String firstName, String lastName, String username, String email, String password, String passwordConfirm) {
        if (firstName.length() == 0 || lastName.length() == 0 || username.length() == 0 || email.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0) {
            makeMessage("Please make sure all fields are filled out.");
            return false;
        } else if (!isEmail(email)) {
            makeMessage("Please enter a valid email.");
        } else if (!password.equals(passwordConfirm)) {
            makeMessage("Please make sure your passwords match.");
            return false;
        }
        return true;
    }

    // registers a new user
    private void registerUser(final String firstName, final String lastName, String username, String email, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("numPoints", 0);
        user.put("bio", " ");
        user.put("orgsJoined", new ArrayList<Organization>());
        user.put("orgsJoinedIds", new ArrayList<String>());

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    goMainActivity();
                } else {
                    Log.e(TAG, String.valueOf(e));
                    makeMessage("There was a problem with your registration. Please try again.");
                }
            }
        });
    }

    // is an email
    private static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    // create spinner for role options
    private void createRoleAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.role_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapter);
        spinnerRole.setOnItemSelectedListener(new RoleSpinnerClass());
    }

}