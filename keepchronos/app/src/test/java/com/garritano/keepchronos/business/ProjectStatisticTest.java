package com.garritano.keepchronos.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.ProjectDaoInterface;
import com.garritano.keepchronos.model.Project;
import com.garritano.keepchronos.model.Task;

public class ProjectStatisticTest {
	private ProjectStatistic statistic;
	private ProjectDaoInterface projectDaoInterface;
	private List<Project> projects;
	private ArrayList<Task> tasks;

	@Before
	public void setUp() throws Exception {
		projects = new ArrayList<Project>();
		projectDaoInterface = mock(ProjectDaoInterface.class);
		when(projectDaoInterface.getAll()).thenReturn(projects);

		tasks = new ArrayList<Task>();

		when(projectDaoInterface.getTasks(any(Project.class))).thenReturn(tasks);

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

	private void assertTotalDuration(Project project, int expected) {
		int averageDuration = statistic.getTotalDuration(project);
		assertEquals(expected, averageDuration);
	}

	@Test
	public void testGetTotalDurationGivenProjectWithoutTask() {
		Project tempProject = new Project();

		assertTotalDuration(tempProject, 0);
	}

	@Test
	public void testGetTotalDurationGivenProjectWithOneTask() {
		Project tempProject = new Project();

		Task tempTask1 = new Task();
		tempTask1.setDuration(30);

		tasks.add(tempTask1);

		assertTotalDuration(tempProject, 30);
	}

	@Test
	public void testGetTotalDurationGivenProject() {
		Project tempProject = new Project();
		Task tempTask1 = new Task();
		tempTask1.setDuration(30);

		Task tempTask2 = new Task();
		tempTask2.setDuration(20);

		tasks.add(tempTask1);
		tasks.add(tempTask2);

		assertTotalDuration(tempProject, 50);
	}
}
