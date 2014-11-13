package com.andreganske.paperinvest;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class PaperInvest extends Application {

    public static final String PAPER_GROUP_NAME = "ALL_PAPERS";

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Paper.class);
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this, "co1z3OCpRS8Ue4JBeNRmWsvj2V48sfSym0kxbCmh", "Er3unH2uztuPFr8Pzfk2oxAYcKwE4Kc9hOcpk9NF");

        ParseUser.enableAutomaticUser();
        // ParseACL defaultACL = new ParseACL();
        // ParseACL.setDefaultACL(defaultACL, true);
    }
}
