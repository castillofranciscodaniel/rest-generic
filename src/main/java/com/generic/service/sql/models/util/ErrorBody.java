package com.generic.service.sql.models.util;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class ErrorBody {

	private String error;

	public ErrorBody() {
		super();
	}

	public ErrorBody(String error, Object... args) {
		super();
		this.error = String.format(error, args);
	}

	public ErrorBody setError(String error, Object... args) {
		this.error = String.format(error, args);
		return this;
	}

}
