package com.andreganske.paperinvest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andreganske.paperinvest.bovespa.BovespaService;

public class PaperInvestActivity extends Activity {
	
	private String codigo;
	
	private BovespaService bovespa;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_paper_invest);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_paper_invest, menu);
		return true;
	}
	
	public void onBtnClicked (View v) {
		if (v.getId() == R.id.button1) {
			EditText sym = (EditText) findViewById(R.id.editText1);
			TextView tv = (TextView) findViewById(R.id.textView2);
			
			if (sym.getText().toString().equalsIgnoreCase("")) {
				tv.setText("Plase, enter a stock code!");
			} else {
				codigo = sym.getText().toString();
				bovespa = new BovespaService(codigo);
				new ContactWebservice().execute(bovespa);
			}
		}
	}
	
	private class ContactWebservice extends AsyncTask<BovespaService, Void, String> {

		@Override
		protected String doInBackground(BovespaService... params) {
			params[0].callService();
			return "Price of " + params[0].getPaperVo().getNome() + ": " + params[0].getPaperVo().getUltimo();
		}
		@Override
		protected void onPostExecute(String result) {
			TextView txt = (TextView) findViewById(R.id.textView2);
			txt.setText(result);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
