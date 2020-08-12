package com.example.charibee.models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

@ParseClassName("MsgRelation")
public class MessageRelation extends ParseObject {

    public static final String TAG = "MessageRelations";

    public static final String USER_1_KEY = "user1";
    public static final String USER_2_KEY = "user2";

    public ParseUser getUser1() {
        return (ParseUser) get(USER_1_KEY);
    }

    public ParseUser getUser2() {
        return (ParseUser) get(USER_2_KEY);
    }

    public void setUser1(ParseUser user) {
        put(USER_1_KEY, user);
    }

    public void setUser2(ParseUser user) {
        put(USER_2_KEY, user);
    }

    // add a msg relation between 2 users
    public static void addMessageRelation(User user1, User user2) {
        // checks that no relation between 2 users exists already
        if (!relationExists(user1, user2)) {
            ParseUser pUser1 = user1.getParseUser();
            ParseUser pUser2 = user2.getParseUser();

            MessageRelation relation1 = new MessageRelation();
            relation1.setUser1(pUser1);
            relation1.setUser2(pUser2);

            relation1.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) { Log.e(TAG, "Failed to save message", e); }
                }
            });

            MessageRelation relation2 = new MessageRelation();
            relation2.setUser1(pUser2);
            relation2.setUser2(pUser1);

            relation2.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) { Log.e(TAG, "Failed to save message", e); }
                }
            });

            Log.v("chatRshipBug", "rship saved");
        }
    }

    // checks if msg relation between 2 users already exists
    public static boolean relationExists(User user1, User user2) {
        Log.v("chatRshipBug", "got to this function");
        for (MessageRelation msgRel : user1.getMsgRelations()) {
            if (msgRel.getUser2().getObjectId().equals(user2.getParseUser().getObjectId())) {
                Log.v("chatRshipBug", "rship exists!");
                return true;
            }
        }
        Log.v("chatRshipBug", "ended function with a false");
        return false;
    }
}
