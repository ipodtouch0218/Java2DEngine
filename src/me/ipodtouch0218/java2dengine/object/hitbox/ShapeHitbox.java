package me.ipodtouch0218.java2dengine.object.hitbox;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

import me.ipodtouch0218.java2dengine.object.GameObject;

public class ShapeHitbox extends Hitbox {

	private GameObject parent;
	private Shape hitbox;
	
	public ShapeHitbox(Shape hitbox) {
		this(hitbox, "");
	}
	public ShapeHitbox(Shape hitbox, GameObject parent) {
		this(hitbox, "", parent);
	}
	public ShapeHitbox(Shape hitbox, String tag) {
		this(hitbox, tag, null);
	}
	public ShapeHitbox(Shape hitbox, String tag, GameObject parent) {
		this.hitbox = hitbox;
		setTag(tag);
		this.parent = parent;
	}
	//-----//
	@Override
	public void render(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.draw(hitbox);
		if (!getTag().equals("")) {
			g.setColor(Color.BLACK);
			Rectangle bounds = hitbox.getBounds();
			g.drawString(getTag(), (int) bounds.getCenterX(), (int) bounds.getCenterY());
		}
	}
	
	//-----//
	//setters
	public void setParent(GameObject parent) {
		this.parent = parent;
	}
	
	//getters
	public GameObject getParent() {
		return parent;
	}
	@Override
	public boolean intersects(double x, double y) {
		double newX = x - (parent == null ? 0 : parent.getX());
		double newY = y - (parent == null ? 0 : parent.getY());
		return hitbox.contains(newX, newY);
	}
	
}
