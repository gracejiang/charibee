package com.example.service.models;

import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class User {

    ParseUser user;

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_BIO = "bio";
    public static final String KEY_NUM_POINTS = "numPoints";
    public static final String KEY_ORGS = "orgs";

    // TODO: eventually abstract all the ParseUser.getXYZ() into User.getXYZ();

    public User() {}

    public User(ParseUser user) {
        this.user = user;
    }

    public String getName() {
        return user.getString(KEY_FIRST_NAME) + " " + user.getString(KEY_LAST_NAME);
    }

    public String getBio() {
        return user.getString(KEY_BIO);
    }

    public void setBio(String bio) {
        user.put(KEY_BIO, bio);
    }

    public List<Organization> getOrgs() {
        List<Organization> orgs = new ArrayList<>();
        JSONArray orgsJsonArray = user.getJSONArray("orgs");


        return orgs;
    }

}
