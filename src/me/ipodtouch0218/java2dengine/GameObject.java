package me.ipodtouch0218.java2dengine;

import java.awt.Graphics;
import java.util.UUID;

import me.ipodtouch0218.java2dengine.display.sprite.GameSprite;
import me.ipodtouch0218.java2dengine.display.sprite.SpriteAnimation;

public abstract class GameObject implements Cloneable {

	protected double x = 0, y = 0;
	protected SpriteAnimation animation;
	protected GameSprite sprite;
	private UUID uuid = UUID.randomUUID();
	
	
	public void tick() {}
	public void onRemove() {}
	
	public void render(Graphics g) {
		if (animation == null || animation.getCurrentFrame().getImage() == null) {
			if (!(sprite == null || sprite.getImage() == null)) {
				g.drawImage(sprite.getScaledImage(), (int) x, (int) y, null);
			}
			return;
		}
		animation.update();
		GameSprite image = animation.getCurrentFrame();
		g.drawImage(image.getScaledImage(), (int) x-(image.getScaledImage().getWidth(null)/2), (int) y-(image.getScaledImage().getHeight(null)/2), null);
	}
	

	
	//---Getters---//
	public UUID getUniqueId() { return uuid; }
	public double getX() { return x; }
	public double getY() { return y; }
	public GameSprite getSprite() { return sprite; }
	public SpriteAnimation getAnimation() { return animation; }
	
	//---Setters---//
	public void setX(double x) { 
		this.x = x; 
	}
	public void setY(double y) { 
		this.y = y; 
	}
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setSprite(GameSprite value) {
		this.sprite = value;
	}
	public void setAnimation(SpriteAnimation value) {
		if (value == null) return;
		if (animation != null && animation.equals(value)) { return; }
		animation = value.clone();
		animation.start();
	}
	public void setUniqueId(UUID uid) {
		this.uuid = uid;
	}
	
	//---Others---//
	public GameObject clone() {
		try {
			return (GameObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
