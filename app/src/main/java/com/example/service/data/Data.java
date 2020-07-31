package com.example.service.data;

import com.example.service.models.Organization;

public class Data {

    public static Organization org = null;
    public static String address = null;
    public static float lat;
    public static float lng;

    Data() {

    }

    public static void setOrg(Organization organization) {
        org = organization;
    }

    public static Organization getOrg() {
        return org;
    }

    public static String getAddress() {
        return address;
    }

    public static float getLat() {
        return lat;
    }

    public static float getLng() {
        return lng;
    }

}
