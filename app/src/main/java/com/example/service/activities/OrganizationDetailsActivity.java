package com.example.service.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.adapters.VolunteersAdapter;
import com.example.service.data.Data;
import com.example.service.models.Organization;
import com.example.service.models.User;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

    private ImageButton btnWebsite;
    private ImageButton btnPhone;
    private ImageButton btnEmail;
    private ImageButton btnAddress;

    private Button btnJoinOrg;
    private Button btnEditOrg;
    private Button btnDeleteOrg;
    private TextView tvOrganizer;
    private RecyclerView rvVolunteers;

    // popup ui views
    private Dialog popupDialog;
    private ImageView ivPopupIcon;
    private TextView tvPopupText;
    private ImageView popupClose;


    // volunteer details
    private VolunteersAdapter adapter;
    private List<ParseUser> volunteersList = new ArrayList<>();

    // org details variables
    private ParseUser currentParseUser;
    private User currentUser;
    private boolean userInOrg;
    private int NUM_OF_COLUMNS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_details);

        // current values
        currentParseUser = ParseUser.getCurrentUser();
        currentUser = new User(currentParseUser);
        org = (Organization) Parcels.unwrap(getIntent().getParcelableExtra(Organization.class.getSimpleName()));

        // bind ui views
        bindUiViews();

        // sets ui views
        setUiValues();

        // sets join or leave into button
        userInOrg = checkIfUserInOrg();
        setButtonValue(userInOrg);

        // popup views
        popupDialog = new Dialog(this);

        // allow users to click organizer profile
        tvOrganizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User organizer = new User(org.getOrganizer());
                Intent intent = new Intent(OrganizationDetailsActivity.this, UserDetailsActivity.class);
                intent.putExtra(User.class.getSimpleName(), Parcels.wrap(organizer));
                OrganizationDetailsActivity.this.startActivity(intent);
            }
        });

        // volunteers recycler view
        updateAdapter(volunteersList);
        queryVolunteers();

        // set visibility for edit, delete, and join/leave buttons based off if owner of org or not
        if (hasPermissionsToEdit()) {
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

            // hide leave / join org button
            btnJoinOrg.setVisibility(View.GONE);
        } else {
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

            // hide edit / delete org buttons
            btnEditOrg.setVisibility(View.GONE);
            btnDeleteOrg.setVisibility(View.GONE);
        }
    }

    // bind ui values
    private void bindUiViews() {
        // bind ui views
        tvName = findViewById(R.id.org_details_name);
        tvCategory = findViewById(R.id.org_details_category);
        tvDescription = findViewById(R.id.org_details_description);

        // image btns
        btnWebsite = findViewById(R.id.org_details_website_btn);
        btnPhone = findViewById(R.id.org_details_phone_btn);
        btnEmail = findViewById(R.id.org_details_email_btn);
        btnAddress = findViewById(R.id.org_details_address_btn);

        // other values
        tvOrganizer = findViewById(R.id.org_details_organizer);
        btnJoinOrg = findViewById(R.id.org_details_join_btn);
        btnEditOrg = findViewById(R.id.org_details_edit_btn);
        btnDeleteOrg = findViewById(R.id.org_details_delete_btn);
        rvVolunteers = findViewById(R.id.org_details_rv_volunteers);
    }

    // set orgs values to ui views
    private void setUiValues() {
        String name = org.getName();
        String category = org.getCategory();
        String description = org.getDescription();
        User organizer = new User(org.getOrganizer());
        String organizerName = organizer.getName();

        // required fields
        tvName.setText(name);
        tvOrganizer.setText("Organized by " + organizerName);
        tvCategory.setText(category);
        tvDescription.setText(description);

        // contact values
        setContactValues();
    }

    // set contact values
    private void setContactValues() {
        String address = org.getAddress();
        String website = org.getWebsite();
        String email = org.getEmail();
        final String phoneNumber = org.getPhoneNumber();

        // not required fields
        setIntoView(btnWebsite, R.drawable.ic_website_24, website);
        setIntoView(btnPhone, R.drawable.ic_phone_24, phoneNumber);
        setIntoView(btnEmail, R.drawable.ic_email_24, email);
        setIntoView(btnAddress, R.drawable.ic_baseline_map_24, address);
    }


    // set value into view if value is not null
    private void setIntoView(final ImageButton ib, final int icon, final String value) {
        if (value != null && value.length() > 0) {
            // bind url
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(icon, value);
                }
            });
        } else {
            ib.setVisibility(View.GONE);
        }
    }

    // show popup for user to see
    private void showPopup(int icon, String value) {
        popupDialog.setContentView(R.layout.popup);

        // bind views
        ivPopupIcon = popupDialog.findViewById(R.id.popup_icon_iv);
        tvPopupText = popupDialog.findViewById(R.id.popup_text_tv);
        popupClose = popupDialog.findViewById(R.id.popup_close_iv);

        tvPopupText.setText(value);
        ivPopupIcon.setImageDrawable(ContextCompat.getDrawable(this, icon));

        // when x button clicked
        popupClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupDialog.dismiss();
            }
        });

        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.show();
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

    // sets recycler view to display all volunteers
    private void updateAdapter(List<ParseUser> usersList) {
        adapter = new VolunteersAdapter(this, usersList); // (1) create adapter
        rvVolunteers.setAdapter(adapter); // (2) set adapter on rv
        rvVolunteers.setLayoutManager(new GridLayoutManager(this, NUM_OF_COLUMNS)); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

    // query all volunteer users
    private void queryVolunteers() {
        volunteersList.clear();
        volunteersList.addAll((List<ParseUser>) org.get(Organization.KEY_VOLUNTEERS));
        adapter.notifyDataSetChanged();
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
            Log.i(TAG, user.getName()  + " deleteOrg: " + user.getOrganizations().size());
        }

        // org.deleteInBackground();
    }

    // go back to discover fragment
    private void goDiscoverFragment() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // if current user has permissions to edit
    private boolean hasPermissionsToEdit() {
        return currentParseUser.getObjectId().equals(org.getOrganizer().getObjectId());
    }

    @Override
    public void onResume() {
        super.onResume();
        getOrgFromParse();
    }

    // when resuming func, redownload org from parse
    private void getOrgFromParse() {
        ParseQuery<Organization> query = ParseQuery.getQuery(Organization.class);
        query.getInBackground(org.getObjectId(), new GetCallback<Organization>() {
            public void done(Organization foundOrg, ParseException e) {
                if (e == null) {
                    Data.setOrg(foundOrg);
                    org = foundOrg;
                    setUiValues();
                } else {
                    Log.e(TAG, "error retriving org from parse");
                }
            }
        });
    }

}