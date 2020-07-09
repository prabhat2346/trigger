package com.nec.edgedisplay.exception;

public class PlayerNotFoundException extends BaseException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3752756692534647182L;

	public PlayerNotFoundException(String message,String playerId) {
		super(message,playerId);
		
	}
	
	public PlayerNotFoundException(String message) {
		super(message);
		
	}

}
