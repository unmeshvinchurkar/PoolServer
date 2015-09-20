package com.pool.esapi;

public class FieldValidationException extends RuntimeException {

	public FieldValidationException() {
		super();
	}

	public FieldValidationException(String fieldName, Throwable cause) {
		super("Data for Field:" + fieldName + " is invalid", cause);
	}

	public FieldValidationException(String fieldName) {
		super("Data for Field:" + fieldName + " is invalid");
	}

	public FieldValidationException(Throwable cause) {
		super(cause);
	}

}
