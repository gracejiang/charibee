package com.example.service.models;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class User {

    public static final String TAG = "User";
    ParseUser user;

    List<MessageRelation> msgRelations = new ArrayList<>();
    List<ParseUser> msgUserRelations = new ArrayList<>();

    public static final String KEY_FIRST_NAME = "firstName";
    public static final String KEY_LAST_NAME = "lastName";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PROFILE_PIC = "profilePic";
    public static final String KEY_BIO = "bio";
    public static final String KEY_NUM_POINTS = "numPoints";
    public static final String KEY_ORGS_IDS = "orgsJoinedIds";
    public static final String KEY_ORGS = "orgsJoined";
    public static final String KEY_MSGS_WITH = "messagesWith";

    // TODO: eventually abstract all the ParseUser.getXYZ() into User.getXYZ();

    public User() {}

    public User(ParseUser user) {
        this.user = user;
        loadAllMessages();
    }

    // get user id
    public String getId() {
        return user.getObjectId();
    }

    // get user's full name
    public String getName() {
        try {
            return user.fetchIfNeeded().getString(KEY_FIRST_NAME) + " " + user.fetchIfNeeded().getString(KEY_LAST_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "errorUser";
    }

    // get user's first name
    public String getFirstName() {
        try {
            return user.fetchIfNeeded().getString(KEY_FIRST_NAME);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "errorUser";
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

    // get user's profile pic
    public ParseFile getProfilePic() {
        try {
            return (ParseFile) user.fetchIfNeeded().get(KEY_PROFILE_PIC);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
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
        } else {
            user.remove(KEY_BIO);
        }
    }

    // get all ids of organizations
    public List<String> getOrganizationIds() {
        List<String> orgIds = new ArrayList<>();
        try {
            orgIds = (List<String>) user.fetchIfNeeded().get(KEY_ORGS_IDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return orgIds;
    }

    // get all organizations from a user
    public List<Organization> getOrganizations() {
        List<Organization> organizations = (List<Organization>) user.get(KEY_ORGS);
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

    // remove organization from user
    public void removeOrg(Organization org) {

        String orgId = org.getObjectId();
        List<String> orgIds = getOrganizationIds();

        // check if org exists in db already
        if (orgIds.contains(orgId)) {

            List<Organization> organizations = getOrganizations();

            for (int i = 0; i < orgIds.size(); i++) {
                if (orgIds.get(i).equals(orgId)) {
                    orgIds.remove(i);
                    organizations.remove(i);

                    user.put(KEY_ORGS_IDS, orgIds);
                    user.put(KEY_ORGS, organizations);
                    user.saveInBackground();

                    return;
                }
            }

        }

    }

    // if user is in org
    public boolean containsOrg(String orgId) {
        List<String> orgIds = getOrganizationIds();
        return (orgIds.contains(orgId));
    }

    // get parse user
    public ParseUser getParseUser() {
        return user;
    }

    // loads all messages
    public void loadAllMessages() {
        // query all relations where first user is the current user
        ParseQuery<MessageRelation> query = ParseQuery.getQuery(MessageRelation.class);
        query.whereEqualTo("user1", user);

        query.findInBackground(new FindCallback<MessageRelation>() {
            public void done(List<MessageRelation> queriedRelations, ParseException e) {
                if (e == null) {
                    msgRelations = queriedRelations;
                    for (MessageRelation msgRel : msgRelations) {
                        msgUserRelations.add(0, msgRel.getUser2());
                    }
                } else {
                    Log.d(TAG, "Error retrieving relations for user: " + e.getMessage());
                }
            }
        });
    }

    public List<MessageRelation> getMsgRelations() {
        return msgRelations;
    }

    public List<ParseUser> getAllMessagesWith() {
        return msgUserRelations;
    }




}
