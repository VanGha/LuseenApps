package com.luseen.vanik.luseenapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setTitle(getIntent().getStringExtra(AppConstants.SETTINGS_ACTIVITY_KEY));

    }

}
