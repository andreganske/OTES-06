package com.andreganske.paperinvest.information;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.andreganske.paperinvest.information.vo.PaperVO;

public class BovespaXMLParser {

    private static final String ns = null;

    public List<PaperVO> parse(InputStream in) throws XmlPullParserException, IOException {
        try {

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            return readFeed(parser);

        } finally {
            in.close();
        }
    }

    private List<PaperVO> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<PaperVO> entries = new ArrayList<PaperVO>();

        parser.require(XmlPullParser.START_TAG, ns, "ComportamentoPapeis");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Papel")) {
                entries.add(readEntry(parser));
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private PaperVO readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        PaperVO paperVO = new PaperVO();

        parser.require(XmlPullParser.START_TAG, ns, "Papel");

        paperVO.codigo = parser.getAttributeValue(null, "Codigo");
        paperVO.nome = parser.getAttributeValue(null, "nome");
        paperVO.ibovespa = parser.getAttributeValue(null, "ibovespa");
        paperVO.data = parser.getAttributeValue(null, "data");
        paperVO.abertura = parser.getAttributeValue(null, "abertura");
        paperVO.minimo = parser.getAttributeValue(null, "minimo");
        paperVO.maximo = parser.getAttributeValue(null, "maximo");
        paperVO.medio = parser.getAttributeValue(null, "medio");
        paperVO.ultimo = parser.getAttributeValue(null, "ultimo");
        paperVO.oscilacao = parser.getAttributeValue(null, "oscilacao");
        paperVO.minino = parser.getAttributeValue(null, "minino");

        parser.require(XmlPullParser.END_TAG, ns, "Papel");

        return paperVO;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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
