package me.ipodtouch0218.java2dengine.display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.JComponent;

import me.ipodtouch0218.java2dengine.GameEngine;
import me.ipodtouch0218.java2dengine.display.ui.UICanvas;
import me.ipodtouch0218.java2dengine.object.GameObject; 

public class GameRenderer extends JComponent {

	private static final long serialVersionUID = 1L;
	private static final String os = System.getProperty("os.name").toLowerCase();
	
	private static GameRenderer instance;
	
	private Class<? extends GameObject>[] renderOrder;
	private GameCamera activeCamera = new GameCamera(500,500);
	private GameEngine engine;
	private Color backgroundColor = Color.black;
	private static VolatileImage background;
	private boolean showSize = true;
	private double resizeDecay;
	
	private VolatileImage renderImage;
	private UICanvas canvas; 
	
	private int oldx,oldy;
	
	public GameRenderer(GameEngine engine) {
		this.engine = engine;
		instance = this;
	}
	
	public void render(double delta) {
		if (renderImage == null || renderImage.validate(gc) == VolatileImage.IMAGE_INCOMPATIBLE || renderImage.getWidth() != GameWindow.getSetWidth() || renderImage.getHeight() != GameWindow.getSetHeight()) {
			renderImage = createVolatile(GameWindow.getSetWidth(), GameWindow.getSetHeight(), false);
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
		renderUI(g);
		
		if (background != null) {
			g.setComposite(AlphaComposite.DstOver);
			g.drawImage(background, 0, 0, GameWindow.getSetWidth(), GameWindow.getSetHeight(), null);
			g.setComposite(AlphaComposite.SrcOver);
		}
		
		///
		
		if (oldx != GameWindow.getActualWidth() || oldy != GameWindow.getActualHeight()) {
			resizeDecay = 2;
		}
		resizeDecay -= delta;
		renderResizeInfo(g);
		
		
		///
		g.dispose();
		getGraphics().drawImage(renderImage, 0, 0, GameWindow.getActualWidth(), GameWindow.getActualHeight(), null);
		oldx = GameWindow.getActualWidth();
		oldy = GameWindow.getActualHeight();
		if (os.indexOf("mac") >= 0) {
			GameWindow.getWindow().repaint();
		}
	}
	private void renderResizeInfo(Graphics2D g) {
		if (showSize && resizeDecay > 0) {
			g.setColor(Color.WHITE);
			g.drawString(GameWindow.getActualWidth() + " x " + GameWindow.getActualHeight(), 4, GameWindow.getSetHeight()-12);
		}
	}
	
	private void renderGameObjects(Graphics2D g) {
		ArrayList<GameObject> toRender = engine.getAllGameObjects();
		ArrayList<GameObject[]> remaining = new ArrayList<>();
		
		if (renderOrder != null) {
			for (Class<? extends GameObject> render : renderOrder) {
				ArrayList<GameObject> lastRenders = new ArrayList<>();
				Iterator<GameObject> renders = toRender.iterator();
				whileloop:
				while (renders.hasNext()) {
					GameObject nextObject = renders.next();
					if (nextObject == null || !nextObject.isRendering()) { continue whileloop; }
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
		obj.render(g);
	}
	
	private void renderUI(Graphics2D g) {
		if (canvas != null)
		canvas.render(g);
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
		if (instance == null) { return; }
		instance.renderOrder = classes; 
	}
	public static void setActiveCamera(GameCamera cam) { 
		if (instance == null) { return; }
		instance.activeCamera = cam; 
	}
	public static void setBackgroundColor(Color value) { 
		if (instance == null) { return; }
		instance.backgroundColor = value;
	}

	//getters
	public static GameCamera getActiveCamera() { 
		if (instance == null) { return null; }
		return instance.activeCamera; 
	}
	public static VolatileImage getLastFrame() { 
		if (instance == null) { return null; }
		return instance.renderImage; 
	}
	
	//static methods
	public static final GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	public static VolatileImage createVolatile(int width, int height, boolean translucent) {
		VolatileImage newImg = gc.createCompatibleVolatileImage(width, height, (translucent ? VolatileImage.TRANSLUCENT : VolatileImage.BITMASK));
		Graphics2D newG = newImg.createGraphics();
		newG.setComposite(AlphaComposite.Clear);
		newG.fillRect(0, 0, width, height);
		newG.dispose();
		return newImg;
	}
	public static void setBackground(VolatileImage bg) {
		background = bg;
	}

	public static void removeBackground() {
		background = null;
	}
}