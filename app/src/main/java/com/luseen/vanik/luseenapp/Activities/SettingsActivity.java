package com.luseen.vanik.luseenapp.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Classes.Settings;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseBroadcastReceiver;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    Switch isInUseBackgroundProcesses;
    Button deleteAccountButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getIntent().getStringExtra(AppConstants.SETTINGS_ACTIVITY_KEY));

        sharedPreferences = getSharedPreferences(AppConstants.SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        isInUseBackgroundProcesses = (Switch) findViewById(R.id.is_in_use_background_processes);
        deleteAccountButton = (Button) findViewById(R.id.action_settings_delete_account);

        isInUseBackgroundProcesses.setChecked(sharedPreferences.getBoolean(AppConstants.SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES, true));
        Settings.setIsInUseBackgroundProcesses(sharedPreferences.getBoolean(AppConstants.SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES, true));
        isInUseBackgroundProcesses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Settings.setIsInUseBackgroundProcesses(isInUseBackgroundProcesses.isChecked());
                editor.putBoolean(AppConstants.SETTINGS_IS_IN_USE_BACKGROUND_PROCESSES,
                        isInUseBackgroundProcesses.isChecked());
                editor.apply();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

                final View dialogContainment = View.inflate(SettingsActivity.this, R.layout.account_delete_dialog, null);

                builder.setTitle(R.string.account_delete_dialog_title);
                builder.setView(dialogContainment);

                builder.setPositiveButton(R.string.delete_account, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText confirmPasswordField = (EditText) dialogContainment.findViewById(R.id.confirm_password_field);

                        if (confirmPasswordField.getText().toString().equals(LoggedUser.getPassword())) {

                            if (InternetConnection.hasInternetConnection(SettingsActivity.this)) {

                                ParseQuery<LuseenUsers> luseenUsersParseQuery = ParseQuery.getQuery(LuseenUsers.class);
                                luseenUsersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                                    @Override
                                    public void done(List<LuseenUsers> users, ParseException e) {

                                        if (e == null) {

                                            for (int j = 0; j < users.size(); j++) {

                                                if (users.get(j).getMail().equals(LoggedUser.getEmail())) {

                                                    try {
                                                        users.get(j).delete();
                                                        users.get(j).saveInBackground();
                                                    } catch (ParseException e1) {
                                                        e1.printStackTrace();
                                                    }

                                                    break;

                                                }

                                            }

                                        }

                                    }
                                });

                            }

                        }

                    }
                });

                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

            }
        });

    }

}
