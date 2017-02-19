package com.luseen.vanik.luseenapp.Parse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;


public class App extends Application {

    private static final String APPLICATION_ID = "sdfYna7P23ts425798fsadasdfhidqj25fsgmmNasdiadslgbjoif743y";
    private static final String SERVER_URL = "https://apps.luseen.co.uk:1341/api";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(LuseenUsers.class);
        ParseObject.registerSubclass(LuseenPosts.class);
        ParseObject.registerSubclass(LuseenNews.class);

        Parse.initialize(new Parse.Configuration.Builder(this).applicationId(APPLICATION_ID)
                .server(SERVER_URL).build());

    }
}
