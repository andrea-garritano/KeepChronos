package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.garritano.keepchronos.model.Project;

public class ProjectDao {

	private EntityManager entityManager;

	public ProjectDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void save(Project project) {
		entityManager.persist(project);
	}

	public List<Project> getAll() {
		return entityManager.createQuery("select p from Project p", Project.class).getResultList();
	}

	public Project findById(Long id) {
		return entityManager.find(Project.class, id);
	}

	public void update(Project project) {
		entityManager.merge(project);
	}
}
