package me.ipodtouch0218.java2dengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import me.ipodtouch0218.java2dengine.display.GameRenderer;
import me.ipodtouch0218.java2dengine.display.sprite.SpriteAnimation;
import me.ipodtouch0218.java2dengine.input.InputHandler;
import me.ipodtouch0218.java2dengine.object.GameObject;

public class GameEngine implements Runnable {

	private static GameEngine instance;
	
	private GameRenderer display;
	
	private Thread gameThread;
	private boolean gameRunning = false;
	private ArrayList<GameObject> gameObjects = new ArrayList<>();
	
	private double fps;
	private double maxfps = 60;
	
	
	public GameEngine(double maxfps) {
		if (instance != null) { return; }
		instance = this;
		
		this.maxfps = maxfps;
		display = new GameRenderer(this);
	}
	
	public synchronized void start() {
		gameThread = new Thread(this);
		gameRunning = true;
		gameThread.start();
	}
	public synchronized void stop() {
		try {
			gameThread.join();
			gameRunning = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<Double> averageRenderTimes = new ArrayList<>();
	public void run() {
		long lastTick = System.nanoTime();
        double tickdelta = 0;
        
        double updatefps = 0;
        int frames = 0;
        
        while(gameRunning) {
        	long now = System.nanoTime();
            if (maxfps > 0) {
	        	double nsTick = 1e9 / maxfps;
	        	tickdelta = (now - lastTick) / nsTick;
            } else {
            	tickdelta = 1;
            }
        	
        	if (tickdelta >= 1) {
        		double tickDeltaS = (now-lastTick) / 1e9;
        		
        		tick(tickDeltaS);
        		display.render(tickDeltaS);
        		
        		averageRenderTimes.add(tickDeltaS);
        		frames++;
        		
        		updatefps += tickDeltaS * 2d;
        		if (updatefps >= 1) {
        			double average = 0;
        			Iterator<Double> it = averageRenderTimes.iterator();
        			while (it.hasNext()) {
        				average += it.next();
        				it.remove();
        			}
        			average /= frames;
        			fps = 1d/average;
        			updatefps = 0;
        			frames = 0;
        		}
        		tickdelta = 0;
        		lastTick = now;
        	}
        	
        }
        stop();
	}
	
	private void tick(double delta) {
		InputHandler.updateMouse();
		try { 
			for (GameObject object : gameObjects) {
				if (object.isTicking()) {
					object.tick(delta);
				}
			}
		} catch (Exception e) { e.printStackTrace(); }
		tickAnimations(delta);
		addQueuedObjects();
		removeQueuedObjects();
	}
	
	@SuppressWarnings("unchecked")
	private void tickAnimations(double delta) {
		for (SpriteAnimation anims : (ArrayList<SpriteAnimation>) SpriteAnimation.getAnimations().clone()) {
			if (!anims.isStopped()) {
				anims.update(delta);
			}
		}
	}
	private ArrayList<GameObject> toAddObjects = new ArrayList<>();
	private ArrayList<GameObject> toRemoveObjects = new ArrayList<>();
	private synchronized void removeQueuedObjects() {
		if (toRemoveObjects.isEmpty()) { return; }
		GameObject[] list = toRemoveObjects.toArray(new GameObject[]{});
		for (GameObject toRemove : list) {
			toRemove.remove();
			gameObjects.remove(toRemove);
			toRemoveObjects.remove(toRemove);
		}
	}
	private synchronized void addQueuedObjects() {
		if (toAddObjects.isEmpty()) { return; }
		GameObject[] list = toAddObjects.toArray(new GameObject[]{});
		for (GameObject toAdd : list) {
			gameObjects.add(toAdd);
			toAdd.onCreate();
			toAddObjects.remove(toAdd);
		}
	}
	
	
	public synchronized <T extends GameObject> T addGameObject(T object) {
		if (gameObjects.contains(object) || object == null) {
			return null;
		}
		toAddObjects.add(object);
	
		return object;
	}
	public synchronized <T extends GameObject> T addGameObject(T object, double x, double y) {
		addGameObject(object);
		object.setLocation(x, y);
		return object;
	}
	
	public synchronized void removeGameObject(GameObject object) {
		if (object == null) return;
		toRemoveObjects.add(object);
	}
	public synchronized void removeGameObject(UUID uuid) {
		GameObject toRemove = getGameObject(uuid);
		removeGameObject(toRemove);
	}
	
	public void setMaxFps(int fps) {
		maxfps = fps;
	}
	
	//---Getters---//
	@SuppressWarnings("unchecked")
	public ArrayList<GameObject> getAllGameObjects() { return (ArrayList<GameObject>) gameObjects.clone(); }
	@SuppressWarnings("unchecked")
	public <T extends GameObject> ArrayList<T> getAllGameObjects(Class<? extends T> cls) {
		ArrayList<GameObject> objs = (ArrayList<GameObject>) gameObjects.clone();
		Iterator<GameObject> it = (objs).iterator();
		while (it.hasNext()) {
			GameObject obj = it.next();
			if (!cls.isAssignableFrom(obj.getClass())) {
				it.remove();
			}
		}
		return (ArrayList<T>) objs;
	}
	
	public GameObject getGameObject(UUID uuid) { 
		for (GameObject object : gameObjects) {
			if (object.getUniqueId().toString().equals(uuid.toString())) return object;
		}
		return null;
	}
	public double getMaxFPS() { return maxfps; }
	public double getFPS() { return fps; }
	public GameRenderer getRenderer() { return display; }
	//---Static---//
	public static GameEngine getInstance() { return instance; }

}
