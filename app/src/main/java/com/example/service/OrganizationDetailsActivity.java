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
    private TextView tvAddress;
    private TextView tvWebsite;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private Button btnJoinOrg;
    private Button btnEditOrg;
    private Button btnDeleteOrg;

    // org details variables
    private ParseUser currentParseUser;
    private User currentUser;
    private boolean userInOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        currentParseUser = ParseUser.getCurrentUser();
        currentUser = new User(currentParseUser);
        org = (Organization) Parcels.unwrap(getIntent().getParcelableExtra(Organization.class.getSimpleName()));

        // bind ui views
        tvName = findViewById(R.id.org_details_name);
        tvCategory = findViewById(R.id.org_details_category);
        tvDescription = findViewById(R.id.org_details_description);
        tvAddress = findViewById(R.id.org_details_address);
        tvWebsite = findViewById(R.id.org_details_website);
        tvEmail = findViewById(R.id.org_details_email);
        tvPhoneNumber = findViewById(R.id.org_details_phone_number);
        btnJoinOrg = findViewById(R.id.org_details_join_btn);
        btnEditOrg = findViewById(R.id.org_details_edit_btn);
        btnDeleteOrg = findViewById(R.id.org_details_delete_btn);

        setOrgValues();

        // when join / leave org button pressed
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


        // set visibility for the edit button
        if (isOrganizer()) {
            // when edit org button pressed
            btnEditOrg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goEditOrganizationActivity();
                }
            });

            // when delete button pressed
            btnDeleteOrg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String name = org.getName();
                    deleteOrg();
                    goDiscoverFragment();
                    makeMessage("Your organization " + name + " has been deleted.");
                }
            });
        } else {
            btnEditOrg.setVisibility(View.GONE);
            btnDeleteOrg.setVisibility(View.GONE);
        }
    }

    private boolean isOrganizer() {
        return currentParseUser.getObjectId().equals(org.getOrganizer().getObjectId());
    }

    // set orgs values to ui views
    private void setOrgValues() {
        String name = org.getName();
        String category = org.getCategory();
        String description = org.getDescription();
        String address = org.getAddress();
        String website = org.getWebsite();
        String email = org.getEmail();
        String phoneNumber = org.getPhoneNumber();
        ParseUser organizer = org.getOrganizer();

        String organizerText = "";
        try {
            organizerText = "Organized by " + organizer.fetchIfNeeded().getString("firstName") + " "
                            + organizer.fetchIfNeeded().getString("lastName") + ".";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // required fields
        tvName.setText(name);
        tvCategory.setText(category);
        tvDescription.setText(description);

        // not required fields
        setIntoView(tvAddress, address);
        setIntoView(tvWebsite, website);
        setIntoView(tvEmail, email);
        setIntoView(tvPhoneNumber, phoneNumber);

        // sets join or leave into button
        userInOrg = checkIfUserInOrg();
        setButtonValue(userInOrg);

    }

    // set value into view if value is not null
    private void setIntoView(TextView tv, String value) {
        if (value != null && value.length() > 0) {
            tv.setText(value);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    // check if user in org
    private boolean checkIfUserInOrg() {
        List<String> orgIds = currentUser.getOrganizationIds();
        String currOrgId = org.getObjectId();
        return orgIds.contains(currOrgId);
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
        currentUser.addOrg(org);

        // add current user to org
        org.addVolunteer(currentParseUser);

        // tell user successfully joined org
        makeMessage("You have successfully joined the group " + org.getName() + "!");
    }

    // removes current user from organization
    private void leaveOrganization() {
        // remove org from current user
        currentUser.removeOrg(org);

        // remove current user from org
        org.removeVolunteer(currentParseUser);

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

    // delete an org
    private void deleteOrg() {
        List<ParseUser> parseUsers = org.getVolunteers();

        for (ParseUser parseUser : parseUsers) {
            User user = new User(parseUser);
            user.removeOrg(org);
            Log.i(TAG, "size: " + user.getOrganizations().size());
        }

        org.deleteInBackground();
    }

    private void goDiscoverFragment() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

}