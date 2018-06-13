package com.garritano.keepchronos.dao.nosql;

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

import com.garritano.keepchronos.dao.nosql.TaskNoSQLDao;
import com.garritano.keepchronos.model.nosql.Project;
import com.garritano.keepchronos.model.nosql.Task;

public class TaskDaoIntegrationTest {

	private static final String PERSISTENCE_UNIT_NAME = "infinispan-pu";
	private static EntityManagerFactory entityManagerFactory;
	protected EntityManager entityManager;
	private static TransactionManager transactionManager;
	private Query query;
	private TaskNoSQLDao taskDao;

	private Project project_another;
	private Task task1;
	private Task task2;

	@BeforeClass
	public static void setUpClass() {
		entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

		// accessing JBoss's Transaction can be done differently but this one works
		// nicely
		transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
	}

	@Before
	public void setUp() throws Exception {
		entityManager = entityManagerFactory.createEntityManager();

		// make sure to have the Task table empty
		transactionManager.begin();
		query = entityManager.createQuery("select t from Task t");
		List<Task> allTasks = query.getResultList();
		for (Task element : allTasks) {
			entityManager.remove(element);
		}
		transactionManager.commit();

		transactionManager.begin();
		query = entityManager.createQuery("select p from Task p");
		assertTrue(query.getResultList().size() == 0);
		transactionManager.commit();

		// get a new EM to make sure data is actually retrieved from the store and not
		// Hibernate’s internal cache
		entityManager.close();
		entityManager = entityManagerFactory.createEntityManager();

		project_another = new Project();
		project_another.setTitle("Another project");
		project_another.setDescription("Another exciting project!");

		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setProject(project_another);
		task1.setDuration(30);

		task2 = new Task();
		task2.setTitle("Second task");
		task2.setDescription("This is my second task, wow!");
		task2.setDuration(20);

		taskDao = new TaskNoSQLDao(entityManager);
	}

	@Test
	public void testSave() throws Exception {
		taskDao.save(task1);

		assertEquals(task1, entityManager.createQuery("from Task where id =:id", Task.class)
				.setParameter("id", task1.getId()).getSingleResult());
	}

	@Test
	public void testEmptyGetAll() throws Exception {
		assertTrue(taskDao.getAll().size() == 0);
	}

	@Test
	public void testOneGetAll() throws Exception {
		taskDao.save(task1);

		assertEquals(task1, taskDao.getAll().get(0));
		assertTrue(taskDao.getAll().size() == 1);
	}

	@Test
	public void testMultipleGetAll() throws Exception {
		taskDao.save(task1);
		taskDao.save(task2);

		assertTrue(taskDao.getAll().size() == 2);
	}

	@Test
	public void testEmptyFindbyId() {
		assertNull(taskDao.findById((long) -1));
	}

	@Test
	public void testNotEmptyFindbyId() throws Exception {
		taskDao.save(task1);

		assertEquals(task1, taskDao.findById(task1.getId()));
	}

	@Test
	public void testUpdate() throws Exception {
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
	public void testGetProjectByTaskIdWithExistingId() throws Exception {
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