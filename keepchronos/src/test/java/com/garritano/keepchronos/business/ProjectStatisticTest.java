package com.garritano.keepchronos.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.ProjectDaoInterface;
import com.garritano.keepchronos.model.Project;

public class ProjectStatisticTest {
	private ProjectStatistic statistic;
	private ProjectDaoInterface projectDaoInterface;
	private List<Project> projects;

	@Before
	public void setUp() throws Exception {
		projects = new ArrayList<Project>();
		projectDaoInterface = mock(ProjectDaoInterface.class);
		when(projectDaoInterface.getAll()).thenReturn(projects);
		
		statistic = new ProjectStatistic(projectDaoInterface);
	}

	private void assertNumberOfProject(int expected) {
		int numberOfProject = statistic.getNumberOfProject();
		assertEquals(expected, numberOfProject);
	}

	@Test
	public void testNoProject() {
		assertNumberOfProject(0);
	}

	@Test
	public void testSingleProject() {
		projects.add(new Project());
		assertNumberOfProject(1);
	}
	
	@Test
	public void testMultipleProjects() {
		projects.add(new Project());
		projects.add(new Project());
		assertNumberOfProject(2);
	}
}
