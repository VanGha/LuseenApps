package com.luseen.vanik.luseenapp.Parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("LuseenPost")
public class LuseenPosts extends ParseObject {

    public String getPosterEmail() {
        return getString("posterEmail");
    }

    public String getPosterName() {
        return getString("PosterName");
    }

    public String getPosterSurname() {
        return getString("PosterSurname");
    }

    public String getInformation() {
        return getString("PosterInformation");
    }

    public String getPostSpeciality() {
        return getString("PostSpeciality");
    }

    public boolean isExampleUsedWhenCreated() {
        return getBoolean("IsExampleUsedWhenCreated");
    }

}

