package com.andreganske.otes06.app01.form;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class CreateUserForm extends Form {
	
	private TextField name;
	private TextField age;
	
	private RecordStore storage;
	
	public CreateUserForm() {
		super("Create new user");
		
		addCommand(new Command("Exit", Command.EXIT, 1));
		addCommand(new Command("Save", Command.OK, 1));
		addCommand(new Command("Show", Command.OK, 2));
		
		insertNameField();
		insertAgeField();
	}
	
	private void insertNameField() {
		name = new TextField("Name", "", 80, TextField.ANY);
		append(name);
	}
	
	private void insertAgeField() {
		age = new TextField("Age", "", 10, TextField.NUMERIC);
		append(age);
	}
	
	public void save() {
		try {
			storage = RecordStore.openRecordStore("user", true);
			byte data[] = (name.getString() + "-" + age.getString()).getBytes();
			storage.addRecord(data, 0, data.length);
			storage.closeRecordStore();
		} catch (RecordStoreFullException e) {
			System.out.println("Storage is full\n" + e.getMessage());
		} catch (RecordStoreNotFoundException e) {
			System.out.println("Storage not found\n" + e.getMessage());
		} catch (RecordStoreNotOpenException e) {
			System.out.println("You need to open  the storage before using it\n" + e.getMessage());
		} catch (RecordStoreException e) {
			System.out.println("Storage error\n" + e.getMessage());
		}
	}
}
