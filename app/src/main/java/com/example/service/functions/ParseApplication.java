package com.example.service.functions;

import android.app.Application;

import com.example.service.models.Message;
import com.example.service.models.MessageRelation;
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
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(MessageRelation.class);

        // chat feature
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // configuration
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("grac-service") // should correspond to APP_ID env variable
                .clientKey("masterKeyMasterMeme101")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://grac-service.herokuapp.com/parse/").build());

    }
}