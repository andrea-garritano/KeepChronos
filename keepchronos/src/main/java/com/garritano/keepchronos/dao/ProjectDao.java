package com.garritano.keepchronos.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.garritano.keepchronos.model.Project;

public class ProjectDao {
	
	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	
	public void save(Project project) {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(project);
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.close();
		entityManagerFactory.close();
	}
	
}
