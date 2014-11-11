package com.andreganske.paperinvest.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.andreganske.paperinvest.information.BovespaXMLParser;
import com.andreganske.paperinvest.information.PaperInformationParser;
import com.andreganske.paperinvest.information.vo.PaperVO;

public class DonwloadXMLTask extends AsyncTask<String, Void, String> {

    PaperInformationParser parser;

    @Override
    protected String doInBackground(String... params) {
        try {
            return loadXmlFromNetwork(params[0]);
        } catch (IOException e) {
            Log.i("DonwloadXMLTask", "DonwloadXMLTask: Error parsing papers: " + e.getMessage());
            return null;
        } catch (XmlPullParserException e) {
            Log.i("DonwloadXMLTask", "DonwloadXMLTask: Error parsing papers: " + e.getMessage());
            return null;
        }

    }

    private String loadXmlFromNetwork(String urlString) throws XmlPullParserException, IOException {
        InputStream stream = null;

        BovespaXMLParser bovespaXMLParser = new BovespaXMLParser();
        List<PaperVO> paperVOs = null;
        Calendar rigthNow = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("MMM dd h:mmaa");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean pref = sharedPrefs.getBoolean("summaryPref", false);

        StringBuilder htmlString = new StringBuilder();
        htmlString.append("<h3>" + getResources().getString(R.string.page_title) + "</h3>");
        htmlString.append("<em>" + getResources().getString(R.string.updated) + " "
                + formatter.format(rightNow.getTime()) + "</em>");

        return null;
    }

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

}
