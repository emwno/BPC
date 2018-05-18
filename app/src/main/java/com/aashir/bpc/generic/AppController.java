package com.aashir.bpc.generic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class AppController extends Application {

    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "apiKey", "secretKey");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
