package com.garritano.keepchronos.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Task extends BasicEntity{
	
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
