package com.springboot.app.commons.models.util;

public class OkBody {

	private String message;

	public OkBody() {
	}

	public OkBody(String message, Object... args) {
		this.message = String.format(message, args);
	}

	public OkBody setMessage(String message, Object... args) {
		this.message = String.format(message, args);
		return this;
	}

	public String getMessage() {
		return this.message;
	}
}
