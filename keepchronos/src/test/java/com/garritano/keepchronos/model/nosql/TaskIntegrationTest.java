package com.garritano.keepchronos.model.nosql;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.garritano.keepchronos.model.nosql.Project;
import com.garritano.keepchronos.model.nosql.Task;

public class TaskIntegrationTest {
	private static final String PERSISTENCE_UNIT_NAME = "infinispan-pu";
	private static EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private static TransactionManager transactionManager;
	private TypedQuery<Task> query;

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
		query = entityManager.createQuery("select t from Task t", Task.class);
		List<Task> allTasks = query.getResultList();
		for (Task element : allTasks) {
			entityManager.remove(element);
		}
		transactionManager.commit();

		transactionManager.begin();
		query = entityManager.createQuery("select p from Task p", Task.class);
		assertTrue(query.getResultList().size() == 0);
		transactionManager.commit();

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
	}

	private void transactPersist(Task t) throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactionManager.begin();
		entityManager.persist(t);
		transactionManager.commit();
	}
	
	/**
	 * Perform a simple query for all the Project entities
	 */
	private List<Task> getAllTasks() {
		entityManager.getTransaction().begin();
		query = entityManager.createQuery("select p from Task p", Task.class);
		entityManager.getTransaction().commit();
		return query.getResultList();
	}

	@Test
	public void testBasicPersistence() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactPersist(task1);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		List<Task> projectsList = getAllTasks();

		// We should have only one task in the database
		assertTrue(projectsList.size() == 1);

		// We should have the same title
		assertEquals(task1.getTitle(), projectsList.get(0).getTitle());

		// and the same description
		assertEquals(task1.getDescription(), projectsList.get(0).getDescription());

		// and the same duration
		assertEquals(task1.getDuration(), projectsList.get(0).getDuration());

		// and the same associate project
		assertEquals(project_another, projectsList.get(0).getProject());
	}

	@Test
	public void testBasicPersistenceWithoutProject() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactPersist(task2);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		List<Task> projectsList = getAllTasks();

		assertEquals(null, projectsList.get(0).getProject());
	}

	@Test
	public void testMultiplePersistence() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactPersist(task1);
		transactPersist(task2);

		// Clear Hibernate’s cache to make sure data is retrieved from the store
		entityManager.clear();

		List<Task> projectsList = getAllTasks();

		// We should have 2 tasks in the database
		assertTrue(projectsList.size() == 2);
	}

	@After
	public void tearDown() {
		task1 = null;
		task2 = null;
		entityManager.close();
	}

	@AfterClass
	public static void tearDownClass() {
		entityManagerFactory.close();
	}
}