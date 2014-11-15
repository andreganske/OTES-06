package com.andreganske.paperinvest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

public class PaperActivity extends Activity {

    protected static final int LOGIN_ACTIVITY_CODE = 100;
    protected static final int EDIT_ACTIVITY_CODE = 200;

    protected TextView loggedinInfoView;

    protected LayoutInflater inflater;

    protected Paper paper;

    protected ParseQueryAdapter<Paper> paperListAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EDIT_ACTIVITY_CODE) {
                paperListAdapter.loadObjects();
            } else if (requestCode == LOGIN_ACTIVITY_CODE) {
                if (ParseUser.getCurrentUser().isNew()) {
                    syncPapersToParse();
                } else {
                    loadFromParse();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.paper_invest_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            if (ParseUser.getCurrentUser() != null) {
                startActivityForResult(new Intent(this, PaperNewActivity.class), EDIT_ACTIVITY_CODE);
            }
        }

        if (item.getItemId() == R.id.action_sync) {
            syncPapersToParse();
        }

        if (item.getItemId() == R.id.action_logout) {
            ParseUser.logOut();
            ParseAnonymousUtils.logIn(null);
            updateLoggedInInfo();
            paperListAdapter.clear();
            ParseObject.unpinAllInBackground(PaperInvest.PAPER_GROUP_NAME);
        }

        if (item.getItemId() == R.id.action_login) {
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), LOGIN_ACTIVITY_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean realUser = !ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser());
        menu.findItem(R.id.action_login).setVisible(!realUser);
        menu.findItem(R.id.action_logout).setVisible(realUser);
        return true;
    }

    protected void openMainView() {
        startActivityForResult(new Intent(this, PaperListActivity.class), 0);
    }

    protected void openEditView(Paper paper) {
        Intent i = new Intent(this, PaperNewActivity.class);
        i.putExtra("ID", paper.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);
    }

    protected void updateLoggedInInfo() {
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            loggedinInfoView.setText(getString(R.string.logged_in, currentUser.getString("name")));
        } else {
            loggedinInfoView.setText(getString(R.string.not_logged_in));
        }
    }

    protected void syncPapersToParse() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
            if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
                ParseQuery<Paper> query = Paper.getQuery();
                query.fromPin(PaperInvest.PAPER_GROUP_NAME);
                query.whereEqualTo("isDraft", true);
                query.findInBackground(new FindCallback<Paper>() {

                    @Override
                    public void done(List<Paper> objects, ParseException e) {
                        if (e == null) {
                            for (final Paper paper : objects) {
                                paper.setDraft(false);
                                paper.saveInBackground(new SaveCallback() {

                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            if (!isFinishing()) {
                                                paperListAdapter.notifyDataSetChanged();
                                            }
                                        } else {
                                            paper.setDraft(true);
                                        }
                                    }
                                });
                            }
                        } else {
                            Log.i("PaperListActivity",
                                    "syncPapersToParse: Error finding pinned papers: " + e.getMessage());
                        }
                    }
                });
            } else {
                ParseLoginBuilder builder = new ParseLoginBuilder(this);
                startActivityForResult(builder.build(), LOGIN_ACTIVITY_CODE);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Your device appears to be offline. Some papers may not have synced to Parse.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    protected void loadFromParse() {
        ParseQuery<Paper> query = Paper.getQuery();
        query.whereEqualTo("author", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Paper>() {

            @Override
            public void done(List<Paper> objects, ParseException e) {
                if (e == null) {
                    ParseObject.pinAllInBackground(objects, new SaveCallback() {

                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                if (!isFinishing()) {
                                    paperListAdapter.loadObjects();
                                }
                            } else {
                                Log.i("PaperListActivity", "Error pinning papers: " + e.getMessage());
                            }
                        }
                    });
                } else {
                    Log.i("PaperListActivity", "loadFromParse: Error finding pinned papers: " + e.getMessage());
                }
            }
        });
    }

}