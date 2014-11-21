package com.andreganske.paperinvest.utils;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class DataService {

    protected HttpResponse httpResponse;

    protected static final String ns = null;

    protected static final String bovespaUrl = "http://www.bmfbovespa.com.br/Pregao-Online/ExecutaAcaoAjax.asp?CodigoPapel=";

    protected void callService(String url) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            httpResponse = client.execute(httpGet);
            readResponse();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    protected abstract void readResponse() throws XmlPullParserException, IOException;

    protected abstract void readEntry(XmlPullParser parser) throws XmlPullParserException, IOException;

    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
