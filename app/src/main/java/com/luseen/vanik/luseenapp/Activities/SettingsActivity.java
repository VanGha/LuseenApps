package com.luseen.vanik.luseenapp.Activities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.luseen.vanik.luseenapp.Classes.Settings;
import com.luseen.vanik.luseenapp.R;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_USE_BACKGROUND_PROCESSES = "use_background_processes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        addPreferencesFromResource(R.xml.preferences);

    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Toast.makeText(this, "" + sharedPreferences.getBoolean(KEY_USE_BACKGROUND_PROCESSES, true), Toast.LENGTH_SHORT).show();

        if (key.equals(KEY_USE_BACKGROUND_PROCESSES)) {

            Settings.setIsInUseBackgroundProcesses(SettingsActivity.this,
                    sharedPreferences.getBoolean(KEY_USE_BACKGROUND_PROCESSES, true));

        }

    }
}
