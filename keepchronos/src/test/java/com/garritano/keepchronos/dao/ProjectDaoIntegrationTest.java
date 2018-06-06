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
	
	@Test
	public void testOneGetAll() {
		Project project = new Project();
		project.setTitle("First project");
		project.setDescription("This is my first project, hi!");
		projectDao.save(project);
		
		assertEquals(project, projectDao.getAll().get(0));
		assertTrue(projectDao.getAll().size() == 1);
	}
	
	@Test
	public void testMultipleGetAll() {
		Project project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		projectDao.save(project1);
		
		Project project2 = new Project();
		project2.setTitle("Second project");
		project2.setDescription("This is my second project, wow!");
		projectDao.save(project2);
		
		assertTrue(projectDao.getAll().size() == 2);
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