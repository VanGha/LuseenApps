package com.luseen.vanik.luseenapp.Parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("LuseenNews")
public class LuseenNews extends ParseObject {

    public String getInformation() {
        return getString("Information");
    }

}
