package com.example.charibee.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.charibee.data.RoleTheme;
import com.example.charibee.fragments.CommunityFragment;
import com.example.charibee.fragments.DiscoverFragment;
import com.example.charibee.fragments.HomeFragment;
import com.example.charibee.fragments.MessagesFragment;
import com.example.charibee.fragments.ProfileFragment;
import com.example.charibee.models.User;
import com.example.service.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView tvToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set theme based off if current user is admin or not
        User user = new User(ParseUser.getCurrentUser());
        RoleTheme.setIsAdmin(user.getRole().equals("Organizer"));
        RoleTheme.applyTheme(this);

        setContentView(R.layout.activity_main);

        // bind views
        toolbar = findViewById(R.id.toolbar);
        nvDrawer = findViewById(R.id.nvView);
        mDrawer = findViewById(R.id.drawer_layout);
        tvToolbarTitle = findViewById(R.id.toolbar_title);

        // top nav bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");

        // side nav bar
        setupDrawerContent(nvDrawer);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        mDrawer.addDrawerListener(drawerToggle);
        View headerView = nvDrawer.getHeaderView(0);
        TextView tvSidebarWelcomeMsg = (TextView) headerView.findViewById(R.id.sidebar_header_tv);
        tvSidebarWelcomeMsg.setText("Welcome back, " + user.getFirstName() + "!");

        // fragment manager
        fragmentManager = getSupportFragmentManager();

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragmentView(item.getItemId());
                tvToolbarTitle.setText(item.getTitle());
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

    // listener for when side/top toolbar is clicked
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    // when sidebar menuItem is clicked
    public void selectDrawerItem(MenuItem menuItem) {
        mDrawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.sidebar_profile_fragment:
                fragmentManager.beginTransaction().replace(R.id.main_frame_layout, new ProfileFragment()).commit();
                tvToolbarTitle.setText(menuItem.getTitle());
                break;
            case R.id.sidebar_settings_fragment:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            case R.id.sidebar_logout_functionality:
                ParseUser.logOut();
                goWelcomeActivity();
                break;
            default:
                break;
        }
    }


    // items selected for sidebar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // sets view for admin account
    private void setAdminView() {
        bottomNavigationView.getMenu().removeItem(R.id.menu_discover);
    }

    // updates fragment view
    public void setFragmentView(int fragmentId) {
        Fragment fragment;
        switch (fragmentId) {
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_discover:
                fragment = new DiscoverFragment();
                break;
            case R.id.menu_messages:
                fragment = new MessagesFragment();
                break;
            case R.id.menu_community:
                fragment = new CommunityFragment();
                break;
            default:
                Log.e(TAG, "default case should not be hit");
                fragment = new HomeFragment();
        }
        fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
    }

    // go to welcome activity
    private void goWelcomeActivity() {
        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(i);
        this.finish();
    }

}