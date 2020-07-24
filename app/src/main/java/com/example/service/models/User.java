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

    public static final String KEY_ORGS_IDS = "orgsJoinedIds";
    public static final String KEY_ORGS = "orgsJoined";


    // TODO: eventually abstract all the ParseUser.getXYZ() into User.getXYZ();

    public User(ParseUser user) {
        this.user = user;
    }

    // get user's full name
    public String getName() {
        return user.getString(KEY_FIRST_NAME) + " " + user.getString(KEY_LAST_NAME);
    }

    // get user's username
    public String getUsername() {
        try {
            return user.fetchIfNeeded().getString(KEY_USERNAME);
        } catch (ParseException e) {
            Log.e(TAG, "ParseError", e);
            return "error";
        }
    }

    // get user's bio
    public String getBio() {
        String bio = user.getString(KEY_BIO);
        if (bio == null) {
            return "";
        }
        return bio;
    }

    // set user's bio
    public void setBio(String bio) {
        if (bio != null && bio.length() > 0) {
            user.put(KEY_BIO, bio);
        }
    }

    // get all ids of organizations
    public List<String> getOrganizationIds() {
        List<String> orgIds = (List<String>) user.get(KEY_ORGS_IDS);
        return orgIds;
    }

    // get all organizations from a user
    public List<Organization> getOrganizations() {
        List<Organization> organizations = (List<Organization>) user.get(KEY_ORGS);
        // uncomment for debugging purposes
//        for (Organization org : organizations) {
//            Log.i(TAG, org.getName());
//        }
        return organizations;
    }

    // add an organization to a user
    public void addOrg(Organization org) {

        String orgId = org.getObjectId();
        List<String> orgIds = getOrganizationIds();

        // check if org exists in db already
        if (!orgIds.contains(orgId)) {
            orgIds.add(orgId);
            user.put(KEY_ORGS_IDS, orgIds);

            List<Organization> organizations = getOrganizations();
            organizations.add(org);
            user.put(KEY_ORGS, organizations);

            user.saveInBackground();
        }
    }

}
