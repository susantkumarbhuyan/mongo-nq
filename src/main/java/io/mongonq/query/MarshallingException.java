package io.mongonq.query;

public class MarshallingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MarshallingException(String message) {
		super(message);
	}

	public MarshallingException(String message, Throwable cause) {
		super(message, cause);
	}
}
