package com.luseen.vanik.luseenapp.Activities;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Classes.Settings;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;


public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_USE_BACKGROUND_PROCESSES = "use_background_processes";
    public static final String KEY_DELETE_ACCOUNT = "delete_account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

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

        if (key.equals(KEY_USE_BACKGROUND_PROCESSES)) {

            Settings.setIsInUseBackgroundProcesses(SettingsActivity.this,
                    sharedPreferences.getBoolean(KEY_USE_BACKGROUND_PROCESSES, true));

        } else if (key.equals(KEY_DELETE_ACCOUNT)) {

            EditTextPreference deleteAccountEditText = (EditTextPreference) findPreference(key);

            if (deleteAccountEditText.getText().equals(LoggedUser.getPassword())) {

                LoggedUser.logoutWithoutSnackBar(SettingsActivity.this);

                ParseQuery<LuseenUsers> luseenUsersParseQuery = ParseQuery.getQuery(LuseenUsers.class);

                luseenUsersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                    @Override
                    public void done(List<LuseenUsers> users, ParseException e) {

                        if (e == null) {

                            for (int i = 0; i < users.size(); i++) {

                                if (users.get(i).getName().equals(LoggedUser.getName()) &&
                                        users.get(i).getMail().equals(LoggedUser.getEmail())) {

                                    try {
                                        users.get(i).delete();
                                        users.get(i).saveInBackground();

                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                    }

                                }

                            }

                        }

                    }
                });

            }

        }

    }
}
