package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.service.data.Data;
import com.example.service.functions.CategorySpinnerClass;
import com.example.service.models.Organization;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class EditOrganizationActivity extends AppCompatActivity {

    private Organization org;

    // ui views
    private EditText etName;
    private Spinner spnCategory;
    private EditText etDescription;
    private Button btnSave;

    // spinner adapter
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organization);

        org = Data.getOrg();

        // bind ui views
        etName = findViewById(R.id.edit_org_name);
        spnCategory = findViewById(R.id.edit_org_category_spinner);
        etDescription = findViewById(R.id.edit_org_description);
        btnSave = findViewById(R.id.edit_org_save_btn);

        // load in views
        createCategoryAdapter();
        loadInViews();

        // when save button clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validFields()) {
                    saveChanges();
                    goHomeFragment();
                }
            }
        });
    }

    // load in views
    private void loadInViews() {
        // load in text views
        etName.setText(org.getName());
        String description = org.getDescription();

        if (description.length() > 0) {
            etDescription.setText(description);
        }

        // load in spinner view
        int spinnerPosition = adapter.getPosition(org.getCategory());
        spnCategory.setSelection(spinnerPosition);
    }

    // checks if fields are valid
    private boolean validFields() {
        if (etName.getText().length() == 0) {
            makeMessage("Please enter a name for your organization.");
            return false;
        } else if (etDescription.getText().length() == 0) {
            makeMessage("Please enter a description for your organization.");
            return false;
        }
        return true;
    }

    // save changes to updated organization
    private void saveChanges() {
        org.setName(etName.getText().toString());
        org.setDescription(etDescription.getText().toString());
        org.setCategory(spnCategory.getSelectedItem().toString());
        org.saveInBackground();
    }

    // return to home fragment
    private void goHomeFragment() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // create spinner for categories
    private void createCategoryAdapter() {
        adapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new CategorySpinnerClass());
    }

    // shows user a message
    private void makeMessage(String message) {
        Toast.makeText(EditOrganizationActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}