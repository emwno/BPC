package com.aashir.bpc.ui.model;

public class EventCard {

    private String mObjectID;
    private String mTitle;
    private String mContent;
    private String mTime;
    private String mImageUrl;
    private String mVenue;
    private String mPhone;
    private String mAddress;
    private double mLatitude;
    private double mLongitude;

    // Object ID
    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String obj) {
        this.mObjectID = obj;
    }

    public String getTitle() {
        return mTitle;
    }

    // Title
    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    // Content
    public void setContent(String description) {
        this.mContent = description;
    }

    public String getTime() {
        return mTime;
    }

    // Date
    public void setTime(String time) {
        this.mTime = time;
    }

    public String getImage() {
        return mImageUrl;
    }

    // Image
    public void setImage(String urlImage) {
        this.mImageUrl = urlImage;
    }

    public String getVenue() {
        return mVenue;
    }

    // Venue
    public void setVenue(String ven) {
        this.mVenue = ven;
    }

    public String getPhone() {
        return mPhone;
    }

    // Phone
    public void setPhone(String pho) {
        this.mPhone = pho;
    }

    public String getAddress() {
        return mAddress;
    }

    // Address
    public void setAddress(String add) {
        this.mAddress = add;
    }

    public double getLatitude() {
        return mLatitude;
    }

    // Latitude
    public void setLatitude(double lat) {
        this.mLatitude = lat;
    }

    public double getLongitude() {
        return mLongitude;
    }

    // Longitude
    public void setLongitude(double lon) {
        this.mLongitude = lon;
    }

}
