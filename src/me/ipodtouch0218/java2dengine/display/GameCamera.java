package me.ipodtouch0218.java2dengine.display;

import java.awt.Rectangle;

import me.ipodtouch0218.java2dengine.object.GameObject;

public class GameCamera {

	private GameObject focusedObject;
	private int x, y;
	private int width, height;
	private Rectangle bounds = new Rectangle(0,0,500,500);
	
	public GameCamera(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public int getX() { 
		focus();
		return x; 
	}
	public int getY() { 
		focus();
		return y; 
	}
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public Rectangle getBounds() { return bounds; }
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	public void setWidth(int width) { 
		this.width = width;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void focusOnObject(GameObject object) {
		focusedObject = object;
	}
	
	private void focus() {
		if (focusedObject == null) return;
		x = (int) Math.max(0, focusedObject.getX()-(width/2));
		y = (int) Math.max(0, focusedObject.getY()-(height/2));
		
		if (bounds != null) {
			x = (int) Math.min(x, bounds.getWidth()-width);
			y = (int) Math.min(y, bounds.getHeight()-height);
		}
	}

	public GameObject getFocusedObject() { return focusedObject; }
}
