package com.nec.edgedisplay.exception;

public class MessageNotFoundException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1908870826312052328L;

	public MessageNotFoundException(String message,String msgId) {
		super(message,msgId);
		
	}

}
