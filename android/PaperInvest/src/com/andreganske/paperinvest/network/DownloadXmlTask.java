package com.andreganske.paperinvest.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;
import android.util.Log;

import com.andreganske.paperinvest.information.BovespaXMLParser;
import com.andreganske.paperinvest.information.vo.PaperVO;

public class DownloadXmlTask extends AsyncTask<String, Void, String> {

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
        BovespaXMLParser bovespaXMLParser = new BovespaXMLParser();
        List<PaperVO> paperVOs = null;
        InputStream stream = null;

        // cria a lista de coisas que vai ser exibida na tela
        StringBuilder htmlString = new StringBuilder();

        try {
            stream = downloadUrl(urlString);
            paperVOs = bovespaXMLParser.parse(stream);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        for (PaperVO paper : paperVOs) {
            // do want you want :)
            // recomend to insert on paper object list
            System.out.println(paper.toString());
        }

        return htmlString.toString();
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
