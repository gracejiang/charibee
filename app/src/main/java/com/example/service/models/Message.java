package com.example.service.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String USER_ID_KEY = "userId";
    public static final String TO_USER_KEY = "toUser";
    public static final String FROM_USER_KEY = "fromUser";
    public static final String BODY_KEY = "body";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public ParseUser getToUser() {
        return (ParseUser) get(TO_USER_KEY);
    }

    public ParseUser getFromUser() {
        return (ParseUser) get(FROM_USER_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    public void setToUser(ParseUser user) {
        put(TO_USER_KEY, user);
    }

    public void setFromUser(ParseUser user) {
        put(FROM_USER_KEY, user);
    }

}