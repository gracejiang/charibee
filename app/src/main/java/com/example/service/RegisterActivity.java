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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private DatabaseReference usersRef = rootRef.child("users");

    // ui views
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPasswordConfirm;
    // add spinner later
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initialize firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        // binds ui elements
        etFirstName = findViewById(R.id.register_first_name);
        etLastName = findViewById(R.id.register_last_name);
        etEmail = findViewById(R.id.register_email);
        etPassword = findViewById(R.id.register_password);
        etPasswordConfirm = findViewById(R.id.register_password_confirm);
        btnRegister = findViewById(R.id.register_submit_btn);

        // register new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String passwordConfirm = etPasswordConfirm.getText().toString();

                if (validRegisterUser(firstName, lastName, email, password, passwordConfirm)) {
                    registerUser(firstName, lastName, email, password);
                }
            }
        });
    }

    // checks if fields are valid to register
    private boolean validRegisterUser(String firstName, String lastName, String email, String password, String passwordConfirm) {
        if (firstName.length() == 0 || lastName.length() == 0 || email.length() == 0 || password.length() == 0 || passwordConfirm.length() == 0) {
            makeMessage("Please make sure all fields are filled out.");
            return false;
        } else if (!isEmail(email)) {
            makeMessage("Please enter a valid email.");
        } else if (!password.equals(passwordConfirm)) {
            makeMessage("Please make sure your passwords match.");
            return false;
        } else if (password.length() < 6) {
            makeMessage("Your password must be at least 6 characters long.");
            return false;
        }
        return true;
    }

    // registers a new user
    private void registerUser(final String firstName, final String lastName, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // registration success
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    String userId = currentUser.getUid();

                    // add user to database
                    DatabaseReference userRef = usersRef.child(userId);

                    // add in custom fields
                    userRef.child("firstName").setValue(firstName);
                    userRef.child("lastName").setValue(lastName);
                    userRef.child("numPoints").setValue(0);
                    userRef.child("profilePicUrl").setValue("");

                    // go to main activity
                    goMainActivity();
                } else {
                    // registration failed
                    Log.e(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
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

}