package com.garritano.keepchronos.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.boot.model.source.spi.AssociationSource;
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
		projectDao = new ProjectDao(entityManager);
		
		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		
		project2 = new Project();
		project2.setTitle("Second project");
		project2.setDescription("This is my second project, wow!");
	}
	
	@Test
	public void testSave() {
		projectDao.save(project1);
		
		assertEquals(project1, entityManager.createQuery("from Project where id =:id", Project.class)
											.setParameter("id", project1.getId())
											.getSingleResult());
	}
	
	@Test
	public void testEmptyGetAll() {
		assertTrue(projectDao.getAll().size() == 0);
	}
	
	@Test
	public void testOneGetAll() {
		projectDao.save(project1);
		
		assertEquals(project1, projectDao.getAll().get(0));
		assertTrue(projectDao.getAll().size() == 1);
	}
	
	@Test
	public void testMultipleGetAll() {
		projectDao.save(project1);
		projectDao.save(project2);
		
		assertTrue(projectDao.getAll().size() == 2);
	}
	
	@Test
	public void testEmptyFindbyId() {
		assertNull(projectDao.findById((long) 34214342));
	}
	
	@Test
	public void testNotEmptyFindbyId() {
		projectDao.save(project1);
		
		assertEquals(project1, projectDao.findById(project1.getId()));
	}
	
	@After
	public void tearDown() {
		project1 = null;
		project2 = null;
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