package com.andreganske.otes06.app02.canvas;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

public class MyCanvas extends Canvas {
	
	private Sprite cannon;
	
	private int posX, posY;
	
	public MyCanvas() {
		setTitle("Jogo de Tiro");
		createcannon();
	}
	
	protected void paint(Graphics graphics) {
		graphics.setColor(255, 255, 255);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		cannon.paint(graphics);
	}
	
	protected void keyPressed(int keyCode) {
		switch (keyCode) {
		case 4:
			break;

		default:
			break;
		}
		super.keyPressed(keyCode);
	}
	
	private void createcannon() {
		try {
			cannon = new Sprite(Image.createImage("/cannon.png"), 35, 40);
			setCenter(cannon);
			cannon.setPosition(posX, posY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setCenter(Sprite obj) {
		posX = (getWidth()/2) - (obj.getWidth()/2);
		posY = getHeight() - obj.getHeight();
	}

}
