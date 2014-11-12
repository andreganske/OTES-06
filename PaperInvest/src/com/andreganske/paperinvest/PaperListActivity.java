package com.andreganske.paperinvest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class PaperListActivity extends Activity {

    private static final int LOGIN_ACTIVITY_CODE = 100;
    private static final int EDIT_ACTIVITY_CODE = 200;

    private ParseQueryAdapter<Paper> paperListAdapter;

    private LayoutInflater inflater;

    private ListView paperListView;

    private LinearLayout noPapersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_paper_list);
        paperListView = (ListView) findViewById(R.id.paper_list_view);
        noPapersView = (LinearLayout) findViewById(R.id.no_papers_view);
        paperListView.setEmptyView(noPapersView);

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
        // paperListAdapter = new PaperListAdapter(this, factory);

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
            //syncPapersToParse();
            //updateLoggedInInfo();
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
            //syncPapersToParse();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        boolean realUser = !ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser());
        // menu.findItem(R.id.action_login).setVisible(!realUser);
        // menu.findItem(R.id.action_logout).setVisible(realUser);
        return true;
    }

    private void openEditView(Paper paper) {
        Intent i = new Intent(this, PaperNewActivity.class);
        i.putExtra("ID", paper.getUuidString());
        startActivityForResult(i, EDIT_ACTIVITY_CODE);
    }

    public class PaperList extends ParseQueryAdapter<Paper> {

        public PaperList(Context context, Class<? extends ParseObject> clazz) {
            super(context, clazz);
        }

        public PaperList(Context context, ParseQueryAdapter.QueryFactory<Paper> queryFactory) {
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

            paperTitle.setText(paper.getCode());

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
