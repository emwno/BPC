package com.aashir.bpc.ui.model;

public class CommentCard {

    private String mAuthor;
    private String mContent;
    private String mPPUrl;
    private String mTime;

    // Author
    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String auth) {
        this.mAuthor = auth;
    }

    // Content
    public String getContent() {
        return mContent;
    }

    public void setContent(String description) {
        this.mContent = description;
    }

    public String getProfilePicture() {
        return mPPUrl;
    }

    // Profile Picture
    public void setProfilePicture(String urlImage) {
        this.mPPUrl = urlImage;
    }

    // Time
    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}