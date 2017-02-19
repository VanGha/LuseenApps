package com.luseen.vanik.luseenapp.Parse;

import com.parse.ParseClassName;
import com.parse.ParseObject;


@ParseClassName("LuseenUsers")
public class LuseenUsers extends ParseObject {

    public String getMail() {
        return getString("mail");
    }

    public String getName() {
        return getString("name");
    }

    public String getSurname() {
        return getString("surname");
    }

    public String getPassword() {
        return getString("password");
    }

    public String getSpeciality() {
        return getString("Specificity");
    }

    public String getRank() {
        return getString("Rank");
    }

}
