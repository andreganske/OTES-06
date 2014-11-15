package com.andreganske.paperinvest.bovespa;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.andreganske.paperinvest.utils.DataService;

public class BovespaService extends DataService {

    private PaperVO paperVo;

    public BovespaService(String codigo) {
        super();
        paperVo = new PaperVO(codigo);
    }

    public PaperVO getPaperVo() {
        return paperVo;
    }

    @Override
    public void callService() {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(bovespaUrl + paperVo.getCodigo());
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

    @Override
    protected void readResponse() throws XmlPullParserException, IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            XmlPullParser parser = Xml.newPullParser();
            InputStream is = entity.getContent();

            parser.setInput(is, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, ns, "ComportamentoPapeis");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                if (parser.getName().equals("Papel")) {
                    readEntry(parser);
                } else {
                    skip(parser);
                }
            }
            is.close();
        }
    }

    @Override
    protected void readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "Papel");

        paperVo.setNome(parser.getAttributeValue(1));
        paperVo.setIbovespa(parser.getAttributeValue(2));
        paperVo.setData(parser.getAttributeValue(3));
        paperVo.setAbertura(parser.getAttributeValue(4));
        paperVo.setMinimo(parser.getAttributeValue(5));
        paperVo.setMaximo(parser.getAttributeValue(6));
        paperVo.setMedio(parser.getAttributeValue(7));
        paperVo.setUltimo(parser.getAttributeValue(8));
        paperVo.setOscilacao(parser.getAttributeValue(9));
    }
}
