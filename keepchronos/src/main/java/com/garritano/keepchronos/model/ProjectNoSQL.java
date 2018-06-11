package com.garritano.keepchronos.model;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import org.hibernate.search.annotations.Indexed;


@Embeddable
@Indexed
public class ProjectNoSQL extends BasicEntity{
	private Long id;
	
	@Override
	@GeneratedValue
	public Long getId() {
		return id;
	}
}
