package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class OrganizationDetailsActivity extends AppCompatActivity {

    // ui views
    private TextView tvName;
    private TextView tvCategory;
    private TextView tvDescription;
    private TextView tvOrganizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        // bind ui views
        tvName = findViewById(R.id.org_details_name);
        tvCategory = findViewById(R.id.org_details_category);
        tvDescription = findViewById(R.id.org_details_description);
        tvOrganizer = findViewById(R.id.org_details_organizer);
    }
}