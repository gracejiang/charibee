package com.example.service.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.service.R;
import com.example.service.adapters.CommunityUsersAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    public static final String TAG = "CommunityFragment";

    private CommunityUsersAdapter adapter;

    // data source
    List<ParseUser> allUsers = new ArrayList<>();

    // ui views
    private EditText etSearch;
    private Button btnSearch;
    private RecyclerView rvUsers;

    public CommunityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_community, container, false);

        etSearch = v.findViewById(R.id.community_search_et);
        btnSearch = v.findViewById(R.id.community_search_btn);
        rvUsers = v.findViewById(R.id.community_users_rv);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "this is being reached");

        // recycler view adapter
        updateAdapter(allUsers);

        // when search button clicked
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "this is being clicked lol..");
                String searchPhrase = etSearch.getText().toString();
                queryUsers(searchPhrase);
            }
        });

    }

    // update users adapter given list of users
    private void updateAdapter(List<ParseUser> usersList) {
        adapter = new CommunityUsersAdapter(getContext(), usersList); // (1) create adapter
        rvUsers.setAdapter(adapter); // (2) set adapter on rv
        rvUsers.setLayoutManager(new LinearLayoutManager(getContext())); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }


    // retrieve users from parse database
    private void queryUsers(String searchTerm) {
        allUsers.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        // add query parameter for search term
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue loading users from parse" + e.getMessage());
                    return;
                }

                allUsers.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });
    }
}