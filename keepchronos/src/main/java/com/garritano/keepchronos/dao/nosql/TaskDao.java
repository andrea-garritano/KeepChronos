package com.garritano.keepchronos.dao.nosql;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import com.garritano.keepchronos.model.nosql.Project;
import com.garritano.keepchronos.model.nosql.Task;

public class TaskDao {

	private EntityManager entityManager;
	private TransactionManager transactionManager;

	public TaskDao(EntityManager entityManager) {
		this.entityManager = entityManager;
		// accessing JBoss's Transaction can be done differently but this one works
		// nicely
		transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();
	}

	public void save(Task task) throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactionManager.begin();
		entityManager.persist(task);
		transactionManager.commit();
	}

	public List<Task> getAll() throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactionManager.begin();
		List<Task> result = entityManager.createQuery("select p from Task p", Task.class)
				.getResultList();
		transactionManager.commit();

		return result;
	}

	public Task findById(Long id) {
		return entityManager.find(Task.class, id);
	}

	public void update(Task task) throws NotSupportedException, SystemException, RollbackException,
			HeuristicMixedException, HeuristicRollbackException {
		transactionManager.begin();
		entityManager.merge(task);
		transactionManager.commit();
	}

	public Project getProjectByTaskId(Long id) {
		Task tempTask = entityManager.find(Task.class, id);
		if (tempTask != null) {
			return tempTask.getProject();
		}
		return null;
	}
}
