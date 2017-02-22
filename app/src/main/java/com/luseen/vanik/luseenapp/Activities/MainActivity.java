package com.luseen.vanik.luseenapp.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.luseen.vanik.luseenapp.Activities.Fragments.MainFragments.MainFragment;
import com.luseen.vanik.luseenapp.Interfaces.AppConstants;
import com.luseen.vanik.luseenapp.Classes.InternetConnection;
import com.luseen.vanik.luseenapp.Classes.LoggedUser;
import com.luseen.vanik.luseenapp.Classes.UserSpeciality;
import com.luseen.vanik.luseenapp.Parse.LuseenPosts;
import com.luseen.vanik.luseenapp.Parse.LuseenUsers;
import com.luseen.vanik.luseenapp.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;

    String loggedUserEmail;
    String currentMenu;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationViewHeaderView = navigationView.getHeaderView(0);
        final TextView headerUserName = (TextView) navigationViewHeaderView.findViewById(R.id.header_name_field);
        final TextView headerUserSurname = (TextView) navigationViewHeaderView.findViewById(R.id.header_surname_field);

        loggedUserEmail = getIntent().getStringExtra(AppConstants.LOGGED_USER_EMAIL);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!drawer.isDrawerOpen(GravityCompat.START)) {
                    YoYo.with(Techniques.FlipInX).duration(1000).playOn(headerUserName);
                    YoYo.with(Techniques.FlipInX).duration(2000).playOn(headerUserSurname);
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        if (InternetConnection.hasInternetConnection(this)) {

            ParseQuery<LuseenUsers> usersParseQuery = ParseQuery.getQuery(LuseenUsers.class);
            usersParseQuery.findInBackground(new FindCallback<LuseenUsers>() {
                @Override
                public void done(List<LuseenUsers> users, ParseException e) {

                    if (e == null) {

                        for (int i = 0; i < users.size(); i++) {

                            if (users.get(i).getMail().equals(loggedUserEmail)) {

                                LoggedUser.setEmail(users.get(i).getMail());
                                LoggedUser.setName(users.get(i).getName());
                                LoggedUser.setSurName(users.get(i).getSurname());
                                LoggedUser.setRank(users.get(i).getRank());
                                LoggedUser.setSpeciality(users.get(i).getSpeciality());

                                break;
                            }
                        }

                        String currentSpeciality = LoggedUser.getSpeciality();

                        if (currentSpeciality.equals(UserSpeciality.getAndroid(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.android);
                            headerUserName.setTextColor(Color.BLACK);
                            headerUserSurname.setTextColor(Color.BLACK);
                        } else if (currentSpeciality.equals(UserSpeciality.getIOS(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.swift);
                            headerUserName.setTextColor(Color.BLACK);
                            headerUserSurname.setTextColor(Color.BLACK);
                        } else if (currentSpeciality.equals(UserSpeciality.getJava(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.java);
                        } else if (currentSpeciality.equals(UserSpeciality.getServer(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.server);
                        } else if (currentSpeciality.equals(UserSpeciality.getLinux(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.linux);
                            headerUserName.setTextColor(Color.BLACK);
                            headerUserSurname.setTextColor(Color.BLACK);
                        } else if (currentSpeciality.equals(UserSpeciality.getSQL(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.sql);
                            headerUserName.setTextColor(Color.BLACK);
                            headerUserSurname.setTextColor(Color.BLACK);
                        } else if (currentSpeciality.equals(UserSpeciality.getWeb(MainActivity.this))) {
                            navigationViewHeaderView.setBackgroundResource(R.drawable.web);
                        }

                        headerUserName.setText(LoggedUser.getName());
                        headerUserSurname.setText(LoggedUser.getSurName());

                    }
                }
            });

        }

        PorterShapeImageView headerUserImage = (PorterShapeImageView) navigationViewHeaderView.findViewById(R.id.user_photo);

        Glide.with(this)
                .load("http://inetklub.ru/avatarki/Muzhichek_v_protivogazike.jpg")
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .centerCrop()
                .into(headerUserImage);
        LoggedUser.setPhoto(headerUserImage);

        setupMenuNews();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            LoggedUser.logout(this, drawer);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;

        if (currentMenu.equals(AppConstants.TAG_MENU_NEWS) ||
                LoggedUser.getRank().equals(getResources().getString(R.string.rank_student))) {
            menu.setGroupVisible(0, false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_create_post) {

            ArrayList<String> dateStringArray = new ArrayList<>();
            ArrayList<String> timeStringArray = new ArrayList<>();

            dateStringArray.add("");
            timeStringArray.add("");

            dateStringArray.add("Сегодня");
            dateStringArray.add("Завтра");
            dateStringArray.add("Послезавтра");

            for (int i = 0; i <= 23; i++) {
                timeStringArray.add(String.valueOf(i + ":" + "00"));
                timeStringArray.add(String.valueOf(i + ":" + "30"));
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            final View postAddDialog = View.inflate(MainActivity.this, R.layout.post_add_dialog, null);
            builder.setView(postAddDialog);

            final EditText addPostInformation = (EditText) postAddDialog.findViewById(R.id.add_post_information);
            final Spinner dateSpinner = (Spinner) postAddDialog.findViewById(R.id.date_spinner);
            final Spinner timeSpinner = (Spinner) postAddDialog.findViewById(R.id.time_spinner);
            final Switch useExamples = (Switch) postAddDialog.findViewById(R.id.use_example_switch);

            ArrayAdapter<String> stringArrayAdapterDate = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_spinner_item, dateStringArray);
            stringArrayAdapterDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dateSpinner.setAdapter(stringArrayAdapterDate);

            ArrayAdapter<String> stringArrayAdapterTime = new ArrayAdapter<>(MainActivity.this,
                    android.R.layout.simple_spinner_item, timeStringArray);
            stringArrayAdapterTime.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeSpinner.setAdapter(stringArrayAdapterTime);

            builder.setPositiveButton(getResources().getString(R.string.create), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    addPostToServer(LoggedUser.getSpeciality(), LoggedUser.getName(), LoggedUser.getSurName(),
                            addPostInformation.getText().toString(), loggedUserEmail);
                    updatePublications();

                }
            });

            builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            builder.create().show();

            dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    addPostInformation.setText(String.valueOf(addPostInformation.getText().toString()) +
                            " " + adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    addPostInformation.setText(String.valueOf(addPostInformation.getText().toString()) +
                            " " + adapterView.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            useExamples.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    dateSpinner.setSelection(0);
                    timeSpinner.setSelection(0);

                    if (useExamples.isChecked())
                        addPostInformation.setText("Example!"); // TODO: 15-Feb-17 MAKE A EXAMPLE OF POST
                    else addPostInformation.setText("");
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addPostToServer(String postSpeciality, String posterName, String posterSurname,
                                 String information, String posterEmail) {

        if (InternetConnection.hasInternetConnection(MainActivity.this)) {

            ParseObject post = ParseObject.create(LuseenPosts.class);
            post.put("PostSpeciality", postSpeciality);
            post.put("PosterName", posterName);
            post.put("PosterSurname", posterSurname);
            post.put("PosterInformation", information);
            post.put("posterEmail", posterEmail);
            post.put("HasComments", false);

            post.saveInBackground();

        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_news) {
            setupMenuNews();
            menu.setGroupVisible(0, false);
        } else if (id == R.id.menu_publications) {
            setupMenuPublications();
            menu.setGroupVisible(0, true);
        } else if (id == R.id.menu_application_settings) {

            Intent toSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
            toSettingsActivity.putExtra(AppConstants.SETTINGS_ACTIVITY_KEY, item.getTitle().toString());

            startActivity(toSettingsActivity);

        } else if (id == R.id.menu_my_posts) {
            setupMenuMyPublications();
            menu.setGroupVisible(0, false);
        } else if (id == R.id.menu_acc_log_out) {
            LoggedUser.logout(MainActivity.this, navigationView);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupMenuNews() {

        currentMenu = AppConstants.TAG_MENU_NEWS;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(AppConstants.TAG_MENU_NEWS) == null) {

            fragmentManager.beginTransaction().replace(R.id.content_main,
                    MainFragment.newInstance(AppConstants.TAG_MENU_NEWS, null), AppConstants.TAG_MENU_NEWS).commit();
            setTitle(R.string.menu_news);

        }

    }

    private void setupMenuPublications() {

        currentMenu = AppConstants.TAG_MENU_PUBLICATIONS;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(AppConstants.TAG_MENU_PUBLICATIONS) == null) {

            fragmentManager.beginTransaction().replace(R.id.content_main,
                    MainFragment.newInstance(AppConstants.TAG_MENU_PUBLICATIONS, null),
                    AppConstants.TAG_MENU_PUBLICATIONS).commit();
            setTitle(R.string.menu_publications);

        }

    }

    private void setupMenuMyPublications() {

        currentMenu = AppConstants.TAG_MENU_MY_PUBLICATIONS;
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentByTag(AppConstants.TAG_MENU_MY_PUBLICATIONS) == null) {

            fragmentManager.beginTransaction().replace(R.id.content_main,
                    MainFragment.newInstance(AppConstants.TAG_MENU_MY_PUBLICATIONS, loggedUserEmail),
                    AppConstants.TAG_MENU_MY_PUBLICATIONS).commit();
            setTitle(R.string.menu_my_publications);

        }

    }

    private void updatePublications() {

        if (InternetConnection.hasInternetConnection(MainActivity.this)) {

            ParseQuery<LuseenPosts> luseenPostsParseQuery = ParseQuery.getQuery(LuseenPosts.class);
            luseenPostsParseQuery.findInBackground(new FindCallback<LuseenPosts>() {
                @Override
                public void done(List<LuseenPosts> posts, ParseException e) {
                    new MainFragment().updateRecycler(posts);
                }
            });
        }
    }

}
