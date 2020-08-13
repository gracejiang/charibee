package com.example.charibee.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.adapters.InterestsSelectAdapter;
import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.List;

public class UpdateInterestsActivity extends AppCompatActivity {

    private User user;

    private RecyclerView rvInterests;
    private Button btnCancel;
    private Button btnSave;

    // interests adapter
    private InterestsSelectAdapter adapter;
    private String[] interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interests);

        // set current data
        user = new User(ParseUser.getCurrentUser());
        interests = getResources().getStringArray(R.array.categories);

        // bind ui views
        rvInterests = findViewById(R.id.update_interests_rv);
        btnCancel = findViewById(R.id.update_interests_cancel_btn);
        btnSave = findViewById(R.id.update_interests_save_btn);

        // admin
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            btnSave.setBackgroundColor(getResources().getColor(R.color.dark_red));
        }

        // update adapter
        updateAdapter(user.getInterests());

        // when cancel buton clicked
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goHome();
            }
        });

        // when save button clicked
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.updateInterests(adapter.getCheckedInterests());
                goHome();
                makeMessage("Your interests have been updated.");
            }
        });
    }

    // update adapter
    private void updateAdapter(List<Boolean> boolList) {
        adapter = new InterestsSelectAdapter(this, interests, boolList); // (1) create adapter
        rvInterests.setAdapter(adapter); // (2) set adapter on rv
        rvInterests.setLayoutManager(new GridLayoutManager(this, 2)); // (3) set layout manager on rv
        adapter.notifyDataSetChanged();
    }


    // return to profile fragment
    private void goHome() {
        finish();
    }

    // send message
    private void makeMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}