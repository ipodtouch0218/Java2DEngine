package me.ipodtouch0218.java2dengine.sound;

import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {

	private static HashMap<String, Sound> cachedSounds = new HashMap<>();
	
	private Clip clip;
	private String dir;
	
	private Sound(String dir) {
		AudioInputStream sound;
		this.dir = dir;
		try {
			sound = AudioSystem.getAudioInputStream(this.getClass().getResource("/res/sound/" + dir));
			clip = AudioSystem.getClip();
			clip.open(sound);
		} catch (UnsupportedAudioFileException | IOException  | LineUnavailableException e) {
			e.printStackTrace();
		}
		cachedSounds.put(dir, this);
	}
	
	public void play(int times) {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.loop(times);
	}
	public void setFramePosition(int value) { clip.setFramePosition(value); }
	public void stop() { clip.stop(); }
	public void play() {
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0); 
        clip.start();
	}
	
	//---Getters---//
	public Clip getClip() { return clip; }
	public String getName() { return dir; }
	
	//---Static---//
	public static Sound getSound(String dir) {
		if (cachedSounds.containsKey(dir)) {
			return cachedSounds.get(dir);
		}
		
		return new Sound(dir);
	}
}
