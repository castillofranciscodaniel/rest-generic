package com.generic.service.sql.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
public abstract class Model<ID> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;

	public Model() {
	}

	public Model(ID id) {
		this.id = id;
	}

}
