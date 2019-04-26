package me.ipodtouch0218.java2dengine.object;

import java.awt.Graphics2D;
import java.util.UUID;

import me.ipodtouch0218.java2dengine.display.sprite.GameSprite;
import me.ipodtouch0218.java2dengine.display.sprite.SpriteAnimation;

public abstract class GameObject implements Cloneable {

	protected double x = 0, y = 0;
	protected SpriteAnimation animation;
	protected GameSprite sprite;
	private boolean rendering = true, ticking = true;
	private UUID uuid = UUID.randomUUID();
	
	public void tick(double delta) {}
	public void onCreate() {}
	public void onRemove() {}
	public final void remove() {
		onRemove();
	}
	
	public void render(Graphics2D g) {
		if (animation == null || animation.getCurrentFrame().getImage() == null) {
			if (!(sprite == null || sprite.getImage() == null)) {
				g.drawImage(sprite.getImage(), (int) x, (int) y, null);
			}
			return;
		}
		GameSprite image = animation.getCurrentFrame();
		g.drawImage(image.getImage(), (int) x-(image.getImage().getWidth(null)/2), (int) y-(image.getImage().getHeight(null)/2), null);
	}
	

	
	//---Getters---//
	public UUID getUniqueId() { return uuid; }
	public double getX() { return x; }
	public double getY() { return y; }
	public GameSprite getSprite() { return sprite; }
	public SpriteAnimation getAnimation() { return animation; }
	public boolean isRendering() { return rendering; }
	public boolean isTicking() { return ticking; }
	
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
		if (this.animation != null && this.animation.equals(value)) { return; }
		animation = value.clone();
		animation.start(true);
	}
	public void setUniqueId(UUID uid) {
		this.uuid = uid;
	}
	public void setTicking(boolean value) {
		this.ticking = value;
	}
	public void setRendering(boolean value) {
		this.rendering = value;
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
