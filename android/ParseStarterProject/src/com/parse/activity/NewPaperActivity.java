package com.parse.activity;

import android.app.Activity;

public class NewPaperActivity extends Activity {

	private Button saveButton;
	private Button deleteButton;
	private EditText paperText;
	private Paper paper;
	private String paperId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		saveButton.setOnClickListener(new OnClickListener() {
			paper.setName(paper);
		}

		deleteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				paper.deleteEventually();
				setResult(Activity.RESULT_OK);
				finish();
			}
		}
	}
}
