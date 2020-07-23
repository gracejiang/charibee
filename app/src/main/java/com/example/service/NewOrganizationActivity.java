package com.example.service;

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

import com.example.service.functions.CategorySpinnerClass;
import com.example.service.models.Organization;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewOrganizationActivity extends AppCompatActivity {

    public static final String TAG = "NewOrganizationActivity";

    // ui views
    private EditText etName;
    private EditText etDescription;
    private Spinner spinnerCategory;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organization);

        // bind ui views
        etName = findViewById(R.id.new_org_name);
        etDescription = findViewById(R.id.new_org_description);
        spinnerCategory = findViewById(R.id.new_org_spinner_categories);
        btnSubmit = findViewById(R.id.new_org_submit_btn);

        // when submit button clicked
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();

                Log.i(TAG, name + " " + description + " " + category);

                if (validOrganization(name, description, category)) {
                    registerOrganization(name, description, category);
                }
            }
        });

        createCategoryAdapter();

    }

    private boolean validOrganization(String name, String description, String category) {
        if (name.length() == 0 || description.length() == 0 || category.length() == 0) {
            makeMessage("Please fill out all the fields.");
            return false;
        }
        return true;
    }

    private void registerOrganization(final String name, String description, String category) {
        Organization org = new Organization();

        // set object values
        org.setName(name);
        org.setDescription(description);
        org.setOrganizer(ParseUser.getCurrentUser());
        org.setCategory(category);

        org.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i(TAG, name + " was registered");
                    goMainActivity();
                } else {
                    Log.e(TAG, "Error will saving", e);
                    makeMessage("There was an error registering your organization. Please try again later.");
                }
            }
        });

        createCategoryAdapter();
    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // displays message to user
    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // create spinner for categories
    private void createCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new CategorySpinnerClass());
    }
}