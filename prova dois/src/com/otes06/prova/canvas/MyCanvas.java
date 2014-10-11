package com.otes06.prova.canvas;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

public class MyCanvas extends GameCanvas {
	
	public MyCanvas() {
		super(false);
		setTitle("Circulo");
	}
	
	public void paint(Graphics g) {
		g.setColor(255, 255, 255);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0, 0, 0);
		g.fillArc(getWidth()/4, getHeight()/4, getWidth()/2, getHeight()/2, 0, 360);
	}
}
