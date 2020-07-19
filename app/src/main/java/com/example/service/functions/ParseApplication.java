package com.example.service.functions;

import android.app.Application;

import com.example.service.models.Organization;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

// setup here:
// https://guides.codepath.org/android/Building-Data-driven-Apps-with-Parse#setup-network-permissions

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // register models
        ParseObject.registerSubclass(Organization.class);

        // configuration
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("grac-service") // should correspond to APP_ID env variable
                .clientKey("masterKeyMasterMeme101")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://grac-service.herokuapp.com/parse/").build());

    }
}