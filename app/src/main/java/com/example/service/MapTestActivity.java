package com.example.service;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MapTestActivity extends AppCompatActivity {

    public static final String TAG = "MapActivity";
    public static final int ERROR_DIALOG_REQUEST = 9001;

    // ui elements
    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_test);

        if (isServicesOK()) {
            init();
        }
    }

    private void init() {
        btnMap = findViewById(R.id.map_btn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MapTestActivity.this, MapActivity.class);
                startActivity(i);
            }
        });
    }

    public boolean isServicesOK() {
        Log.i(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapTestActivity.this);
        if (available == ConnectionResult.SUCCESS) {
            Log.i(TAG, "isServicesOK: google play services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.i(TAG, "isServicesOk: an error occurred but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MapTestActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this,  "you cant make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}