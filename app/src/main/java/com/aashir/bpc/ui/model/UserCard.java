package com.aashir.bpc.ui.model;

public class UserCard {

    private String mName;
    private String mPPUrl;

    // Author
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getProfilePicture() {
        return mPPUrl;
    }

    // Profile Picture
    public void setProfilePicture(String urlImage) {
        this.mPPUrl = urlImage;
    }

}