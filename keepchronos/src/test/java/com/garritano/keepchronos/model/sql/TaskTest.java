package com.garritano.keepchronos.model.sql;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TaskTest {
	
	private Task task1;
	private Task task2;
	
	@Before
	public void setUp() {
		task1 = new Task();
		task2 = new Task();
	}
	
	@Test
	public void testSameObject() {
		assertTrue(task1.equals(task1));
	}
	
	@Test
	public void testNullObject() {
		assertFalse(task1.equals(null));
	}
	
	@Test
	public void testEqualsNotSettedAttributes() {
		assertTrue(task1.equals(task2));
		assertEquals(task1.hashCode(), task2.hashCode());
	}
	
	@Test
	public void testDifferentClass() {
		assertFalse(task1.equals(new Object()));
	}
	
	@Test
	public void testDifferentDuration() {
		task1.setDuration(20);
		task2.setDuration(30);
		
		assertFalse(task1.equals(task2));
	}
	
	@Test
	public void testFirstProjectNullSecondFilled() {
		task1.setProject(null);
		Project project = new Project();
		task2.setProject(project);
		assertFalse(task1.equals(task2));
	}
	
	@Test
	public void testDifferentProject() {
		Project project1 = new Project();
		project1.setTitle("This is my title n1");
		task1.setProject(project1);
		
		Project project2 = new Project();
		project2.setTitle("and this is my title n2");
		task1.setProject(project2);
		
		assertFalse(task1.equals(task2));
	}
	
	@Test
	public void testBusinessKeyEquivalent() {
		Project project = new Project();
		task1.setTitle("this is my title");
		task1.setDescription("and this is my description");
		task1.setDuration(30);
		task1.setProject(project);
		
		task2.setTitle("this is my title");
		task2.setDescription("and this is my description");
		task2.setDuration(30);
		task2.setProject(project);
		
		assertEquals(task1, task2);
		assertEquals(task1.hashCode(), task2.hashCode());
	}
	

	@After
	public void tearDown() {
		task1 = null;
		task2 = null;
	}

}
