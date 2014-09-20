package com.andreganske.otes06.app02.middlet;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.andreganske.otes06.app02.canvas.MyCanvas;

public class MyMidlet extends MIDlet {

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		
	}

	protected void pauseApp() {
		
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			Display.getDisplay(this).setCurrent(new MyCanvas());
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
