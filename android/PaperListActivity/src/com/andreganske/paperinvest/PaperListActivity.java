package com.andreganske.paperinvest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andreganske.paperinvest.bovespa.BovespaPapers;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class PaperListActivity extends PaperActivity {

    private ListView paperListView;

    private LinearLayout noPapersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);

        ParseAnalytics.trackAppOpened(getIntent());

        paperListView = (ListView) findViewById(R.id.paper_list_view);
        noPapersView = (LinearLayout) findViewById(R.id.no_papers_view);
        paperListView.setEmptyView(noPapersView);
        loggedinInfoView = (TextView) findViewById(R.id.loggedin_info);

        ParseQueryAdapter.QueryFactory<Paper> factory = new ParseQueryAdapter.QueryFactory<Paper>() {

            @Override
            public ParseQuery<Paper> create() {
                ParseQuery<Paper> query = Paper.getQuery();
                query.orderByAscending("code");
                query.fromLocalDatastore();
                return query;
            }
        };

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        paperListAdapter = new PaperListAdapter(this, factory);
        paperListView.setAdapter(paperListAdapter);
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

    protected class PaperListAdapter extends ParseQueryAdapter<Paper> {

        public PaperListAdapter(Context context, ParseQueryAdapter.QueryFactory<Paper> queryFactory) {
            super(context, queryFactory);
        }

        @Override
        public View getItemView(Paper paper, View view, ViewGroup parent) {
            ViewHolder holder;
            if (view == null) {
                view = inflater.inflate(R.layout.list_item_paper, parent, false);

                holder = new ViewHolder();
                holder.codigo = (TextView) view.findViewById(R.id.paper_codigo);
                holder.name = (TextView) view.findViewById(R.id.paper_name);
                holder.ultimo = (TextView) view.findViewById(R.id.paper_ultimo);
                holder.oscilacao = (TextView) view.findViewById(R.id.paper_oscilacao);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.codigo.setText(paper.getCode());
            holder.name.setText(BovespaPapers.getNameByCode(paper.getCode()).getName());
            holder.ultimo.setText("R$" + paper.getUltimo());
            holder.oscilacao.setText(paper.getOscilacao() + "%");

            if (paper.getOscilacao().contains("-")) {
                holder.oscilacao.setTextColor(Color.RED);
            } else if (paper.getOscilacao().compareToIgnoreCase("0,00") == 0) {
                holder.oscilacao.setTextColor(Color.LTGRAY);
            } else {
                holder.oscilacao.setTextColor(Color.GREEN);
            }

            if (paper.isDraft()) {
                holder.codigo.setTypeface(null, Typeface.ITALIC);
            } else {
                holder.codigo.setTypeface(null, Typeface.NORMAL);
            }
            return view;
        }
    }

    protected static class ViewHolder {
        TextView codigo;
        TextView name;
        TextView ultimo;
        TextView oscilacao;
    }

}
