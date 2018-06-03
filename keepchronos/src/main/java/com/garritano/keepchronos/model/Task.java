package com.garritano.keepchronos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Task {
	

	private Long id;
	private String title;
	private String description;
	private long elapsed;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getElapsed() {
		return elapsed;
	}
	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}
}
