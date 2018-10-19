package me.ipodtouch0218.java2dengine.display.ui;

import java.awt.Graphics2D;

import me.ipodtouch0218.java2dengine.display.ui.events.UIEventHandler;

public class UIElement {

	private float x, y;
	private UIEventHandler eventHandler;
	private boolean mouseIsHovering;
	private boolean isRendering;
	
	public void render(Graphics2D g) {}
	
	//--------//
	
	//setters
	public void setEventHandler(UIEventHandler value) {
		this.eventHandler = value;
	}
	public void setRendering(boolean value) {
		this.isRendering = value; 
	}
	
	//getters
	public float getX() { return x; }
	public float getY() { return y; }
	public UIEventHandler getEventHandler() { return eventHandler; }
	public boolean isRendering() { return isRendering; }
}
