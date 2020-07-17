package com.example.service.models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.service.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewOrganizationActivity extends AppCompatActivity {

    public static final String TAG = "NewOrganizationActivity";

    // ui views
    private EditText etName;
    private EditText etDescription;
    private EditText etCategory;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organization);

        // bind ui views
        etName = findViewById(R.id.new_org_name);
        etDescription = findViewById(R.id.new_org_description);
        etCategory = findViewById(R.id.new_org_category);
        btnSubmit = findViewById(R.id.new_org_submit_btn);

        // when submit button clicked
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String category = etCategory.getText().toString();

                if (validOrganization(name, description, category)) {
                    registerOrganization(name, description, category);
                }
            }
        });

    }

    private boolean validOrganization(String name, String description, String category) {
        if (name.length() == 0 || description.length() == 0 || category.length() == 0) {
            makeMessage("Please fill out all the fields.");
            return false;
        }
        return true;
    }

    private void registerOrganization(String name, String description, String category) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        Organization org = new Organization(name, description, category, currentUser);
        org.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                } else {
                    Log.e(TAG, "Error will saving", e);
                    makeMessage("There was an error registering your organization. Please try again later.");
                }
            }
        });
    }

    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}