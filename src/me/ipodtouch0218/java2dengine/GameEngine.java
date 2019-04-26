package me.ipodtouch0218.java2dengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import me.ipodtouch0218.java2dengine.display.GameRenderer;
import me.ipodtouch0218.java2dengine.display.sprite.SpriteAnimation;
import me.ipodtouch0218.java2dengine.event.EventHandler;
import me.ipodtouch0218.java2dengine.input.InputHandler;
import me.ipodtouch0218.java2dengine.object.GameObject;

public class GameEngine implements Runnable {

	private static Thread runningThread;
	
	private static GameRenderer display = new GameRenderer(); //deals with rendering all objects, also contains the window the final image is drawn to.
	private static boolean gameRunning = false; //if the game *should* be running. does not indicate if the game is actually running or not at the moment, only if it should be by the next loop.
	
	private static ArrayList<GameObject> gameObjects = new ArrayList<>(); //generic list of all objects. looped through for rendering and ticking each frame.
	private static ArrayList<EventHandler> eventHandlers = new ArrayList<>(); //list of event handlers, sorted by priority.
	private static ArrayList<SyncTask> syncTasks = new ArrayList<>(); //list of all tasks. tasks are iterated through each tick method, and will call its runnable if the timer <= 0
	
//	private static ArrayList<GameObject> toAddObjects = new ArrayList<>(); //list of all objects that should be created next tick.
//	private static ArrayList<GameObject> toRemoveObjects = new ArrayList<>(); //list of all objects that should be removed on the tick
	
	private static double fps; //the current framerate of the engine
	private static double maxfps = 60; //the maximum allowed framerate of the engine. 
	private static boolean allowSlowdown; //if the game shoud slow-down or skip frames
	
	private static double timedelta; //the time delta since the last renderer frame, in seconds. used to keep all objects consistant across mutliple framerates.
	
	private GameEngine() {}
	
	public void run() {
		
		ArrayList<Double> averageRenderTimes = new ArrayList<>();
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
        		if (!allowSlowdown) {
        			timedelta = (now-lastTick) / 1e9;
        		} else {
        			timedelta = 1/maxfps;
        		}
        		
        		tick(timedelta);
        		display.render(timedelta);
        		
        		averageRenderTimes.add(timedelta);
        		frames++;
        		
        		updatefps += timedelta * 2d; //update the current fps twice per second
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
	
	//main tick method, called once every frame.
	private void tick(double delta) {
		InputHandler.updateMouse();
		for (GameObject object : gameObjects.toArray(new GameObject[]{})) {
			try {
				object.tick(delta);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tickAnimations(delta);
		tickTasks(delta);
	}
	
	
	private static void tickAnimations(double delta) {
		for (SpriteAnimation anims : SpriteAnimation.getAnimations()) {
			if (!anims.isStopped()) {
				anims.update(delta);
			}
		}
	}
	
	public static void tickTasks(double delta) {
		Iterator<SyncTask> tasks = syncTasks.iterator();
		while (tasks.hasNext()) {
			SyncTask task = tasks.next();
			if (task == null) { continue; }
			task.tick(delta);
			if (task.countdownTimer <= 0) {
				tasks.remove();
			}
		}
	}	
	
	//---STATIC METHODS---//
	
	//Engine Start, Stop, and Speed
	public static void start() {
		InputHandler.getInputHandler();
		System.setProperty("sun.java2d.opengl", "True");
		if (gameRunning || (runningThread != null && runningThread.isAlive()))  {
			return;
		}
		gameRunning = true;
		runningThread = new Thread(new GameEngine());
		runningThread.start();
	}
	public static synchronized void stop() {
		if (runningThread != null && runningThread.isAlive()) {
			try {
				runningThread.join();
			} catch (InterruptedException e) {}
		}
		gameRunning = false;
	}
	
	//Engine Rendering
	public static GameRenderer getRenderer() {
		return display;
	}
	public static void setMaxFPS(int newMaxFPS) {
		maxfps = newMaxFPS;
	}
	public static void allowSlowdown(boolean value) {
		allowSlowdown = value;
	}
	
	//Add-Remove Objects
	public static synchronized <T extends GameObject> T addGameObject(T object) {
		if (object == null || gameObjects.contains(object)/* || toAddObjects.contains(object)*/) { //object already exists added, do not add it again as it would tick twice per loop
			return null; 
		}
//		toAddObjects.add(object);
		object.onCreate();
		gameObjects.add(object);
		System.out.println("add: " + object);
		return object;
	}
	public static synchronized <T extends GameObject> T addGameObject(T object, double x, double y) {
		object.setLocation(x, y);
		addGameObject(object);
		return object;
	}
	public static synchronized void removeGameObject(GameObject object) {
		if (object == null) return;
//		toRemoveObjects.add(object);
		object.onRemove();
		gameObjects.remove(object);
		System.out.println("remove: " + object);
	}
	public static synchronized void removeGameObject(UUID uuid) {
		GameObject toRemove = getGameObject(uuid);
		removeGameObject(toRemove);
	}
	
	//Get Game Objects
	public static GameObject getGameObject(UUID uuid) { 
		for (GameObject object : gameObjects) {
			if (object.getUniqueId().equals(uuid)) return object;
		}
		return null;
	}
	public static ArrayList<GameObject> getAllGameObjects() { 
		return gameObjects; 
	}
	@SuppressWarnings("unchecked")
	public static <T extends GameObject> ArrayList<T> getAllGameObjects(Class<? extends T> cls) {
		ArrayList<GameObject> matches = new ArrayList<>(); //objects where "cls" = getClass *OR* "cls" is a superclass of getClass
		
		Iterator<GameObject> iterator = gameObjects.iterator();
		while (iterator.hasNext()) {
			GameObject objToCheck = iterator.next();
			if (cls.isAssignableFrom(objToCheck.getClass())) {
				matches.add(objToCheck);
			}
		}
		
		return (ArrayList<T>) matches; //matches has to contain classes assignable to cls, so we can safely cast.
	}
	
	//Sync Tasks
	public static void scheduleSyncTask(TaskRunnable runnable, double timer) {
		syncTasks.add(new SyncTask(runnable, timer));
	}
	public static void removeTask(SyncTask task) {
		syncTasks.remove(task);
	}
	
	//Value Getters
	public static double getFPS() { return fps; }
	public static double getMaxFPS() { return maxfps; }
}
