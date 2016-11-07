package com.flydubai.smartrezmail.server.exception;

/**
 * 
 * @author Amit.Lulla
 *
 * Runtime Exception when an Email Address is Invalid - Validated using javax.mail API
 */
public class InvalidEmailAddressException extends RuntimeException {

	private static final long serialVersionUID = 7521502257697884074L;

	public InvalidEmailAddressException(String message) {
		super(message);
	}
}
