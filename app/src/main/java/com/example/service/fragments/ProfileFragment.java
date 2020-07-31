package com.example.service.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.service.R;
import com.example.service.activities.EditProfileActivity;
import com.example.service.activities.SettingsActivity;
import com.example.service.activities.WelcomeActivity;
import com.example.service.models.User;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    User user;

    // ui views
    private ImageView ivAvatar;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvBio;
    private Button btnEditProfile;
    private Button btnUpdateSettings;
    private Button btnLogout;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // find views
        ivAvatar = view.findViewById(R.id.profile_avatar_iv);
        tvFullName = view.findViewById(R.id.profile_fullname);
        tvUsername = view.findViewById(R.id.profile_username);
        tvBio = view.findViewById(R.id.profile_bio);
        btnEditProfile = view.findViewById(R.id.profile_edit_btn);
        btnUpdateSettings = view.findViewById(R.id.profile_settings_btn);
        btnLogout = view.findViewById(R.id.profile_logout_btn);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set values
        setValues();

        // edit profile button clicked
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goEditProfileActivity();
            }
        });

        // settings button clicked
        btnUpdateSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSettingsActivity();
            }
        });

        // logout button clicked
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                goWelcomeActivity();
            }
        });
    }

    private void setValues() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        user = new User(currentUser);

        String firstName = (String) currentUser.get("firstName");
        String lastName = (String) currentUser.get("lastName");
        String username = currentUser.getUsername();
        String bio = user.getBio();

        // set profile pic
        ParseFile profilePic = (ParseFile) currentUser.get("profilePic");
        if (profilePic != null) {
            Glide.with(getContext())
                    .load(httpToHttps(profilePic.getUrl()))
                    .circleCrop()
                    .into(ivAvatar);
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // set text
        tvFullName.setText(firstName + " " + lastName);
        tvUsername.setText("@" + username);
        tvBio.setText(bio);
    }

    @Override
    public void onResume() {
        setValues();
        super.onResume();
    }

    // go to edit profile activity
    private void goEditProfileActivity() {
        Intent i = new Intent(getContext(), EditProfileActivity.class);
        startActivity(i);
    }

    // go to settings activity
    private void goSettingsActivity() {
        Intent i = new Intent(getContext(), SettingsActivity.class);
        startActivity(i);
    }

    // go to welcome activity
    private void goWelcomeActivity() {
        Intent i = new Intent(getContext(), WelcomeActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    // converts http link to https
    private String httpToHttps(String url) {
        if (url.contains("https")) {
            return url;
        }

        String httpsUrl = "https" + url.substring(4);
        return httpsUrl;
    }

    public void updateBioTv(String bio) {
        tvBio.setText(bio);
    }

}