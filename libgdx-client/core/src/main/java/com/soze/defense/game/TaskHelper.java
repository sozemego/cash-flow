package com.soze.defense.game;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class TaskHelper {

	public static void runTask(Runnable task) {
		Timer.post(new Task() {
			@Override
			public void run() {
				task.run();
			}
		});
	}

}
