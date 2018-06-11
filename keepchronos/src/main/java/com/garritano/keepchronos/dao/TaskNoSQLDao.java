package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.TransactionManager;

import com.garritano.keepchronos.model.ProjectNoSQL;
import com.garritano.keepchronos.model.Task;
import com.garritano.keepchronos.model.TaskNoSQL;

public class TaskNoSQLDao {
	
	protected EntityManager entityManager;
	private TransactionManager transactionManager;
	
	public TaskNoSQLDao(EntityManager entityManager) {
		this.entityManager = entityManager;
		//accessing JBoss's Transaction can be done differently but this one works nicely
		transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
	}
	
	public void save(TaskNoSQL task) throws Exception{
		transactionManager.begin();
		entityManager.persist(task);
		transactionManager.commit();
	}
	public List<TaskNoSQL> getAll() throws Exception{
		transactionManager.begin();
		List<TaskNoSQL> result = entityManager.createQuery("select p from TaskNoSQL p", TaskNoSQL.class).getResultList();
		transactionManager.commit();
		
		return result;
	}
/*	public List<TaskNoSQL> getAll() {
		return entityManager.createQuery("select p from Task p", TaskNoSQL.class).getResultList();
	}*/
	
	public TaskNoSQL findById(Long id) {
		return entityManager.find(TaskNoSQL.class, id);
	}
	
	public void update(TaskNoSQL task) throws Exception{
		transactionManager.begin();
		entityManager.merge(task);
		transactionManager.commit();
	}
	
	public ProjectNoSQL getProjectByTaskId(Long id) {
		TaskNoSQL tempTask = entityManager.find(TaskNoSQL.class, id);
		if (tempTask!=null) {
			return tempTask.getProject();
		}
		return null;
	}
}
