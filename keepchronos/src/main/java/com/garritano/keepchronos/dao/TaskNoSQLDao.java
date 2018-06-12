package com.garritano.keepchronos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.garritano.keepchronos.model.ProjectNoSQL;
import com.garritano.keepchronos.model.TaskNoSQL;

public class TaskNoSQLDao {
	
	private EntityManager entityManager;
	private TransactionManager transactionManager;
	
	public TaskNoSQLDao(EntityManager entityManager) {
		this.entityManager = entityManager;
		//accessing JBoss's Transaction can be done differently but this one works nicely
		transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
	}
	
	public void save(TaskNoSQL task) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		transactionManager.begin();
		entityManager.persist(task);
		transactionManager.commit();
	}
	
	public List<TaskNoSQL> getAll() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
		transactionManager.begin();
		List<TaskNoSQL> result = entityManager.createQuery("select p from TaskNoSQL p", TaskNoSQL.class).getResultList();
		transactionManager.commit();
		
		return result;
	}
	
	public TaskNoSQL findById(Long id) {
		return entityManager.find(TaskNoSQL.class, id);
	}
	
	public void update(TaskNoSQL task) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
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
