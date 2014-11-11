package com.andreganske.paperinvest;

import android.app.Activity;

import com.andreganske.paperinvest.network.DownloadXmlTask;

public class NetworkActivity extends Activity {

    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";

    public static final String URL_paper = "http://www.bmfbovespa.com.br/Pregao-Online/ExecutaAcaoAjax.asp?CodigoPapel=";

    private static boolean wifiConnected = false;
    private static boolean mobileConnected = false;

    public static boolean refreshDisplay = true;
    public static String sPref = null;

    public void loadPapers() {
        if (sPref.equals(ANY) && (wifiConnected || mobileConnected)) {
            new DownloadXmlTask().execute(URL_paper);
        } else if (sPref.equals(WIFI) && wifiConnected) {
            new DownloadXmlTask().execute(URL_paper);
        }
    }

}
