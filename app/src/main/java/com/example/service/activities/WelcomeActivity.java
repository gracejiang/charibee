package com.example.service.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.service.R;
import com.example.service.location.MapTestActivity;
import com.parse.ParseUser;

/*
 *  First screen that users see
 *  Option to Login or Register
 *  Redirects to main page if user is already logged in
 */

public class WelcomeActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private Button btnMap;
    private ImageView ivIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // check if user is already logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            goMainActivity();
        }

        btnLogin = findViewById(R.id.welcome_login_btn);
        btnRegister = findViewById(R.id.welcome_register_btn);
        btnMap = findViewById(R.id.welcome_map_btn);
        ivIcon = findViewById(R.id.welcome_icon);

        // set icon
        Drawable iconDrawable = getResources().getDrawable(R.drawable.ic_app_icon);
        ivIcon.setImageDrawable(iconDrawable);

        // when login button clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginActivity();
            }
        });

        // when register button clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterActivity();
            }
        });

        // when map button clicked
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goMapActivity();
            }
        });

    }

    // go map activity
    private void goMapActivity() {
        Intent i = new Intent(this, MapTestActivity.class);
        startActivity(i);
    }

    // goes to main activity
    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    // goes to login activity
    private void goToLoginActivity() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    // goes to register activity
    private void goToRegisterActivity() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

}