package com.andreganske.otes06.app01.form;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

public class MyForm extends Form {
	
	private TextField field;
	
	private TextField email;
	
	private ChoiceGroup choise;

	public MyForm(String title) {
		super(title);
	}
	
	public void setText(String text) {
		append(text);
	}
	
	public void insertTextField(String label, String text, int size) {
		field = new TextField(label, text, size, TextField.ANY);
		append(field);
	}
	
	public void insertEmailField(String label, String text, int size) {
		email = new TextField(label, text, size, TextField.EMAILADDR);
		append(email);
	}
	
	public void insertChoiceGroup(String label, String[] stringElements) {
		choise = new ChoiceGroup(label, ChoiceGroup.MULTIPLE, stringElements, null);
		append(choise);
	}
	
	public void insertBotaoSair() {
		addCommand(new Command("Sair", Command.EXIT, 1));
	}
	
	public void insertBotaoVoltar() {
		addCommand(new Command("voltar", Command.BACK, 1));
	}
	
	public void insertBotaoAvancar() {
		addCommand(new Command("avancar", Command.OK, 1));
	}
}
