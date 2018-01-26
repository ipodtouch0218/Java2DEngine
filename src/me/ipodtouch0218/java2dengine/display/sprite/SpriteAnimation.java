package me.ipodtouch0218.java2dengine.display.sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class SpriteAnimation implements Cloneable {

	private int delayLength; //time it takes to change to the next frame
	private ArrayList<AnimationFrame> frames = new ArrayList<>(); //all the frames in one list
	
	private int delayCounter; //counts upwards until it hits the delayLength, then moves on to the next frame
	private int currentSprite; //index of the sprite currently being displayed
	private int animationDirection = 1; //1 for forwards, -1 for backwards
	private boolean stopped = true; //if the animation should be running
	private boolean loop = true; //if the animation should loop when restarting
	
	public SpriteAnimation(GameSprite[] sprites, int delayTime) {
		delayLength = delayTime;
		
		Arrays.asList(sprites).forEach(spr -> frames.add(new AnimationFrame(spr, delayLength)));
	}
	
	public void update() {
        if (!stopped) {
            delayCounter++;
            
            if (delayCounter > delayLength) {
                delayCounter = 0;
                currentSprite += animationDirection;

                if (loop) {
	                if (currentSprite > frames.size() - 1) {
	                    currentSprite = 0;
	                }
	                else if (currentSprite < 0) {
	                    currentSprite = frames.size() - 1;
	                }
                } else {
	                if (currentSprite > frames.size() - 1) {
	                    currentSprite = frames.size() - 1;
	                }
	                else if (currentSprite < 0) {
	                    currentSprite = 0;
	                }
                }
            }
        }

	}
	
	public void start() {
		stopped = false;
	}
	
	public void stop() {
		stopped = true;
	}
	
	public void reset() {
		currentSprite = 0;
		delayCounter = 0;
	}
	
	//---Getters---//
	public GameSprite getCurrentFrame() { return frames.get(currentSprite).getSprite(); }
	public int getDelayLength() { return delayLength; }
	public int getAnimationDirection() { return animationDirection; }
	public ArrayList<AnimationFrame> getFrames() { return frames; }
	public boolean isStopped() { return stopped; }
	public boolean isLooping() { return loop; }
	public int getFrameNumber() { return currentSprite; }
	
	//---Setters---//
	public void setDelayLength(int value) { delayLength = value; }
	public void setAnimationDirection(int value) { animationDirection = value; }
	public void setLooping(boolean value) { loop = value; }

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof SpriteAnimation)) return false;
		
		return this.frames.equals(((SpriteAnimation) obj).frames);
	}
	
	public SpriteAnimation clone() {
		try {
			return (SpriteAnimation) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
