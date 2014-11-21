package com.andreganske.paperinvest.bovespa;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.andreganske.paperinvest.bovespa.vo.PaperVO;
import com.andreganske.paperinvest.utils.DataService;

public class BovespaListService extends DataService {

    private List<PaperVO> paperVos;

    private List<String> papers;

    BovespaListService(List<String> papers) {
        super();
        this.papers = papers;
    }

    public List<PaperVO> getPaperVos() {
        return paperVos;
    }

    public void callService() {
        String url = bovespaUrl;
        for (String paper : papers) {
            url.concat(paper + "|");
        }
        super.callService(url);
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

        PaperVO paper = new PaperVO(parser.getAttributeValue(0), parser.getAttributeValue(1),
                parser.getAttributeValue(2), parser.getAttributeValue(3), parser.getAttributeValue(4),
                parser.getAttributeValue(5), parser.getAttributeValue(6), parser.getAttributeValue(7),
                parser.getAttributeValue(8), parser.getAttributeValue(9));

        paperVos.add(paper);
    }

}
