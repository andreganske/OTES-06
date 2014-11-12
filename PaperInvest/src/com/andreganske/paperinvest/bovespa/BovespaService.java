package com.andreganske.paperinvest.bovespa;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class BovespaService {
		
	private PaperVO paperVo;
	
	private List<PaperVO> paperVos;
	
	private HttpResponse httpResponse;
	
	private static final String ns = null;
	
	private static final String bovespaUrl = "http://www.bmfbovespa.com.br/Pregao-Online/ExecutaAcaoAjax.asp?CodigoPapel=";
	
	public BovespaService (String codigo) {
		super();
		paperVo = new PaperVO(codigo);
	}
	
	public BovespaService (List<String> codigos) {
		super();
		for (String codigo : codigos) {
			PaperVO paper = new PaperVO(codigo);
			paperVos.add(paper);
		}
	}
	
	public PaperVO getPaperVo() {
		return paperVo;
	}
	
	public void setPaperVo(PaperVO paperVo) {
		this.paperVo = paperVo;
	}

	public void callService() {
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = null;
		
		if (paperVo != null) {
			httpGet = new HttpGet(bovespaUrl + paperVo.getCodigo());
		} else if (!paperVos.isEmpty()) {
			String url = bovespaUrl;
			for (PaperVO paper : paperVos) {
				url.concat(paper.getCodigo() + "|");
			}
			httpGet = new HttpGet(url);
		}
		
		try {
			httpResponse = client.execute(httpGet);
			readResponse();
		} catch (Exception e) {
			Log.i("BovespaService.callService", "Cannot execute httpResponse " + e.getMessage());
		}
	}
	
	private void readResponse () {
		HttpEntity entity = httpResponse.getEntity();
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("BovespaService.readResponse", "Cannot read response " + e.getMessage());
		}
	}
	
    private void readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
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
