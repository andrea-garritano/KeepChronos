package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class ProjectDao implements ProjectDaoInterface{

	private EntityManager entityManager;

	public ProjectDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(Project project) {
		entityManager.getTransaction().begin();
		entityManager.persist(project);
		entityManager.getTransaction().commit();
	}

	public List<Project> getAll() {
		return entityManager.createQuery("select p from Project p", Project.class).getResultList();
	}

	public Project findById(Long id) {
		return entityManager.find(Project.class, id);
	}

	public void update(Project project) {
		entityManager.getTransaction().begin();
		entityManager.merge(project);
		entityManager.getTransaction().commit();
	}

	@Override
	public List<Task> getTasks(Project tempProject) {
		return entityManager.createQuery("select t from Task t where project_id = :project_id", Task.class)
				.setParameter("project_id", tempProject.getId()).getResultList();
	}
}
