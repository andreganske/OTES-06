package com.andreganske.paperinvest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PaperInvest extends Application {

    public static final String PAPER_GROUP_NAME = "ALL_PAPERS";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Paper.class);
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this, "4VEGMPH0IHn92IEStOxf33G7l9hNIADVdwDbhZu6", "YvCiHaAkeTQ7vbEGYhGwg0Ff5Rlz0TdrLEP07jhK");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
