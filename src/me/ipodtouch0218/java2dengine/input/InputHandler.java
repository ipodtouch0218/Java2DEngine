package me.ipodtouch0218.java2dengine.input;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import me.ipodtouch0218.java2dengine.GameEngine;
import me.ipodtouch0218.java2dengine.display.GameRenderer;
import me.ipodtouch0218.java2dengine.display.GameWindow;

public class InputHandler implements MouseListener {
	
	private static ArrayList<KeyStroke> pressedStrokes = new ArrayList<>();
	
	private static InputHandler instance;
	private static ArrayList<KeyStroke> savedStrokes = new ArrayList<>();
	private static InputMap input = null; 
	private static ActionMap act = null;
	
	private static Point2D.Double mousePosWorld;
	private static Point2D.Float mousePosUI;
	private static boolean mouseLeftDown, mouseRightDown;
	
	private InputHandler(GameRenderer renderer) {
		input = renderer.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW); 
		act = renderer.getActionMap();
	}
	
	public static void assignKeyStroke(KeyStroke stroke) {
		input.put(stroke, stroke.toString());
		act.put(stroke.toString(), toAddAction(stroke));
		KeyStroke releaseStr = KeyStroke.getKeyStroke(stroke.getKeyCode(), stroke.getModifiers(), true);
		input.put(releaseStr, releaseStr.toString());
		act.put(releaseStr.toString(), toRemoveAction(stroke));
		savedStrokes.add(stroke);
	}
	
	public static InputHandler getInputHandler() {
		if (instance == null) { instance = new InputHandler(GameEngine.getRenderer()); }
		return instance;
	}

	
	public static ArrayList<KeyStroke> getAllKeys() { return pressedStrokes; }
	
	private static KeyStroke getStroke(int code, int modifiers) {
		return KeyStroke.getKeyStroke(code, modifiers);
	}
	public static boolean isKeyPressed(int key) {
		return isKeyPressed(key, 0);
	}
	public static boolean isKeyPressed(int key, int modifier) {
		return isKeyPressed(getStroke(key, modifier));
	}
	private static boolean isKeyPressed(KeyStroke stroke) {
		if (input.keys() == null || !savedStrokes.contains(stroke)) {
			assignKeyStroke(stroke);
		}
		
		return pressedStrokes.contains(stroke);
	}
	
	private static Action toAddAction(KeyStroke stroke) {
		return new AbstractAction() {
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent arg0) {
				if (!pressedStrokes.contains(stroke)) {
					pressedStrokes.add(stroke);
				}
			}
		};
	}
	
	private static Action toRemoveAction(KeyStroke stroke) {
		return new AbstractAction() {
			private static final long serialVersionUID = 1L;
			public void actionPerformed(ActionEvent e) {
				pressedStrokes.remove(stroke);
			}
		};
	}
	
	//--others
	
	
	
	//---Getters---//
	public static Point2D.Double getMousePosInWorld() { return mousePosWorld; }
	public static Point2D.Float getMousePosInUI() { return mousePosUI; }
	public static boolean isLeftMouseDown() { return mouseLeftDown; }
	public static boolean isRightMouseDown() { return mouseRightDown; }
	public static boolean isMouseInWindow() { 
		JFrame window = new GameWindow();
		Rectangle rec = new Rectangle((int) window.getLocation().getX(), (int) window.getLocation().getY(), window.getWidth(), window.getHeight());
		return rec.contains(MouseInfo.getPointerInfo().getLocation()); 
	}
	
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
	
	//---others---//
	public static void updateMouse() {
		if (GameWindow.hasUserFocus()) {
			mousePosUI = calcMouseUIPos();
			mousePosWorld = calcMouseWorldPos();
		}
	}
	
	private static float mouseX, mouseY;
	private static Point2D.Float calcMouseUIPos() {
		if (MouseInfo.getPointerInfo() != null) {
			Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
			
			mouseX = (float) (mouseLoc.getX()-GameWindow.getWindow().getX())/GameWindow.getSetWidth();
			mouseY = (float) (mouseLoc.getY()-GameWindow.getWindow().getY())/GameWindow.getSetHeight();
		}
		
		return new Point2D.Float(mouseX, mouseY);
	}
	
	private static Point2D.Double calcMouseWorldPos() {
		if (MouseInfo.getPointerInfo() == null) { return new Point2D.Double(-1,-1); }
		int camX = GameRenderer.getActiveCamera() == null ? 0 : GameRenderer.getActiveCamera().getX();
		int camY = GameRenderer.getActiveCamera() == null ? 0 : GameRenderer.getActiveCamera().getY();
		
		int camW = GameRenderer.getActiveCamera() == null ? GameWindow.getSetWidth() : GameRenderer.getActiveCamera().getWidth();
		int camH = GameRenderer.getActiveCamera() == null ? GameWindow.getSetHeight() : GameRenderer.getActiveCamera().getHeight();
		
		Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
		
		double mouseX = (camX+(mouseLoc.getX()-GameWindow.getWindow().getLocationOnScreen().getX())*((double) camW/(double) GameWindow.getScaledWidth()));
		double mouseY = (camY+(mouseLoc.getY()-GameWindow.getWindow().getLocationOnScreen().getY())*((double) camH/(double) GameWindow.getScaledHeight()));
		return new Point2D.Double(mouseX, mouseY);
	}
}
