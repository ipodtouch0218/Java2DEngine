package me.ipodtouch0218.java2dengine;

import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import me.ipodtouch0218.java2dengine.display.GameRenderer;
import me.ipodtouch0218.java2dengine.display.GameWindow;

public class InputHandler extends KeyAdapter implements MouseListener {

	private ArrayList<Integer> pressedKeys = new ArrayList<>();
	private static InputHandler instance;
	private Thread mouseUpdate;
	
	public InputHandler(GameRenderer renderer) {
		instance = this;
		mouseUpdate = new Thread() { 
			public void run() {
				while (true) { 
					if (GameWindow.getWindow().isFocused()) {
						
						int camX = GameRenderer.getActiveCamera() == null ? 0 : GameRenderer.getActiveCamera().getX();
						int camY = GameRenderer.getActiveCamera() == null ? 0 : GameRenderer.getActiveCamera().getY();
						
						int camW = GameRenderer.getActiveCamera() == null ? GameWindow.getSetWidth() : GameRenderer.getActiveCamera().getWidth();
						int camH = GameRenderer.getActiveCamera() == null ? GameWindow.getSetHeight() : GameRenderer.getActiveCamera().getHeight();
						
						mouseX = (int) (camX+(MouseInfo.getPointerInfo().getLocation().getX()-renderer.getLocationOnScreen().getX())*((double) camW/(double) GameWindow.getScaledWidth()));
						mouseY = (int) (camY+(MouseInfo.getPointerInfo().getLocation().getY()-renderer.getLocationOnScreen().getY())*((double) camH/(double) GameWindow.getScaledHeight()));
					}
				}
			}
		};
		mouseUpdate.start();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	    if (!pressedKeys.contains(e.getKeyCode())) {
			pressedKeys.add(e.getKeyCode());
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (pressedKeys.contains(e.getKeyCode())) {
			pressedKeys.remove((Integer) e.getKeyCode());
			return;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isKeyPressed(String key) {
		for (int code : ((ArrayList<Integer>) instance.pressedKeys.clone())) {
			if (KeyEvent.getKeyText(code).equalsIgnoreCase(key)) return true;
		}
		return false;
	}
	public static ArrayList<Integer> getDownKeys() {
		return instance.pressedKeys;
	}
	
	public static InputHandler getInputHandler() {
		return instance;
	}

	//---Getters---//
	public static int getMouseX() { return mouseX; }
	public static int getMouseY() { return mouseY; }
	public static boolean isLeftMouseDown() { return mouseLeftDown; }
	public static boolean isRightMouseDown() { return mouseRightDown; }
	public static boolean isMouseInWindow() { 
		JFrame window = GameWindow.getWindow();
		Rectangle rec = new Rectangle((int) window.getLocation().getX(), (int) window.getLocation().getY(), window.getWidth(), window.getHeight());

		return rec.contains(MouseInfo.getPointerInfo().getLocation()); 
	}
	
	private static int mouseX, mouseY;
	private static boolean mouseLeftDown, mouseRightDown;
	
	//---Listeners---//
	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}

	public void mousePressed(MouseEvent arg0) {
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			mouseLeftDown = true;
		} else if (SwingUtilities.isRightMouseButton(arg0)) {
			mouseRightDown = true;
		}
	}

	public void mouseReleased(MouseEvent arg0) {
		if (SwingUtilities.isLeftMouseButton(arg0)) {
			mouseLeftDown = false;
		} else if (SwingUtilities.isRightMouseButton(arg0)) {
			mouseRightDown = false;
		}
	}
}
