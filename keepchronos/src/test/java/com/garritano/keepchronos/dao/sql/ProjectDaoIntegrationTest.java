package com.garritano.keepchronos.dao.sql;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.garritano.keepchronos.dao.sql.ProjectDao;
import com.garritano.keepchronos.model.sql.Project;

public class ProjectDaoIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
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

		// make sure to drop the Project table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Project").executeUpdate();
		entityManager.getTransaction().commit();
		
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
				.setParameter("id", project1.getId()).getSingleResult());
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

	@Test
	public void testUpdate() {
		projectDao.save(project1);
		project1.setDescription("new description!");
		projectDao.update(project1);

		assertEquals(project1.getDescription(), projectDao.findById(project1.getId()).getDescription());
	}

	@After
	public void tearDown() {
		project1 = null;
		project2 = null;
		if (entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().rollback();
		}
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}

}