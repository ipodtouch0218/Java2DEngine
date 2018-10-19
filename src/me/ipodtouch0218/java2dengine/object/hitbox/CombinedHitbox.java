package me.ipodtouch0218.java2dengine.object.hitbox;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class CombinedHitbox extends Hitbox {
	
	private ArrayList<ShapeHitbox> hitboxes = new ArrayList<>();
	
	public CombinedHitbox(ShapeHitbox... hitboxes) {
		
	}

	


	@Override
	public void render(Graphics2D g) {
		for (ShapeHitbox hitbox : hitboxes) {
			hitbox.render(g);
		}
	}
	
	//-----//
	//getters
	@Override
	public boolean intersects(double x, double y) {
		for (ShapeHitbox hitbox : hitboxes) {
			if (hitbox.intersects(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	
	
}
