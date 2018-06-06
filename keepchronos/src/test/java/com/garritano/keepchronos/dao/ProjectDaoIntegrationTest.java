package com.garritano.keepchronos.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.garritano.keepchronos.model.Project;

public class ProjectDaoIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private ProjectDao projectDao;
	
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
		projectDao = new ProjectDao(entityManager);
	}
	
	@Test
	public void testSave() {
		Project project = new Project();
		project.setTitle("First project");
		project.setDescription("This is my first project, hi!");
		projectDao.save(project);
		assertEquals(project, entityManager.createQuery("from Project where id =:id", Project.class)
											.setParameter("id", project.getId())
											.getSingleResult());
	}
	
	@Test
	public void testEmptyGetAll() {
		assertNull(projectDao.getAll());
	}
	
	@After
	public void tearDown() {
		if ( entityManager.getTransaction().isActive() ) {
			entityManager.getTransaction().rollback();
		}
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
	
}