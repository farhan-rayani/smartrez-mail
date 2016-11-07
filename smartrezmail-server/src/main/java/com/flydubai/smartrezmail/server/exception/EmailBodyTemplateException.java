package com.flydubai.smartrezmail.server.exception;

/**
 * 
 * @author Amit.Lulla
 * Since 1.0 28 Jan 2015
 * 
 * Configuration Exception -- Extending default RuntimeException.
 */
public class EmailBodyTemplateException extends RuntimeException{

	private static final long serialVersionUID = 1187501360660594679L;

	public EmailBodyTemplateException(String message, Exception cause) {
		super(message, cause);
	}
}
