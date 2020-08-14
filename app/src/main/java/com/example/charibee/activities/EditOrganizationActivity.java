package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.charibee.data.Data;
import com.example.charibee.data.RoleTheme;
import com.example.charibee.functions.CategorySpinnerClass;
import com.example.charibee.location.MapActivity;
import com.example.charibee.models.Organization;
import com.example.service.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class EditOrganizationActivity extends AppCompatActivity {

    public static final String TAG = "EditOrganizationActivity";

    private Organization org;

    // ui views
    private EditText etName;
    private Spinner spnCategory;
    private EditText etTagline;
    private EditText etDescription;
    private Button btnAddress;
    private EditText etWebsite;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private Button btnSave;

    // spinner adapter
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoleTheme.applyTheme(this);
        setContentView(R.layout.activity_edit_organization);

        org = Data.getOrg();
        Data.clearAddress();

        // bind ui views
        etName = findViewById(R.id.edit_org_name);
        spnCategory = findViewById(R.id.edit_org_category_spinner);
        etTagline = findViewById(R.id.edit_org_tagline);
        etDescription = findViewById(R.id.edit_org_description);
        btnAddress = findViewById(R.id.edit_org_address_btn);
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
                }
            }
        });

        // when edit address button clicked
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMapActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        btnAddress.setText(Data.getAddress());
        super.onResume();
    }

    // load in views
    private void loadInViews() {
        // load in text views
        etName.setText(org.getName());
        etTagline .setText(org.getTagline());
        etDescription.setText(org.getDescription());

        // set if theres a value
        setIntoView(btnAddress, org.getAddress());
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

    private void setIntoView(Button btn, String value) {
        if (value != null && value.length() > 0) {
            btnAddress.setText(value);
            Data.setAddress(value);
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
        org.setTagline(etTagline.getText().toString());
        org.setDescription(etDescription.getText().toString());
        org.setCategory(spnCategory.getSelectedItem().toString());
        org.setAddress(Data.getAddress());
        org.setWebsite(etWebsite.getText().toString());
        org.setEmail(etEmail.getText().toString());
        org.setPhoneNumber(etPhoneNumber.getText().toString());

        org.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // error while posting pic
                    Log.e(TAG, "error updating your profile", e);
                    return;
                } else {
                    goHomeFragment();
                }
            }
        });

    }

    // go to map activity
    private void goMapActivity() {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
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