package com.example.service.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.activities.NewOrganizationActivity;
import com.example.service.adapters.HomeOrgsAdapter;
import com.example.service.models.Organization;
import com.example.service.models.User;
import com.parse.ParseUser;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeOrgsAdapter adapter;
    private List<Organization> orgs;

    // ui views
    private Button btnNewOrg;
    private RecyclerView rvOrgs;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // bind ui views
        btnNewOrg = v.findViewById(R.id.home_new_org_btn);
        rvOrgs = v.findViewById(R.id.home_orgs_rv);

        // set view if volunteer
        if (ParseUser.getCurrentUser().get("role").equals("Volunteer")) {
            btnNewOrg.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrive & display orgs of current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        User user = new User(currentUser);
        orgs = user.getOrganizations();
        updateAdapter(orgs);

        // when new org button clicked
        btnNewOrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), NewOrganizationActivity.class);
                startActivity(i);
            }
        });

    }

    // update org adapter given list of organizations
    private void updateAdapter(List<Organization> orgsList) {
        adapter = new HomeOrgsAdapter(getContext(), orgsList); // (1) create adapter
        rvOrgs.setAdapter(adapter); // (2) set adapter on rv
        rvOrgs.setLayoutManager(new LinearLayoutManager(getContext())); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

}