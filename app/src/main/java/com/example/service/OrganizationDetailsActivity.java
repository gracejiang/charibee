package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.service.models.Organization;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class OrganizationDetailsActivity extends AppCompatActivity {

    public static final String TAG = "OrganizationDetailsActivity";

    Organization org;

    // ui views
    private TextView tvName;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        org = (Organization) Parcels.unwrap(getIntent().getParcelableExtra(Organization.class.getSimpleName()));

        // bind ui views
        tvName = findViewById(R.id.org_details_name);
        tvCategory = findViewById(R.id.org_details_category);
        tvDescription = findViewById(R.id.org_details_description);
        tvOrganizer = findViewById(R.id.org_details_organizer);

        setOrgValues();
    }

    // set orgs values to ui views
    private void setOrgValues() {

        String name = org.getName();
        String category = org.getCategory();
        String description = org.getDescription();
        ParseUser organizer = org.getOrganizer();

        String organizerText = "";
        try {
            organizerText = "Organized by " + organizer.fetchIfNeeded().getString("firstName") + " "
                            + organizer.fetchIfNeeded().getString("lastName") + ".";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvName.setText(name);
        tvCategory.setText(category);
        tvDescription.setText(description);
        tvOrganizer.setText(organizerText);
    }

    private void testType() {

    }

}