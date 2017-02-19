package com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class LoginFragment extends Fragment {

    ImageView logo;
    LinearLayout dataWindow;

    ProgressBar loadingProgress;
    TextView windowTittle, forgotPassword;
    EditText loginField, passwordField;
    Button doneButton;

    LayoutInflater inflater;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logo = (ImageView) view.findViewById(R.id.image_logo);
        dataWindow = (LinearLayout) view.findViewById(R.id.data_window);
        windowTittle = (TextView) view.findViewById(R.id.window_tittle);
        loadingProgress = (ProgressBar) view.findViewById(R.id.load_progress);
        loginField = (EditText) view.findViewById(R.id.login_mail_or_username_field);
        passwordField = (EditText) view.findViewById(R.id.login_password_field);
        doneButton = (Button) view.findViewById(R.id.done_button);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadingProgress.setVisibility(View.VISIBLE);

                if (InternetConnection.hasInternetConnection(getContext())) {

                    ParseQuery<LuseenUsers> usersParseQuery = ParseQuery.getQuery(LuseenUsers.class);

                    usersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                        @Override
                        public void done(List<LuseenUsers> users, ParseException e) {

                            if (e == null) {

                                loadingProgress.setVisibility(View.GONE);
                                loginField.setEnabled(true);
                                passwordField.setEnabled(true);
                                doneButton.setEnabled(true);

                                String tapedLogin = loginField.getText().toString();
                                String tapedPassword = passwordField.getText().toString();

                                if (!tapedLogin.isEmpty() && !tapedPassword.isEmpty() &&
                                        !tapedLogin.trim().isEmpty() && !tapedPassword.trim().isEmpty()) {

                                    boolean isLogged = false;

                                    for (int i = 0; i < users.size(); i++) {

                                        if (users.get(i).getMail().equalsIgnoreCase(tapedLogin) || users
                                                .get(i).getName().equals(tapedLogin) && users.get(i).
                                                getPassword().equals(tapedPassword)) {

                                            isLogged = true;
                                            LoggedUser.login(getContext(), users.get(i).getMail());

                                            break;
                                        }

                                    }

                                    if (!isLogged) {
                                        Toast.makeText(getContext(), R.string.incorrect_login_or_password, Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }


                        }

                    });

                    loginField.setEnabled(false);
                    passwordField.setEnabled(false);
                    doneButton.setEnabled(false);

                } else {

                    loadingProgress.setVisibility(View.GONE);

                }

            }
        });

    }

}
