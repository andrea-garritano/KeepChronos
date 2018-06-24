package com.garritano.keepchronos.dao;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.TaskDao;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class TaskDaoTest {
	private TaskDao taskDao;
	private EntityManager entityManager;
	TypedQuery<Task> query;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		entityManager = mock(EntityManager.class);
		taskDao = new TaskDao(entityManager);
		query = (TypedQuery<Task>) mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(Task.class))).thenReturn(query);
	}

	@Test
	public void testSave() {
		EntityTransaction transaction = mock(EntityTransaction.class);
		when(entityManager.getTransaction()).thenReturn(transaction);

		taskDao.save(new Task());

		verify(entityManager).persist(any(Task.class));
		verify(transaction).begin();
		verify(transaction).commit();
	}

	@Test
	public void testGetAllNoTasks() {
		List<Task> pList = new ArrayList<Task>();
		when(query.getResultList()).thenReturn(pList);

		List<Task> returnedTasks = taskDao.getAll();

		assertEquals(0, returnedTasks.size());
	}

	@Test
	public void testgetAllOneTask() {
		List<Task> pList = new ArrayList<Task>();
		pList.add(new Task());

		when(query.getResultList()).thenReturn(pList);

		List<Task> returnedTasks = taskDao.getAll();

		assertEquals(1, returnedTasks.size());
	}

	@Test
	public void testgetAllMoreThanOneTasks() {
		List<Task> pList = new ArrayList<Task>();
		pList.add(new Task());
		pList.add(new Task());
		when(query.getResultList()).thenReturn(pList);

		List<Task> returnedTasks = taskDao.getAll();

		assertEquals(2, returnedTasks.size());
	}

	@Test
	public void testFindByIdNoTask() {
		when(entityManager.find(eq(Task.class), anyLong())).thenReturn(null);
		
		Task returnedTask = taskDao.findById((long) 0);

		assertNull(returnedTask);
	}

	@Test
	public void testFindByIdOneTask() {
		Task tempTask = new Task();
		when(entityManager.find(eq(Task.class), anyLong())).thenReturn(tempTask);
		
		Task returnedTask = taskDao.findById((long) 0);

		assertEquals(returnedTask, tempTask);
	}
	
	@Test
	public void testGetProjectByTaskIdNoProject() {
		when(entityManager.find(eq(Task.class), anyLong())).thenReturn(null);
		
		Project tempProject = taskDao.getProjectByTaskId((long) 0);
		
		assertNull(tempProject);
	}
	
	@Test
	public void testGetProjectByTaskIdWithProject() {
		Task tempTask = mock(Task.class);
		when(entityManager.find(eq(Task.class), anyLong())).thenReturn(tempTask);
		Project tempProject = new Project();
		when(tempTask.getProject()).thenReturn(tempProject);
		
		Project returnedProject = taskDao.getProjectByTaskId((long) 0);
		
		assertEquals(tempProject, returnedProject);
	}
}