package com.luseen.vanik.luseenapp.Classes;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.luseen.vanik.luseenapp.Activities.SettingsActivity;
import com.luseen.vanik.luseenapp.Services.NotificationService;

public class Settings {

    public static boolean isInUseBackgroundProcesses(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(SettingsActivity.KEY_USE_BACKGROUND_PROCESSES, true);
    }

    public static void setIsInUseBackgroundProcesses(Context context, boolean isInUseBackgroundProcesses) {

        Intent runNotificationsService = new Intent(context, NotificationService.class);

        if (isInUseBackgroundProcesses)
            context.startService(runNotificationsService);
        else
            context.stopService(runNotificationsService);

    }

    public boolean deleteAccount() {
        return false;
    }

}
