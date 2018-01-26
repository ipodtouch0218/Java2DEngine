package me.ipodtouch0218.java2dengine.display;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import me.ipodtouch0218.java2dengine.GameEngine;
import me.ipodtouch0218.java2dengine.GameObject;
import me.ipodtouch0218.java2dengine.InputHandler;
import me.ipodtouch0218.java2dengine.display.GameWindow.ScaleMode; 

public class GameRenderer extends Canvas {

	private static final long serialVersionUID = 1L;
	
	private static Class<? extends GameObject>[] renderOrder;
	private static GameCamera activeCamera = new GameCamera(500,500);
	private GameEngine engine;
	private static BufferedImage image;
	private static Color backgroundColor = Color.black;
	private static ImageFilter filter;
	
	public GameRenderer(GameEngine engine) {
		new GameWindow(500, 500, "", this);
		addKeyListener(new InputHandler(this));
		addMouseListener(InputHandler.getInputHandler());
		this.engine = engine;
	}
	
	@SuppressWarnings("unchecked")
	public void render() {
		if (image == null) {
			if (activeCamera == null) {
				image = new BufferedImage(GameWindow.getSetWidth(), GameWindow.getSetHeight(), BufferedImage.TYPE_INT_ARGB);
			} else {
				image = new BufferedImage((int) activeCamera.getBounds().getWidth(), (int) activeCamera.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
			}
		}
		if (getBufferStrategy() == null) {
			createBufferStrategy(3);
			return;
		}

		Graphics g = image.getGraphics();
		
		g.setColor(backgroundColor);
		if (activeCamera == null) {
			g.fillRect(0, 0, GameWindow.getActualWidth(), GameWindow.getActualHeight());
		} else {
			g.fillRect(0, 0, (int) activeCamera.getBounds().getWidth(), (int) activeCamera.getBounds().getHeight());
		}
		g.setColor(Color.white);
		
		///
		
		ArrayList<GameObject> toRender = (ArrayList<GameObject>) engine.getAllGameObjects().clone();
		ArrayList<GameObject[]> remaining = new ArrayList<>();
		
		if (renderOrder != null) {
			for (Class<? extends GameObject> render : renderOrder) {
				ArrayList<GameObject> lastRenders = new ArrayList<>();
				Iterator<GameObject> renders = toRender.iterator();
				while (renders.hasNext()) {
					GameObject nextObject = renders.next();
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
		
		///
		
		int w = GameWindow.getSetWidth(), h = GameWindow.getSetHeight();
		if (activeCamera != null) {
			w = (int) activeCamera.getBounds().getWidth();
			h = (int) activeCamera.getBounds().getHeight();
		}
		
		Image finalImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics finalGraphics = finalImage.getGraphics();
		
		finalGraphics.setColor(Color.BLACK);
		finalGraphics.fillRect(0, 0, w, h);
		
		int x = 0, y = 0;
		if (activeCamera != null) {
			x = activeCamera.getX();
			y = activeCamera.getY();
		}
		
		if (GameWindow.getScaleMode() == ScaleMode.INCREASE_SIZE) {
			finalGraphics.drawImage(image.getScaledInstance((int) (GameWindow.getActualWidth()*GameWindow.getSetScaleX()), (int) (GameWindow.getActualHeight()*GameWindow.getSetScaleY()), Image.SCALE_FAST), 0, 0, null);
		}
		if (GameWindow.getScaleMode() == ScaleMode.SCALE_RELATIVE) {
			if (activeCamera == null) {
				finalGraphics.drawImage(image.getScaledInstance(GameWindow.getActualWidth(), GameWindow.getActualHeight(), Image.SCALE_DEFAULT), 0, 0, null);
			} else {
				int xw = activeCamera.getWidth();
				int yh = activeCamera.getHeight();
				finalGraphics.drawImage(image.getSubimage(x, y, xw, yh).getScaledInstance(GameWindow.getActualWidth(), GameWindow.getActualHeight(), Image.SCALE_DEFAULT), 0, 0, null);
			}
		}

		if (filter != null) {
			ImageProducer producer = new FilteredImageSource(finalImage.getSource(), filter);  
			finalImage = Toolkit.getDefaultToolkit().createImage(producer);
		}
		
		getGraphics().drawImage(finalImage, 0, 0, null);
		finalGraphics.dispose();
		g.dispose();
	}
	
	
	private void renderObj(GameObject obj, Graphics g) {
		obj.render(g);
	}


	@SafeVarargs
	public static void setRenderPriority(Class<? extends GameObject>... classes) {
		renderOrder = classes;
	}
	public static GameCamera getActiveCamera() { return activeCamera; }
	public static void setActiveCamera(GameCamera cam) { 
		activeCamera = cam; 
		
		if (activeCamera == null) {
			image = new BufferedImage(GameWindow.getSetWidth(), GameWindow.getSetHeight(), BufferedImage.TYPE_INT_ARGB);
		} else {
			image = new BufferedImage((int) activeCamera.getBounds().getWidth(), (int) activeCamera.getBounds().getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
	}
	public static void setBackgroundColor(Color value) {
		backgroundColor = value;
	}
	public BufferedImage getLastFrame() { return image; }

	public static void setFilter(ImageFilter value) {
		filter = value;
	}  

	
}