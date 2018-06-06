package com.garritano.keepchronos.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
	private Project project;
	
	@Before
	public void setUp() {
		project = new Project();
	}

	@Test
	public void testEqualsNullObject() {
		assertFalse(project.equals(null));
	}
	
	@Test
	public void testEqualsnotIstanceofProject() {
		assertFalse(project.equals(new Object()));
	}
	
	@Test
	public void testEqualsSameId() {
		Project projectSameId = new Project();
		project.setId((long) 0);
		projectSameId.setId(project.getId());
		
		assertTrue(project.equals(projectSameId));
	}

}
