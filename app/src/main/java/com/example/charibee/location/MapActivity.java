package com.example.charibee.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.service.R;
import com.example.charibee.data.Data;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnConnectionFailedListener {

    public static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    // ui
    private AutoCompleteTextView etSearchText;
    private ImageView ivCurrLocation;
    private Button btnSetLocation;

    // vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;

    // save results
    private String setAddress = "";
    private double lat;
    private double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // set up places API client
        String apiKey = "AIzaSyAscLrhAje_CS0O8nJt-0IzlQDkB5WhxJs";
        Places.initialize(getApplicationContext(), apiKey);
        PlacesClient placesClient = Places.createClient(this);

        // bind ui views
        etSearchText = findViewById(R.id.map_et_search);
        ivCurrLocation = findViewById(R.id.map_ic_current_loc);
        btnSetLocation = findViewById(R.id.map_set_location_btn);

        getLocationPermission();
        if (mLocationPermissionsGranted) {
            initMap();
            getDeviceLocation();
            init();

            btnSetLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveResults();
                }
            });
        }
    }

    // saves results and returns to previous activity
    private void saveResults() {
        Data.setAddress(setAddress);
        Data.setLat(lat);
        Data.setLng(lng);
        finish();
    }

    // views to be initialized when map permissions approved
    private void init() {
        setSearchTextListener();
        setGPSLocatorListener();
    }


    // listens for search text and geolocates when enter pressed
    private void setSearchTextListener() {
        Log.d(TAG, "init: initializing");

        etSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                // if enter button pressed
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {

                    // execute method for searching
                    geoLocate(etSearchText.getText().toString());
                }
                return false;
            }
        });
    }

    // when gps button is clicked
    private void setGPSLocatorListener() {
        ivCurrLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
    }

    // finds location of string and pans camera to location
    private void geoLocate(String locationString) {
        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();

        try {
            list = geocoder.getFromLocationName(locationString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            setAddress = address.getAddressLine(0);
            lat = address.getLatitude();
            lng = address.getLongitude();
            moveCamera(new LatLng(lat, lng), DEFAULT_ZOOM, setAddress);
        }
    }

    // initialize map fragment
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
            }
        });
    }

    // get devices location
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Log.i(TAG, "onComplete: found location");
                            Location currLocation = (Location) task.getResult();
                            LatLng currLocationLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());

                            // display current location
                            moveCamera(currLocationLatLng, DEFAULT_ZOOM, "My Location");

                            // checks permissions (required for setMyLocationEnabled function)
                            if (ActivityCompat.checkSelfPermission(MapActivity.this, FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapActivity.this, COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }

                            mMap.setMyLocationEnabled(true);

                        } else {
                            Log.i(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException" + e.getMessage());
        }
    }

    // move camera to lat/long
    private void moveCamera(LatLng latLng, float zoom, String title) {
        // map is null, cannot move camera at time
        if (mMap == null) {
            Log.i(TAG, "mMap null, async error");
            return;
        }

        // move the camera to correct position
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        // drop a pin at the location
        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
    }

    // if all the permissions are granted
    private void getLocationPermission() {
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)  == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionsGranted = true;
        } else {
            // will invoke onRequestPermissionsResult
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    // set mLocationPermissionsGranted to correct value
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                }
            }
        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}