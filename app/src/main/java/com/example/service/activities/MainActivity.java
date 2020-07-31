package com.example.service.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.service.R;
import com.example.service.fragments.DiscoverFragment;
import com.example.service.fragments.HomeFragment;
import com.example.service.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

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
    }

    public void setFragmentView(int fragmentId) {
        Fragment fragment;
        switch (fragmentId) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_discover:
                fragment = new DiscoverFragment();
                break;
            case R.id.menu_profile:
                fragment = new ProfileFragment();
                break;
            default:
                Log.e(TAG, "default case should not be hit");
                fragment = new HomeFragment();
        }

        fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
    }
}