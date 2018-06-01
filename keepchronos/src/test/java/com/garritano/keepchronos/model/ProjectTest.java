package com.garritano.keepchronos.model;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import com.garritano.keepchronos.model.Project;

public class ProjectTest {

	@Test
	public void testBaseCase() {
		Project project_old = new Project();
	}
	
	@Test
	public void testPersistence() {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql-pu");
	EntityManager em = emf.createEntityManager();
	
	Project project = new Project();
	project.setTitle("First project");
	project.setDescription("This is my first project, hi!");
	
	em.getTransaction().begin();
	em.persist(project);
	em.getTransaction().commit();
	
	em.close();
	emf.close();
	}

}
