package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.service.data.Data;
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
    private Button btnJoinOrg;
    private Button btnEditOrg;

    // org details variables
    private String currUserId = "";
    private boolean userInOrg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        currUserId = ParseUser.getCurrentUser().getObjectId();
        org = (Organization) Parcels.unwrap(getIntent().getParcelableExtra(Organization.class.getSimpleName()));

        // bind ui views
        tvName = findViewById(R.id.org_details_name);
        tvCategory = findViewById(R.id.org_details_category);
        tvDescription = findViewById(R.id.org_details_description);
        tvOrganizer = findViewById(R.id.org_details_organizer);
        btnJoinOrg = findViewById(R.id.org_details_join_btn);
        btnEditOrg = findViewById(R.id.org_details_edit_btn);

        setOrgValues();

        // when join org button pressed
        btnJoinOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userInOrg) {
                    leaveOrganization();
                    userInOrg = false;
                    setButtonValue(userInOrg);
                } else {
                    joinOrganization();
                    userInOrg = true;
                    setButtonValue(userInOrg);
                }
            }
        });

        if (currUserId.equals(org.getOrganizer().getObjectId())) {
            // when edit org button pressed
            btnEditOrg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goEditOrganizationActivity();
                }
            });
        } else {
            btnEditOrg.setVisibility(View.GONE);
        }
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

        userInOrg = checkIfUserInOrg();
        setButtonValue(userInOrg);

    }


    // check if in org
    private boolean checkIfUserInOrg() {
        if (org.getVolunteerIds().contains(currUserId)) {
            return true;
        }
        return false;
    }

    // set button value based off in org or not
    private void setButtonValue(boolean inOrg) {
        if (inOrg) {
            btnJoinOrg.setText("Leave Organization");
        } else {
            btnJoinOrg.setText("Join Organization");
        }
    }

    // lets current user join organization
    private void joinOrganization() {
        // add org to current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        User user = new User(currentUser);
        user.addOrg(org);

        // add current user to org
        org.addVolunteer(currentUser);

        // tell user successfully joined org
        makeMessage("You have successfully joined the group " + org.getName() + "!");
    }

    // removes current user from organization
    private void leaveOrganization() {
        // remove org from current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        User user = new User(currentUser);
        user.removeOrg(org);

        // remove current user from org
        org.removeVolunteer(currentUser);

        // tell user successfully left org
        makeMessage("You have successfully left the group " + org.getName() + "!");
    }

    // go to edit organization activity
    private void goEditOrganizationActivity() {
        // looked into: https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
        // but can't implement both Parcelable and Serializable in same class (Organization), so decided to abstract data
        // out to seperate data class

        Intent i = new Intent(this, EditOrganizationActivity.class);
        Data.setOrg(org);
        startActivity(i);
    }

    // display message to user
    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}