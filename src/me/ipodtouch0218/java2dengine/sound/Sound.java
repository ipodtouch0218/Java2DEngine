package me.ipodtouch0218.java2dengine.sound;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.UnsupportedAudioFileException;

import me.ipodtouch0218.java2dengine.input.InputHandler;

public class Sound implements Cloneable {

	private static HashMap<String, Sound> cachedSounds = new HashMap<>();

	private AudioFormat format;
	private byte[] rawdata;
	private long length;

	private boolean playMultiple;
	private Clip clip;
	private String dir;
	
	private boolean playing;
	
	private Sound(String dir, boolean playMultiple) {
		System.out.println("loaded sound: " + dir);
		this.dir = dir;
		this.playMultiple = playMultiple;
		try {   
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("/res/sounds/" + dir)); 
            format = inputStream.getFormat();
            length = inputStream.getFrameLength();
            
            rawdata = new byte[(int) (inputStream.getFrameLength() * format.getFrameSize())];
            DataInputStream dis = new DataInputStream(inputStream);
            dis.readFully(rawdata);
            dis.close();
            
            clip = getClipFromByteArray(rawdata, format, length, false);
        } catch (IOException | UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        }
    
		cachedSounds.put(dir, this);
	}
	
	public void stop() { 
		playing = false;
		if (clip == null || !clip.isOpen()) { return; }
		clip.stop(); 
	}
	public void play() {
		if (clip == null || !clip.isOpen()) { return; }
		if (clip.isActive() && playMultiple) {
			if (clip.getLongFramePosition() < 3000) { return; }
			Clip newClip = getClipFromByteArray(rawdata, format, length, true);
			newClip.start();
			return;
		}
		clip.setFramePosition(0);
		clip.start();
		playing = true;
		LineListener listener = new LineListener() {
			public void update(LineEvent event) {
				if (event.getType() != Type.STOP) {
					return;
				}
				playing = false;
			}
		};
		clip.addLineListener(listener);
	}
	
	public void setVolume(int amount) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue((float) (gainControl.getMaximum()*(amount/100d)));
	}
	
	//---Getters---//
	public String getName() { return dir; }
	public String getFileName() {
		String[] split = dir.split("/");
		String output = split[split.length-1];
		output.replaceAll("\\..+", "");
		return output;
	}
	public Clip getClip() { return clip; }
	public boolean isPlaying() { return playing; }
	
	//---Static---//
	public static Sound getSound(String dir, boolean playMultiple) {
		if (cachedSounds.containsKey(dir)) {
			return cachedSounds.get(dir);
		}
		return new Sound(dir, playMultiple);
	}
	public static boolean isSound(String dir) {
		try {   
            AudioSystem.getAudioInputStream(InputHandler.getInputHandler().getClass().getResource("/res/sounds/" + dir));
            return true;
		} catch (UnsupportedAudioFileException | IOException | NullPointerException e) {
			return false;
		}
	}
	private static Clip getClipFromByteArray(byte[] sample, AudioFormat format, long framelength, boolean dispose) {        
		try {
	        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(sample));
	        AudioInputStream audioInputStream = new AudioInputStream(dis, format, framelength);
			Clip clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        if (dispose) {
		        clip.addLineListener(new LineListener() {
		        	@Override
		        	public void update(LineEvent event) {
		        		if (event.getType() == LineEvent.Type.STOP) {
		        			clip.close();
		        		}
		        	}
		        	
		        });
	        }
	        return clip;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void unloadSound(String loc) {
		cachedSounds.remove(loc);
	}
}
