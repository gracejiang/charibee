package com.example.service.models;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class User {

    public static final String TAG = "User";
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
        String bio = user.getString(KEY_BIO);
        if (bio == null) {
            return "";
        }
        return bio;
    }

    public String getUsername() {
        try {
            return user.fetchIfNeeded().getString("username");
        } catch (ParseException e) {
            Log.e(TAG, "ParseError", e);
            return "error";
        }
    }

    public void setBio(String bio) {
        if (bio != null && bio.length() > 0) {
            user.put(KEY_BIO, bio);
        }
    }

    public List<Organization> getOrganizations() {
        List<Organization> organizations = new ArrayList<>();



        return organizations;
    }

    public void addOrg(Organization org) {
        List<Organization> orgsList = new ArrayList<>();
        orgsList.add(org);
        user.put("orgsJoined", orgsList);
    }

}
