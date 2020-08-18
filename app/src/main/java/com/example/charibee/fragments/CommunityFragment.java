package com.example.charibee.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.User;
import com.example.service.R;
import com.example.charibee.adapters.CommunityUsersAdapter;
import com.example.charibee.functions.CustomItemDivider;
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
    List<ParseUser> allUsers;

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

        // resetting search
        etSearch.setText("");

        // recycler view adapter
        allUsers = new ArrayList<>();
        updateAdapter(allUsers);

        // add dividers btwn msgs
        RecyclerView.ItemDecoration dividerItemDecoration = new CustomItemDivider(ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider));
        rvUsers.addItemDecoration(dividerItemDecoration);

        // when search button clicked
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchPhrase = etSearch.getText().toString();
                if (searchPhrase != null & searchPhrase.length() > 0) {
                    queryUsers(searchPhrase);
                } else {
                    allUsers.clear();
                    updateAdapter(allUsers);
                }
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
    private void queryUsers(final String searchTerm) {
        allUsers.clear();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> users, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "issue loading users from parse" + e.getMessage());
                    return;
                }

                allUsers.addAll(filterUsers(users, searchTerm));
                adapter.notifyDataSetChanged();
            }
        });
    }

    // filters users based off search term
    private List<ParseUser> filterUsers(List<ParseUser> pUsers, String searchTerm) {
        List<ParseUser> filteredUsers = new ArrayList<>();
        for (ParseUser pU : pUsers) {
            User u = new User(pU);
            if (u.getName().toLowerCase().contains(searchTerm.toLowerCase())) {
                filteredUsers.add(pU);
            }
        }
        return filteredUsers;
    }
}