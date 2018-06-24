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

import com.garritano.keepchronos.dao.ProjectDao;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class ProjectDaoTest {
	private ProjectDao projectDao;
	private EntityManager entityManager;
	TypedQuery<Project> query;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		entityManager = mock(EntityManager.class);
		projectDao = new ProjectDao(entityManager);
		query = (TypedQuery<Project>) mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(Project.class))).thenReturn(query);
	}

	@Test
	public void testSave() {
		EntityTransaction transaction = mock(EntityTransaction.class);
		when(entityManager.getTransaction()).thenReturn(transaction);

		projectDao.save(new Project());

		verify(entityManager).persist(any(Project.class));
		verify(transaction).begin();
		verify(transaction).commit();
	}

	@Test
	public void testGetAllNoProjects() {
		List<Project> pList = new ArrayList<Project>();
		when(query.getResultList()).thenReturn(pList);

		List<Project> returnedProjects = projectDao.getAll();

		assertEquals(0, returnedProjects.size());
	}

	@Test
	public void testgetAllOneProject() {
		List<Project> pList = new ArrayList<Project>();
		pList.add(new Project());

		when(query.getResultList()).thenReturn(pList);

		List<Project> returnedProjects = projectDao.getAll();

		assertEquals(1, returnedProjects.size());
	}

	@Test
	public void testgetAllMoreThanOneProjects() {
		List<Project> pList = new ArrayList<Project>();
		pList.add(new Project());
		pList.add(new Project());
		when(query.getResultList()).thenReturn(pList);

		List<Project> returnedProjects = projectDao.getAll();

		assertEquals(2, returnedProjects.size());
	}

	@Test
	public void testFindByIdNoProject() {
		when(entityManager.find(eq(Project.class), anyLong())).thenReturn(null);
		
		Project returnedProject = projectDao.findById((long) 0);

		assertNull(returnedProject);
	}

	@Test
	public void testFindByIdOneProject() {
		Project tempProject = new Project();
		when(entityManager.find(eq(Project.class), anyLong())).thenReturn(tempProject);
		
		Project returnedProject = projectDao.findById((long) 0);

		assertEquals(returnedProject, tempProject);
	}
	
	@SuppressWarnings("unchecked")
	private TypedQuery<Task> mockQuery() {
		TypedQuery<Task> queryTask = (TypedQuery<Task>) mock(TypedQuery.class);
		when(entityManager.createQuery(anyString(), eq(Task.class))).thenReturn(queryTask);
		
		TypedQuery<Task> queryTaskParameter = (TypedQuery<Task>) mock(TypedQuery.class);
		when(queryTask.setParameter(eq("project_id"), anyObject())).thenReturn(queryTaskParameter);
		return queryTaskParameter;
	}


	@Test
	public void testGetTaskNoTask() {
		TypedQuery<Task> queryTaskParameter = mockQuery();
		
		List<Task> tList = new ArrayList<Task>();
		when(queryTaskParameter.getResultList()).thenReturn(tList);
		
		List<Task> taskList = projectDao.getTasks(new Project());
		
		assertEquals(0, taskList.size());
	}
	
	@Test
	public void testGetTaskOneTask() {
		TypedQuery<Task> queryTaskParameter = mockQuery();
		
		List<Task> tList = new ArrayList<Task>();
		tList.add(new Task());
		
		when(queryTaskParameter.getResultList()).thenReturn(tList);
		
		List<Task> taskList = projectDao.getTasks(new Project());
		
		assertEquals(1, taskList.size());
	}
	
	@Test
	public void testGetTaskMoreThanOneTask() {
		TypedQuery<Task> queryTaskParameter = mockQuery();
		
		List<Task> tList = new ArrayList<Task>();
		tList.add(new Task());
		tList.add(new Task());
		
		when(queryTaskParameter.getResultList()).thenReturn(tList);
		
		List<Task> taskList = projectDao.getTasks(new Project());
		
		assertEquals(2, taskList.size());
	}


}