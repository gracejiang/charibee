package com.example.service.models;

import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("TodoItem")
public class Organization extends ParseObject {

    String name;
    String description;
    String category;
    ParseUser organizer;
    // String location;
    // member ids
    // shifts

    Organization() {

    }

    public Organization(String name, String description, String category, ParseUser organizer) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.organizer = organizer;
    }
}
