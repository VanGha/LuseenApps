package com.luseen.vanik.luseenapp.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.luseen.vanik.luseenapp.Classes.Settings;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.R;

public class SettingsActivity extends AppCompatActivity {

    Switch isInUseBackgroundProcesses;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getIntent().getStringExtra(AppConstants.SETTINGS_ACTIVITY_KEY));

        sharedPreferences = getSharedPreferences(AppConstants.SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        isInUseBackgroundProcesses = (Switch) findViewById(R.id.is_in_use_background_processes);

        isInUseBackgroundProcesses.setChecked(sharedPreferences.getBoolean(AppConstants.SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES, true));
        isInUseBackgroundProcesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInUseBackgroundProcesses.isChecked()) {
                    isInUseBackgroundProcesses.setChecked(false);
                } else {
                    isInUseBackgroundProcesses.setChecked(true);
                }

                Settings.setIsInUseBackgroundProcesses(isInUseBackgroundProcesses.isChecked());
                editor.putBoolean(AppConstants.SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES,
                        isInUseBackgroundProcesses.isChecked());
                editor.apply();
            }
        });

    }

}
