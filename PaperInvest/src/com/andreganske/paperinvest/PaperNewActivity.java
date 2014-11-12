package com.andreganske.paperinvest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreganske.paperinvest.bovespa.BovespaService;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PaperNewActivity extends Activity {

    private BovespaService bovespa;

    private Button deleteButton;
    private Button searchButton;
    private EditText paperText;
    private Button saveButton;

    private String codigo;

    private Paper paper;

    private String paperId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_new);

        paperText = (EditText) findViewById(R.id.paper_text);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

        if (getIntent().hasExtra("ID")) {
            paperId = getIntent().getExtras().getString("ID");
        }

        if (paperId == null) {
            paper = new Paper();
            paper.setUuidString();
        } else {
            ParseQuery<Paper> query = Paper.getQuery();
            query.fromLocalDatastore();
            query.whereEqualTo("uuid", paperId);
            query.getFirstInBackground(new GetCallback<Paper>() {

                @Override
                public void done(Paper object, ParseException e) {
                    if (!isFinishing()) {
                        paper = object;
                        paperText.setText(paper.getCode());
                        deleteButton.setVisibility(View.VISIBLE);

                        bovespa = new BovespaService(paper.getCode());
                        new ContactWebservice().execute(bovespa);
                    }
                }
            });
        }
        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                TextView tv = (TextView) findViewById(R.id.paper_result);
                tv.setText("Done!!!");

                paper.setCode(paperText.getText().toString());
                paper.setDraft(true);
                paper.setAuthor(ParseUser.getCurrentUser());
                paper.pinInBackground(PaperInvest.PAPER_GROUP_NAME, new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (isFinishing()) {
                            return;
                        }
                        if (e == null) {
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error saving " + e.getMessage(), Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                paper.deleteEventually();
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        searchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText sym = (EditText) findViewById(R.id.paper_text);
                TextView tv = (TextView) findViewById(R.id.paper_result);

                if (sym.getText().toString().equalsIgnoreCase("")) {
                    tv.setText("Plase, enter a stock code!");
                } else {
                    codigo = sym.getText().toString();
                    bovespa = new BovespaService(codigo);
                    new ContactWebservice().execute(bovespa);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.paper_invest_menu, menu);
        return true;
    }

    private class ContactWebservice extends AsyncTask<BovespaService, Void, String> {

        @Override
        protected String doInBackground(BovespaService... params) {
            params[0].callService();
            return "Price of " + params[0].getPaperVo().getNome() + ": " + params[0].getPaperVo().getUltimo();
        }

        @Override
        protected void onPostExecute(String result) {
            TextView txt = (TextView) findViewById(R.id.paper_result);
            txt.setText(result);
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
