package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.object.Paper;

public class PaperApplication extends Application {

    public static final String PAPER_GROUP_NAME = "ALL_PAPERS";

    @Override
    public void onCreate() {
        super.onCreate();

        // Register paper subclass 
        ParseObject.registerSubclass(Paper.class);

        // Enable local datastore
        Parse.enableLocalDatastore(getApplicationContext());

        // Add your initialization code here
        Parse.initialize(this, "4VEGMPH0IHn92IEStOxf33G7l9hNIADVdwDbhZu6", "YvCiHaAkeTQ7vbEGYhGwg0Ff5Rlz0TdrLEP07jhK");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        // defaultACL.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACL, true);
    }
}