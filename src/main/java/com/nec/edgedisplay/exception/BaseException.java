package com.nec.edgedisplay.exception;

public class BaseException extends Exception {

	/**
	 * 
	 */
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1642322380021621157L;

	private String message;
	private String id;

	public BaseException(final String message, final String id) {
		super();
		this.message = message;
		this.id = id;

	}

	public BaseException(final String message) {
		super();
		this.message = message;

	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BaseException [message=" + message + ", id=" + id + "]";
	}

}