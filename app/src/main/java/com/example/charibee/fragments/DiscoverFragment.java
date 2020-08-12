package com.example.charibee.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.Organization;
import com.example.service.R;
import com.example.charibee.adapters.DiscoverOrgsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscoverFragment extends Fragment {

    public static final String TAG = "DiscoverFragment";

    private DiscoverOrgsAdapter adapter;

    // data source
    private List<Organization> allOrgs = new ArrayList<>();

    // filter integrated with search
    private List<Organization> filterOrgs = new ArrayList<>();
    private int filterPosition = 0;

    // ui views
    private EditText etSearch;
    private Button btnSearch;
    private Spinner spnCategory;
    private RecyclerView rvOrgs;

    public DiscoverFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // bind ui views
        etSearch = view.findViewById(R.id.discover_search_et);
        btnSearch = view.findViewById(R.id.discover_search_btn);
        spnCategory = view.findViewById(R.id.discover_category_filter_spinner);
        rvOrgs = view.findViewById(R.id.discover_orgs_rv);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recycler view adapter
        updateAdapter(allOrgs);

        // spinner adapter
        createCategoryAdapter();

        // when search button clicked
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchPhrase = etSearch.getText().toString();
                updateAdapter(search(searchPhrase));
            }
        });

        // populate data
        queryOrgs();
    }

    // search for specific organization
    private List<Organization> search(String phrase) {
        // checking if a filter is already selected
        List<Organization> searchFromOrgs = allOrgs;
        if (filterPosition != 0) {
            searchFromOrgs = filterOrgs;
        }

        // searching from proper list
        List<Organization> searchResults = new ArrayList<>();
        for (Organization org : searchFromOrgs) {
            if (org.getName().toLowerCase().contains(phrase)) {
                searchResults.add(org);
            }
        }
        return searchResults;
    }

    // update org adapter given list of organizations
    private void updateAdapter(List<Organization> orgsList) {
        adapter = new DiscoverOrgsAdapter(getContext(), orgsList); // (1) create adapter
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

                allOrgs.addAll(orgs);
                Collections.sort(allOrgs);
                adapter.notifyDataSetChanged();
            }
        });
    }

    // FILTER BY FEATURES
    // create spinner for category options
    private void createCategoryAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.categories_with_all, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new CategoryFilterSpinner());
    }

    // filter by functionality
    private void filterBy(String categoryName) {
        List<Organization> filterResults = new ArrayList<>();
        for (Organization org : allOrgs) {
            if (org.getCategory().toLowerCase().equals(categoryName)) {
                filterResults.add(org);
            }
        }
        filterOrgs = filterResults;
        updateAdapter(filterResults);
    }


    // filter spinner
    class CategoryFilterSpinner implements AdapterView.OnItemSelectedListener {

        private String[] categories = {
                "All",
                "Advocacy & Human Rights",
                "Animals",
                "Arts & Culture",
                "Children & Youth",
                "Community",
                "Crisis Support",
                "Disaster Relief",
                "Education & Literacy",
                "Emergency & Safety",
                "Faith-Based",
                "Health",
                "Homeless & Housing",
                "Hunger",
                "Immigrant & Refugees",
                "International",
                "Justice & Legal",
                "LGBTQ+",
                "Misc & Other",
                "People with Disabilities",
                "Politics",
                "Race & Ethnicity",
                "Seniors",
                "Special Needs",
                "Technology",
                "Women"};

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
            filterPosition = position;
            if (position == 0) {
                updateAdapter(allOrgs);
            } else {
                String categoryName = categories[position];
                filterBy(categoryName.toLowerCase());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }




}
