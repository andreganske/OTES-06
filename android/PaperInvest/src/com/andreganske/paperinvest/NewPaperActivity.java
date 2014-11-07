package com.andreganske.paperinvest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class NewPaperActivity extends Activity {

    private Button saveButton;
    private Button deleteButton;
    private EditText paperText;
    private Paper paper;
    private String paperId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_paper);

        if (getIntent().hasExtra("ID")) {
            paperId = getIntent().getExtras().getString("ID");
        }

        paperText = (EditText) findViewById(R.id.paper_text);
        saveButton = (Button) findViewById(R.id.saveButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);

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
                        paperText.setText(paper.getName());
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        saveButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                paper.setName(paperText.getText().toString());
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
    }
}
