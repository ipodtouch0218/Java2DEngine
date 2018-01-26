package me.ipodtouch0218.java2dengine.display.sprite;

import java.awt.image.BufferedImage;

public class AnimationFrame {

	private GameSprite sprite;
	private int duration;
	
	public AnimationFrame(BufferedImage image, int timer) {
		this(new GameSprite(image), timer);
	}
	public AnimationFrame(GameSprite sprite, int timer) {
		this.sprite = sprite;
		duration = timer;
	}
	
	//---Getters---//
	public GameSprite getSprite() { return sprite; }
	public int getDuration() { return duration; }
	
	//---Setters---//
	public void setSprite(GameSprite sprite) { this.sprite = sprite; }
	public void setDuration(int duration) { this.duration = duration; }
}
