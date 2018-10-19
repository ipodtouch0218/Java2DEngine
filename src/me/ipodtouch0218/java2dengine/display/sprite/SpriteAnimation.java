package me.ipodtouch0218.java2dengine.display.sprite;

import java.util.ArrayList;
import java.util.Arrays;

public class SpriteAnimation implements Cloneable {

	private static final ArrayList<SpriteAnimation> animations = new ArrayList<>();
	
	private ArrayList<GameSprite> frames = new ArrayList<>(); //all the frames in one list
	
	private int currentSprite; //index of the sprite currently being displayed
	private int animationDirection = 1; //1 for forwards, -1 for backwards
	private boolean stopped = true; //if the animation should be running
	private boolean loop = true; //if the animation should loop when restarting
	
	private double fps;
	private double lastUpdate = 0;
	
	public SpriteAnimation(GameSprite[] sprites, double fps) {

		this.fps = fps;
		
		frames.addAll(Arrays.asList(sprites));
		animations.add(this);
	}
	
	public void update(double delta) {
        if (stopped) { return; }
    	double updateDelta = 1d/fps;
        lastUpdate += delta;

        if (lastUpdate >= updateDelta) {
        	
        	currentSprite += animationDirection;
        	
        	if (loop) {
	        	if (currentSprite >= frames.size()) {
	        		currentSprite = 0;
	        	}
	        	if (currentSprite < 0) {
	        		currentSprite = frames.size()-1;
	        	}
        	} else {
        		if (currentSprite >= frames.size() && currentSprite < 0) {
        			currentSprite = Math.max(0, Math.min(currentSprite, frames.size()));        			
        			stop();
        		}
        	}
        	
        	lastUpdate = 0;
        }

	}
	
	public void start(boolean looping) {
		stopped = false;
		loop = looping;
	}
	
	public void stop() {
		stopped = true;
	}
	
	public void reset() {
		currentSprite = 0;
	}
	
	//---Getters---//
	public GameSprite getCurrentFrame() { return frames.get(currentSprite); }
//	public int getDelayLength() { return delayLength; }
	public int getAnimationDirection() { return animationDirection; }
	public ArrayList<GameSprite> getFrames() { return frames; }
	public boolean isStopped() { return stopped; }
	public boolean isLooping() { return loop; }
	public int getFrameNumber() { return currentSprite; }
	
	//---Setters---//
	public void setFPS(double value) { fps = value; }
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
	
	//---Static---//
	public static ArrayList<SpriteAnimation> getAnimations() {
		return animations;
	}
}
