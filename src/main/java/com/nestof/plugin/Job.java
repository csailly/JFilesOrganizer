/**
 * 
 */
package com.nestof.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author csailly
 * @creationDate 8 juil. 2011
 * @Description
 */
public class Job {
	private List<Task> tasks;

	private Calendar calendar;

	public Job() {
		calendar = Calendar.getInstance();
		tasks = new ArrayList<Task>();
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
