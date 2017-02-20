package com.luseen.vanik.luseenapp.Classes;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.luseen.vanik.luseenapp.Activities.LogRegActivity;
import com.luseen.vanik.luseenapp.Activities.MainActivity;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.R;

public class LoggedUser {

    private static String email;
    private static String name;
    private static String surName;
    private static String rank;
    private static String speciality;

    private static PorterShapeImageView userPhoto;

    public static void setEmail(String email) {
        LoggedUser.email = email;
    }

    public static void setName(String name) {
        LoggedUser.name = name;
    }

    public static void setSurName(String surName) {
        LoggedUser.surName = surName;
    }

    public static void setRank(String rank) {
        LoggedUser.rank = rank;
    }

    public static void setSpeciality(String speciality) {
        LoggedUser.speciality = speciality;
    }

    public static String getEmail() {
        return email;
    }

    public static String getName() {
        return name;
    }

    public static String getSurName() {
        return surName;
    }

    public static String getRank() {
        return rank;
    }

    public static String getSpeciality() {
        return speciality;
    }

    public static void login(Context context, String userEmail) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.LOGGED_USER_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(AppConstants.IS_LOGGED, true);
        editor.putString(AppConstants.LOGGED_USER_EMAIL, userEmail);

        editor.apply();

        Intent gotoHomeActivity = new Intent(context, MainActivity.class);
        gotoHomeActivity.putExtra(AppConstants.LOGGED_USER_EMAIL,
                userEmail);
        context.startActivity(gotoHomeActivity);

    }

    public static void logout(final Context context, View snackBarView) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.LOGGED_USER_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(AppConstants.IS_LOGGED, false);

        editor.apply();

        Snackbar.make(snackBarView, R.string.question_want_to_leave, Snackbar.LENGTH_LONG).
                setAction(R.string.sign_out, new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, LogRegActivity.class));
                    }

                }).show();

    }

    public static boolean isLogged(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.LOGGED_USER_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(AppConstants.IS_LOGGED, false);
    }

    public static void setPhoto(PorterShapeImageView photo) {
        userPhoto = photo;
    }

    public static PorterShapeImageView getPhoto() {
        return userPhoto;
    }

}
