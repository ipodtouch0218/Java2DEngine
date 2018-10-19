package me.ipodtouch0218.java2dengine.display;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.jogamp.glg2d.GLG2DCanvas;

import me.ipodtouch0218.java2dengine.GameEngine;

public class GameWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static GameWindow instance;
	private static int setWidth = 500, setHeight = 500;
    private static double scaleX = 1, scaleY = 1;
    
	public GameWindow() {	
		instance = this;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		setFocusable(true);
		setContentPane(GameEngine.getInstance().getRenderer());
		setVisible(true);
		pack();
		toFront();		
		requestFocus();
		requestFocusInWindow();
	}
	
	//---Setters---//
	public static void center() {
		instance.setLocationRelativeTo(null);
	}
	public static void setScaleSize(double xScale, double yScale) {
		scaleX = xScale;
		scaleY = yScale;
		
		Dimension size = new Dimension((int) (setWidth*scaleX), (int) (setHeight*scaleY)+47);
		getWindow().setPreferredSize(size);
		getWindow().setSize(size);
	}
	
	public static void setWindowSize(int width, int height) {
		setWidth = width;
		setHeight = height;
		
		Dimension size = new Dimension((int) (width*scaleX), (int) (height*scaleY)+47);
		getWindow().getContentPane().setPreferredSize(size);
		getWindow().pack();
	}
	
	public static void setIcon(Image image) {
		getWindow().setIconImage(image);
	}
	
	public static void lockSize(Dimension size) {
		getWindow().getContentPane().setMaximumSize(size);
	}
	public static void unlockSize() {
		getWindow().getContentPane().setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
	}
	
	public static void setWindowName(String name) { getWindow().setTitle(name); }
	
	//---Getters---//
	public static int getSetWidth() { return setWidth; }
	public static int getSetHeight() { return setHeight; }
	public static double getSetScaleX() { return scaleX; }
	public static double getSetScaleY() { return scaleY; }
	
	public static int getActualWidth() { 
		if (instance == null) return 0;
		return (int) (getWindow().getContentPane().getWidth()); 
	}
	public static int getActualHeight() { 
		if (instance == null) return 0;
		return (int) (getWindow().getContentPane().getHeight()); 
	}
	
	public static void setCursor(Image image, int x, int y) {
		BufferedImage fakeImage = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		fakeImage.getGraphics().drawImage(image, 0, 0, null);
		getWindow().getRootPane().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(fakeImage, new Point(x,y), "reticle"));
	}
	
	public static boolean hasUserFocus() { return getWindow().isFocused(); }
	
	public static int getScaledWidth() { return (int) (getSetWidth()*getSetScaleX()); }
	public static int getScaledHeight() { return (int) (getSetHeight()*getSetScaleY()); }
	
	
	
	//----//
	public static JFrame getWindow() {
		if (instance == null) { new GameWindow(); }
		return instance;
	}
}
