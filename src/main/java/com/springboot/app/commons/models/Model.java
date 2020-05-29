package com.springboot.app.commons.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class Model<ID> {

	private ID id;

	public Model() {
	}

	public Model(ID id) {
		this.id = id;
	}

}
