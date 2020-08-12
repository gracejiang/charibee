package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.charibee.data.Data;
import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class UserDetailsActivity extends AppCompatActivity {

    public static final String TAG = "UserDetailsActivity";

    User user;

    // ui views
    ImageView ivAvatar;
    TextView tvFullName;
    TextView tvUsername;
    TextView tvBio;
    Button btnMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        // unwrap
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra(User.class.getSimpleName()));

        // bind views
        ivAvatar = findViewById(R.id.user_details_avatar_iv);
        tvFullName = findViewById(R.id.user_details_fullname);
        tvUsername = findViewById(R.id.user_details_username);
        tvBio = findViewById(R.id.user_details_bio);
        btnMsg = findViewById(R.id.user_details_msg_btn);

        // load values into views
        setValues();

        if (user.getId().equals(ParseUser.getCurrentUser().getObjectId())) {
            btnMsg.setVisibility(View.GONE);
        }
        btnMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Data.setToUser(user);
                goChatActivity();
            }
        });
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