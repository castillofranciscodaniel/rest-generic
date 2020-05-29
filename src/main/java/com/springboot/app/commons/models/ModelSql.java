package com.springboot.app.commons.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@MappedSuperclass
public abstract class ModelSql<T> extends Model<T> {

	public ModelSql() {
	}

	public ModelSql(T id) {
		super(id);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Override
	public T getId() {
		return super.getId();
	}

}
