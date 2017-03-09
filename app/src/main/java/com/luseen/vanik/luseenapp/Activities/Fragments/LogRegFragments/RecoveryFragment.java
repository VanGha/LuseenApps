package com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.luseen.vanik.luseenapp.Activities.Fragments.MainFragments.MainFragment;
import com.luseen.vanik.luseenapp.Activities.LogRegActivity;
import com.luseen.vanik.luseenapp.Activities.MainActivity;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class RecoveryFragment extends Fragment {

    EditText recoveryNameField, recoveryEmailField;
    Button doneButton;

    public static RecoveryFragment newInstance() {

        Bundle args = new Bundle();

        RecoveryFragment fragment = new RecoveryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RecoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recovery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recoveryNameField = (EditText) view.findViewById(R.id.recovery_username);
        recoveryEmailField = (EditText) view.findViewById(R.id.recovery_mail);
        doneButton = (Button) view.findViewById(R.id.done_button);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (InternetConnection.hasInternetConnection(getContext())) {

                    ParseQuery<LuseenUsers> usersParseQuery = ParseQuery.getQuery(LuseenUsers.class);

                    usersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                        @Override
                        public void done(List<LuseenUsers> users, ParseException e) {

                            if (e == null) {

                                boolean finder = false;

                                AlertDialog.Builder showPasswordDialogBuilder = new AlertDialog.
                                        Builder(getContext());
                                showPasswordDialogBuilder.setTitle(R.string.show_password);

                                for (int i = 0; i < users.size(); i++) {

                                    if (users.get(i).getMail().equals(recoveryEmailField.getText().toString()) &&
                                            users.get(i).getName().equals(recoveryNameField.getText().toString())) {

                                        finder = true;

                                        showPasswordDialogBuilder.setMessage("" + users.get(i).getPassword());

                                        showPasswordDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                LogRegActivity.scrollTabToPosition(0);

                                            }
                                        });

                                        showPasswordDialogBuilder.create().show();
                                        break;
                                    }

                                }

                                if (!finder) {

                                    Toast.makeText(getContext(), R.string.incorrect_data, Toast.LENGTH_SHORT).show();
                                    YoYo.with(Techniques.Shake).duration(1000).playOn(recoveryEmailField);
                                    YoYo.with(Techniques.Shake).duration(1000).playOn(recoveryNameField);

                                }

                            } else {

                                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();

                            }

                        }

                    });

                }
            }
        });

    }
}
