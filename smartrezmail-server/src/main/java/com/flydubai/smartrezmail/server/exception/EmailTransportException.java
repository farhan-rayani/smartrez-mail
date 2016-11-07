package com.flydubai.smartrezmail.server.exception;

/**
 * 
 * @author Amit.Lulla
 * Since 1.0 28 Jan 2015
 * 
 * Email Transport Exception -- Extending default RuntimeException.
 */
public class EmailTransportException extends RuntimeException {
	
	private static final long serialVersionUID = -4571219582577718425L;

	public EmailTransportException(String message, Exception cause) {
		super(message, cause);
	}
}
