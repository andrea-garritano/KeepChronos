package com.garritano.keepchronos.model.sql;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.garritano.keepchronos.model.BasicEntity;

@Entity
public class Task extends BasicEntity {

	private Project project;
	private int duration;

	@ManyToOne
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
