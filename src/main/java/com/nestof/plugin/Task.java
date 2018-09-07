/**
 * 
 */
package com.nestof.plugin;

import java.nio.file.Path;

/**
 * @author csailly
 * @creationDate 8 juil. 2011
 * @Description
 */
public class Task {
	public static String TASK_MOVE = "MOVE";

	public static String TASK_COPY = "COPY";

	public static String TASK_RENAME = "RENAME";

	public static String TASK_CREATE_DIR = "MKDIR";

	private Path source;

	private Path dest;

	private final String taskType;

	public Task(Path source, Path dest, String taskType) {
		super();
		this.source = source;
		this.dest = dest;
		this.taskType = taskType;
	}

	public Path getDest() {
		return this.dest;
	}

	public Path getSource() {
		return this.source;
	}

	public boolean isCopyTask() {
		return TASK_COPY.equals(this.taskType);
	}

	public boolean isMkdirTask() {
		return TASK_CREATE_DIR.equals(this.taskType);
	}

	public boolean isMoveTask() {
		return TASK_MOVE.equals(this.taskType);
	}

	public boolean isRenameTask() {
		return TASK_RENAME.equals(this.taskType);
	}

	public void setDest(Path dest) {
		this.dest = dest;
	}

	public void setSource(Path source) {
		this.source = source;
	}

}
