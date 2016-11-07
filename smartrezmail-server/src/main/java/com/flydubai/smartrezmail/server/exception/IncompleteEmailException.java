package com.flydubai.smartrezmail.server.exception;

/**
 * 
 * @author Amit.Lulla
 *
 * Incomplete Email Exception - In case Required fields are not provided e.g. To/From Address
 */
public class IncompleteEmailException extends RuntimeException {

	private static final long serialVersionUID = -5356669884453957632L;

	public IncompleteEmailException(String message) {
		super(message);
	}

}
