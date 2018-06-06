package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.garritano.keepchronos.model.Project;

public class ProjectDao {
	
	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	
	public void save(Project project) {
		initEm();
		entityManager.persist(project);
		closeEm();
	}
	
	public List<Project> getAll() {
		initEm();
		List<Project> result = entityManager.createQuery("select p from Project p").getResultList();
		closeEm();
		
		if(result.isEmpty()) {
			return null;
		}
		return result;
	}
	
	
	private void initEm() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();
	}
	private void closeEm() {
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.close();
		entityManagerFactory.close();
	}
	
}
