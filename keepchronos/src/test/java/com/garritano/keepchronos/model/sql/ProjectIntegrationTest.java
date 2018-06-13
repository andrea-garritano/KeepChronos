package com.garritano.keepchronos.model.sql;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.garritano.keepchronos.model.sql.Project;

public class ProjectIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private Query query;

	private Project project1;
	private Project project2;

	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		// make sure to drop the Project table for testing
		entityManager.createNativeQuery("delete from Project").executeUpdate();
		entityManager.getTransaction().commit();

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		project2 = new Project();
		project2.setTitle("Second project");
		project2.setDescription("This is my second project, wow!");
	}
	
	private void transactionPersist(Project p) {
		entityManager.getTransaction().begin();
		entityManager.persist(p);
		entityManager.getTransaction().commit();
	}

	@Test
	public void testBasicPersistence() {
		transactionPersist(project1);
		entityManager.clear();

		// Perform a simple query for all the Project entities
		query = entityManager.createQuery("select p from Project p");

		// We should have only one project in the database
		assertTrue(query.getResultList().size() == 1);

		// We should have the same title
		assertEquals(project1.getTitle(), ((Project) query.getSingleResult()).getTitle());

		// and the same description
		assertEquals(project1.getDescription(), ((Project) query.getSingleResult()).getDescription());
	}

	@Test
	public void testMultiplePersistence() {
		transactionPersist(project1);
		transactionPersist(project2);
		entityManager.clear();

		// Perform a simple query for all the Project entities
		query = entityManager.createQuery("select p from Project p");

		// We should have 2 projects in the database
		assertTrue(query.getResultList().size() == 2);
	}

	@After
	public void tearDown() {
		project1 = null;
		project2 = null;
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
}
