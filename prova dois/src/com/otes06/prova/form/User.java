package com.otes06.prova.form;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.lcdui.Ticker;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class User extends Form {
	
	private TextField name;
	
	private Ticker ticker;
	
	private RecordStore storage;

	public User() {
		super("Novo usuário");
		
		addCommand(new Command("Ok", Command.OK, 1));
		addCommand(new Command("Sair", Command.EXIT, 1));
		
		initNameField();
		
		showNames();
	}
	
	private void initNameField() {
		name = new TextField("Nome", "", 80, TextField.ANY);
		append(name);
	}
	
	public void createTicker() {
		ticker = new Ticker("Obrigado por digitar seu nome, sr(a): " + name.getString());
		this.setTicker(ticker);
	}
	
	public void save() {
		try {
			storage = RecordStore.openRecordStore("NOMES", true);
			byte data[] = (name.getString().getBytes());
			storage.addRecord(data, 0, data.length);
			storage.closeRecordStore();
			
			showNames();
			
		} catch (RecordStoreFullException e) {
			System.out.println("Storage is full \n" + e.getMessage());
		} catch (RecordStoreNotFoundException e) {
			System.out.println("Storage not found \n" + e.getMessage());
		} catch (RecordStoreNotOpenException e) {
			System.out.println("You need to open  the storage before using it \n" + e.getMessage());
		} catch (RecordStoreException e) {
			System.out.println("Storage error \n" + e.getMessage());
		}
	}
	
	
	public void showNames() {
		try {
			storage = RecordStore.openRecordStore("NOMES", true);
			int size = storage.getNumRecords();
			
			if (size == 0) return;
			
			append("Usuários cadastrados:\n");		
			for (int i = size; i >= 1; i--) {
				append("* " + new String(storage.getRecord(i)) + "\n");
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
