package com.aashir.bpc.ui.model;

public class AssignmentCard {

    private String mObjectID;
    private String mSubject;
    private String mTitle;
    private String mContent;
    private String mTime;
    private String mTeacher;
    private String mPostTime;
    private String mDueTime;

    // Object ID
    public String getObjectID() {
        return mObjectID;
    }

    public void setObjectID(String obj) {
        this.mObjectID = obj;
    }

    public String getSubject() {
        return mSubject;
    }

    // Subject
    public void setSubject(String sub) {
        this.mSubject = sub;
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

    // Time
    public void setTime(String time) {
        this.mTime = time;
    }

    public String getTeacher() {
        return mTeacher;
    }

    // Teacher
    public void setTeacher(String teach) {
        this.mTeacher = teach;
    }

    // Post Time
    public void setPostTime(String ptime) {
        this.mPostTime = ptime;
    }

    public String getPostTime() {
        return mPostTime;
    }

    // Due Time
    public void setDueTime(String dtime) {
        this.mDueTime = dtime;
    }

    public String getDueTime() {
        return mDueTime;
    }

}
