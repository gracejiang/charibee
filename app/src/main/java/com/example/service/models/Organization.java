package com.example.service.models;


import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


@ParseClassName("Organization")
public class Organization extends ParseObject {

    public static final String TAG = "Organization";

    public static final String KEY_NAME = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_VOLUNTEERS = "volunteers";
    public static final String KEY_VOLUNTEER_IDS = "volunteerIds";
    // location;
    // member ids
    // shifts

    public Organization() { }

    public String getName() {
        try {
            return this.fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            Log.e(TAG, "ParseError", e);
            return getString(KEY_NAME);
        }
    }

    public void setName(String name) {
        put(KEY_NAME, name);
    }

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getCategory() {
        return getString(KEY_CATEGORY);
    }

    public void setCategory(String category) {
        put(KEY_CATEGORY, category);
    }

    public ParseUser getOrganizer() {
        return getParseUser(KEY_ORGANIZER);
    }

    public void setOrganizer(ParseUser organizer) {
        put(KEY_ORGANIZER, organizer);
    }

    // get all ids of volunteers in org
    public List<String> getVolunteerIds() {
        List<String> volunteerIds = (List<String>) this.get(KEY_VOLUNTEER_IDS);
        return volunteerIds;
    }

    // get all volunteers in org
    public List<ParseUser> getVolunteers() {
        List<ParseUser> volunteers = (List<ParseUser>) this.get(KEY_VOLUNTEERS);
        // uncomment for debugging purposes
//        for (ParseUser parseUser : volunteers) {
//            User user = new User(parseUser);
//            Log.i(TAG, user.getUsername());
//        }
        return volunteers;
    }

    // add a volunteer to an organization
    public void addVolunteer(ParseUser volunteer) {

        String volunteerId = volunteer.getObjectId();
        List<String> volunteerIds = getVolunteerIds();

        // check if volunteer exists in db already
        if (!volunteerIds.contains(volunteerId)) {
            volunteerIds.add(volunteerId);
            put(KEY_VOLUNTEER_IDS, volunteerIds);

            List<ParseUser> volunteers = getVolunteers();
            volunteers.add(volunteer);
            put(KEY_VOLUNTEERS, volunteers);

            this.saveInBackground();
        }
    }
}
