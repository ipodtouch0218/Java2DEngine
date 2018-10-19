package me.ipodtouch0218.java2dengine.object.hitbox;

import java.awt.Graphics2D;
import java.awt.Shape;

import me.ipodtouch0218.java2dengine.display.ui.UIElement;

public class UIHitbox extends Hitbox {

	private UIElement owner;
	private Shape shape;
	
	public UIHitbox(UIElement owner, Shape shape) {
		this.owner = owner;
		this.shape = shape;
	}
	
	@Override
	public boolean intersects(double x, double y) {
		return shape.contains(x-owner.getX(), y-owner.getY());
	}

	@Override
	public void render(Graphics2D g) {}
	
}
