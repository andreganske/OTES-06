package com.andreganske.paperinvest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class PaperListActivity extends Activity {

    private static final int LOGIN_ACTIVITY_CODE = 100;
    private static final int EDIT_ACTIVITY_CODE = 200;

    private ParseQueryAdapter<Paper> paperListAdapter;

    private LayoutInflater inflater;

    private ListView paperListView;

    private LinearLayout noPapersView;

    private TextView loggedinInfoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);

        paperListView = (ListView) findViewById(R.id.paper_list_view);
        noPapersView = (LinearLayout) findViewById(R.id.no_papers_view);
        paperListView.setEmptyView(noPapersView);
        loggedinInfoView = (TextView) findViewById(R.id.loggedin_info);

        ParseQueryAdapter.QueryFactory<Paper> factory = new ParseQueryAdapter.QueryFactory<Paper>() {
            @Override
            public ParseQuery<Paper> create() {
                ParseQuery<Paper> query = Paper.getQuery();
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();
                return query;
            }
        };

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paperListAdapter = new PaperListAdapter(this, factory);

        paperListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Paper paper = paperListAdapter.getItem(position);
                openEditView(paper);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            syncPapersToParse();
            updateLoggedInInfo();
        }
    }

    private void updateLoggedInInfo() {
        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            loggedinInfoView.setText(getString(R.string.logged_in, currentUser.getString("name")));
        } else {
            loggedinInfoView.setText(getString(R.string.not_logged_in));
        }
    }

    private void openEditView(Paper paper) {
        Intent i = new Intent(this, NewPaperActivity.class);
        i.putExtra("ID", paper.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);
    }

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
        getMenuInflater().inflate(R.menu.paper_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_new) {
            if (ParseUser.getCurrentUser() != null) {
                startActivityForResult(new Intent(this, NewPaperActivity.class), EDIT_ACTIVITY_CODE);
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

    private void syncPapersToParse() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if ((ni != null) && (ni.isConnected())) {
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
                        Log.i("PaperListActivity", "syncPapersToParse: Error finding pinned papers: " + e.getMessage());
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(),
                    "Your device appears to be offline. Some papers may not have synced to Parse.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void loadFromParse() {
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

    private class PaperListAdapter extends ParseQueryAdapter<Paper> {
        public PaperListAdapter(Context context, ParseQueryAdapter.QueryFactory<Paper> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Paper paper, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_paper, parent, false);
                holder = new ViewHolder();
                holder.paperTitle = (TextView) view.findViewById(R.id.paper_name);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            TextView paperTitle = holder.paperTitle;
            paperTitle.setText(paper.getName());
            if (paper.isDraft()) {
                paperTitle.setTypeface(null, Typeface.ITALIC);
            } else {
                paperTitle.setTypeface(null, Typeface.NORMAL);
            }
            return view;
        }
    }

    private static class ViewHolder {
        TextView paperTitle;
    }
}