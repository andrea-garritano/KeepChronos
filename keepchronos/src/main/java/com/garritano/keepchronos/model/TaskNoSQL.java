package com.garritano.keepchronos.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
public class TaskNoSQL extends BasicEntity{
	
	private ProjectNoSQL project;
	private int duration;
	
	@ManyToOne
	public ProjectNoSQL getProject() {
		return project;
	}

	public void setProject(ProjectNoSQL project) {
		this.project = project;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
}
