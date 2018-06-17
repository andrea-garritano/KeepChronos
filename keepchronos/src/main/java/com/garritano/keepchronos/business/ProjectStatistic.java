package com.garritano.keepchronos.business;

import java.util.List;

import com.garritano.keepchronos.dao.ProjectDaoInterface;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class ProjectStatistic {
	private ProjectDaoInterface projectDaoInterface;

	public ProjectStatistic(ProjectDaoInterface projectDaoInterface) {
		super();
		this.projectDaoInterface = projectDaoInterface;
	}

	public int getNumberOfProject() {
		List<Project> projects = projectDaoInterface.getAll();

		return projects.size();
	}

	public int getTotalDuration(Project tempProject) {
		List<Task> tasks = projectDaoInterface.getTasks(tempProject);
		int sum = 0;
		for (Task task : tasks) {
			sum += task.getDuration();
		}
		return sum;
	}
}
