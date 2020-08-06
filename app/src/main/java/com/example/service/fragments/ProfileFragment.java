package com.example.service.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.example.service.models.User;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    User user;

    // ui views
    public ImageView ivAvatar;
    private TextView tvFullName;
    private TextView tvUsername;
    private TextView tvBio;
    private Button btnEditProfile;

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
            try {
                if (profilePic.getUrl() != null) {
                    downloadAndShowImage(profilePic);
                } else if (profilePic.getData() != null) {
                    byte[] photoBytes = profilePic.getData();
                    ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length));
                } else {
                    Log.e(TAG, "couldnt load profile pic");
                }
            } catch (ParseException e) {
                downloadAndShowImage(profilePic);
            }
        } else {
            Log.e(TAG, "couldn't load profile pic");
        }

        // set text
        tvFullName.setText(firstName + " " + lastName);
        tvUsername.setText("@" + username);
        tvBio.setText(bio);
    }

    private void downloadAndShowImage(ParseFile profilePic) {
        Glide.with(getContext())
                .load(httpToHttps(profilePic.getUrl()))
                .circleCrop()
                .into(ivAvatar);
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }

    // go to edit profile activity
    private void goEditProfileActivity() {
        Intent i = new Intent(getContext(), EditProfileActivity.class);
        startActivity(i);
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