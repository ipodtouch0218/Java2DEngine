package me.ipodtouch0218.java2dengine.object.hitbox;

import java.awt.Graphics2D;

public abstract class Hitbox {

	private String tag = "";
	
	public abstract boolean intersects(double x, double y);
	public abstract void render(Graphics2D g);
	
	//-----//
	//getters
	public String getTag() { return tag; }

	//setters
	public void setTag(String value) {
		if (value == null) { return; }
		this.tag = value;
	}
}
