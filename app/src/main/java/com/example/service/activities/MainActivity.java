package com.example.service.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.service.R;
import com.example.service.fragments.DiscoverFragment;
import com.example.service.fragments.HomeFragment;
import com.example.service.fragments.MessagesFragment;
import com.example.service.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment = new HomeFragment();
    DiscoverFragment discoverFragment = new DiscoverFragment();
    MessagesFragment messagesFragment = new MessagesFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // fragment manager
        fragmentManager = getSupportFragmentManager();

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragmentView(item.getItemId());
                return true;
            }
        });

        // default bottom nav selection
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        // remove discover view if admin
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            setAdminView();
        }
    }

    private void setAdminView() {
        bottomNavigationView.getMenu().removeItem(R.id.menu_discover);
    }

    public void setFragmentView(int fragmentId) {
        Fragment fragment;
        switch (fragmentId) {
            case R.id.menu_home:
                fragment = homeFragment;
                break;
            case R.id.menu_discover:
                fragment = discoverFragment;
                break;
            case R.id.menu_messages:
                fragment = messagesFragment;
                break;
            case R.id.menu_profile:
                fragment = profileFragment;
                break;
            default:
                Log.e(TAG, "default case should not be hit");
                fragment = homeFragment;
        }

        fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
    }

}