package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.charibee.adapters.InterestsIconAdapter;
import com.example.charibee.data.Data;
import com.example.charibee.data.RoleTheme;
import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String TAG = "UserDetailsActivity";

    User user;

    // ui views
    private ImageView ivAvatar;
    private ImageView ivAdmin;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvBio;
    private RecyclerView rvInterests;

    // adapter for interests
    private InterestsIconAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoleTheme.applyTheme(this);
        setContentView(R.layout.activity_user_details);

        // unwrap
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        // bind views
        ivAvatar = findViewById(R.id.user_details_avatar_iv);
        ivAdmin = findViewById(R.id.user_details_admin_iv);
        tvFullName = findViewById(R.id.user_details_fullname);
        tvUsername = findViewById(R.id.user_details_username);
        tvBio = findViewById(R.id.user_details_bio);
        rvInterests = findViewById(R.id.user_details_interests);

        // top nav bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvToolbarTitle = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        tvToolbarTitle.setText(user.getName());
        setTitle("");

        // load values into views
        setValues();
    }

    // options menu for messaging someone
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.user_details_top_menu, menu);

        // if viewing own profile, don't show message option
        if (user.getId().equals(ParseUser.getCurrentUser().getObjectId())) {
            menu.findItem(R.id.user_details_msg_mbtn).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    // when message button clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_details_msg_mbtn:
                Data.setToUser(user);
                goChatActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // go to chat activity
    private void goChatActivity() {
        Intent i = new Intent(this, ChatActivity.class);
        startActivity(i);
    }

    // loads values into views
    private void setValues() {
        String fullname = (String) user.getName();
        String username = user.getUsername();
        String bio = user.getBio();

        // set admin
        if (user.getRole().equals("Organizer")) {
            // hide interests
            TextView tvInterests = findViewById(R.id.user_details_interets_tv);
            tvInterests.setVisibility(View.GONE);
            rvInterests.setVisibility(View.GONE);

            // show admin badge
            ivAdmin.setImageResource(R.drawable.ic_admin);
        } else {
            // show interests
            updateAdapter(user.getStringInterests());

            // hide admin badge
            ivAdmin.setVisibility(View.GONE);
        }

        // set profile pic
        ParseFile profilePic = (ParseFile) user.getProfilePic();
        if (profilePic != null) {
            Glide.with(this)
                    .load(httpToHttps(profilePic.getUrl()))
                    .circleCrop()
                    .into(ivAvatar);
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // set text
        tvFullName.setText(fullname);
        tvUsername.setText("@" + username);
        tvBio.setText(bio);
    }

    private void updateAdapter(List<String> interestsList) {
        adapter = new InterestsIconAdapter(this, interestsList); // (1) create adapter
        rvInterests.setAdapter(adapter); // (2) set adapter on rv
        rvInterests.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

    // converts http link to https
    private String httpToHttps(String url) {
        if (url == null) {
            return "";
        }

        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }
}