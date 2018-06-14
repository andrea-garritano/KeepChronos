package com.garritano.keepchronos.model.sql;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
	private TypedQuery<Project> query;

	private Project project1;
	private Project project2;

	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	}

	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();

		// make sure to drop the Project table for testing
		entityManager.getTransaction().begin();
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

	/**
	 * Perform a simple query for all the Project entities
	 */
	private List<Project> getAllProjects() {
		//
		entityManager.getTransaction().begin();
		query = entityManager.createQuery("select p from Project p", Project.class);
		entityManager.getTransaction().commit();
		return query.getResultList();
	}

	@Test
	public void testBasicPersistence() {
		transactionPersist(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		List<Project> projectsList = getAllProjects();

		// We should have only one project in the database
		assertTrue(projectsList.size() == 1);

		// We should have the same title
		assertEquals(project1.getTitle(), projectsList.get(0).getTitle());

		// and the same description
		assertEquals(project1.getDescription(), projectsList.get(0).getDescription());
	}

	@Test
	public void testMultiplePersistence() {
		transactionPersist(project1);
		transactionPersist(project2);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		List<Project> projectsList = getAllProjects();

		// We should have 2 projects in the database
		assertTrue(projectsList.size() == 2);
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
