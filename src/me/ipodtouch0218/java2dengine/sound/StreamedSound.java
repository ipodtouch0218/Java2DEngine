package me.ipodtouch0218.java2dengine.sound;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class StreamedSound {

	private boolean playing = false;
	private String dir;
	private float volume = -5f;
	private long frameLength = 1;
	private int startPoint = 0;
	
	private Clip tempClip; 
	
	public StreamedSound(String dir) {
		this.dir = dir;
	}
	
	public void play() {
		play(startPoint);
	}
	public void play(int pos) {
		if (tempClip == null) {
			load();
		}
		if (tempClip == null) { return; }
		if (tempClip.isActive()) { return; }
		tempClip.setFramePosition(pos);
		FloatControl gainControl = (FloatControl) tempClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume); // Reduce volume by 10 decibels.
		tempClip.start();
		playing = true;
	}
	public void loop(int start, int end, int times) {
		if (tempClip == null) {
			load();
		}
		if (tempClip == null) { return; }
		if (tempClip.isActive()) { return; }
		tempClip.setFramePosition(startPoint);
		try {
		tempClip.setLoopPoints(start, end);

		FloatControl gainControl = (FloatControl) tempClip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(volume); // Reduce volume by 10 decibels.
		} catch (Exception e) {}
		tempClip.loop(times);
		playing = true;
	}
	public void stop() {
		if (playing && tempClip != null) {
			tempClip.stop();
			tempClip = null;
		}
		playing = false;
	}
	
	private void load() {
		Line.Info linfo = new Line.Info(Clip.class);
		Line line = null;
		AudioInputStream ais = null;
		try {
			line = AudioSystem.getLine(linfo);
			ais = AudioSystem.getAudioInputStream(new BufferedInputStream(StreamedSound.class.getResourceAsStream("/res/sounds/" + dir)));
			
			tempClip = (Clip) line;
			tempClip.addLineListener(new LineListener() {
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						tempClip.close();
						tempClip = null;
					}
				}
			});
			
			tempClip.open(ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setStartingPos(int pos) {
		startPoint = pos;
	}
	
	
	public String getDir() { return dir; }
	public String getFile() {
		String[] folders = dir.split("/");
		return folders[folders.length-1];
	}
	public boolean isPlaying() { 
		return playing; 
	}
	public long getFramePosition() {
		if (tempClip == null) {
			return 0;
		}
		return tempClip.getLongFramePosition();
	}
	public long getFrameLength() {
		if (tempClip != null) {
			frameLength = tempClip.getFrameLength();
		}
		return Math.max(1, frameLength);
	}
}
