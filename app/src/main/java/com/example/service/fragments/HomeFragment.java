package com.example.service.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.service.MainActivity;
import com.example.service.R;
import com.example.service.models.NewOrganizationActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    // ui views
    private Button btnNewOrg;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // bind ui views
        btnNewOrg = v.findViewById(R.id.home_new_org_btn);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // when new org button clicked
        btnNewOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewOrganizationActivity.class);
                startActivity(i);
            }
        });
    }

}