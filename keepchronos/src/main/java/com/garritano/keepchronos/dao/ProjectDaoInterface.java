package com.garritano.keepchronos.dao;

import java.util.List;

import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public interface ProjectDaoInterface {

	List<Project> getAll();

	List<Task> getTasks(Project tempProject);

}
