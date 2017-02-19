package com.luseen.vanik.luseenapp.Classes;


import android.content.Context;
import android.support.annotation.NonNull;

import com.luseen.vanik.luseenapp.R;


public class UserSpeciality {

    @NonNull
    public static String getAndroid(Context context) {
        return context.getResources().getString(R.string.speciality_android);
    }

    @NonNull
    public static String getIOS(Context context) {
        return context.getResources().getString(R.string.speciality_ios);
    }

    @NonNull
    public static String getWeb(Context context) {
        return context.getResources().getString(R.string.speciality_web);
    }

    @NonNull
    public static String getLinux(Context context) {
        return context.getResources().getString(R.string.speciality_linux);
    }

    @NonNull
    public static String getServer(Context context) {
        return context.getResources().getString(R.string.speciality_server);
    }

    @NonNull
    public static String getSQL(Context context) {
        return context.getResources().getString(R.string.speciality_sql);
    }

    @NonNull
    public static String getJava(Context context) {
        return context.getResources().getString(R.string.speciality_java);
    }

}
