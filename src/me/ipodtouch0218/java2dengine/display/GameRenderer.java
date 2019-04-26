package me.ipodtouch0218.java2dengine.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JComponent;

import me.ipodtouch0218.java2dengine.GameEngine;
import me.ipodtouch0218.java2dengine.object.GameObject; 

public class GameRenderer extends JComponent {

	private static final long serialVersionUID = 1L;
	
	private static Class<? extends GameObject>[] renderOrder;
	private static GameCamera activeCamera = new GameCamera(500,500);
	private static Color backgroundColor = Color.black;
	private static VolatileImage background;
	
	private static boolean showSize = true;
	private static double resizeDecay;
	
	private static VolatileImage renderImage;
	private static int oldx, oldy;
	
	
	public void render(double delta) {
		if (renderImage == null || renderImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE || renderImage.getWidth() != GameWindow.getSetWidth() || renderImage.getHeight() != GameWindow.getSetHeight()) {
			renderImage = createVolatile(GameWindow.getSetWidth(), GameWindow.getSetHeight(), true);
			renderImage.setAccelerationPriority(1);
		}

		Graphics2D g = renderImage.createGraphics();
		
		if (background == null) {
			g.setColor(backgroundColor);
			g.fillRect(0, 0, GameWindow.getSetWidth(), GameWindow.getSetHeight());
			g.setColor(Color.white);
		} else {
			g.drawImage(background, 0, 0, GameWindow.getSetWidth(), GameWindow.getSetHeight(), null);
		}
		
		///
		
		renderGameObjects(g);
		
		if (background != null) {
			g.setComposite(AlphaComposite.DstOver);
			g.drawImage(background, 0, 0, GameWindow.getSetWidth(), GameWindow.getSetHeight(), null);
			g.setComposite(AlphaComposite.SrcOver);
		}
		
		///
		
		renderResizeInfo(g, delta);
		
		///
		g.dispose();
		getGraphics().drawImage(renderImage, 0, 0, GameWindow.getActualWidth(), GameWindow.getActualHeight(), null);
		oldx = GameWindow.getActualWidth();
		oldy = GameWindow.getActualHeight();

		repaint();
	}
	
	private void renderResizeInfo(Graphics2D g, double delta) {
		if (oldx != GameWindow.getActualWidth() || oldy != GameWindow.getActualHeight()) {
			resizeDecay = 2;
		}
		if (showSize && resizeDecay > 0) {
			resizeDecay -= delta;
			g.setColor(Color.WHITE);
			g.drawString(GameWindow.getActualWidth() + " x " + GameWindow.getActualHeight(), 4, GameWindow.getSetHeight()-12);
		}
	}
	
	private void renderGameObjects(Graphics2D g) {
		@SuppressWarnings("unchecked")
		ArrayList<GameObject> toRender = (ArrayList<GameObject>) GameEngine.getAllGameObjects().clone();
		ArrayList<GameObject[]> remaining = new ArrayList<>();
		
		if (renderOrder != null) {
			for (Class<? extends GameObject> render : renderOrder) {
				ArrayList<GameObject> lastRenders = new ArrayList<>();
				Iterator<GameObject> renders = toRender.iterator();
				whileloop:
				while (renders.hasNext()) {
					GameObject nextObject = renders.next();
					if (nextObject == null) { continue whileloop; }
					if (render.isAssignableFrom(nextObject.getClass())) { 
						lastRenders.add(nextObject);
						renders.remove();
					}
				}
				remaining.add(0, lastRenders.toArray(new GameObject[]{}));
			}
		}
		toRender.forEach(ob -> renderObj(ob, g));
		
		for (GameObject[] remains : remaining) {
			Arrays.asList(remains).forEach(ob -> renderObj(ob, g));
		}
	}
	
	private void renderObj(GameObject obj, Graphics2D g) {
		if (obj == null) { return; }
		try {
			obj.render(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//---------//
	
	//methods
	public static void clearImage(VolatileImage img) {
		Graphics2D newG = img.createGraphics();
		newG.setComposite(AlphaComposite.Clear);
		newG.fillRect(0, 0, img.getWidth(), img.getHeight());
		newG.dispose();
	}
	
	//setters
	
	@SafeVarargs
	public static void setRenderPriority(Class<? extends GameObject>... classes) {
		renderOrder = classes; 
	}
	public static void setActiveCamera(GameCamera cam) { 
		activeCamera = cam; 
	}
	public static void setBackgroundColor(Color value) { 
		backgroundColor = value;
	}

	//getters
	public static GameCamera getActiveCamera() { 
		return activeCamera; 
	}
	public static VolatileImage getLastFrame() { 
		return renderImage; 
	}
	
	//static methods
	public static final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	public static VolatileImage createVolatile(int width, int height, boolean translucent) {
		VolatileImage newImg = gc.createCompatibleVolatileImage(width, height, (translucent ? VolatileImage.TRANSLUCENT : VolatileImage.BITMASK));
		Graphics2D newG = newImg.createGraphics();
		newG.setComposite(AlphaComposite.Clear);
		newG.fillRect(0, 0, width, height);
		newG.setComposite(AlphaComposite.SrcOver);
		newG.dispose();
		return newImg;
	}
	public static void setBackground(VolatileImage bg) {
		background = bg;
	}

	public static void removeBackground() {
		background = null;
	}
	
	//other
	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(renderImage, 0, 0, GameWindow.getActualWidth(), GameWindow.getActualHeight(), null);
	}
}