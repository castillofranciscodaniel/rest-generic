package com.springboot.app.commons.models;

import org.springframework.data.annotation.Id;

import lombok.ToString;

@ToString
public abstract class ModelNoSql<T> extends Model<T> {

	public ModelNoSql() {
	}

	public ModelNoSql(T id) {
		super(id);
	}
	
	@Override
	@Id
	public T getId() {
		return super.getId();
	}
		
}
