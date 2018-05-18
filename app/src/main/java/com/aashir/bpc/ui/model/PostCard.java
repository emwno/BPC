package com.aashir.bpc.ui.model;

public class PostCard {

    private String mObjectID;
    private String mAuthor;
    private String mContent;
    private String mImageThumbUrl;
    private String mImageID;
    private String mPPUrl;
    private String mTime;
    private boolean mHasImage = false;
    private boolean mLike = false;

    // Object ID
    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String obj) {
        this.mObjectID = obj;
    }

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

    public String getImageThumb() {
        return mImageThumbUrl;
    }

    // Image Thumbnail
    public void setImageThumb(String urlImage) {
        this.mImageThumbUrl = urlImage;
    }

    public String getImageID() {
        return mImageID;
    }

    // Image
    public void setImageID(String id) {
        this.mImageID = id;
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

    public boolean getHasImage() {
        return mHasImage;
    }

    // Has Image
    public void setHasImage(boolean doesit) {
        this.mHasImage = doesit;
    }

    public boolean getLike() {
        return mLike;
    }

    // Like
    public void setLike(boolean isit) {
        this.mLike = isit;
    }

}