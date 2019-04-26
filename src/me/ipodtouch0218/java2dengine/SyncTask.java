package me.ipodtouch0218.java2dengine;

class SyncTask {

	double countdownTimer;
	private TaskRunnable runnable;
	
	SyncTask(TaskRunnable runnable, double timer) {
		this.runnable = runnable;
		this.countdownTimer = timer;
	}
	
	public void tick(double delta) {
		countdownTimer -= delta;
		
		if (countdownTimer <= 0) {
			runnable.runTask();
		}
	}
}
