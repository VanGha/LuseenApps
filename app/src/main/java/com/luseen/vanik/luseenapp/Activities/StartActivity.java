package com.luseen.vanik.luseenapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.Settings;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.R;
import com.luseen.vanik.luseenapp.Services.NotificationService;

import java.util.Timer;
import java.util.TimerTask;


public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (InternetConnection.hasInternetConnection(StartActivity.this)) {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent runNotificationsService = new Intent(StartActivity.this, NotificationService.class);

                    if (LoggedUser.isLogged(StartActivity.this)) {

                        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.LOGGED_USER_SHARED_PREFERENCE,
                                MODE_PRIVATE);
                        LoggedUser.login(StartActivity.this, sharedPreferences.getString(AppConstants.LOGGED_USER_EMAIL, ""));

                        if (Settings.isInUseBackgroundProcesses())
                            startService(runNotificationsService);

                    } else {

                        startActivity(new Intent(StartActivity.this, LogRegActivity.class));
                        if (Settings.isInUseBackgroundProcesses())
                            stopService(runNotificationsService);
                    }

                }
            }, 2000);

        } else {

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startActivity(new Intent(StartActivity.this, LogRegActivity.class));
                }
            }, 2000);
        }
    }

}
