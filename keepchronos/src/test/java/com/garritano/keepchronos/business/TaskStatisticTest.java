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
	
	private void assertTotalDuration(int expected) {
		int durationTotal = statistic.getTotalDuration();
		assertEquals(expected, durationTotal);
	}
	
	@Test
	public void testSumDurationNoTask() {
		assertTotalDuration(0);
	}
	
	@Test
	public void testSumDurationOneTask() {
		Task temp_task = new Task();
		temp_task.setDuration(20);
		tasks.add(temp_task);
		
		assertTotalDuration(20);
	}
	
	@Test
	public void testSumDurationMultipleTasks() {
		Task tempTask1 = new Task();
		tempTask1.setDuration(20);
		tasks.add(tempTask1);
		
		Task tempTask2 = new Task();
		tempTask2.setDuration(30);
		tasks.add(tempTask2);
		
		assertTotalDuration(50);
	}
	
	private void assertAverageDuration(int expected) {
		int averageDurarion = statistic.getAverageDuration();
		assertEquals(expected, averageDurarion);
	}
	
	@Test
	public void testAverageDurarionNoTask() {
		assertAverageDuration(0);
	}
	
	@Test
	public void testAverageDurarionOneTask() {
		Task temp_task = new Task();
		temp_task.setDuration(20);
		tasks.add(temp_task);
		
		assertAverageDuration(20);
	}
	
	@Test
	public void testAverageDurarionMultipleTasks() {
		Task tempTask1 = new Task();
		tempTask1.setDuration(20);
		tasks.add(tempTask1);
		
		Task tempTask2 = new Task();
		tempTask2.setDuration(30);
		tasks.add(tempTask2);
		
		assertAverageDuration(25);
	}
}
