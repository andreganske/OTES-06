package com.andreganske.otes06.app01.form;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class ListUserForm extends Form {
	
	private RecordStore storage;

	public ListUserForm() {
		super("Users");

		addCommand(new Command("Back", Command.BACK, 1));
		addCommand(new Command("List", Command.OK, 1));
	}
	
	public void show() {
		try {
			storage = RecordStore.openRecordStore("user", true);
			int size = storage.getNumRecords();
			if (size == 0) {
				append("Sorry but there is nothing to read! Come back later!");
			} else {
				append("Name \t\t | Age\n");
				
				String tmp;
				String[] user = new String[2];
				
				for (int i = size; i >= 1; i--) {
					tmp = new String(storage.getRecord(i));
					user[0] = tmp.substring(0, tmp.indexOf("-"));
					user[1] = tmp.substring(tmp.indexOf("-") + 1);
					append(user[0] + " \t\t | " + user[1] + " \n");
				}				
			}
			storage.closeRecordStore();
			
		} catch (RecordStoreNotOpenException e) {
			System.out.println("You need to open  the storage before using it\n" + e.getMessage());
		} catch (InvalidRecordIDException e) {
			System.out.println("Storage not found\n" + e.getMessage());
		} catch (RecordStoreException e) {
			System.out.println("Storage error\n" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Unknow error\n" + e.getMessage());
		}
	}

}
