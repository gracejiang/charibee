package com.example.service.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.service.R;
import com.example.service.WelcomeActivity;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    // ui views
    private ImageView ivAvatar;
    private TextView tvFullName;
    private TextView tvUsername;
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
        btnLogout = view.findViewById(R.id.profile_logout_btn);

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String firstName = (String) currentUser.get("firstName");
        String lastName = (String) currentUser.get("lastName");
        String username = currentUser.getUsername();

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

        // logout button clicked
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                goWelcomeActivity();
            }
        });
    }

    // go to welcome activity when log out button clicked
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

}