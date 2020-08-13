package com.example.charibee.data;

import android.content.Context;

import com.example.service.R;

public class RoleTheme {

    public static boolean isAdmin = true;

    RoleTheme() {
    }

    public static void setIsAdmin(Boolean bool) {
        isAdmin = bool;
    }

    public static void applyTheme(Context context) {
        if (isAdmin) {
            context.getTheme().applyStyle(R.style.AdminTheme, true);
        } else {
            context.getTheme().applyStyle(R.style.VolunteerTheme, true);
        }
    }
}
