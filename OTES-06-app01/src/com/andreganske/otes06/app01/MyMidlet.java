package com.andreganske.otes06.app01;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.andreganske.otes06.app01.form.CreateUserForm;
import com.andreganske.otes06.app01.form.ListUserForm;

public class MyMidlet extends MIDlet implements CommandListener {
	
	private CreateUserForm loginForm;
	
	private ListUserForm displayForm;

	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {

	}

	protected void pauseApp() {

	}

	protected void startApp() throws MIDletStateChangeException {
		// Initialize one form object
		loginForm = new CreateUserForm();
		displayForm = new ListUserForm();
		
		// set event listeners
		loginForm.setCommandListener(this);
		displayForm.setCommandListener(this);

		Display.getDisplay(this).setCurrent(loginForm);
	}
	
	public void commandAction(Command cmd, Displayable form){
		switch (cmd.getCommandType()) {
			case Command.EXIT:
				notifyDestroyed();
				break;
			case Command.OK:
				if (form == loginForm){
					if(cmd.getPriority() == 1) {
						loginForm.save();
					} else if (cmd.getPriority() == 2) {
						Display.getDisplay(this).setCurrent(displayForm);
					}
				} else if (form == displayForm) {
					displayForm.show();
				}
				break;
			case Command.BACK:
				Display.getDisplay(this).setCurrent(loginForm);
				break;
		}
	}
}
