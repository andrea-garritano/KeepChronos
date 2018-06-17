package com.garritano.keepchronos.business;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.dao.ProjectDaoInterface;

public class StatisticTest {
	private Statistic statistic;
	private ProjectDaoInterface projectDaoInterface;

	@Before
	public void setUp() throws Exception {
		statistic = new Statistic(projectDaoInterface);
	}

	private void assertNumberOfProject(int expected) {
		int numberOfProject = statistic.getNumberOfProject();
		assertEquals(expected, numberOfProject);
	}
	
	@Test
	public void testNoProject() {
		assertNumberOfProject(0);
	}

	@After
	public void tearDown() throws Exception {
	}
}
