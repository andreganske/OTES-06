package com.otes06.prova;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Ticker;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.otes06.prova.canvas.MyCanvas;
import com.otes06.prova.form.User;

public class MyMidlet extends MIDlet implements CommandListener {
	
	private User form;
	
	private Canvas canvas;
	
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		
	}

	protected void pauseApp() {
		
	}

	protected void startApp() throws MIDletStateChangeException {
		// questoesUmAQuatro();
		questaoCinco();
	}

	public void commandAction(Command cmd, Displayable form) {
		switch (cmd.getCommandType()) {
		case Command.OK:
			if (form == this.form) {
				if (cmd.getPriority() == 1) {
					this.form.createTicker();
				}
			}
			break;
		case Command.EXIT:
			this.form.save();
			break;
		}
	}
	
	private void questoesUmAQuatro() {
		form = new User();
		form.setCommandListener(this);		
		Display.getDisplay(this).setCurrent(form);
	}
	
	private void questaoCinco() {
		canvas = new MyCanvas();
		canvas.setCommandListener(this);
		Display.getDisplay(this).setCurrent(canvas);
	}
}
