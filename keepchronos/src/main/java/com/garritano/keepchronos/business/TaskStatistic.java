package com.garritano.keepchronos.business;

import java.util.List;

import com.garritano.keepchronos.dao.TaskDaoInterface;
import com.garritano.keepchronos.model.Task;

public class TaskStatistic {
	private TaskDaoInterface taskDaoInterface;
	
	public TaskStatistic(TaskDaoInterface taskDaoInterface) {
		super();
		this.taskDaoInterface = taskDaoInterface;
	}

	public int getNumberOfTask() {
		List<Task> tasks = taskDaoInterface.getAll();
		
		return tasks.size();
	}

}
