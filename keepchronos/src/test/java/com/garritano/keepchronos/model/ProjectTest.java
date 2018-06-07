package com.garritano.keepchronos.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest {
	
	private Project project1;
	private Project project2;
	
	@Before
	public void setUp() throws Exception {
		project1 = new Project();
		project1.setTitle("First project");
		project1.setDescription("This is my first project, hi!");
		project1.setId((long) 1);
		
		project2 = new Project();
		project2.setId((long) 1);
	}

	@Test
	public void testEqualsWithNull() {
		assertFalse(project1.equals(null));
	}
	
	@Test
	public void testEqualsWithOtherClass() {
		assertFalse(project1.equals(new Object()));
	}
	
	@Test
	public void testEqualsWithIdNull() {
		project2.setId(null);
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testEqualsWithDifferentId() {
		project2.setId((long) -1);
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testEqualsWithFirstIdNull() {
		project1.setId(null);
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testEqualsWithFirstNullSecondDifferent() {
		project1.setId(null);
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testHashCodeTrue() {
		assertTrue(project1.hashCode() == project2.hashCode());
	}
	
	@Test
	public void testHashCodeFalse() {
		project2.setId((long) 2);
		
		assertFalse(project1.hashCode() == project2.hashCode());
	}
	
	@Test
	public void testHashCodeWithNullId() {
		project1.setId(null);
		project2.setId(null);
		assertTrue(project1.hashCode() == project2.hashCode());
	}
	
	@After
	public void tearDown() {
		project1 = null;
		project2 = null;
	}
}
