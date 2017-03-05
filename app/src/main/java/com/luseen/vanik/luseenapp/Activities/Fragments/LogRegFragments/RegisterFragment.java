package com.luseen.vanik.luseenapp.Activities.Fragments.LogRegFragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.luseen.vanik.luseenapp.Activities.LogRegActivity;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class RegisterFragment extends Fragment {

    ImageView logo;
    LinearLayout dataWindow;

    ProgressBar loadingProgress;
    TextView windowTittle;
    EditText userNameField, userSurnameField, emailField, regPasswordField,
            regPasswordDuplicateField;
    Button doneButton;

    String[] registrationRank;
    String[] registrationSpeciality;

    Spinner spinnerSpeciality, spinnerRank;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logo = (ImageView) view.findViewById(R.id.image_logo);
        dataWindow = (LinearLayout) view.findViewById(R.id.data_window);
        windowTittle = (TextView) view.findViewById(R.id.window_tittle);
        loadingProgress = (ProgressBar) view.findViewById(R.id.register_load_progress);
        userNameField = (EditText) view.findViewById(R.id.register_username_field);
        userSurnameField = (EditText) view.findViewById(R.id.register_user_surname_field);
        emailField = (EditText) view.findViewById(R.id.register_mail_field);
        regPasswordField = (EditText) view.findViewById(R.id.register_password_field);
        regPasswordDuplicateField = (EditText) view.findViewById(R.id.register_password_duplicate_field);
        doneButton = (Button) view.findViewById(R.id.done_button);

        registrationRank = new String[]{getResources().getString(R.string.rank_field),
                getResources().getString(R.string.rank_developer),
                getResources().getString(R.string.rank_trainer),
                getResources().getString(R.string.rank_student)};

        registrationSpeciality = new String[]{getResources().getString(R.string.speciality_field),
                getResources().getString(R.string.speciality_web),
                getResources().getString(R.string.speciality_java),
                getResources().getString(R.string.speciality_android),
                getResources().getString(R.string.speciality_ios),
                getResources().getString(R.string.speciality_sql),
                getResources().getString(R.string.speciality_linux),
                getResources().getString(R.string.speciality_server)};

        spinnerSpeciality = (Spinner) view.findViewById(R.id.specialty);
        spinnerSpeciality.setAdapter(setupSpinnerAdapter(AppConstants.TYPE_SPECIALITY));
        spinnerSpeciality.setPrompt(getResources().getString(R.string.register_speciality_tittle));
        spinnerSpeciality.setSelection(0, true);

        spinnerRank = (Spinner) view.findViewById(R.id.rank);
        spinnerRank.setAdapter(setupSpinnerAdapter(AppConstants.TYPE_RANK));
        spinnerRank.setPrompt(getResources().getString(R.string.register_rank_tittle));
        spinnerRank.setSelection(0, true);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (InternetConnection.hasInternetConnection(getContext())) {

                    final String postRegisterEmailField = emailField.getText().toString();
                    final String postRegisterNameField = userNameField.getText().toString();
                    final String postRegisterSurnameField = userSurnameField.getText().toString();
                    final String postRegisterPasswordField = regPasswordField.getText().toString();
                    final String postRegisterDupPasswordField = regPasswordDuplicateField.getText().toString();
                    final String postRegisterSpecialitySpinnerData = spinnerSpeciality.getSelectedItem().toString();
                    final String postRegisterRankSpinnerData = spinnerRank.getSelectedItem().toString();

                    if (!postRegisterNameField.isEmpty() && !postRegisterEmailField.isEmpty() &&
                            !postRegisterPasswordField.isEmpty() && !postRegisterDupPasswordField.isEmpty() &&
                            !postRegisterSpecialitySpinnerData.equals(getResources().getString(R.string.speciality_field)) &&
                            !postRegisterRankSpinnerData.equals(getResources().getString(R.string.rank_field))) {

                        ParseQuery<LuseenUsers> luseenUsersParseQuery = ParseQuery.getQuery(LuseenUsers.class);
                        luseenUsersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                            @Override
                            public void done(List<LuseenUsers> users, ParseException e) {

                                if (e == null) {

                                    boolean hasDuplicateUserNameInServer = false;
                                    boolean hasDuplicateUserSurnameInServer = false;
                                    boolean hasDuplicateUserEmailInServer = false;

                                    for (int i = 0; i < users.size(); i++) {

                                        if (users.get(i).getName().equals(userNameField.getText().toString())) {
                                            hasDuplicateUserNameInServer = true;
                                            break;
                                        }

                                    }

                                    for (int i = 0; i < users.size(); i++) {

                                        if (users.get(i).getSurname().equals(userSurnameField.getText().toString())) {
                                            hasDuplicateUserSurnameInServer = true;
                                            break;
                                        }

                                    }

                                    for (int i = 0; i < users.size(); i++) {

                                        if (users.get(i).getMail().equals(emailField.getText().toString())) {
                                            hasDuplicateUserEmailInServer = true;
                                            break;
                                        }

                                    }

                                    if (!hasDuplicateUserEmailInServer) {

                                        if (postRegisterDupPasswordField.equals(postRegisterPasswordField)) {

                                            registerUser(postRegisterNameField, postRegisterSurnameField,
                                                    postRegisterEmailField, postRegisterPasswordField,
                                                    postRegisterSpecialitySpinnerData, postRegisterRankSpinnerData);

                                            clearFields();

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                            builder.setMessage(getResources().getString(R.string.successfully_registered));
                                            builder.setPositiveButton(getResources().getString(R.string.log_in),
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {

                                                            LoggedUser.login(getContext(), postRegisterEmailField);

                                                        }
                                                    });

                                            builder.create().show();

                                            LogRegActivity.scrollTabToPosition(0);

                                        } else {
                                            Toast.makeText(getContext(), R.string.password_fields_not_once,
                                                    Toast.LENGTH_SHORT).show();
                                            YoYo.with(Techniques.Shake).duration(1000).playOn(regPasswordDuplicateField);
                                        }

                                    } else {

                                        if (hasDuplicateUserNameInServer &&
                                                hasDuplicateUserSurnameInServer) {
                                            Toast.makeText(getContext(), R.string.user_duplicate,
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.user_mail_duplicate,
                                                    Toast.LENGTH_SHORT).show();
                                            YoYo.with(Techniques.Shake).duration(1000).playOn(emailField);
                                        }
                                    }
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), R.string.empty_fields, Toast.LENGTH_SHORT).show();
                        YoYo.with(Techniques.Shake).duration(1000).playOn(userNameField);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(userSurnameField);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(emailField);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(regPasswordField);
                        YoYo.with(Techniques.Shake).duration(1000).playOn(regPasswordDuplicateField);
                    }

                }

            }

        });

    }

    private void clearFields() {

        userNameField.setText("");
        userSurnameField.setText("");
        emailField.setText("");
        regPasswordField.setText("");
        regPasswordDuplicateField.setText("");

        spinnerRank.setSelection(0, true);
        spinnerSpeciality.setSelection(0, true);

    }

    private ArrayAdapter<String> setupSpinnerAdapter(String type) {

        ArrayAdapter<String> adapter;

        if (type.equals(AppConstants.TYPE_RANK)) {

            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, registrationRank);


        } else {

            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, registrationSpeciality);

        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    private void registerUser(String name, String surname, String email, String password, String specificity, String rank) {

        loadingProgress.setVisibility(View.VISIBLE);

        ParseObject user = ParseObject.create(LuseenUsers.class);
        user.put("name", name);
        user.put("surname", surname);
        user.put("mail", email);
        user.put("password", password);
        user.put("Specificity", specificity);
        user.put("Rank", rank);

        user.saveInBackground();

        loadingProgress.setVisibility(View.GONE);
    }

}
