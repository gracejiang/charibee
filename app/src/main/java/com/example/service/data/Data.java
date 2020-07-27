package com.example.service.data;

import com.example.service.models.Organization;

public class Data {

    private static Organization org = null;

    Data() {

    }

    public static void setOrg(Organization organization) {
        org = organization;
    }

    public static Organization getOrg() {
        return org;
    }
}
