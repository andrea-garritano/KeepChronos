package com.garritano.keepchronos.model.nosql;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;

import org.hibernate.search.annotations.Indexed;

import com.garritano.keepchronos.model.BasicEntity;

@Embeddable
@Indexed
public class Project extends BasicEntity {

	private Long id;

	@Override
	@GeneratedValue
	public Long getId() {
		return id;
	}
}
