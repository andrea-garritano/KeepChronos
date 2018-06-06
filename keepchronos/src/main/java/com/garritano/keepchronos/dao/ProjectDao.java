package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;

import com.garritano.keepchronos.model.Project;

public class ProjectDao {
	
	public ProjectDao(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	protected EntityManager entityManager;
	
	public void save(Project project) {
		entityManager.persist(project);
	}
	
	public List<Project> getAll() {
		return entityManager.createQuery("select p from Project p").getResultList();
	}
}
