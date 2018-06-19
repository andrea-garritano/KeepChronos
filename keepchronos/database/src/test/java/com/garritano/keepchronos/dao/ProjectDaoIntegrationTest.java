package com.garritano.keepchronos.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.ProjectDao;
import com.garritano.keepchronos.dao.TaskDao;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class ProjectDaoIntegrationTest {

	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private ProjectDao projectDao;

	private Project project1;
	private Project project2;

	private void deleteTable(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		// make sure to drop the Task table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Task").executeUpdate();
		entityManager.getTransaction().commit();

		// make sure to drop the Project table for testing
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("delete from Project").executeUpdate();
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
		projectDao = new ProjectDao(entityManager);
		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		projectDao.save(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(project1, entityManager.createQuery("from Project where id =:id", Project.class)
				.setParameter("id", project1.getId()).getSingleResult());
		entityManager.close();
		entityManagerFactory.close();
	}

	private void assertEmptyGetAll(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		projectDao = new ProjectDao(entityManager);
		assertTrue(projectDao.getAll().size() == 0);
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testSave() {
		assertSave("mysql-pu");
		assertSave("postgresql-pu");
	}

	@Test
	public void testEmptyGetAll() {
		assertEmptyGetAll("mysql-pu");
		assertEmptyGetAll("postgresql-pu");
	}

	private void assertOneGetAll(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		projectDao.save(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(project1, projectDao.getAll().get(0));
		assertTrue(projectDao.getAll().size() == 1);
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
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		project2 = new Project();
		project2.setTitle("Second project");
		project2.setDescription("This is my second project, wow!");

		projectDao.save(project1);
		projectDao.save(project2);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertTrue(projectDao.getAll().size() == 2);

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
		projectDao = new ProjectDao(entityManager);
		assertNull(projectDao.findById((long) 34214342));
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
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		projectDao.save(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(project1, projectDao.findById(project1.getId()));
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
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		projectDao.save(project1);
		project1.setDescription("new description!");
		projectDao.update(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(project1.getDescription(), projectDao.findById(project1.getId()).getDescription());
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testUpdate() {
		assertUpdate("mysql-pu");
		assertUpdate("postgresql-pu");
	}

	private void assertGetTasksWithNoTask(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		projectDao.save(project1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(0, projectDao.getTasks(project1).size());
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testGetTasksWithNoTask() {
		assertGetTasksWithNoTask("mysql-pu");
		assertGetTasksWithNoTask("postgresql-pu");
	}

	private void assertGetTasksWithOneTask(String persistenceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnit);
		entityManager = entityManagerFactory.createEntityManager();
		projectDao = new ProjectDao(entityManager);

		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");

		projectDao.save(project1);

		Task tempTask = new Task();
		tempTask.setProject(project1);
		TaskDao taskDao = new TaskDao(entityManager);
		taskDao.save(tempTask);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		assertEquals(tempTask, projectDao.getTasks(project1).get(0));
		entityManager.close();
		entityManagerFactory.close();
	}

	@Test
	public void testGetTasksWithOneTask() {
		assertGetTasksWithOneTask("mysql-pu");
		assertGetTasksWithOneTask("postgresql-pu");
	}
}