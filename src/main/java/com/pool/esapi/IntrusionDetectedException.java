package com.pool.esapi;

public class IntrusionDetectedException extends RuntimeException {

	public IntrusionDetectedException() {
		super();
	}

	public IntrusionDetectedException(String fieldName, Throwable cause) {
		super("Intrusion detected on Field:" + fieldName , cause);
	}

	public IntrusionDetectedException(String fieldName) {
		super("Intrusion detected on Field:" + fieldName);
	}

	public IntrusionDetectedException(Throwable cause) {
		super("Intrusion detected on a field", cause);
	}

}
