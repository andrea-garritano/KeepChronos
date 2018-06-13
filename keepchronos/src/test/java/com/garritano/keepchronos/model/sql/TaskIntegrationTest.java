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
import com.garritano.keepchronos.model.sql.Task;

public class TaskIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;

	private Project project_another;
	private Task task1;
	private Task task2;

	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();
		
		entityManager.getTransaction().begin();
		// make sure to drop the Task table for testing
		entityManager.createNativeQuery("delete from Task").executeUpdate();
		entityManager.getTransaction().commit();
		
		// make sure to drop the Task table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Project").executeUpdate();
		entityManager.getTransaction().commit();

		project_another = new Project();
		project_another.setTitle("Another project");
		project_another.setDescription("Another exciting project!");
		entityManager.persist(project_another);

		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setProject(project_another);
		task1.setDuration(30);

		task2 = new Task();
		task2.setTitle("Second task");
		task2.setDescription("This is my second task, wow!");
		task2.setDuration(20);
		entityManager.getTransaction().begin();
	}

	@Test
	public void testBasicPersistence() {
		entityManager.persist(task1);
		entityManager.getTransaction().commit();

		// Perform a simple query for all the Task entities
		Query query = entityManager.createQuery("select p from Task p");

		// We should have only one task in the database
		assertTrue(query.getResultList().size() == 1);

		// We should have the same title
		assertTrue(((Task) query.getSingleResult()).getTitle().equals(task1.getTitle()));

		// and the same description
		assertTrue(((Task) query.getSingleResult()).getDescription().equals(task1.getDescription()));

		// and the same id
		assertTrue(((Task) query.getSingleResult()).getId() == (task1.getId()));

		// and the same associate project
		assertTrue(((Task) query.getSingleResult()).getProject().equals(project_another));

		// and the same duration
		assertTrue(((Task) query.getSingleResult()).getDuration() == (task1.getDuration()));
	}

	@Test
	public void testMultiplePersistence() {
		entityManager.persist(task1);
		entityManager.persist(task2);
		entityManager.getTransaction().commit();

		// Perform a simple query for all the Task entities
		Query query = entityManager.createQuery("select p from Task p");

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