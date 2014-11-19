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
        Parse.initialize(this, "P87xbt0SGXOAdjsHk2CiXPugwWL6vzQfNf7QEVcF", "SgINFlm3D5qbnlhEROOQUbuVD6eyhB6C8qf6YG8F");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
