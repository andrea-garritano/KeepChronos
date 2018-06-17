package com.garritano.keepchronos.business;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.TaskDaoInterface;
import com.garritano.keepchronos.model.Task;

public class TaskStatisticTest {
	private TaskStatistic statistic;
	private TaskDaoInterface taskDaoInterface;
	private List<Task> tasks;

	@Before
	public void setUp() throws Exception {
		tasks = new ArrayList<Task>();
		taskDaoInterface = mock(TaskDaoInterface.class);
		when(taskDaoInterface.getAll()).thenReturn(tasks);
		
		statistic = new TaskStatistic(taskDaoInterface);
	}

	private void assertNumberOfTask(int expected) {
		int numberOfTask = statistic.getNumberOfTask();
		assertEquals(expected, numberOfTask);
	}

	@Test
	public void testNoTask() {
		assertNumberOfTask(0);
	}

	@Test
	public void testSingleTask() {
		tasks.add(new Task());
		assertNumberOfTask(1);
	}
	
	@Test
	public void testMultipleTasks() {
		tasks.add(new Task());
		tasks.add(new Task());
		assertNumberOfTask(2);
	}

	@After
	public void tearDown() throws Exception {
	}
}
