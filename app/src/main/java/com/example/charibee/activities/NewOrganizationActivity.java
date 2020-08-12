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

import com.example.service.R;
import com.example.charibee.data.Data;
import com.example.charibee.functions.CategorySpinnerClass;
import com.example.charibee.location.MapActivity;
import com.example.charibee.models.Organization;
import com.example.charibee.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.regex.Pattern;

public class NewOrganizationActivity extends AppCompatActivity {

    public static final String TAG = "NewOrganizationActivity";

    // ui views
    private EditText etName;
    private Spinner spinnerCategory;
    private EditText etTagline;
    private EditText etDescription;
    private Button btnSetAddress;
    private EditText etWebsite;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_organization);

        // bind ui views
        etName = findViewById(R.id.new_org_name);
        spinnerCategory = findViewById(R.id.new_org_spinner_categories);
        etTagline = findViewById(R.id.new_org_tagline);
        etDescription = findViewById(R.id.new_org_description);
        btnSetAddress = findViewById(R.id.new_org_set_address_btn);
        etWebsite = findViewById(R.id.new_org_website);
        etEmail = findViewById(R.id.new_org_email);
        etPhoneNumber = findViewById(R.id.new_org_phone_number);
        btnSubmit = findViewById(R.id.new_org_submit_btn);

        // reset address data
        clearData();

        // when set address button clicked
        btnSetAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goMapActivity();
            }
        });

        // when submit button clicked
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = etName.getText().toString();
                String category = spinnerCategory.getSelectedItem().toString();
                String tagline = etTagline.getText().toString();
                String description = etDescription.getText().toString();
                String website = etWebsite.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();

                if (validOrganization(name, category, tagline, description, email, phoneNumber)) {
                    registerOrganization(name, category, tagline, description, website, email, phoneNumber);
                }
            }
        });

        createCategoryAdapter();

    }

    @Override
    protected void onResume() {
        btnSetAddress.setText(Data.getAddress());
        super.onResume();
    }

    private void clearData() {
        Data.clearAddress();
        Data.setLat(0);
        Data.setLng(0);
    }

    private boolean validOrganization(String name, String category, String tagline, String description, String email, String phoneNumber) {
        if (name.length() == 0 || category.length() == 0 || tagline.length() == 0 || description.length() == 0) {
            makeMessage("Please fill out all required fields.");
            return false;
        } else if (email.length() != 0 && !isEmail(email)) {
            makeMessage("Please enter a valid email.");
            return false;
        }
        return true;
    }

    private void registerOrganization(final String name, String category, String tagline, String description, String website, String email, String phoneNumber) {
        final Organization org = new Organization();

        final ParseUser currentUser = ParseUser.getCurrentUser();

        // set org values
        org.setName(name);
        org.setCategory(category);
        org.setTagline(tagline);
        org.setDescription(description);
        org.setOrganizer(currentUser);
        org.setAddress(Data.getAddress());
        org.setWebsite(website);
        org.setEmail(email);
        org.setPhoneNumber(phoneNumber);

        // save to parse
        org.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // add org to user
                    User u = new User(currentUser);
                    u.addOrg(org);

                    // add user to org
                    org.addVolunteer(currentUser);

                    // return to main activity
                    goMainActivity();
                } else {
                    Log.e(TAG, "Error will saving", e);
                    makeMessage("There was an error registering your organization. Please try again later.");
                }
            }
        });

        createCategoryAdapter();
    }

    // go to map activity
    private void goMapActivity() {
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
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

    // checks if an email is valid
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

    // create spinner for categories
    private void createCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
        spinnerCategory.setOnItemSelectedListener(new CategorySpinnerClass());
    }
}