package com.example.charibee.data;

import com.example.charibee.models.Organization;
import com.example.charibee.models.User;
import com.parse.ParseUser;

public class Data {

    public static Organization org = null;
    public static String address = "Click to Enter Address";
    public static double lat;
    public static double lng;
    public static User toUser;
    public static User currUser = new User(ParseUser.getCurrentUser());

    Data() {
    }

    public static void updateCurrUser() {
        currUser = new User(ParseUser.getCurrentUser());
    }

    public static User getCurrUser() {
        return currUser;
    }

    public static void setOrg(Organization organization) {
        org = organization;
    }

    public static Organization getOrg() {
        return org;
    }

    public static void setAddress(String newAddress) {
        address = newAddress;
    }

    public static void clearAddress() {
        address = "Click to Enter Address";
    }

    public static String getAddress() {
        return address;
    }

    public static void setLat(double latitude) {
        lat = latitude;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLng(double longitude) {
        lng = longitude;
    }

    public static double getLng() {
        return lng;
    }

    public static User getToUser() {
        return toUser;
    }

    public static void setToUser(User pUser) {
        toUser = pUser;
    }

}
