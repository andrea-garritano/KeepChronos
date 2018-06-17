package com.garritano.keepchronos.business;

import java.util.List;

import com.garritano.keepchronos.dao.ProjectDaoInterface;
import com.garritano.keepchronos.model.Project;

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
}
