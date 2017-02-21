package com.luseen.vanik.luseenapp.Parse;


import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("LuseenPostComment")
public class LuseenPostComment extends ParseObject {

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
