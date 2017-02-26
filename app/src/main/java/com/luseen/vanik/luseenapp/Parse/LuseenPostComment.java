package com.luseen.vanik.luseenapp.Parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("LuseenPostComment")
public class LuseenPostComment extends ParseObject {

    private boolean isLoaded;

    public boolean isAdded() {
        return isLoaded;
    }

    public void setAdded(boolean loaded) {
        isLoaded = loaded;
    }

    public String getSenderName() {
        return getString("SenderName");
    }

    public String getSenderSurname() {
        return getString("SenderSurname");
    }

    public String getComment() {
        return getString("Comment");
    }

    public String getPostId() {
        return getString("PostId");
    }

}
