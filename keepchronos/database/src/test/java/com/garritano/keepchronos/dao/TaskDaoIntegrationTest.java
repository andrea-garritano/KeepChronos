package com.garritano.keepchronos.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.TaskDao;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class TaskDaoIntegrationTest {
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private TaskDao taskDao;

	private Project project_another;
	private Task task1;
	private Task task2;

	private void deleteTable(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		
		// make sure to drop the Project table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Project").executeUpdate();
		entityManager.getTransaction().commit();

		// make sure to drop the Task table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Task").executeUpdate();
		entityManager.getTransaction().commit();

		entityManager.close();
		entityManagerFactory.close();
	}

	@Before
	public void setUp() throws Exception {
		deleteTable("mysql-pu");
		deleteTable("postgresql-pu");
	}

	private void assertSave(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		taskDao.save(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(task1, entityManager.createQuery("from Task where id =:id", Task.class)
				.setParameter("id", task1.getId()).getSingleResult());
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testSave() {
		assertSave("mysql-pu");
		assertSave("postgresql-pu");

	}

	private void assertEmptyGetAll(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		assertTrue(taskDao.getAll().size() == 0);
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testEmptyGetAll() {
		assertEmptyGetAll("mysql-pu");
		assertEmptyGetAll("postgresql-pu");
	}

	private void assertOneGetAll(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		taskDao.save(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(task1, taskDao.getAll().get(0));
		assertTrue(taskDao.getAll().size() == 1);
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testOneGetAll() {
		assertOneGetAll("mysql-pu");
		assertOneGetAll("postgresql-pu");
	}

	private void assertMultipleGetAll(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		
		task2 = new Task();
		task2.setTitle("Second task");
		task2.setDescription("This is my second task, wow!");
		task2.setDuration(30);
		
		taskDao.save(task1);
		taskDao.save(task2);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertTrue(taskDao.getAll().size() == 2);
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testMultipleGetAll() {
		assertMultipleGetAll("mysql-pu");
		assertMultipleGetAll("postgresql-pu");
	}

	private void assertEmptyFindbyId(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		assertNull(taskDao.findById((long) -1));
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testEmptyFindbyId() {
		assertEmptyFindbyId("mysql-pu");
		assertEmptyFindbyId("postgresql-pu");
	}

	private void assertNotEmptyFindbyId(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		
		taskDao.save(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(task1, taskDao.findById(task1.getId()));
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testNotEmptyFindbyId() {
		assertNotEmptyFindbyId("mysql-pu");
		assertNotEmptyFindbyId("postgresql-pu");

	}

	private void assertUpdate(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		task1 = new Task();
		task1.setTitle("First task");
		task1.setDescription("This is my first task, hi!");
		task1.setDuration(20);
		
		taskDao.save(task1);
		task1.setDescription("new description!");
		taskDao.update(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(task1.getDescription(), taskDao.findById(task1.getId()).getDescription());
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testUpdate() {
		assertUpdate("mysql-pu");
		assertUpdate("postgresql-pu");
	}

	private void assertGetProjectByTaskIdWithNonExistingId(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		taskDao = new TaskDao(entityManager);
		assertNull(taskDao.getProjectByTaskId((long) -1));
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testGetProjectByTaskIdWithNonExistingId() {
		assertGetProjectByTaskIdWithNonExistingId("mysql-pu");
		assertGetProjectByTaskIdWithNonExistingId("postgresql-pu");
	}

	private void assertGetProjectByTaskIdWithExistingId(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
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
		
		taskDao.save(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(task1.getProject(), taskDao.getProjectByTaskId(task1.getId()));
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testGetProjectByTaskIdWithExistingId() {
		assertGetProjectByTaskIdWithExistingId("mysql-pu");
		assertGetProjectByTaskIdWithExistingId("postgresql-pu");
	}

}