package com.example.service.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.service.R;
import com.example.service.WelcomeActivity;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {

    private TextView tvName;
    private Button btnLogout;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        String name = (String) currentUser.get("firstName");

        // find views
        tvName = view.findViewById(R.id.profile_name);
        btnLogout = view.findViewById(R.id.profile_logout_btn);

        // set text
        tvName.setText("Welcome back, " + name + "!");

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


}