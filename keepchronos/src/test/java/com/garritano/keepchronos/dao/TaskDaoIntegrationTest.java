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
import com.garritano.keepchronos.model.Task;

public class TaskDaoIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "mysql-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private TaskDao taskDao;

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
		entityManager.getTransaction().begin();
		taskDao = new TaskDao(entityManager);

		project_another = new Project();
		project_another.setTitle("Another project");
		project_another.setDescription("Another exciting project!");
		entityManager.persist(project_another);

		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		task1.setProject(project_another);

		task2 = new Task();
		task2.setTitle("Second task");
		task2.setDescription("This is my second task, wow!");
		task2.setDuration(30);
		task1.setProject(project_another);
	}

	@Test
	public void testSave() {
		taskDao.save(task1);

		assertEquals(task1, entityManager.createQuery("from Task where id =:id", Task.class)
				.setParameter("id", task1.getId()).getSingleResult());
	}

	@Test
	public void testEmptyGetAll() {
		assertTrue(taskDao.getAll().size() == 0);
	}

	@Test
	public void testOneGetAll() {
		taskDao.save(task1);

		assertEquals(task1, taskDao.getAll().get(0));
		assertTrue(taskDao.getAll().size() == 1);
	}

	@Test
	public void testMultipleGetAll() {
		taskDao.save(task1);
		taskDao.save(task2);

		assertTrue(taskDao.getAll().size() == 2);
	}

	@Test
	public void testEmptyFindbyId() {
		assertNull(taskDao.findById((long) -1));
	}

	@Test
	public void testNotEmptyFindbyId() {
		taskDao.save(task1);

		assertEquals(task1, taskDao.findById(task1.getId()));
	}

	@Test
	public void testUpdate() {
		taskDao.save(task1);
		task1.setDescription("new description!");
		taskDao.update(task1);

		assertEquals(task1.getDescription(), taskDao.findById(task1.getId()).getDescription());
	}

	@Test
	public void testGetProjectByTaskIdWithNonExistingId() {
		assertNull(taskDao.getProjectByTaskId((long) -1));
	}

	@Test
	public void testGetProjectByTaskIdWithExistingId() {
		taskDao.save(task1);
		assertEquals(task1.getProject(), taskDao.getProjectByTaskId(task1.getId()));
	}

	@After
	public void tearDown() {
		task1 = null;
		task2 = null;
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