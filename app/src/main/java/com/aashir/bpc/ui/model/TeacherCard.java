package com.aashir.bpc.ui.model;

public class TeacherCard {

    private String mName;
    private String mPPUrl;
    private String mSubject;
    private String mPhone;
    private String mEmail;

    // Name
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    // Profile Picture
    public String getProfilePicture() {
        return mPPUrl;
    }

    public void setProfilePicture(String urlImage) {
        this.mPPUrl = urlImage;
    }

    // Subject
    public String getSubject() {
        return mSubject;
    }

    public void setSubject(String subj) {
        this.mSubject = subj;
    }

    // Phone
    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        this.mPhone = phone;
    }

    // Email
    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

}