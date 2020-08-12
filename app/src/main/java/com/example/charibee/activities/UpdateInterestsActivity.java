package com.example.charibee.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.charibee.models.User;
import com.example.service.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UpdateInterestsActivity extends AppCompatActivity {

    private User currUser;

    private RecyclerView rvInterests;
    private Button btnCancel;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_interests);

        currUser = new User(ParseUser.getCurrentUser());

        // bind ui views
        rvInterests = findViewById(R.id.update_interests_rv);
        btnCancel = findViewById(R.id.update_interests_cancel_btn);
        btnSave = findViewById(R.id.update_interests_save_btn);

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
                List<String> interests = new ArrayList<>();
                // add all interests
                currUser.updateInterests(interests);
                goHome();
            }
        });
    }


    // return to profile fragment
    private void goHome() {
        finish();
    }
}