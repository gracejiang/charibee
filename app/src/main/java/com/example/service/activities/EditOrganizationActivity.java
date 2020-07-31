package com.example.service.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.service.R;
import com.example.service.data.Data;
import com.example.service.functions.CategorySpinnerClass;
import com.example.service.models.Organization;

public class EditOrganizationActivity extends AppCompatActivity {

    private Organization org;

    // ui views
    private EditText etName;
    private Spinner spnCategory;
    private EditText etTagline;
    private EditText etDescription;
    private EditText etAddress;
    private EditText etWebsite;
    private EditText etEmail;
    private EditText etPhoneNumber;
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
        etTagline = findViewById(R.id.edit_org_tagline);
        etDescription = findViewById(R.id.edit_org_description);
        etAddress = findViewById(R.id.edit_org_address);
        etWebsite = findViewById(R.id.edit_org_website);
        etEmail = findViewById(R.id.edit_org_email);
        etPhoneNumber = findViewById(R.id.edit_org_phone_number);
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
        etTagline .setText(org.getTagline());
        etDescription.setText(org.getDescription());

        // set if theres a value
        setIntoView(etAddress, org.getAddress());
        setIntoView(etWebsite, org.getWebsite());
        setIntoView(etEmail, org.getEmail());
        setIntoView(etPhoneNumber, org.getPhoneNumber());

        // load in spinner view
        int spinnerPosition = adapter.getPosition(org.getCategory());
        spnCategory.setSelection(spinnerPosition);
    }

    // set value into view if there is a value
    private void setIntoView(EditText et, String value) {
        if (value != null && value.length() > 0) {
            et.setText(value);
        }
    }

    // checks if fields are valid
    private boolean validFields() {
        if (etName.getText().length() == 0) {
            makeMessage("Please enter a name for your organization.");
            return false;
        } else if (etTagline.length() == 0) {
            makeMessage("Please enter a tagline for your organization.");
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
        org.setAddress(etAddress.getText().toString());
        org.setWebsite(etWebsite.getText().toString());
        org.setEmail(etEmail.getText().toString());
        org.setPhoneNumber(etPhoneNumber.getText().toString());

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