package com.example.service.models;


import android.util.Log;

import com.example.service.data.Data;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;


@ParseClassName("Organization")
public class Organization extends ParseObject implements Comparable {

    public static final String TAG = "Organization";

    public static final String KEY_NAME = "name";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_TAGLINE = "tagline";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ORGANIZER = "organizer";
    public static final String KEY_VOLUNTEERS = "volunteers";
    public static final String KEY_VOLUNTEER_IDS = "volunteerIds";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_ADDRESS_POINTS = "addressPoints";
    public static final String KEY_WEBSITE = "website";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE_NUMBER = "phoneNumber";
    // location;
    // member ids
    // shifts

    public Organization() { }

    public String getName() {
        try {
            return this.fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            Log.e(TAG, "ParseError", e);
            return "";
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
        try {
            return fetchIfNeeded().getString(KEY_CATEGORY);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
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

    public void setTagline(String tagline) {
        put(KEY_TAGLINE, tagline);
    }

    public String getTagline() {
        try {
            return fetchIfNeeded().getString(KEY_TAGLINE);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setAddress(String address) {
        if (address != null && address.length() > 0 && !address.equals("Click to Enter Address")) {
            put(KEY_ADDRESS, address);
            put(KEY_ADDRESS_POINTS, new ParseGeoPoint(Data.getLat(), Data.getLng()));
        }
    }

    public String getAddress() {
        return getString(KEY_ADDRESS);
    }

    public void setWebsite(String website) {
        if (website != null && website.length() > 0) {
            put(KEY_WEBSITE, website);
        }
    }

    public String getWebsite() {
        return getString(KEY_WEBSITE);
    }

    public void setEmail(String email) {
        if (email != null && email.length() > 0) {
            put(KEY_EMAIL, email);
        }
    }

    public String getEmail() {
        return getString(KEY_EMAIL);
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() > 0) {
            put(KEY_PHONE_NUMBER, phoneNumber);
        }
    }

    public String getPhoneNumber() {
        return getString(KEY_PHONE_NUMBER);
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

    // remove volunteer from organization
    public void removeVolunteer(ParseUser volunteer) {
        String volunteerId = volunteer.getObjectId();
        List<String> volunteerIds = getVolunteerIds();

        // check if volunteer exists in db already
        if (volunteerIds.contains(volunteerId)) {

            List<ParseUser> volunteers = getVolunteers();

            for (int i = 0; i < volunteerIds.size(); i++) {
                if (volunteerIds.get(i).equals(volunteerId)) {
                    volunteerIds.remove(i);
                    volunteers.remove(i);

                    put(KEY_VOLUNTEER_IDS, volunteerIds);
                    put(KEY_VOLUNTEERS, volunteers);

                    this.saveInBackground();

                    return;
                }
            }

        }
    }

    @Override
    public int compareTo(Object o) {
        return this.getName().compareTo(((Organization) o).getName());
    }
}
