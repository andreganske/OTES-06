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
        Parse.initialize(this, "vy6Q3MjXafFT8VGo3KQH0H1IdXMfn8ZaSdEhCqGe", "yIEoIgCfov5BPLPcoK5OdI19uDwsE43bvnkNfoUV");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
