package me.ipodtouch0218.java2dengine.sound;

import java.io.BufferedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class StreamedSound {

	private boolean playing;
	
	private String dir;
	private double bufferSize;
	private long startpos;
	
	private long framePos;
	
	public StreamedSound(String dir, double bufferSize) {
		this.dir = dir;
		this.bufferSize = bufferSize;
	}
	
	public void play() {
		try {
		    AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(StreamedSound.class.getResourceAsStream("/res/sounds/" + dir)));
		    AudioFormat audioFormat = audioStream.getFormat();
		    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		    SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		    sourceLine.open(audioFormat);
		    sourceLine.start();
	
		    //right now, the line is set up and all data is availible
		    new Thread(()->{
		    	playing = true;
		    	byte[] buffer = new byte[(int) (audioFormat.getSampleRate()*bufferSize)];
		    	long bytesRead = startpos;
		    	try {
			    	while (playing) {
			    		if (bytesRead != -1) {
				        	bytesRead = audioStream.read(buffer, 0, buffer.length);
							framePos += (bytesRead/audioFormat.getFrameSize());
							
				        	sourceLine.write(buffer, 0, buffer.length);
			    		} else {
			    			playing = false;
			    		}
			    	}
			    	
				    sourceLine.drain();
				    sourceLine.close();
		    	} catch (Exception e) { e.printStackTrace(); }
		    }).start();
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void loop(int times) {
		loop(0, -1, times);
	}
	
	public void loop(long loopStart, long loopEnd, int times) {
		try {
		    AudioInputStream audioStream = AudioSystem.getAudioInputStream(StreamedSound.class.getResourceAsStream("/res/sounds/" + dir));
		    AudioFormat audioFormat = audioStream.getFormat();
		    DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		    SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
		    sourceLine.open(audioFormat);
		    sourceLine.start();
	
		    //right now, the line is set up and all data is availible
		    new Thread(()->{
		    	playing = true;
		    	byte[] buffer = new byte[4096];
		    	long bytesRead = 0;
		    	framePos = startpos;
		    	int loops = 0;
		    	long loopEndPos = (loopEnd == -1 ? audioStream.getFrameLength() : loopEnd);
		    	
		    	try {
		    		audioStream.skip(startpos);
			    	while (playing) {
			    		
			        	bytesRead = audioStream.read(buffer, 0, buffer.length);
						framePos += (bytesRead/audioFormat.getFrameSize());
						
			        	sourceLine.write(buffer, 0, buffer.length);
			    		
			    		
			    		if (bytesRead <= 0 || framePos >= loopEndPos) {
			    			loops++;
			    			if (loops != times) {
//			    				audioStream.close();
			    				audioStream.reset();
			    				audioStream.skip(loopStart);
			    				framePos = loopStart;
			    			} else {
			    				playing = false;
			    			}
			    		}
			    	}
				    sourceLine.close();
		    	} catch (Exception e) { 
		    		e.printStackTrace(); 
		    	}
		    }).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		playing = false;
	}
	
	//--setters--//
	public void setStartPos(long startpos) {
		this.startpos = startpos;
	}
	
	//--getters--//
	public boolean isPlaying() { return playing; }
	public long getFramePos() { return framePos; }
}
