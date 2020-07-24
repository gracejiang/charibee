package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.service.models.Organization;
import com.example.service.models.User;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class OrganizationDetailsActivity extends AppCompatActivity {

    public static final String TAG = "OrganizationDetailsActivity";

    Organization org;

    // ui views
    private TextView tvName;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvOrganizer;
    private TextView btnJoinOrg;

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
        btnJoinOrg = findViewById(R.id.org_details_join_btn);

        setOrgValues();

        // when join org button pressed
        btnJoinOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinOrganization();
            }
        });
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

    private void joinOrganization() {
        // add org to current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        User user = new User(currentUser);
        user.addOrg(org);

        // add current user to org
        org.addVolunteer(currentUser);
    }

}