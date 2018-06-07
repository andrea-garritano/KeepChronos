package com.garritano.keepchronos.model;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.boot.model.source.spi.AssociationSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ProjectIntegrationTest {
	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	
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
		
		entityManager.getTransaction().begin();
		
		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		
		project2 = new Project();
		project2.setTitle("Second project");
		project2.setDescription("This is my second project, wow!");
	}
	
	@Test
	public void testBasicPersistence() {
		
		entityManager.persist(project1);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		// Perform a simple query for all the Project entities
		Query query = entityManager.createQuery("select p from Project p");
		
		// We should have only one project in the database
		assertTrue(query.getResultList().size() == 1);
		
		// We should have the same title
		assertTrue(((Project) query.getSingleResult())
				.getTitle()
				.equals(project1.getTitle()));
		
		// and the same description
		assertTrue(((Project) query.getSingleResult())
				.getDescription()
				.equals(project1.getDescription()));
		
		// and the same id
		assertTrue(((Project) query.getSingleResult()).getId() == (project1.getId()));
		}
	
	@Test
	public void testMultiplePersistence() {

		entityManager.persist(project1);
		entityManager.persist(project2);
		
		entityManager.getTransaction().commit();
		entityManager.clear();
		
		// Perform a simple query for all the Project entities
		Query query = entityManager.createQuery("select p from Project p");

		// We should have 2 projects in the database
		assertTrue(query.getResultList().size() == 2);
	}
	
	@Test
	public void testEqualsWithNull() {
		project1.setId((long) 1);
		assertFalse(project1.equals(null));
	}
	
	@Test
	public void testEqualsWithOtherClass() {
		assertFalse(project1.equals(new Object()));
	}
	
	@Test
	public void testEqualsWithIdNull() {
		project1.setId((long) 1);
		Project projectIdNull = new Project();
		projectIdNull.setId(null);
		
		assertFalse(project1.equals(projectIdNull));
	}
	
	@Test
	public void testEqualsWithDifferentId() {
		project1.setId((long) 1);
		Project projectDifferentId = new Project();
		projectDifferentId.setId((long) -1);
		
		assertFalse(project1.equals(projectDifferentId));
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
