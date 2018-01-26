package me.ipodtouch0218.java2dengine.display;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class GameWindow {

	private static int setWidth = 300, setHeight = 300;
    private static double scaleX = 1, scaleY = 1;
    
	private static JFrame window;
	private static ScaleMode scaleMode = ScaleMode.SCALE_RELATIVE;
	
	public GameWindow(int width, int height, String title, GameRenderer engine) {
		JFrame frame = new JFrame(title);
		window = frame;
		
		setWindowSize(width, height);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		frame.setFocusable(true);
		frame.add(engine);
		frame.setVisible(true);
		frame.toFront();
		frame.requestFocus();
		frame.requestFocusInWindow();
	}
	
	//---Setters---//
	public static void setScaleSize(double xScale, double yScale) {
		scaleX = xScale;
		scaleY = yScale;
	}
	
	public static void setWindowSize(int width, int height) {
		setWidth = width;
		setHeight = height;
		
		Dimension size = new Dimension((int) (width*scaleX), (int) (height*scaleY));
		window.setMinimumSize(size);
		window.setPreferredSize(size);
		window.setSize(size);
	}
	
	public static void setIconImage(Image image) {
		window.setIconImage(image);
	}
	
	public static void lockSize(Dimension size) {
		window.setMaximumSize(size);
	}
	public static void unlockSize() {
		window.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	public static void setName(String name) { window.setTitle(name); }
	public static void setScaleMode(ScaleMode mode) { scaleMode = mode; }
	
	//---Getters---//
	public static ScaleMode getScaleMode() { return scaleMode; }
	public static int getSetWidth() { return setWidth; }
	public static int getSetHeight() { return setHeight; }
	public static double getSetScaleX() { return scaleX; }
	public static double getSetScaleY() { return scaleY; }
	
	public static double getActualScaleX() { return (double) getActualWidth()/(double) setWidth; }
	public static double getActualScaleY() { return (double) getActualHeight()/(double) setHeight; }
	public static int getActualWidth() { 
		if (window == null) return 0;
		return (int) (window.getWidth()/scaleX); 
	}
	public static int getActualHeight() { 
		if (window == null) return 0;
		return (int) (window.getHeight()/scaleY); 
	}
	
	public static void setCursor(Image image, int x, int y) {
		BufferedImage fakeImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		fakeImage.getGraphics().drawImage(image, 0, 0, null);
		window.getRootPane().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(fakeImage, new Point(x,y), "reticle"));
	}
	
	public static int getScaledWidth() { return (int) (getSetWidth()*getSetScaleX()*getActualScaleX()); }
	public static int getScaledHeight() { return (int) (getSetHeight()*getSetScaleY()*getActualScaleY()); }
	
	public static JFrame getWindow() { return window; }
	
	
	public static enum ScaleMode {
		SCALE_RELATIVE,
		INCREASE_SIZE,
		DISABLE_SCALING;
	}
}
