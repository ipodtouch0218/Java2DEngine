package me.ipodtouch0218.java2dengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import me.ipodtouch0218.java2dengine.display.GameRenderer;

public class GameEngine implements Runnable {

	private static GameEngine instance;
	
	private GameRenderer display;
	
	private Thread gameThread;
	private boolean gameRunning = false;
	private ArrayList<GameObject> gameObjects = new ArrayList<>();
	private int fps;
	private boolean logFps;
	private boolean frameskip;
	private double tickRate;
	
	public GameEngine(double tickrate) {
		if (instance != null) { return; }
		
		this.tickRate = tickrate;
		instance = this;
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
	
	
	public void run() {
		long lastTime = System.nanoTime();
        double ns = 1e9 / tickRate;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(gameRunning) {
        	long now = System.nanoTime();
        	delta = (now - lastTime) / ns;
        	while(delta >= 1) {
        		lastTime = now;
        		tick();
        		if (!frameskip) {
        			display.render();
        			frames++;
        		}
        		if (frameskip && delta < 2) {
        			display.render();
        			frames++;
        		}
        		delta--;
        	}
        	
        	if (System.currentTimeMillis() - timer > 1000) {
        		timer += 1000;
        		fps = frames;
        		frames = 0;
        		if (logFps) {
        			System.out.println(fps);
        		}
        	}
        }
        stop();
	}
	
	@SuppressWarnings("unchecked")
	private void tick() {
		((ArrayList<GameObject>) gameObjects.clone()).forEach(obj -> obj.tick());
	}
	
	
	public <T extends GameObject> T addGameObject(T object) {
		if (gameObjects.contains(object) || object == null) {
			return null;
		}
		gameObjects.add(object);
	

		return object;
	}
	public <T extends GameObject> T addGameObject(T object, double x, double y) {
		if (gameObjects.contains(object) || object == null) {
			return null;
		}
		object.setLocation(x, y);
		gameObjects.add(object);
		
		
		return object;
	}
	
	public void removeGameObject(GameObject object) {
		if (object == null) return;
		object.onRemove();
		gameObjects.remove(object);
	}
	public void removeGameObject(UUID uuid) {
		GameObject toRemove = getGameObject(uuid);
		if (toRemove == null) return;
		toRemove.onRemove();
		gameObjects.remove(toRemove);
	}
	
	public void logFramerate(boolean value) {
		logFps = value;
	}
	public void enableFrameskip(boolean value) {
		frameskip = value;
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
	public int getFPS() { return fps; }
	public double getTickrate() { return tickRate; }
	
	//---Static---//
	public static GameEngine getInstance() { return instance; }
}
