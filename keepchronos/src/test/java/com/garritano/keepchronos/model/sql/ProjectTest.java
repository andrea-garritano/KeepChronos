package com.garritano.keepchronos.model.sql;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.garritano.keepchronos.model.Project;

public class ProjectTest {
	
	private Project project1;
	private Project project2;

	@Before
	public void setUp() {
		project1 = new Project();
		project2 = new Project();
	}
	
	@Test
	public void testSameObject() {
		assertTrue(project1.equals(project1));
	}
	
	@Test
	public void testNullObject() {
		assertFalse(project1.equals(null));
	}
	
	@Test
	public void testDifferentClass() {
		assertFalse(project1.equals(new Object()));
	}
	
	@Test
	public void testEqualsNotSettedAttributes() {
		assertTrue(project1.equals(project2));
		assertEquals(project1.hashCode(), project2.hashCode());
	}
	
	@Test
	public void testFirstDescriptionNullSecondDescriptionFilled() {
		project2.setDescription("test description");
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testDiffetentDescription() {
		project1.setDescription("description n1");
		project2.setDescription("description n2");
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testFirstTitleNullSecondTitleFilled() {
		project2.setTitle("test title");
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testDiffetentTitle() {
		project1.setTitle("title n1");
		project2.setTitle("title n2");
		
		assertFalse(project1.equals(project2));
	}
	
	@Test
	public void testBusinessKeyEquivalent() {
		project1.setTitle("this is my title");
		project2.setTitle("this is my title");
		
		project1.setDescription("and this is my description");
		project2.setDescription("and this is my description");
		
		assertEquals(project1, project2);
		assertEquals(project1.hashCode(), project2.hashCode());
	}

	@After
	public void tearDown() {
		project1 = null;
		project2 = null;
	}

}
