package com.andreganske.paperinvest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreganske.paperinvest.bovespa.BovespaPapers;
import com.andreganske.paperinvest.bovespa.BovespaService;
import com.andreganske.paperinvest.bovespa.PaperVO;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class PaperNewActivity extends PaperActivity {

    private BovespaService bovespa;

    private Button deleteButton;
    private Button searchButton;
    private Button saveButton;

    private AutoCompleteTextView paperText;

    private RelativeLayout loader;

    private PaperVO paperVO;

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
        loader = (RelativeLayout) findViewById(R.id.paperLoader);

        saveButton.setEnabled(false);

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
                    bovespa = new BovespaService(code.getText().toString());
                    new ContactWebservice().execute(bovespa);
                }
            }
        });
    }

    protected class ContactWebservice extends AsyncTask<BovespaService, Void, String> {

        @Override
        protected String doInBackground(BovespaService... params) {
            params[0].callService();

            paper.setCode(params[0].getPaperVo().getCodigo());
            paperVO = params[0].getPaperVo();

            if (validadeResult()) {
                paper.setUltimo(paperVO.getUltimo());
                paper.setOscilacao(paperVO.getOscilacao());
                return "ok";
            } else {
                return "nok";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            loader.setVisibility(View.INVISIBLE);

            if (result.compareTo("ok") == 0) {
                ((TextView) findViewById(R.id.paperCode)).setText(paperVO.getNome());
                ((TextView) findViewById(R.id.paperlast)).setText(paperVO.getUltimo());
                ((TextView) findViewById(R.id.paperOscilation)).setText(paperVO.getOscilacao());
                saveButton.setEnabled(true);
            } else {
                findViewById(R.id.paperNewError).setVisibility(View.VISIBLE);
                saveButton.setEnabled(false);
            }
        }

        @Override
        protected void onPreExecute() {
            loader.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private boolean validadeResult() {
            if (paperVO.getCodigo() == null || paperVO.getCodigo().length() == 0) {
                return false;
            } else if (paperVO.getNome() == null || paperVO.getNome().length() == 0) {
                return false;
            } else if (paperVO.getUltimo() == null || paperVO.getUltimo().length() == 0) {
                return false;
            } else if (paperVO.getOscilacao() == null || paperVO.getOscilacao().length() == 0) {
                return false;
            }

            return true;
        }
    }

}
