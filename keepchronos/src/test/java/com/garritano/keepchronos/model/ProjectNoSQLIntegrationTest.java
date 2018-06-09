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

public class ProjectNoSQLIntegrationTest {
	private static final String PERSISTENCE_UNIT_NAME = "infinispan-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private static TransactionManager transactionManager;
	private Query query;
	
	private ProjectNoSQL project1;
	private ProjectNoSQL project2;
	
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
		List<ProjectNoSQL> allElements = query.getResultList();
		for (ProjectNoSQL element: allElements) {
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
		
		project1 = new ProjectNoSQL();
		project1.setTitle("First project");

		project2 = new ProjectNoSQL();
		project2.setTitle("Second project");
	}
	
	private void transactionPersist(ProjectNoSQL project) throws Exception {
		transactionManager.begin();
		entityManager.persist(project);
		transactionManager.commit();
	}
	
	@Test
	public void testBasicPersistence() throws Exception {
		transactionPersist(project1);
		
		// Perform a simple query for all the ProjectNoSQL entities
		Query query = entityManager.createQuery("select p from ProjectNoSQL p");
		
		// We should have only one project in the database
		assertTrue(query.getResultList().size() == 1);
		
		// We should have the same title
		assertTrue(((ProjectNoSQL) query.getSingleResult())
				.getTitle()
				.equals(project1.getTitle()));
		
		// and the same id
		assertTrue(((ProjectNoSQL) query.getSingleResult()).getId() == (project1.getId()));
		}
	
	@Test
	public void testMultiplePersistence() throws Exception{
		transactionPersist(project1);
		transactionPersist(project2);
		
		// Perform a simple query for all the ProjectNoSQL entities
		Query query = entityManager.createQuery("select p from ProjectNoSQL p");

		// We should have 2 projects in the database
		assertTrue(query.getResultList().size() == 2);
	}

	@After
	public void tearDown() throws Exception {
		project1 = null;
		project2 = null;
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
}
