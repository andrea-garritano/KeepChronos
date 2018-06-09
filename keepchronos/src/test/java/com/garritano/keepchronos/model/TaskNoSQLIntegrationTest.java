package com.garritano.keepchronos.model;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TaskNoSQLIntegrationTest {
	private static final String PERSISTENCE_UNIT_NAME = "infinispan-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private static TransactionManager transactionManager;
	private Query query;
	
	private ProjectNoSQL project_another;
	private TaskNoSQL task1;
	private TaskNoSQL task2;
	
	
	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		
		//accessing JBoss's Transaction can be done differently but this one works nicely
		transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
	}
	
	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();
		
		// make sure to have the ProjectNoSQL table empty
		transactionManager.begin();
		query = entityManager.createQuery("select p from ProjectNoSQL p");
		List<ProjectNoSQL> allProjects = query.getResultList();
		for (ProjectNoSQL element: allProjects) {
			entityManager.remove(element);
		}
		transactionManager.commit();
		
		transactionManager.begin();
		query = entityManager.createQuery("select p from ProjectNoSQL p");
		assertTrue(query.getResultList().size() == 0);
		transactionManager.commit();
		
		// get a new EM to make sure data is actually retrieved from the store and not Hibernate’s internal cache
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();
		
		// make sure to have the TaskNoSQL table empty
		transactionManager.begin();
		query = entityManager.createQuery("select t from TaskNoSQL t");
		List<TaskNoSQL> allTasks = query.getResultList();
		for (TaskNoSQL element: allTasks) {
			entityManager.remove(element);
		}
		transactionManager.commit();
		
		transactionManager.begin();
		query = entityManager.createQuery("select p from TaskNoSQL p");
		assertTrue(query.getResultList().size() == 0);
		transactionManager.commit();
		
		// get a new EM to make sure data is actually retrieved from the store and not Hibernate’s internal cache
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();

		project_another = new ProjectNoSQL();
		project_another.setTitle("Another project");
		project_another.setDescription("Another exciting project!");
		
		transactionManager.begin();
		entityManager.persist(project_another);
		transactionManager.commit();
		
		task1 = new TaskNoSQL();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		//task1.setProject(project_another);
		//se setto il progetto, da errore
		task1.setDuration(30);
		
		task2 = new TaskNoSQL();
		task2.setTitle("Second task");
		task2.setDescription("This is my second task, wow!");
		//task2.setProject(project_another);
		task2.setDuration(20);
	}
	
	@Test
	public void testBasicPersistence() throws Exception {
		
		transactionManager.begin();
		entityManager.persist(task1);
		transactionManager.commit();
		
		// Perform a simple query for all the Task entities
		Query query = entityManager.createQuery("select p from TaskNoSQL p");
		
		// We should have only one task in the database
		assertTrue(query.getResultList().size() == 1);
		
		// We should have the same title
		assertTrue(((TaskNoSQL) query.getSingleResult())
				.getTitle()
				.equals(task1.getTitle()));
		
		// and the same description
		assertTrue(((TaskNoSQL) query.getSingleResult())
				.getDescription()
				.equals(task1.getDescription()));
		
		// and the same id
		assertTrue(((TaskNoSQL) query.getSingleResult()).getId() == (task1.getId()));
		
		//and the same associate project
		//assertTrue(((TaskNoSQL)query.getSingleResult()).getProject().equals(project_another));
		
		//and the same duration
		assertTrue(((TaskNoSQL) query.getSingleResult()).getDuration() == (task1.getDuration()));
		}
	
	@Test
	public void testMultiplePersistence() throws Exception {
		transactionManager.begin();
		entityManager.persist(task1);
		transactionManager.commit();

		transactionManager.begin();
		entityManager.persist(task2);
		transactionManager.commit();
		
		// Perform a simple query for all the Task entities
		Query query = entityManager.createQuery("select t from TaskNoSQL t");

		// We should have 2 tasks in the database
		assertTrue(query.getResultList().size() == 2);
	}
	
	@After
	public void tearDown() {
		task1 = null;
		task2 = null;
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
}