package com.aashir.bpc.generic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class AppController extends Application {

    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YvfhzbQCNEl6fWXgdF1fHXZnQwk7Ba8VXOrDTsy7", "KBAW0mhegwB8sTweKRFMlxMOqyJPSEBQdUWjytmO");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
