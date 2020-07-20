package com.example.service.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.service.R;
import com.example.service.functions.OrgsAdapter;
import com.example.service.models.Organization;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    public static final String TAG = "DiscoverFragment";

    private OrgsAdapter adapter;

    // data source
    private List<Organization> allOrgs = new ArrayList<>();
    private List<Organization> currOrgs = new ArrayList<>();

    // ui views
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvOrgs;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // bind ui views
        etSearch = view.findViewById(R.id.discover_search_et);
        btnSearch = view.findViewById(R.id.discover_search_btn);
        rvOrgs = view.findViewById(R.id.discover_orgs_rv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recycler view adapter
        updateMovieAdapter(allOrgs);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchPhrase = etSearch.getText().toString();
                updateMovieAdapter(search(searchPhrase));
            }
        });

        queryOrgs();
    }

    // search for specific organization
    private List<Organization> search(String phrase) {
        List<Organization> searchResults = new ArrayList<>();
        for (Organization org : allOrgs) {
            if (org.getName().toLowerCase().contains(phrase)) {
                searchResults.add(org);
            }
        }
        return searchResults;
    }

    // update org adapter given list of organizations
    private void updateMovieAdapter(List<Organization> orgsList) {
        adapter = new OrgsAdapter(getContext(), orgsList); // (1) create adapter
        rvOrgs.setAdapter(adapter); // (2) set adapter on rv
        rvOrgs.setLayoutManager(new LinearLayoutManager(getContext())); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }

    // retrieve orgs from parse database
    private void queryOrgs() {
        allOrgs.clear();
        ParseQuery<Organization> query = ParseQuery.getQuery(Organization.class);

        query.findInBackground(new FindCallback<Organization>() {
            @Override
            public void done(List<Organization> orgs, ParseException e) {

                if (e != null) {
                    // issue getting post
                    Log.e(TAG, "issue loading orgs from parse" + e.getMessage());
                    return;
                }

                for (Organization org : orgs) {
                    // Log.i(TAG, org.getName());
                }

                allOrgs.addAll(orgs);
                adapter.notifyDataSetChanged();

            }
        });
    }




}