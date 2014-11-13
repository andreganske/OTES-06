package com.andreganske.paperinvest;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andreganske.paperinvest.bovespa.BovespaPapers;
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
    private AutoCompleteTextView paperText;
    private Button saveButton;

    private String codigo;

    protected Paper paper;

    private String paperId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_new);

        if (getIntent().hasExtra("ID")) {
            paperId = getIntent().getExtras().getString("ID");
        }

        paperText = (AutoCompleteTextView) findViewById(R.id.paper_text);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        searchButton = (Button) findViewById(R.id.searchButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                BovespaPapers.INDICES);
        paperText = (AutoCompleteTextView) findViewById(R.id.paper_text);
        paperText.setAdapter(adapter);

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
                paper.setCode(paperText.getText().toString());
                paper.setDraft(true);
                paper.setAuthor(ParseUser.getCurrentUser());
                paper.pinInBackground(PaperInvest.PAPER_GROUP_NAME, new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        if (isFinishing()) {
                            openMainView();
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
                EditText code = (EditText) findViewById(R.id.paper_text);

                if (code.getText().toString().equalsIgnoreCase("")) {
                    findViewById(R.id.paperErrorCode).setVisibility(View.VISIBLE);
                } else {
                    codigo = code.getText().toString();
                    bovespa = new BovespaService(codigo);
                    new ContactWebservice().execute(bovespa);
                }
            }
        });
    }

    private void openMainView() {
        startActivityForResult(new Intent(this, PaperListActivity.class), 0);
    }

    private class ContactWebservice extends AsyncTask<BovespaService, Void, String> {

        @Override
        protected String doInBackground(BovespaService... params) {
            try {
                params[0].callService();
            } catch (Exception e) {
                e.printStackTrace();
            }

            paper.setUltimo(params[0].getPaperVo().getUltimo());
            paper.setOscilacao(params[0].getPaperVo().getOscilacao());

            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.compareTo("ok") == 0) {
                ((TextView) findViewById(R.id.paperCode)).setText(paper.getCode());
                ((TextView) findViewById(R.id.paperlast)).setText(paper.getUltimo());
                ((TextView) findViewById(R.id.paperOscilation)).setText(paper.getOscilacao());
            }
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
