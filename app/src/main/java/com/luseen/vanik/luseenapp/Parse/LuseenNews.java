package com.luseen.vanik.luseenapp.Parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("LuseenNews")
public class LuseenNews extends ParseObject {

    public String getInformation() {
        return getString("Information");
    }

}
