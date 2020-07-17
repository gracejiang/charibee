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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference rootRef = database.getReference();
    private DatabaseReference userRef;

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

        // get current user
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = rootRef.child("users").child(currentUser.getUid());

        // find views
        tvName = view.findViewById(R.id.profile_name);
        btnLogout = view.findViewById(R.id.profile_logout_btn);

        final String name;

        // set user specific info
        userRef.child("firstName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                name = (String) snap.getValue();
                tvName.setText("Welcome back, " + snap.getValue() + "!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // logout button clicked
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
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