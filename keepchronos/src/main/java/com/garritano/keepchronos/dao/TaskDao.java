package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class TaskDao {

	private EntityManager entityManager;

	public TaskDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(Task task) {
		entityManager.persist(task);
	}

	public List<Task> getAll() {
		return entityManager.createQuery("select p from Task p", Task.class).getResultList();
	}

	public Task findById(Long id) {
		return entityManager.find(Task.class, id);
	}

	public void update(Task task) {
		entityManager.merge(task);
	}

	public Project getProjectByTaskId(Long id) {
		Task tempTask = entityManager.find(Task.class, id);
		if (tempTask != null) {
			return tempTask.getProject();
		}
		return null;
	}
}
